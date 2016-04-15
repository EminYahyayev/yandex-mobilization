package com.ewintory.yandex.mobilization.ui.activity;

import android.app.ProgressDialog;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ewintory.yandex.mobilization.YandexApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Base class for all activities. Binds views and watches memory leaks
 *
 * @see ButterKnife
 * @see RefWatcher
 */
abstract class BaseActivity extends AppCompatActivity {

    private Toast mToast;
    private ProgressDialog mProgressDialog;

    @CallSuper
    @Override protected void onDestroy() {
        super.onDestroy();
        YandexApplication.get(this).getRefWatcher().watch(this);
    }

    @CallSuper
    @Override public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @SuppressWarnings("unused")
    protected final void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected final void showToast(String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, duration);
        mToast.show();
    }

    @SuppressWarnings("unused")
    protected final void showProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @SuppressWarnings("unused")
    protected final void hideProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
