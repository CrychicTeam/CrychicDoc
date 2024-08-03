package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class LAItemMisc {

    public static final ItemEntry<Item> EXPLOSIVE_FUNGUS = L2Artifacts.REGISTRATE.item("explosive_fungus", p -> new Item(p.food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8F).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1), 1.0F).alwaysEat().fast().build()))).tag(new TagKey[] { Tags.Items.MUSHROOMS }).register();

    public static final ItemEntry<Item> PETRIFIED_FUNGUS = L2Artifacts.REGISTRATE.item("petrified_fungus", p -> new Item(p.food(new FoodProperties.Builder().nutrition(3).saturationMod(0.4F).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1), 1.0F).alwaysEat().build()))).tag(new TagKey[] { Tags.Items.MUSHROOMS }).register();

    public static final ItemEntry<Item> NUTRITIOUS_FUNGUS = L2Artifacts.REGISTRATE.item("nutritious_fungus", p -> new Item(p.food(new FoodProperties.Builder().nutrition(12).saturationMod(1.2F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 1), 1.0F).alwaysEat().fast().build()))).tag(new TagKey[] { Tags.Items.MUSHROOMS }).register();

    public static void register() {
    }
}