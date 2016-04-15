package com.ewintory.yandex.mobilization;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;


public final class YandexApplication extends Application {

    private RefWatcher refWatcher;

    public static YandexApplication get(Context context) {
        return (YandexApplication) context.getApplicationContext();
    }

    @Override
    public final void onCreate() {
        super.onCreate();

        refWatcher = BuildConfig.DEBUG
                ? LeakCanary.install(this)
                : RefWatcher.DISABLED;

        configureTimber();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private void configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                // Add the line number to the tag
                @Override protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) + ":" + element.getLineNumber();
                }
            });
        } else {
            // Release mode
            Timber.plant(new ReleaseTree());
        }
    }
}
