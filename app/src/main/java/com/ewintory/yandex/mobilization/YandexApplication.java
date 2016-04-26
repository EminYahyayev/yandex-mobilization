package com.ewintory.yandex.mobilization;

import android.app.Application;
import android.content.Context;

import com.ewintory.yandex.mobilization.data.DaggerDataComponent;
import com.ewintory.yandex.mobilization.data.DataComponent;
import com.ewintory.yandex.mobilization.data.DataModule;
import com.ewintory.yandex.mobilization.network.DaggerNetworkComponent;
import com.ewintory.yandex.mobilization.network.NetworkComponent;
import com.ewintory.yandex.mobilization.network.NetworkModule;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;


public final class YandexApplication extends Application {

    private NetworkComponent mNetworkComponent;
    private DataComponent mDataComponent;
    private RefWatcher mRefWatcher;

    public static YandexApplication get(Context context) {
        return (YandexApplication) context.getApplicationContext();
    }

    @Override
    public final void onCreate() {
        super.onCreate();

        mRefWatcher = BuildConfig.DEBUG
//                ? LeakCanary.install(this)
                ? RefWatcher.DISABLED
                : RefWatcher.DISABLED;

        configureTimber();

        setupDagger();
    }

    public DataComponent getDataComponent() {
        return mDataComponent;
    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void setupDagger() {
        final AppModule appModule = new AppModule(this);
        final NetworkModule networkModule = new NetworkModule();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .appModule(appModule)
                .networkModule(networkModule)
                .build();

        mDataComponent = DaggerDataComponent.builder()
                .appModule(appModule)
                .networkModule(networkModule)
                .dataModule(new DataModule())
                .build();
    }

    private void configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                // Adds the line number to the tag
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
