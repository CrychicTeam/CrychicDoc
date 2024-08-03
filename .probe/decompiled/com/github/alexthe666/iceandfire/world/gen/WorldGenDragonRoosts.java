package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.BlockGoldPile;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class WorldGenDragonRoosts extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    protected static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    protected final Block treasureBlock;

    public WorldGenDragonRoosts(Codec<NoneFeatureConfiguration> configuration, Block treasureBlock) {
        super(configuration);
        this.treasureBlock = treasureBlock;
    }

    @Override
    public String getId() {
        return "dragon_roost";
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!WorldUtil.canGenerate(IafConfig.generateDragonRoostChance, context.level(), context.random(), context.origin(), this.getId(), true)) {
            return false;
        } else {
            boolean isMale = new Random().nextBoolean();
            int radius = 12 + context.random().nextInt(8);
            this.spawnDragon(context, radius, isMale);
            this.generateSurface(context, radius);
            this.generateShell(context, radius);
            radius -= 2;
            this.hollowOut(context, radius);
            radius += 15;
            this.generateDecoration(context, radius, isMale);
            return true;
        }
    }

    protected void generateRoostPile(WorldGenLevel level, RandomSource random, BlockPos position, Block block) {
        int radius = random.nextInt(4);
        for (int i = 0; i < radius; i++) {
            int layeredRadius = radius - i;
            double circularArea = this.getCircularArea(radius);
            BlockPos up = position.above(i);
            for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(up.offset(-layeredRadius, 0, -layeredRadius), up.offset(layeredRadius, 0, layeredRadius)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                if (blockpos.m_123331_(position) <= circularArea) {
                    level.m_7731_(blockpos, block.defaultBlockState(), 2);
                }
            }
        }
    }

    protected double getCircularArea(int radius, int height) {
        double area = (double) ((float) (radius + height + radius) * 0.333F + 0.5F);
        return (double) Mth.floor(area * area);
    }

    protected double getCircularArea(int radius) {
        double area = (double) ((float) (radius + radius) * 0.333F + 0.5F);
        return (double) Mth.floor(area * area);
    }

    protected BlockPos getSurfacePosition(WorldGenLevel level, BlockPos position) {
        return level.m_5452_(Heightmap.Types.WORLD_SURFACE_WG, position);
    }

    protected BlockState transform(Block block) {
        return this.transform(block.defaultBlockState());
    }

    private void generateDecoration(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, int radius, boolean isMale) {
        int height = radius / 5;
        double circularArea = this.getCircularArea(radius, height);
        BlockPos.betweenClosedStream(context.origin().offset(-radius, -height, -radius), context.origin().offset(radius, height, radius)).map(BlockPos::m_7949_).forEach(position -> {
            if (position.m_123331_(context.origin()) <= circularArea) {
                double distance = position.m_123331_(context.origin()) / circularArea;
                if (!context.level().m_46859_(context.origin()) && context.random().nextDouble() > distance * 0.5) {
                    BlockState state = context.level().m_8055_(position);
                    if (!(state.m_60734_() instanceof BaseEntityBlock) && state.m_60800_(context.level(), position) >= 0.0F) {
                        BlockState transformed = this.transform(state);
                        if (transformed != state) {
                            context.level().m_7731_(position, transformed, 2);
                        }
                    }
                }
                this.handleCustomGeneration(context, position, distance);
                if (distance > 0.5 && context.random().nextInt(1000) == 0) {
                    new WorldGenRoostBoulder(this.transform(Blocks.COBBLESTONE).m_60734_(), context.random().nextInt(3), true).generate(context.level(), context.random(), this.getSurfacePosition(context.level(), position));
                }
                if (distance < 0.3 && context.random().nextInt(isMale ? 200 : 300) == 0) {
                    this.generateTreasurePile(context.level(), context.random(), position);
                }
                if (distance < 0.3 && context.random().nextInt(isMale ? 500 : 700) == 0) {
                    BlockPos surfacePosition = context.level().m_5452_(Heightmap.Types.WORLD_SURFACE, position);
                    boolean wasPlaced = context.level().m_7731_(surfacePosition, (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                    if (wasPlaced && context.level().m_7702_(surfacePosition) instanceof ChestBlockEntity chest) {
                        chest.m_59626_(this.getRoostLootTable(), context.random().nextLong());
                    }
                }
                if (context.random().nextInt(5000) == 0) {
                    new WorldGenRoostArch(this.transform(Blocks.COBBLESTONE).m_60734_()).generate(context.level(), context.random(), this.getSurfacePosition(context.level(), position));
                }
            }
        });
    }

    private void hollowOut(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = 2;
        double circularArea = this.getCircularArea(radius, height);
        BlockPos up = context.origin().above(height - 1);
        BlockPos.betweenClosedStream(up.offset(-radius, 0, -radius), up.offset(radius, height, radius)).map(BlockPos::m_7949_).forEach(position -> {
            if (position.m_123331_(context.origin()) <= circularArea) {
                context.level().m_7731_(position, Blocks.AIR.defaultBlockState(), 2);
            }
        });
    }

    private void generateShell(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = radius / 5;
        double circularArea = this.getCircularArea(radius, height);
        BlockPos.betweenClosedStream(context.origin().offset(-radius, -height, -radius), context.origin().offset(radius, 1, radius)).map(BlockPos::m_7949_).forEach(position -> {
            if (position.m_123331_(context.origin()) < circularArea) {
                context.level().m_7731_(position, context.random().nextBoolean() ? this.transform(Blocks.GRAVEL) : this.transform(Blocks.DIRT), 2);
            } else if (position.m_123331_(context.origin()) == circularArea) {
                context.level().m_7731_(position, this.transform(Blocks.COBBLESTONE), 2);
            }
        });
    }

    private void generateSurface(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, int radius) {
        int height = 2;
        double circularArea = this.getCircularArea(radius, height);
        BlockPos.betweenClosedStream(context.origin().offset(-radius, height, -radius), context.origin().offset(radius, 0, radius)).map(BlockPos::m_7949_).forEach(position -> {
            int heightDifference = position.m_123342_() - context.origin().m_123342_();
            if (position.m_123331_(context.origin()) <= circularArea && heightDifference < 2 + context.random().nextInt(height) && !context.level().m_46859_(position.below())) {
                if (context.level().m_46859_(position.above())) {
                    context.level().m_7731_(position, this.transform(Blocks.GRASS), 2);
                } else {
                    context.level().m_7731_(position, this.transform(Blocks.DIRT), 2);
                }
            }
        });
    }

    private void generateTreasurePile(WorldGenLevel level, RandomSource random, BlockPos origin) {
        int layers = random.nextInt(3);
        for (int i = 0; i < layers; i++) {
            int radius = layers - i;
            double circularArea = this.getCircularArea(radius);
            for (BlockPos position : (Set) BlockPos.betweenClosedStream(origin.offset(-radius, i, -radius), origin.offset(radius, i, radius)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                if (position.m_123331_(origin) <= circularArea) {
                    position = level.m_5452_(Heightmap.Types.WORLD_SURFACE, position);
                    if (this.treasureBlock instanceof BlockGoldPile) {
                        BlockState state = level.m_8055_(position);
                        boolean placed = false;
                        if (state.m_60795_()) {
                            level.m_7731_(position, (BlockState) this.treasureBlock.defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 1 + random.nextInt(7)), 2);
                            placed = true;
                        } else if (state.m_60734_() instanceof SnowLayerBlock) {
                            level.m_7731_(position.below(), (BlockState) this.treasureBlock.defaultBlockState().m_61124_(BlockGoldPile.LAYERS, (Integer) state.m_61143_(SnowLayerBlock.LAYERS)), 2);
                            placed = true;
                        }
                        if (placed && level.m_8055_(position.below()).m_60734_() instanceof BlockGoldPile) {
                            level.m_7731_(position.below(), (BlockState) this.treasureBlock.defaultBlockState().m_61124_(BlockGoldPile.LAYERS, 8), 2);
                        }
                    }
                }
            }
        }
    }

    private void spawnDragon(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, int ageOffset, boolean isMale) {
        EntityDragonBase dragon = this.getDragonType().create(context.level().m_6018_());
        dragon.setGender(isMale);
        dragon.growDragon(40 + ageOffset);
        dragon.setAgingDisabled(true);
        dragon.m_21153_(dragon.m_21233_());
        dragon.setVariant(new Random().nextInt(4));
        dragon.m_19890_((double) context.origin().m_123341_() + 0.5, (double) context.level().m_5452_(Heightmap.Types.WORLD_SURFACE_WG, context.origin()).m_123342_() + 1.5, (double) context.origin().m_123343_() + 0.5, context.random().nextFloat() * 360.0F, 0.0F);
        dragon.homePos = new HomePosition(context.origin(), context.level().m_6018_());
        dragon.hasHomePosition = true;
        dragon.setHunger(50);
        context.level().m_7967_(dragon);
    }

    protected abstract EntityType<? extends EntityDragonBase> getDragonType();

    protected abstract ResourceLocation getRoostLootTable();

    protected abstract BlockState transform(BlockState var1);

    protected abstract void handleCustomGeneration(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> var1, BlockPos var2, double var3);
}