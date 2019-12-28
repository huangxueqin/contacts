package com.xueqin.contacts;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xueqin.contact.provider.ContactAvatarProvider;
import com.xueqin.contacts.data.util.IOUtils;
import com.xueqin.contacts.loader.ImageDownloader;
import com.xueqin.contacts.loader.SimpleImageLoader;
import com.xueqin.contacts.provider.ContactProviderManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class ContactApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    private void initImageLoader() {
        SimpleImageLoader.getInstance().setImageDownloader(new ImageDownloader() {
            @Nullable
            @Override
            public Bitmap download(@NotNull String url) {
                final ContactAvatarProvider provider = ContactProviderManager.getInstance()
                        .getProvider(ContactAvatarProvider.class.getName());
                if (provider != null) {
                    InputStream is = null;
                    try {
                        is = provider.getContactAvatar(ContactApp.this, url);
                        return BitmapFactory.decodeStream(is);
                    } finally {
                        IOUtils.closeQuietly(is);
                    }
                }
                return null;
            }
        });
    }

}
