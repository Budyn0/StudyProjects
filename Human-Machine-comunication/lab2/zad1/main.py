#!/usr/bin/env python
# -*- coding: utf-8 -*-
from __future__ import division  # Division in Python 2.7

import matplotlib

matplotlib.use('Agg')  # So that we can render files without GUI
import matplotlib.pyplot as plt
from matplotlib import rc
import numpy as np
from math import floor

def plot_color_gradients(gradient, names):
    # For pretty latex fonts (commented out, because it does not work on some machines)
    # rc('text', usetex=True)
    # rc('font', family='serif', serif=['Times'], size=10)
    rc('legend', fontsize=10)

    column_width_pt = 400  # Show in latex using \the\linewidth
    pt_per_inch = 72
    size = column_width_pt / pt_per_inch

    fig, axes = plt.subplots(nrows=len(gradient), sharex=True, figsize=(size, 0.75 * size))
    fig.subplots_adjust(top=1.00, bottom=0.05, left=0.25, right=0.95)

    for ax, gradient, name in zip(axes, gradient, names):
        # Create image with two lines and draw gradient on it
        img = np.zeros((2, 1024, 3))
        for i, v in enumerate(np.linspace(0, 1, 1024)):
            img[:, i] = gradient(v)

        im = ax.imshow(img, aspect='auto')
        im.set_extent([0, 1, 0, 1])
        ax.yaxis.set_visible(False)

        pos = list(ax.get_position().bounds)
        x_text = pos[0] - 0.25
        y_text = pos[1] + pos[3] / 2.
        fig.text(x_text, y_text, name, va='center', ha='left', fontsize=10)

    fig.savefig('gradient.pdf')


def hsv2rgb(h, s, v):
    if s == 0:
        return (v, v, v)
    if s > 0:
        h_i = floor(h / 60)
        f = h / 60 - h_i
        p = v * (100 - s) / 10000
        q = v * (100 - (s * f)) / 10000
        t = v * (100 - (s * (1 - f))) / 10000

        v /= 100

        if h_i == 0:
            return v, t, p
        elif h_i == 1:
            return q, v, p
        elif h_i == 2:
            return p, v, t
        elif h_i == 3:
            return p, q, v
        elif h_i == 4:
            return t, p, v
        elif h_i == 5:
            return v, p, q


def gradient_rgb_bw(v):
    return v, v, v


def gradient_rgb_gbr(v):
    if v <= 0.5:
        return 0, 1 - 2 * v, 2 * v
    else:
        v = v - 0.5
        return 2 * v, 0, 1 - 2 * v


def gradient_rgb_gbr_full(v):
    if v <= 0.25:
        return 0, 1, 4 * v
    elif v <= 0.5:
        v = v - 0.25
        return 0, 1 - 4 * v, 1
    elif v <= 0.75:
        v = v - 0.5
        return 4 * v, 0, 1
    else:
        v = v - 0.75
        return 1, 0, 1 - 4 * v


def gradient_hsv_bw(v):
    return hsv2rgb(0, 0, v)


def gradient_hsv_gbr(v):
    h = 120 + 240 * v
    return hsv2rgb(h, 100, 100)


def gradient_hsv_unknown(v):
    h = 120 - 120 * v
    return hsv2rgb(h, 50, 100)


def gradient_hsv_custom(v):
    h = 360 * v
    s = 50 + 50 * v
    return hsv2rgb(h, s, 110)


def gradient_rgb_wb_custom(v):
    r = 0
    g = 0
    b = 0
    if v <= 0.15:
        r = 1
        g = 1 - 1/0.15 * v
        b = 1
    elif 0.15 < v <= 0.3:
        r = 1 - 1/0.15 * (v - 0.15)
        g = 0
        b = 1
    elif 0.3 < v <= 0.45:
        r = 0
        g = 1/0.15 * (v - 0.3)
        b = 1
    elif 0.45 < v <= 0.6:
        r = 0
        g = 1
        b = 1 - 1/0.15 * (v - 0.45)
    elif 0.6 < v <= 0.75:
        r = 1/0.15 * (v - 0.6)
        g = 1
        b = 0
    elif 0.75 < v <= 0.85:
        r = 1
        g = 1 - 1/0.1 * (v - 0.75)
        b = 0
    elif 0.85 < v <= 1:
        r = 1 - round(1/0.15 * (v - 0.85), 2)
        g = 0
        b = 0
    return r, g, b


if __name__ == '__main__':
    def toname(g):
        return g.__name__.replace('gradient_', '').replace('_', '-').upper()


    gradients = (gradient_rgb_bw, gradient_rgb_gbr, gradient_rgb_gbr_full, gradient_rgb_wb_custom,
                 gradient_hsv_bw, gradient_hsv_gbr, gradient_hsv_unknown, gradient_hsv_custom)

    plot_color_gradients(gradients, [toname(g) for g in gradients])
