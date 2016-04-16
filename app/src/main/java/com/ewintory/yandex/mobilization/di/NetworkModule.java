package com.ewintory.yandex.mobilization.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.ewintory.yandex.mobilization.network.deserializer.ArtistDeserializer;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class NetworkModule {

    public static final String API_BASE_URL = "http://download.cdn.yandex.net/mobilization-2016/";

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private String mBaseUrl;

    public NetworkModule() {
        this.mBaseUrl = API_BASE_URL;
    }

    @Provides
    @Singleton SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton Cache provideOkHttpCache(Application application) {
        File cacheDir = new File(application.getCacheDir(), "http");
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }

    @Provides
    @Singleton Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // TODO: why needed?
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.registerTypeAdapter(Artist.class, new ArtistDeserializer());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        clientBuilder.readTimeout(10, TimeUnit.SECONDS);
        clientBuilder.cache(cache);
        clientBuilder.addInterceptor(logging);
        return clientBuilder.build();
    }

    @Provides
    @Singleton Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton YandexApi provideYandexApi(Retrofit retrofit) {
        return retrofit.create(YandexApi.class);
    }
}
