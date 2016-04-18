package com.ewintory.yandex.mobilization.utils;

import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;

import com.github.florent37.glidepalette.BitmapPalette;

public final class ArtistsPaletteBuilderInterceptor
        implements BitmapPalette.PaletteBuilderInterceptor {
    @NonNull
    @Override
    public Palette.Builder intercept(Palette.Builder builder) {
        return builder.maximumColorCount(24);
    }
}
