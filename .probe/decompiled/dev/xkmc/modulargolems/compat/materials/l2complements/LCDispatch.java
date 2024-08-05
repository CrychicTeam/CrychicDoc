package dev.xkmc.modulargolems.compat.materials.l2complements;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.modulargolems.compat.materials.common.ModDispatch;
import dev.xkmc.modulargolems.events.event.GolemSweepEvent;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LCDispatch extends ModDispatch {

    public static final String MODID = "l2complements";

    public LCDispatch() {
        LCCompatRegistry.register();
        MinecraftForge.EVENT_BUS.register(LCDispatch.class);
    }

    @Override
    public void genLang(RegistrateLangProvider pvd) {
        pvd.add("golem_material.l2complements.totemic_gold", "Totemic Gold");
        pvd.add("golem_material.l2complements.poseidite", "Poseidite");
        pvd.add("golem_material.l2complements.shulkerate", "Shulkerate");
        pvd.add("golem_material.l2complements.eternium", "Eternium");
    }

    @Override
    public void genRecipe(RegistrateRecipeProvider pvd) {
        RecipeCategory var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCCompatRegistry.FORCE_FIELD.get(), 1)::m_126132_, (Item) LCItems.FORCE_FIELD.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) LCItems.FORCE_FIELD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) LCCompatRegistry.FREEZE_UP.get())::m_126132_, (Item) LCItems.HARD_ICE.get()).pattern("CAC").pattern("1B2").pattern("CAC").define('C', Items.GOLD_INGOT).define('A', (ItemLike) LCItems.HARD_ICE.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('1', new EnchantmentIngredient((Enchantment) LCEnchantments.ICE_THORN.get(), 1)).define('2', new EnchantmentIngredient((Enchantment) LCEnchantments.ICE_BLADE.get(), 1)).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) LCCompatRegistry.FLAME_UP.get())::m_126132_, (Item) LCItems.SOUL_FLAME.get()).pattern("CAC").pattern("1B2").pattern("CAC").define('C', Items.GOLD_INGOT).define('A', (ItemLike) LCItems.SOUL_FLAME.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('1', new EnchantmentIngredient((Enchantment) LCEnchantments.FLAME_THORN.get(), 1)).define('2', new EnchantmentIngredient((Enchantment) LCEnchantments.FLAME_BLADE.get(), 1)).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCCompatRegistry.TELEPORT_UP.get(), 1)::m_126132_, (Item) LCItems.VOID_EYE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) LCItems.VOID_EYE.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCCompatRegistry.ATK_UP.get(), 1)::m_126132_, (Item) LCItems.EXPLOSION_SHARD.get()).requires((ItemLike) GolemItems.QUARTZ.get()).requires((ItemLike) LCItems.EXPLOSION_SHARD.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        var10003 = RecipeCategory.MISC;
        safeUpgrade(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCCompatRegistry.SPEED_UP.get(), 1)::m_126132_, (Item) LCItems.CAPTURED_WIND.get()).requires((ItemLike) GolemItems.SPEED.get()).requires((ItemLike) LCItems.CAPTURED_WIND.get()).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) LCCompatRegistry.UPGRADE_CURSE.get())::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("A1A").pattern("CBC").pattern("A2A").define('C', Items.DRAGON_BREATH).define('A', (ItemLike) LCItems.CURSED_DROPLET.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('1', new EnchantmentIngredient((Enchantment) LCEnchantments.CURSE_BLADE.get(), 1)).define('2', Items.NETHER_STAR).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) LCCompatRegistry.UPGRADE_INCARCERATE.get())::m_126132_, (Item) LCItems.BLACKSTONE_CORE.get()).pattern("A1A").pattern("CBC").pattern("A2A").define('C', Items.DRAGON_BREATH).define('A', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('1', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1)).define('2', Items.NETHER_STAR).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
        safeUpgrade(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) LCCompatRegistry.UPGRADE_CLEANSE.get())::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("A1A").pattern("CBC").pattern("A2A").define('C', Items.DRAGON_BREATH).define('A', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('1', new EnchantmentIngredient((Enchantment) LCEnchantments.CURSE_BLADE.get(), 1)).define('2', Items.HEART_OF_THE_SEA).m_176498_(ConditionalRecipeWrapper.mod(pvd, "l2complements"));
    }

    @Override
    public ConfigDataProvider getDataGen(DataGenerator gen) {
        return new LCConfigGen(gen);
    }

    @Override
    public void dispatchClientSetup() {
        ForceFieldLayer.registerLayer();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onGolemSweep(GolemSweepEvent event) {
        int lv = event.getStack().getEnchantmentLevel((Enchantment) LCEnchantments.WIND_SWEEP.get());
        if (lv > 0) {
            double amount = LCConfig.COMMON.windSweepIncrement.get();
            event.setBox(event.getBox().inflate(amount * (double) lv));
        }
    }
}