package de.keksuccino.fancymenu.util.rendering.ui.theme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UIColorThemeSerializer {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final TypeAdapter<DrawableColor> DRAWABLE_COLOR_TYPE_ADAPTER = new TypeAdapter<DrawableColor>() {

        public void write(JsonWriter out, DrawableColor value) throws IOException {
            out.beginObject();
            out.name("hex").value(value.getHex());
            out.endObject();
        }

        public DrawableColor read(JsonReader in) throws IOException {
            String hex = null;
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals("hex")) {
                    hex = in.nextString();
                    break;
                }
            }
            in.endObject();
            return hex != null ? DrawableColor.of(hex) : DrawableColor.WHITE;
        }
    };

    @Nullable
    public static UIColorTheme deserializeTheme(@NotNull String json) {
        Objects.requireNonNull(json);
        try {
            Gson gson = buildGsonInstance();
            return (UIColorTheme) gson.fromJson(json, UIColorTheme.class);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static UIColorTheme deserializeThemeFromFile(@NotNull File file) {
        String json = "";
        for (String s : FileUtils.getFileLines(file)) {
            json = json + s;
        }
        return deserializeTheme(json);
    }

    @Nullable
    public static String serializeTheme(@NotNull UIColorTheme theme) {
        Objects.requireNonNull(theme);
        try {
            Gson gson = buildGsonInstance();
            return gson.toJson(theme);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void serializeThemeToFile(@NotNull UIColorTheme theme, @NotNull File file) {
        Objects.requireNonNull(theme);
        Objects.requireNonNull(file);
        try {
            Gson gson = buildGsonInstance();
            String json = gson.toJson(theme);
            if (json != null) {
                FileUtils.writeTextToFile(file, false, new String[] { json });
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private static Gson buildGsonInstance() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(DrawableColor.class, DRAWABLE_COLOR_TYPE_ADAPTER);
        return gsonBuilder.create();
    }
}