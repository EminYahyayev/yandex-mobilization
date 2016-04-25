package com.ewintory.yandex.mobilization.utils;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import com.github.florent37.glidepalette.BitmapPalette;

public final class ArtistsPaletteBuilderInterceptor
        implements BitmapPalette.PaletteBuilderInterceptor {

    private static final int ARTISTS_MAXIMUM_COLOR_COUNT = 24;

    @NonNull
    @Override
    public Palette.Builder intercept(Palette.Builder builder) {
        return builder.maximumColorCount(ARTISTS_MAXIMUM_COLOR_COUNT);
    }
}
