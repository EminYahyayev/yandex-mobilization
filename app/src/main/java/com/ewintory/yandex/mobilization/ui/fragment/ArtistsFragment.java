package com.ewintory.yandex.mobilization.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
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

    public interface Listener {
        void onArtistSelected(@NonNull Artist artist);

        Listener DUMMY = new Listener() {
            @Override public void onArtistSelected(@NonNull Artist artist) {/** dummy */}
        };
    }

    private static final String STATE_ARTISTS = "state_artists";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.progress_bar) ContentLoadingProgressBar mProgressBar;
    @Bind(R.id.error_view) TextView mErrorView;

    private Listener mListener = Listener.DUMMY;
    private ArtistsAdapter mArtistsAdapter;
    private List<Artist> mArtists;

    private YandexApi mYandexApi;
    private Call<List<Artist>> mArtistsCall;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof Listener))
            throw new ClassCastException("Activity must implement Listener interface");

        mListener = (Listener) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        mYandexApi = ApiFactory.createApi(getContext(), YandexApi.class);

        if (savedState != null && savedState.containsKey(STATE_ARTISTS)) {
            mArtists = savedState.getParcelableArrayList(STATE_ARTISTS);
            updateContent();
        }
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
        if (mArtists == null) {
            mArtistsCall = mYandexApi.artists();
            mArtistsCall.enqueue(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mArtistsCall != null)
            mArtistsCall.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mArtists != null)
            outState.putParcelableArrayList(STATE_ARTISTS, new ArrayList<Parcelable>(mArtists));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mArtistsAdapter.setListener(DUMMY);
    }

    @Override
    public void onDetach() {
        mListener = Listener.DUMMY;
        super.onDetach();
    }

    @Override
    public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
        if (!response.isSuccessful()) {
            Timber.e("Artists call wasn't successful");
            mArtists = null;
        } else {
            mArtists = response.body();
        }
        updateContent();
    }

    @Override
    public void onFailure(Call<List<Artist>> call, Throwable t) {
        Timber.e(t.getMessage());
        mArtists = null;
        updateContent();
    }

    private void updateContent() {
        if (mProgressBar != null)
            mProgressBar.hide();

        if (mArtists != null) {
            if (mErrorView != null)
                mErrorView.setVisibility(View.GONE);
        } else {
            if (mErrorView != null)
                mErrorView.setVisibility(View.VISIBLE);
        }

        mArtistsAdapter.setArtists(mArtists);
    }

    @Override
    public void onArtistItemClick(@NonNull Artist artist) {
        mListener.onArtistSelected(artist);
    }
}
