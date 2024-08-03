package journeymap.client;

import com.google.common.base.Joiner;
import com.google.common.collect.Ordering;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.util.FormattedCharSequence;

public class Constants {

    public static final Ordering<String> CASE_INSENSITIVE_NULL_SAFE_ORDER = Ordering.from(String.CASE_INSENSITIVE_ORDER).nullsLast();

    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    private static final Joiner path = Joiner.on(File.separator).useForNull("");

    private static final String END = null;

    public static String JOURNEYMAP_DIR = "journeymap";

    public static String CONFIG_DIR_LEGACY = path.join(JOURNEYMAP_DIR, "config", new Object[0]);

    public static String CONFIG_DIR = path.join(JOURNEYMAP_DIR, "config", new Object[] { Journeymap.JM_VERSION.toMajorMinorString(), END });

    public static String DATA_DIR = path.join(JOURNEYMAP_DIR, "data", new Object[0]);

    public static String SP_DATA_DIR = path.join(DATA_DIR, Constants.WorldType.sp, new Object[] { END });

    public static String MP_DATA_DIR = path.join(DATA_DIR, Constants.WorldType.mp, new Object[] { END });

    public static String RESOURCE_PACKS_DEFAULT = "Default";

    private static String ICON_DIR = path.join(JOURNEYMAP_DIR, "icon", new Object[0]);

    public static String WEB_DIR = path.join(JOURNEYMAP_DIR, "web", new Object[0]);

    public static String ENTITY_ICON_DIR = path.join(ICON_DIR, "entity", new Object[] { END });

    public static String WAYPOINT_ICON_DIR = path.join(ICON_DIR, "waypoint", new Object[] { END });

    public static String THEME_ICON_DIR = path.join(ICON_DIR, "theme", new Object[] { END });

    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        try {
            String lang = Minecraft.getInstance().getLanguageManager().getSelected();
            locale = new Locale(lang);
        } catch (Exception var2) {
            Journeymap.getLogger().warn("Couldn't determine locale from game settings, defaulting to " + locale);
        }
        return locale;
    }

    public static String getString(String key) {
        if (Minecraft.getInstance() == null) {
            return key;
        } else {
            try {
                String result = getTranslatedTextComponent(key).getString();
                if (result.equals(key) && JourneymapClient.getInstance().isMapping()) {
                }
                return result;
            } catch (Throwable var2) {
                Journeymap.getLogger().warn(String.format("Message key '%s' threw exception: %s", key, var2.getMessage()));
                return key;
            }
        }
    }

    public static MutableComponent getStringTextComponent(String key) {
        return Component.literal(key);
    }

    public static MutableComponent getTranslatedTextComponent(String key) {
        return Component.translatable(key);
    }

    public static FormattedCharSequence getFormattedText(String text, Style style, Font fontRenderer, int size) {
        Component textComponent = getStringTextComponent(text);
        List<FormattedCharSequence> toolTipList = Language.getInstance().getVisualOrder(fontRenderer.getSplitter().splitLines(textComponent, size, style));
        return FormattedCharSequence.composite(toolTipList);
    }

    public static String getString(String key, Object... params) {
        if (Minecraft.getInstance() == null) {
            return String.format("%s (%s)", key, Joiner.on(",").join(params));
        } else {
            try {
                String result = I18n.get(key, params);
                if (result.equals(key) && JourneymapClient.getInstance().isMapping()) {
                    Journeymap.getLogger().warn("Message key not found: " + String.format("%s (%s)", key, Joiner.on(",").join(params)));
                }
                return result;
            } catch (Throwable var3) {
                Journeymap.getLogger().warn(String.format("Message key '%s' threw exception: %s", key, var3.getMessage()));
                return key;
            }
        }
    }

    public static boolean safeEqual(String first, String second) {
        int result = CASE_INSENSITIVE_NULL_SAFE_ORDER.compare(first, second);
        return result != 0 ? false : CASE_INSENSITIVE_NULL_SAFE_ORDER.compare(first, second) == 0;
    }

    public static PackRepository getResourcePacks() {
        PackRepository resourcepackrepository = null;
        try {
            resourcepackrepository = Minecraft.getInstance().getResourcePackRepository();
        } catch (Throwable var2) {
            Journeymap.getLogger().error(String.format("Can't get resource pack names: %s", LogFormatter.toString(var2)));
        }
        return resourcepackrepository;
    }

    public static String birthdayMessage() {
        Calendar today = Calendar.getInstance();
        int month = today.get(2);
        int date = today.get(5);
        if (month == 6 && date == 2) {
            return getString("jm.common.birthday", "techbrew");
        } else {
            return month == 8 && date == 21 ? getString("jm.common.birthday", "mysticdrew") : null;
        }
    }

    public static enum WorldType {

        mp, sp
    }
}