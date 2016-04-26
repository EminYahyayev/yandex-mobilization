package com.ewintory.yandex.mobilization.network;

import com.ewintory.yandex.mobilization.model.Artist;

import java.util.List;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

public interface YandexApi {

    String API_BASE_URL = "http://download.cdn.yandex.net/mobilization-2016/";

    @Headers({"User-Agent: Yandex-Mobilization-App"})
    @GET("artists.json") Observable<Result<List<Artist>>> artists();

}
