package com.simibubi.create.content.decoration.palettes;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.block.connected.RotatedPillarCTBehaviour;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class PaletteBlockPattern {

    public static final PaletteBlockPattern CUT = create("cut", PaletteBlockPattern.PatternNameType.PREFIX, PaletteBlockPartial.ALL_PARTIALS);

    public static final PaletteBlockPattern BRICKS = create("cut_bricks", PaletteBlockPattern.PatternNameType.WRAP, PaletteBlockPartial.ALL_PARTIALS).textures("brick");

    public static final PaletteBlockPattern SMALL_BRICKS = create("small_bricks", PaletteBlockPattern.PatternNameType.WRAP, PaletteBlockPartial.ALL_PARTIALS).textures("small_brick");

    public static final PaletteBlockPattern POLISHED = create("polished_cut", PaletteBlockPattern.PatternNameType.PREFIX, PaletteBlockPartial.FOR_POLISHED).textures("polished", "slab");

    public static final PaletteBlockPattern LAYERED = create("layered", PaletteBlockPattern.PatternNameType.PREFIX).blockStateFactory(p -> p::cubeColumn).textures("layered", "cap").connectedTextures(v -> new HorizontalCTBehaviour(ct(v, PaletteBlockPattern.CTs.LAYERED), ct(v, PaletteBlockPattern.CTs.CAP)));

    public static final PaletteBlockPattern PILLAR = create("pillar", PaletteBlockPattern.PatternNameType.SUFFIX).blockStateFactory(p -> p::pillar).block(ConnectedPillarBlock::new).textures("pillar", "cap").connectedTextures(v -> new RotatedPillarCTBehaviour(ct(v, PaletteBlockPattern.CTs.PILLAR), ct(v, PaletteBlockPattern.CTs.CAP)));

    public static final PaletteBlockPattern[] VANILLA_RANGE = new PaletteBlockPattern[] { CUT, POLISHED, BRICKS, SMALL_BRICKS, LAYERED, PILLAR };

    public static final PaletteBlockPattern[] STANDARD_RANGE = new PaletteBlockPattern[] { CUT, POLISHED, BRICKS, SMALL_BRICKS, LAYERED, PILLAR };

    static final String TEXTURE_LOCATION = "block/palettes/stone_types/%s/%s";

    private PaletteBlockPattern.PatternNameType nameType;

    private String[] textures;

    private String id;

    private boolean isTranslucent;

    private TagKey<Block>[] blockTags;

    private TagKey<Item>[] itemTags;

    private Optional<Function<String, ConnectedTextureBehaviour>> ctFactory;

    private PaletteBlockPattern.IPatternBlockStateGenerator blockStateGenerator;

    private NonNullFunction<BlockBehaviour.Properties, ? extends Block> blockFactory;

    private NonNullFunction<NonNullSupplier<Block>, NonNullBiConsumer<DataGenContext<Block, ? extends Block>, RegistrateRecipeProvider>> additionalRecipes;

    private PaletteBlockPartial<? extends Block>[] partials;

    @OnlyIn(Dist.CLIENT)
    private RenderType renderType;

    private static PaletteBlockPattern create(String name, PaletteBlockPattern.PatternNameType nameType, PaletteBlockPartial<?>... partials) {
        PaletteBlockPattern pattern = new PaletteBlockPattern();
        pattern.id = name;
        pattern.ctFactory = Optional.empty();
        pattern.nameType = nameType;
        pattern.partials = (PaletteBlockPartial<? extends Block>[]) partials;
        pattern.additionalRecipes = $ -> NonNullBiConsumer.noop();
        pattern.isTranslucent = false;
        pattern.blockFactory = Block::new;
        pattern.textures = new String[] { name };
        pattern.blockStateGenerator = p -> p::cubeAll;
        return pattern;
    }

    public PaletteBlockPattern.IPatternBlockStateGenerator getBlockStateGenerator() {
        return this.blockStateGenerator;
    }

    public boolean isTranslucent() {
        return this.isTranslucent;
    }

    public TagKey<Block>[] getBlockTags() {
        return this.blockTags;
    }

    public TagKey<Item>[] getItemTags() {
        return this.itemTags;
    }

    public NonNullFunction<BlockBehaviour.Properties, ? extends Block> getBlockFactory() {
        return this.blockFactory;
    }

    public PaletteBlockPartial<? extends Block>[] getPartials() {
        return this.partials;
    }

    public String getTexture(int index) {
        return this.textures[index];
    }

    public void addRecipes(NonNullSupplier<Block> baseBlock, DataGenContext<Block, ? extends Block> c, RegistrateRecipeProvider p) {
        ((NonNullBiConsumer) this.additionalRecipes.apply(baseBlock)).accept(c, p);
    }

    public Optional<Supplier<ConnectedTextureBehaviour>> createCTBehaviour(String variant) {
        return this.ctFactory.map(d -> () -> (ConnectedTextureBehaviour) d.apply(variant));
    }

    private PaletteBlockPattern blockStateFactory(PaletteBlockPattern.IPatternBlockStateGenerator factory) {
        this.blockStateGenerator = factory;
        return this;
    }

    private PaletteBlockPattern textures(String... textures) {
        this.textures = textures;
        return this;
    }

    private PaletteBlockPattern block(NonNullFunction<BlockBehaviour.Properties, ? extends Block> blockFactory) {
        this.blockFactory = blockFactory;
        return this;
    }

    private PaletteBlockPattern connectedTextures(Function<String, ConnectedTextureBehaviour> factory) {
        this.ctFactory = Optional.of(factory);
        return this;
    }

    public PaletteBlockPattern.IBlockStateProvider cubeAll(String variant) {
        ResourceLocation all = toLocation(variant, this.textures[0]);
        return (ctx, prov) -> prov.simpleBlock((Block) ctx.get(), prov.models().cubeAll(this.createName(variant), all));
    }

    public PaletteBlockPattern.IBlockStateProvider cubeBottomTop(String variant) {
        ResourceLocation side = toLocation(variant, this.textures[0]);
        ResourceLocation bottom = toLocation(variant, this.textures[1]);
        ResourceLocation top = toLocation(variant, this.textures[2]);
        return (ctx, prov) -> prov.simpleBlock((Block) ctx.get(), prov.models().cubeBottomTop(this.createName(variant), side, bottom, top));
    }

    public PaletteBlockPattern.IBlockStateProvider pillar(String variant) {
        ResourceLocation side = toLocation(variant, this.textures[0]);
        ResourceLocation end = toLocation(variant, this.textures[1]);
        return (ctx, prov) -> prov.getVariantBuilder((Block) ctx.getEntry()).forAllStatesExcept(state -> {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
            return axis == Direction.Axis.Y ? ConfiguredModel.builder().modelFile(prov.models().cubeColumn(this.createName(variant), side, end)).uvLock(false).build() : ConfiguredModel.builder().modelFile(prov.models().cubeColumnHorizontal(this.createName(variant) + "_horizontal", side, end)).uvLock(false).rotationX(90).rotationY(axis == Direction.Axis.X ? 90 : 0).build();
        }, BlockStateProperties.WATERLOGGED, ConnectedPillarBlock.NORTH, ConnectedPillarBlock.SOUTH, ConnectedPillarBlock.EAST, ConnectedPillarBlock.WEST);
    }

    public PaletteBlockPattern.IBlockStateProvider cubeColumn(String variant) {
        ResourceLocation side = toLocation(variant, this.textures[0]);
        ResourceLocation end = toLocation(variant, this.textures[1]);
        return (ctx, prov) -> prov.simpleBlock((Block) ctx.get(), prov.models().cubeColumn(this.createName(variant), side, end));
    }

    protected String createName(String variant) {
        if (this.nameType == PaletteBlockPattern.PatternNameType.WRAP) {
            String[] split = this.id.split("_");
            if (split.length == 2) {
                String formatString = "%s_%s_%s";
                return String.format(formatString, split[0], variant, split[1]);
            }
        }
        String formatString = "%s_%s";
        return this.nameType == PaletteBlockPattern.PatternNameType.SUFFIX ? String.format(formatString, variant, this.id) : String.format(formatString, this.id, variant);
    }

    protected static ResourceLocation toLocation(String variant, String texture) {
        return Create.asResource(String.format("block/palettes/stone_types/%s/%s", texture, variant + (texture.equals("cut") ? "_" : "_cut_") + texture));
    }

    protected static CTSpriteShiftEntry ct(String variant, PaletteBlockPattern.CTs texture) {
        ResourceLocation resLoc = (ResourceLocation) texture.srcFactory.apply(variant);
        ResourceLocation resLocTarget = (ResourceLocation) texture.targetFactory.apply(variant);
        return CTSpriteShifter.getCT(texture.type, resLoc, new ResourceLocation(resLocTarget.getNamespace(), resLocTarget.getPath() + "_connected"));
    }

    public static enum CTs {

        PILLAR(AllCTTypes.RECTANGLE, s -> PaletteBlockPattern.toLocation(s, "pillar")), CAP(AllCTTypes.OMNIDIRECTIONAL, s -> PaletteBlockPattern.toLocation(s, "cap")), LAYERED(AllCTTypes.HORIZONTAL_KRYPPERS, s -> PaletteBlockPattern.toLocation(s, "layered"));

        public CTType type;

        private Function<String, ResourceLocation> srcFactory;

        private Function<String, ResourceLocation> targetFactory;

        private CTs(CTType type, Function<String, ResourceLocation> factory) {
            this(type, factory, factory);
        }

        private CTs(CTType type, Function<String, ResourceLocation> srcFactory, Function<String, ResourceLocation> targetFactory) {
            this.type = type;
            this.srcFactory = srcFactory;
            this.targetFactory = targetFactory;
        }
    }

    @FunctionalInterface
    interface IBlockStateProvider extends NonNullBiConsumer<DataGenContext<Block, ? extends Block>, RegistrateBlockstateProvider> {
    }

    @FunctionalInterface
    interface IPatternBlockStateGenerator extends Function<PaletteBlockPattern, Function<String, PaletteBlockPattern.IBlockStateProvider>> {
    }

    static enum PatternNameType {

        PREFIX, SUFFIX, WRAP
    }
}