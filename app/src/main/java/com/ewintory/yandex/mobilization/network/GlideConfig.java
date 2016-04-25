package com.ewintory.yandex.mobilization.network;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.ewintory.yandex.mobilization.YandexApplication;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;

public final class GlideConfig implements GlideModule {

    @Inject OkHttpClient mOkHttpClient;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // increase Glide's image quality
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        YandexApplication.get(context).getNetworkComponent().inject(this);

        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(mOkHttpClient));
    }
}