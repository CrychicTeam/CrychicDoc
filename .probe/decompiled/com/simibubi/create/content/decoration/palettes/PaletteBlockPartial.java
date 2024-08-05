package com.simibubi.create.content.decoration.palettes;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonnullType;
import java.util.Arrays;
import java.util.function.Supplier;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class PaletteBlockPartial<B extends Block> {

    public static final PaletteBlockPartial<StairBlock> STAIR = new PaletteBlockPartial.Stairs();

    public static final PaletteBlockPartial<SlabBlock> SLAB = new PaletteBlockPartial.Slab(false);

    public static final PaletteBlockPartial<SlabBlock> UNIQUE_SLAB = new PaletteBlockPartial.Slab(true);

    public static final PaletteBlockPartial<WallBlock> WALL = new PaletteBlockPartial.Wall();

    public static final PaletteBlockPartial<?>[] ALL_PARTIALS = new PaletteBlockPartial[] { STAIR, SLAB, WALL };

    public static final PaletteBlockPartial<?>[] FOR_POLISHED = new PaletteBlockPartial[] { STAIR, UNIQUE_SLAB, WALL };

    private String name;

    private PaletteBlockPartial(String name) {
        this.name = name;
    }

    @NonnullType
    public BlockBuilder<B, CreateRegistrate> create(String variantName, PaletteBlockPattern pattern, BlockEntry<? extends Block> block, AllPaletteStoneTypes variant) {
        String patternName = Lang.nonPluralId(pattern.createName(variantName));
        String blockName = patternName + "_" + this.name;
        BlockBuilder<B, CreateRegistrate> blockBuilder = (BlockBuilder<B, CreateRegistrate>) Create.REGISTRATE.block(blockName, p -> this.createBlock(block)).blockstate((c, p) -> this.generateBlockState(c, p, variantName, pattern, block)).recipe((c, p) -> this.createRecipes(variant, block, c, p)).transform(b -> this.transformBlock(b, variantName, pattern));
        ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> itemBuilder = (ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>>) blockBuilder.item().transform(b -> this.transformItem(b, variantName, pattern));
        if (this.canRecycle()) {
            itemBuilder.tag(new TagKey[] { variant.materialTag });
        }
        return (BlockBuilder<B, CreateRegistrate>) itemBuilder.build();
    }

    protected ResourceLocation getTexture(String variantName, PaletteBlockPattern pattern, int index) {
        return PaletteBlockPattern.toLocation(variantName, pattern.getTexture(index));
    }

    protected BlockBuilder<B, CreateRegistrate> transformBlock(BlockBuilder<B, CreateRegistrate> builder, String variantName, PaletteBlockPattern pattern) {
        this.getBlockTags().forEach(xva$0 -> builder.tag(new TagKey[] { xva$0 }));
        return (BlockBuilder<B, CreateRegistrate>) builder.transform(TagGen.pickaxeOnly());
    }

    protected ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> transformItem(ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>> builder, String variantName, PaletteBlockPattern pattern) {
        this.getItemTags().forEach(xva$0 -> builder.tag(new TagKey[] { xva$0 }));
        return builder;
    }

    protected boolean canRecycle() {
        return true;
    }

    protected abstract Iterable<TagKey<Block>> getBlockTags();

    protected abstract Iterable<TagKey<Item>> getItemTags();

    protected abstract B createBlock(Supplier<? extends Block> var1);

    protected abstract void createRecipes(AllPaletteStoneTypes var1, BlockEntry<? extends Block> var2, DataGenContext<Block, ? extends Block> var3, RegistrateRecipeProvider var4);

    protected abstract void generateBlockState(DataGenContext<Block, B> var1, RegistrateBlockstateProvider var2, String var3, PaletteBlockPattern var4, Supplier<? extends Block> var5);

    private static class Slab extends PaletteBlockPartial<SlabBlock> {

        private boolean customSide;

        public Slab(boolean customSide) {
            super("slab");
            this.customSide = customSide;
        }

        protected SlabBlock createBlock(Supplier<? extends Block> block) {
            return new SlabBlock(BlockBehaviour.Properties.copy((BlockBehaviour) block.get()));
        }

        @Override
        protected boolean canRecycle() {
            return false;
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov, String variantName, PaletteBlockPattern pattern, Supplier<? extends Block> block) {
            String name = ctx.getName();
            ResourceLocation mainTexture = this.getTexture(variantName, pattern, 0);
            ResourceLocation sideTexture = this.customSide ? this.getTexture(variantName, pattern, 1) : mainTexture;
            ModelFile bottom = prov.models().slab(name, sideTexture, mainTexture, mainTexture);
            ModelFile top = prov.models().slabTop(name + "_top", sideTexture, mainTexture, mainTexture);
            ModelFile doubleSlab;
            if (this.customSide) {
                doubleSlab = prov.models().cubeColumn(name + "_double", sideTexture, mainTexture);
            } else {
                doubleSlab = prov.models().getExistingFile(prov.modLoc(pattern.createName(variantName)));
            }
            prov.slabBlock((SlabBlock) ctx.get(), bottom, top, doubleSlab);
        }

        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return Arrays.asList(BlockTags.SLABS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return Arrays.asList(ItemTags.SLABS);
        }

        @Override
        protected void createRecipes(AllPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock, DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.slab(DataIngredient.items((Block) patternBlock.get(), new Block[0]), category, c::get, c.getName(), false);
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c::get, 2);
            DataIngredient ingredient = DataIngredient.items((Block) c.get(), new Block[0]);
            ShapelessRecipeBuilder.shapeless(category, (ItemLike) patternBlock.get()).requires(ingredient).requires(ingredient).unlockedBy("has_" + c.getName(), ingredient.getCritereon(p)).m_176500_(p, "create:" + c.getName() + "_recycling");
        }

        @Override
        protected BlockBuilder<SlabBlock, CreateRegistrate> transformBlock(BlockBuilder<SlabBlock, CreateRegistrate> builder, String variantName, PaletteBlockPattern pattern) {
            builder.loot((lt, block) -> lt.m_247577_(block, lt.m_247233_(block)));
            return super.transformBlock((BlockBuilder<B, CreateRegistrate>) builder, variantName, pattern);
        }
    }

    private static class Stairs extends PaletteBlockPartial<StairBlock> {

        public Stairs() {
            super("stairs");
        }

        protected StairBlock createBlock(Supplier<? extends Block> block) {
            return new StairBlock(() -> ((Block) block.get()).defaultBlockState(), BlockBehaviour.Properties.copy((BlockBehaviour) block.get()));
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov, String variantName, PaletteBlockPattern pattern, Supplier<? extends Block> block) {
            prov.stairsBlock((StairBlock) ctx.get(), this.getTexture(variantName, pattern, 0));
        }

        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return Arrays.asList(BlockTags.STAIRS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return Arrays.asList(ItemTags.STAIRS);
        }

        @Override
        protected void createRecipes(AllPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock, DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.stairs(DataIngredient.items((Block) patternBlock.get(), new Block[0]), category, c::get, c.getName(), false);
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c::get, 1);
        }
    }

    private static class Wall extends PaletteBlockPartial<WallBlock> {

        public Wall() {
            super("wall");
        }

        protected WallBlock createBlock(Supplier<? extends Block> block) {
            return new WallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) block.get()).forceSolidOn());
        }

        @Override
        protected ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> transformItem(ItemBuilder<BlockItem, BlockBuilder<WallBlock, CreateRegistrate>> builder, String variantName, PaletteBlockPattern pattern) {
            builder.model((c, p) -> p.wallInventory(c.getName(), this.getTexture(variantName, pattern, 0)));
            return super.transformItem((ItemBuilder<BlockItem, BlockBuilder<B, CreateRegistrate>>) builder, variantName, pattern);
        }

        @Override
        protected void generateBlockState(DataGenContext<Block, WallBlock> ctx, RegistrateBlockstateProvider prov, String variantName, PaletteBlockPattern pattern, Supplier<? extends Block> block) {
            prov.wallBlock((WallBlock) ctx.get(), pattern.createName(variantName), this.getTexture(variantName, pattern, 0));
        }

        @Override
        protected Iterable<TagKey<Block>> getBlockTags() {
            return Arrays.asList(BlockTags.WALLS);
        }

        @Override
        protected Iterable<TagKey<Item>> getItemTags() {
            return Arrays.asList(ItemTags.WALLS);
        }

        @Override
        protected void createRecipes(AllPaletteStoneTypes type, BlockEntry<? extends Block> patternBlock, DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
            RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;
            p.stonecutting(DataIngredient.tag(type.materialTag), category, c::get, 1);
            DataIngredient ingredient = DataIngredient.items((Block) patternBlock.get(), new Block[0]);
            ShapedRecipeBuilder.shaped(category, (ItemLike) c.get(), 6).pattern("XXX").pattern("XXX").define('X', ingredient).unlockedBy("has_" + p.safeName(ingredient), ingredient.getCritereon(p)).save(p, p.safeId((ItemLike) c.get()));
        }
    }
}