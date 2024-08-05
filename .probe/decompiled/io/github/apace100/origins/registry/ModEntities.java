package io.github.apace100.origins.registry;

import io.github.apace100.origins.entity.EnderianPearlEntity;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final RegistryObject<EntityType<EnderianPearlEntity>> ENDERIAN_PEARL = register("enderian_pearl", () -> EntityType.Builder.of(EnderianPearlEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        return OriginRegisters.ENTITY_TYPES.register(name, () -> ((EntityType.Builder) builder.get()).build("origins:" + name));
    }

    public static void register() {
    }
}