package com.xueqin.contacts.data.provider;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xueqin.contact.model.ContactInfo;
import com.xueqin.contact.provider.ContactDataProvider;
import com.xueqin.contacts.data.util.AssetAvatarResolver;
import com.xueqin.contacts.data.util.ContactParser;
import com.xueqin.contacts.data.util.IOUtils;

import java.util.List;

@Keep
public class ContactDataProviderImpl implements ContactDataProvider {

    private static final String CONTACT_INFO_PATH = "contacts.json";

    @Nullable
    @Override
    public List<ContactInfo> queryContacts(@NonNull Context context) {
        final String json = IOUtils.readTextFromAsset(context, CONTACT_INFO_PATH);
        List<ContactInfo> contacts = ContactParser.parseContactList(json);
        // resolve real avatar path in assets
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        for (ContactInfo contactInfo : contacts) {
            String avatarBaseName = contactInfo.getAvatarUrl();
            if (!TextUtils.isEmpty(avatarBaseName)) {
                // add a asset scheme header, so app module can recognized
                contactInfo.setAvatarUrl("asset://" + AssetAvatarResolver.getAvatarPathInAsset(dm, avatarBaseName));
            }
        }
        return contacts;
    }
}
