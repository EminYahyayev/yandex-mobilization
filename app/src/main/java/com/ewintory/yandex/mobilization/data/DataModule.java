package com.ewintory.yandex.mobilization.data;

import com.ewintory.yandex.mobilization.network.YandexApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class DataModule {

    public DataModule() {}

    @Provides
    @Singleton ArtistsRepository provideArtistsRepository(YandexApi yandexApi) {
        return new ArtistsRepositoryImpl(yandexApi);
    }
}
