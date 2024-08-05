package com.simibubi.create.foundation.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.chassis.LinearChassisBlock;
import com.simibubi.create.content.contraptions.chassis.RadialChassisBlock;
import com.simibubi.create.content.contraptions.mounted.CartAssembleRailType;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleBlock;
import com.simibubi.create.content.decoration.steamWhistle.WhistleExtenderBlock;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pointing;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonnullType;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.apache.commons.lang3.tuple.Pair;

public class BlockStateGen {

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> axisBlockProvider(boolean customItem) {
        return (c, p) -> axisBlock(c, p, getBlockModel(customItem, c, p));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalBlockProvider(boolean customItem) {
        return (c, p) -> p.directionalBlock((Block) c.get(), getBlockModel(customItem, c, p));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalBlockProviderIgnoresWaterlogged(boolean customItem) {
        return (c, p) -> directionalBlockIgnoresWaterlogged(c, p, getBlockModel(customItem, c, p));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalBlockProvider(boolean customItem) {
        return (c, p) -> p.horizontalBlock((Block) c.get(), getBlockModel(customItem, c, p));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalAxisBlockProvider(boolean customItem) {
        return (c, p) -> horizontalAxisBlock(c, p, getBlockModel(customItem, c, p));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simpleCubeAll(String path) {
        return (c, p) -> p.simpleBlock((Block) c.get(), p.models().cubeAll(c.getName(), p.modLoc("block/" + path)));
    }

    public static <T extends DirectionalAxisKineticBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> directionalAxisBlockProvider() {
        return (c, p) -> directionalAxisBlock(c, p, ($, vertical) -> p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/" + (vertical ? "vertical" : "horizontal"))));
    }

    public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalWheelProvider(boolean customItem) {
        return (c, p) -> horizontalWheel(c, p, getBlockModel(customItem, c, p));
    }

    private static <T extends Block> Function<BlockState, ModelFile> getBlockModel(boolean customItem, DataGenContext<Block, T> c, RegistrateBlockstateProvider p) {
        return $ -> customItem ? AssetLookup.partialBaseModel(c, p) : AssetLookup.standardModel(c, p);
    }

    public static <T extends Block> void directionalBlockIgnoresWaterlogged(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStatesExcept(state -> {
            Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    public static <T extends Block> void axisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        axisBlock(ctx, prov, modelFunc, false);
    }

    public static <T extends Block> void axisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc, boolean uvLock) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStatesExcept(state -> {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.AXIS);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).uvLock(uvLock).rotationX(axis == Direction.Axis.Y ? 0 : 90).rotationY(axis == Direction.Axis.X ? 90 : (axis == Direction.Axis.Z ? 180 : 0)).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    public static <T extends Block> void simpleBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).build(), BlockStateProperties.WATERLOGGED);
    }

    public static <T extends Block> void horizontalAxisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStates(state -> {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationY(axis == Direction.Axis.X ? 90 : 0).build();
        });
    }

    public static <T extends DirectionalAxisKineticBlock> void directionalAxisBlock(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BiFunction<BlockState, Boolean, ModelFile> modelFunc) {
        prov.getVariantBuilder((Block) ctx.getEntry()).forAllStates(state -> {
            boolean alongFirst = (Boolean) state.m_61143_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
            Direction direction = (Direction) state.m_61143_(DirectionalAxisKineticBlock.FACING);
            boolean vertical = direction.getAxis().isHorizontal() && direction.getAxis() == Direction.Axis.X == alongFirst;
            int xRot = direction == Direction.DOWN ? 270 : (direction == Direction.UP ? 90 : 0);
            int yRot = direction.getAxis().isVertical() ? (alongFirst ? 0 : 90) : (int) direction.toYRot();
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state, vertical)).rotationX(xRot).rotationY(yRot).build();
        });
    }

    public static <T extends Block> void horizontalWheel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, Function<BlockState, ModelFile> modelFunc) {
        prov.getVariantBuilder((Block) ctx.get()).forAllStates(state -> ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(90).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build());
    }

    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String textureSubDir) {
        cubeAll(ctx, prov, textureSubDir, ctx.getName());
    }

    public static <T extends Block> void cubeAll(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, String textureSubDir, String name) {
        String texturePath = "block/" + textureSubDir + name;
        prov.simpleBlock((Block) ctx.get(), prov.models().cubeAll(ctx.getName(), prov.modLoc(texturePath)));
    }

    public static NonNullBiConsumer<DataGenContext<Block, CartAssemblerBlock>, RegistrateBlockstateProvider> cartAssembler() {
        return (c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(state -> {
            CartAssembleRailType type = (CartAssembleRailType) state.m_61143_(CartAssemblerBlock.RAIL_TYPE);
            Boolean powered = (Boolean) state.m_61143_(CartAssemblerBlock.POWERED);
            Boolean backwards = (Boolean) state.m_61143_(CartAssemblerBlock.BACKWARDS);
            RailShape shape = (RailShape) state.m_61143_(CartAssemblerBlock.RAIL_SHAPE);
            int yRotation = shape == RailShape.EAST_WEST ? 270 : 0;
            if (backwards) {
                yRotation += 180;
            }
            return ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/block_" + type.getSerializedName() + (powered ? "_powered" : "")))).rotationY(yRotation % 360).build();
        });
    }

    public static NonNullBiConsumer<DataGenContext<Block, BlazeBurnerBlock>, RegistrateBlockstateProvider> blazeHeater() {
        return (c, p) -> ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.modLoc("block/" + c.getName() + "/block"))).build();
    }

    public static <B extends LinearChassisBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> linearChassis() {
        return (c, p) -> {
            ResourceLocation side = p.modLoc("block/" + c.getName() + "_side");
            ResourceLocation top = p.modLoc("block/linear_chassis_end");
            ResourceLocation top_sticky = p.modLoc("block/linear_chassis_end_sticky");
            Vector<ModelFile> models = new Vector(4);
            for (boolean isTopSticky : Iterate.trueAndFalse) {
                for (boolean isBottomSticky : Iterate.trueAndFalse) {
                    models.add(p.models().withExistingParent(c.getName() + (isTopSticky ? "_top" : "") + (isBottomSticky ? "_bottom" : ""), "block/cube_bottom_top").texture("side", side).texture("bottom", isBottomSticky ? top_sticky : top).texture("top", isTopSticky ? top_sticky : top));
                }
            }
            BiFunction<Boolean, Boolean, ModelFile> modelFunc = (t, b) -> (ModelFile) models.get((t ? 0 : 2) + (b ? 0 : 1));
            axisBlock(c, p, state -> (ModelFile) modelFunc.apply((Boolean) state.m_61143_(LinearChassisBlock.STICKY_TOP), (Boolean) state.m_61143_(LinearChassisBlock.STICKY_BOTTOM)));
        };
    }

    public static <B extends RadialChassisBlock> NonNullBiConsumer<DataGenContext<Block, B>, RegistrateBlockstateProvider> radialChassis() {
        return (c, p) -> {
            String path = "block/" + c.getName();
            ResourceLocation side = p.modLoc(path + "_side");
            ResourceLocation side_sticky = p.modLoc(path + "_side_sticky");
            String templateModelPath = "block/radial_chassis";
            ModelFile base = p.models().getExistingFile(p.modLoc(templateModelPath + "/base"));
            Vector<ModelFile> faces = new Vector(3);
            Vector<ModelFile> stickyFaces = new Vector(3);
            for (Direction.Axis axis : Iterate.axes) {
                String suffix = "side_" + axis.getSerializedName();
                faces.add(p.models().withExistingParent("block/" + c.getName() + "_" + suffix, p.modLoc(templateModelPath + "/" + suffix)).texture("side", side));
            }
            for (Direction.Axis axis : Iterate.axes) {
                String suffix = "side_" + axis.getSerializedName();
                stickyFaces.add(p.models().withExistingParent("block/" + c.getName() + "_" + suffix + "_sticky", p.modLoc(templateModelPath + "/" + suffix)).texture("side", side_sticky));
            }
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder((Block) c.get());
            BlockState propertyGetter = (BlockState) ((RadialChassisBlock) c.get()).m_49966_().m_61124_(RadialChassisBlock.f_55923_, Direction.Axis.Y);
            for (Direction.Axis axis : Iterate.axes) {
                builder.part().modelFile(base).rotationX(axis != Direction.Axis.Y ? 90 : 0).rotationY(axis != Direction.Axis.X ? 0 : 90).addModel().condition(RadialChassisBlock.f_55923_, axis).end();
            }
            for (Direction face : Iterate.horizontalDirections) {
                for (boolean sticky : Iterate.trueAndFalse) {
                    for (Direction.Axis axis : Iterate.axes) {
                        int horizontalAngle = (int) face.toYRot();
                        int index = axis.ordinal();
                        int xRot = 0;
                        int yRot = 0;
                        if (axis == Direction.Axis.X) {
                            xRot = -horizontalAngle + 180;
                        }
                        if (axis == Direction.Axis.Y) {
                            yRot = horizontalAngle;
                        }
                        if (axis == Direction.Axis.Z) {
                            yRot = -horizontalAngle + 270;
                            if (face.getAxis() == Direction.Axis.Z) {
                                index = 0;
                                xRot = horizontalAngle + 180;
                                yRot = 90;
                            }
                        }
                        builder.part().modelFile((ModelFile) (sticky ? stickyFaces : faces).get(index)).rotationX((xRot + 360) % 360).rotationY((yRot + 360) % 360).addModel().condition(RadialChassisBlock.f_55923_, axis).condition(((RadialChassisBlock) c.get()).getGlueableSide(propertyGetter, face), sticky).end();
                    }
                }
            }
        };
    }

    public static <P extends Block> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> naturalStoneTypeBlock(String type) {
        return (c, p) -> {
            ConfiguredModel[] variants = new ConfiguredModel[4];
            for (int i = 0; i < variants.length; i++) {
                variants[i] = ConfiguredModel.builder().modelFile(p.models().cubeAll(type + "_natural_" + i, p.modLoc("block/palettes/stone_types/natural/" + type + "_" + i))).buildLast();
            }
            p.getVariantBuilder((Block) c.get()).partialState().setModels(variants);
        };
    }

    public static <P extends EncasedPipeBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> encasedPipe() {
        return (c, p) -> {
            ModelFile open = AssetLookup.partialBaseModel(c, p, "open");
            ModelFile flat = AssetLookup.partialBaseModel(c, p, "flat");
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder((Block) c.get());
            for (boolean flatPass : Iterate.trueAndFalse) {
                for (Direction d : Iterate.directions) {
                    int verticalAngle = d == Direction.UP ? 90 : (d == Direction.DOWN ? -90 : 0);
                    builder.part().modelFile(flatPass ? flat : open).rotationX(verticalAngle).rotationY((int) (d.toYRot() + (float) (d.getAxis().isVertical() ? 90 : 0)) % 360).addModel().condition((Property) EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(d), !flatPass).end();
                }
            }
        };
    }

    public static <P extends TrapDoorBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> uvLockedTrapdoorBlock(P block, ModelFile bottom, ModelFile top, ModelFile open) {
        return (c, p) -> p.getVariantBuilder(block).forAllStatesExcept(state -> {
            int xRot = 0;
            int yRot = (int) ((Direction) state.m_61143_(TrapDoorBlock.f_54117_)).toYRot() + 180;
            boolean isOpen = (Boolean) state.m_61143_(TrapDoorBlock.OPEN);
            if (!isOpen) {
                yRot = 0;
            }
            yRot %= 360;
            return ConfiguredModel.builder().modelFile(isOpen ? open : (state.m_61143_(TrapDoorBlock.HALF) == Half.TOP ? top : bottom)).rotationX(xRot).rotationY(yRot).uvLock(!isOpen).build();
        }, TrapDoorBlock.POWERED, TrapDoorBlock.WATERLOGGED);
    }

    public static <P extends WhistleExtenderBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> whistleExtender() {
        return (c, p) -> {
            BlockModelProvider models = p.models();
            String basePath = "block/steam_whistle/extension/";
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder((Block) c.get());
            for (WhistleBlock.WhistleSize size : WhistleBlock.WhistleSize.values()) {
                String basePathSize = basePath + size.getSerializedName() + "_";
                ModelFile.ExistingModelFile topRim = models.getExistingFile(Create.asResource(basePathSize + "top_rim"));
                ModelFile.ExistingModelFile single = models.getExistingFile(Create.asResource(basePathSize + "single"));
                ModelFile.ExistingModelFile double_ = models.getExistingFile(Create.asResource(basePathSize + "double"));
                builder.part().modelFile(topRim).addModel().condition(WhistleExtenderBlock.SIZE, size).condition(WhistleExtenderBlock.SHAPE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE).end().part().modelFile(single).addModel().condition(WhistleExtenderBlock.SIZE, size).condition(WhistleExtenderBlock.SHAPE, WhistleExtenderBlock.WhistleExtenderShape.SINGLE).end().part().modelFile(double_).addModel().condition(WhistleExtenderBlock.SIZE, size).condition(WhistleExtenderBlock.SHAPE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE_CONNECTED).end();
            }
        };
    }

    public static <P extends FluidPipeBlock> NonNullBiConsumer<DataGenContext<Block, P>, RegistrateBlockstateProvider> pipe() {
        return (c, p) -> {
            String path = "block/" + c.getName();
            String LU = "lu";
            String RU = "ru";
            String LD = "ld";
            String RD = "rd";
            String LR = "lr";
            String UD = "ud";
            String U = "u";
            String D = "d";
            String L = "l";
            String R = "r";
            List<String> orientations = ImmutableList.of(LU, RU, LD, RD, LR, UD, U, D, L, R);
            Map<String, Pair<Integer, Integer>> uvs = ImmutableMap.builder().put(LU, Pair.of(12, 4)).put(RU, Pair.of(8, 4)).put(LD, Pair.of(12, 0)).put(RD, Pair.of(8, 0)).put(LR, Pair.of(4, 8)).put(UD, Pair.of(0, 8)).put(U, Pair.of(4, 4)).put(D, Pair.of(0, 0)).put(L, Pair.of(4, 0)).put(R, Pair.of(0, 4)).build();
            Map<Direction.Axis, ResourceLocation> coreTemplates = new IdentityHashMap();
            Map<Pair<String, Direction.Axis>, ModelFile> coreModels = new HashMap();
            for (Direction.Axis axis : Iterate.axes) {
                coreTemplates.put(axis, p.modLoc(path + "/core_" + axis.getSerializedName()));
            }
            for (Direction.Axis axis : Iterate.axes) {
                ResourceLocation parent = (ResourceLocation) coreTemplates.get(axis);
                for (String s : orientations) {
                    Pair<String, Direction.Axis> key = Pair.of(s, axis);
                    String modelName = path + "/" + s + "_" + axis.getSerializedName();
                    coreModels.put(key, p.models().withExistingParent(modelName, parent).element().from(4.0F, 4.0F, 4.0F).to(12.0F, 12.0F, 12.0F).face(Direction.get(Direction.AxisDirection.POSITIVE, axis)).end().face(Direction.get(Direction.AxisDirection.NEGATIVE, axis)).end().faces((d, builder) -> {
                        Pair<Integer, Integer> pair = (Pair<Integer, Integer>) uvs.get(s);
                        float u = (float) ((Integer) pair.getKey()).intValue();
                        float v = (float) ((Integer) pair.getValue()).intValue();
                        if (d == Direction.UP) {
                            builder.uvs(u + 4.0F, v + 4.0F, u, v);
                        }
                        if (d == Direction.DOWN) {
                            builder.uvs(u + 4.0F, v, u, v + 4.0F);
                        }
                        if (d == Direction.NORTH) {
                            builder.uvs(u, v, u + 4.0F, v + 4.0F);
                        }
                        if (d == Direction.SOUTH) {
                            builder.uvs(u + 4.0F, v, u, v + 4.0F);
                        }
                        if (d == Direction.EAST) {
                            builder.uvs(u, v, u + 4.0F, v + 4.0F);
                        }
                        if (d == Direction.WEST) {
                            builder.uvs(u + 4.0F, v, u, v + 4.0F);
                        }
                        builder.texture("#0");
                    }).end());
                }
            }
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder((Block) c.get());
            for (Direction.Axis axis : Iterate.axes) {
                putPart(coreModels, builder, axis, LU, true, false, true, false);
                putPart(coreModels, builder, axis, RU, true, false, false, true);
                putPart(coreModels, builder, axis, LD, false, true, true, false);
                putPart(coreModels, builder, axis, RD, false, true, false, true);
                putPart(coreModels, builder, axis, UD, true, true, false, false);
                putPart(coreModels, builder, axis, U, true, false, false, false);
                putPart(coreModels, builder, axis, D, false, true, false, false);
                putPart(coreModels, builder, axis, LR, false, false, true, true);
                putPart(coreModels, builder, axis, L, false, false, true, false);
                putPart(coreModels, builder, axis, R, false, false, false, true);
            }
        };
    }

    private static void putPart(Map<Pair<String, Direction.Axis>, ModelFile> coreModels, MultiPartBlockStateBuilder builder, Direction.Axis axis, String s, boolean up, boolean down, boolean left, boolean right) {
        Direction positiveAxis = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        Map<Direction, BooleanProperty> propertyMap = FluidPipeBlock.f_55154_;
        Direction upD = Pointing.UP.getCombinedDirection(positiveAxis);
        Direction leftD = Pointing.LEFT.getCombinedDirection(positiveAxis);
        Direction rightD = Pointing.RIGHT.getCombinedDirection(positiveAxis);
        Direction downD = Pointing.DOWN.getCombinedDirection(positiveAxis);
        if (axis == Direction.Axis.Y || axis == Direction.Axis.X) {
            leftD = leftD.getOpposite();
            rightD = rightD.getOpposite();
        }
        builder.part().modelFile((ModelFile) coreModels.get(Pair.of(s, axis))).addModel().condition((Property) propertyMap.get(upD), up).condition((Property) propertyMap.get(leftD), left).condition((Property) propertyMap.get(rightD), right).condition((Property) propertyMap.get(downD), down).end();
    }

    public static Function<BlockState, ConfiguredModel[]> mapToAir(@NonnullType RegistrateBlockstateProvider p) {
        return state -> ConfiguredModel.builder().modelFile(p.models().getExistingFile(p.mcLoc("block/air"))).build();
    }
}