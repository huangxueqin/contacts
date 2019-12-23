package com.xueqin.contacts.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    @Nullable
    public static String readTextFromAssets(@NonNull Context context, @NonNull String fileName) {
        return null;
    }

    public static void closeQuietly(@Nullable Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
