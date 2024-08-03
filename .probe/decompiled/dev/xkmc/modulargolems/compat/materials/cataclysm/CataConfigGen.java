package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.init.ModItems;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class CataConfigGen extends ConfigDataProvider {

    public CataConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for Cataclysm");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        collector.add(ModularGolems.MATERIALS, new ResourceLocation("cataclysm", "cataclysm"), new GolemMaterialConfig().addMaterial(new ResourceLocation("cataclysm", "ignitium"), Ingredient.of((ItemLike) ModItems.IGNITIUM_INGOT.get())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 450.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 20.0).addStat((GolemStatType) GolemTypes.STAT_SWEEP.get(), 2.0).addStat((GolemStatType) GolemTypes.STAT_RANGE.get(), 1.0).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).addModifier((GolemModifier) GolemModifiers.DAMAGE_CAP.get(), 3).addModifier((GolemModifier) CataCompatRegistry.IGNIS_FIREBALL.get(), 2).addModifier((GolemModifier) CataCompatRegistry.IGNIS_ATTACK.get(), 1).end().addMaterial(new ResourceLocation("cataclysm", "witherite"), Ingredient.of((ItemLike) ModItems.WITHERITE_INGOT.get())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 390.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 20.0).addStat((GolemStatType) GolemTypes.STAT_REGEN.get(), 2.0).addStat((GolemStatType) GolemTypes.STAT_SWEEP.get(), 2.0).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).addModifier((GolemModifier) GolemModifiers.DAMAGE_CAP.get(), 2).addModifier((GolemModifier) GolemModifiers.PROJECTILE_REJECT.get(), 1).addModifier((GolemModifier) CataCompatRegistry.HARBINGER_BEAM.get(), 1).addModifier((GolemModifier) CataCompatRegistry.HARBINGER_MISSILE.get(), 1).end());
    }
}