package dev.xkmc.l2archery.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2archery.compat.JeedHelper;
import dev.xkmc.l2archery.content.crafting.BowBuilder;
import dev.xkmc.l2archery.content.enchantment.PotionArrowEnchantment;
import dev.xkmc.l2archery.content.item.GenericArrowItem;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.content.upgrade.BowUpgradeBuilder;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.init.registrate.ArcheryEnchantments;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2complements.content.enchantment.core.EnchantmentRecipeBuilder;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.compat.jeed.JeedDataGenerator;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2library.serial.recipe.RecordRecipeFinished;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeGen {

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        cross(pvd, Items.IRON_NUGGET, Items.ARROW, (Item) ArcheryItems.STARTER_ARROW.get(), 4);
        cross(pvd, Items.COPPER_INGOT, (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.COPPER_ARROW.get(), 4);
        cross(pvd, Items.IRON_INGOT, (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.IRON_ARROW.get(), 4);
        cross(pvd, Items.GOLD_INGOT, (Item) ArcheryItems.IRON_ARROW.get(), (Item) ArcheryItems.GOLD_ARROW.get(), 4);
        cross(pvd, Items.OBSIDIAN, (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.OBSIDIAN_ARROW.get(), 4);
        cross(pvd, Items.DIAMOND, (Item) ArcheryItems.OBSIDIAN_ARROW.get(), (Item) ArcheryItems.DIAMOND_ARROW.get(), 4);
        cross(pvd, (Item) LCItems.EXPLOSION_SHARD.get(), (Item) ArcheryItems.OBSIDIAN_ARROW.get(), (Item) ArcheryItems.DESTROYER_ARROW.get(), 4);
        cross(pvd, (Item) ArcheryItems.OBSIDIAN_ARROW.get(), Items.QUARTZ, (Item) ArcheryItems.QUARTZ_ARROW.get(), 1);
        cross(pvd, (Item) ArcheryItems.STARTER_ARROW.get(), Items.WITHER_ROSE, (Item) ArcheryItems.WITHER_ARROW.get(), 1);
        cross(pvd, (Item) ArcheryItems.STARTER_ARROW.get(), Items.BLACKSTONE, (Item) ArcheryItems.BLACKSTONE_ARROW.get(), 1);
        cross(pvd, Items.PHANTOM_MEMBRANE, (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.NO_FALL_ARROW.get(), 4);
        cross(pvd, Items.BLUE_ICE, (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.ICE_ARROW.get(), 4);
        full(pvd, Items.SOUL_SOIL, (Item) ArcheryItems.STARTER_ARROW.get(), Items.GUNPOWDER, (Item) ArcheryItems.FIRE_1_ARROW.get(), 4);
        full(pvd, Items.GHAST_TEAR, (Item) ArcheryItems.FIRE_1_ARROW.get(), Items.BLAZE_ROD, (Item) ArcheryItems.FIRE_2_ARROW.get(), 4);
        full(pvd, Items.TNT, (Item) ArcheryItems.STARTER_ARROW.get(), Items.FIRE_CHARGE, (Item) ArcheryItems.TNT_1_ARROW.get(), 4);
        full(pvd, Items.END_CRYSTAL, (Item) ArcheryItems.TNT_1_ARROW.get(), Items.TNT, (Item) ArcheryItems.TNT_2_ARROW.get(), 4);
        full(pvd, Items.CREEPER_HEAD, (Item) ArcheryItems.TNT_2_ARROW.get(), Items.END_CRYSTAL, (Item) ArcheryItems.TNT_3_ARROW.get(), 4);
        full(pvd, Items.OBSIDIAN, Items.END_ROD, Items.ENDER_EYE, (Item) ArcheryItems.ENDER_ARROW.get(), 4);
        full(pvd, Items.PHANTOM_MEMBRANE, (Item) ArcheryItems.STARTER_ARROW.get(), Items.GUNPOWDER, (Item) ArcheryItems.STORM_ARROW.get(), 4);
        cross(pvd, (Item) LCItems.RESONANT_FEATHER.get(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.DISPELL_ARROW.get(), 4);
        full(pvd, Items.FERMENTED_SPIDER_EYE, (Item) ArcheryItems.STARTER_ARROW.get(), Items.MAGMA_CREAM, (Item) ArcheryItems.ACID_ARROW.get(), 4);
        full(pvd, (Item) ArcheryItems.DESTROYER_ARROW.get(), (Item) LCItems.SPACE_SHARD.get(), (Item) LCItems.VOID_EYE.get(), (Item) ArcheryItems.VOID_ARROW.get(), 1);
        cross(pvd, LCMats.TOTEMIC_GOLD.getNugget(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.TOTEMIC_GOLD_ARROW.get(), 4);
        cross(pvd, LCMats.POSEIDITE.getNugget(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.POSEIDITE_ARROW.get(), 4);
        cross(pvd, LCMats.SHULKERATE.getNugget(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.SHULKERATE_ARROW.get(), 4);
        cross(pvd, LCMats.SCULKIUM.getNugget(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.SCULKIUM_ARROW.get(), 4);
        cross(pvd, LCMats.ETERNIUM.getNugget(), (Item) ArcheryItems.STARTER_ARROW.get(), (Item) ArcheryItems.ETERNIUM_ARROW.get(), 4);
        cross(pvd, (Item) LCItems.EXPLOSION_SHARD.get(), (Item) ArcheryItems.QUARTZ_ARROW.get(), (Item) ArcheryItems.TEARING_ARROW.get(), 4);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.STARTER_BOW.get(), 1)::m_126132_, Items.BOW).pattern(" AC").pattern("ABC").pattern(" AC").define('A', Items.BAMBOO).define('B', Items.BOW).define('C', Items.VINE).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.GLOW_AIM_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.STARTER_BOW.get()).pattern("GGC").pattern("FBC").pattern("GGC").define('C', Items.PHANTOM_MEMBRANE).define('G', Items.GLOWSTONE_DUST).define('B', (ItemLike) ArcheryItems.STARTER_BOW.get()).define('F', Items.ENDER_EYE).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.IRON_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.STARTER_BOW.get()).pattern(" AC").pattern("ABC").pattern(" AC").define('A', Items.IRON_INGOT).define('B', (ItemLike) ArcheryItems.STARTER_BOW.get()).define('C', Items.WEEPING_VINES).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.MASTER_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.STARTER_BOW.get()).pattern(" AC").pattern("ABC").pattern(" AC").define('A', Items.COPPER_INGOT).define('B', (ItemLike) ArcheryItems.STARTER_BOW.get()).define('C', Items.TWISTING_VINES).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.MAGNIFY_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.GLOW_AIM_BOW.get()).pattern("A").pattern("B").define('B', (ItemLike) ArcheryItems.GLOW_AIM_BOW.get()).define('A', Items.SPYGLASS).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.ENDER_AIM_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.GLOW_AIM_BOW.get()).pattern("2OR").pattern("ABR").pattern("1OR").define('O', Items.DRAGON_HEAD).define('R', LCMats.SHULKERATE.getIngot()).define('1', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1)).define('2', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1)).define('A', (ItemLike) LCItems.VOID_EYE.get()).define('B', (ItemLike) ArcheryItems.GLOW_AIM_BOW.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.VOID_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.ENDER_AIM_BOW.get()).pattern("2OR").pattern("ABR").pattern("1OR").define('O', Items.DRAGON_HEAD).define('R', (ItemLike) LCItems.SPACE_SHARD.get()).define('1', new EnchantmentIngredient(Enchantments.MENDING, 1)).define('2', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('A', (ItemLike) LCItems.VOID_EYE.get()).define('B', (ItemLike) ArcheryItems.ENDER_AIM_BOW.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.FLAME_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.MASTER_BOW.get()).pattern("DCB").pattern("EAB").pattern("DCB").define('A', (ItemLike) ArcheryItems.MASTER_BOW.get()).define('B', Items.SOUL_SOIL).define('C', Items.BLAZE_ROD).define('D', Items.GHAST_TEAR).define('E', (ItemLike) LCItems.SOUL_FLAME.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.EXPLOSION_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.FLAME_BOW.get()).pattern("DDB").pattern("CAB").pattern("DDB").define('A', (ItemLike) ArcheryItems.FLAME_BOW.get()).define('B', Items.BLAZE_ROD).define('D', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('C', Items.NETHER_STAR).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.SUN_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.EXPLOSION_BOW.get()).pattern("EDB").pattern("CAB").pattern("EDB").define('A', (ItemLike) ArcheryItems.EXPLOSION_BOW.get()).define('B', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('C', (ItemLike) LCItems.SUN_MEMBRANE.get()).define('D', Items.NETHER_STAR).define('E', Items.CREEPER_HEAD).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.FROZE_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.MASTER_BOW.get()).pattern("BBB").pattern("DAB").pattern("BBB").define('A', (ItemLike) ArcheryItems.MASTER_BOW.get()).define('B', Items.BLUE_ICE).define('D', Items.POWDER_SNOW_BUCKET).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.WINTER_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.FROZE_BOW.get()).pattern("CBB").pattern("DAB").pattern("CBB").define('A', (ItemLike) ArcheryItems.FROZE_BOW.get()).define('B', Items.BLUE_ICE).define('C', Items.POWDER_SNOW_BUCKET).define('D', (ItemLike) LCItems.HARD_ICE.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.STORM_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.MASTER_BOW.get()).pattern("DCB").pattern("EAB").pattern("DCB").define('A', (ItemLike) ArcheryItems.MASTER_BOW.get()).define('B', Items.PHANTOM_MEMBRANE).define('C', Items.FEATHER).define('D', Items.GUNPOWDER).define('E', (ItemLike) LCItems.STORM_CORE.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.BLACKSTONE_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.MASTER_BOW.get()).pattern("CBB").pattern("DAB").pattern("CBB").define('A', (ItemLike) ArcheryItems.MASTER_BOW.get()).define('B', Items.BLACKSTONE).define('C', Items.SOUL_SAND).define('D', Items.GILDED_BLACKSTONE).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.TURTLE_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.IRON_BOW.get()).pattern("CCB").pattern("DAB").pattern("CCB").define('A', (ItemLike) ArcheryItems.IRON_BOW.get()).define('B', Items.SCUTE).define('C', Items.IRON_INGOT).define('D', Items.GOLD_INGOT).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.EARTH_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.TURTLE_BOW.get()).pattern("CCB").pattern("DAB").pattern("CCB").define('A', (ItemLike) ArcheryItems.TURTLE_BOW.get()).define('B', Items.BLACKSTONE).define('C', Items.GOLD_INGOT).define('D', (ItemLike) LCItems.BLACKSTONE_CORE.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.GAIA_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.EARTH_BOW.get()).pattern("CCB").pattern("DAB").pattern("CCB").define('A', (ItemLike) ArcheryItems.EARTH_BOW.get()).define('B', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('C', Items.NETHERITE_INGOT).define('D', (ItemLike) LCItems.FORCE_FIELD.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.EAGLE_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.IRON_BOW.get()).pattern("1LO").pattern("AB2").pattern("1LO").define('O', (ItemLike) LCItems.RESONANT_FEATHER.get()).define('L', Items.LEATHER).define('1', new EnchantmentIngredient(Enchantments.POWER_ARROWS, 5)).define('2', new EnchantmentIngredient(Enchantments.PUNCH_ARROWS, 2)).define('A', Items.NETHERITE_INGOT).define('B', (ItemLike) ArcheryItems.IRON_BOW.get()).m_176498_(pvd);
        unlock(pvd, new BowBuilder((ItemLike) ArcheryItems.WIND_BOW.get(), 1)::m_126132_, (Item) ArcheryItems.STORM_BOW.get()).pattern("CCB").pattern("DAB").pattern("CCB").define('A', (ItemLike) ArcheryItems.STORM_BOW.get()).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).define('C', Items.PHANTOM_MEMBRANE).define('D', (ItemLike) LCItems.CAPTURED_BULLET.get()).m_176498_(pvd);
        RecipeCategory var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) ArcheryItems.UPGRADE.get(), 1)::m_126132_, Items.AMETHYST_SHARD).pattern("BCB").pattern("DAD").pattern("BCB").define('A', Items.AMETHYST_SHARD).define('B', Items.GOLD_NUGGET).define('C', Items.REDSTONE).define('D', Items.LAPIS_LAZULI).m_176498_(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.GLOW_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("BCB").pattern("BAB").pattern("B B").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.GLOWSTONE_DUST).define('C', Items.ENDER_EYE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.FIRE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("DED").pattern("CAC").pattern("BBB").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.SOUL_SOIL).define('C', Items.BLAZE_ROD).define('D', Items.GHAST_TEAR).define('E', (ItemLike) LCItems.SOUL_FLAME.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.ICE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.POWDER_SNOW_BUCKET).define('C', Items.BLUE_ICE).define('E', (ItemLike) LCItems.HARD_ICE.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.EXPLOSION_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("BAB").pattern("CDC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.NETHER_STAR).define('C', Items.CREEPER_HEAD).define('D', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('E', (ItemLike) LCItems.EXPLOSION_SHARD.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.NO_FALL_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('C', Items.PHANTOM_MEMBRANE).define('E', (ItemLike) LCItems.CAPTURED_BULLET.get()).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.ENDER_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("3 4").pattern("CAC").pattern("1B2").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', (ItemLike) LCItems.VOID_EYE.get()).define('C', Items.DRAGON_HEAD).define('1', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('2', new EnchantmentIngredient(Enchantments.MENDING, 1)).define('3', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1)).define('4', new EnchantmentIngredient(Enchantments.VANISHING_CURSE, 1)).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.DAMAGE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("C C").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', (ItemLike) ArcheryItems.DIAMOND_ARROW.get()).define('C', new EnchantmentIngredient(Enchantments.POWER_ARROWS, 5)).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.PUNCH_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("C C").pattern(" A ").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', new EnchantmentIngredient(Enchantments.PUNCH_ARROWS, 2)).define('C', (ItemLike) ArcheryItems.GOLD_ARROW.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.MAGNIFY_UP_1.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern(" B ").pattern("CAC").pattern(" C ").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.SPYGLASS).define('C', Items.COPPER_INGOT).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.MAGNIFY_UP_2.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern(" B ").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.SPYGLASS).define('C', Items.COPPER_INGOT).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.MAGNIFY_UP_3.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.SPYGLASS).define('C', Items.COPPER_INGOT).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.SHINE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CBC").pattern("DAD").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('C', Items.SPECTRAL_ARROW).define('D', Items.GLOWSTONE_DUST).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.SUPERDAMAGE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CBC").pattern("DAD").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).define('D', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('C', new EnchantmentIngredient(Enchantments.POWER_ARROWS, 5)).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.BLACKSTONE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.GILDED_BLACKSTONE).define('C', Items.BLACKSTONE).define('E', (ItemLike) LCItems.BLACKSTONE_CORE.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.HARM_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.FERMENTED_SPIDER_EYE).define('E', Items.NETHER_WART).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.HEAL_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.GLISTERING_MELON_SLICE).define('E', Items.NETHER_WART).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.LEVITATE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', (ItemLike) LCItems.CAPTURED_BULLET.get()).define('E', Items.NETHER_WART).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.RAILGUN_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CBC").pattern("BAB").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('C', (ItemLike) LCItems.CAPTURED_WIND.get()).define('B', (ItemLike) LCItems.SPACE_SHARD.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.FLUX_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern(" X ").pattern("ICI").pattern("GPG").define('C', Items.GOLD_INGOT).define('G', Items.COPPER_INGOT).define('I', Items.IRON_INGOT).define('P', Items.REDSTONE).define('X', (ItemLike) ArcheryItems.UPGRADE.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.FLOAT_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.PHANTOM_MEMBRANE).define('E', (ItemLike) LCItems.CAPTURED_BULLET.get()).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.SLOW_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.COBWEB).define('E', Items.FERMENTED_SPIDER_EYE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.WEAK_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.GLOWSTONE_DUST).define('E', Items.FERMENTED_SPIDER_EYE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.POISON_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CDC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.SPIDER_EYE).define('E', Items.FERMENTED_SPIDER_EYE).define('D', Items.PUFFERFISH).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.WITHER_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CDC").pattern("CAC").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.WITHER_ROSE).define('D', Items.WITHER_SKELETON_SKULL).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.CORROSION_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', Items.MAGMA_CREAM).define('E', Items.FERMENTED_SPIDER_EYE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.CURSE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', LCItems.CURSED_DROPLET).define('E', Items.REDSTONE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.CLEANSE_UP.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', Items.DRAGON_BREATH).define('C', LCItems.LIFE_ESSENCE).define('E', Items.REDSTONE).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.ADVANCED_INFINITY.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('C', LCItems.CAPTURED_BULLET).define('E', Items.LAPIS_LAZULI).save(pvd);
        unlock(pvd, new BowUpgradeBuilder((Upgrade) ArcheryItems.EXPLOSION_BREAKER.get())::unlockedBy, (Item) ArcheryItems.UPGRADE.get()).pattern("CEC").pattern("EAE").pattern("CBC").define('A', (ItemLike) ArcheryItems.UPGRADE.get()).define('B', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('C', Items.END_CRYSTAL).define('E', Items.LAPIS_LAZULI).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_GLOW.get(), 1)::unlockedBy, Items.BOOK).pattern("CBC").pattern("BAB").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('C', Items.SPECTRAL_ARROW).define('B', Items.GLOWSTONE_DUST).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_GLOW.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_MAGNIFY.get(), 1)::unlockedBy, Items.BOOK).pattern(" B ").pattern("CAC").pattern(" C ").define('A', Items.BOOK).define('B', Items.SPYGLASS).define('C', Items.COPPER_INGOT).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_HARM.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.FERMENTED_SPIDER_EYE).define('E', Items.NETHER_WART).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_HARM.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_HEAL.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.GLISTERING_MELON_SLICE).define('E', Items.NETHER_WART).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_HEAL.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_LEVITATE.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', (ItemLike) LCItems.CAPTURED_BULLET.get()).define('E', Items.NETHER_WART).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_LEVITATE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_FLOAT.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("EAE").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.PHANTOM_MEMBRANE).define('E', (ItemLike) LCItems.CAPTURED_BULLET.get()).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_FLOAT.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_SLOW.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.COBWEB).define('E', Items.FERMENTED_SPIDER_EYE).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_SLOW.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_WEAK.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("EAE").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.GLOWSTONE_DUST).define('E', Items.FERMENTED_SPIDER_EYE).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_WEAK.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_POISON.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.SPIDER_EYE).define('E', Items.PUFFERFISH).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_POISON.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_WITHER.get(), 1)::unlockedBy, Items.BOOK).pattern("CEC").pattern("CAC").pattern("CBC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.WITHER_ROSE).define('E', Items.WITHER_SKELETON_SKULL).save(pvd, getID((Enchantment) ArcheryEnchantments.ENCH_WITHER.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_GLOW_AIM.get(), 1)::unlockedBy, Items.BOOK).pattern("BCB").pattern("BAB").pattern("B B").define('A', Items.BOOK).define('B', Items.GLOWSTONE_DUST).define('C', Items.ENDER_EYE).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_EXPLODE.get(), 1)::unlockedBy, Items.BOOK).pattern("CDC").pattern("BAB").pattern("CDC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', LCItems.STORM_CORE).define('D', LCItems.STRONG_CHARGE).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_EXPLOSION_BREAK.get(), 1)::unlockedBy, Items.BOOK).pattern("CDC").pattern("BAB").pattern("CDC").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.CREEPER_HEAD).define('C', LCItems.STORM_CORE).define('D', Items.END_CRYSTAL).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_DISTORTION.get(), 1)::unlockedBy, Items.BOOK).pattern("CDC").pattern("BAB").pattern("CDC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.PUFFERFISH).define('D', LCItems.GUARDIAN_EYE).save(pvd);
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) ArcheryEnchantments.ENCH_CHAOTIC.get(), 1)::unlockedBy, Items.BOOK).pattern("CDC").pattern("BAB").pattern("CDC").define('A', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('B', Items.DRAGON_BREATH).define('C', Items.DRAGON_HEAD).define('D', LCItems.GUARDIAN_EYE).save(pvd);
        Consumer<FinishedRecipe> loader = ConditionalRecipeWrapper.mod(pvd, "jeed");
        for (Entry<ResourceKey<Item>, Item> e : ForgeRegistries.ITEMS.getEntries()) {
            if (e.getValue() instanceof GenericBowItem) {
                loader.accept(new RecordRecipeFinished<>(new ResourceLocation("l2archery", "jeed_bow/" + ((ResourceKey) e.getKey()).location().getPath()), (RecipeSerializer<?>) JeedHelper.REC.get(), new JeedHelper.ArcheryJeedFinished(JeedHelper.JeedType.BOW, ((ResourceKey) e.getKey()).location())));
            }
            if (e.getValue() instanceof GenericArrowItem) {
                loader.accept(new RecordRecipeFinished<>(new ResourceLocation("l2archery", "jeed_arrow/" + ((ResourceKey) e.getKey()).location().getPath()), (RecipeSerializer<?>) JeedHelper.REC.get(), new JeedHelper.ArcheryJeedFinished(JeedHelper.JeedType.ARROW, ((ResourceKey) e.getKey()).location())));
            }
        }
        for (Entry<ResourceKey<Enchantment>, Enchantment> e : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            if (e.getValue() instanceof PotionArrowEnchantment) {
                loader.accept(new RecordRecipeFinished<>(new ResourceLocation("l2archery", "jeed_enchantment/" + ((ResourceKey) e.getKey()).location().getPath()), (RecipeSerializer<?>) JeedHelper.REC.get(), new JeedHelper.ArcheryJeedFinished(JeedHelper.JeedType.ENCHANTMENT, ((ResourceKey) e.getKey()).location())));
            }
        }
        for (Entry<ResourceKey<Upgrade>, Upgrade> ex : ArcheryRegister.UPGRADE.get().getEntries()) {
            loader.accept(new RecordRecipeFinished<>(new ResourceLocation("l2archery", "jeed_upgrade/" + ((ResourceKey) ex.getKey()).location().getPath()), (RecipeSerializer<?>) JeedHelper.REC.get(), new JeedHelper.ArcheryJeedFinished(JeedHelper.JeedType.UPGRADE, ((ResourceKey) ex.getKey()).location())));
        }
        JeedDataGenerator gen = new JeedDataGenerator("l2archery");
        gen.add((Item) ArcheryItems.TURTLE_BOW.get(), MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DAMAGE_RESISTANCE);
        gen.add((Item) ArcheryItems.EARTH_BOW.get(), MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DAMAGE_RESISTANCE);
        gen.add((Item) ArcheryItems.GAIA_BOW.get(), (MobEffect) LCEffects.STONE_CAGE.get(), MobEffects.DAMAGE_RESISTANCE);
        gen.add((Item) ArcheryItems.WIND_BOW.get(), MobEffects.MOVEMENT_SPEED);
        gen.generate(pvd);
    }

    private static ResourceLocation getID(Enchantment item) {
        return new ResourceLocation("l2archery", "enchantments/" + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath());
    }

    private static void cross(RegistrateRecipeProvider pvd, Item core, Item side, Item out, int count) {
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.COMBAT, out, count)::m_126132_, core).pattern(" S ").pattern("SCS").pattern(" S ").define('S', side).define('C', core).m_176498_(pvd);
    }

    private static void full(RegistrateRecipeProvider pvd, Item core, Item side, Item corner, Item out, int count) {
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.COMBAT, out, count)::m_126132_, core).pattern("CSC").pattern("SAS").pattern("CSC").define('A', core).define('S', side).define('C', corner).m_176498_(pvd);
    }

    private static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }
}