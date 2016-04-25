package com.ewintory.yandex.mobilization.network;

import com.ewintory.yandex.mobilization.AppModule;
import com.ewintory.yandex.mobilization.ui.fragment.ArtistsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(GlideConfig glideConfig);

    void inject(ArtistsFragment artistsFragment);
}
