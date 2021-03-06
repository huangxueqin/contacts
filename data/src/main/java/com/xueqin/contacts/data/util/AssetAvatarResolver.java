package com.xueqin.contacts.data.util;

import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class AssetAvatarResolver {

    private static final String AVATAR_FOLDER = "avatars";
    private static final String AVATAR_FILE_EXTENSION = ".png";

    @NonNull
    public static String getAvatarPathInAsset(@NonNull DisplayMetrics dm, @NonNull String baseName) {
        final String avatarFilename;
        if (baseName.indexOf('.') > 0) {
            avatarFilename = baseName.split("\\.")[0];
        } else {
            avatarFilename = baseName;
        }
        String filenameByDpi = getAvatarFilenameForScreenDpi(dm, avatarFilename);
        return AVATAR_FOLDER + "/" + filenameByDpi + AVATAR_FILE_EXTENSION;
    }

    @NonNull
    public static String getAvatarFilenameForScreenDpi(@NonNull DisplayMetrics dm, @NonNull String suggestName) {
        final int densityDpi = dm.densityDpi;
        if (densityDpi < DisplayMetrics.DENSITY_HIGH) {
            return suggestName;
        } else if (densityDpi < DisplayMetrics.DENSITY_XHIGH) {
            return suggestName + "@2x";
        } else {
            return suggestName + "@3x";
        }
    }

}
