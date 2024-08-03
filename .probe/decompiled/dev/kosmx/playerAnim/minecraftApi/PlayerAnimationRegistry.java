package dev.kosmx.playerAnim.minecraftApi;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationSerializing;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public final class PlayerAnimationRegistry {

    private static final HashMap<ResourceLocation, KeyframeAnimation> animations = new HashMap();

    @Nullable
    public static KeyframeAnimation getAnimation(@NotNull ResourceLocation identifier) {
        return (KeyframeAnimation) animations.get(identifier);
    }

    @NotNull
    public static Optional<KeyframeAnimation> getAnimationOptional(@NotNull ResourceLocation identifier) {
        return Optional.ofNullable(getAnimation(identifier));
    }

    public static Map<ResourceLocation, KeyframeAnimation> getAnimations() {
        return Map.copyOf(animations);
    }

    public static Map<String, KeyframeAnimation> getModAnimations(String modid) {
        HashMap<String, KeyframeAnimation> map = new HashMap();
        for (Entry<ResourceLocation, KeyframeAnimation> entry : animations.entrySet()) {
            if (((ResourceLocation) entry.getKey()).getNamespace().equals(modid)) {
                map.put(((ResourceLocation) entry.getKey()).getPath(), (KeyframeAnimation) entry.getValue());
            }
        }
        return map;
    }

    @Internal
    public static void resourceLoaderCallback(@NotNull ResourceManager manager, Logger logger) {
        animations.clear();
        for (Entry<ResourceLocation, Resource> resource : manager.listResources("player_animation", location -> location.getPath().endsWith(".json")).entrySet()) {
            try {
                InputStream input = ((Resource) resource.getValue()).open();
                try {
                    for (KeyframeAnimation animation : AnimationSerializing.deserializeAnimation(input)) {
                        animations.put(new ResourceLocation(((ResourceLocation) resource.getKey()).getNamespace(), serializeTextToString((String) animation.extraData.get("name")).toLowerCase(Locale.ROOT)), animation);
                    }
                } catch (Throwable var9) {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException var10) {
                logger.error("Error while loading payer animation: " + resource.getKey());
                logger.error(var10.getMessage());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                var10.printStackTrace(pw);
                String sStackTrace = sw.toString();
                logger.error(sStackTrace);
            }
        }
    }

    public static String serializeTextToString(String arg) {
        try {
            MutableComponent component = Component.Serializer.fromJson(arg);
            if (component != null) {
                return component.getString();
            }
        } catch (Exception var2) {
        }
        return arg.replace("\"", "");
    }
}