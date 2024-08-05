package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.botania.common.item.BotaniaItems;

public class BotConfigGen extends ConfigDataProvider {

    public BotConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for Botania");
    }

    @Override
    public void add(ConfigDataProvider.Collector map) {
        map.add(ModularGolems.MATERIALS, new ResourceLocation("botania", "botania"), new GolemMaterialConfig().addMaterial(new ResourceLocation("botania", "manasteel"), Ingredient.of(BotaniaItems.manaSteel)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 150.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).addModifier((GolemModifier) BotCompatRegistry.MANA_MENDING.get(), 1).addModifier((GolemModifier) BotCompatRegistry.MANA_BOOSTING.get(), 1).end().addMaterial(new ResourceLocation("botania", "terrasteel"), Ingredient.of(BotaniaItems.terrasteel)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 300.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 30.0).addModifier((GolemModifier) BotCompatRegistry.MANA_MENDING.get(), 2).addModifier((GolemModifier) BotCompatRegistry.MANA_BOOSTING.get(), 2).addModifier((GolemModifier) BotCompatRegistry.MANA_PRODUCTION.get(), 1).addModifier((GolemModifier) BotCompatRegistry.MANA_BURST.get(), 1).end().addMaterial(new ResourceLocation("botania", "elementium"), Ingredient.of(BotaniaItems.elementium)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 200.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).addModifier((GolemModifier) BotCompatRegistry.MANA_MENDING.get(), 1).addModifier((GolemModifier) BotCompatRegistry.MANA_BOOSTING.get(), 1).addModifier((GolemModifier) BotCompatRegistry.PIXIE_ATTACK.get(), 1).addModifier((GolemModifier) BotCompatRegistry.PIXIE_COUNTERATTACK.get(), 1).end());
    }
}