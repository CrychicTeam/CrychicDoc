package com.github.einjerjar.mc.keymap.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import net.minecraft.locale.Language;

public class Utils {

    public static final String SEPARATOR = "--------------------";

    protected static final int MAX_SLUG_LENGTH = 256;

    protected static Language languageInstance = null;

    private Utils() {
    }

    public static synchronized Language languageInstance() {
        if (languageInstance == null) {
            languageInstance = Language.getInstance();
        }
        return languageInstance;
    }

    public static String translate(String key) {
        return languageInstance().getOrDefault(key);
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    public static String slugify(String s) {
        String intermediateResult = Normalizer.normalize(s, Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[^-_a-zA-Z\\d]", "-").replaceAll("\\s+", "-").replaceAll("-+", "-").replaceAll("^-", "").replaceAll("-$", "").toLowerCase();
        return intermediateResult.substring(0, Math.min(256, intermediateResult.length()));
    }
}