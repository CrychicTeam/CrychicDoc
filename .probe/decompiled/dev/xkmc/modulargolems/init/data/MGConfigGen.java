package dev.xkmc.modulargolems.init.data;

import dev.xkmc.l2itemselector.init.L2ItemSelector;
import dev.xkmc.l2itemselector.select.item.SimpleItemSelectConfig;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class MGConfigGen extends ConfigDataProvider {

    public MGConfigGen(DataGenerator generator) {
        super(generator, "Golem Config");
    }

    @Override
    public void add(ConfigDataProvider.Collector map) {
        map.add(L2ItemSelector.ITEM_SELECTOR, new ResourceLocation("modulargolems", "wand"), new SimpleItemSelectConfig().add(new ResourceLocation("modulargolems", "wand"), new Item[] { (Item) GolemItems.OMNI_COMMAND.get(), (Item) GolemItems.OMNI_DISPENSE.get(), (Item) GolemItems.OMNI_RETRIVAL.get(), (Item) GolemItems.OMNI_RIDER.get(), (Item) GolemItems.OMNI_SQUAD.get() }));
        map.add(ModularGolems.MATERIALS, new ResourceLocation("modulargolems", "vanilla"), new GolemMaterialConfig().addMaterial(new ResourceLocation("modulargolems", "copper"), Ingredient.of(Items.COPPER_INGOT)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 50.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addModifier((GolemModifier) GolemModifiers.THUNDER_IMMUNE.get(), 1).end().addMaterial(new ResourceLocation("modulargolems", "iron"), Ingredient.of(Items.IRON_INGOT)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 100.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).end().addMaterial(new ResourceLocation("modulargolems", "gold"), Ingredient.of(Items.GOLD_INGOT)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 20.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 5.0).addStat((GolemStatType) GolemTypes.STAT_WEIGHT.get(), -0.4).addStat((GolemStatType) GolemTypes.STAT_REGEN.get(), 4.0).end().addMaterial(new ResourceLocation("modulargolems", "netherite"), Ingredient.of(Items.NETHERITE_INGOT)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 300.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 30.0).addStat((GolemStatType) GolemTypes.STAT_WEIGHT.get(), -0.4).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addStat((GolemStatType) GolemTypes.STAT_SWEEP.get(), 2.0).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).end().addMaterial(new ResourceLocation("modulargolems", "sculk"), Ingredient.of(MGTagGen.SCULK_MATS)).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 500.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 30.0).addStat((GolemStatType) GolemTypes.STAT_SPEED.get(), 0.5).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addModifier((GolemModifier) GolemModifiers.SONIC.get(), 1).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).end());
        map.add(ModularGolems.PARTS, new ResourceLocation("modulargolems", "default"), new GolemPartConfig().addPart((GolemPart<?, ?>) GolemItems.GOLEM_BODY.get()).addFilter(StatFilterType.HEALTH, 0.5).addFilter(StatFilterType.ATTACK, 0.0).addFilter(StatFilterType.MOVEMENT, 0.0).addFilter(StatFilterType.MASS, 0.3).addFilter(StatFilterType.HEAD, 1.0).end().addPart((GolemPart<?, ?>) GolemItems.GOLEM_ARM.get()).addFilter(StatFilterType.HEALTH, 0.0).addFilter(StatFilterType.ATTACK, 0.5).addFilter(StatFilterType.MOVEMENT, 0.0).addFilter(StatFilterType.MASS, 0.2).addFilter(StatFilterType.HEAD, 0.0).end().addPart((GolemPart<?, ?>) GolemItems.GOLEM_LEGS.get()).addFilter(StatFilterType.HEALTH, 0.5).addFilter(StatFilterType.ATTACK, 0.0).addFilter(StatFilterType.MOVEMENT, 1.0).addFilter(StatFilterType.MASS, 0.3).addFilter(StatFilterType.HEAD, 0.0).end().addPart((GolemPart<?, ?>) GolemItems.HUMANOID_BODY.get()).addFilter(StatFilterType.HEALTH, 0.5).addFilter(StatFilterType.ATTACK, 0.0).addFilter(StatFilterType.MOVEMENT, 0.0).addFilter(StatFilterType.MASS, 0.4).addFilter(StatFilterType.HEAD, 1.0).end().addPart((GolemPart<?, ?>) GolemItems.HUMANOID_ARMS.get()).addFilter(StatFilterType.HEALTH, 0.0).addFilter(StatFilterType.ATTACK, 1.0).addFilter(StatFilterType.MOVEMENT, 0.0).addFilter(StatFilterType.MASS, 0.3).addFilter(StatFilterType.HEAD, 0.0).end().addPart((GolemPart<?, ?>) GolemItems.HUMANOID_LEGS.get()).addFilter(StatFilterType.HEALTH, 0.5).addFilter(StatFilterType.ATTACK, 0.0).addFilter(StatFilterType.MOVEMENT, 1.0).addFilter(StatFilterType.MASS, 0.3).addFilter(StatFilterType.HEAD, 0.0).end().addPart((GolemPart<?, ?>) GolemItems.DOG_BODY.get()).addFilter(StatFilterType.HEALTH, 0.7).addFilter(StatFilterType.ATTACK, 1.0).addFilter(StatFilterType.MOVEMENT, 0.0).addFilter(StatFilterType.MASS, 0.7).addFilter(StatFilterType.HEAD, 1.0).end().addPart((GolemPart<?, ?>) GolemItems.DOG_LEGS.get()).addFilter(StatFilterType.HEALTH, 0.3).addFilter(StatFilterType.ATTACK, 0.0).addFilter(StatFilterType.MOVEMENT, 1.0).addFilter(StatFilterType.MASS, 0.3).addFilter(StatFilterType.HEAD, 0.0).end().addEntity((GolemType<?, ?>) GolemTypes.TYPE_GOLEM.get()).end().addEntity((GolemType<?, ?>) GolemTypes.TYPE_HUMANOID.get()).addFilter((GolemStatType) GolemTypes.STAT_HEALTH.get(), 0.4).addFilter((GolemStatType) GolemTypes.STAT_ATTACK.get(), 0.3).addFilter((GolemStatType) GolemTypes.STAT_REGEN.get(), 0.5).end().addEntity((GolemType<?, ?>) GolemTypes.TYPE_DOG.get()).addFilter((GolemStatType) GolemTypes.STAT_HEALTH.get(), 0.2).addFilter((GolemStatType) GolemTypes.STAT_ATTACK.get(), 0.6).addFilter((GolemStatType) GolemTypes.STAT_REGEN.get(), 0.2).addFilter((GolemStatType) GolemTypes.STAT_SWEEP.get(), 0.0).end());
    }
}