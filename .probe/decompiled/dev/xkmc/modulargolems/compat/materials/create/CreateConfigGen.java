package dev.xkmc.modulargolems.compat.materials.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.modulargolems.content.config.GolemMaterialConfig;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.Objects;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class CreateConfigGen extends ConfigDataProvider {

    public CreateConfigGen(DataGenerator generator) {
        super(generator, "Golem Config for Create");
    }

    @Override
    public void add(ConfigDataProvider.Collector map) {
        ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
        map.add(ModularGolems.MATERIALS, new ResourceLocation("create", "create"), new GolemMaterialConfig().addMaterial(new ResourceLocation("create", "zinc"), Ingredient.of(AllTags.forgeItemTag("ingots/zinc"))).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 50.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 10.0).addModifier((GolemModifier) CreateCompatRegistry.COATING.get(), 1).end().addMaterial(new ResourceLocation("create", "andesite_alloy"), Ingredient.of((ItemLike) AllItems.ANDESITE_ALLOY.get())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 30.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 5.0).addModifier((GolemModifier) CreateCompatRegistry.BODY.get(), 1).addModifier((GolemModifier) CreateCompatRegistry.MOBILE.get(), 1).addModifier((GolemModifier) CreateCompatRegistry.FORCE.get(), 1).addModifier((GolemModifier) GolemModifiers.MAGIC_RES.get(), 1).end().addMaterial(new ResourceLocation("create", "brass"), Ingredient.of(AllTags.forgeItemTag("ingots/brass"))).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 150.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 0.5).addModifier((GolemModifier) CreateCompatRegistry.BODY.get(), 1).addModifier((GolemModifier) CreateCompatRegistry.MOBILE.get(), 1).addModifier((GolemModifier) CreateCompatRegistry.FORCE.get(), 1).addModifier((GolemModifier) GolemModifiers.MAGIC_RES.get(), 2).end().addMaterial(new ResourceLocation("create", "railway"), Ingredient.of((ItemLike) AllBlocks.RAILWAY_CASING.get())).addStat((GolemStatType) GolemTypes.STAT_HEALTH.get(), 300.0).addStat((GolemStatType) GolemTypes.STAT_ATTACK.get(), 15.0).addStat((GolemStatType) GolemTypes.STAT_SWEEP.get(), 1.0).addStat((GolemStatType) GolemTypes.STAT_KBRES.get(), 1.0).addStat((GolemStatType) GolemTypes.STAT_ATKKB.get(), 1.0).addModifier((GolemModifier) CreateCompatRegistry.BODY.get(), 1).addModifier((GolemModifier) CreateCompatRegistry.MOBILE.get(), 2).addModifier((GolemModifier) CreateCompatRegistry.FORCE.get(), 2).addModifier((GolemModifier) GolemModifiers.MAGIC_RES.get(), 2).addModifier((GolemModifier) GolemModifiers.FIRE_IMMUNE.get(), 1).end());
    }
}