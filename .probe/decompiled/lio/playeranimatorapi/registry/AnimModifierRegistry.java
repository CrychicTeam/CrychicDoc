package lio.playeranimatorapi.registry;

import com.google.gson.JsonObject;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import lio.playeranimatorapi.modifier.HeadPosBoundCamera;
import lio.playeranimatorapi.modifier.HeadRotBoundCamera;
import lio.playeranimatorapi.modifier.LengthModifier;
import lio.playeranimatorapi.modifier.MirrorOnAltHandModifier;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnimModifierRegistry {

    private static final Map<ResourceLocation, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier>> modifiers = new HashMap();

    public static void registerModifier(ResourceLocation ID, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier> function) {
        modifiers.put(ID, function);
    }

    public static Map<ResourceLocation, BiFunction<CustomModifierLayer, JsonObject, AbstractModifier>> getModifiers() {
        return modifiers;
    }

    public static void register() {
        registerModifier(new ResourceLocation("player-animator", "mirror"), (layer, json) -> new MirrorModifier());
        registerModifier(new ResourceLocation("player-animator", "speed"), (layer, json) -> {
            try {
                return new SpeedModifier(json.get("speed").getAsFloat());
            } catch (UnsupportedOperationException | IllegalArgumentException | NullPointerException var3) {
                return new SpeedModifier(1.0F);
            }
        });
        registerModifier(new ResourceLocation("playeranimatorapi", "length"), (layer, json) -> {
            try {
                return new LengthModifier(layer, json.get("desiredLength").getAsFloat());
            } catch (UnsupportedOperationException | IllegalArgumentException | NullPointerException var3) {
                return new LengthModifier(layer, -1.0F);
            }
        });
        registerModifier(new ResourceLocation("playeranimatorapi", "mirroronalthand"), (layer, json) -> new MirrorOnAltHandModifier(layer));
        registerModifier(new ResourceLocation("playeranimatorapi", "headposboundcamera"), (layer, json) -> new HeadPosBoundCamera(layer));
        registerModifier(new ResourceLocation("playeranimatorapi", "headrotboundcamera"), (layer, json) -> new HeadRotBoundCamera(layer));
    }
}