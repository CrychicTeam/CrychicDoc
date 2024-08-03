package cristelknight.wwoo.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult.PartialResult;
import cristelknight.wwoo.ExpandedEcosphere;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class Util {

    public static MutableComponent translatableText(String id) {
        return Component.translatable("expanded_ecosphere.config.text." + id);
    }

    public static <T> T readConfig(JsonElement load, Codec<T> codec, DynamicOps<JsonElement> ops) {
        DataResult<Pair<T, JsonElement>> decode = codec.decode(ops, load);
        Optional<PartialResult<Pair<T, JsonElement>>> error = decode.error();
        if (error.isPresent()) {
            throw new IllegalArgumentException("Couldn't read " + load.toString() + ", crashing instead.");
        } else {
            return (T) ((Pair) decode.result().orElseThrow()).getFirst();
        }
    }

    public static ExpandedEcosphere.Mode getMode(String mode) {
        if (!ExpandedEcosphere.isTerraBlenderLoaded()) {
            return ExpandedEcosphere.Mode.DEFAULT;
        } else {
            try {
                return ExpandedEcosphere.Mode.valueOf(mode.toUpperCase());
            } catch (IllegalArgumentException var2) {
                ExpandedEcosphere.LOGGER.warn("Invalid Mode '{}' for option '{}'", mode, "mode");
                return ExpandedEcosphere.Mode.DEFAULT;
            }
        }
    }

    @Nullable
    public static JsonObject getObjectFromPath(Path path) {
        InputStream im;
        try {
            if (path == null) {
                ExpandedEcosphere.LOGGER.error("Path was null");
                return null;
            }
            im = Files.newInputStream(path);
        } catch (IOException var8) {
            ExpandedEcosphere.LOGGER.error("Couldn't read " + path);
            return null;
        }
        try {
            InputStreamReader reader = new InputStreamReader(im);
            JsonObject var4;
            try {
                JsonElement el = JsonParser.parseReader(reader);
                if (!el.isJsonObject()) {
                    throw new RuntimeException("Input stream is on JsonElement");
                }
                var4 = el.getAsJsonObject();
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            reader.close();
            return var4;
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }
}