package com.ewintory.yandex.mobilization.ui.widget;

import android.support.annotation.CallSuper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;


public class DrawerToggleListener implements DrawerLayout.DrawerListener {
    private ActionBarDrawerToggle mDrawerToggle;

    public DrawerToggleListener(ActionBarDrawerToggle drawerToggle) {
        mDrawerToggle = drawerToggle;
    }

    @CallSuper
    @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @CallSuper
    @Override public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @CallSuper
    @Override public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @CallSuper
    @Override public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }
}
