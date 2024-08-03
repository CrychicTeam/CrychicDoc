package com.simibubi.create.foundation.block;

import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraft.world.level.block.WeatheringCopperSlabBlock;
import net.minecraft.world.level.block.WeatheringCopperStairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.ArrayUtils;

public class CopperBlockSet {

    protected static final WeatheringCopper.WeatherState[] WEATHER_STATES = WeatheringCopper.WeatherState.values();

    protected static final int WEATHER_STATE_COUNT = WEATHER_STATES.length;

    protected static final Map<WeatheringCopper.WeatherState, Supplier<Block>> BASE_BLOCKS = new EnumMap(WeatheringCopper.WeatherState.class);

    public static final CopperBlockSet.Variant<?>[] DEFAULT_VARIANTS = new CopperBlockSet.Variant[] { CopperBlockSet.BlockVariant.INSTANCE, CopperBlockSet.SlabVariant.INSTANCE, CopperBlockSet.StairVariant.INSTANCE };

    protected final String name;

    protected final String generalDirectory;

    protected final CopperBlockSet.Variant<?>[] variants;

    protected final Map<CopperBlockSet.Variant<?>, BlockEntry<?>[]> entries = new HashMap();

    protected final NonNullBiConsumer<DataGenContext<Block, ?>, RegistrateRecipeProvider> mainBlockRecipe;

    protected final String endTextureName;

    public CopperBlockSet(AbstractRegistrate<?> registrate, String name, String endTextureName, CopperBlockSet.Variant<?>[] variants) {
        this(registrate, name, endTextureName, variants, NonNullBiConsumer.noop(), "copper/");
    }

    public CopperBlockSet(AbstractRegistrate<?> registrate, String name, String endTextureName, CopperBlockSet.Variant<?>[] variants, String generalDirectory) {
        this(registrate, name, endTextureName, variants, NonNullBiConsumer.noop(), generalDirectory);
    }

    public CopperBlockSet(AbstractRegistrate<?> registrate, String name, String endTextureName, CopperBlockSet.Variant<?>[] variants, NonNullBiConsumer<DataGenContext<Block, ?>, RegistrateRecipeProvider> mainBlockRecipe) {
        this(registrate, name, endTextureName, variants, mainBlockRecipe, "copper/");
    }

    public CopperBlockSet(AbstractRegistrate<?> registrate, String name, String endTextureName, CopperBlockSet.Variant<?>[] variants, NonNullBiConsumer<DataGenContext<Block, ?>, RegistrateRecipeProvider> mainBlockRecipe, String generalDirectory) {
        this.name = name;
        this.generalDirectory = generalDirectory;
        this.endTextureName = endTextureName;
        this.variants = variants;
        this.mainBlockRecipe = mainBlockRecipe;
        for (boolean waxed : Iterate.falseAndTrue) {
            for (CopperBlockSet.Variant<?> variant : this.variants) {
                BlockEntry<?>[] entries = waxed ? (BlockEntry[]) this.entries.get(variant) : new BlockEntry[WEATHER_STATE_COUNT * 2];
                for (WeatheringCopper.WeatherState state : WEATHER_STATES) {
                    int index = this.getIndex(state, waxed);
                    BlockEntry<?> entry = this.createEntry(registrate, variant, state, waxed);
                    entries[index] = entry;
                    if (waxed) {
                        CopperRegistries.addWaxable(() -> (Block) entries[this.getIndex(state, false)].get(), () -> (Block) entry.get());
                    } else if (state != WeatheringCopper.WeatherState.UNAFFECTED) {
                        CopperRegistries.addWeathering(() -> (Block) entries[this.getIndex(WEATHER_STATES[state.ordinal() - 1], false)].get(), () -> (Block) entry.get());
                    }
                }
                if (!waxed) {
                    this.entries.put(variant, entries);
                }
            }
        }
    }

    protected <T extends Block> BlockEntry<?> createEntry(AbstractRegistrate<?> registrate, CopperBlockSet.Variant<T> variant, WeatheringCopper.WeatherState state, boolean waxed) {
        String name = "";
        if (waxed) {
            name = name + "waxed_";
        }
        name = name + getWeatherStatePrefix(state);
        name = name + this.name;
        String suffix = variant.getSuffix();
        if (!suffix.equals("")) {
            name = Lang.nonPluralId(name);
        }
        name = name + suffix;
        Supplier<Block> baseBlock = (Supplier<Block>) BASE_BLOCKS.get(state);
        BlockBuilder<T, ?> builder = ((BlockBuilder) registrate.block(name, variant.getFactory(this, state, waxed)).initialProperties(() -> (Block) baseBlock.get()).loot((lt, block) -> variant.generateLootTable(lt, (T) block, this, state, waxed)).blockstate((ctx, prov) -> variant.generateBlockState(ctx, prov, this, state, waxed)).recipe((c, p) -> variant.generateRecipes(((BlockEntry[]) this.entries.get(CopperBlockSet.BlockVariant.INSTANCE))[state.ordinal()], c, p)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { BlockTags.NEEDS_STONE_TOOL }).simpleItem();
        if (variant == CopperBlockSet.BlockVariant.INSTANCE && state == WeatheringCopper.WeatherState.UNAFFECTED) {
            builder.recipe((c, p) -> this.mainBlockRecipe.accept(c, p));
        }
        if (waxed) {
            builder.recipe((ctx, prov) -> {
                Block unwaxed = (Block) this.get(variant, state, false).get();
                ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, (ItemLike) ctx.get()).requires(unwaxed).requires(Items.HONEYCOMB).unlockedBy("has_unwaxed", RegistrateRecipeProvider.m_125977_(unwaxed)).save(prov, new ResourceLocation(ctx.getId().getNamespace(), "crafting/" + this.generalDirectory + ctx.getName() + "_from_honeycomb"));
            });
        }
        return builder.register();
    }

    protected int getIndex(WeatheringCopper.WeatherState state, boolean waxed) {
        return state.ordinal() + (waxed ? WEATHER_STATE_COUNT : 0);
    }

    public String getName() {
        return this.name;
    }

    public String getEndTextureName() {
        return this.endTextureName;
    }

    public CopperBlockSet.Variant<?>[] getVariants() {
        return this.variants;
    }

    public boolean hasVariant(CopperBlockSet.Variant<?> variant) {
        return ArrayUtils.contains(this.variants, variant);
    }

    public BlockEntry<?> get(CopperBlockSet.Variant<?> variant, WeatheringCopper.WeatherState state, boolean waxed) {
        BlockEntry<?>[] entries = (BlockEntry<?>[]) this.entries.get(variant);
        return entries != null ? entries[this.getIndex(state, waxed)] : null;
    }

    public BlockEntry<?> getStandard() {
        return this.get(CopperBlockSet.BlockVariant.INSTANCE, WeatheringCopper.WeatherState.UNAFFECTED, false);
    }

    public static String getWeatherStatePrefix(WeatheringCopper.WeatherState state) {
        return state != WeatheringCopper.WeatherState.UNAFFECTED ? state.name().toLowerCase(Locale.ROOT) + "_" : "";
    }

    static {
        BASE_BLOCKS.put(WeatheringCopper.WeatherState.UNAFFECTED, (Supplier) () -> Blocks.COPPER_BLOCK);
        BASE_BLOCKS.put(WeatheringCopper.WeatherState.EXPOSED, (Supplier) () -> Blocks.EXPOSED_COPPER);
        BASE_BLOCKS.put(WeatheringCopper.WeatherState.WEATHERED, (Supplier) () -> Blocks.WEATHERED_COPPER);
        BASE_BLOCKS.put(WeatheringCopper.WeatherState.OXIDIZED, (Supplier) () -> Blocks.OXIDIZED_COPPER);
    }

    public static class BlockVariant implements CopperBlockSet.Variant<Block> {

        public static final CopperBlockSet.BlockVariant INSTANCE = new CopperBlockSet.BlockVariant();

        protected BlockVariant() {
        }

        @Override
        public String getSuffix() {
            return "";
        }

        @Override
        public NonNullFunction<BlockBehaviour.Properties, Block> getFactory(CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            return waxed ? Block::new : p -> new WeatheringCopperFullBlock(state, p);
        }

        @Override
        public void generateBlockState(DataGenContext<Block, Block> ctx, RegistrateBlockstateProvider prov, CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            Block block = (Block) ctx.get();
            String path = RegisteredObjects.getKeyOrThrow(block).getPath();
            String baseLoc = "block/" + blocks.generalDirectory + CopperBlockSet.getWeatherStatePrefix(state);
            ResourceLocation texture = prov.modLoc(baseLoc + blocks.getName());
            if (Objects.equals(blocks.getName(), blocks.getEndTextureName())) {
                prov.simpleBlock(block, prov.models().cubeAll(path, texture));
            } else {
                ResourceLocation endTexture = prov.modLoc(baseLoc + blocks.getEndTextureName());
                prov.simpleBlock(block, prov.models().cubeColumn(path, texture, endTexture));
            }
        }

        @Override
        public void generateRecipes(BlockEntry<?> blockVariant, DataGenContext<Block, Block> ctx, RegistrateRecipeProvider prov) {
        }
    }

    public static class SlabVariant implements CopperBlockSet.Variant<SlabBlock> {

        public static final CopperBlockSet.SlabVariant INSTANCE = new CopperBlockSet.SlabVariant();

        protected SlabVariant() {
        }

        @Override
        public String getSuffix() {
            return "_slab";
        }

        @Override
        public NonNullFunction<BlockBehaviour.Properties, SlabBlock> getFactory(CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            return waxed ? SlabBlock::new : p -> new WeatheringCopperSlabBlock(state, p);
        }

        public void generateLootTable(RegistrateBlockLootTables lootTable, SlabBlock block, CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            lootTable.m_247577_(block, lootTable.m_247233_(block));
        }

        @Override
        public void generateBlockState(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockstateProvider prov, CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            ResourceLocation fullModel = prov.modLoc("block/" + CopperBlockSet.getWeatherStatePrefix(state) + blocks.getName());
            String baseLoc = "block/" + blocks.generalDirectory + CopperBlockSet.getWeatherStatePrefix(state);
            ResourceLocation texture = prov.modLoc(baseLoc + blocks.getName());
            ResourceLocation endTexture = prov.modLoc(baseLoc + blocks.getEndTextureName());
            prov.slabBlock((SlabBlock) ctx.get(), fullModel, texture, endTexture, endTexture);
        }

        @Override
        public void generateRecipes(BlockEntry<?> blockVariant, DataGenContext<Block, SlabBlock> ctx, RegistrateRecipeProvider prov) {
            prov.slab(DataIngredient.items((Block) blockVariant.get(), new Block[0]), RecipeCategory.BUILDING_BLOCKS, ctx::get, null, true);
        }
    }

    public static class StairVariant implements CopperBlockSet.Variant<StairBlock> {

        public static final CopperBlockSet.StairVariant INSTANCE = new CopperBlockSet.StairVariant(CopperBlockSet.BlockVariant.INSTANCE);

        protected final CopperBlockSet.Variant<?> parent;

        protected StairVariant(CopperBlockSet.Variant<?> parent) {
            this.parent = parent;
        }

        @Override
        public String getSuffix() {
            return "_stairs";
        }

        @Override
        public NonNullFunction<BlockBehaviour.Properties, StairBlock> getFactory(CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            if (!blocks.hasVariant(this.parent)) {
                throw new IllegalStateException("Cannot add StairVariant '" + this.toString() + "' without parent Variant '" + this.parent.toString() + "'!");
            } else {
                Supplier<BlockState> defaultStateSupplier = () -> blocks.get(this.parent, state, waxed).getDefaultState();
                return waxed ? p -> new StairBlock(defaultStateSupplier, p) : p -> {
                    WeatheringCopperStairBlock block = new WeatheringCopperStairBlock(state, Blocks.AIR.defaultBlockState(), p);
                    ObfuscationReflectionHelper.setPrivateValue(StairBlock.class, block, defaultStateSupplier, "stateSupplier");
                    return block;
                };
            }
        }

        @Override
        public void generateBlockState(DataGenContext<Block, StairBlock> ctx, RegistrateBlockstateProvider prov, CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            String baseLoc = "block/" + blocks.generalDirectory + CopperBlockSet.getWeatherStatePrefix(state);
            ResourceLocation texture = prov.modLoc(baseLoc + blocks.getName());
            ResourceLocation endTexture = prov.modLoc(baseLoc + blocks.getEndTextureName());
            prov.stairsBlock((StairBlock) ctx.get(), texture, endTexture, endTexture);
        }

        @Override
        public void generateRecipes(BlockEntry<?> blockVariant, DataGenContext<Block, StairBlock> ctx, RegistrateRecipeProvider prov) {
            prov.stairs(DataIngredient.items((Block) blockVariant.get(), new Block[0]), RecipeCategory.BUILDING_BLOCKS, ctx::get, null, true);
        }
    }

    public interface Variant<T extends Block> {

        String getSuffix();

        NonNullFunction<BlockBehaviour.Properties, T> getFactory(CopperBlockSet var1, WeatheringCopper.WeatherState var2, boolean var3);

        default void generateLootTable(RegistrateBlockLootTables lootTable, T block, CopperBlockSet blocks, WeatheringCopper.WeatherState state, boolean waxed) {
            lootTable.m_245724_(block);
        }

        void generateRecipes(BlockEntry<?> var1, DataGenContext<Block, T> var2, RegistrateRecipeProvider var3);

        void generateBlockState(DataGenContext<Block, T> var1, RegistrateBlockstateProvider var2, CopperBlockSet var3, WeatheringCopper.WeatherState var4, boolean var5);
    }
}