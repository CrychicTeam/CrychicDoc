package dev.latvian.mods.rhino;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ImplementationVersion {

    private static final ImplementationVersion version = new ImplementationVersion();

    private String versionString;

    public static String get() {
        return version.versionString;
    }

    private ImplementationVersion() {
        Enumeration<URL> urls;
        try {
            urls = ImplementationVersion.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        } catch (IOException var7) {
            return;
        }
        while (urls.hasMoreElements()) {
            URL metaUrl = (URL) urls.nextElement();
            try {
                InputStream is = metaUrl.openStream();
                label50: {
                    try {
                        Manifest mf = new Manifest(is);
                        Attributes attrs = mf.getMainAttributes();
                        if ("Mozilla Rhino".equals(attrs.getValue("Implementation-Title"))) {
                            this.versionString = "Rhino " + attrs.getValue("Implementation-Version") + " " + attrs.getValue("Built-Date").replaceAll("-", " ");
                            break label50;
                        }
                    } catch (Throwable var8) {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (Throwable var6) {
                                var8.addSuppressed(var6);
                            }
                        }
                        throw var8;
                    }
                    if (is != null) {
                        is.close();
                    }
                    continue;
                }
                if (is != null) {
                    is.close();
                }
                return;
            } catch (IOException var9) {
            }
        }
    }
}