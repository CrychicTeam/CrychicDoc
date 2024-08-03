package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenHydraCave extends Feature<NoneFeatureConfiguration> implements TypedFeature {

    public static final ResourceLocation HYDRA_CHEST = new ResourceLocation("iceandfire", "chest/hydra_cave");

    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public WorldGenHydraCave(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        ChunkGenerator generator = context.chunkGenerator();
        if (rand.nextInt(IafConfig.generateHydraChance) == 0 && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position, this.getId())) {
            int i1 = 8;
            int i2 = i1 - 2;
            int dist = 6;
            if (!worldIn.m_46859_(position.offset(i1 - dist, -3, -i1 + dist)) && !worldIn.m_46859_(position.offset(i1 - dist, -3, i1 - dist)) && !worldIn.m_46859_(position.offset(-i1 + dist, -3, -i1 + dist)) && !worldIn.m_46859_(position.offset(-i1 + dist, -3, i1 - dist))) {
                int ySize = rand.nextInt(2);
                int j = i1 + rand.nextInt(2);
                int k = 5 + ySize;
                int l = i1 + rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockpos : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                    boolean doorwayX = blockpos.m_123341_() >= position.m_123341_() - 2 + rand.nextInt(2) && blockpos.m_123341_() <= position.m_123341_() + 2 + rand.nextInt(2);
                    boolean doorwayZ = blockpos.m_123343_() >= position.m_123343_() - 2 + rand.nextInt(2) && blockpos.m_123343_() <= position.m_123343_() + 2 + rand.nextInt(2);
                    boolean isNotInDoorway = !doorwayX && !doorwayZ && blockpos.m_123342_() > position.m_123342_() || blockpos.m_123342_() > position.m_123342_() + k - (1 + rand.nextInt(2));
                    if (blockpos.m_123331_(position) <= (double) (f * f)) {
                        if (!(worldIn.m_8055_(position).m_60734_() instanceof ChestBlock) && isNotInDoorway) {
                            worldIn.m_7731_(blockpos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                            if (worldIn.m_8055_(position.below()).m_60734_() == Blocks.GRASS_BLOCK) {
                                worldIn.m_7731_(blockpos.below(), Blocks.DIRT.defaultBlockState(), 3);
                            }
                            if (rand.nextInt(4) == 0) {
                                worldIn.m_7731_(blockpos.above(), Blocks.GRASS.defaultBlockState(), 2);
                            }
                            if (rand.nextInt(9) == 0) {
                                Holder<ConfiguredFeature<?, ?>> holder = (Holder<ConfiguredFeature<?, ?>>) context.level().m_9598_().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(TreeFeatures.SWAMP_OAK).orElse((Holder.Reference) null);
                                if (holder != null) {
                                    ((ConfiguredFeature) holder.get()).place(worldIn, generator, rand, blockpos.above());
                                }
                            }
                        }
                        if (blockpos.m_123342_() == position.m_123342_()) {
                            worldIn.m_7731_(blockpos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                        }
                        if (blockpos.m_123342_() <= position.m_123342_() - 1 && !worldIn.m_8055_(blockpos).m_60815_()) {
                            worldIn.m_7731_(blockpos, Blocks.STONE.defaultBlockState(), 3);
                        }
                    }
                }
                ySize = rand.nextInt(2);
                j = i2 + rand.nextInt(2);
                k = 4 + ySize;
                l = i2 + rand.nextInt(2);
                f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockposx : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                    if (blockposx.m_123331_(position) <= (double) (f * f) && blockposx.m_123342_() > position.m_123342_() && !(worldIn.m_8055_(position).m_60734_() instanceof ChestBlock)) {
                        worldIn.m_7731_(blockposx, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
                for (BlockPos blockposxx : (Set) BlockPos.betweenClosedStream(position.offset(-j, -k, -l), position.offset(j, k + 8, l)).map(BlockPos::m_7949_).collect(Collectors.toSet())) {
                    if (blockposxx.m_123331_(position) <= (double) (f * f) && blockposxx.m_123342_() == position.m_123342_()) {
                        if (rand.nextInt(30) == 0 && this.isTouchingAir(worldIn, blockposxx.above())) {
                            worldIn.m_7731_(blockposxx.above(1), (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.FACING, HORIZONTALS[new Random().nextInt(3)]), 2);
                            if (worldIn.m_8055_(blockposxx.above(1)).m_60734_() instanceof ChestBlock) {
                                BlockEntity tileentity1 = worldIn.m_7702_(blockposxx.above(1));
                                if (tileentity1 instanceof ChestBlockEntity) {
                                    ((ChestBlockEntity) tileentity1).m_59626_(HYDRA_CHEST, rand.nextLong());
                                }
                            }
                        } else if (rand.nextInt(45) == 0 && this.isTouchingAir(worldIn, blockposxx.above())) {
                            worldIn.m_7731_(blockposxx.above(), (BlockState) Blocks.SKELETON_SKULL.defaultBlockState().m_61124_(SkullBlock.ROTATION, rand.nextInt(15)), 2);
                        } else if (rand.nextInt(35) == 0 && this.isTouchingAir(worldIn, blockposxx.above())) {
                            worldIn.m_7731_(blockposxx.above(), (BlockState) Blocks.OAK_LEAVES.defaultBlockState().m_61124_(LeavesBlock.PERSISTENT, true), 2);
                            for (Direction facing : Direction.values()) {
                                if (rand.nextFloat() < 0.3F && facing != Direction.DOWN) {
                                    worldIn.m_7731_(blockposxx.above().relative(facing), Blocks.OAK_LEAVES.defaultBlockState(), 2);
                                }
                            }
                        } else if (rand.nextInt(15) == 0 && this.isTouchingAir(worldIn, blockposxx.above())) {
                            worldIn.m_7731_(blockposxx.above(), Blocks.TALL_GRASS.defaultBlockState(), 2);
                        } else if (rand.nextInt(15) == 0 && this.isTouchingAir(worldIn, blockposxx.above())) {
                            worldIn.m_7731_(blockposxx.above(), rand.nextBoolean() ? Blocks.BROWN_MUSHROOM.defaultBlockState() : Blocks.RED_MUSHROOM.defaultBlockState(), 2);
                        }
                    }
                }
                EntityHydra hydra = new EntityHydra(IafEntityRegistry.HYDRA.get(), worldIn.m_6018_());
                hydra.setVariant(rand.nextInt(3));
                hydra.m_21446_(position, 15);
                hydra.m_19890_((double) position.m_123341_() + 0.5, (double) position.m_123342_() + 1.5, (double) position.m_123343_() + 0.5, rand.nextFloat() * 360.0F, 0.0F);
                worldIn.m_7967_(hydra);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTouchingAir(LevelAccessor worldIn, BlockPos pos) {
        boolean isTouchingAir = true;
        for (Direction direction : HORIZONTALS) {
            if (!worldIn.m_46859_(pos.relative(direction))) {
                isTouchingAir = false;
            }
        }
        return isTouchingAir;
    }

    @Override
    public IafWorldData.FeatureType getFeatureType() {
        return IafWorldData.FeatureType.SURFACE;
    }

    @Override
    public String getId() {
        return "hydra_cave";
    }
}