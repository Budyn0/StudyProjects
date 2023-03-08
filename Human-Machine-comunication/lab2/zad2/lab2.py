''''
Linki i komentarze są dla mojej potrzeby.
'''

import matplotlib.pyplot as plt
from matplotlib import rc
import numpy as np
#https://rasterio.readthedocs.io/en/latest/
import rasterio as rasterio


def plot_map():
    # https://stackoverflow.com/questions/7618858/how-to-to-read-a-matrix-from-a-given-file
    with open('big.dem') as matrix:
        # Read Width, height, distance - elevation = height, width data
        width, height, distance = map(int, matrix.readline().split())
        elevation = np.genfromtxt(matrix, skip_header=1)

    convert = rasterio.Affine.translation(0, height) * rasterio.Affine.scale(distance, -distance)

    # https://gis.stackexchange.com/questions/279953/numpy-array-to-gtiff-using-rasterio-without-source-raster
    with rasterio.open('solution.tif', 'w', driver='GTiff', height=height, width=width, count=1, dtype=elevation.dtype,
                       convert=convert) as dst: dst.write(elevation, 1)

    # map,max,low
    with rasterio.open('solution.tif') as src:
        elevation = src.read(1)

        low = elevation.min()
        high = elevation.max()

        # https://matplotlib.org/stable/tutorials/colors/colormaps.html colors
        cmap = plt.cm.get_cmap('RdYlGn_r', 256)
        cmap.set_under('green')
        cmap.set_over('red')
        norm = plt.Normalize(vmin=low, vmax=high)

        extent = [0, width * distance, 0, height * distance]
        plt.imshow(elevation, cmap=cmap, norm=norm, extent=extent, interpolation='bilinear')
        plt.xlabel('x')
        plt.ylabel('y')
        plt.title('mapa')
        plt.colorbar()
        plt.show()


if __name__ == '__main__':
    plot_map()
