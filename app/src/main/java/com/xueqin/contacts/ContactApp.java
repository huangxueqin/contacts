package com.xueqin.contacts;

import android.app.Application;
import android.os.Build;

import com.xueqin.contacts.loader.SimpleImageLoader;
import com.xueqin.contacts.loader.UrlLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class ContactApp extends Application {

    private static final String SCHEME_ASSET = "asset://";

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    private void initImageLoader() {
        // set placeholder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SimpleImageLoader.getInstance()
                    .setPlaceholderDrawable(getDrawable(R.drawable.ic_launcher_foreground));
        } else {
            SimpleImageLoader.getInstance()
                    .setPlaceholderDrawable(getResources()
                            .getDrawable(R.drawable.ic_launcher_foreground));
        }
        // set custom asset url loader
        SimpleImageLoader.getInstance().addCustomUrlLoader(new UrlLoader() {
            @Override
            public boolean canHandle(@NotNull String url) {
                return url.startsWith(SCHEME_ASSET);
            }

            @Nullable
            @Override
            public InputStream open(@NotNull String url) {
                try {
                    return getAssets().open(url.substring(SCHEME_ASSET.length()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
