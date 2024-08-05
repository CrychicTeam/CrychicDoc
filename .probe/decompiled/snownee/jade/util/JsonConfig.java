package snownee.jade.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.api.theme.Theme;
import snownee.jade.impl.theme.ThemeSerializer;

public class JsonConfig<T> {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().enableComplexMapKeySerialization().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).registerTypeAdapter(Theme.class, new ThemeSerializer()).setLenient().create();

    private final File file;

    private final Codec<T> codec;

    private final JsonConfig.CachedSupplier<T> configGetter;

    public JsonConfig(String fileName, Codec<T> codec, @Nullable Runnable onUpdate, Supplier<T> defaultFactory) {
        this.file = new File(CommonProxy.getConfigDirectory(), fileName + (fileName.endsWith(".json") ? "" : ".json"));
        this.codec = codec;
        this.configGetter = new JsonConfig.CachedSupplier<>(() -> {
            if (!this.file.exists()) {
                T def = (T) defaultFactory.get();
                this.write(def, false);
                return def;
            } else {
                try {
                    FileReader reader = new FileReader(this.file, StandardCharsets.UTF_8);
                    Object var11;
                    try {
                        var11 = codec.parse(JsonOps.INSTANCE, (JsonElement) GSON.fromJson(reader, JsonElement.class)).get().left().orElseThrow();
                    } catch (Throwable var8) {
                        try {
                            reader.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                        throw var8;
                    }
                    reader.close();
                    return var11;
                } catch (Throwable var9) {
                    Jade.LOGGER.error("Failed to read config file %s".formatted(this.file), var9);
                    if (this.file.length() > 0L) {
                        try {
                            this.file.renameTo(new File(this.file.getPath() + ".invalid"));
                        } catch (Exception var6) {
                        }
                    }
                    T def = (T) defaultFactory.get();
                    this.write(def, false);
                    return def;
                }
            }
        });
        this.configGetter.onUpdate = onUpdate;
    }

    public JsonConfig(String fileName, Codec<T> codec, @Nullable Runnable onUpdate) {
        this(fileName, codec, onUpdate, () -> JadeCodecs.createFromEmptyMap(codec));
        JadeCodecs.createFromEmptyMap(codec);
    }

    public T get() {
        return this.configGetter.get();
    }

    public void save() {
        this.write(this.get(), false);
    }

    public void write(T t, boolean invalidate) {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(this.file, StandardCharsets.UTF_8);
            try {
                writer.write(GSON.toJson((JsonElement) this.codec.encodeStart(JsonOps.INSTANCE, t).get().left().orElseThrow()));
                if (invalidate) {
                    this.invalidate();
                }
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            writer.close();
        } catch (Throwable var8) {
            Jade.LOGGER.error("Failed to write config file %s".formatted(this.file), var8);
        }
    }

    public void invalidate() {
        this.configGetter.invalidate();
    }

    public File getFile() {
        return this.file;
    }

    static class CachedSupplier<T> {

        private final Supplier<T> supplier;

        private T value;

        private Runnable onUpdate;

        public CachedSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public T get() {
            if (this.value == null) {
                synchronized (this) {
                    this.value = (T) this.supplier.get();
                    Objects.requireNonNull(this.value);
                    if (this.onUpdate != null) {
                        this.onUpdate.run();
                    }
                }
            }
            return this.value;
        }

        public void invalidate() {
            this.value = null;
        }
    }
}