package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.Keymap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class KeyLayout {

    private static final String LAYOUT_ROOT = "assets/keymap/layouts";

    protected static KeyLayout layoutDefault;

    protected static KeyLayout layoutCurrent;

    protected static HashMap<String, KeyLayout> layouts = new HashMap();

    protected KeyMeta meta;

    protected Keys keys;

    public static void registerLayout(KeyLayout layout) {
        layouts.put(layout.meta.code, layout);
        updateMouseKeys(layout.keys.mouse());
        updateMouseKeys(layout.keys.basic());
        updateMouseKeys(layout.keys.numpad());
        updateMouseKeys(layout.keys.extra());
    }

    protected static void updateMouseKeys(List<KeyRow> rows) {
        for (KeyRow row : rows) {
            for (KeyData key : row.row) {
                if (key.code() < 10) {
                    key.mouse(true);
                }
            }
        }
    }

    public static void loadKeys() {
        layouts.clear();
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        ClassLoader loader = KeyLayout.class.getClassLoader();
        Stream<Path> files = null;
        try {
            Keymap.logger().warn(loader.getResource("/"));
            Keymap.logger().warn(loader.getResource("assets/keymap/layouts"));
            URI layoutUri = (URI) Objects.requireNonNull(((URL) Objects.requireNonNull(loader.getResource("assets/keymap/layouts"))).toURI());
            Keymap.logger().warn("Keymap layout stream: {}", layoutUri);
            Keymap.logger().warn("Keymap layout stream scheme: {}", layoutUri.getScheme());
            Path path;
            if (layoutUri.getScheme().equals("jar")) {
                FileSystem fs = getFileSystem(layoutUri);
                path = fs.getPath("assets/keymap/layouts");
            } else {
                path = Path.of(layoutUri);
            }
            files = Files.walk(path, 1, new FileVisitOption[0]);
            Iterator<Path> it = files.iterator();
            while (it.hasNext()) {
                Path p = (Path) it.next();
                if (p.getFileName().toString().endsWith(".json")) {
                    tryLoadLayout(gson, loader, p);
                }
            }
        } catch (Exception var12) {
            Keymap.logger().error(var12.getMessage());
            var12.printStackTrace();
        } finally {
            if (files != null) {
                files.close();
            }
        }
    }

    private static void tryLoadLayout(Gson gson, ClassLoader loader, Path p) {
        try {
            InputStreamReader reader = new InputStreamReader((InputStream) Objects.requireNonNull(loader.getResourceAsStream(p.toString())), StandardCharsets.UTF_8);
            try {
                registerLayout((KeyLayout) gson.fromJson(reader, KeyLayout.class));
            } catch (Throwable var7) {
                try {
                    reader.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            reader.close();
        } catch (Exception var8) {
            Keymap.logger().warn("Can't load {} ; {}", p.getFileName(), var8.getMessage());
            var8.printStackTrace();
        }
    }

    private static FileSystem getFileSystem(URI layoutUri) throws IOException {
        FileSystem fs;
        try {
            fs = FileSystems.getFileSystem(layoutUri);
            Keymap.logger().info("GET_FS");
        } catch (Exception var3) {
            fs = FileSystems.newFileSystem(layoutUri, Collections.emptyMap());
            Keymap.logger().info("NEW_FS");
        }
        return fs;
    }

    public static KeyLayout getCurrentLayout() {
        return layoutCurrent;
    }

    public static KeyLayout getLayoutWithCode(String code) {
        if (layouts.containsKey(code)) {
            return (KeyLayout) layouts.get(code);
        } else {
            Keymap.logger().warn("Cannot find layout for [{}], defaulting to en_us", code);
            return (KeyLayout) layouts.get("en_us");
        }
    }

    public String toString() {
        return "KeyLayout(meta=" + this.meta() + ", keys=" + this.keys() + ")";
    }

    public KeyLayout(KeyMeta meta, Keys keys) {
        this.meta = meta;
        this.keys = keys;
    }

    public static KeyLayout layoutDefault() {
        return layoutDefault;
    }

    public static KeyLayout layoutCurrent() {
        return layoutCurrent;
    }

    public static HashMap<String, KeyLayout> layouts() {
        return layouts;
    }

    public KeyMeta meta() {
        return this.meta;
    }

    public KeyLayout meta(KeyMeta meta) {
        this.meta = meta;
        return this;
    }

    public Keys keys() {
        return this.keys;
    }

    public KeyLayout keys(Keys keys) {
        this.keys = keys;
        return this;
    }
}