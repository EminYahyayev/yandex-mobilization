package com.ewintory.yandex.mobilization.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.ewintory.yandex.mobilization.BuildConfig;
import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;

import butterknife.Bind;

public final class ArtistDetailActivity extends BaseActivity {

    public static final String EXTRA_ARTIST =
            BuildConfig.APPLICATION_ID + ".extras.ARTIST";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.artist_detail_cover) ImageView mArtistCover;
    @Bind(R.id.artist_detail_description_label) TextView mArtistDescriptionLabel;
    @Bind(R.id.artist_detail_description) TextView mArtistDescription;

    @Bind(R.id.artist_detail_link) TextView mArtistLink;
    @Bind(R.id.artist_detail_link_icon) ImageView mArtistLinkIcon;
    @Bind(R.id.artist_detail_link_container) ViewGroup mArtistLinkContainer;

    @Bind(R.id.artist_detail_albums) TextView mArtistAlbums;
    @Bind(R.id.artist_detail_albums_icon) ImageView mArtistAlbumsIcon;
    @Bind(R.id.artist_detail_albums_container) ViewGroup mArtistAlbumsContainer;

    @Bind(R.id.artist_detail_tracks) TextView mArtistTracks;
    @Bind(R.id.artist_detail_tracks_icon) ImageView mArtistTracksIcon;
    @Bind(R.id.artist_detail_tracks_container) ViewGroup mArtistTracksContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_artist_detail);

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

        final int albums = artist.getAlbums();
        mArtistAlbums.setText(getResources()
                .getQuantityString(R.plurals.artist_detail_albums, albums, albums));

        final int tracks = artist.getTracks();
        mArtistTracks.setText(getResources()
                .getQuantityString(R.plurals.artist_detail_tracks, tracks, tracks));

        // prepare thumbnail request using a small cover image
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load(artist.getSmallCover());

        // Loads and displays the small cover retrieved by the given thumbnail request
        // if it finishes before the big cover request.
        Glide.with(this)
                .load(artist.getBigCover())
                .error(R.color.artist_image_error)
                .thumbnail(thumbnailRequest)
                .into(mArtistCover);
    }
}
