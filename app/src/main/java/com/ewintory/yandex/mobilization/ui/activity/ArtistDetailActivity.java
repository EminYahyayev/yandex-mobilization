package com.ewintory.yandex.mobilization.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
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

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_artist_detail);

        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        // prepare thumbnail request using a small cover image
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(this)
                .load(artist.getSmallCover());

        // Loads and displays the small cover retrieved by the given thumbnail request
        // if it finishes before the big cover request.
        Glide.with(this)
                .load(artist.getBigCover())
                .error(R.color.artist_image_error)
                .priority(Priority.HIGH)
                .thumbnail(thumbnailRequest)
                .into(mArtistCover);
    }
}
