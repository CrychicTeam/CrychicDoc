package dev.xkmc.modulargolems.compat.materials.blazegear;

import com.flashfyre.blazegear.registry.BGItems;
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

public class BGConfigGen extends ConfigDataProvider {

    public BGConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for BlazeGear");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        collector.add(ModularGolems.MATERIALS, new ResourceLocation("blazegear", "blazegear"), new GolemMaterialConfig().addMaterial(new ResourceLocation("blazegear", "brimsteel"), Ingredient.of((ItemLike) BGItems.BRIMSTEEL_INGOT.get())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 100.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).addModifier((GolemModifier) BGCompatRegistry.BLAZING.get(), 1).end());
    }
}