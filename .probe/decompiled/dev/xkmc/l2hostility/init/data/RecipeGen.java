package dev.xkmc.l2hostility.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.content.enchantment.core.EnchantmentRecipeBuilder;
import dev.xkmc.l2complements.content.recipe.BurntRecipeBuilder;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2hostility.compat.data.TFData;
import dev.xkmc.l2hostility.compat.gateway.GatewayConfigGen;
import dev.xkmc.l2hostility.content.traits.base.AttributeTrait;
import dev.xkmc.l2hostility.content.traits.base.SelfEffectTrait;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.AdaptingTrait;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import dev.xkmc.l2hostility.content.traits.common.FieryTrait;
import dev.xkmc.l2hostility.content.traits.common.GravityTrait;
import dev.xkmc.l2hostility.content.traits.common.InvisibleTrait;
import dev.xkmc.l2hostility.content.traits.common.ReflectTrait;
import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import dev.xkmc.l2hostility.content.traits.common.ShulkerTrait;
import dev.xkmc.l2hostility.content.traits.goals.CounterStrikeTrait;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.CorrosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.DrainTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ErosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.GrowthTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ReprintTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.SplitTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DementorTrait;
import dev.xkmc.l2hostility.content.traits.legendary.DispellTrait;
import dev.xkmc.l2hostility.content.traits.legendary.KillerAuraTrait;
import dev.xkmc.l2hostility.content.traits.legendary.PullingTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RagnarokTrait;
import dev.xkmc.l2hostility.content.traits.legendary.RepellingTrait;
import dev.xkmc.l2hostility.content.traits.legendary.UndyingTrait;
import dev.xkmc.l2hostility.init.registrate.LHBlocks;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import java.lang.invoke.StringConcatFactory;
import java.util.function.BiFunction;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeGen {

    private static final String currentFolder = "";

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        RecipeCategory var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHBlocks.BURST_SPAWNER.get(), 1)::m_126132_, Items.NETHER_STAR).pattern("ADA").pattern("BCB").pattern("ABA").define('C', Items.NETHER_STAR).define('B', LCItems.EXPLOSION_SHARD).define('A', Items.NETHERITE_INGOT).define('D', LCItems.CURSED_DROPLET).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.DETECTOR.get(), 1)::m_126132_, Items.LIGHTNING_ROD).pattern("ADA").pattern("BCB").pattern("ABA").define('A', Items.ROTTEN_FLESH).define('B', Items.BONE).define('C', Items.IRON_INGOT).define('D', Items.LIGHTNING_ROD).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.DETECTOR_GLASSES.get(), 1)::m_126132_, Items.ENDER_EYE).pattern("ADA").define('A', Items.ENDER_EYE).define('D', Items.IRON_INGOT).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.BOTTLE_SANITY.get(), 3)::m_126132_, (Item) LHItems.HOSTILITY_ORB.get()).requires((ItemLike) LHItems.HOSTILITY_ORB.get()).requires(Items.GLASS_BOTTLE, 3).save(pvd, getID((Item) LHItems.BOTTLE_SANITY.get(), "_craft"));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.BOTTLE_SANITY.get(), 3)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).requires((ItemLike) LHItems.BOTTLE_SANITY.get()).requires(LCItems.LIFE_ESSENCE).requires(Items.GLASS_BOTTLE, 3).save(pvd, getID((Item) LHItems.BOTTLE_SANITY.get(), "_renew"));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.BOTTLE_CURSE.get(), 3)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).requires(LCItems.CURSED_DROPLET).requires(Items.GLASS_BOTTLE, 3).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.BOOSTER_POTION.get(), 1)::m_126132_, (Item) LHItems.WITCH_DROPLET.get()).requires(LHItems.WITCH_DROPLET).requires(LHItems.BOTTLE_SANITY).requires(LCItems.LIFE_ESSENCE).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.WITCH_CHARGE.get(), 1)::m_126132_, (Item) LHItems.WITCH_DROPLET.get()).requires(LHItems.WITCH_DROPLET).requires(LCItems.CURSED_DROPLET).requires(Items.GUNPOWDER).requires(Items.BLAZE_POWDER).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.ETERNAL_WITCH_CHARGE.get(), 1)::m_126132_, (Item) LHItems.WITCH_DROPLET.get()).pattern("ABA").pattern("BCB").pattern("DBD").define('A', Items.GUNPOWDER).define('D', Items.BLAZE_POWDER).define('B', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('C', LHItems.WITCH_DROPLET).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LHItems.BOOK_OMNISCIENCE.get(), 1)::m_126132_, (Item) LHItems.BOOK_COPY.get()).requires((ItemLike) LHItems.BOOK_COPY.get()).requires(((ReprintTrait) LHTraits.REPRINT.get()).m_5456_()).requires(((SplitTrait) LHTraits.SPLIT.get()).m_5456_()).requires(Items.NETHER_STAR).m_176498_(pvd);
        pvd.storage(LHItems.CHAOS_INGOT, RecipeCategory.MISC, LHBlocks.CHAOS);
        pvd.storage(LHItems.MIRACLE_INGOT, RecipeCategory.MISC, LHBlocks.MIRACLE);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHBlocks.HOSTILITY_BEACON.get(), 1)::m_126132_, (Item) LHItems.HOSTILITY_ESSENCE.get()).pattern("E3E").pattern("1B1").pattern("C2C").define('B', Items.BEACON).define('C', Items.CRYING_OBSIDIAN).define('E', LHItems.HOSTILITY_ESSENCE).define('1', (ItemLike) LHTraits.KILLER_AURA.get()).define('2', (ItemLike) LHTraits.GRAVITY.get()).define('3', (ItemLike) LHTraits.DRAIN.get()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CHAOS_INGOT.get(), 1)::m_126132_, (Item) LHItems.HOSTILITY_ORB.get()).pattern("B4B").pattern("1A2").pattern("B3B").define('A', (ItemLike) LHItems.HOSTILITY_ORB.get()).define('B', (ItemLike) LHItems.BOTTLE_CURSE.get()).define('1', (ItemLike) LCItems.SOUL_FLAME.get()).define('2', (ItemLike) LCItems.HARD_ICE.get()).define('3', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('4', (ItemLike) LCItems.CAPTURED_WIND.get()).m_176498_(pvd);
        convert(pvd, (Item) LHItems.BOTTLE_CURSE.get(), (Item) LHItems.HOSTILITY_ESSENCE.get(), 512);
        recycle(pvd, LHTagGen.CHAOS_CURIO, (Item) LHItems.CHAOS_INGOT.get(), 1.0F);
        recycle(pvd, LHTagGen.TRAIT_ITEM, (Item) LHItems.MIRACLE_POWDER.get(), 1.0F);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.MIRACLE_INGOT.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("ABA").pattern("ACA").pattern("ABA").define('C', (ItemLike) LHItems.CHAOS_INGOT.get()).define('B', (ItemLike) LHItems.HOSTILITY_ESSENCE.get()).define('A', (ItemLike) LHItems.MIRACLE_POWDER.get()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.LOOT_1.get(), 1)::m_126132_, Items.EMERALD).pattern(" A ").pattern("DID").pattern(" A ").define('I', Items.EMERALD).define('A', Items.GOLD_INGOT).define('D', Items.COPPER_INGOT).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.LOOT_2.get(), 1)::m_126132_, Items.DIAMOND).pattern(" A ").pattern("DID").pattern(" A ").define('I', Items.DIAMOND).define('A', Items.BLAZE_POWDER).define('D', Items.DRAGON_BREATH).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.LOOT_3.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern(" A ").pattern("DID").pattern(" A ").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', LCItems.LIFE_ESSENCE).define('D', LHItems.WITCH_DROPLET).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.LOOT_4.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern(" A ").pattern("DID").pattern(" A ").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('A', LCItems.BLACKSTONE_CORE).define('D', LCItems.FORCE_FIELD).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_SLOTH.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("B1B").pattern("CIC").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('1', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1)).define('B', Items.COPPER_INGOT).define('C', (ItemLike) LHItems.BOTTLE_SANITY.get()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_ENVY.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("B1B").pattern("CIC").pattern("B2B").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1)).define('2', new EnchantmentIngredient(Enchantments.SILK_TOUCH, 1)).define('B', Items.PRISMARINE_SHARD).define('C', Items.ENDER_EYE).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_LUST.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("B1B").pattern("CID").pattern("B2B").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1)).define('2', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1)).define('B', Items.PHANTOM_MEMBRANE).define('C', ((RegenTrait) LHTraits.REGEN.get()).m_5456_()).define('D', ((InvisibleTrait) LHTraits.INVISIBLE.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_GREED.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("B1B").pattern("CID").pattern("B2B").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1)).define('2', new EnchantmentIngredient(Enchantments.BLOCK_FORTUNE, 1)).define('B', Items.GOLD_INGOT).define('C', ((AttributeTrait) LHTraits.SPEEDY.get()).m_5456_()).define('D', ((AttributeTrait) LHTraits.TANK.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_GLUTTONY.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("B1B").pattern("CID").pattern("B2B").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('1', new EnchantmentIngredient(Enchantments.MOB_LOOTING, 1)).define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1)).define('B', Items.NETHERITE_INGOT).define('C', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).define('D', ((TargetEffectTrait) LHTraits.WITHER.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_WRATH.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("314").pattern("5I6").pattern("B2B").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('B', (ItemLike) LHItems.HOSTILITY_ESSENCE.get()).define('1', ((FieryTrait) LHTraits.FIERY.get()).m_5456_()).define('2', ((ReprintTrait) LHTraits.REPRINT.get()).m_5456_()).define('3', ((ShulkerTrait) LHTraits.SHULKER.get()).m_5456_()).define('4', ((ShulkerTrait) LHTraits.GRENADE.get()).m_5456_()).define('5', ((CounterStrikeTrait) LHTraits.STRIKE.get()).m_5456_()).define('6', ((ReflectTrait) LHTraits.REFLECT.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.CURSE_PRIDE.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("515").pattern("3I4").pattern("B2B").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('B', (ItemLike) LHItems.HOSTILITY_ESSENCE.get()).define('1', ((KillerAuraTrait) LHTraits.KILLER_AURA.get()).m_5456_()).define('2', ((SelfEffectTrait) LHTraits.PROTECTION.get()).m_5456_()).define('3', ((DementorTrait) LHTraits.DEMENTOR.get()).m_5456_()).define('4', ((AdaptingTrait) LHTraits.ADAPTIVE.get()).m_5456_()).define('5', ((GrowthTrait) LHTraits.GROWTH.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_OCEAN.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.GUARDIAN_EYE.get()).define('B', LCMats.POSEIDITE.getIngot()).define('D', ((TargetEffectTrait) LHTraits.CONFUSION.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_LIFE.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', ((UndyingTrait) LHTraits.UNDYING.get()).m_5456_()).define('B', LCMats.SHULKERATE.getIngot()).define('D', ((RepellingTrait) LHTraits.REPELLING.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_HEALING.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', Items.GHAST_TEAR).define('B', LCMats.TOTEMIC_GOLD.getIngot()).define('D', ((RegenTrait) LHTraits.REGEN.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_DIVINITY.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('B', LCMats.TOTEMIC_GOLD.getIngot()).define('D', ((DispellTrait) LHTraits.DISPELL.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_REFLECTION.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("1A2").pattern("DID").pattern("3A4").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.FORCE_FIELD.get()).define('1', ((TargetEffectTrait) LHTraits.POISON.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.SLOWNESS.get()).m_5456_()).define('3', ((TargetEffectTrait) LHTraits.CONFUSION.get()).m_5456_()).define('4', ((TargetEffectTrait) LHTraits.BLIND.get()).m_5456_()).define('D', ((ReflectTrait) LHTraits.REFLECT.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_CORROSION.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', ((CorrosionTrait) LHTraits.CORROSION.get()).m_5456_()).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('D', ((ErosionTrait) LHTraits.EROSION.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RING_INCARCERATION.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("1I2").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('1', ((TargetEffectTrait) LHTraits.SLOWNESS.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.FREEZING.get()).m_5456_()).define('A', ((KillerAuraTrait) LHTraits.KILLER_AURA.get()).m_5456_()).define('B', (ItemLike) LCItems.BLACKSTONE_CORE.get()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.FLAMING_THORN.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("DID").pattern("BAB").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', ((DrainTrait) LHTraits.DRAIN.get()).m_5456_()).define('B', (ItemLike) LCItems.WARDEN_BONE_SHARD.get()).define('D', ((TargetEffectTrait) LHTraits.SOUL_BURNER.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.WITCH_WAND.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("123").pattern("7I4").pattern("S65").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('S', Items.STICK).define('1', ((TargetEffectTrait) LHTraits.POISON.get()).m_5456_()).define('2', ((TargetEffectTrait) LHTraits.WITHER.get()).m_5456_()).define('3', ((TargetEffectTrait) LHTraits.SLOWNESS.get()).m_5456_()).define('4', ((TargetEffectTrait) LHTraits.WEAKNESS.get()).m_5456_()).define('5', ((TargetEffectTrait) LHTraits.LEVITATION.get()).m_5456_()).define('6', ((TargetEffectTrait) LHTraits.FREEZING.get()).m_5456_()).define('7', ((TargetEffectTrait) LHTraits.CURSED.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.INFINITY_GLOVE.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern("BAB").pattern("III").pattern("DID").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', ((SplitTrait) LHTraits.SPLIT.get()).m_5456_()).define('B', ((EnderTrait) LHTraits.ENDER.get()).m_5456_()).define('D', ((PullingTrait) LHTraits.PULLING.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.ODDEYES_GLASSES.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern(" A ").pattern("1I2").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', Items.GOLD_INGOT).define('1', Items.CYAN_STAINED_GLASS_PANE).define('2', Items.MAGENTA_STAINED_GLASS_PANE).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.TRIPLE_STRIP_CAPE.get(), 1)::m_126132_, (Item) LHItems.CHAOS_INGOT.get()).pattern(" I ").pattern("CCC").pattern("FFF").define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('C', ItemTags.BANNERS).define('F', (ItemLike) LCItems.RESONANT_FEATHER.get()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.ABRAHADABRA.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("AIA").pattern("EOE").pattern("BIC").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('E', LCMats.ETERNIUM.getIngot()).define('O', (ItemLike) LHItems.RING_REFLECTION.get()).define('A', ((RagnarokTrait) LHTraits.RAGNAROK.get()).m_5456_()).define('B', ((RepellingTrait) LHTraits.REPELLING.get()).m_5456_()).define('C', ((PullingTrait) LHTraits.PULLING.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.NIDHOGGUR.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("AIA").pattern("EOE").pattern("BIB").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('E', LCMats.ETERNIUM.getIngot()).define('O', (ItemLike) LHItems.CURSE_GREED.get()).define('A', ((RagnarokTrait) LHTraits.RAGNAROK.get()).m_5456_()).define('B', ((PullingTrait) LHTraits.PULLING.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.PLATINUM_STAR.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("BIB").pattern("ISI").pattern("BIB").define('S', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('B', ((KillerAuraTrait) LHTraits.KILLER_AURA.get()).m_5456_()).define('I', Items.NETHER_STAR).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.RESTORATION.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("BLB").pattern("SIS").pattern("BGB").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('B', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('S', ((DispellTrait) LHTraits.DISPELL.get()).m_5456_()).define('L', ((AuraEffectTrait) LHTraits.MOONWALK.get()).m_5456_()).define('G', ((GravityTrait) LHTraits.GRAVITY.get()).m_5456_()).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.ABYSSAL_THORN.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("AIA").pattern("IEI").pattern("XIX").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('E', LCMats.ETERNIUM.getIngot()).define('A', LCItems.GUARDIAN_EYE).define('X', LCItems.BLACKSTONE_CORE).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.DIVINITY_CROSS.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("STS").pattern("TIT").pattern("ETA").define('I', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('T', LCItems.LIFE_ESSENCE).define('E', ((DrainTrait) LHTraits.DRAIN.get()).m_5456_()).define('A', ((KillerAuraTrait) LHTraits.KILLER_AURA.get()).m_5456_()).define('S', LHItems.WITCH_DROPLET).m_176498_(pvd);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LHItems.DIVINITY_LIGHT.get(), 1)::m_126132_, (Item) LHItems.MIRACLE_INGOT.get()).pattern("STS").pattern("TIT").pattern("ETA").define('T', (ItemLike) LHItems.MIRACLE_INGOT.get()).define('I', LHItems.CURSE_SLOTH).define('E', ((GravityTrait) LHTraits.GRAVITY.get()).m_5456_()).define('A', ((KillerAuraTrait) LHTraits.KILLER_AURA.get()).m_5456_()).define('S', LHItems.HOSTILITY_ORB).m_176498_(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LHEnchantments.INSULATOR.get(), 1)::unlockedBy, (Item) LHItems.CHAOS_INGOT.get()).pattern("AIA").pattern("DBD").pattern("ACA").define('B', Items.BOOK).define('D', Items.LAPIS_LAZULI).define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.FORCE_FIELD.get()).define('C', (ItemLike) LHItems.BOTTLE_SANITY.get()).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LHEnchantments.SPLIT_SUPPRESS.get(), 1)::unlockedBy, (Item) LHItems.CHAOS_INGOT.get()).pattern("AIA").pattern("DBD").pattern("ACA").define('B', Items.BOOK).define('D', Items.LAPIS_LAZULI).define('I', (ItemLike) LHItems.CHAOS_INGOT.get()).define('A', (ItemLike) LCItems.GUARDIAN_EYE.get()).define('C', (ItemLike) LHItems.BOTTLE_SANITY.get()).save(pvd);
        if (ModList.get().isLoaded("twilightforest")) {
            TFData.genRecipe(pvd);
        }
        if (ModList.get().isLoaded("cataclysm")) {
            CataclysmData.genRecipe(pvd);
        }
        if (ModList.get().isLoaded("gateways")) {
            GatewayConfigGen.genRecipe(pvd);
        }
    }

    private static ResourceLocation getID(Enchantment item) {
        return new ResourceLocation("l2hostility", StringConcatFactory.makeConcatWithConstants < "makeConcatWithConstants", "\u0001" > (ForgeRegistries.ENCHANTMENTS.getKey(item).getPath()));
    }

    private static ResourceLocation getID(Enchantment item, String suffix) {
        return new ResourceLocation("l2hostility", ForgeRegistries.ENCHANTMENTS.getKey(item).getPath() + suffix);
    }

    public static ResourceLocation getID(ItemLike item) {
        return new ResourceLocation("l2hostility", StringConcatFactory.makeConcatWithConstants < "makeConcatWithConstants", "\u0001" > (ForgeRegistries.ITEMS.getKey(item.asItem()).getPath()));
    }

    private static ResourceLocation getID(Item item, String suffix) {
        return new ResourceLocation("l2hostility", ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
    }

    private static void storage(RegistrateRecipeProvider pvd, ItemEntry<?> nugget, ItemEntry<?> ingot, BlockEntry<?> block) {
        storage(pvd, nugget::get, ingot::get);
        storage(pvd, ingot::get, block::get);
    }

    public static void storage(RegistrateRecipeProvider pvd, NonNullSupplier<ItemLike> from, NonNullSupplier<ItemLike> to) {
        RecipeCategory var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) to.get(), 1)::m_126132_, ((ItemLike) from.get()).asItem()).pattern("XXX").pattern("XXX").pattern("XXX").define('X', (ItemLike) from.get()).m_176500_(pvd, getID(((ItemLike) to.get()).asItem()) + "_storage");
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) from.get(), 9)::m_126132_, ((ItemLike) to.get()).asItem()).requires((ItemLike) to.get()).m_176500_(pvd, getID(((ItemLike) to.get()).asItem()) + "_unpack");
    }

    public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }

    public static void smithing(RegistrateRecipeProvider pvd, TagKey<Item> in, Item mat, Item out) {
        Ingredient var10001 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(var10001, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::m_266439_, mat).save(pvd, getID(out));
    }

    public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
        Ingredient var10001 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(var10001, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::m_266439_, mat).save(pvd, getID(out));
    }

    public static void smelting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
        unlock(pvd, SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::m_126132_, source).save(pvd, getID(source));
    }

    public static void blasting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
        unlock(pvd, SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::m_126132_, source).save(pvd, getID(source));
    }

    private static void convert(RegistrateRecipeProvider pvd, Item in, Item out, int count) {
        unlock(pvd, new BurntRecipeBuilder(Ingredient.of(in), out.getDefaultInstance(), count)::unlockedBy, in).save(pvd, getID(out));
    }

    public static void recycle(RegistrateRecipeProvider pvd, TagKey<Item> source, Item result, float experience) {
        unlock(pvd, SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::m_126132_, result).save(pvd, getID(result, "_recycle"));
    }
}