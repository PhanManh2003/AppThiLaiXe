package com.example.appthilaixe.util;

import android.content.Context;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    public static String loadJSON(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
