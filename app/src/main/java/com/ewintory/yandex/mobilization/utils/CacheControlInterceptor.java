package com.ewintory.yandex.mobilization.utils;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Offline cache support interceptor
 *
 * @see <a href="http://stackoverflow.com/a/34266019">SO answer</a>
 */
public class CacheControlInterceptor implements Interceptor {

    private final Context mAppContext;

    public CacheControlInterceptor(Context context) {
        mAppContext = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String cacheHeaderValue = NetworkUtils.isNetworkAvailable(mAppContext)
                ? "public, max-age=2419200"
                : "public, only-if-cached, max-stale=2419200";
        Request request = originalRequest.newBuilder().build();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheHeaderValue)
                .build();
    }
}
