package io.github.edwinmindcraft.origins.api.registry;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.badge.BadgeFactory;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;

public class OriginsBuiltinRegistries {

    public static final ResourceKey<Registry<BadgeFactory>> BADGE_FACTORY_KEY = ResourceKey.createRegistryKey(Origins.identifier("badge_factory"));

    public static Supplier<IForgeRegistry<Origin>> ORIGINS;

    public static Supplier<IForgeRegistry<BadgeFactory>> BADGE_FACTORIES;
}