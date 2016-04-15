package com.ewintory.yandex.mobilization.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.network.ApiFactory;
import com.ewintory.yandex.mobilization.network.YandexApi;
import com.ewintory.yandex.mobilization.ui.adapter.ArtistsAdapter;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A fragment displaying the list of artists
 */
public final class ArtistsFragment extends BaseFragment
        implements Callback<List<Artist>>, ArtistsAdapter.OnArtistClickListener {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ContentLoadingProgressBar mProgressBar;
    @Bind(R.id.error_view) TextView mErrorView;

    private ArtistsAdapter mArtistsAdapter;

    private YandexApi mYandexApi;
    private Call<List<Artist>> mArtistsCall;

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mYandexApi = ApiFactory.createApi(getContext(), YandexApi.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);

        mArtistsAdapter = new ArtistsAdapter(this);
        mArtistsAdapter.setListener(this);

        final int spanCount = getResources().getInteger(R.integer.artists_columns);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override public int getSpanSize(int position) {
                // return position % 9 == 4 ? spanCount : 1;
                return 1;
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mArtistsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mArtistsCall = mYandexApi.artists();
        mArtistsCall.enqueue(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mArtistsCall != null)
            mArtistsCall.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mArtistsAdapter.setListener(DUMMY);
    }

    @Override
    public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
        if (mProgressBar != null)
            mProgressBar.hide();

        if (mErrorView != null)
            mErrorView.setVisibility(response.isSuccessful() ? View.GONE : View.VISIBLE);

        if (response.isSuccessful()) {
            mArtistsAdapter.setArtists(response.body());
        } else {
            mArtistsAdapter.setArtists(null);
            Timber.e("Artists call wasn't successful");
        }
    }

    @Override
    public void onFailure(Call<List<Artist>> call, Throwable t) {
        if (mProgressBar != null)
            mProgressBar.hide();
        mArtistsAdapter.setArtists(null);
        if (mErrorView != null)
            mErrorView.setVisibility(View.VISIBLE);
        Timber.e(t.getMessage());
    }

    @Override
    public void onArtistClick(Artist artist) {
        Timber.d("onArtistClick: name=%s", artist.getName());
    }
}
