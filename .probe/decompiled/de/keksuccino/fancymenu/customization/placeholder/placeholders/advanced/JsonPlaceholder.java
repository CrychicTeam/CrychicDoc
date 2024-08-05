package de.keksuccino.fancymenu.customization.placeholder.placeholders.advanced;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.events.ModReloadEvent;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.json.JsonUtils;
import de.keksuccino.konkrete.web.WebUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsonPlaceholder extends Placeholder {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static volatile Map<String, List<String>> cachedPlaceholders = new HashMap();

    protected static volatile List<String> currentlyUpdatingPlaceholders = new ArrayList();

    protected static volatile List<String> invalidWebPlaceholderLinks = new ArrayList();

    protected static boolean eventsRegistered = false;

    public JsonPlaceholder() {
        super("json");
        if (!eventsRegistered) {
            EventHandler.INSTANCE.registerListenersOf(JsonPlaceholder.class);
            eventsRegistered = true;
        }
    }

    @EventListener
    public static void onReload(ModReloadEvent e) {
        try {
            cachedPlaceholders.clear();
            invalidWebPlaceholderLinks.clear();
            LOGGER.info("[FANCYMENU] V2 JsonPlaceholder cache successfully cleared!");
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String source = (String) dps.values.get("source");
        String jsonPath = (String) dps.values.get("json_path");
        if (source != null && jsonPath != null) {
            source = StringUtils.convertFormatCodes(source, "§", "&");
            File f = new File(source);
            if (!f.exists() || !f.getAbsolutePath().replace("\\", "/").startsWith(Minecraft.getInstance().gameDirectory.getAbsolutePath().replace("\\", "/"))) {
                String linkTemp = Minecraft.getInstance().gameDirectory.getAbsolutePath().replace("\\", "/") + "/" + source;
                f = new File(linkTemp);
            }
            if (f.isFile()) {
                List<String> json = JsonUtils.getJsonValueByPath(f, jsonPath);
                if (json != null) {
                    return formatJsonToString(json);
                }
            } else if (!isInvalidWebPlaceholderLink(source)) {
                List<String> json = getCachedWebPlaceholder(dps.placeholderString);
                if (json != null) {
                    return formatJsonToString(json);
                }
                if (!isWebPlaceholderUpdating(dps.placeholderString)) {
                    cacheWebPlaceholder(dps.placeholderString, source, jsonPath);
                }
                return "";
            }
        }
        return null;
    }

    protected static String formatJsonToString(List<String> json) {
        if (!json.isEmpty()) {
            if (json.size() == 1) {
                return (String) json.get(0);
            } else {
                String rep = "";
                for (String s2 : json) {
                    if (rep.length() == 0) {
                        rep = rep + s2;
                    } else {
                        rep = rep + "%n%" + s2;
                    }
                }
                return rep;
            }
        } else {
            return "§c[error while formatting JSON string]";
        }
    }

    protected static boolean isInvalidWebPlaceholderLink(String link) {
        try {
            return invalidWebPlaceholderLinks.contains(link);
        } catch (Exception var2) {
            var2.printStackTrace();
            return true;
        }
    }

    protected static List<String> getCachedWebPlaceholder(String placeholder) {
        try {
            return (List<String>) cachedPlaceholders.get(placeholder);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    protected static boolean isWebPlaceholderUpdating(String placeholder) {
        try {
            return currentlyUpdatingPlaceholders.contains(placeholder);
        } catch (Exception var2) {
            var2.printStackTrace();
            return true;
        }
    }

    protected static void cacheWebPlaceholder(String placeholder, String source, String jsonPath) {
        try {
            if (!currentlyUpdatingPlaceholders.contains(placeholder)) {
                currentlyUpdatingPlaceholders.add(placeholder);
                new Thread(() -> {
                    try {
                        if (WebUtils.isValidUrl(source)) {
                            cachedPlaceholders.put(placeholder, JsonUtils.getJsonValueByPath(getJsonStringFromURL(source), jsonPath));
                        } else {
                            invalidWebPlaceholderLinks.add(source);
                        }
                    } catch (Exception var5) {
                        var5.printStackTrace();
                    }
                    try {
                        currentlyUpdatingPlaceholders.remove(placeholder);
                    } catch (Exception var4x) {
                        var4x.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    protected static String getJsonStringFromURL(String url) {
        BufferedReader in = null;
        StringBuilder contentBuilder = new StringBuilder();
        try {
            URL u = new URL(url);
            URLConnection con = u.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                contentBuilder.append(line);
            }
            in.close();
            return contentBuilder.toString();
        } catch (Exception var8) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
            var8.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("source");
        l.add("json_path");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.helper.placeholder.json");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.helper.placeholder.json.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.advanced");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("source", "path_or_link_to_json");
        dps.values.put("json_path", "$.some.json.path");
        return dps;
    }
}