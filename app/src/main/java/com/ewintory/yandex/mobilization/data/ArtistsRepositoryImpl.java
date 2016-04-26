package com.ewintory.yandex.mobilization.data;

import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
        return mYandexApi.artists()
                .map(result -> {
                    if (!result.isError()) {
                        Response<List<Artist>> response = result.response();
                        if (response.isSuccessful()) {
                            return response.body();
                        } else {
                            Timber.w("Failed to load artists, code: %d", response.code());
                        }
                    } else {
                        Timber.w(result.error(), result.error().getMessage());
                    }

                    return Collections.<Artist>emptyList();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
