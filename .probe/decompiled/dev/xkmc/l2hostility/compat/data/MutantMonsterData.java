package dev.xkmc.l2hostility.compat.data;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import fuzs.mutantmonsters.init.ModRegistry;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class MutantMonsterData {

    public static void genConfig(ConfigDataProvider.Collector collector) {
        collector.add(L2Hostility.ENTITY, new ResourceLocation("mutantmonsters", "bosses"), new EntityConfig().put(EntityConfig.entity(0, 0, 0.0, 0.0, List.of((EntityType) ModRegistry.MUTANT_ZOMBIE_ENTITY_TYPE.get(), (EntityType) ModRegistry.MUTANT_SKELETON_ENTITY_TYPE.get(), (EntityType) ModRegistry.MUTANT_CREEPER_ENTITY_TYPE.get(), (EntityType) ModRegistry.MUTANT_ENDERMAN_ENTITY_TYPE.get())).minLevel(100).trait(List.of(EntityConfig.trait((MobTrait) LHTraits.REPRINT.get(), 1, 1, 150, 0.5F), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 1, 2, 200, 0.5F)))));
    }
}