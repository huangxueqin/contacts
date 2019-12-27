package com.xueqin.contacts.util;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.xueqin.contacts.model.ContactInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUtils {

    @Nullable
    public static ContactInfo parseContactInfo(@Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        try {
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            String title = jsonObject.getString("title");
            String introduction = jsonObject.getString("introduction");
            String avatarFileName = jsonObject.getString("avatar_filename");

            if (TextUtils.isEmpty(firstName) ||
                    TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(title) ||
                    TextUtils.isEmpty(introduction) ||
                    TextUtils.isEmpty(avatarFileName)) {
                return null;
            }

            return new ContactInfo(firstName, lastName, title, introduction, avatarFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
