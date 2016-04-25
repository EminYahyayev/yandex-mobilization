package com.ewintory.yandex.mobilization.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.ewintory.yandex.mobilization.BuildConfig;
import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.utils.CollectionUtils;

import java.util.HashSet;

import butterknife.BindView;

public final class ArtistDetailActivity extends BaseActivity {

    public static final String EXTRA_ARTIST =
            BuildConfig.APPLICATION_ID + ".extras.ARTIST";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.scroll_view) NestedScrollView mScrollView;

    @BindView(R.id.artist_detail_content) ViewGroup mArtistDetailContent;

    @BindView(R.id.artist_detail_cover) ImageView mArtistCover;
    @BindView(R.id.artist_detail_description_label) TextView mArtistDescriptionLabel;
    @BindView(R.id.artist_detail_description) TextView mArtistDescription;

    @BindView(R.id.artist_detail_link) TextView mArtistLink;
    @BindView(R.id.artist_detail_link_icon) ImageView mArtistLinkIcon;
    @BindView(R.id.artist_detail_link_container) ViewGroup mArtistLinkContainer;

    @BindView(R.id.artist_detail_albums) TextView mArtistAlbums;
    @BindView(R.id.artist_detail_albums_icon) ImageView mArtistAlbumsIcon;
    @BindView(R.id.artist_detail_albums_container) ViewGroup mArtistAlbumsContainer;

    @BindView(R.id.artist_detail_tracks) TextView mArtistTracks;
    @BindView(R.id.artist_detail_tracks_icon) ImageView mArtistTracksIcon;
    @BindView(R.id.artist_detail_tracks_container) ViewGroup mArtistTracksContainer;

    @BindView(R.id.artist_detail_genres) ViewGroup mGenres;
    @BindView(R.id.artist_detail_genres_container) ViewGroup mGenresContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_artist_detail);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        final Artist artist = getIntent().getParcelableExtra(EXTRA_ARTIST);
        if (artist == null) {
            throw new IllegalArgumentException("Artist must not be null");
        }

        processArtist(artist);
    }

    private void processArtist(@NonNull Artist artist) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(artist.getName());

        mArtistDescription.setText(artist.getDescription());

        final String link = artist.getLink();
        mArtistLink.setText(link);
        mArtistLinkContainer.setVisibility(TextUtils.isEmpty(link) ? View.GONE : View.VISIBLE);
        mArtistLinkContainer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        });

        final int albums = artist.getAlbums();
        mArtistAlbums.setText(getResources()
                .getQuantityString(R.plurals.artist_detail_albums, albums, albums));

        final int tracks = artist.getTracks();
        mArtistTracks.setText(getResources()
                .getQuantityString(R.plurals.artist_detail_tracks, tracks, tracks));

        displayGenres(artist.getGenres());

        // prepare thumbnail request using a small cover image
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load(artist.getSmallCover());

        // Loads and displays the small cover retrieved by the given thumbnail request
        // if it finishes before the big cover request.
        final String coverUrl = artist.getBigCover();
        //noinspection unchecked
        Glide.with(this)
                .load(coverUrl)
                .error(R.color.artist_image_error)
                .thumbnail(thumbnailRequest)
                .into(mArtistCover);
    }

    private void displayGenres(HashSet<String> genres) {
        if (CollectionUtils.isEmpty(genres)) {
            mGenresContainer.setVisibility(View.GONE);
        } else {
            mGenresContainer.setVisibility(View.VISIBLE);
            mGenres.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            for (final String genre : genres) {
                TextView chipView = (TextView) inflater.inflate(
                        R.layout.partial_genre_chip, mGenres, false);
                chipView.setText(genre);
                chipView.setContentDescription(genre);
                // chipView.setOnClickListener(view -> showToast(genre + " clicked"));

                mGenres.addView(chipView);
            }
        }
    }
}
