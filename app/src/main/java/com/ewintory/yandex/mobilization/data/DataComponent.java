package com.ewintory.yandex.mobilization.data;

import com.ewintory.yandex.mobilization.AppModule;
import com.ewintory.yandex.mobilization.network.NetworkModule;
import com.ewintory.yandex.mobilization.ui.fragment.ArtistsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DataModule.class})
public interface DataComponent {

    void inject(ArtistsFragment artistsFragment);

}
