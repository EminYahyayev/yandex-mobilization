package com.ewintory.yandex.mobilization.network;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ewintory.yandex.mobilization.BuildConfig;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.deserializer.ArtistDeserializer;
import com.ewintory.yandex.mobilization.utils.CacheControlInterceptor;
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
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class NetworkModule {

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int CONNECTION_TIMEOUT_SECONDS = 10;

    public NetworkModule() {}

    @Provides
    @Singleton SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Artist.class, new ArtistDeserializer());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton Cache provideOkHttpCache(Application application) {
        File cacheDir = new File(application.getCacheDir(), "http");
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }

    @Provides
    @Singleton OkHttpClient provideOkHttpClient(final Application application, Cache cache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ? Level.BASIC : Level.NONE);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        clientBuilder.readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        // Добавляем поддержку offline кэша
        clientBuilder.addInterceptor(new CacheControlInterceptor(application));
        clientBuilder.addNetworkInterceptor(new CacheControlInterceptor(application));
        clientBuilder.addInterceptor(logging);
        clientBuilder.cache(cache);
        return clientBuilder.build();
    }

    @Provides
    @Singleton Retrofit provideRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                // Добавляем поддержку RxJava
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(YandexApi.API_BASE_URL)
                .client(client)
                .build();
    }

    @Provides
    @Singleton YandexApi provideYandexApi(Retrofit retrofit) {
        return retrofit.create(YandexApi.class);
    }
}
