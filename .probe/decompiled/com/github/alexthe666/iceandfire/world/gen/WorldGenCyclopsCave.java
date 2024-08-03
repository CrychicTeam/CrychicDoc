package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenCyclopsCave extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    public static final ResourceLocation CYCLOPS_CHEST = new ResourceLocation("iceandfire", "chest/cyclops_cave");

    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public WorldGenCyclopsCave(Codec<NoneFeatureConfiguration> configuration) {
        super(configuration);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.spawnCyclopsCaveChance, context.level(), context.random(), context.origin(), this.getId(), true)) {
            return false;
        } else {
            int size = 16;
            int distance = 6;
            if (!context.level().m_46859_(context.origin().offset(size - distance, -3, -size + distance)) && !context.level().m_46859_(context.origin().offset(size - distance, -3, size - distance)) && !context.level().m_46859_(context.origin().offset(-size + distance, -3, -size + distance)) && !context.level().m_46859_(context.origin().offset(-size + distance, -3, size - distance))) {
                generateShell(context, size);
                int innerSize = size - 2;
                int x = innerSize + context.random().nextInt(2);
                int y = 10 + context.random().nextInt(2);
                int z = innerSize + context.random().nextInt(2);
                float radius = (float) (x + y + z) * 0.333F + 0.5F;
                int sheepPenCount = 0;
                for (BlockPos position : (Set) BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                    if (position.m_123331_(context.origin()) <= (double) (radius * radius) && position.m_123342_() > context.origin().m_123342_() && !(context.level().m_8055_(context.origin()).m_60734_() instanceof AbstractChestBlock)) {
                        context.level().m_7731_(position, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                for (BlockPos positionx : (Set) BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                    if (positionx.m_123331_(context.origin()) <= (double) (radius * radius) && positionx.m_123342_() == context.origin().m_123342_()) {
                        if (context.random().nextInt(130) == 0 && this.isTouchingAir(context.level(), positionx.above())) {
                            this.generateSkeleton(context.level(), positionx.above(), context.random(), context.origin(), radius);
                        }
                        if (context.random().nextInt(130) == 0 && positionx.m_123331_(context.origin()) <= (double) (radius * radius) * 0.8F && sheepPenCount < 2) {
                            this.generateSheepPen(context.level(), positionx.above(), context.random(), context.origin(), radius);
                            sheepPenCount++;
                        }
                        if (context.random().nextInt(80) == 0 && this.isTouchingAir(context.level(), positionx.above())) {
                            context.level().m_7731_(positionx.above(), (BlockState) IafBlockRegistry.GOLD_PILE.get().defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 8), 3);
                            context.level().m_7731_(positionx.above().north(), (BlockState) IafBlockRegistry.GOLD_PILE.get().defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                            context.level().m_7731_(positionx.above().south(), (BlockState) IafBlockRegistry.GOLD_PILE.get().defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                            context.level().m_7731_(positionx.above().west(), (BlockState) IafBlockRegistry.GOLD_PILE.get().defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                            context.level().m_7731_(positionx.above().east(), (BlockState) IafBlockRegistry.GOLD_PILE.get().defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 1 + new Random().nextInt(7)), 3);
                            context.level().m_7731_(positionx.above(2), (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                            if (context.level().m_8055_(positionx.above(2)).m_60734_() instanceof AbstractChestBlock && context.level().m_7702_(positionx.above(2)) instanceof ChestBlockEntity chestBlockEntity) {
                                chestBlockEntity.m_59626_(CYCLOPS_CHEST, context.random().nextLong());
                            }
                        }
                        if (context.random().nextInt(50) == 0 && this.isTouchingAir(context.level(), positionx.above())) {
                            int torchHeight = context.random().nextInt(2) + 1;
                            for (int fence = 0; fence < torchHeight; fence++) {
                                context.level().m_7731_(positionx.above(1 + fence), this.getFenceState(context.level(), positionx.above(1 + fence)), 3);
                            }
                            context.level().m_7731_(positionx.above(1 + torchHeight), Blocks.TORCH.defaultBlockState(), 2);
                        }
                    }
                }
                EntityCyclops cyclops = IafEntityRegistry.CYCLOPS.get().create(context.level().m_6018_());
                cyclops.m_19890_((double) context.origin().m_123341_() + 0.5, (double) context.origin().m_123342_() + 1.5, (double) context.origin().m_123343_() + 0.5, context.random().nextFloat() * 360.0F, 0.0F);
                context.level().m_7967_(cyclops);
                return true;
            } else {
                return false;
            }
        }
    }

    private static void generateShell(FeaturePlaceContext<NoneFeatureConfiguration> context, int size) {
        int x = size + context.random().nextInt(2);
        int y = 12 + context.random().nextInt(2);
        int z = size + context.random().nextInt(2);
        float radius = (float) (x + y + z) * 0.333F + 0.5F;
        for (BlockPos position : (Set) BlockPos.betweenClosedStream(context.origin().offset(-x, -y, -z), context.origin().offset(x, y, z)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
            boolean doorwayX = position.m_123341_() >= context.origin().m_123341_() - 2 + context.random().nextInt(2) && position.m_123341_() <= context.origin().m_123341_() + 2 + context.random().nextInt(2);
            boolean doorwayZ = position.m_123343_() >= context.origin().m_123343_() - 2 + context.random().nextInt(2) && position.m_123343_() <= context.origin().m_123343_() + 2 + context.random().nextInt(2);
            boolean isNotInDoorway = !doorwayX && !doorwayZ && position.m_123342_() > context.origin().m_123342_() || position.m_123342_() > context.origin().m_123342_() + y - (3 + context.random().nextInt(2));
            if (position.m_123331_(context.origin()) <= (double) (radius * radius)) {
                BlockState state = context.level().m_8055_(position);
                if (!(state.m_60734_() instanceof AbstractChestBlock) && state.m_60800_(context.level(), position) >= 0.0F && isNotInDoorway) {
                    context.level().m_7731_(position, Blocks.STONE.defaultBlockState(), 3);
                }
                if (position.m_123342_() == context.origin().m_123342_()) {
                    context.level().m_7731_(position, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
                }
                if (position.m_123342_() <= context.origin().m_123342_() - 1 && !state.m_60815_()) {
                    context.level().m_7731_(position, Blocks.COBBLESTONE.defaultBlockState(), 3);
                }
            }
        }
    }

    private void generateSheepPen(ServerLevelAccessor level, BlockPos position, RandomSource random, BlockPos origin, float radius) {
        int width = 5 + random.nextInt(3);
        int sheepAmount = 2 + random.nextInt(3);
        Direction direction = Direction.NORTH;
        BlockPos end = position;
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int side = 0; side < width; side++) {
                BlockPos relativePosition = end.relative(direction, side);
                if (origin.m_123331_(relativePosition) <= (double) (radius * radius)) {
                    level.m_7731_(relativePosition, this.getFenceState(level, relativePosition), 3);
                    if (level.m_46859_(relativePosition.relative(direction.getClockWise())) && sheepAmount > 0) {
                        BlockPos sheepPos = relativePosition.relative(direction.getClockWise());
                        Sheep sheep = new Sheep(EntityType.SHEEP, level.getLevel());
                        sheep.m_6034_((double) ((float) sheepPos.m_123341_() + 0.5F), (double) ((float) sheepPos.m_123342_() + 0.5F), (double) ((float) sheepPos.m_123343_() + 0.5F));
                        sheep.setColor(random.nextInt(4) == 0 ? DyeColor.YELLOW : DyeColor.WHITE);
                        level.m_7967_(sheep);
                        sheepAmount--;
                    }
                }
            }
            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }
        for (int sideCount = 0; sideCount < 4; sideCount++) {
            for (int sidex = 0; sidex < width; sidex++) {
                BlockPos relativePosition = end.relative(direction, sidex);
                if (origin.m_123331_(relativePosition) <= (double) (radius * radius)) {
                    level.m_7731_(relativePosition, this.getFenceState(level, relativePosition), 3);
                }
            }
            end = end.relative(direction, width);
            direction = direction.getClockWise();
        }
    }

    private void generateSkeleton(LevelAccessor level, BlockPos position, RandomSource random, BlockPos origin, float radius) {
        Direction direction = HORIZONTALS[new Random().nextInt(3)];
        Direction.Axis oppositeAxis = direction.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        int maxRibHeight = random.nextInt(2);
        for (int spine = 0; spine < 5 + random.nextInt(2) * 2; spine++) {
            BlockPos segment = position.relative(direction, spine);
            if (origin.m_123331_(segment) <= (double) (radius * radius)) {
                level.m_7731_(segment, (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, direction.getAxis()), 2);
            }
            if (spine % 2 != 0) {
                BlockPos rightRib = segment.relative(direction.getCounterClockWise());
                BlockPos leftRib = segment.relative(direction.getClockWise());
                if (origin.m_123331_(rightRib) <= (double) (radius * radius)) {
                    level.m_7731_(rightRib, (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                if (origin.m_123331_(leftRib) <= (double) (radius * radius)) {
                    level.m_7731_(leftRib, (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                for (int ribHeight = 1; ribHeight < maxRibHeight + 2; ribHeight++) {
                    if (origin.m_123331_(rightRib.above(ribHeight).relative(direction.getCounterClockWise())) <= (double) (radius * radius)) {
                        level.m_7731_(rightRib.above(ribHeight).relative(direction.getCounterClockWise()), (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                    }
                    if (origin.m_123331_(leftRib.above(ribHeight).relative(direction.getClockWise())) <= (double) (radius * radius)) {
                        level.m_7731_(leftRib.above(ribHeight).relative(direction.getClockWise()), (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, Direction.Axis.Y), 2);
                    }
                }
                if (origin.m_123331_(rightRib.above(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    level.m_7731_(rightRib.above(maxRibHeight + 2), (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
                if (origin.m_123331_(leftRib.above(maxRibHeight + 2)) <= (double) (radius * radius)) {
                    level.m_7731_(leftRib.above(maxRibHeight + 2), (BlockState) Blocks.BONE_BLOCK.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, oppositeAxis), 2);
                }
            }
        }
    }

    private boolean isTouchingAir(LevelAccessor level, BlockPos position) {
        boolean isTouchingAir = true;
        for (Direction direction : HORIZONTALS) {
            if (!level.m_46859_(position.relative(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }

    private BlockState getFenceState(LevelAccessor level, BlockPos position) {
        boolean east = level.m_8055_(position.east()).m_60734_() == Blocks.OAK_FENCE;
        boolean west = level.m_8055_(position.west()).m_60734_() == Blocks.OAK_FENCE;
        boolean north = level.m_8055_(position.north()).m_60734_() == Blocks.OAK_FENCE;
        boolean south = level.m_8055_(position.south()).m_60734_() == Blocks.OAK_FENCE;
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) Blocks.OAK_FENCE.defaultBlockState().m_61124_(FenceBlock.f_52310_, east)).m_61124_(FenceBlock.f_52312_, west)).m_61124_(FenceBlock.f_52309_, north)).m_61124_(FenceBlock.f_52311_, south);
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "cyclops_cave";
    }
}