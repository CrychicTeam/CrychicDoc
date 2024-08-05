package io.github.apace100.origins.util;

import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.edwinmindcraft.apoli.api.ApoliAPI;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.HashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class PowerKeyManager {

    private static final HashMap<ResourceLocation, String> KEY_CACHE = new HashMap();

    public static void clearCache() {
        KEY_CACHE.clear();
    }

    public static String getKeyIdentifier(ResourceLocation powerId) {
        if (KEY_CACHE.containsKey(powerId)) {
            return (String) KEY_CACHE.get(powerId);
        } else {
            String key = getKeyFromPower(powerId);
            KEY_CACHE.put(powerId, key);
            return key;
        }
    }

    private static String getKeyFromPower(ResourceLocation powerId) {
        if (PowerTypeRegistry.contains(powerId)) {
            Registry<ConfiguredPower<?, ?>> powers = ApoliAPI.getPowers();
            ConfiguredPower<?, ?> powerType = powers.get(powerId);
            return powerType == null ? "" : (String) powerType.getKey(null).map(key -> key.key().equals("none") ? "key.origins.primary_active" : key.key()).or(() -> powerType.getContainedPowerKeys().stream().filter(powers::m_142003_).map(x -> getKeyFromPower(x.location())).filter(x -> !x.isBlank()).findFirst()).orElse("");
        } else {
            return "";
        }
    }
}