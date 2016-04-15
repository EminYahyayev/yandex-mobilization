package com.ewintory.yandex.mobilization.network;

import android.content.Context;

import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.deserializer.ArtistDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiFactory {

    public static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    public static final String API_BASE_URL = "http://download.cdn.yandex.net/mobilization-2016/";

    private static OkHttpClient sClient;
    private static Retrofit sRetrofit;

    public static <S> S createApi(Context context, Class<S> apiClass) {
        if (sRetrofit == null) {
            sRetrofit = createRetrofit(context);
        }
        return sRetrofit.create(apiClass);
    }

    public static OkHttpClient getClient(Context context) {
        if (sClient == null) {
            sClient = createOkHttpClient(context);
        }
        return sClient;
    }

    private static Retrofit createRetrofit(Context context) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .client(getClient(context))
                        .addConverterFactory(GsonConverterFactory.create(createGson()));
        return builder.build();
    }

    private static OkHttpClient createOkHttpClient(Context context) {
        Context appContext = context.getApplicationContext();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(appContext.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(10, TimeUnit.SECONDS);
        clientBuilder.cache(cache);
        clientBuilder.addInterceptor(logging);

        return clientBuilder.build();
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Artist.class, new ArtistDeserializer());
        return gsonBuilder.create();
    }

    private ApiFactory() {
        throw new AssertionError("No instances.");
    }
}