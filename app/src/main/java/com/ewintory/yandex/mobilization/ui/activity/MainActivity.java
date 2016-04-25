package com.ewintory.yandex.mobilization.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.ui.fragment.ArtistsFragment;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public final class MainActivity extends BaseActivity
        implements ArtistsFragment.Listener {

    private static final int SEARCH_VIEW_THRESHOLD_MILLIS = 500;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Subscription mSearchViewSubscription = Subscriptions.empty();
    private BehaviorSubject<String> mSearchViewSubject;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        setTitle(R.string.title_main);

        mSearchViewSubject = BehaviorSubject.create("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(true);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        mSearchViewSubscription = RxSearchView.queryTextChanges(searchView)
                .doOnNext(query -> Timber.d("SearchView query: %s", query))
                .throttleWithTimeout(SEARCH_VIEW_THRESHOLD_MILLIS, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .map(CharSequence::toString)
                .doOnNext(query -> Timber.d("SearchView throttled query: %s", query))
                .subscribe(mSearchViewSubject);

        return true;
    }

    @Override
    protected void onPause() {
        mSearchViewSubscription.unsubscribe();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_about)
                    .setMessage(R.string.about_message)
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // prevents back press if search view is opened
        if (searchItem == null || !MenuItemCompat.collapseActionView(searchItem)) {
            super.onBackPressed();
        }
    }

    @Override
    public void onArtistSelected(@NonNull Artist artist) {
        Timber.d("Artist selected: name=%s", artist.getName());
        Intent intent = new Intent(this, ArtistDetailActivity.class);
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST, artist);
        startActivity(intent);
    }
}
