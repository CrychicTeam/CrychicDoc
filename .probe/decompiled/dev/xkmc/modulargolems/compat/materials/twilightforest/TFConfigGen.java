package dev.xkmc.modulargolems.compat.materials.twilightforest;

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
import twilightforest.data.tags.ItemTagGenerator;

public class TFConfigGen extends ConfigDataProvider {

    public TFConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for Twilight Forest");
    }

    @Override
    public void add(ConfigDataProvider.Collector map) {
        map.add(ModularGolems.MATERIALS, new ResourceLocation("twilightforest", "twilightforest"), new GolemMaterialConfig().addMaterial(new ResourceLocation("twilightforest", "ironwood"), Ingredient.of(ItemTagGenerator.IRONWOOD_INGOTS)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 200.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addStat((GolemStatType) GolemTypes.STAT_REGEN.get(), 2.0).addModifier((GolemModifier) TFCompatRegistry.TF_DAMAGE.get(), 1).addModifier((GolemModifier) TFCompatRegistry.TF_HEALING.get(), 1).end().addMaterial(new ResourceLocation("twilightforest", "steeleaf"), Ingredient.of(ItemTagGenerator.STEELEAF_INGOTS)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 20.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 24.0).addModifier((GolemModifier) TFCompatRegistry.TF_DAMAGE.get(), 1).addModifier((GolemModifier) TFCompatRegistry.TF_HEALING.get(), 1).end().addMaterial(new ResourceLocation("twilightforest", "knightmetal"), Ingredient.of(ItemTagGenerator.KNIGHTMETAL_INGOTS)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 300.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 20.0).addStat((GolemStatType) GolemTypes.STAT_WEIGHT.get(), -0.4).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addModifier((GolemModifier) GolemModifiers.THORN.get(), 2).addModifier((GolemModifier) TFCompatRegistry.TF_DAMAGE.get(), 1).addModifier((GolemModifier) TFCompatRegistry.TF_HEALING.get(), 1).end().addMaterial(new ResourceLocation("twilightforest", "fiery"), Ingredient.of(ItemTagGenerator.FIERY_INGOTS)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 200.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 20.0).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).addModifier((GolemModifier) GolemModifiers.THORN.get(), 1).addModifier((GolemModifier) TFCompatRegistry.FIERY.get(), 1).addModifier((GolemModifier) TFCompatRegistry.TF_DAMAGE.get(), 1).addModifier((GolemModifier) TFCompatRegistry.TF_HEALING.get(), 1).end());
    }
}