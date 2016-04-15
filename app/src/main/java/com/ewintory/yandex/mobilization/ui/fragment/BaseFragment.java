package com.ewintory.yandex.mobilization.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.ewintory.yandex.mobilization.YandexApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Base class for all fragments. Binds views and watches memory leaks
 *
 * @see ButterKnife
 * @see RefWatcher
 */
abstract class BaseFragment extends Fragment {

    private Toast mToast;

    @CallSuper
    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @CallSuper
    @Override public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @CallSuper
    @Override public void onDestroy() {
        super.onDestroy();
        YandexApplication.get(getActivity()).getRefWatcher().watch(this);
    }

    @SuppressWarnings("unused")
    protected final void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected final void showToast(String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, duration);
        mToast.show();
    }
}