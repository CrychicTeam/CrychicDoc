package dev.xkmc.l2complements.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2complements.content.enchantment.core.EnchantmentRecipeBuilder;
import dev.xkmc.l2complements.content.recipe.BurntRecipeBuilder;
import dev.xkmc.l2complements.content.recipe.DiffusionRecipeBuilder;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCBlocks;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.compat.jeed.JeedDataGenerator;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.nethersdelight.common.registry.NDItems;

public class RecipeGen {

    private static final TagKey<Item>[] TOOLS = (TagKey<Item>[]) List.of(Tags.Items.ARMORS_BOOTS, Tags.Items.ARMORS_LEGGINGS, Tags.Items.ARMORS_CHESTPLATES, Tags.Items.ARMORS_HELMETS, ItemTags.SWORDS, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.PICKAXES, ItemTags.HOES).toArray(TagKey[]::new);

    private static final String[] TOOL_NAME = new String[] { "boots", "leggings", "chestplate", "helmet", "sword", "axe", "shovel", "pickaxe", "hoe" };

    private static String currentFolder = "";

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        for (int i = 0; i < LCMats.values().length; i++) {
            LCMats mat = LCMats.values()[i];
            ItemEntry<?>[] arr = LCItems.GEN_ITEM[i];
            genTools(pvd, mat, arr);
        }
        currentFolder = "storage/";
        for (int i = 0; i < LCMats.values().length; i++) {
            storage(pvd, LCItems.MAT_NUGGETS[i], LCItems.MAT_INGOTS[i], LCBlocks.GEN_BLOCK[i]);
        }
        currentFolder = "craft/";
        RecipeCategory var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.WIND_BOTTLE.get(), 1)::m_126132_, Items.GLASS_BOTTLE).requires(Items.GLASS_BOTTLE).requires(Items.PHANTOM_MEMBRANE).save(pvd, getID((Item) LCItems.WIND_BOTTLE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCBlocks.ETERNAL_ANVIL.get(), 1)::m_126132_, LCMats.ETERNIUM.getIngot()).pattern("AAA").pattern(" B ").pattern("BBB").define('A', LCMats.ETERNIUM.getBlock()).define('B', LCMats.ETERNIUM.getIngot()).save(pvd, getID(((AnvilBlock) LCBlocks.ETERNAL_ANVIL.get()).m_5456_()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, LCMats.ETERNIUM.getNugget(), 1)::m_126132_, (Item) LCItems.EXPLOSION_SHARD.get()).pattern("3C4").pattern("BAB").pattern("1C2").define('A', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('B', Items.ANVIL).define('C', Items.ENDER_PEARL).define('1', new EnchantmentIngredient(Enchantments.MENDING, 1)).define('2', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('3', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('4', new EnchantmentIngredient(Enchantments.UNBREAKING, 3)).save(pvd, getID(LCMats.ETERNIUM.getNugget()));
        blasting(pvd, Items.TOTEM_OF_UNDYING, LCMats.TOTEMIC_GOLD.getIngot(), 1.0F);
        blasting(pvd, Items.TRIDENT, LCMats.POSEIDITE.getIngot(), 1.0F);
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, LCMats.SHULKERATE.getIngot(), 1)::m_126132_, (Item) LCItems.CAPTURED_BULLET.get()).requires(Items.SHULKER_SHELL, 2).requires((ItemLike) LCItems.CAPTURED_BULLET.get()).requires(Items.IRON_INGOT).save(pvd, getID(LCMats.SHULKERATE.getIngot()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, LCMats.SCULKIUM.getIngot(), 2)::m_126132_, (Item) LCItems.WARDEN_BONE_SHARD.get()).requires(Items.ECHO_SHARD).requires((ItemLike) LCItems.WARDEN_BONE_SHARD.get(), 2).requires(Items.COPPER_INGOT).save(pvd, getID(LCMats.SCULKIUM.getIngot()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.GUARDIAN_RUNE.get(), 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("DAD").pattern("CBC").pattern("DCD").define('A', (ItemLike) LCItems.CURSED_DROPLET.get()).define('B', LCMats.POSEIDITE.getIngot()).define('C', Items.NAUTILUS_SHELL).define('D', Items.PRISMARINE_SHARD).save(pvd, getID((Item) LCItems.GUARDIAN_RUNE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.PIGLIN_RUNE.get(), 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("DAD").pattern("CBC").pattern("DCD").define('A', (ItemLike) LCItems.CURSED_DROPLET.get()).define('B', Items.NETHER_STAR).define('C', Items.NETHERITE_SCRAP).define('D', Items.BLACKSTONE).save(pvd, getID((Item) LCItems.PIGLIN_RUNE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.FRAGILE_WARP_STONE.get(), 1)::m_126132_, (Item) LCItems.VOID_EYE.get()).requires(Items.ECHO_SHARD).requires((ItemLike) LCItems.VOID_EYE.get(), 1).requires(Items.ENDER_PEARL).save(pvd, getID((Item) LCItems.FRAGILE_WARP_STONE.get()));
        smithing(pvd, (Item) LCItems.FRAGILE_WARP_STONE.get(), LCMats.SHULKERATE.getIngot(), (Item) LCItems.REINFORCED_WARP_STONE.get());
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.TOTEM_OF_DREAM.get(), 1)::m_126132_, (Item) LCItems.FRAGILE_WARP_STONE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', LCMats.TOTEMIC_GOLD.getIngot()).define('B', (ItemLike) LCItems.FRAGILE_WARP_STONE.get()).define('C', Items.ENDER_PEARL).save(pvd, getID((Item) LCItems.TOTEM_OF_DREAM.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.TOTEM_OF_THE_SEA.get(), 1)::m_126132_, LCMats.POSEIDITE.getIngot()).pattern("ACA").pattern("ABA").pattern("ACA").define('A', LCMats.TOTEMIC_GOLD.getIngot()).define('B', Items.HEART_OF_THE_SEA).define('C', LCMats.POSEIDITE.getIngot()).save(pvd, getID((Item) LCItems.TOTEM_OF_THE_SEA.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.SOUL_CHARGE.get(), 2)::m_126132_, Items.BLAZE_POWDER).requires(ItemTags.SOUL_FIRE_BASE_BLOCKS).requires(Items.BLAZE_POWDER, 2).requires(Items.GUNPOWDER, 2).save(pvd, getID((Item) LCItems.SOUL_CHARGE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.BLACK_CHARGE.get(), 2)::m_126132_, Items.BLAZE_POWDER).requires(Items.BLACKSTONE).requires(Items.BLAZE_POWDER, 2).requires(Items.GUNPOWDER, 2).save(pvd, getID((Item) LCItems.BLACK_CHARGE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.STRONG_CHARGE.get(), 2)::m_126132_, Items.BLAZE_POWDER).requires(Ingredient.of(Items.COAL, Items.CHARCOAL)).requires(Items.BLAZE_POWDER, 2).requires(Items.GUNPOWDER, 2).save(pvd, getID((Item) LCItems.STRONG_CHARGE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.TOTEMIC_CARROT.get(), 1)::m_126132_, LCMats.TOTEMIC_GOLD.getIngot()).pattern("AAA").pattern("ABA").pattern("AAA").define('A', LCMats.TOTEMIC_GOLD.getNugget()).define('B', Items.CARROT).save(pvd, getID((Item) LCItems.TOTEMIC_CARROT.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.ENCHANT_TOTEMIC_CARROT.get(), 1)::m_126132_, LCMats.TOTEMIC_GOLD.getIngot()).pattern("ACA").pattern("ABA").pattern("AAA").define('A', LCMats.TOTEMIC_GOLD.getIngot()).define('B', Items.CARROT).define('C', (ItemLike) LCItems.LIFE_ESSENCE.get()).save(pvd, getID((Item) LCItems.ENCHANT_TOTEMIC_CARROT.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.TOTEMIC_APPLE.get(), 1)::m_126132_, LCMats.TOTEMIC_GOLD.getIngot()).pattern("ACA").pattern("ABA").pattern("AAA").define('A', LCMats.TOTEMIC_GOLD.getIngot()).define('B', Items.APPLE).define('C', (ItemLike) LCItems.LIFE_ESSENCE.get()).save(pvd, getID((Item) LCItems.TOTEMIC_APPLE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.ENCHANTED_TOTEMIC_APPLE.get(), 1)::m_126132_, LCMats.TOTEMIC_GOLD.getIngot()).pattern("ACA").pattern("CBC").pattern("ACA").define('A', LCMats.TOTEMIC_GOLD.getBlock()).define('B', Items.APPLE).define('C', (ItemLike) LCItems.LIFE_ESSENCE.get()).save(pvd, getID((Item) LCItems.ENCHANTED_TOTEMIC_APPLE.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapelessRecipeBuilder(var10003, (ItemLike) LCItems.WARDEN_BONE_SHARD.get(), 1)::m_126132_, (Item) LCItems.RESONANT_FEATHER.get()).requires(TagGen.DELICATE_BONE).requires((ItemLike) LCItems.RESONANT_FEATHER.get()).save(pvd, getID((Item) LCItems.WARDEN_BONE_SHARD.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.SONIC_SHOOTER.get(), 1)::m_126132_, LCMats.SCULKIUM.getIngot()).pattern("ABB").pattern("III").pattern("IC ").define('I', LCMats.SCULKIUM.getIngot()).define('A', LCItems.VOID_EYE).define('B', (ItemLike) LCItems.RESONANT_FEATHER.get()).define('C', (ItemLike) LCItems.EXPLOSION_SHARD.get()).save(pvd, getID((Item) LCItems.SONIC_SHOOTER.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.HELLFIRE_WAND.get(), 1)::m_126132_, (Item) LCItems.SUN_MEMBRANE.get()).pattern(" FM").pattern(" CF").pattern("C  ").define('F', (ItemLike) LCItems.SOUL_FLAME.get()).define('M', (ItemLike) LCItems.SUN_MEMBRANE.get()).define('C', (ItemLike) LCItems.EXPLOSION_SHARD.get()).save(pvd, getID((Item) LCItems.HELLFIRE_WAND.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.WINTERSTORM_WAND.get(), 1)::m_126132_, (Item) LCItems.HARD_ICE.get()).pattern(" FM").pattern(" CF").pattern("C  ").define('F', (ItemLike) LCItems.HARD_ICE.get()).define('M', (ItemLike) LCItems.STORM_CORE.get()).define('C', Items.STICK).save(pvd, getID((Item) LCItems.WINTERSTORM_WAND.get()));
        var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LCItems.DIFFUSION_WAND.get(), 1)::m_126132_, (Item) LCItems.STORM_CORE.get()).pattern(" FM").pattern(" CF").pattern("C  ").define('F', Items.DIAMOND).define('M', (ItemLike) LCItems.STORM_CORE.get()).define('C', Items.STICK).save(pvd, getID((Item) LCItems.DIFFUSION_WAND.get()));
        currentFolder = "vanilla/renew/";
        Consumer<FinishedRecipe> cond = ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(LCConfig.COMMON_PATH, LCConfig.COMMON.enableVanillaItemRecipe, true));
        unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.MISC, Items.ECHO_SHARD, 1)::m_126132_, (Item) LCItems.RESONANT_FEATHER.get()).requires((ItemLike) LCItems.RESONANT_FEATHER.get()).requires(Items.AMETHYST_SHARD).requires(Items.SCULK, 4).save(cond, getID(Items.ECHO_SHARD));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.ELYTRA, 1)::m_126132_, (Item) LCItems.SUN_MEMBRANE.get()).pattern("ABA").pattern("CEC").pattern("D D").define('A', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('B', (ItemLike) LCItems.CAPTURED_WIND.get()).define('C', (ItemLike) LCItems.SUN_MEMBRANE.get()).define('D', (ItemLike) LCItems.RESONANT_FEATHER.get()).define('E', (ItemLike) LCItems.STORM_CORE.get()).save(cond, getID(Items.ELYTRA));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.ANCIENT_DEBRIS, 1)::m_126132_, (Item) LCItems.EXPLOSION_SHARD.get()).pattern("ABA").pattern("ACA").pattern("ADA").define('A', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('B', Items.NETHER_STAR).define('C', Items.CRYING_OBSIDIAN).define('D', (ItemLike) LCItems.FORCE_FIELD.get()).save(cond, getID(Items.ANCIENT_DEBRIS));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.GILDED_BLACKSTONE, 1)::m_126132_, (Item) LCItems.BLACKSTONE_CORE.get()).pattern("ABA").pattern("BCB").pattern("ABA").define('A', Items.BLACKSTONE).define('B', Items.GOLD_INGOT).define('C', (ItemLike) LCItems.BLACKSTONE_CORE.get()).save(cond, getID(Items.GILDED_BLACKSTONE));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.REINFORCED_DEEPSLATE, 1)::m_126132_, (Item) LCItems.WARDEN_BONE_SHARD.get()).pattern(" B ").pattern("BAB").pattern(" B ").define('A', Items.DEEPSLATE).define('B', (ItemLike) LCItems.WARDEN_BONE_SHARD.get()).save(cond, getID(Items.REINFORCED_DEEPSLATE));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.HEART_OF_THE_SEA, 1)::m_126132_, (Item) LCItems.GUARDIAN_EYE.get()).pattern("ABA").pattern("BCB").pattern("ABA").define('A', Items.PRISMARINE_SHARD).define('B', Items.PRISMARINE_CRYSTALS).define('C', (ItemLike) LCItems.GUARDIAN_EYE.get()).save(cond, getID(Items.HEART_OF_THE_SEA));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1)::m_126132_, (Item) LCItems.BLACKSTONE_CORE.get()).pattern("BAB").pattern("BCB").pattern("BBB").define('B', Items.DIAMOND).define('C', Items.NETHERRACK).define('A', (ItemLike) LCItems.BLACKSTONE_CORE.get()).save(cond, getID(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE));
        currentFolder = "vanilla/upgrade/";
        cond = ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(LCConfig.COMMON_PATH, LCConfig.COMMON.enableToolRecraftRecipe, true));
        for (int i = 0; i < 9; i++) {
            smithing(pvd, TOOLS[i], Items.IRON_BLOCK, ForgeRegistries.ITEMS.getValue(new ResourceLocation("iron_" + TOOL_NAME[i])), cond);
            smithing(pvd, TOOLS[i], Items.GOLD_BLOCK, ForgeRegistries.ITEMS.getValue(new ResourceLocation("golden_" + TOOL_NAME[i])), cond);
            smithing(pvd, TOOLS[i], Items.DIAMOND_BLOCK, ForgeRegistries.ITEMS.getValue(new ResourceLocation("diamond_" + TOOL_NAME[i])), cond);
        }
        currentFolder = "enchantments/";
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_PROJECTILE.get(), 1)::unlockedBy, (Item) LCItems.FORCE_FIELD.get()).pattern("1B1").pattern("BCB").pattern("2B2").define('1', new EnchantmentIngredient(Enchantments.PROJECTILE_PROTECTION, 4)).define('2', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('B', (ItemLike) LCItems.FORCE_FIELD.get()).define('C', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).save(pvd, getID((Enchantment) LCEnchantments.ENCH_PROJECTILE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_EXPLOSION.get(), 1)::unlockedBy, (Item) LCItems.EXPLOSION_SHARD.get()).pattern("1B1").pattern("BCB").pattern("2B2").define('1', new EnchantmentIngredient(Enchantments.BLAST_PROTECTION, 4)).define('2', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('B', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('C', Items.CRYING_OBSIDIAN).save(pvd, getID((Enchantment) LCEnchantments.ENCH_EXPLOSION.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_FIRE.get(), 1)::unlockedBy, (Item) LCItems.SUN_MEMBRANE.get()).pattern("1B1").pattern("BCB").pattern("2B2").define('1', new EnchantmentIngredient(Enchantments.FIRE_PROTECTION, 4)).define('2', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('B', (ItemLike) LCItems.SOUL_FLAME.get()).define('C', (ItemLike) LCItems.HARD_ICE.get()).save(pvd, getID((Enchantment) LCEnchantments.ENCH_FIRE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_ENVIRONMENT.get(), 1)::unlockedBy, (Item) LCItems.VOID_EYE.get()).pattern("1B1").pattern("BCB").pattern("2B2").define('1', (ItemLike) LCItems.SUN_MEMBRANE.get()).define('2', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('B', (ItemLike) LCItems.VOID_EYE.get()).define('C', (ItemLike) LCItems.CAPTURED_WIND.get()).save(pvd, getID((Enchantment) LCEnchantments.ENCH_ENVIRONMENT.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_MAGIC.get(), 1)::unlockedBy, (Item) LCItems.RESONANT_FEATHER.get()).pattern("1B1").pattern("BCB").pattern("2B2").define('1', (ItemLike) LCItems.VOID_EYE.get()).define('2', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('B', (ItemLike) LCItems.RESONANT_FEATHER.get()).define('C', (ItemLike) LCItems.FORCE_FIELD.get()).save(pvd, getID((Enchantment) LCEnchantments.ENCH_MAGIC.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_INVINCIBLE.get(), 1)::unlockedBy, (Item) LCItems.SPACE_SHARD.get()).pattern("A1A").pattern("203").pattern("A4A").define('A', (ItemLike) LCItems.SPACE_SHARD.get()).define('0', new EnchantmentIngredient((Enchantment) LCEnchantments.ENCH_ENVIRONMENT.get(), 1)).define('1', new EnchantmentIngredient((Enchantment) LCEnchantments.ENCH_MAGIC.get(), 1)).define('2', new EnchantmentIngredient((Enchantment) LCEnchantments.ENCH_EXPLOSION.get(), 1)).define('3', new EnchantmentIngredient((Enchantment) LCEnchantments.ENCH_FIRE.get(), 1)).define('4', new EnchantmentIngredient((Enchantment) LCEnchantments.ENCH_PROJECTILE.get(), 1)).save(pvd, getID((Enchantment) LCEnchantments.ENCH_INVINCIBLE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENCH_MATES.get(), 1)::unlockedBy, Items.NETHER_STAR).pattern("BAB").pattern("B1B").pattern("BAB").define('1', new EnchantmentIngredient(Enchantments.ALL_DAMAGE_PROTECTION, 4)).define('A', Items.NETHER_STAR).define('B', Items.END_ROD).save(pvd, getID((Enchantment) LCEnchantments.ENCH_MATES.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SHULKER_ARMOR.get(), 1)::unlockedBy, LCMats.SHULKERATE.getNugget()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', LCMats.SHULKERATE.getNugget()).define('B', Items.BOOK).define('C', Items.GLASS).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.SHULKER_ARMOR.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.STABLE_BODY.get(), 1)::unlockedBy, Items.CRYING_OBSIDIAN).pattern("LCL").pattern("ABA").pattern("LAL").define('A', Items.OBSIDIAN).define('B', Items.BOOK).define('C', Items.CRYING_OBSIDIAN).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.STABLE_BODY.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.LIFE_SYNC.get(), 1)::unlockedBy, (Item) LCItems.FORCE_FIELD.get()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', Items.WITHER_ROSE).define('B', Items.BOOK).define('C', (ItemLike) LCItems.FORCE_FIELD.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.LIFE_SYNC.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.LIFE_MENDING.get(), 1)::unlockedBy, Items.BOOK).pattern("LCL").pattern("ABA").pattern("FCF").define('A', Items.WEEPING_VINES).define('B', Items.BOOK).define('C', Items.TWISTING_VINES).define('L', Items.LAPIS_LAZULI).define('F', Items.ROTTEN_FLESH).save(pvd, getID((Enchantment) LCEnchantments.LIFE_MENDING.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SAFEGUARD.get(), 1)::unlockedBy, Items.BOOK).pattern("LCL").pattern("ABA").pattern("FCF").define('A', LCMats.SHULKERATE.getNugget()).define('B', Items.BOOK).define('C', Items.AMETHYST_SHARD).define('L', Items.LAPIS_LAZULI).define('F', Items.NETHERITE_SCRAP).save(pvd, getID((Enchantment) LCEnchantments.SAFEGUARD.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENDER_MASK.get(), 1)::unlockedBy, Items.ENDER_EYE).pattern("LCL").pattern("ABA").pattern("LAL").define('A', Items.ENDER_EYE).define('B', Items.BOOK).define('C', Items.CARVED_PUMPKIN).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.ENDER_MASK.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SHINNY.get(), 1)::unlockedBy, Items.GOLD_INGOT).pattern("LAL").pattern("ABA").pattern("LAL").define('A', Items.GOLD_INGOT).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.SHINNY.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SNOW_WALKER.get(), 1)::unlockedBy, Items.LEATHER).pattern("LAL").pattern("ABA").pattern("LAL").define('A', Items.LEATHER).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.SNOW_WALKER.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SOUL_BOUND.get(), 1)::unlockedBy, (Item) LCItems.VOID_EYE.get()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.VOID_EYE.get()).define('B', new EnchantmentIngredient(Enchantments.BINDING_CURSE, 1)).define('L', Items.ECHO_SHARD).save(pvd, getID((Enchantment) LCEnchantments.SOUL_BOUND.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.DAMPENED.get(), 1)::unlockedBy, LCMats.SCULKIUM.getNugget()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', LCMats.SCULKIUM.getNugget()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.DAMPENED.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ENDER.get(), 1)::unlockedBy, Items.ENDER_PEARL).pattern("LAL").pattern("ABA").pattern("LCL").define('A', Items.ENDER_PEARL).define('B', Items.BOOK).define('C', Items.HOPPER).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.ENDER.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.HARDENED.get(), 1)::unlockedBy, LCMats.SHULKERATE.getIngot()).pattern("SCS").pattern("ABA").pattern("LAL").define('A', LCMats.SHULKERATE.getIngot()).define('S', (ItemLike) LCItems.WARDEN_BONE_SHARD.get()).define('C', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.HARDENED.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ETERNAL.get(), 1)::unlockedBy, (Item) LCItems.SPACE_SHARD.get()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', LCMats.ETERNIUM.getIngot()).define('C', (ItemLike) LCItems.SPACE_SHARD.get()).define('B', new EnchantmentIngredient((Enchantment) LCEnchantments.HARDENED.get(), 1)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.ETERNAL.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), 1)::unlockedBy, Items.DIAMOND).pattern(" A ").pattern("LBL").pattern(" L ").define('A', Items.DIAMOND).define('B', new EnchantmentIngredient(Enchantments.UNBREAKING, 1)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), "_1"));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), 2)::unlockedBy, Items.DIAMOND).pattern("LAL").pattern("ABA").pattern("LAL").define('A', Items.DIAMOND).define('B', new EnchantmentIngredient(Enchantments.UNBREAKING, 2)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), "_2"));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), 3)::unlockedBy, Items.DIAMOND).pattern("L1L").pattern("2B3").pattern("L4L").define('1', Items.DIAMOND_HELMET).define('2', Items.DIAMOND_CHESTPLATE).define('3', Items.DIAMOND_LEGGINGS).define('4', Items.DIAMOND_BOOTS).define('B', new EnchantmentIngredient(Enchantments.UNBREAKING, 3)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.DURABLE_ARMOR.get(), "_3"));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SMELT.get(), 1)::unlockedBy, (Item) LCItems.SOUL_FLAME.get()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', Items.LAVA_BUCKET).define('C', (ItemLike) LCItems.SOUL_FLAME.get()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.SMELT.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.WIND_SWEEP.get(), 1)::unlockedBy, LCMats.SHULKERATE.getNugget()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', LCMats.SHULKERATE.getNugget()).define('B', Items.BOOK).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.WIND_SWEEP.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ICE_BLADE.get(), 1)::unlockedBy, (Item) LCItems.HARD_ICE.get()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.HARD_ICE.get()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.ICE_BLADE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.FLAME_BLADE.get(), 1)::unlockedBy, (Item) LCItems.SOUL_FLAME.get()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.SOUL_FLAME.get()).define('B', new EnchantmentIngredient(Enchantments.FIRE_ASPECT, 2)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.FLAME_BLADE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.ICE_THORN.get(), 1)::unlockedBy, (Item) LCItems.HARD_ICE.get()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.HARD_ICE.get()).define('B', new EnchantmentIngredient(Enchantments.THORNS, 3)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.ICE_THORN.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.FLAME_THORN.get(), 1)::unlockedBy, (Item) LCItems.SOUL_FLAME.get()).pattern("LAL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.SOUL_FLAME.get()).define('B', new EnchantmentIngredient(Enchantments.THORNS, 3)).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.FLAME_THORN.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.SHARP_BLADE.get(), 1)::unlockedBy, (Item) LCItems.CURSED_DROPLET.get()).pattern("LCL").pattern("ABA").pattern("LAL").define('A', (ItemLike) LCItems.EXPLOSION_SHARD.get()).define('C', (ItemLike) LCItems.CURSED_DROPLET.get()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.SHARP_BLADE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.CURSE_BLADE.get(), 1)::unlockedBy, (Item) LCItems.CURSED_DROPLET.get()).pattern("LCL").pattern("ABA").pattern("LCL").define('A', Items.FERMENTED_SPIDER_EYE).define('C', (ItemLike) LCItems.CURSED_DROPLET.get()).define('B', Items.BOOK).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LCEnchantments.CURSE_BLADE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.VOID_TOUCH.get(), 1)::unlockedBy, (Item) LCItems.RESONANT_FEATHER.get()).pattern("LCL").pattern("ABA").pattern("LCL").define('A', (ItemLike) LCItems.VOID_EYE.get()).define('C', (ItemLike) LCItems.SUN_MEMBRANE.get()).define('B', Items.BOOK).define('L', (ItemLike) LCItems.RESONANT_FEATHER.get()).save(pvd, getID((Enchantment) LCEnchantments.VOID_TOUCH.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.CUBIC.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern("ECE").pattern("BAB").pattern("DBD").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.STONE_PICKAXE).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', Items.IRON_INGOT).save(pvd, getID((Enchantment) LCEnchantments.CUBIC.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.PLANE.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern("ECE").pattern("DAD").pattern("BBB").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.STONE_HOE).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', Items.IRON_INGOT).save(pvd, getID((Enchantment) LCEnchantments.PLANE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.DRILL.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern("ECB").pattern("DAB").pattern("EDB").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.STONE_SHOVEL).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', Items.IRON_INGOT).save(pvd, getID((Enchantment) LCEnchantments.DRILL.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.VIEN.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern("ECE").pattern("BAB").pattern("DBD").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.IRON_PICKAXE).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', Items.GOLD_INGOT).save(pvd, getID((Enchantment) LCEnchantments.VIEN.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.TREE.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern("ECE").pattern("BAB").pattern("DBD").define('A', new EnchantmentIngredient(Enchantments.BLOCK_EFFICIENCY, 1)).define('B', Items.IRON_AXE).define('C', (ItemLike) LCItems.STORM_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', Items.GOLD_INGOT).save(pvd, getID((Enchantment) LCEnchantments.TREE.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.CHUNK_CUBIC.get(), 1)::unlockedBy, (Item) LCItems.BLACKSTONE_CORE.get()).pattern("ECE").pattern("BAB").pattern("DBD").define('A', new EnchantmentIngredient((Enchantment) LCEnchantments.CUBIC.get(), 1)).define('B', Items.NETHERITE_PICKAXE).define('C', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', LCMats.SCULKIUM.getIngot()).save(pvd, getID((Enchantment) LCEnchantments.CHUNK_CUBIC.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LCEnchantments.CHUNK_PLANE.get(), 1)::unlockedBy, (Item) LCItems.BLACKSTONE_CORE.get()).pattern("ECE").pattern("DAD").pattern("BBB").define('A', new EnchantmentIngredient((Enchantment) LCEnchantments.PLANE.get(), 1)).define('B', Items.NETHERITE_HOE).define('C', (ItemLike) LCItems.BLACKSTONE_CORE.get()).define('D', Items.LAPIS_LAZULI).define('E', LCMats.SCULKIUM.getIngot()).save(pvd, getID((Enchantment) LCEnchantments.CHUNK_PLANE.get()));
        currentFolder = "burnt/";
        convert(pvd, Items.EMERALD, (Item) LCItems.EMERALD.get(), 15552);
        convert(pvd, Items.EMERALD_BLOCK, (Item) LCItems.EMERALD.get(), 1728);
        convert(pvd, Items.ROTTEN_FLESH, (Item) LCItems.CURSED_DROPLET.get(), 15552);
        convert(pvd, Items.SOUL_SAND, (Item) LCItems.CURSED_DROPLET.get(), 27648);
        convert(pvd, Items.SOUL_SOIL, (Item) LCItems.CURSED_DROPLET.get(), 27648);
        convert(pvd, Items.GHAST_TEAR, (Item) LCItems.CURSED_DROPLET.get(), 576);
        convert(pvd, Items.NETHER_STAR, (Item) LCItems.CURSED_DROPLET.get(), 64);
        convert(pvd, Items.COOKED_BEEF, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_CHICKEN, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_MUTTON, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_PORKCHOP, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_RABBIT, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_COD, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COOKED_SALMON, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.BEEF, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.CHICKEN, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.MUTTON, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.PORKCHOP, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.RABBIT, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.COD, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.SALMON, (Item) LCItems.LIFE_ESSENCE.get(), 15552);
        convert(pvd, Items.TOTEM_OF_UNDYING, (Item) LCItems.LIFE_ESSENCE.get(), 64);
        if (ModList.get().isLoaded("nethersdelight")) {
            convert(pvd, (Item) NDItems.HOGLIN_LOIN.get(), (Item) LCItems.LIFE_ESSENCE.get(), 15552, "nethersdelight");
            convert(pvd, (Item) NDItems.HOGLIN_SIRLOIN.get(), (Item) LCItems.LIFE_ESSENCE.get(), 15552, "nethersdelight");
        }
        currentFolder = "diffusion/";
        diffuse(pvd, Blocks.LAPIS_BLOCK, Blocks.STONE, Blocks.LAPIS_ORE);
        diffuse(pvd, Blocks.LAPIS_BLOCK, Blocks.DEEPSLATE, Blocks.DEEPSLATE_LAPIS_ORE);
        diffuse(pvd, Blocks.REDSTONE_BLOCK, Blocks.STONE, Blocks.REDSTONE_ORE);
        diffuse(pvd, Blocks.REDSTONE_BLOCK, Blocks.DEEPSLATE, Blocks.DEEPSLATE_REDSTONE_ORE);
        diffuse(pvd, Blocks.DIAMOND_BLOCK, Blocks.STONE, Blocks.DIAMOND_ORE);
        diffuse(pvd, Blocks.DIAMOND_BLOCK, Blocks.DEEPSLATE, Blocks.DEEPSLATE_DIAMOND_ORE);
        diffuse(pvd, Blocks.EMERALD_BLOCK, Blocks.STONE, Blocks.EMERALD_ORE);
        diffuse(pvd, Blocks.EMERALD_BLOCK, Blocks.DEEPSLATE, Blocks.DEEPSLATE_EMERALD_ORE);
        diffuse(pvd, Blocks.QUARTZ_BLOCK, Blocks.NETHERRACK, Blocks.NETHER_QUARTZ_ORE);
        cond = ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(LCConfig.COMMON_PATH, LCConfig.COMMON.enableSpawnEggRecipe, true));
        currentFolder = "eggs/undead/";
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.ZOMBIE_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("AAA").pattern("ABA").pattern("ACA").define('A', Items.ROTTEN_FLESH).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).save(cond, getID(Items.ZOMBIE_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.HUSK_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("ADA").pattern("ABA").pattern("ACA").define('A', Items.ROTTEN_FLESH).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).define('D', Items.SAND).save(cond, getID(Items.HUSK_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.DROWNED_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("ADA").pattern("ABA").pattern("ACA").define('A', Items.ROTTEN_FLESH).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).define('D', Items.KELP).save(cond, getID(Items.DROWNED_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.ZOMBIFIED_PIGLIN_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("ADA").pattern("ABA").pattern("ACA").define('A', Items.ROTTEN_FLESH).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).define('D', Items.GOLD_INGOT).save(cond, getID(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.SKELETON_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("AAA").pattern("ABA").pattern("ACA").define('A', Items.BONE).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).save(cond, getID(Items.SKELETON_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.STRAY_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("ADA").pattern("ABA").pattern("ACA").define('A', Items.BONE).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).define('D', Items.SNOWBALL).save(cond, getID(Items.STRAY_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.WITHER_SKELETON_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("ADA").pattern("ABA").pattern("ACA").define('A', Items.BONE).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).define('D', Items.WITHER_SKELETON_SKULL).save(cond, getID(Items.WITHER_SKELETON_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.PHANTOM_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("AAA").pattern("ABA").pattern("ACA").define('A', Items.PHANTOM_MEMBRANE).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).save(cond, getID(Items.PHANTOM_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.GHAST_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.CURSED_DROPLET.get()).pattern("AAA").pattern("ABA").pattern("ACA").define('A', Items.GHAST_TEAR).define('B', (ItemLike) LCItems.CURSED_DROPLET.get()).define('C', Items.EGG).save(cond, getID(Items.GHAST_SPAWN_EGG));
        currentFolder = "eggs/animal/";
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.PIG_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.PORKCHOP).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.PIG_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.COW_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.BEEF).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.COW_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.MOOSHROOM_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.RED_MUSHROOM).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.COW_SPAWN_EGG).save(cond, getID(Items.MOOSHROOM_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.SHEEP_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.MUTTON).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.SHEEP_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.CHICKEN_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.CHICKEN).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.CHICKEN_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.RABBIT_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.RABBIT).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.RABBIT_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.BEE_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.HONEYCOMB).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.BEE_SPAWN_EGG));
        currentFolder = "eggs/aquatic_spawn/";
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.COD_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.COD).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.COD_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.SALMON_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.SALMON).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.SALMON_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.TROPICAL_FISH_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.TROPICAL_FISH).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.TROPICAL_FISH_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.SQUID_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.INK_SAC).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.SQUID_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.GLOW_SQUID_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.GLOW_INK_SAC).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.GLOW_SQUID_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.FROG_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern(" A ").pattern("LBL").pattern(" C ").define('A', Items.FROGSPAWN).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.FROG_SPAWN_EGG));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.TURTLE_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("AAA").pattern("LBL").pattern("ACA").define('A', Items.SCUTE).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.EGG).save(cond, getID(Items.TURTLE_SPAWN_EGG));
        currentFolder = "eggs/aquatic_alternate/";
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.COD_BUCKET, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern(" A ").pattern("LBL").pattern(" C ").define('A', Items.COD).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.BUCKET).save(cond, getID(Items.COD_BUCKET));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.SALMON_BUCKET, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern(" A ").pattern("LBL").pattern(" C ").define('A', Items.SALMON).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.BUCKET).save(cond, getID(Items.SALMON_BUCKET));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.TROPICAL_FISH_BUCKET, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern(" A ").pattern("LBL").pattern(" C ").define('A', Items.TROPICAL_FISH).define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.BUCKET).save(cond, getID(Items.TROPICAL_FISH_BUCKET));
        unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.MISC, Items.TURTLE_SPAWN_EGG, 1)::m_126132_, (Item) LCItems.LIFE_ESSENCE.get()).pattern("LBL").pattern(" C ").define('B', (ItemLike) LCItems.LIFE_ESSENCE.get()).define('L', LCMats.TOTEMIC_GOLD.getIngot()).define('C', Items.TURTLE_EGG).save(cond, getID(Items.TURTLE_SPAWN_EGG));
        JeedDataGenerator jeed = new JeedDataGenerator("l2complements");
        jeed.add((Item) LCItems.SOUL_CHARGE.get(), (MobEffect) LCEffects.FLAME.get());
        jeed.add((Item) LCItems.BLACK_CHARGE.get(), (MobEffect) LCEffects.STONE_CAGE.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.FLAME_BLADE.get(), 1), (MobEffect) LCEffects.FLAME.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.FLAME_THORN.get(), 1), (MobEffect) LCEffects.FLAME.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.ICE_BLADE.get(), 1), (MobEffect) LCEffects.ICE.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.ICE_THORN.get(), 1), (MobEffect) LCEffects.ICE.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.CURSE_BLADE.get(), 1), (MobEffect) LCEffects.CURSE.get());
        jeed.add(new EnchantmentIngredient((Enchantment) LCEnchantments.SHARP_BLADE.get(), 1), (MobEffect) LCEffects.BLEED.get());
        jeed.add((Item) LCItems.TOTEM_OF_DREAM.get(), MobEffects.REGENERATION, MobEffects.ABSORPTION, MobEffects.FIRE_RESISTANCE);
        jeed.add((Item) LCItems.TOTEM_OF_THE_SEA.get(), MobEffects.REGENERATION, MobEffects.ABSORPTION, MobEffects.FIRE_RESISTANCE);
        jeed.add(LCMats.POSEIDITE.getArmor(EquipmentSlot.HEAD), MobEffects.CONDUIT_POWER, MobEffects.DOLPHINS_GRACE);
        jeed.add(LCMats.POSEIDITE.getArmor(EquipmentSlot.CHEST), MobEffects.CONDUIT_POWER, MobEffects.DOLPHINS_GRACE);
        jeed.add(LCMats.POSEIDITE.getArmor(EquipmentSlot.LEGS), MobEffects.CONDUIT_POWER, MobEffects.DOLPHINS_GRACE);
        jeed.add(LCMats.POSEIDITE.getArmor(EquipmentSlot.FEET), MobEffects.CONDUIT_POWER, MobEffects.DOLPHINS_GRACE);
        jeed.generate(pvd);
    }

    private static ResourceLocation getID(Enchantment item) {
        return new ResourceLocation("l2complements", currentFolder + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath());
    }

    private static ResourceLocation getID(Enchantment item, String suffix) {
        return new ResourceLocation("l2complements", currentFolder + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath() + suffix);
    }

    private static ResourceLocation getID(Item item) {
        return new ResourceLocation("l2complements", currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
    }

    private static ResourceLocation getID(Item item, String suffix) {
        return new ResourceLocation("l2complements", currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
    }

    private static void convert(RegistrateRecipeProvider pvd, Item in, Item out, int count) {
        unlock(pvd, new BurntRecipeBuilder(Ingredient.of(in), out.getDefaultInstance(), count)::unlockedBy, in).save(pvd, getID(in));
    }

    private static void convert(RegistrateRecipeProvider pvd, Item in, Item out, int count, String modid) {
        unlock(pvd, new BurntRecipeBuilder(Ingredient.of(in), out.getDefaultInstance(), count)::unlockedBy, in).save(ConditionalRecipeWrapper.mod(pvd, modid), getID(in));
    }

    private static void diffuse(RegistrateRecipeProvider pvd, Block in, Block base, Block out) {
        unlock(pvd, new DiffusionRecipeBuilder(in, base, out)::unlockedBy, in.asItem()).save(pvd, getID(out.asItem()));
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

    public static void genTools(RegistrateRecipeProvider pvd, LCMats mat, ItemEntry<?>[] arr) {
        currentFolder = "generated_tools/" + mat.name().toLowerCase(Locale.ROOT) + "/craft/";
        Item ingot = mat.getIngot();
        RecipeCategory var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[0].get(), 1)::m_126132_, (Item) arr[0].get()).pattern("A A").pattern("A A").define('A', ingot).save(pvd, getID((Item) arr[0].get()));
        var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[1].get(), 1)::m_126132_, (Item) arr[1].get()).pattern("AAA").pattern("A A").pattern("A A").define('A', ingot).save(pvd, getID((Item) arr[1].get()));
        var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[2].get(), 1)::m_126132_, (Item) arr[2].get()).pattern("A A").pattern("AAA").pattern("AAA").define('A', ingot).save(pvd, getID((Item) arr[2].get()));
        var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[3].get(), 1)::m_126132_, (Item) arr[3].get()).pattern("AAA").pattern("A A").define('A', ingot).save(pvd, getID((Item) arr[3].get()));
        ingot = mat.getToolIngot();
        Item stick = mat.getToolStick();
        var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[4].get(), 1)::m_126132_, (Item) arr[4].get()).pattern("A").pattern("A").pattern("B").define('A', ingot).define('B', stick).save(pvd, getID((Item) arr[4].get()));
        var10003 = RecipeCategory.TOOLS;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[5].get(), 1)::m_126132_, (Item) arr[5].get()).pattern("AA").pattern("AB").pattern(" B").define('A', ingot).define('B', stick).save(pvd, getID((Item) arr[5].get()));
        var10003 = RecipeCategory.TOOLS;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[6].get(), 1)::m_126132_, (Item) arr[6].get()).pattern("A").pattern("B").pattern("B").define('A', ingot).define('B', stick).save(pvd, getID((Item) arr[6].get()));
        var10003 = RecipeCategory.TOOLS;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[7].get(), 1)::m_126132_, (Item) arr[7].get()).pattern("AAA").pattern(" B ").pattern(" B ").define('A', ingot).define('B', stick).save(pvd, getID((Item) arr[7].get()));
        var10003 = RecipeCategory.TOOLS;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) arr[8].get(), 1)::m_126132_, (Item) arr[8].get()).pattern("AA").pattern(" B").pattern(" B").define('A', ingot).define('B', stick).save(pvd, getID((Item) arr[8].get()));
        currentFolder = "generated_tools/" + mat.name().toLowerCase(Locale.ROOT) + "/recycle/";
        ingot = mat.getNugget();
        for (int i = 0; i < 9; i++) {
            smelting(pvd, (Item) arr[i].get(), ingot, 0.1F);
        }
        currentFolder = "generated_tools/" + mat.name().toLowerCase(Locale.ROOT) + "/upgrade/";
        stick = mat.getBlock().asItem();
        Consumer<FinishedRecipe> cond = ConditionalRecipeWrapper.of(pvd, BooleanValueCondition.of(LCConfig.COMMON_PATH, LCConfig.COMMON.enableToolRecraftRecipe, true));
        for (int i = 0; i < 9; i++) {
            smithing(pvd, TOOLS[i], stick, (Item) arr[i].get(), cond);
        }
    }

    public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }

    public static void smithing(RegistrateRecipeProvider pvd, TagKey<Item> in, Item mat, Item out) {
        smithing(pvd, in, mat, out, pvd);
    }

    public static void smithing(RegistrateRecipeProvider pvd, TagKey<Item> in, Item mat, Item out, Consumer<FinishedRecipe> cons) {
        Ingredient var10001 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(var10001, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.MISC, out)::m_266439_, mat).save(cons, getID(out));
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
}