package de.keksuccino.konkrete.web;

import de.keksuccino.konkrete.input.CharacterFilter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    public static boolean isValidUrl(String url) {
        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            try {
                URL u = new URL(url);
                HttpURLConnection c = (HttpURLConnection) u.openConnection();
                c.addRequestProperty("User-Agent", "Mozilla/4.0");
                c.setRequestMethod("HEAD");
                int r = c.getResponseCode();
                if (r == 200) {
                    return true;
                }
            } catch (Exception var6) {
                System.out.println("Unable to check for valid url via HEAD request!");
                System.out.println("Trying alternative method..");
                try {
                    URL ux = new URL(url);
                    HttpURLConnection cx = (HttpURLConnection) ux.openConnection();
                    cx.addRequestProperty("User-Agent", "Mozilla/4.0");
                    int rx = cx.getResponseCode();
                    if (rx == 200) {
                        return true;
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static List<String> getPlainTextContentOfPage(URL webLink) {
        List<String> l = new ArrayList();
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(webLink.openStream(), StandardCharsets.UTF_8));
            for (String s = r.readLine(); s != null; s = r.readLine()) {
                l.add(s);
            }
            r.close();
        } catch (Exception var6) {
            if (r != null) {
                try {
                    r.close();
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }
            l.clear();
        }
        return l;
    }

    public static String filterURL(String url) {
        if (url == null) {
            return null;
        } else {
            CharacterFilter f = new CharacterFilter();
            f.addAllowedCharacters("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "-", ".", "_", "~", ":", "/", "?", "#", "[", "]", "@", "!", "$", "&", "'", "(", ")", "*", "+", ",", ";", "%", "=");
            return f.filterForAllowedChars(url);
        }
    }
}