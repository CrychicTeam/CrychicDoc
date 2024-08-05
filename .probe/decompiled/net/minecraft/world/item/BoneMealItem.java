package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BoneMealItem extends Item {

    public static final int GRASS_SPREAD_WIDTH = 3;

    public static final int GRASS_SPREAD_HEIGHT = 1;

    public static final int GRASS_COUNT_MULTIPLIER = 3;

    public BoneMealItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockPos $$3 = $$2.relative(useOnContext0.getClickedFace());
        if (growCrop(useOnContext0.getItemInHand(), $$1, $$2)) {
            if (!$$1.isClientSide) {
                $$1.m_46796_(1505, $$2, 0);
            }
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            BlockState $$4 = $$1.getBlockState($$2);
            boolean $$5 = $$4.m_60783_($$1, $$2, useOnContext0.getClickedFace());
            if ($$5 && growWaterPlant(useOnContext0.getItemInHand(), $$1, $$3, useOnContext0.getClickedFace())) {
                if (!$$1.isClientSide) {
                    $$1.m_46796_(1505, $$3, 0);
                }
                return InteractionResult.sidedSuccess($$1.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static boolean growCrop(ItemStack itemStack0, Level level1, BlockPos blockPos2) {
        BlockState $$3 = level1.getBlockState(blockPos2);
        if ($$3.m_60734_() instanceof BonemealableBlock) {
            BonemealableBlock $$4 = (BonemealableBlock) $$3.m_60734_();
            if ($$4.isValidBonemealTarget(level1, blockPos2, $$3, level1.isClientSide)) {
                if (level1 instanceof ServerLevel) {
                    if ($$4.isBonemealSuccess(level1, level1.random, blockPos2, $$3)) {
                        $$4.performBonemeal((ServerLevel) level1, level1.random, blockPos2, $$3);
                    }
                    itemStack0.shrink(1);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean growWaterPlant(ItemStack itemStack0, Level level1, BlockPos blockPos2, @Nullable Direction direction3) {
        if (level1.getBlockState(blockPos2).m_60713_(Blocks.WATER) && level1.getFluidState(blockPos2).getAmount() == 8) {
            if (!(level1 instanceof ServerLevel)) {
                return true;
            } else {
                RandomSource $$4 = level1.getRandom();
                label78: for (int $$5 = 0; $$5 < 128; $$5++) {
                    BlockPos $$6 = blockPos2;
                    BlockState $$7 = Blocks.SEAGRASS.defaultBlockState();
                    for (int $$8 = 0; $$8 < $$5 / 16; $$8++) {
                        $$6 = $$6.offset($$4.nextInt(3) - 1, ($$4.nextInt(3) - 1) * $$4.nextInt(3) / 2, $$4.nextInt(3) - 1);
                        if (level1.getBlockState($$6).m_60838_(level1, $$6)) {
                            continue label78;
                        }
                    }
                    Holder<Biome> $$9 = level1.m_204166_($$6);
                    if ($$9.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if ($$5 == 0 && direction3 != null && direction3.getAxis().isHorizontal()) {
                            $$7 = (BlockState) BuiltInRegistries.BLOCK.m_203431_(BlockTags.WALL_CORALS).flatMap(p_204098_ -> p_204098_.m_213653_(level1.random)).map(p_204100_ -> ((Block) p_204100_.value()).defaultBlockState()).orElse($$7);
                            if ($$7.m_61138_(BaseCoralWallFanBlock.FACING)) {
                                $$7 = (BlockState) $$7.m_61124_(BaseCoralWallFanBlock.FACING, direction3);
                            }
                        } else if ($$4.nextInt(4) == 0) {
                            $$7 = (BlockState) BuiltInRegistries.BLOCK.m_203431_(BlockTags.UNDERWATER_BONEMEALS).flatMap(p_204091_ -> p_204091_.m_213653_(level1.random)).map(p_204095_ -> ((Block) p_204095_.value()).defaultBlockState()).orElse($$7);
                        }
                    }
                    if ($$7.m_204338_(BlockTags.WALL_CORALS, p_204093_ -> p_204093_.m_61138_(BaseCoralWallFanBlock.FACING))) {
                        for (int $$10 = 0; !$$7.m_60710_(level1, $$6) && $$10 < 4; $$10++) {
                            $$7 = (BlockState) $$7.m_61124_(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection($$4));
                        }
                    }
                    if ($$7.m_60710_(level1, $$6)) {
                        BlockState $$11 = level1.getBlockState($$6);
                        if ($$11.m_60713_(Blocks.WATER) && level1.getFluidState($$6).getAmount() == 8) {
                            level1.setBlock($$6, $$7, 3);
                        } else if ($$11.m_60713_(Blocks.SEAGRASS) && $$4.nextInt(10) == 0) {
                            ((BonemealableBlock) Blocks.SEAGRASS).performBonemeal((ServerLevel) level1, $$4, $$6, $$11);
                        }
                    }
                }
                itemStack0.shrink(1);
                return true;
            }
        } else {
            return false;
        }
    }

    public static void addGrowthParticles(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2) {
        if (int2 == 0) {
            int2 = 15;
        }
        BlockState $$3 = levelAccessor0.m_8055_(blockPos1);
        if (!$$3.m_60795_()) {
            double $$4 = 0.5;
            double $$5;
            if ($$3.m_60713_(Blocks.WATER)) {
                int2 *= 3;
                $$5 = 1.0;
                $$4 = 3.0;
            } else if ($$3.m_60804_(levelAccessor0, blockPos1)) {
                blockPos1 = blockPos1.above();
                int2 *= 3;
                $$4 = 3.0;
                $$5 = 1.0;
            } else {
                $$5 = $$3.m_60808_(levelAccessor0, blockPos1).max(Direction.Axis.Y);
            }
            levelAccessor0.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, 0.0, 0.0, 0.0);
            RandomSource $$8 = levelAccessor0.getRandom();
            for (int $$9 = 0; $$9 < int2; $$9++) {
                double $$10 = $$8.nextGaussian() * 0.02;
                double $$11 = $$8.nextGaussian() * 0.02;
                double $$12 = $$8.nextGaussian() * 0.02;
                double $$13 = 0.5 - $$4;
                double $$14 = (double) blockPos1.m_123341_() + $$13 + $$8.nextDouble() * $$4 * 2.0;
                double $$15 = (double) blockPos1.m_123342_() + $$8.nextDouble() * $$5;
                double $$16 = (double) blockPos1.m_123343_() + $$13 + $$8.nextDouble() * $$4 * 2.0;
                if (!levelAccessor0.m_8055_(BlockPos.containing($$14, $$15, $$16).below()).m_60795_()) {
                    levelAccessor0.addParticle(ParticleTypes.HAPPY_VILLAGER, $$14, $$15, $$16, $$10, $$11, $$12);
                }
            }
        }
    }
}