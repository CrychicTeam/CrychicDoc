package net.minecraftforge.client.model.generators;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

public abstract class BlockStateProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @VisibleForTesting
    protected final Map<Block, IGeneratedBlockState> registeredBlocks = new LinkedHashMap();

    private final PackOutput output;

    private final String modid;

    private final BlockModelProvider blockModels;

    private final ItemModelProvider itemModels;

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public static final ImmutableMap<Direction, Property<WallSide>> WALL_PROPS = ImmutableMap.builder().put(Direction.EAST, BlockStateProperties.EAST_WALL).put(Direction.NORTH, BlockStateProperties.NORTH_WALL).put(Direction.SOUTH, BlockStateProperties.SOUTH_WALL).put(Direction.WEST, BlockStateProperties.WEST_WALL).build();

    public BlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        this.output = output;
        this.modid = modid;
        this.blockModels = new BlockModelProvider(output, modid, exFileHelper) {

            @Override
            public CompletableFuture<?> run(CachedOutput cache) {
                return CompletableFuture.allOf();
            }

            @Override
            protected void registerModels() {
            }
        };
        this.itemModels = new ItemModelProvider(output, modid, this.blockModels.existingFileHelper) {

            @Override
            protected void registerModels() {
            }

            @Override
            public CompletableFuture<?> run(CachedOutput cache) {
                return CompletableFuture.allOf();
            }
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.models().clear();
        this.itemModels().clear();
        this.registeredBlocks.clear();
        this.registerStatesAndModels();
        CompletableFuture<?>[] futures = new CompletableFuture[2 + this.registeredBlocks.size()];
        int i = 0;
        futures[i++] = this.models().generateAll(cache);
        futures[i++] = this.itemModels().generateAll(cache);
        for (Entry<Block, IGeneratedBlockState> entry : this.registeredBlocks.entrySet()) {
            futures[i++] = this.saveBlockState(cache, ((IGeneratedBlockState) entry.getValue()).toJson(), (Block) entry.getKey());
        }
        return CompletableFuture.allOf(futures);
    }

    protected abstract void registerStatesAndModels();

    public VariantBlockStateBuilder getVariantBuilder(Block b) {
        if (this.registeredBlocks.containsKey(b)) {
            IGeneratedBlockState old = (IGeneratedBlockState) this.registeredBlocks.get(b);
            Preconditions.checkState(old instanceof VariantBlockStateBuilder);
            return (VariantBlockStateBuilder) old;
        } else {
            VariantBlockStateBuilder ret = new VariantBlockStateBuilder(b);
            this.registeredBlocks.put(b, ret);
            return ret;
        }
    }

    public MultiPartBlockStateBuilder getMultipartBuilder(Block b) {
        if (this.registeredBlocks.containsKey(b)) {
            IGeneratedBlockState old = (IGeneratedBlockState) this.registeredBlocks.get(b);
            Preconditions.checkState(old instanceof MultiPartBlockStateBuilder);
            return (MultiPartBlockStateBuilder) old;
        } else {
            MultiPartBlockStateBuilder ret = new MultiPartBlockStateBuilder(b);
            this.registeredBlocks.put(b, ret);
            return ret;
        }
    }

    public BlockModelProvider models() {
        return this.blockModels;
    }

    public ItemModelProvider itemModels() {
        return this.itemModels;
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(this.modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }

    public ResourceLocation blockTexture(Block block) {
        ResourceLocation name = this.key(block);
        return new ResourceLocation(name.getNamespace(), "block/" + name.getPath());
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    public ModelFile cubeAll(Block block) {
        return this.models().cubeAll(this.name(block), this.blockTexture(block));
    }

    public void simpleBlock(Block block) {
        this.simpleBlock(block, this.cubeAll(block));
    }

    public void simpleBlock(Block block, Function<ModelFile, ConfiguredModel[]> expander) {
        this.simpleBlock(block, (ConfiguredModel[]) expander.apply(this.cubeAll(block)));
    }

    public void simpleBlock(Block block, ModelFile model) {
        this.simpleBlock(block, new ConfiguredModel(model));
    }

    public void simpleBlockItem(Block block, ModelFile model) {
        this.itemModels().getBuilder(this.key(block).getPath()).parent(model);
    }

    public void simpleBlockWithItem(Block block, ModelFile model) {
        this.simpleBlock(block, model);
        this.simpleBlockItem(block, model);
    }

    public void simpleBlock(Block block, ConfiguredModel... models) {
        this.getVariantBuilder(block).partialState().setModels(models);
    }

    public void axisBlock(RotatedPillarBlock block) {
        this.axisBlock(block, this.blockTexture(block));
    }

    public void logBlock(RotatedPillarBlock block) {
        this.axisBlock(block, this.blockTexture(block), this.extend(this.blockTexture(block), "_top"));
    }

    public void axisBlock(RotatedPillarBlock block, ResourceLocation baseName) {
        this.axisBlock(block, this.extend(baseName, "_side"), this.extend(baseName, "_end"));
    }

    public void axisBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
        this.axisBlock(block, this.models().cubeColumn(this.name(block), side, end), this.models().cubeColumnHorizontal(this.name(block) + "_horizontal", side, end));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, String renderType) {
        this.axisBlockWithRenderType(block, this.blockTexture(block), renderType);
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, String renderType) {
        this.axisBlockWithRenderType(block, this.blockTexture(block), this.extend(this.blockTexture(block), "_top"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, String renderType) {
        this.axisBlockWithRenderType(block, this.extend(baseName, "_side"), this.extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, String renderType) {
        this.axisBlock(block, this.models().cubeColumn(this.name(block), side, end).renderType(renderType), this.models().cubeColumnHorizontal(this.name(block) + "_horizontal", side, end).renderType(renderType));
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) {
        this.axisBlockWithRenderType(block, this.blockTexture(block), renderType);
    }

    public void logBlockWithRenderType(RotatedPillarBlock block, ResourceLocation renderType) {
        this.axisBlockWithRenderType(block, this.blockTexture(block), this.extend(this.blockTexture(block), "_top"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation baseName, ResourceLocation renderType) {
        this.axisBlockWithRenderType(block, this.extend(baseName, "_side"), this.extend(baseName, "_end"), renderType);
    }

    public void axisBlockWithRenderType(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end, ResourceLocation renderType) {
        this.axisBlock(block, this.models().cubeColumn(this.name(block), side, end).renderType(renderType), this.models().cubeColumnHorizontal(this.name(block) + "_horizontal", side, end).renderType(renderType));
    }

    public void axisBlock(RotatedPillarBlock block, ModelFile vertical, ModelFile horizontal) {
        this.getVariantBuilder(block).partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).modelForState().modelFile(vertical).addModel().partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).modelForState().modelFile(horizontal).rotationX(90).addModel().partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
    }

    public void horizontalBlock(Block block, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
        this.horizontalBlock(block, this.models().orientable(this.name(block), side, front, top));
    }

    public void horizontalBlock(Block block, ModelFile model) {
        this.horizontalBlock(block, model, 180);
    }

    public void horizontalBlock(Block block, ModelFile model, int angleOffset) {
        this.horizontalBlock(block, $ -> model, angleOffset);
    }

    public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        this.horizontalBlock(block, modelFunc, 180);
    }

    public void horizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + angleOffset) % 360).build());
    }

    public void horizontalFaceBlock(Block block, ModelFile model) {
        this.horizontalFaceBlock(block, model, 180);
    }

    public void horizontalFaceBlock(Block block, ModelFile model, int angleOffset) {
        this.horizontalFaceBlock(block, $ -> model, angleOffset);
    }

    public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        this.horizontalFaceBlock(block, modelFunc, 180);
    }

    public void horizontalFaceBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(((AttachFace) state.m_61143_(BlockStateProperties.ATTACH_FACE)).ordinal() * 90).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + angleOffset + (state.m_61143_(BlockStateProperties.ATTACH_FACE) == AttachFace.CEILING ? 180 : 0)) % 360).build());
    }

    public void directionalBlock(Block block, ModelFile model) {
        this.directionalBlock(block, model, 180);
    }

    public void directionalBlock(Block block, ModelFile model, int angleOffset) {
        this.directionalBlock(block, $ -> model, angleOffset);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        this.directionalBlock(block, modelFunc, 180);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        this.getVariantBuilder(block).forAllStates(state -> {
            Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + angleOffset) % 360).build();
        });
    }

    public void stairsBlock(StairBlock block, ResourceLocation texture) {
        this.stairsBlock(block, texture, texture, texture);
    }

    public void stairsBlock(StairBlock block, String name, ResourceLocation texture) {
        this.stairsBlock(block, name, texture, texture, texture);
    }

    public void stairsBlock(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        this.stairsBlockInternal(block, this.key(block).toString(), side, bottom, top);
    }

    public void stairsBlock(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        this.stairsBlockInternal(block, name + "_stairs", side, bottom, top);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation texture, String renderType) {
        this.stairsBlockWithRenderType(block, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation texture, String renderType) {
        this.stairsBlockWithRenderType(block, name, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, String renderType) {
        this.stairsBlockInternalWithRenderType(block, this.key(block).toString(), side, bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, String renderType) {
        this.stairsBlockInternalWithRenderType(block, name + "_stairs", side, bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation texture, ResourceLocation renderType) {
        this.stairsBlockWithRenderType(block, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.stairsBlockWithRenderType(block, name, texture, texture, texture, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        this.stairsBlockInternalWithRenderType(block, this.key(block).toString(), side, bottom, top, renderType);
    }

    public void stairsBlockWithRenderType(StairBlock block, String name, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        this.stairsBlockInternalWithRenderType(block, name + "_stairs", side, bottom, top, renderType);
    }

    private void stairsBlockInternal(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        ModelFile stairs = this.models().stairs(baseName, side, bottom, top);
        ModelFile stairsInner = this.models().stairsInner(baseName + "_inner", side, bottom, top);
        ModelFile stairsOuter = this.models().stairsOuter(baseName + "_outer", side, bottom, top);
        this.stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    private void stairsBlockInternalWithRenderType(StairBlock block, String baseName, ResourceLocation side, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        ModelFile stairs = this.models().stairs(baseName, side, bottom, top).renderType(renderType);
        ModelFile stairsInner = this.models().stairsInner(baseName + "_inner", side, bottom, top).renderType(renderType);
        ModelFile stairsOuter = this.models().stairsOuter(baseName + "_outer", side, bottom, top).renderType(renderType);
        this.stairsBlock(block, stairs, stairsInner, stairsOuter);
    }

    public void stairsBlock(StairBlock block, ModelFile stairs, ModelFile stairsInner, ModelFile stairsOuter) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = (Direction) state.m_61143_(StairBlock.FACING);
            Half half = (Half) state.m_61143_(StairBlock.HALF);
            StairsShape shape = (StairsShape) state.m_61143_(StairBlock.SHAPE);
            int yRot = (int) facing.getClockWise().toYRot();
            if (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT) {
                yRot += 270;
            }
            if (shape != StairsShape.STRAIGHT && half == Half.TOP) {
                yRot += 90;
            }
            yRot %= 360;
            boolean uvlock = yRot != 0 || half == Half.TOP;
            return ConfiguredModel.builder().modelFile(shape == StairsShape.STRAIGHT ? stairs : (shape != StairsShape.INNER_LEFT && shape != StairsShape.INNER_RIGHT ? stairsOuter : stairsInner)).rotationX(half == Half.BOTTOM ? 0 : 180).rotationY(yRot).uvLock(uvlock).build();
        }, StairBlock.WATERLOGGED);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation texture) {
        this.slabBlock(block, doubleslab, texture, texture, texture);
    }

    public void slabBlock(SlabBlock block, ResourceLocation doubleslab, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        this.slabBlock(block, this.models().slab(this.name(block), side, bottom, top), this.models().slabTop(this.name(block) + "_top", side, bottom, top), this.models().getExistingFile(doubleslab));
    }

    public void slabBlock(SlabBlock block, ModelFile bottom, ModelFile top, ModelFile doubleslab) {
        this.getVariantBuilder(block).partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(bottom)).partialState().with(SlabBlock.TYPE, SlabType.TOP).addModels(new ConfiguredModel(top)).partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleslab));
    }

    public void buttonBlock(ButtonBlock block, ResourceLocation texture) {
        ModelFile button = this.models().button(this.name(block), texture);
        ModelFile buttonPressed = this.models().buttonPressed(this.name(block) + "_pressed", texture);
        this.buttonBlock(block, button, buttonPressed);
    }

    public void buttonBlock(ButtonBlock block, ModelFile button, ModelFile buttonPressed) {
        this.getVariantBuilder(block).forAllStates(state -> {
            Direction facing = (Direction) state.m_61143_(ButtonBlock.f_54117_);
            AttachFace face = (AttachFace) state.m_61143_(ButtonBlock.f_53179_);
            boolean powered = (Boolean) state.m_61143_(ButtonBlock.POWERED);
            return ConfiguredModel.builder().modelFile(powered ? buttonPressed : button).rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180)).rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot()).uvLock(face == AttachFace.WALL).build();
        });
    }

    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) {
        ModelFile pressurePlate = this.models().pressurePlate(this.name(block), texture);
        ModelFile pressurePlateDown = this.models().pressurePlateDown(this.name(block) + "_down", texture);
        this.pressurePlateBlock(block, pressurePlate, pressurePlateDown);
    }

    public void pressurePlateBlock(PressurePlateBlock block, ModelFile pressurePlate, ModelFile pressurePlateDown) {
        this.getVariantBuilder(block).partialState().with(PressurePlateBlock.POWERED, true).addModels(new ConfiguredModel(pressurePlateDown)).partialState().with(PressurePlateBlock.POWERED, false).addModels(new ConfiguredModel(pressurePlate));
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, ResourceLocation texture) {
        ModelFile sign = this.models().sign(this.name(signBlock), texture);
        this.signBlock(signBlock, wallSignBlock, sign);
    }

    public void signBlock(StandingSignBlock signBlock, WallSignBlock wallSignBlock, ModelFile sign) {
        this.simpleBlock(signBlock, sign);
        this.simpleBlock(wallSignBlock, sign);
    }

    public void fourWayBlock(CrossCollisionBlock block, ModelFile post, ModelFile side) {
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block).part().modelFile(post).addModel().end();
        this.fourWayMultipart(builder, side);
    }

    public void fourWayMultipart(MultiPartBlockStateBuilder builder, ModelFile side) {
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
            Direction dir = (Direction) e.getKey();
            if (dir.getAxis().isHorizontal()) {
                builder.part().modelFile(side).rotationY(((int) dir.toYRot() + 180) % 360).uvLock(true).addModel().condition((Property) e.getValue(), true);
            }
        });
    }

    public void fenceBlock(FenceBlock block, ResourceLocation texture) {
        String baseName = this.key(block).toString();
        this.fourWayBlock(block, this.models().fencePost(baseName + "_post", texture), this.models().fenceSide(baseName + "_side", texture));
    }

    public void fenceBlock(FenceBlock block, String name, ResourceLocation texture) {
        this.fourWayBlock(block, this.models().fencePost(name + "_fence_post", texture), this.models().fenceSide(name + "_fence_side", texture));
    }

    public void fenceBlockWithRenderType(FenceBlock block, ResourceLocation texture, String renderType) {
        String baseName = this.key(block).toString();
        this.fourWayBlock(block, this.models().fencePost(baseName + "_post", texture).renderType(renderType), this.models().fenceSide(baseName + "_side", texture).renderType(renderType));
    }

    public void fenceBlockWithRenderType(FenceBlock block, String name, ResourceLocation texture, String renderType) {
        this.fourWayBlock(block, this.models().fencePost(name + "_fence_post", texture).renderType(renderType), this.models().fenceSide(name + "_fence_side", texture).renderType(renderType));
    }

    public void fenceBlockWithRenderType(FenceBlock block, ResourceLocation texture, ResourceLocation renderType) {
        String baseName = this.key(block).toString();
        this.fourWayBlock(block, this.models().fencePost(baseName + "_post", texture).renderType(renderType), this.models().fenceSide(baseName + "_side", texture).renderType(renderType));
    }

    public void fenceBlockWithRenderType(FenceBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.fourWayBlock(block, this.models().fencePost(name + "_fence_post", texture).renderType(renderType), this.models().fenceSide(name + "_fence_side", texture).renderType(renderType));
    }

    public void fenceGateBlock(FenceGateBlock block, ResourceLocation texture) {
        this.fenceGateBlockInternal(block, this.key(block).toString(), texture);
    }

    public void fenceGateBlock(FenceGateBlock block, String name, ResourceLocation texture) {
        this.fenceGateBlockInternal(block, name + "_fence_gate", texture);
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, ResourceLocation texture, String renderType) {
        this.fenceGateBlockInternalWithRenderType(block, this.key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, String name, ResourceLocation texture, String renderType) {
        this.fenceGateBlockInternalWithRenderType(block, name + "_fence_gate", texture, ResourceLocation.tryParse(renderType));
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, ResourceLocation texture, ResourceLocation renderType) {
        this.fenceGateBlockInternalWithRenderType(block, this.key(block).toString(), texture, renderType);
    }

    public void fenceGateBlockWithRenderType(FenceGateBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.fenceGateBlockInternalWithRenderType(block, name + "_fence_gate", texture, renderType);
    }

    private void fenceGateBlockInternal(FenceGateBlock block, String baseName, ResourceLocation texture) {
        ModelFile gate = this.models().fenceGate(baseName, texture);
        ModelFile gateOpen = this.models().fenceGateOpen(baseName + "_open", texture);
        ModelFile gateWall = this.models().fenceGateWall(baseName + "_wall", texture);
        ModelFile gateWallOpen = this.models().fenceGateWallOpen(baseName + "_wall_open", texture);
        this.fenceGateBlock(block, gate, gateOpen, gateWall, gateWallOpen);
    }

    private void fenceGateBlockInternalWithRenderType(FenceGateBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) {
        ModelFile gate = this.models().fenceGate(baseName, texture).renderType(renderType);
        ModelFile gateOpen = this.models().fenceGateOpen(baseName + "_open", texture).renderType(renderType);
        ModelFile gateWall = this.models().fenceGateWall(baseName + "_wall", texture).renderType(renderType);
        ModelFile gateWallOpen = this.models().fenceGateWallOpen(baseName + "_wall_open", texture).renderType(renderType);
        this.fenceGateBlock(block, gate, gateOpen, gateWall, gateWallOpen);
    }

    public void fenceGateBlock(FenceGateBlock block, ModelFile gate, ModelFile gateOpen, ModelFile gateWall, ModelFile gateWallOpen) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            ModelFile model = gate;
            if ((Boolean) state.m_61143_(FenceGateBlock.IN_WALL)) {
                model = gateWall;
            }
            if ((Boolean) state.m_61143_(FenceGateBlock.OPEN)) {
                model = model == gateWall ? gateWallOpen : gateOpen;
            }
            return ConfiguredModel.builder().modelFile(model).rotationY((int) ((Direction) state.m_61143_(FenceGateBlock.f_54117_)).toYRot()).uvLock(true).build();
        }, FenceGateBlock.POWERED);
    }

    public void wallBlock(WallBlock block, ResourceLocation texture) {
        this.wallBlockInternal(block, this.key(block).toString(), texture);
    }

    public void wallBlock(WallBlock block, String name, ResourceLocation texture) {
        this.wallBlockInternal(block, name + "_wall", texture);
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, String renderType) {
        this.wallBlockInternalWithRenderType(block, this.key(block).toString(), texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, String renderType) {
        this.wallBlockInternalWithRenderType(block, name + "_wall", texture, ResourceLocation.tryParse(renderType));
    }

    public void wallBlockWithRenderType(WallBlock block, ResourceLocation texture, ResourceLocation renderType) {
        this.wallBlockInternalWithRenderType(block, this.key(block).toString(), texture, renderType);
    }

    public void wallBlockWithRenderType(WallBlock block, String name, ResourceLocation texture, ResourceLocation renderType) {
        this.wallBlockInternalWithRenderType(block, name + "_wall", texture, renderType);
    }

    private void wallBlockInternal(WallBlock block, String baseName, ResourceLocation texture) {
        this.wallBlock(block, this.models().wallPost(baseName + "_post", texture), this.models().wallSide(baseName + "_side", texture), this.models().wallSideTall(baseName + "_side_tall", texture));
    }

    private void wallBlockInternalWithRenderType(WallBlock block, String baseName, ResourceLocation texture, ResourceLocation renderType) {
        this.wallBlock(block, this.models().wallPost(baseName + "_post", texture).renderType(renderType), this.models().wallSide(baseName + "_side", texture).renderType(renderType), this.models().wallSideTall(baseName + "_side_tall", texture).renderType(renderType));
    }

    public void wallBlock(WallBlock block, ModelFile post, ModelFile side, ModelFile sideTall) {
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block).part().modelFile(post).addModel().condition(WallBlock.UP, true).end();
        WALL_PROPS.entrySet().stream().filter(e -> ((Direction) e.getKey()).getAxis().isHorizontal()).forEach(e -> {
            this.wallSidePart(builder, side, e, WallSide.LOW);
            this.wallSidePart(builder, sideTall, e, WallSide.TALL);
        });
    }

    private void wallSidePart(MultiPartBlockStateBuilder builder, ModelFile model, Entry<Direction, Property<WallSide>> entry, WallSide height) {
        builder.part().modelFile(model).rotationY(((int) ((Direction) entry.getKey()).toYRot() + 180) % 360).uvLock(true).addModel().condition((Property) entry.getValue(), height);
    }

    public void paneBlock(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge) {
        this.paneBlockInternal(block, this.key(block).toString(), pane, edge);
    }

    public void paneBlock(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge) {
        this.paneBlockInternal(block, name + "_pane", pane, edge);
    }

    public void paneBlockWithRenderType(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, String renderType) {
        this.paneBlockInternalWithRenderType(block, this.key(block).toString(), pane, edge, ResourceLocation.tryParse(renderType));
    }

    public void paneBlockWithRenderType(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge, String renderType) {
        this.paneBlockInternalWithRenderType(block, name + "_pane", pane, edge, ResourceLocation.tryParse(renderType));
    }

    public void paneBlockWithRenderType(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) {
        this.paneBlockInternalWithRenderType(block, this.key(block).toString(), pane, edge, renderType);
    }

    public void paneBlockWithRenderType(IronBarsBlock block, String name, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) {
        this.paneBlockInternalWithRenderType(block, name + "_pane", pane, edge, renderType);
    }

    private void paneBlockInternal(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge) {
        ModelFile post = this.models().panePost(baseName + "_post", pane, edge);
        ModelFile side = this.models().paneSide(baseName + "_side", pane, edge);
        ModelFile sideAlt = this.models().paneSideAlt(baseName + "_side_alt", pane, edge);
        ModelFile noSide = this.models().paneNoSide(baseName + "_noside", pane);
        ModelFile noSideAlt = this.models().paneNoSideAlt(baseName + "_noside_alt", pane);
        this.paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
    }

    private void paneBlockInternalWithRenderType(IronBarsBlock block, String baseName, ResourceLocation pane, ResourceLocation edge, ResourceLocation renderType) {
        ModelFile post = this.models().panePost(baseName + "_post", pane, edge).renderType(renderType);
        ModelFile side = this.models().paneSide(baseName + "_side", pane, edge).renderType(renderType);
        ModelFile sideAlt = this.models().paneSideAlt(baseName + "_side_alt", pane, edge).renderType(renderType);
        ModelFile noSide = this.models().paneNoSide(baseName + "_noside", pane).renderType(renderType);
        ModelFile noSideAlt = this.models().paneNoSideAlt(baseName + "_noside_alt", pane).renderType(renderType);
        this.paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
    }

    public void paneBlock(IronBarsBlock block, ModelFile post, ModelFile side, ModelFile sideAlt, ModelFile noSide, ModelFile noSideAlt) {
        MultiPartBlockStateBuilder builder = this.getMultipartBuilder(block).part().modelFile(post).addModel().end();
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach(e -> {
            Direction dir = (Direction) e.getKey();
            if (dir.getAxis().isHorizontal()) {
                boolean alt = dir == Direction.SOUTH;
                builder.part().modelFile(!alt && dir != Direction.WEST ? side : sideAlt).rotationY(dir.getAxis() == Direction.Axis.X ? 90 : 0).addModel().condition((Property) e.getValue(), true).end().part().modelFile(!alt && dir != Direction.EAST ? noSide : noSideAlt).rotationY(dir == Direction.WEST ? 270 : (dir == Direction.SOUTH ? 90 : 0)).addModel().condition((Property) e.getValue(), false);
            }
        });
    }

    public void doorBlock(DoorBlock block, ResourceLocation bottom, ResourceLocation top) {
        this.doorBlockInternal(block, this.key(block).toString(), bottom, top);
    }

    public void doorBlock(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top) {
        this.doorBlockInternal(block, name + "_door", bottom, top);
    }

    public void doorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top, String renderType) {
        this.doorBlockInternalWithRenderType(block, this.key(block).toString(), bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void doorBlockWithRenderType(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top, String renderType) {
        this.doorBlockInternalWithRenderType(block, name + "_door", bottom, top, ResourceLocation.tryParse(renderType));
    }

    public void doorBlockWithRenderType(DoorBlock block, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        this.doorBlockInternalWithRenderType(block, this.key(block).toString(), bottom, top, renderType);
    }

    public void doorBlockWithRenderType(DoorBlock block, String name, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        this.doorBlockInternalWithRenderType(block, name + "_door", bottom, top, renderType);
    }

    private void doorBlockInternal(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top) {
        ModelFile bottomLeft = this.models().doorBottomLeft(baseName + "_bottom_left", bottom, top);
        ModelFile bottomLeftOpen = this.models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top);
        ModelFile bottomRight = this.models().doorBottomRight(baseName + "_bottom_right", bottom, top);
        ModelFile bottomRightOpen = this.models().doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top);
        ModelFile topLeft = this.models().doorTopLeft(baseName + "_top_left", bottom, top);
        ModelFile topLeftOpen = this.models().doorTopLeftOpen(baseName + "_top_left_open", bottom, top);
        ModelFile topRight = this.models().doorTopRight(baseName + "_top_right", bottom, top);
        ModelFile topRightOpen = this.models().doorTopRightOpen(baseName + "_top_right_open", bottom, top);
        this.doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    private void doorBlockInternalWithRenderType(DoorBlock block, String baseName, ResourceLocation bottom, ResourceLocation top, ResourceLocation renderType) {
        ModelFile bottomLeft = this.models().doorBottomLeft(baseName + "_bottom_left", bottom, top).renderType(renderType);
        ModelFile bottomLeftOpen = this.models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top).renderType(renderType);
        ModelFile bottomRight = this.models().doorBottomRight(baseName + "_bottom_right", bottom, top).renderType(renderType);
        ModelFile bottomRightOpen = this.models().doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top).renderType(renderType);
        ModelFile topLeft = this.models().doorTopLeft(baseName + "_top_left", bottom, top).renderType(renderType);
        ModelFile topLeftOpen = this.models().doorTopLeftOpen(baseName + "_top_left_open", bottom, top).renderType(renderType);
        ModelFile topRight = this.models().doorTopRight(baseName + "_top_right", bottom, top).renderType(renderType);
        ModelFile topRightOpen = this.models().doorTopRightOpen(baseName + "_top_right_open", bottom, top).renderType(renderType);
        this.doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    public void doorBlock(DoorBlock block, ModelFile bottomLeft, ModelFile bottomLeftOpen, ModelFile bottomRight, ModelFile bottomRightOpen, ModelFile topLeft, ModelFile topLeftOpen, ModelFile topRight, ModelFile topRightOpen) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int yRot = (int) ((Direction) state.m_61143_(DoorBlock.FACING)).toYRot() + 90;
            boolean right = state.m_61143_(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            boolean open = (Boolean) state.m_61143_(DoorBlock.OPEN);
            boolean lower = state.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER;
            if (open) {
                yRot += 90;
            }
            if (right && open) {
                yRot += 180;
            }
            yRot %= 360;
            ModelFile model = null;
            if (lower && right && open) {
                model = bottomRightOpen;
            } else if (lower && !right && open) {
                model = bottomLeftOpen;
            }
            if (lower && right && !open) {
                model = bottomRight;
            } else if (lower && !right && !open) {
                model = bottomLeft;
            }
            if (!lower && right && open) {
                model = topRightOpen;
            } else if (!lower && !right && open) {
                model = topLeftOpen;
            }
            if (!lower && right && !open) {
                model = topRight;
            } else if (!lower && !right && !open) {
                model = topLeft;
            }
            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).build();
        }, DoorBlock.POWERED);
    }

    public void trapdoorBlock(TrapDoorBlock block, ResourceLocation texture, boolean orientable) {
        this.trapdoorBlockInternal(block, this.key(block).toString(), texture, orientable);
    }

    public void trapdoorBlock(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable) {
        this.trapdoorBlockInternal(block, name + "_trapdoor", texture, orientable);
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, ResourceLocation texture, boolean orientable, String renderType) {
        this.trapdoorBlockInternalWithRenderType(block, this.key(block).toString(), texture, orientable, ResourceLocation.tryParse(renderType));
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable, String renderType) {
        this.trapdoorBlockInternalWithRenderType(block, name + "_trapdoor", texture, orientable, ResourceLocation.tryParse(renderType));
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, ResourceLocation texture, boolean orientable, ResourceLocation renderType) {
        this.trapdoorBlockInternalWithRenderType(block, this.key(block).toString(), texture, orientable, renderType);
    }

    public void trapdoorBlockWithRenderType(TrapDoorBlock block, String name, ResourceLocation texture, boolean orientable, ResourceLocation renderType) {
        this.trapdoorBlockInternalWithRenderType(block, name + "_trapdoor", texture, orientable, renderType);
    }

    private void trapdoorBlockInternal(TrapDoorBlock block, String baseName, ResourceLocation texture, boolean orientable) {
        ModelFile bottom = orientable ? this.models().trapdoorOrientableBottom(baseName + "_bottom", texture) : this.models().trapdoorBottom(baseName + "_bottom", texture);
        ModelFile top = orientable ? this.models().trapdoorOrientableTop(baseName + "_top", texture) : this.models().trapdoorTop(baseName + "_top", texture);
        ModelFile open = orientable ? this.models().trapdoorOrientableOpen(baseName + "_open", texture) : this.models().trapdoorOpen(baseName + "_open", texture);
        this.trapdoorBlock(block, bottom, top, open, orientable);
    }

    private void trapdoorBlockInternalWithRenderType(TrapDoorBlock block, String baseName, ResourceLocation texture, boolean orientable, ResourceLocation renderType) {
        ModelFile bottom = orientable ? this.models().trapdoorOrientableBottom(baseName + "_bottom", texture).renderType(renderType) : this.models().trapdoorBottom(baseName + "_bottom", texture).renderType(renderType);
        ModelFile top = orientable ? this.models().trapdoorOrientableTop(baseName + "_top", texture).renderType(renderType) : this.models().trapdoorTop(baseName + "_top", texture).renderType(renderType);
        ModelFile open = orientable ? this.models().trapdoorOrientableOpen(baseName + "_open", texture).renderType(renderType) : this.models().trapdoorOpen(baseName + "_open", texture).renderType(renderType);
        this.trapdoorBlock(block, bottom, top, open, orientable);
    }

    public void trapdoorBlock(TrapDoorBlock block, ModelFile bottom, ModelFile top, ModelFile open, boolean orientable) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int xRot = 0;
            int yRot = (int) ((Direction) state.m_61143_(TrapDoorBlock.f_54117_)).toYRot() + 180;
            boolean isOpen = (Boolean) state.m_61143_(TrapDoorBlock.OPEN);
            if (orientable && isOpen && state.m_61143_(TrapDoorBlock.HALF) == Half.TOP) {
                xRot += 180;
                yRot += 180;
            }
            if (!orientable && !isOpen) {
                yRot = 0;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(isOpen ? open : (state.m_61143_(TrapDoorBlock.HALF) == Half.TOP ? top : bottom)).rotationX(xRot).rotationY(yRot).build();
        }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
    }

    private CompletableFuture<?> saveBlockState(CachedOutput cache, JsonObject stateJson, Block owner) {
        ResourceLocation blockName = (ResourceLocation) Preconditions.checkNotNull(this.key(owner));
        Path outputPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(blockName.getNamespace()).resolve("blockstates").resolve(blockName.getPath() + ".json");
        return DataProvider.saveStable(cache, stateJson, outputPath);
    }

    @NotNull
    @Override
    public String getName() {
        return "Block States: " + this.modid;
    }

    public static class ConfiguredModelList {

        private final List<ConfiguredModel> models;

        private ConfiguredModelList(List<ConfiguredModel> models) {
            Preconditions.checkArgument(!models.isEmpty());
            this.models = models;
        }

        public ConfiguredModelList(ConfiguredModel model) {
            this(ImmutableList.of(model));
        }

        public ConfiguredModelList(ConfiguredModel... models) {
            this(Arrays.asList(models));
        }

        public JsonElement toJSON() {
            if (this.models.size() == 1) {
                return ((ConfiguredModel) this.models.get(0)).toJSON(false);
            } else {
                JsonArray ret = new JsonArray();
                for (ConfiguredModel m : this.models) {
                    ret.add(m.toJSON(true));
                }
                return ret;
            }
        }

        public BlockStateProvider.ConfiguredModelList append(ConfiguredModel... models) {
            return new BlockStateProvider.ConfiguredModelList(ImmutableList.builder().addAll(this.models).add(models).build());
        }
    }
}