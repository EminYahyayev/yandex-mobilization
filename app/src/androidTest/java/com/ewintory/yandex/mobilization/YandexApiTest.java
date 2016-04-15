package com.ewintory.yandex.mobilization;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.ApiFactory;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.ewintory.yandex.mobilization.utils.CollectionUtils;

import java.util.List;

import retrofit2.Response;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class YandexApiTest extends ApplicationTestCase<Application> {
    public YandexApiTest() {
        super(Application.class);
    }

    public void testArtists() throws Exception {
        YandexApi api = ApiFactory.createApi(getContext(), YandexApi.class);

        Response<List<Artist>> response = api.artists().execute();

        if (!response.isSuccessful()) {
            fail("Response isn't successful. Code: " + response.code());
        }

        if (CollectionUtils.isEmpty(response.body())) {
            fail("Response body is empty.");
        }

        List<Artist> artists = response.body();
        System.out.println("Artists count=" + artists.size());
        System.out.println(artists.get(0));

        assertEquals(true, response.isSuccessful());
    }
}