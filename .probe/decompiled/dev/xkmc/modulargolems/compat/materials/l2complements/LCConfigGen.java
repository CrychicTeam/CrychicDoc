package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.l2complements.init.materials.LCMats;
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

public class LCConfigGen extends ConfigDataProvider {

    public LCConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for L2Complements");
    }

    @Override
    public void add(ConfigDataProvider.Collector collector) {
        collector.add(ModularGolems.MATERIALS, new ResourceLocation("l2complements", "l2complements"), new GolemMaterialConfig().addMaterial(new ResourceLocation("l2complements", "totemic_gold"), Ingredient.of(LCMats.TOTEMIC_GOLD.getIngot())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 100.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addStat((GolemStatType) GolemTypes.STAT_WEIGHT.get(), -0.4).addStat((GolemStatType) GolemTypes.STAT_REGEN.get(), 10.0).addModifier((GolemModifier) GolemModifiers.RECYCLE.get(), 1).addModifier((GolemModifier) LCCompatRegistry.TOTEMIC_GOLD.get(), 1).end().addMaterial(new ResourceLocation("l2complements", "poseidite"), Ingredient.of(LCMats.POSEIDITE.getIngot())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 200.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 20.0).addModifier((GolemModifier) GolemModifiers.SWIM.get(), 1).addModifier((GolemModifier) LCCompatRegistry.CONDUIT.get(), 1).addModifier((GolemModifier) LCCompatRegistry.POSEIDITE.get(), 1).end().addMaterial(new ResourceLocation("l2complements", "shulkerate"), Ingredient.of(LCMats.SHULKERATE.getIngot())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 1000.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addStat((GolemStatType) GolemTypes.STAT_WEIGHT.get(), -0.4).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addModifier((GolemModifier) GolemModifiers.DAMAGE_CAP.get(), 2).addModifier((GolemModifier) GolemModifiers.PROJECTILE_REJECT.get(), 1).end().addMaterial(new ResourceLocation("l2complements", "eternium"), Ingredient.of(LCMats.ETERNIUM.getIngot())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 1000.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addStat((GolemStatType) GolemTypes.STAT_REGEN.get(), 1000.0).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addModifier((GolemModifier) GolemModifiers.IMMUNITY.get(), 1).end());
    }
}