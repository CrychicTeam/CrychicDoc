package lio.playeranimatorapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.data.gson.AnimationSerializing;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import lio.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceReloadListener implements ResourceManagerReloadListener {

    private static final Logger logger = LogManager.getLogger(ModInit.class);

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        PlayerAnimations.animLengthsMap = new HashMap();
        PlayerAnimations.geckoMap = new HashMap();
        for (Entry<ResourceLocation, Resource> resource : resourceManager.listResources("player_animation", location -> location.getPath().endsWith(".json")).entrySet()) {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(JsonParser.parseReader(((Resource) resource.getValue()).openAsReader()), "resource");
                if (jsonObject.has("animations")) {
                    for (Entry<String, JsonElement> object : jsonObject.get("animations").getAsJsonObject().entrySet()) {
                        ResourceLocation resourceLocation = new ResourceLocation(((ResourceLocation) resource.getKey()).getNamespace(), ((String) object.getKey()).toLowerCase(Locale.ROOT));
                        PlayerAnimations.animLengthsMap.put(resourceLocation, ((JsonElement) object.getValue()).getAsJsonObject().get("animation_length").getAsFloat());
                        if (((JsonElement) object.getValue()).getAsJsonObject().has("geckoResource")) {
                            PlayerAnimations.geckoMap.put(resourceLocation, new ResourceLocation(((JsonElement) object.getValue()).getAsJsonObject().get("geckoResource").getAsString()));
                        }
                    }
                } else {
                    InputStream input = ((Resource) resource.getValue()).open();
                    try {
                        for (KeyframeAnimation animation : AnimationSerializing.deserializeAnimation(input)) {
                            PlayerAnimations.animLengthsMap.put(new ResourceLocation(((ResourceLocation) resource.getKey()).getNamespace(), PlayerAnimationRegistry.serializeTextToString((String) animation.extraData.get("name")).toLowerCase(Locale.ROOT)), (float) (animation.endTick / 20));
                            if (jsonObject.has("geckoResource")) {
                                PlayerAnimations.geckoMap.put(new ResourceLocation(((ResourceLocation) resource.getKey()).getNamespace(), PlayerAnimationRegistry.serializeTextToString((String) animation.extraData.get("name")).toLowerCase(Locale.ROOT)), new ResourceLocation(jsonObject.get("geckoResource").getAsString()));
                            }
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
                }
            } catch (NullPointerException | IOException var10) {
                logger.warn("Could not load animation resource " + ((ResourceLocation) resource.getKey()).toString() + " " + var10);
            }
        }
    }
}