package com.craisinlord.integrated_api.modinit;

import com.craisinlord.integrated_api.modinit.registry.CustomRegistry;
import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class IAConditionsRegistry {

    public static final ResourceKey<Registry<Supplier<Boolean>>> IA_JSON_CONDITIONS_KEY = ResourceKey.createRegistryKey(new ResourceLocation("integrated_api", "json_conditions"));

    public static final CustomRegistry<Supplier<Boolean>> IA_JSON_CONDITIONS_REGISTRY = CustomRegistry.of("integrated_api", IA_JSON_CONDITIONS_KEY, false, false, true);

    public static final RegistryEntry<Supplier<Boolean>> ALWAYS_TRUE = IA_JSON_CONDITIONS_REGISTRY.register("always_true", () -> () -> true);

    public static final RegistryEntry<Supplier<Boolean>> ALWAYS_FALSE = IA_JSON_CONDITIONS_REGISTRY.register("always_false", () -> () -> true);

    private IAConditionsRegistry() {
    }
}