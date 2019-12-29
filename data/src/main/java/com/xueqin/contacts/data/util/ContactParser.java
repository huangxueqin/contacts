package com.xueqin.contacts.data.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xueqin.contact.model.ContactInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactParser {

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

            if (TextUtils.isEmpty(firstName) ||
                    TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(title) ||
                    TextUtils.isEmpty(introduction)) {
                return null;
            }

            String avatarFileName = jsonObject.getString("avatar_filename");

            return new ContactInfo(firstName, lastName, title, introduction, avatarFileName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * parse list of contact info from a json array string
     * @return the parsed result. return an empty list if parse failed
     */
    @NonNull
    public static List<ContactInfo> parseContactList(@Nullable String contactJson) {
        List<ContactInfo> contacts = new ArrayList<>();
        if (!TextUtils.isEmpty(contactJson)) {
            try {
                JSONArray contactJa = new JSONArray(contactJson);
                for (int i = 0; i < contactJa.length(); i++) {
                    ContactInfo ci = parseContactInfo(contactJa.getJSONObject(i));
                    if (ci != null) {
                        contacts.add(ci);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }
}
