package vectorwing.farmersdelight.data;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class ItemTags extends ItemTagsProvider {

    public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockTagProvider, "farmersdelight", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206421_(ModTags.WILD_CROPS, ModTags.WILD_CROPS_ITEM);
        this.m_206421_(net.minecraft.tags.BlockTags.SMALL_FLOWERS, net.minecraft.tags.ItemTags.SMALL_FLOWERS);
        this.m_206424_(net.minecraft.tags.ItemTags.TALL_FLOWERS).add(ModItems.WILD_RICE.get());
        this.m_206424_(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(ModItems.GOLDEN_KNIFE.get());
        this.registerModTags();
        this.registerForgeTags();
        this.registerCompatibilityTags();
    }

    private void registerModTags() {
        this.m_206424_(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
        this.m_206424_(ModTags.STRAW_HARVESTERS).addTag(ModTags.KNIVES);
        this.m_206424_(ModTags.WOLF_PREY).addTag(ForgeTags.RAW_CHICKEN).addTag(ForgeTags.RAW_MUTTON).add(Items.RABBIT);
        this.m_206424_(ModTags.CABBAGE_ROLL_INGREDIENTS).addTag(ForgeTags.RAW_PORK).addTag(ForgeTags.RAW_FISHES).addTag(ForgeTags.RAW_CHICKEN).addTag(ForgeTags.RAW_BEEF).addTag(ForgeTags.RAW_MUTTON).addTag(ForgeTags.EGGS).addTag(Tags.Items.MUSHROOMS).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
        this.m_206424_(ModTags.CANVAS_SIGNS).add(ModItems.CANVAS_SIGN.get()).add(ModItems.WHITE_CANVAS_SIGN.get()).add(ModItems.ORANGE_CANVAS_SIGN.get()).add(ModItems.MAGENTA_CANVAS_SIGN.get()).add(ModItems.LIGHT_BLUE_CANVAS_SIGN.get()).add(ModItems.YELLOW_CANVAS_SIGN.get()).add(ModItems.LIME_CANVAS_SIGN.get()).add(ModItems.PINK_CANVAS_SIGN.get()).add(ModItems.GRAY_CANVAS_SIGN.get()).add(ModItems.LIGHT_GRAY_CANVAS_SIGN.get()).add(ModItems.CYAN_CANVAS_SIGN.get()).add(ModItems.PURPLE_CANVAS_SIGN.get()).add(ModItems.BLUE_CANVAS_SIGN.get()).add(ModItems.BROWN_CANVAS_SIGN.get()).add(ModItems.GREEN_CANVAS_SIGN.get()).add(ModItems.RED_CANVAS_SIGN.get()).add(ModItems.BLACK_CANVAS_SIGN.get());
        this.m_206424_(ModTags.HANGING_CANVAS_SIGNS).add(ModItems.HANGING_CANVAS_SIGN.get()).add(ModItems.WHITE_HANGING_CANVAS_SIGN.get()).add(ModItems.ORANGE_HANGING_CANVAS_SIGN.get()).add(ModItems.MAGENTA_HANGING_CANVAS_SIGN.get()).add(ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get()).add(ModItems.YELLOW_HANGING_CANVAS_SIGN.get()).add(ModItems.LIME_HANGING_CANVAS_SIGN.get()).add(ModItems.PINK_HANGING_CANVAS_SIGN.get()).add(ModItems.GRAY_HANGING_CANVAS_SIGN.get()).add(ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get()).add(ModItems.CYAN_HANGING_CANVAS_SIGN.get()).add(ModItems.PURPLE_HANGING_CANVAS_SIGN.get()).add(ModItems.BLUE_HANGING_CANVAS_SIGN.get()).add(ModItems.BROWN_HANGING_CANVAS_SIGN.get()).add(ModItems.GREEN_HANGING_CANVAS_SIGN.get()).add(ModItems.RED_HANGING_CANVAS_SIGN.get()).add(ModItems.BLACK_HANGING_CANVAS_SIGN.get());
        this.m_206424_(ModTags.WOODEN_CABINETS).add(ModItems.OAK_CABINET.get()).add(ModItems.SPRUCE_CABINET.get()).add(ModItems.BIRCH_CABINET.get()).add(ModItems.JUNGLE_CABINET.get()).add(ModItems.ACACIA_CABINET.get()).add(ModItems.DARK_OAK_CABINET.get()).add(ModItems.MANGROVE_CABINET.get()).add(ModItems.CHERRY_CABINET.get()).add(ModItems.BAMBOO_CABINET.get()).add(ModItems.CRIMSON_CABINET.get()).add(ModItems.WARPED_CABINET.get());
        this.m_206424_(ModTags.CABINETS).addTag(ModTags.WOODEN_CABINETS);
        this.m_206424_(ModTags.OFFHAND_EQUIPMENT).add(Items.SHIELD).m_176839_(new ResourceLocation("create:extendo_grip"));
        this.m_206424_(ModTags.SERVING_CONTAINERS).add(Items.BOWL, Items.GLASS_BOTTLE, Items.BUCKET);
        this.m_206424_(ModTags.FLAT_ON_CUTTING_BOARD).add(Items.TRIDENT, Items.SPYGLASS).m_176839_(new ResourceLocation("supplementaries:quiver")).addOptional(new ResourceLocation("autumnity:turkey")).addOptional(new ResourceLocation("autumnity:cooked_turkey"));
    }

    private void registerForgeTags() {
        this.m_206424_(ForgeTags.BERRIES).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
        this.m_206424_(ForgeTags.BREAD).addTag(ForgeTags.BREAD_WHEAT);
        this.m_206424_(ForgeTags.BREAD_WHEAT).add(Items.BREAD);
        this.m_206424_(ForgeTags.COOKED_BACON).add(ModItems.COOKED_BACON.get());
        this.m_206424_(ForgeTags.COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
        this.m_206424_(ForgeTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
        this.m_206424_(ForgeTags.COOKED_PORK).add(Items.COOKED_PORKCHOP, ModItems.COOKED_BACON.get());
        this.m_206424_(ForgeTags.COOKED_MUTTON).add(Items.COOKED_MUTTON, ModItems.COOKED_MUTTON_CHOPS.get());
        this.m_206424_(ForgeTags.COOKED_EGGS).add(ModItems.FRIED_EGG.get());
        this.m_206424_(ForgeTags.COOKED_FISHES).addTags(new TagKey[] { ForgeTags.COOKED_FISHES_COD, ForgeTags.COOKED_FISHES_SALMON });
        this.m_206424_(ForgeTags.COOKED_FISHES_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
        this.m_206424_(ForgeTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());
        this.m_206424_(ForgeTags.CROPS).addTags(new TagKey[] { ForgeTags.CROPS_CABBAGE, ForgeTags.CROPS_ONION, ForgeTags.CROPS_RICE, ForgeTags.CROPS_TOMATO });
        this.m_206424_(ForgeTags.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
        this.m_206424_(ForgeTags.CROPS_ONION).add(ModItems.ONION.get());
        this.m_206424_(ForgeTags.CROPS_RICE).add(ModItems.RICE.get());
        this.m_206424_(ForgeTags.CROPS_TOMATO).add(ModItems.TOMATO.get());
        this.m_206424_(ForgeTags.DOUGH).add(ModItems.WHEAT_DOUGH.get());
        this.m_206424_(ForgeTags.DOUGH_WHEAT).add(ModItems.WHEAT_DOUGH.get());
        this.m_206424_(ForgeTags.EGGS).add(Items.EGG);
        this.m_206424_(ForgeTags.GRAIN).addTags(new TagKey[] { ForgeTags.GRAIN_WHEAT, ForgeTags.GRAIN_RICE });
        this.m_206424_(ForgeTags.GRAIN_WHEAT).add(Items.WHEAT);
        this.m_206424_(ForgeTags.GRAIN_RICE).add(ModItems.RICE.get());
        this.m_206424_(ForgeTags.MILK).addTags(new TagKey[] { ForgeTags.MILK_BUCKET, ForgeTags.MILK_BOTTLE });
        this.m_206424_(ForgeTags.MILK_BUCKET).add(Items.MILK_BUCKET);
        this.m_206424_(ForgeTags.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());
        this.m_206424_(ForgeTags.PASTA).addTags(new TagKey[] { ForgeTags.PASTA_RAW_PASTA });
        this.m_206424_(ForgeTags.PASTA_RAW_PASTA).add(ModItems.RAW_PASTA.get());
        this.m_206424_(ForgeTags.RAW_BACON).add(ModItems.BACON.get());
        this.m_206424_(ForgeTags.RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
        this.m_206424_(ForgeTags.RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());
        this.m_206424_(ForgeTags.RAW_PORK).add(Items.PORKCHOP, ModItems.BACON.get());
        this.m_206424_(ForgeTags.RAW_MUTTON).add(Items.MUTTON, ModItems.MUTTON_CHOPS.get());
        this.m_206424_(ForgeTags.RAW_FISHES).addTags(new TagKey[] { ForgeTags.RAW_FISHES_COD, ForgeTags.RAW_FISHES_SALMON, ForgeTags.RAW_FISHES_TROPICAL });
        this.m_206424_(ForgeTags.RAW_FISHES_COD).add(Items.COD, ModItems.COD_SLICE.get());
        this.m_206424_(ForgeTags.RAW_FISHES_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
        this.m_206424_(ForgeTags.RAW_FISHES_TROPICAL).add(Items.TROPICAL_FISH);
        this.m_206424_(ForgeTags.SALAD_INGREDIENTS).addTags(new TagKey[] { ForgeTags.SALAD_INGREDIENTS_CABBAGE });
        this.m_206424_(ForgeTags.SALAD_INGREDIENTS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
        this.m_206424_(ForgeTags.SEEDS).addTags(new TagKey[] { ForgeTags.SEEDS_CABBAGE, ForgeTags.SEEDS_RICE, ForgeTags.SEEDS_TOMATO });
        this.m_206424_(ForgeTags.SEEDS_CABBAGE).add(ModItems.CABBAGE_SEEDS.get());
        this.m_206424_(ForgeTags.SEEDS_RICE).add(ModItems.RICE.get());
        this.m_206424_(ForgeTags.SEEDS_TOMATO).add(ModItems.TOMATO_SEEDS.get());
        this.m_206424_(ForgeTags.VEGETABLES).addTags(new TagKey[] { ForgeTags.VEGETABLES_BEETROOT, ForgeTags.VEGETABLES_CARROT, ForgeTags.VEGETABLES_ONION, ForgeTags.VEGETABLES_POTATO, ForgeTags.VEGETABLES_TOMATO });
        this.m_206424_(ForgeTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
        this.m_206424_(ForgeTags.VEGETABLES_CARROT).add(Items.CARROT);
        this.m_206424_(ForgeTags.VEGETABLES_ONION).add(ModItems.ONION.get());
        this.m_206424_(ForgeTags.VEGETABLES_POTATO).add(Items.POTATO);
        this.m_206424_(ForgeTags.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());
        this.m_206424_(ForgeTags.TOOLS).addTags(new TagKey[] { ForgeTags.TOOLS_AXES, ForgeTags.TOOLS_KNIVES, ForgeTags.TOOLS_PICKAXES, ForgeTags.TOOLS_SHOVELS });
        this.m_206424_(ForgeTags.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.NETHERITE_AXE);
        this.m_206424_(ForgeTags.TOOLS_KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
        this.m_206424_(ForgeTags.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.NETHERITE_PICKAXE);
        this.m_206424_(ForgeTags.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.NETHERITE_SHOVEL);
    }

    public void registerCompatibilityTags() {
        this.m_206424_(CompatibilityTags.CREATE_UPRIGHT_ON_BELT).add(ModItems.MILK_BOTTLE.get()).add(ModItems.HOT_COCOA.get()).add(ModItems.APPLE_CIDER.get()).add(ModItems.MELON_JUICE.get()).add(ModItems.PIE_CRUST.get()).add(ModItems.APPLE_PIE.get()).add(ModItems.SWEET_BERRY_CHEESECAKE.get()).add(ModItems.CHOCOLATE_PIE.get());
        this.m_206424_(CompatibilityTags.CREATE_CA_PLANT_FOODS).add(ModItems.PUMPKIN_SLICE.get()).add(ModItems.ROTTEN_TOMATO.get()).add(ModItems.RICE_PANICLE.get());
        this.m_206424_(CompatibilityTags.CREATE_CA_PLANTS).add(ModItems.SANDY_SHRUB.get()).add(ModItems.BROWN_MUSHROOM_COLONY.get()).add(ModItems.RED_MUSHROOM_COLONY.get());
        this.m_206424_(CompatibilityTags.ORIGINS_MEAT).add(ModItems.FRIED_EGG.get()).add(ModItems.COD_SLICE.get()).add(ModItems.COOKED_COD_SLICE.get()).add(ModItems.SALMON_SLICE.get()).add(ModItems.COOKED_SALMON_SLICE.get()).add(ModItems.BACON_AND_EGGS.get());
        this.m_206424_(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS).add(ModItems.CABBAGE_SEEDS.get()).add(ModItems.ONION.get()).add(ModItems.RICE.get());
        this.m_206424_(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS).add(ModItems.ONION.get());
        this.m_206424_(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS).add(ModItems.TOMATO_SEEDS.get()).add(ModItems.RICE.get());
        this.m_206424_(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS).add(ModItems.CABBAGE_SEEDS.get());
        this.m_206424_(CompatibilityTags.TINKERS_CONSTRUCT_SEEDS).add(ModItems.ONION.get());
    }
}