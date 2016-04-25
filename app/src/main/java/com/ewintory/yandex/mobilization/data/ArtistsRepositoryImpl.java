package com.ewintory.yandex.mobilization.data;

import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.List;
import java.util.Set;

import rx.Observable;

final class ArtistsRepositoryImpl implements ArtistsRepository {

    private final YandexApi mYandexApi;
    private BehaviorRelay<Set<String>> mGenresRelay;

    public ArtistsRepositoryImpl(YandexApi api) {
        mYandexApi = api;
    }

    @Override
    public Observable<Set<String>> genres() {
        return mGenresRelay.asObservable();
    }

    @Override
    public Observable<List<Artist>> artists() {
        return mYandexApi.artists();
    }
}
