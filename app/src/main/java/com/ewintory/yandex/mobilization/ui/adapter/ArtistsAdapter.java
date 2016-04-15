package com.ewintory.yandex.mobilization.ui.adapter;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.utils.ArtistsPaletteBuilderInterceptor;
import com.ewintory.yandex.mobilization.utils.StringUtils;
import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

public final class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistHolder> {

    public interface OnArtistClickListener {
        void onArtistClick(Artist artist);

        OnArtistClickListener DUMMY = new OnArtistClickListener() {
            @Override public void onArtistClick(Artist artist) {/** dummy */}
        };
    }

    private final WeakReference<Fragment> mFragmentReference;
    private final LayoutInflater mInflater;
    private final StringBuilder mStringBuilder = new StringBuilder(30);

    @Nullable
    private List<Artist> mArtists;
    private OnArtistClickListener mListener = OnArtistClickListener.DUMMY;

    public ArtistsAdapter(Fragment fragment) {
        mInflater = LayoutInflater.from(fragment.getContext());
        mFragmentReference = new WeakReference<>(fragment);

        setHasStableIds(true);
    }

    public ArtistsAdapter setArtists(@Nullable List<Artist> artists) {
        mArtists = artists;
        notifyDataSetChanged();
        return this;
    }

    public ArtistsAdapter setListener(OnArtistClickListener listener) {
        mListener = listener;
        return this;
    }

    private Artist getArtist(int position) {
        if (mArtists == null)
            throw new IllegalArgumentException("mArtists==null, position=" + position);

        return mArtists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getArtist(position).getId();
    }

    @Override
    public int getItemCount() {
        return mArtists != null ? mArtists.size() : 0;
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArtistHolder(mInflater.inflate(R.layout.item_artist, parent, false));
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        final Artist artist = getArtist(position);

        holder.nameView.setText(artist.getName());
        holder.genresView.setText(StringUtils.join(artist.getGenres(), ", ", mStringBuilder));
        holder.tracksView.setText(String.valueOf(artist.getTracks()));
        holder.albumsView.setText(String.valueOf(artist.getAlbums()));

        // reset colors if an artist bounded to this holder is changed
        // prevents unnecessary color blinking
        if (holder.oldItemId != holder.getItemId()) {
            holder.resetColors();
        }

        final String coverUrl = artist.getCover(Artist.COVER_TYPE_SMALL);
        if (!mFragmentReference.isEnqueued()) {
            GlidePalette listener = GlidePalette.with(coverUrl)
                    .setPaletteBuilderInterceptor(new ArtistsPaletteBuilderInterceptor())
                    .intoCallBack(holder);

            Glide.with(mFragmentReference.get())
                    .load(coverUrl)
                    .crossFade(200)
                    .placeholder(R.color.artist_image_placeholder)
                    .error(R.color.artist_image_error)
                    .listener(listener)
                    .into(holder.imageView);
        }
    }

    final class ArtistHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, BitmapPalette.CallBack {

        @Bind(R.id.artist_item_image) ImageView imageView;
        @Bind(R.id.artist_item_name) TextView nameView;
        @Bind(R.id.artist_item_genres) TextView genresView;
        @Bind(R.id.artist_item_tracks) TextView tracksView;
        @Bind(R.id.artist_item_albums) TextView albumsView;

        @Bind(R.id.artist_item_footer) ViewGroup footerView;
        @Bind(R.id.artist_item_tracks_icon) ImageView tracksIconView;
        @Bind(R.id.artist_item_albums_icon) ImageView albumsIconView;

        @BindColor(R.color.artist_footer_background) int mColorBackground;
        @BindColor(R.color.body_text_white) int mColorTitle;
        @BindColor(R.color.body_text_1_inverse) int mColorSubtitle;

        long oldItemId = RecyclerView.NO_ID;

        public ArtistHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onArtistClick(getArtist(getAdapterPosition()));
        }

        @Override
        public void onPaletteLoaded(@Nullable Palette palette) {
            if (palette != null) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    applySwatch(swatch);
                }
            }
        }

        private void applySwatch(@NonNull Palette.Swatch swatch) {
            // using body and title colors as primary and secondary are looking better
            int primary = swatch.getBodyTextColor();
            int secondary = swatch.getBodyTextColor();
            int background = swatch.getRgb();

            footerView.setBackgroundColor(background);
            nameView.setTextColor(primary);
            genresView.setTextColor(secondary);
            tracksView.setTextColor(secondary);
            albumsView.setTextColor(secondary);
            tracksIconView.setColorFilter(secondary, PorterDuff.Mode.MULTIPLY);
            albumsIconView.setColorFilter(secondary, PorterDuff.Mode.MULTIPLY);
        }

        void resetColors() {
            footerView.setBackgroundColor(mColorBackground);
            nameView.setTextColor(mColorTitle);
            genresView.setTextColor(mColorSubtitle);
            tracksView.setTextColor(mColorSubtitle);
            albumsView.setTextColor(mColorSubtitle);
            tracksIconView.setColorFilter(mColorSubtitle, PorterDuff.Mode.MULTIPLY);
            albumsIconView.setColorFilter(mColorSubtitle, PorterDuff.Mode.MULTIPLY);
        }
    }
}
