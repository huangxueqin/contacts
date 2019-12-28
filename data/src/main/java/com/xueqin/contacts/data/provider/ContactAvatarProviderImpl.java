package com.xueqin.contacts.data.provider;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xueqin.contact.provider.ContactAvatarProvider;
import com.xueqin.contacts.data.util.AssetAvatarResolver;

import java.io.IOException;
import java.io.InputStream;

@Keep
public class ContactAvatarProviderImpl implements ContactAvatarProvider {

    @Nullable
    @Override
    public InputStream getContactAvatar(@NonNull Context context, @NonNull String url) {
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        final String avatarPath = AssetAvatarResolver.getAvatarPathInAsset(dm, url);
        try {
            return context.getAssets().open(avatarPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
