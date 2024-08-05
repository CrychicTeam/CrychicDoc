package me.lucko.spark.lib.adventure.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;
import org.jetbrains.annotations.NotNull;

public final class UTF8ResourceBundleControl extends Control {

    private static final UTF8ResourceBundleControl INSTANCE = new UTF8ResourceBundleControl();

    @NotNull
    public static Control get() {
        return INSTANCE;
    }

    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (format.equals("java.properties")) {
            String bundle = this.toBundleName(baseName, locale);
            String resource = this.toResourceName(bundle, "properties");
            InputStream is = null;
            if (reload) {
                URL url = loader.getResource(resource);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        is = connection.getInputStream();
                    }
                }
            } else {
                is = loader.getResourceAsStream(resource);
            }
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                PropertyResourceBundle var15;
                try {
                    var15 = new PropertyResourceBundle(isr);
                } catch (Throwable var13) {
                    try {
                        isr.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                    throw var13;
                }
                isr.close();
                return var15;
            } else {
                return null;
            }
        } else {
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
}