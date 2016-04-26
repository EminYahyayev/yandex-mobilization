package com.ewintory.yandex.mobilization.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.YandexApplication;
import com.ewintory.yandex.mobilization.data.ArtistsRepository;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.ui.adapter.ArtistItemAnimator;
import com.ewintory.yandex.mobilization.ui.adapter.ArtistsAdapter;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

/**
 * A fragment displaying the list of artists
 */
public final class ArtistsFragment extends BaseFragment
        implements ArtistsAdapter.OnArtistClickListener {

    public interface Listener {
        void onArtistSelected(@NonNull Artist artist);

        Listener DUMMY = artist -> {/** dummy */};
    }

    private static final String STATE_ARTISTS = "state_artists";

    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.error_view) TextView mErrorView;
    @BindView(R.id.empty_view) TextView mEmptyView;

    @Inject ArtistsRepository mArtistsRepository;

    private Subscription mArtistsSubscription = Subscriptions.empty();
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private Observable<Void> mSwipeRefreshesObservable;

    private Listener mListener = Listener.DUMMY;
    private ArtistsAdapter mArtistsAdapter;
    private BehaviorRelay<List<Artist>> mArtistsRelay;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof Listener))
            throw new ClassCastException("Activity must implement Listener interface");

        mListener = (Listener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setHasOptionsMenu(true);
        YandexApplication.get(getContext()).getDataComponent().inject(this);

        mArtistsAdapter = new ArtistsAdapter(this);
        mArtistsAdapter.setListener(this);

        mArtistsRelay = BehaviorRelay.create();

        if (savedState != null && savedState.containsKey(STATE_ARTISTS)) {
            Timber.d("Artists was saved! Restoring them.");
            List<Artist> artists = savedState.getParcelableArrayList(STATE_ARTISTS);
            mArtistsRelay.call(artists);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        mSwipeRefreshesObservable = RxSwipeRefreshLayout.refreshes(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getIntArray(R.array.swipe_progress_colors));

        final int spanCount = getResources().getInteger(R.integer.artists_columns);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new ArtistItemAnimator(spanCount));
        mRecyclerView.setAdapter(mArtistsAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        subscribeArtists();
    }

    @Override
    public void onStop() {
        super.onStop();
        unsubscribeArtists();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mArtistsRelay.hasValue())
            outState.putParcelableArrayList(STATE_ARTISTS, new ArrayList<>(mArtistsRelay.getValue()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mArtistsAdapter.setListener(DUMMY);
        mSubscriptions.clear();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mListener = Listener.DUMMY;
        super.onDetach();
    }

    private void subscribeArtists() {
        mArtistsSubscription.unsubscribe();
        mArtistsSubscription = mSwipeRefreshesObservable
                .startWith(Observable.just(null))
                .doOnNext(ignore -> Timber.v("SwipeRefresh received."))
                .flatMap(ignore -> mArtistsRepository.artists())
                .doOnNext(list -> Timber.v("Artists loaded, size: %d", list.size()))
                .doOnNext(mArtistsAdapter)
                .doOnNext(artists -> mSwipeRefreshLayout.setRefreshing(false))
                .subscribe((artists1) -> {
                    updateContent(artists1);
                }, throwable -> {
                    updateContent(null);
                }, () -> Timber.i("Artists completed."));
    }

    private void unsubscribeArtists() {
        mArtistsSubscription.unsubscribe();
    }

    private void updateContent(List<Artist> artists) {
        if (mProgressBar != null)
            mProgressBar.hide();

        if (mErrorView != null)
            mErrorView.setVisibility(artists == null ? View.VISIBLE : View.GONE);

        if (mEmptyView != null)
            mEmptyView.setVisibility(
                    (artists != null && artists.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onArtistItemClick(@NonNull Artist artist, @NonNull ArtistsAdapter.ArtistHolder holder) {
        mListener.onArtistSelected(artist);
    }
}
