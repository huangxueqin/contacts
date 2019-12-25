package com.xueqin.contacts.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xueqin.contacts.model.ContactInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AssetUtils {

    private static final String TAG = "AssetUtils";

    private static final String CONTACT_ASSET_NAME = "contacts.json";

    private static final String AVATAR_ASSET_FOLDER = "avatars";

    @NonNull
    public static List<ContactInfo> loadAllContacts(@NonNull Context context) {
        List<ContactInfo> contacts = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(CONTACT_ASSET_NAME)));
            StringBuffer sb = new StringBuffer();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONArray contactJa = new JSONArray(sb.toString());
            for (int i = 0; i < contactJa.length(); i++) {
                ContactInfo ci = parseContactInfo(contactJa.getJSONObject(i));
                if (ci != null) {
                    contacts.add(ci);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(br);
        }
        return contacts;
    }

    @Nullable
    public static Bitmap loadAvatar(@NonNull Context context, @NonNull ContactInfo ci) {
        return loadAvatar(context, ci.getAvatarFileName());
    }

    @Nullable
    public static Bitmap loadAvatar(@NonNull Context context, @NonNull String avatarFileName) {
        String targetPath = getAvatarAssetPath(context, avatarFileName);
        Log.d(TAG, "target avatar path is " + targetPath);
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NonNull
    private static String getAvatarAssetPath(@NonNull Context context, @NonNull String avatarFileName) {
        String avatarFileNameWithoutExtension = avatarFileName.split("\\.")[0];
        float screenDensity = ScreenUtils.getDensity(context);
        if (screenDensity < 1.5f) {
            // lower than xhdpi
            return AVATAR_ASSET_FOLDER + "/" + avatarFileName;
        } else if (screenDensity < 2.5f) {
            // lower than xxhdpi
            return AVATAR_ASSET_FOLDER + "/" + avatarFileNameWithoutExtension + "@2x.png";
        } else {
            // xxhdpi or higher
            return AVATAR_ASSET_FOLDER + "/" + avatarFileNameWithoutExtension + "@3x.png";
        }
    }

    @Nullable
    private static ContactInfo parseContactInfo(@Nullable JSONObject contactJo) {
        if (contactJo == null) {
            return null;
        }
        try {
            String firstName = contactJo.getString("first_name");
            String lastName = contactJo.getString("last_name");
            String title = contactJo.getString("title");
            String introduction = contactJo.getString("introduction");
            String avatarFileName = contactJo.getString("avatar_filename");

            if (TextUtils.isEmpty(firstName) ||
                    TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(title) ||
                    TextUtils.isEmpty(introduction) ||
                    TextUtils.isEmpty(avatarFileName)) {
                return null;
            }

            ContactInfo ci = new ContactInfo();
            ci.setFirstName(firstName);
            ci.setLastName(lastName);
            ci.setTitle(title);
            ci.setIntroduction(introduction);
            ci.setAvatarFileName(avatarFileName);
            return ci;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
