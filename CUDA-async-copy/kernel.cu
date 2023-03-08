#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <chrono>

struct Data {
    int N, R, BS, K;
};

Data data[] = {
    //nasycenie N
       // {1536, 32, 32, 8},
       // {1536, 32, 32, 8},
      //  {1536, 16, 32, 8},
      //  {1536, 16, 32, 4},
       // {1536, 8, 32, 4},
       // {1536, 8, 32, 4},

      //  {832,  32, 32, 8},
       // {832,  32, 32, 8},
       // {832,  16, 32, 8},
       // {832,  16, 32, 4},
       // {832,  8, 32, 4},
       // {832,  8, 32, 4},

      //  {512,  32, 32, 8},
       // {512,  32, 32, 8},
       // {512,  16, 32, 8},
       // {512,  16, 32, 4},
       // {512,  8, 32, 4},
       // {512,  8, 32, 4},

       // {256, 32, 32, 8},
       // {256, 32, 32, 8},
       // {256, 16, 32, 8},
        //{256, 16, 32, 4},
       // {256, 8, 32, 4},
       // {256, 8, 32, 4},

        //{164, 32, 32, 8},
        //{164, 32, 32, 8},
       // {164, 16, 32, 8},
       // {164, 16, 32, 4},
       // {164, 8, 32, 4},
       // {164, 8, 32, 4},

       // {80,  32, 32, 8},
       // {80,  32, 32, 8},
       // {80,  16, 32, 8},
       // {80,  16, 32, 4},
       // {80,  8, 32, 4},
       // {80,  8, 32, 4},

        //nasycenie K
             {832,  32, 16, 32},
             {832,  32, 16, 28},
             {832,  32, 16, 24},
             {832,  32, 16, 22},
             {832,  32, 16, 20},
             {832,  32, 16, 18},
             {832,  32, 16, 16},
             {832,  32, 16, 12},
             {832,  32, 16, 10},
             {832,  32, 16, 8},
             {832,  32, 16, 6},
             {832,  32, 16, 4},
};


void calculateMatrixSeq(int N, int R, float* mat, float* out) {
    for (int i = R; i < N - R; i++) {
        for (int j = R; j < N - R; j++) {
            float sum = 0;
            for (int x = i - R; x <= i + R; x++) {
                for (int y = j - R; y <= j + R; y++) {
                    sum += mat[x * N + y];
                }
            }
            out[(i - R) * (N - 2 * R) + j - R] = sum;
        }
    }
}

void compareMatrices(const float* out1, const float* out2, const int N, const int R) {
    int out_size = N - 2 * R;

    for (int i = 0; i < out_size * out_size - 1; i++) {
        if ((out1[i] != out2[i])) {
            fprintf(stderr, "Error in [%d] %f != %f.\n", i, out2[i], out1[i]);
            exit(1);
        }
    }
}

__global__ void calculateMatrixGlobal(const float* mat, float* out, const int N, const int R, const int K)
{
    int out_size = N - 2 * R;
    int i = threadIdx.x + blockIdx.x * blockDim.x;
    int j = (threadIdx.y + blockIdx.y * blockDim.y) * K;

    for (int k = 0; k < K; k++)
    {
        float sum = 0;
        if (i < out_size && j + k < out_size) {
            for (int ry = -R; ry <= R; ry++) {
                for (int rx = -R; rx <= R; rx++) {
                    sum += mat[(j + k + R + ry) * N + (i + R + rx)];
                }
            }
            out[(j + k) * out_size + i] = sum;
        }
    }
}

__device__ inline int translateIndex(int index, int neededWidth, int xOffset, int yOffset, int N) {
    int mat_x = index / neededWidth;
    int mat_y = index % neededWidth;
    return (mat_y + xOffset) * N + (mat_x + yOffset);
}

__global__ void calculateMatrixShared(const float* mat, float* out, int N, int R, int K) {
    extern __shared__ float ref[];

    unsigned int i = ((blockIdx.x * blockDim.x) + threadIdx.x) + R;
    unsigned int j = ((blockIdx.y * blockDim.y * K) + threadIdx.y) + R;

    unsigned int neededWidth = N - 2 * R - blockIdx.x * blockDim.x >= blockDim.x ? blockDim.x + 2 * R : N - 2 * R - blockIdx.x * blockDim.x + 2 * R;
    if (N < (blockDim.x + 2 * R)) neededWidth = N;

    unsigned int neededHeight = N - 2 * R - blockIdx.y * blockDim.x >= blockDim.x ? blockDim.x + 2 * R : N - 2 * R - blockIdx.y * blockDim.x + 2 * R;
    if (N < (blockDim.x + 2 * R)) neededHeight = N;

    unsigned int neededSize = neededHeight * neededWidth;
    unsigned int threadNum = threadIdx.x * blockDim.x + threadIdx.y;
    for (int k = 0; k < K; k++) {
        for (unsigned int ii = threadNum; ii < neededSize; ii += blockDim.x * blockDim.x) {
            int threadInd = translateIndex(ii, neededWidth, blockIdx.x * blockDim.x, blockIdx.y * blockDim.y * K + k * blockDim.x, N);
            ref[ii] = mat[threadInd];
        }
        __syncthreads();

        unsigned int threadx = threadIdx.x;
        unsigned int thready = threadIdx.y;

        if (i < N - R && j < N - R) {
            float sum = 0;
            for (unsigned int x = threadx; x <= threadx + 2 * R; x++)
                for (unsigned int y = thready; y <= thready + 2 * R; y++)
                    sum += ref[y * neededWidth + x];

            out[(i - R) * (N - 2 * R) + (j - R)] = sum;
            j += blockDim.x;
        }
        __syncthreads();
    }
}

void randomizeArray(float* mat, int N) {
    srand(time(NULL));
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            mat[i * N + j] = (int)(rand() % 100);
        }
    }
}

void calculateMatrix(const int N, const int R, const int K, const int OUT_SIZE, const char* name, const float* mat, const float* out_seq, const dim3 threadsMatrix, const dim3 blocksMatrix, const int sharedMemSize) {
    float* copy_mat, * copy_out;
    cudaEvent_t start, stop;
    float time = 0;

    auto* out = (float*)malloc(OUT_SIZE * OUT_SIZE * sizeof(float));

    cudaMalloc((void**)&copy_mat, N * N * sizeof(float));
    cudaMalloc((void**)&copy_out, OUT_SIZE * OUT_SIZE * sizeof(float));
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaMemcpyAsync(copy_mat, mat, N * N * sizeof(float), cudaMemcpyHostToDevice);
    cudaEventRecord(start, nullptr);

    if (strcmp(name, "global") == 0) {
        calculateMatrixGlobal << <blocksMatrix, threadsMatrix >> > (copy_mat, copy_out, N, R, K);
    }
    else {
        calculateMatrixShared << <blocksMatrix, threadsMatrix, sharedMemSize >> > (copy_mat, copy_out, N, R, K);
    }

    cudaEventRecord(stop, nullptr);
    cudaMemcpyAsync(out, copy_out, OUT_SIZE * OUT_SIZE * sizeof(float), cudaMemcpyDeviceToHost);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&time, start, stop);
    cudaEventDestroy(start);
    cudaEventDestroy(stop);
    cudaFree(copy_mat);
    cudaFree(copy_out);
    compareMatrices(out_seq, out, N, R);
    free(out);

    printf("%f;", time);
}

int main() {
    for (auto& one : data) {
        int N = one.N;
        int R = one.R;
        int BS = one.BS;
        int K = one.K;
        int OUT_SIZE = one.N - one.R * 2;
        printf("%d;%d;%d;%d;", N, R, BS, K);

        auto* out = (float*)malloc(OUT_SIZE * OUT_SIZE * sizeof(float));

        dim3 threadsMatrix(BS, BS);
        dim3 blocksMatrix(ceil(OUT_SIZE / (float)BS), ceil(OUT_SIZE / (float)BS / K));
        int sharedMemSize = sizeof(float) * (BS + 2 * R) * (BS + 2 * R);

        auto* mat = (float*)malloc(N * N * sizeof(float));
        randomizeArray(mat, N);

        auto startSeq = std::chrono::high_resolution_clock::now();
        calculateMatrixSeq(N, R, (float*)mat, (float*)out);
        auto stopSeq = std::chrono::high_resolution_clock::now();
        auto timeSeq = std::chrono::duration_cast<std::chrono::microseconds>(stopSeq - startSeq);
        printf("%f;", timeSeq.count() / 1000.0f);

        calculateMatrix(N, R, K, OUT_SIZE, "global", mat, out, threadsMatrix, blocksMatrix, sharedMemSize);
        calculateMatrix(N, R, K, OUT_SIZE, "shared", mat, out, threadsMatrix, blocksMatrix, sharedMemSize);

        printf("\n");

        free(mat);
        free(out);
    }
    return 0;
}
