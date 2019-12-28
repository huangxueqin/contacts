package com.xueqin.contacts.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IOUtils {

    public static String readTextFromAsset(@NonNull Context context,
                                           @NonNull String path) {
        InputStream is = null;
        try {
            is = context.getAssets().open(path);
            return readTextFromStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(is);
        }
        return null;
    }

    @NonNull
    public static String readTextFromStream(@NonNull InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
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
