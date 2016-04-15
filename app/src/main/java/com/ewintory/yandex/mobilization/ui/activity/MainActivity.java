package com.ewintory.yandex.mobilization.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ewintory.yandex.mobilization.R;
import com.ewintory.yandex.mobilization.model.Artist;
import com.ewintory.yandex.mobilization.ui.fragment.ArtistsFragment;

import butterknife.Bind;
import timber.log.Timber;

public final class MainActivity extends BaseActivity
        implements ArtistsFragment.Listener {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(mToolbar);
        setTitle(R.string.title_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_about)
                    .setMessage(R.string.about_message)
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArtistSelected(@NonNull Artist artist) {
        Timber.d("Artist selected: name=%s", artist.getName());
        Intent intent = new Intent(this, ArtistDetailActivity.class);
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST, artist);
        startActivity(intent);
    }
}
