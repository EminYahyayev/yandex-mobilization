package com.ewintory.yandex.mobilization.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.YandexApplication;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.ewintory.yandex.mobilization.ui.adapter.ArtistItemAnimator;
import com.ewintory.yandex.mobilization.ui.adapter.ArtistsAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ContentLoadingProgressBar mProgressBar;
    @Bind(R.id.error_view) TextView mErrorView;
    @Bind(R.id.empty_view) TextView mEmptyView;

    @Inject YandexApi mYandexApi;

    private Subscription mArtistsSubscription = Subscriptions.empty();

    private Listener mListener = Listener.DUMMY;
    private ArtistsAdapter mArtistsAdapter;
    private List<Artist> mArtists;

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
        YandexApplication.get(getContext()).getNetworkComponent().inject(this);

        mArtistsAdapter = new ArtistsAdapter(this);
        mArtistsAdapter.setListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        final int spanCount = getResources().getInteger(R.integer.artists_columns);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new ArtistItemAnimator(spanCount));
        mRecyclerView.setAdapter(mArtistsAdapter);

        if (savedState != null && savedState.containsKey(STATE_ARTISTS)) {
            Timber.i("Artists was saved!");
            mArtists = savedState.getParcelableArrayList(STATE_ARTISTS);
            updateContent();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mArtists == null) {
            reloadArtists();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mArtistsSubscription.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mArtists != null)
            outState.putParcelableArrayList(STATE_ARTISTS, new ArrayList<Parcelable>(mArtists));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        mArtistsAdapter.setListener(DUMMY);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mListener = Listener.DUMMY;
        super.onDetach();
    }

    private void reloadArtists() {
        mArtistsSubscription.unsubscribe();
        mArtistsSubscription = mYandexApi.artists()
                .doOnNext(list -> Timber.v("Artists loaded, size: %d", list.size()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists -> {
                    mArtists = artists;
                    updateContent();
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException response = (HttpException) throwable;
                        int code = response.code();
                        Timber.e("Artists failed to load with code=%d", code);
                    }

                    mArtists = null;
                    updateContent();
                });
    }

    private void updateContent() {
        if (mProgressBar != null)
            mProgressBar.hide();

        if (mErrorView != null)
            mErrorView.setVisibility(mArtists == null ? View.VISIBLE : View.GONE);

        if (mEmptyView != null)
            mEmptyView.setVisibility(
                    (mArtists != null && mArtists.isEmpty()) ? View.VISIBLE : View.GONE);

        if (mArtistsAdapter != null)
            mArtistsAdapter.setArtists(mArtists);
    }

    @Override
    public void onArtistItemClick(@NonNull Artist artist, @NonNull ArtistsAdapter.ArtistHolder holder) {
        mListener.onArtistSelected(artist);
    }
}
