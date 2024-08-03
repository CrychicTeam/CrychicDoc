package dev.xkmc.l2weaponry.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2complements.content.enchantment.core.EnchantmentRecipeBuilder;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.compat.jeed.JeedDataGenerator;
import dev.xkmc.l2library.serial.conditions.BooleanValueCondition;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import dev.xkmc.l2weaponry.compat.DDCompat;
import dev.xkmc.l2weaponry.compat.aerial.AHToolMats;
import dev.xkmc.l2weaponry.compat.dragons.DragonToolMats;
import dev.xkmc.l2weaponry.compat.twilightforest.TFToolMats;
import dev.xkmc.l2weaponry.compat.undergarden.UGToolMats;
import dev.xkmc.l2weaponry.init.materials.ILWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeGen {

    private static String currentFolder = "";

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        currentFolder = "misc/";
        RecipeCategory var10003 = RecipeCategory.MISC;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LWItems.HANDLE.get(), 2)::m_126132_, Items.STICK).pattern(" SI").pattern("SIS").pattern("IS ").define('S', Items.STICK).define('I', Items.COPPER_INGOT).save(pvd, getID((Item) LWItems.HANDLE.get()));
        currentFolder = "generated/";
        for (LWToolMats mat : LWToolMats.values()) {
            tools(pvd, mat.getStick(), mat.getToolIngot(), mat);
            if (mat.getNugget() != Items.AIR) {
                currentFolder = "generated/recycle/";
                for (LWToolTypes type : LWToolTypes.values()) {
                    if (mat.hasTool(type)) {
                        smelting(pvd, mat.getTool(type), mat.getNugget(), 0.1F);
                    }
                }
            }
        }
        if (ModList.get().isLoaded("twilightforest")) {
            for (ILWToolMats matx : TFToolMats.values()) {
                tools(pvd, matx.getStick(), matx.getIngot(), matx);
            }
        }
        if (ModList.get().isLoaded("iceandfire")) {
            for (ILWToolMats matx : DragonToolMats.values()) {
                tools(pvd, matx.getStick(), matx.getIngot(), matx);
            }
        }
        if (ModList.get().isLoaded("undergarden")) {
            for (ILWToolMats matx : UGToolMats.values()) {
                tools(pvd, matx.getStick(), matx.getIngot(), matx);
            }
        }
        if (ModList.get().isLoaded("aerialhell")) {
            for (ILWToolMats matx : AHToolMats.values()) {
                tools(pvd, matx.getStick(), matx.getIngot(), matx);
            }
        }
        if (ModList.get().isLoaded("deeperdarker")) {
            currentFolder = "generated/alternate/";
            DDCompat.onRecipeGen(pvd);
        }
        currentFolder = "legendary/";
        var10003 = RecipeCategory.COMBAT;
        unlock(pvd, new ShapedRecipeBuilder(var10003, (ItemLike) LWItems.STORM_JAVELIN.get(), 1)::m_126132_, (Item) LCItems.GUARDIAN_EYE.get()).pattern("cds").pattern("wgd").pattern("jwc").define('d', LWToolMats.POSEIDITE.getTool(LWToolTypes.DAGGER)).define('j', LWToolMats.POSEIDITE.getTool(LWToolTypes.JAVELIN)).define('s', LWToolMats.POSEIDITE.getTool(LWToolTypes.SPEAR)).define('g', (ItemLike) LCItems.GUARDIAN_EYE.get()).define('c', new EnchantmentIngredient(Enchantments.CHANNELING, 1)).define('w', (ItemLike) LCItems.STORM_CORE.get()).save(pvd, getID((Item) LWItems.STORM_JAVELIN.get()));
        smithing(pvd, LWToolMats.IRON.getTool(LWToolTypes.SPEAR), (Item) LCItems.HARD_ICE.get(), (Item) LWItems.FROZEN_SPEAR.get());
        smithing(pvd, LWToolMats.NETHERITE.getTool(LWToolTypes.BATTLE_AXE), (Item) LCItems.SOUL_FLAME.get(), (Item) LWItems.FLAME_AXE.get());
        smithing(pvd, LWToolMats.NETHERITE.getTool(LWToolTypes.HAMMER), (Item) LCItems.BLACKSTONE_CORE.get(), (Item) LWItems.BLACK_HAMMER.get());
        smithing(pvd, LWToolMats.NETHERITE.getTool(LWToolTypes.THROWING_AXE), (Item) LCItems.BLACKSTONE_CORE.get(), (Item) LWItems.BLACK_AXE.get());
        smithing(pvd, LWToolMats.SHULKERATE.getTool(LWToolTypes.SPEAR), (Item) LCItems.VOID_EYE.get(), (Item) LWItems.ENDER_SPEAR.get());
        smithing(pvd, LWToolMats.SHULKERATE.getTool(LWToolTypes.JAVELIN), (Item) LCItems.STORM_CORE.get(), (Item) LWItems.ENDER_JAVELIN.get());
        smithing(pvd, LWToolMats.SHULKERATE.getTool(LWToolTypes.DAGGER), (Item) LCItems.VOID_EYE.get(), (Item) LWItems.ENDER_DAGGER.get());
        smithing(pvd, LWToolMats.SHULKERATE.getTool(LWToolTypes.MACHETE), (Item) LCItems.STORM_CORE.get(), (Item) LWItems.ENDER_MACHETE.get());
        smithing(pvd, LWToolMats.SCULKIUM.getTool(LWToolTypes.DAGGER), (Item) LCItems.RESONANT_FEATHER.get(), (Item) LWItems.ABYSS_DAGGER.get());
        smithing(pvd, LWToolMats.SCULKIUM.getTool(LWToolTypes.MACHETE), (Item) LCItems.RESONANT_FEATHER.get(), (Item) LWItems.ABYSS_MACHETE.get());
        smithing(pvd, LWToolMats.SCULKIUM.getTool(LWToolTypes.HAMMER), (Item) LCItems.RESONANT_FEATHER.get(), (Item) LWItems.ABYSS_HAMMER.get());
        smithing(pvd, LWToolMats.SCULKIUM.getTool(LWToolTypes.BATTLE_AXE), (Item) LCItems.RESONANT_FEATHER.get(), (Item) LWItems.ABYSS_AXE.get());
        smithing(pvd, LWToolMats.TOTEMIC_GOLD.getTool(LWToolTypes.CLAW), (Item) LCItems.CURSED_DROPLET.get(), (Item) LWItems.BLOOD_CLAW.get());
        smithing(pvd, LWToolMats.TOTEMIC_GOLD.getTool(LWToolTypes.BATTLE_AXE), (Item) LCItems.LIFE_ESSENCE.get(), (Item) LWItems.HOLY_AXE.get());
        smithing(pvd, LWToolMats.TOTEMIC_GOLD.getTool(LWToolTypes.HAMMER), (Item) LCItems.SUN_MEMBRANE.get(), (Item) LWItems.HOLY_HAMMER.get());
        smithing(pvd, LWToolMats.ETERNIUM.getTool(LWToolTypes.CLAW), (Item) LCItems.SUN_MEMBRANE.get(), (Item) LWItems.CHEATER_CLAW.get());
        smithing(pvd, LWToolMats.ETERNIUM.getTool(LWToolTypes.MACHETE), (Item) LCItems.VOID_EYE.get(), (Item) LWItems.CHEATER_MACHETE.get());
        currentFolder = "enchantments/";
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.HEAVY.get(), 1)::unlockedBy, Items.ANVIL).pattern("AAA").pattern("CBC").pattern("LDL").define('A', Items.ANVIL).define('B', Items.BOOK).define('C', Items.GOLD_INGOT).define('D', (ItemLike) LWItems.HANDLE.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.HEAVY.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.NO_AGGRO.get(), 1)::unlockedBy, (Item) LCItems.CAPTURED_WIND.get()).pattern("LAL").pattern("CBC").pattern("LCL").define('A', LCItems.CAPTURED_WIND).define('B', Items.BOOK).define('C', LCItems.HARD_ICE).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.NO_AGGRO.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.ENDER_HAND.get(), 1)::unlockedBy, LCMats.POSEIDITE.getIngot()).pattern("LAL").pattern("CBC").pattern("LDL").define('A', LCMats.POSEIDITE.getIngot()).define('B', Items.BOOK).define('C', Items.ENDER_EYE).define('D', (ItemLike) LWItems.HANDLE.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.ENDER_HAND.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.HEAVY_SHIELD.get(), 1)::unlockedBy, Items.ANVIL).pattern("DAD").pattern("CBC").pattern("LDL").define('A', Items.ANVIL).define('B', Items.BOOK).define('D', Items.NETHERITE_INGOT).define('C', (ItemLike) LWItems.HANDLE.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.HEAVY_SHIELD.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.HARD_SHIELD.get(), 1)::unlockedBy, LCMats.SHULKERATE.getIngot()).pattern("DLD").pattern("CBC").pattern("LDL").define('B', Items.BOOK).define('D', LCMats.SHULKERATE.getIngot()).define('C', (ItemLike) LWItems.HANDLE.get()).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.HARD_SHIELD.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.ENERGIZED_WILL.get(), 1)::unlockedBy, (Item) LCItems.SOUL_FLAME.get()).pattern("CLC").pattern("DBD").pattern("CLC").define('B', Items.BOOK).define('C', Items.REDSTONE).define('D', LCItems.SOUL_FLAME).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.ENERGIZED_WILL.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.RAISED_SPIRIT.get(), 1)::unlockedBy, (Item) LCItems.SOUL_FLAME.get()).pattern("CLC").pattern("DBD").pattern("CLC").define('B', Items.BOOK).define('C', Items.GLOWSTONE_DUST).define('D', LCItems.SOUL_FLAME).define('L', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.RAISED_SPIRIT.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.GHOST_SLASH.get(), 1)::unlockedBy, (Item) LCItems.RESONANT_FEATHER.get()).pattern("CLC").pattern("DBD").pattern("CLC").define('B', Items.BOOK).define('L', LCItems.EXPLOSION_SHARD).define('D', LCItems.RESONANT_FEATHER).define('C', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.GHOST_SLASH.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.CLAW_BLOCK.get(), 1)::unlockedBy, (Item) LCItems.WARDEN_BONE_SHARD.get()).pattern("LLL").pattern("DBD").pattern("CCC").define('B', Items.BOOK).define('L', LCItems.WARDEN_BONE_SHARD).define('D', LCItems.EXPLOSION_SHARD).define('C', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.CLAW_BLOCK.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.PROJECTION.get(), 1)::unlockedBy, (Item) LCItems.VOID_EYE.get()).pattern("CBC").pattern("1E2").pattern("LDR").define('1', new EnchantmentIngredient(Enchantments.INFINITY_ARROWS, 1)).define('2', new EnchantmentIngredient(Enchantments.LOYALTY, 1)).define('L', LCItems.VOID_EYE).define('R', LCItems.GUARDIAN_EYE).define('B', Items.ZOMBIE_HEAD).define('D', Items.CONDUIT).define('E', LCMats.POSEIDITE.getIngot()).define('C', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.PROJECTION.get()));
        unlock(pvd, new EnchantmentRecipeBuilder((Enchantment) LWEnchantments.INSTANT_THROWING.get(), 1)::unlockedBy, (Item) LCItems.STORM_CORE.get()).pattern(" B ").pattern("C1C").pattern(" E ").define('1', new EnchantmentIngredient(Enchantments.QUICK_CHARGE, 1)).define('B', LCItems.STORM_CORE).define('E', Items.CROSSBOW).define('C', Items.LAPIS_LAZULI).save(pvd, getID((Enchantment) LWEnchantments.INSTANT_THROWING.get()));
        JeedDataGenerator jeed = new JeedDataGenerator("l2weaponry");
        jeed.add((Item) LWItems.BLACK_HAMMER.get(), (MobEffect) LCEffects.STONE_CAGE.get());
        jeed.add((Item) LWItems.ENDER_JAVELIN.get(), MobEffects.SLOW_FALLING, MobEffects.LEVITATION);
        jeed.add((Item) LWItems.ENDER_MACHETE.get(), MobEffects.SLOW_FALLING, MobEffects.LEVITATION);
        jeed.add((Item) LWItems.FLAME_AXE.get(), (MobEffect) LCEffects.FLAME.get());
        jeed.add((Item) LWItems.FROZEN_SPEAR.get(), (MobEffect) LCEffects.ICE.get());
        jeed.generate(pvd);
        if (ModList.get().isLoaded("twilightforest")) {
            jeed = new JeedDataGenerator("l2weaponry", "twilightforest");
            jeed.add(TFToolMats.IRONWOOD.getTool(LWToolTypes.ROUND_SHIELD), MobEffects.DAMAGE_RESISTANCE);
            jeed.add(TFToolMats.IRONWOOD.getTool(LWToolTypes.PLATE_SHIELD), MobEffects.DAMAGE_RESISTANCE);
            jeed.add(TFToolMats.FIERY.getTool(LWToolTypes.ROUND_SHIELD), MobEffects.FIRE_RESISTANCE);
            jeed.add(TFToolMats.FIERY.getTool(LWToolTypes.PLATE_SHIELD), MobEffects.FIRE_RESISTANCE);
            for (LWToolTypes e : LWToolTypes.values()) {
                if (TFToolMats.STEELEAF.hasTool(e)) {
                    jeed.add(TFToolMats.STEELEAF.getTool(e), (MobEffect) LCEffects.BLEED.get());
                }
            }
            jeed.generate(pvd);
        }
    }

    private static ResourceLocation getID(Enchantment item) {
        return new ResourceLocation("l2weaponry", currentFolder + ForgeRegistries.ENCHANTMENTS.getKey(item).getPath());
    }

    public static ResourceLocation getID(Item item) {
        return new ResourceLocation("l2weaponry", currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath());
    }

    private static ResourceLocation getID(Item item, String suffix) {
        return new ResourceLocation("l2weaponry", currentFolder + ForgeRegistries.ITEMS.getKey(item).getPath() + suffix);
    }

    public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }

    private static void buildTool(RegistrateRecipeProvider pvd, Item handle, Item ingot, ILWToolMats mat, LWToolTypes type, String... strs) {
        if (mat.hasTool(type)) {
            RecipeCategory var10003 = RecipeCategory.TOOLS;
            ShapedRecipeBuilder b = unlock(pvd, new ShapedRecipeBuilder(var10003, mat.getTool(type), 1)::m_126132_, mat.getIngot());
            boolean stick = false;
            boolean leather = false;
            boolean chain = false;
            for (String str : strs) {
                b = b.pattern(str);
                stick |= str.indexOf(72) >= 0;
                leather |= str.indexOf(76) >= 0;
                chain |= str.indexOf(67) >= 0;
            }
            b.define('I', ingot);
            if (stick) {
                b.define('H', handle);
            }
            if (leather) {
                b = b.define('L', Items.LEATHER);
            }
            if (chain) {
                b = b.define('C', Items.CHAIN);
            }
            mat.saveRecipe(b, pvd, type, getID(mat.getTool(type)));
        }
    }

    public static void upgrade(RegistrateRecipeProvider pvd, ILWToolMats base, ILWToolMats mat) {
        currentFolder = "generated/upgrade/";
        for (LWToolTypes t : LWToolTypes.values()) {
            if (mat.hasTool(t)) {
                smithing(pvd, base.getTool(t), mat.getIngot(), mat.getTool(t));
            }
        }
    }

    public static void tools(RegistrateRecipeProvider pvd, Item handle, Item ingot, ILWToolMats mat) {
        ILWToolMats base = mat.getBaseUpgrade();
        if (base != null) {
            upgrade(pvd, base, mat);
        } else {
            currentFolder = "generated/craft/";
            buildTool(pvd, handle, ingot, mat, LWToolTypes.CLAW, "III", "HLH", "H H");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.DAGGER, " I", "H ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.HAMMER, "III", "IHI", " H ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.BATTLE_AXE, "III", "IH ", "H  ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.SPEAR, " II", " HI", "H  ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.MACHETE, "  I", " I ", " H ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.ROUND_SHIELD, " I ", "IHI", " I ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.PLATE_SHIELD, "III", "IHI", " I ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.THROWING_AXE, "II", "IH");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.JAVELIN, "  I", " H ", "I  ");
            buildTool(pvd, handle, ingot, mat, LWToolTypes.NUNCHAKU, " C ", "H H", "I I");
            currentFolder = "generated/upgrade/";
            BooleanValueCondition toggle = BooleanValueCondition.of(LCConfig.COMMON_PATH, LCConfig.COMMON.enableToolRecraftRecipe, true);
            Consumer<FinishedRecipe> cond = mat.getProvider(pvd, toggle);
            for (LWToolTypes t : LWToolTypes.values()) {
                if (mat.hasTool(t)) {
                    Ingredient var10001 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
                    unlock(pvd, SmithingTransformRecipeBuilder.smithing(var10001, Ingredient.of(t.tag), Ingredient.of(mat.getBlock()), RecipeCategory.COMBAT, mat.getTool(t))::m_266439_, mat.getBlock()).save(cond, getID(mat.getTool(t)));
                }
            }
        }
    }

    public static void smithing(RegistrateRecipeProvider pvd, TagKey<Item> in, Item mat, Item out) {
        Ingredient var10001 = AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(var10001, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.COMBAT, out)::m_266439_, mat).save(pvd, getID(out));
    }

    public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
        Ingredient ing = mat == Items.NETHERITE_INGOT ? Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE) : AbstractSmithingRecipe.TEMPLATE_PLACEHOLDER;
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(ing, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.COMBAT, out)::m_266439_, mat).save(pvd, getID(out));
    }

    public static void smelting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
        unlock(pvd, SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::m_126132_, source).save(pvd, getID(source));
    }

    public static void blasting(RegistrateRecipeProvider pvd, Item source, Item result, float experience) {
        unlock(pvd, SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), RecipeCategory.MISC, result, experience, 200)::m_126132_, source).save(pvd, getID(source));
    }
}