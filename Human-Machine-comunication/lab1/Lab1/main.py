from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

pd.options.mode.chained_assignment = None


def main():
    fig, (line_plot, box_plot) = plt.subplots(constrained_layout=True, nrows=1, ncols=2)

    data_last_row = []
    box_plot_labels = []

    # plit
    # obliczanie drugiej osi
    def generation_to_effort(x):
        return x * 2.5 + 2.5

    def effort_to_generations(x):
        return (x - 2.5) / 2.5

    # funkcja rysowania wykresu/zweryfikuj obliczanei generacji
    # nie liczy 32 sprawdz
    def draw_the_plot(path, label_name, color_name, figure):
        # sprawdz jeszcze raz
        path1 = str(Path.cwd()) + path
        # odczytanie pliku
        df = pd.read_csv(path1)
        df2 = df.loc[:, df.columns != 'generation']
        df2 = df2.loc[:, df2.columns != 'effort']
        # obliczanie dla 32
        dataframe_mean_values = df2.mean(axis=1)
        new_df = df[['effort', 'generation']]
        # kolumny
        new_df['mean'] = dataframe_mean_values
        # rysowanie wykresu, ustawienie markera, coloru zmiana na procenty ze średniej
        line_plot.plot(new_df['effort'] / 1000, new_df['mean'] * 100, figure, ls='-', ms=7, markevery=25,
                       label=label_name,
                       color=color_name, fillstyle='full', markeredgecolor='black')
        # legenda
        line_plot.legend(numpoints=2, loc='lower right')
        data_last_row.append(np.array(df2)[199] * 100)
        box_plot_labels.append(label_name)

    # parametry funkcji
    draw_the_plot(r'\resources\1ers.csv', '1-Evol-RS', 'blue', 'o')
    draw_the_plot(r'\resources\1crs.csv', '1-Coev-RS', 'green', 'v')
    draw_the_plot(r'\resources\2crs.csv', '2-Coev-RS', 'red', 'D')
    draw_the_plot(r'\resources\1c.csv', '1-Coev', 'black', 's')
    draw_the_plot(r'\resources\2c.csv', '2-Coev', '#FF33FF', 'd')

    # ustawienie linii i osi
    # linei nie idą tak jak mają/sprawdz
    line_plot.set_xlabel('Rozegranych gier (x1000)')
    line_plot.set_ylim(top=100, bottom=60)
    line_plot.set_ylabel('Odsetek wygranych gier [%]')
    # druga oś
    secax = line_plot.secondary_xaxis('top', functions=(effort_to_generations, generation_to_effort))
    # dodanie nazwy
    secax.set_xlabel('Pokolenie')

    # kolor
    flierprops = dict(marker='+', markerfacecolor='b', markersize=7,
                      linestyle='none', markeredgecolor='b')

    bp = box_plot.boxplot(data_last_row, patch_artist=False,
                          notch='True', flierprops=flierprops, showmeans=True,
                          meanprops={'marker': 'o', 'markerfacecolor': 'blue', 'markeredgecolor': 'black'})

    # wartość osi Y i ustawienei po prawej
    box_plot.set_ylim(top=100, bottom=60)
    box_plot.set_xticklabels(box_plot_labels, rotation=30)
    box_plot.yaxis.tick_right()

    # mediana
    for median in bp['medians']:
        median.set(color='red',
                   linewidth=2)
        # box
    for box in bp['boxes']:
        box.set(color='blue', linewidth=1)

    for whisker in bp['whiskers']:
        whisker.set(color='blue',
                    linewidth=1,
                    linestyle="--")

    # grid
    box_plot.grid(linestyle='dotted')
    line_plot.grid(linestyle='dotted')

    # zapisanie
    plt.savefig('laboratorium1.pdf')
    plt.close()


if __name__ == '__main__':
    main()
