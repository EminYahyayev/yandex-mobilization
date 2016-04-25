package com.ewintory.yandex.mobilization.data;

import com.ewintory.yandex.mobilization.model.Artist;

import java.util.List;
import java.util.Set;

import rx.Observable;

public interface ArtistsRepository {

    Observable<Set<String>> genres();

    Observable<List<Artist>> artists();

}
