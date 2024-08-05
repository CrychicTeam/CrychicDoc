package dev.xkmc.modulargolems.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2library.serial.ingredients.EnchantmentIngredient;
import dev.xkmc.l2library.serial.recipe.NBTRecipe;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.content.item.card.NameFilterCard;
import dev.xkmc.modulargolems.content.recipe.GolemAssembleBuilder;
import dev.xkmc.modulargolems.init.material.GolemWeaponType;
import dev.xkmc.modulargolems.init.material.VanillaGolemWeaponMaterial;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class RecipeGen {

    public static void genRecipe(RegistrateRecipeProvider pvd) {
        ITagManager<Item> manager = (ITagManager<Item>) Objects.requireNonNull(ForgeRegistries.ITEMS.tags());
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.GOLEM_TEMPLATE.get())::m_126132_, Items.CLAY).pattern("CBC").pattern("BAB").pattern("CBC").define('A', Items.COPPER_INGOT).define('B', Items.STICK).define('C', Items.CLAY_BALL).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.RETRIEVAL_WAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" ET").pattern(" SE").pattern("S  ").define('E', Items.ENDER_PEARL).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.DISPENSE_WAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" ET").pattern(" SE").pattern("S  ").define('E', Items.DISPENSER).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.COMMAND_WAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" ET").pattern(" SE").pattern("S  ").define('E', Items.GOLD_INGOT).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.RIDER_WAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" ET").pattern(" SE").pattern("S  ").define('E', Items.WHITE_BANNER).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.SQUAD_WAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern("  T").pattern(" E ").pattern("S  ").define('E', Items.WHITE_BANNER).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.OMNI_COMMAND.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" 1T").pattern("2S3").pattern("S4 ").define('1', (ItemLike) GolemItems.COMMAND_WAND.get()).define('2', (ItemLike) GolemItems.DISPENSE_WAND.get()).define('3', (ItemLike) GolemItems.RETRIEVAL_WAND.get()).define('4', (ItemLike) GolemItems.RIDER_WAND.get()).define('S', Items.GOLD_INGOT).define('T', Items.REDSTONE_BLOCK).m_176498_(pvd);
        RecipeCategory var10001 = RecipeCategory.MISC;
        unlock(pvd, ShapedRecipeBuilder.shaped(var10001, (ItemLike) GolemItems.EMPTY_UPGRADE.get(), 4)::m_126132_, Items.AMETHYST_SHARD).pattern("CBC").pattern("BAB").pattern("CBC").define('A', Items.AMETHYST_SHARD).define('B', Items.IRON_INGOT).define('C', Items.CLAY_BALL).m_176498_(pvd);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.GOLEM_BODY);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.GOLEM_ARM);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.GOLEM_LEGS);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.HUMANOID_BODY);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.HUMANOID_ARMS);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.HUMANOID_LEGS);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.DOG_BODY);
        pvd.stonecutting(DataIngredient.items((Item) GolemItems.GOLEM_TEMPLATE.get(), new Item[0]), RecipeCategory.MISC, GolemItems.DOG_LEGS);
        unlock(pvd, new GolemAssembleBuilder((ItemLike) GolemItems.HOLDER_GOLEM.get(), 1)::m_126132_, (Item) GolemItems.GOLEM_BODY.get()).pattern("ABA").pattern(" L ").define('A', (ItemLike) GolemItems.GOLEM_ARM.get()).define('B', (ItemLike) GolemItems.GOLEM_BODY.get()).define('L', (ItemLike) GolemItems.GOLEM_LEGS.get()).m_176498_(pvd);
        unlock(pvd, new GolemAssembleBuilder((ItemLike) GolemItems.HOLDER_HUMANOID.get(), 1)::m_126132_, (Item) GolemItems.HUMANOID_BODY.get()).pattern("A").pattern("B").pattern("C").define('A', (ItemLike) GolemItems.HUMANOID_BODY.get()).define('B', (ItemLike) GolemItems.HUMANOID_ARMS.get()).define('C', (ItemLike) GolemItems.HUMANOID_LEGS.get()).m_176498_(pvd);
        unlock(pvd, new GolemAssembleBuilder((ItemLike) GolemItems.HOLDER_DOG.get(), 1)::m_126132_, (Item) GolemItems.HUMANOID_BODY.get()).pattern("A").pattern("B").define('A', (ItemLike) GolemItems.DOG_BODY.get()).define('B', (ItemLike) GolemItems.DOG_LEGS.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.CARD[DyeColor.WHITE.getId()].get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).pattern(" P ").pattern("PTP").pattern(" P ").define('P', Items.PAPER).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).save(pvd, new ResourceLocation("modulargolems", "craft_config_card"));
        for (int i = 0; i < 16; i++) {
            Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(DyeColor.byId(i).getName() + "_dye"));
            assert dye != null;
            unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD[i].get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires(MGTagGen.CONFIG_CARD).requires(dye).m_176498_(pvd);
        }
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_PATH.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.MAP).requires(Items.INK_SAC).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_NAME.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.BOOK).requires(Items.INK_SAC).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_NAME.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.BOOK).requires(Items.INK_SAC).requires(Items.PAPER).save(e -> pvd.accept(new NBTRecipe(e, NameFilterCard.getFriendly())), new ResourceLocation("modulargolems", "target_filter_friendly"));
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_DEF.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.PAPER).requires(Items.IRON_INGOT).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_TYPE.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.PAPER).requires(Items.CLAY_BALL).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.CARD_UUID.get())::m_126132_, (Item) GolemItems.GOLEM_TEMPLATE.get()).requires((ItemLike) GolemItems.GOLEM_TEMPLATE.get()).requires(Items.PAPER).requires(Items.INK_SAC).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.GOLEMGUARD_HELMET.get())::m_126132_, Items.IRON_INGOT).pattern(" B ").pattern("III").pattern("IAI").define('I', Items.IRON_HELMET).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.REDSTONE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.GOLEMGUARD_CHESTPLATE.get())::m_126132_, Items.IRON_INGOT).pattern("IAI").pattern("III").pattern("BIB").define('I', Items.IRON_CHESTPLATE).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.REDSTONE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.GOLEMGUARD_SHINGUARD.get())::m_126132_, Items.IRON_INGOT).pattern("BIB").pattern(" A ").pattern("I I").define('I', Items.IRON_LEGGINGS).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.REDSTONE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.WINDSPIRIT_HELMET.get())::m_126132_, Items.DIAMOND).pattern(" B ").pattern("III").pattern("IAI").define('I', Items.DIAMOND_HELMET).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.LAPIS_LAZULI).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.WINDSPIRIT_CHESTPLATE.get())::m_126132_, Items.DIAMOND).pattern("IAI").pattern("III").pattern("BIB").define('I', Items.DIAMOND_CHESTPLATE).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.LAPIS_LAZULI).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.WINDSPIRIT_SHINGUARD.get())::m_126132_, Items.DIAMOND).pattern("BIB").pattern(" A ").pattern("I I").define('I', Items.DIAMOND_LEGGINGS).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.LAPIS_LAZULI).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.BARBARICFLAMEVANGUARD_HELMET.get())::m_126132_, Items.DIAMOND).pattern(" B ").pattern("III").pattern("IAI").define('I', Items.NETHERITE_HELMET).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.QUARTZ).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.BARBARICFLAMEVANGUARD_CHESTPLATE.get())::m_126132_, Items.DIAMOND).pattern("IAI").pattern("III").pattern("BIB").define('I', Items.NETHERITE_CHESTPLATE).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.QUARTZ).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, (ItemLike) GolemItems.BARBARICFLAMEVANGUARD_SHINGUARD.get())::m_126132_, Items.DIAMOND).pattern("BIB").pattern(" A ").pattern("I I").define('I', Items.NETHERITE_LEGGINGS).define('A', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).define('B', Items.QUARTZ).m_176498_(pvd);
        for (GolemWeaponType type : GolemWeaponType.values()) {
            for (VanillaGolemWeaponMaterial mat : VanillaGolemWeaponMaterial.values()) {
                Item item = (Item) GolemItems.METALGOLEM_WEAPON[type.ordinal()][mat.ordinal()].get();
                if (mat == VanillaGolemWeaponMaterial.NETHERITE) {
                    Item prev = (Item) GolemItems.METALGOLEM_WEAPON[type.ordinal()][VanillaGolemWeaponMaterial.DIAMOND.ordinal()].get();
                    smithing(pvd, prev, mat.getIngot(), item);
                } else {
                    type.pattern(unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, item)::m_126132_, mat.getIngot())).define('I', mat.getIngot()).define('S', Items.STICK).define('T', (ItemLike) GolemItems.GOLEM_TEMPLATE.get()).m_176498_(pvd);
                }
            }
        }
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.FIRE_IMMUNE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', Items.MAGMA_CREAM).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.THUNDER_IMMUNE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', Items.LIGHTNING_ROD).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.RECYCLE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern(" C ").pattern("ABA").pattern(" D ").define('A', Items.ENDER_PEARL).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.TOTEM_OF_UNDYING).define('D', Items.RESPAWN_ANCHOR).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.DIAMOND.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CCC").pattern("CBC").pattern("CCC").define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.DIAMOND).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.NETHERITE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', Items.NETHERITE_INGOT).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.DIAMOND).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.QUARTZ.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', Items.QUARTZ_BLOCK).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.QUARTZ).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.GOLD.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', Items.GOLDEN_APPLE).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.GOLDEN_CARROT).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.SPONGE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern(" A ").pattern("ABA").pattern(" A ").define('A', Items.WET_SPONGE).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.ENCHANTED_GOLD.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.ENCHANTED_GOLDEN_APPLE).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.FLOAT.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(ItemTags.BOATS).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.SWIM.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.HEART_OF_THE_SEA).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.PLAYER_IMMUNE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.NETHER_STAR).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.BELL.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.BELL).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.ENDER_SIGHT.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.ENDER_EYE).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.SPEED.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.RABBIT_FOOT).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.WEAK.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CDC").pattern("ABA").pattern("CDC").define('A', Items.DRAGON_BREATH).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.FERMENTED_SPIDER_EYE).define('D', Items.REDSTONE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.SLOW.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CDC").pattern("ABA").pattern("EDE").define('A', Items.DRAGON_BREATH).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.FERMENTED_SPIDER_EYE).define('D', Items.REDSTONE).define('E', Items.SUGAR).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.WITHER.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CDC").pattern("ABA").pattern("CDC").define('A', Items.DRAGON_BREATH).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.WITHER_ROSE).define('D', Items.REDSTONE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.EMERALD.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', Items.EMERALD_BLOCK).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.EMERALD).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.PICKUP.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("AAA").pattern("DBD").pattern(" C ").define('A', Items.HOPPER).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.LAVA_BUCKET).define('D', Items.ENDER_PEARL).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.PICKUP_MENDING.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(new EnchantmentIngredient(Enchantments.MENDING, 1)).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.PICKUP_NO_DESTROY.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.ZOMBIE_HEAD).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.TALENTED.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CEC").pattern("ABA").pattern("CAC").define('E', Items.NETHER_STAR).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Tags.Items.HEADS).define('A', Items.CHORUS_FLOWER).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.CAULDRON.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CEC").pattern("ABA").pattern("CDC").define('A', Items.NETHER_STAR).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.DRAGON_BREATH).define('D', Items.CAULDRON).define('E', Items.DRAGON_HEAD).m_176498_(pvd);
        unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, (ItemLike) GolemItems.MOUNT_UPGRADE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).requires((ItemLike) GolemItems.EMPTY_UPGRADE.get()).requires(Items.SADDLE).m_176498_(pvd);
        unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, (ItemLike) GolemItems.SIZE_UPGRADE.get())::m_126132_, (Item) GolemItems.EMPTY_UPGRADE.get()).pattern("CAC").pattern("ABA").pattern("CAC").define('A', Items.IRON_BLOCK).define('B', (ItemLike) GolemItems.EMPTY_UPGRADE.get()).define('C', Items.COPPER_BLOCK).m_176498_(pvd);
        CompatManager.dispatchGenRecipe(pvd);
    }

    public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
        return (T) func.apply("has_" + pvd.safeName(item), DataIngredient.items(item, new Item[0]).getCritereon(pvd));
    }

    public static void smithing(RegistrateRecipeProvider pvd, Item in, Item mat, Item out) {
        Ingredient ing = Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE);
        unlock(pvd, SmithingTransformRecipeBuilder.smithing(ing, Ingredient.of(in), Ingredient.of(mat), RecipeCategory.COMBAT, out)::m_266439_, mat).save(pvd, getID(out));
    }

    private static ResourceLocation getID(Item item) {
        return new ResourceLocation("modulargolems", ForgeRegistries.ITEMS.getKey(item).getPath());
    }
}