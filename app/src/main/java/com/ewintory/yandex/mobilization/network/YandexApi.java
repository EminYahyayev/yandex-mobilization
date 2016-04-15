package com.ewintory.yandex.mobilization.network;

import com.ewintory.yandex.mobilization.model.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface YandexApi {

    @Headers({
            "Cache-Control: max-age=640000, max-stale=0",
            "User-Agent: Yandex-Mobilization-App"
    })
    @GET("artists.json") Call<List<Artist>> artists();

}
