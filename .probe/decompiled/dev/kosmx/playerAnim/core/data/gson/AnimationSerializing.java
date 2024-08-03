package dev.kosmx.playerAnim.core.data.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AnimationSerializing {

    public static final Gson SERIALIZER;

    public static List<KeyframeAnimation> deserializeAnimation(Reader stream) {
        return (List<KeyframeAnimation>) SERIALIZER.fromJson(stream, AnimationJson.getListedTypeToken());
    }

    public static List<KeyframeAnimation> deserializeAnimation(InputStream stream) throws IOException {
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        List var2;
        try {
            var2 = deserializeAnimation(reader);
        } catch (Throwable var5) {
            try {
                reader.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }
            throw var5;
        }
        reader.close();
        return var2;
    }

    public static String serializeAnimation(KeyframeAnimation animation) {
        return SERIALIZER.toJson(animation, KeyframeAnimation.class);
    }

    public static Writer writeAnimation(KeyframeAnimation animation, Writer writer) throws IOException {
        writer.write(serializeAnimation(animation));
        return writer;
    }

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        AnimationJson animationJson = new AnimationJson();
        builder.registerTypeAdapter(AnimationJson.getListedTypeToken(), animationJson);
        builder.registerTypeAdapter(KeyframeAnimation.class, animationJson);
        SERIALIZER = builder.create();
    }
}