package com.simibubi.create.foundation.gui;

import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Theme {

    private static final List<Theme> THEMES = new ArrayList();

    public static final Theme BASE = addTheme(new Theme());

    protected final Map<String, Theme.ColorHolder> colors;

    private int priority = 0;

    public static Theme addTheme(@Nonnull Theme theme) {
        THEMES.add(theme);
        THEMES.sort(Comparator.comparingInt(Theme::getPriority).reversed());
        return theme;
    }

    public static void removeTheme(Theme theme) {
        THEMES.remove(theme);
    }

    public static void reload() {
        THEMES.forEach(Theme::init);
    }

    private static Theme.ColorHolder resolve(String key) {
        return (Theme.ColorHolder) THEMES.stream().map(theme -> theme.get(key)).filter(Objects::nonNull).findFirst().map(holder -> holder.lookupKey == null ? holder : resolve(holder.lookupKey)).orElse(Theme.ColorHolder.MISSING);
    }

    @Nonnull
    public static Couple<Color> p(@Nonnull Theme.Key key) {
        return p(key.get());
    }

    @Nonnull
    public static Couple<Color> p(String key) {
        return resolve(key).asPair();
    }

    @Nonnull
    public static Color c(@Nonnull Theme.Key key, boolean first) {
        return c(key.get(), first);
    }

    @Nonnull
    public static Color c(String key, boolean first) {
        return p(key).get(first);
    }

    public static int i(@Nonnull Theme.Key key, boolean first) {
        return i(key.get(), first);
    }

    public static int i(String key, boolean first) {
        return p(key).get(first).getRGB();
    }

    @Nonnull
    public static Color c(@Nonnull Theme.Key key) {
        return c(key.get());
    }

    @Nonnull
    public static Color c(String key) {
        return resolve(key).get();
    }

    public static int i(@Nonnull Theme.Key key) {
        return i(key.get());
    }

    public static int i(String key) {
        return resolve(key).get().getRGB();
    }

    protected Theme() {
        this.colors = new HashMap();
        this.init();
    }

    protected void init() {
        this.put(Theme.Key.BUTTON_IDLE, new Color(-578111786, true), new Color(-1869957418, true));
        this.put(Theme.Key.BUTTON_HOVER, new Color(-6636589, true), new Color(-795165741, true));
        this.put(Theme.Key.BUTTON_CLICK, new Color(-1), new Color(-285212673));
        this.put(Theme.Key.BUTTON_DISABLE, new Color(-2138009456, true), new Color(1620086928, true));
        this.put(Theme.Key.BUTTON_SUCCESS, new Color(-863438968, true), new Color(-870265824, true));
        this.put(Theme.Key.BUTTON_FAIL, new Color(-856192888, true), new Color(-859037664, true));
        this.put(Theme.Key.TEXT, new Color(-1118482), new Color(-6052957));
        this.put(Theme.Key.TEXT_DARKER, new Color(-6052957), new Color(-8355712));
        this.put(Theme.Key.TEXT_ACCENT_STRONG, new Color(-7686442), new Color(-7686442));
        this.put(Theme.Key.TEXT_ACCENT_SLIGHT, new Color(-2232577), new Color(-6246208));
        this.put(Theme.Key.STREAK, new Color(1052688, false));
        this.put(Theme.Key.VANILLA_TOOLTIP_BORDER, new Color(1347420415, true), new Color(1344798847, true));
        this.put(Theme.Key.VANILLA_TOOLTIP_BACKGROUND, new Color(-267386864, true));
        this.put(Theme.Key.PONDER_BUTTON_IDLE, new Color(1623245055, true), new Color(817938687, true));
        this.put(Theme.Key.PONDER_BUTTON_HOVER, new Color(-255803137, true), new Color(-1597980417, true));
        this.put(Theme.Key.PONDER_BUTTON_CLICK, new Color(-1), new Color(-570425345));
        this.put(Theme.Key.PONDER_BUTTON_DISABLE, new Color(-2138009456, true), new Color(546345104, true));
        this.put(Theme.Key.PONDER_BACKGROUND_TRANSPARENT, new Color(-587202560, true));
        this.put(Theme.Key.PONDER_BACKGROUND_FLAT, new Color(-16777216, false));
        this.put(Theme.Key.PONDER_BACKGROUND_IMPORTANT, new Color(-586281440, true));
        this.put(Theme.Key.PONDER_IDLE, new Color(1090514653, true), new Color(553643741, true));
        this.put(Theme.Key.PONDER_HOVER, new Color(1895825407, true), new Color(822083583, true));
        this.put(Theme.Key.PONDER_HIGHLIGHT, new Color(-251662627, true), new Color(1627385565, true));
        this.put(Theme.Key.TEXT_WINDOW_BORDER, new Color(1618632704, true), new Color(544890880, true));
        this.put(Theme.Key.PONDER_BACK_ARROW, new Color(-257255015, true), new Color(816486809, true));
        this.put(Theme.Key.PONDER_PROGRESSBAR, new Color(-2130710819, true), new Color(1358950109, true));
        this.put(Theme.Key.PONDER_MISSING_CREATE, new Color(1889027328, true), new Color(1885938688, true));
        this.lookup(Theme.Key.PONDER_MISSING_VANILLA, Theme.Key.VANILLA_TOOLTIP_BORDER);
        this.put(Theme.Key.CONFIG_TITLE_A, new Color(-3760196, true), new Color(-608069, true));
        this.put(Theme.Key.CONFIG_TITLE_B, new Color(-608069, true), new Color(-263788, true));
    }

    protected void put(String key, Color c) {
        this.colors.put(key, Theme.ColorHolder.single(c));
    }

    protected void put(Theme.Key key, Color c) {
        this.put(key.get(), c);
    }

    protected void put(String key, Color c1, Color c2) {
        this.colors.put(key, Theme.ColorHolder.pair(c1, c2));
    }

    protected void put(Theme.Key key, Color c1, Color c2) {
        this.put(key.get(), c1, c2);
    }

    protected void lookup(Theme.Key key, Theme.Key source) {
        this.colors.put(key.get(), Theme.ColorHolder.lookup(source.get()));
    }

    @Nullable
    protected Theme.ColorHolder get(String key) {
        return (Theme.ColorHolder) this.colors.get(key);
    }

    public int getPriority() {
        return this.priority;
    }

    public Theme setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    private static class ColorHolder {

        private static final Theme.ColorHolder MISSING = single(Color.BLACK);

        private Couple<Color> colors;

        private String lookupKey;

        private static Theme.ColorHolder single(Color c) {
            Theme.ColorHolder h = new Theme.ColorHolder();
            h.colors = Couple.create(c.setImmutable(), c.setImmutable());
            return h;
        }

        private static Theme.ColorHolder pair(Color first, Color second) {
            Theme.ColorHolder h = new Theme.ColorHolder();
            h.colors = Couple.create(first.setImmutable(), second.setImmutable());
            return h;
        }

        private static Theme.ColorHolder lookup(String key) {
            Theme.ColorHolder h = new Theme.ColorHolder();
            h.lookupKey = key;
            return h;
        }

        private Color get() {
            return this.colors.getFirst();
        }

        private Couple<Color> asPair() {
            return this.colors;
        }
    }

    public static class Key {

        public static final Theme.Key BUTTON_IDLE = new Theme.Key();

        public static final Theme.Key BUTTON_HOVER = new Theme.Key();

        public static final Theme.Key BUTTON_CLICK = new Theme.Key();

        public static final Theme.Key BUTTON_DISABLE = new Theme.Key();

        public static final Theme.Key BUTTON_SUCCESS = new Theme.Key();

        public static final Theme.Key BUTTON_FAIL = new Theme.Key();

        public static final Theme.Key TEXT = new Theme.Key();

        public static final Theme.Key TEXT_DARKER = new Theme.Key();

        public static final Theme.Key TEXT_ACCENT_STRONG = new Theme.Key();

        public static final Theme.Key TEXT_ACCENT_SLIGHT = new Theme.Key();

        public static final Theme.Key STREAK = new Theme.Key();

        public static final Theme.Key VANILLA_TOOLTIP_BORDER = new Theme.Key();

        public static final Theme.Key VANILLA_TOOLTIP_BACKGROUND = new Theme.Key();

        public static final Theme.Key PONDER_BACKGROUND_TRANSPARENT = new Theme.Key();

        public static final Theme.Key PONDER_BACKGROUND_FLAT = new Theme.Key();

        public static final Theme.Key PONDER_BACKGROUND_IMPORTANT = new Theme.Key();

        public static final Theme.Key PONDER_IDLE = new Theme.Key();

        public static final Theme.Key PONDER_HOVER = new Theme.Key();

        public static final Theme.Key PONDER_HIGHLIGHT = new Theme.Key();

        public static final Theme.Key TEXT_WINDOW_BORDER = new Theme.Key();

        public static final Theme.Key PONDER_BACK_ARROW = new Theme.Key();

        public static final Theme.Key PONDER_PROGRESSBAR = new Theme.Key();

        public static final Theme.Key PONDER_MISSING_CREATE = new Theme.Key();

        public static final Theme.Key PONDER_MISSING_VANILLA = new Theme.Key();

        public static final Theme.Key PONDER_BUTTON_IDLE = new Theme.Key();

        public static final Theme.Key PONDER_BUTTON_HOVER = new Theme.Key();

        public static final Theme.Key PONDER_BUTTON_CLICK = new Theme.Key();

        public static final Theme.Key PONDER_BUTTON_DISABLE = new Theme.Key();

        public static final Theme.Key CONFIG_TITLE_A = new Theme.Key();

        public static final Theme.Key CONFIG_TITLE_B = new Theme.Key();

        private static int index = 0;

        private final String s;

        protected Key() {
            this.s = "_" + index++;
        }

        protected Key(String s) {
            this.s = s;
        }

        public String get() {
            return this.s;
        }
    }
}