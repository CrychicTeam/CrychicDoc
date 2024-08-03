package com.github.einjerjar.mc.keymap.objects;

import com.github.einjerjar.mc.keymap.Keymap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Credits {

    private static final String CREDITS_ROOT = "assets/keymap/credits.json";

    static Credits instance;

    List<Credits.LanguageCredits> language;

    List<Credits.LayoutCredits> layout;

    List<Credits.CoreCredits> core;

    public static synchronized Credits instance() {
        if (instance == null) {
            loadCredits();
        }
        return instance;
    }

    public static void loadCredits() {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        ClassLoader loader = Credits.class.getClassLoader();
        try {
            URI creditUri = ((URL) Objects.requireNonNull(loader.getResource("assets/keymap/credits.json"))).toURI();
            if (creditUri.getScheme().equals("jar")) {
                try {
                    FileSystems.getFileSystem(creditUri);
                } catch (Exception var9) {
                    FileSystems.newFileSystem(creditUri, Collections.emptyMap());
                }
            }
            InputStreamReader reader = new InputStreamReader((InputStream) Objects.requireNonNull(loader.getResourceAsStream("assets/keymap/credits.json")), StandardCharsets.UTF_8);
            try {
                instance = (Credits) gson.fromJson(reader, Credits.class);
            } catch (Throwable var8) {
                try {
                    reader.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            reader.close();
        } catch (Exception var10) {
            Keymap.logger().warn("CANT LOAD CREDITS");
            var10.printStackTrace();
        }
    }

    public List<Credits.LanguageCredits> language() {
        return this.language;
    }

    public List<Credits.LayoutCredits> layout() {
        return this.layout;
    }

    public List<Credits.CoreCredits> core() {
        return this.core;
    }

    public Credits language(List<Credits.LanguageCredits> language) {
        this.language = language;
        return this;
    }

    public Credits layout(List<Credits.LayoutCredits> layout) {
        this.layout = layout;
        return this;
    }

    public Credits core(List<Credits.CoreCredits> core) {
        this.core = core;
        return this;
    }

    public String toString() {
        return "Credits(language=" + this.language() + ", layout=" + this.layout() + ", core=" + this.core() + ")";
    }

    public Credits() {
    }

    public Credits(List<Credits.LanguageCredits> language, List<Credits.LayoutCredits> layout, List<Credits.CoreCredits> core) {
        this.language = language;
        this.layout = layout;
        this.core = core;
    }

    public static class CoreCredits {

        String name;

        List<String> contributions;

        public String name() {
            return this.name;
        }

        public List<String> contributions() {
            return this.contributions;
        }

        public Credits.CoreCredits name(String name) {
            this.name = name;
            return this;
        }

        public Credits.CoreCredits contributions(List<String> contributions) {
            this.contributions = contributions;
            return this;
        }

        public String toString() {
            return "Credits.CoreCredits(name=" + this.name() + ", contributions=" + this.contributions() + ")";
        }

        public CoreCredits() {
        }

        public CoreCredits(String name, List<String> contributions) {
            this.name = name;
            this.contributions = contributions;
        }
    }

    public static class LanguageCredits {

        String lang;

        List<String> name;

        public String lang() {
            return this.lang;
        }

        public List<String> name() {
            return this.name;
        }

        public Credits.LanguageCredits lang(String lang) {
            this.lang = lang;
            return this;
        }

        public Credits.LanguageCredits name(List<String> name) {
            this.name = name;
            return this;
        }

        public String toString() {
            return "Credits.LanguageCredits(lang=" + this.lang() + ", name=" + this.name() + ")";
        }

        public LanguageCredits() {
        }

        public LanguageCredits(String lang, List<String> name) {
            this.lang = lang;
            this.name = name;
        }
    }

    public static class LayoutCredits {

        String key;

        List<String> name;

        public String key() {
            return this.key;
        }

        public List<String> name() {
            return this.name;
        }

        public Credits.LayoutCredits key(String key) {
            this.key = key;
            return this;
        }

        public Credits.LayoutCredits name(List<String> name) {
            this.name = name;
            return this;
        }

        public String toString() {
            return "Credits.LayoutCredits(key=" + this.key() + ", name=" + this.name() + ")";
        }

        public LayoutCredits() {
        }

        public LayoutCredits(String key, List<String> name) {
            this.key = key;
            this.name = name;
        }
    }
}