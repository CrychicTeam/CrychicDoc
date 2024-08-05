package com.github.alexthe666.citadel.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WebHelper {

    @Nullable
    public static BufferedReader getURLContents(@Nonnull String urlString, @Nonnull String backupFileLoc) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            InputStream stream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            return new BufferedReader(reader);
        } catch (Exception var7) {
            var7.printStackTrace();
            try {
                return new BufferedReader(new InputStreamReader(WebHelper.class.getClass().getClassLoader().getResourceAsStream(backupFileLoc), StandardCharsets.UTF_8));
            } catch (NullPointerException var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }
}