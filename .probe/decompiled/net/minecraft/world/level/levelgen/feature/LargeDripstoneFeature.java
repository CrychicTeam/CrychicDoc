package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.phys.Vec3;

public class LargeDripstoneFeature extends Feature<LargeDripstoneConfiguration> {

    public LargeDripstoneFeature(Codec<LargeDripstoneConfiguration> codecLargeDripstoneConfiguration0) {
        super(codecLargeDripstoneConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeDripstoneConfiguration> featurePlaceContextLargeDripstoneConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextLargeDripstoneConfiguration0.level();
        BlockPos $$2 = featurePlaceContextLargeDripstoneConfiguration0.origin();
        LargeDripstoneConfiguration $$3 = featurePlaceContextLargeDripstoneConfiguration0.config();
        RandomSource $$4 = featurePlaceContextLargeDripstoneConfiguration0.random();
        if (!DripstoneUtils.isEmptyOrWater($$1, $$2)) {
            return false;
        } else {
            Optional<Column> $$5 = Column.scan($$1, $$2, $$3.floorToCeilingSearchRange, DripstoneUtils::m_159664_, DripstoneUtils::m_159649_);
            if ($$5.isPresent() && $$5.get() instanceof Column.Range) {
                Column.Range $$6 = (Column.Range) $$5.get();
                if ($$6.height() < 4) {
                    return false;
                } else {
                    int $$7 = (int) ((float) $$6.height() * $$3.maxColumnRadiusToCaveHeightRatio);
                    int $$8 = Mth.clamp($$7, $$3.columnRadius.getMinValue(), $$3.columnRadius.getMaxValue());
                    int $$9 = Mth.randomBetweenInclusive($$4, $$3.columnRadius.getMinValue(), $$8);
                    LargeDripstoneFeature.LargeDripstone $$10 = makeDripstone($$2.atY($$6.ceiling() - 1), false, $$4, $$9, $$3.stalactiteBluntness, $$3.heightScale);
                    LargeDripstoneFeature.LargeDripstone $$11 = makeDripstone($$2.atY($$6.floor() + 1), true, $$4, $$9, $$3.stalagmiteBluntness, $$3.heightScale);
                    LargeDripstoneFeature.WindOffsetter $$12;
                    if ($$10.isSuitableForWind($$3) && $$11.isSuitableForWind($$3)) {
                        $$12 = new LargeDripstoneFeature.WindOffsetter($$2.m_123342_(), $$4, $$3.windSpeed);
                    } else {
                        $$12 = LargeDripstoneFeature.WindOffsetter.noWind();
                    }
                    boolean $$14 = $$10.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary($$1, $$12);
                    boolean $$15 = $$11.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary($$1, $$12);
                    if ($$14) {
                        $$10.placeBlocks($$1, $$4, $$12);
                    }
                    if ($$15) {
                        $$11.placeBlocks($$1, $$4, $$12);
                    }
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private static LargeDripstoneFeature.LargeDripstone makeDripstone(BlockPos blockPos0, boolean boolean1, RandomSource randomSource2, int int3, FloatProvider floatProvider4, FloatProvider floatProvider5) {
        return new LargeDripstoneFeature.LargeDripstone(blockPos0, boolean1, int3, (double) floatProvider4.m_214084_(randomSource2), (double) floatProvider5.m_214084_(randomSource2));
    }

    private void placeDebugMarkers(WorldGenLevel worldGenLevel0, BlockPos blockPos1, Column.Range columnRange2, LargeDripstoneFeature.WindOffsetter largeDripstoneFeatureWindOffsetter3) {
        worldGenLevel0.m_7731_(largeDripstoneFeatureWindOffsetter3.offset(blockPos1.atY(columnRange2.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        worldGenLevel0.m_7731_(largeDripstoneFeatureWindOffsetter3.offset(blockPos1.atY(columnRange2.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        for (BlockPos.MutableBlockPos $$4 = blockPos1.atY(columnRange2.floor() + 2).mutable(); $$4.m_123342_() < columnRange2.ceiling() - 1; $$4.move(Direction.UP)) {
            BlockPos $$5 = largeDripstoneFeatureWindOffsetter3.offset($$4);
            if (DripstoneUtils.isEmptyOrWater(worldGenLevel0, $$5) || worldGenLevel0.m_8055_($$5).m_60713_(Blocks.DRIPSTONE_BLOCK)) {
                worldGenLevel0.m_7731_($$5, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
            }
        }
    }

    static final class LargeDripstone {

        private BlockPos root;

        private final boolean pointingUp;

        private int radius;

        private final double bluntness;

        private final double scale;

        LargeDripstone(BlockPos blockPos0, boolean boolean1, int int2, double double3, double double4) {
            this.root = blockPos0;
            this.pointingUp = boolean1;
            this.radius = int2;
            this.bluntness = double3;
            this.scale = double4;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        private int getMinY() {
            return this.pointingUp ? this.root.m_123342_() : this.root.m_123342_() - this.getHeight();
        }

        private int getMaxY() {
            return !this.pointingUp ? this.root.m_123342_() : this.root.m_123342_() + this.getHeight();
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel worldGenLevel0, LargeDripstoneFeature.WindOffsetter largeDripstoneFeatureWindOffsetter1) {
            while (this.radius > 1) {
                BlockPos.MutableBlockPos $$2 = this.root.mutable();
                int $$3 = Math.min(10, this.getHeight());
                for (int $$4 = 0; $$4 < $$3; $$4++) {
                    if (worldGenLevel0.m_8055_($$2).m_60713_(Blocks.LAVA)) {
                        return false;
                    }
                    if (DripstoneUtils.isCircleMostlyEmbeddedInStone(worldGenLevel0, largeDripstoneFeatureWindOffsetter1.offset($$2), this.radius)) {
                        this.root = $$2;
                        return true;
                    }
                    $$2.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }
                this.radius /= 2;
            }
            return false;
        }

        private int getHeightAtRadius(float float0) {
            return (int) DripstoneUtils.getDripstoneHeight((double) float0, (double) this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel worldGenLevel0, RandomSource randomSource1, LargeDripstoneFeature.WindOffsetter largeDripstoneFeatureWindOffsetter2) {
            for (int $$3 = -this.radius; $$3 <= this.radius; $$3++) {
                for (int $$4 = -this.radius; $$4 <= this.radius; $$4++) {
                    float $$5 = Mth.sqrt((float) ($$3 * $$3 + $$4 * $$4));
                    if (!($$5 > (float) this.radius)) {
                        int $$6 = this.getHeightAtRadius($$5);
                        if ($$6 > 0) {
                            if ((double) randomSource1.nextFloat() < 0.2) {
                                $$6 = (int) ((float) $$6 * Mth.randomBetween(randomSource1, 0.8F, 1.0F));
                            }
                            BlockPos.MutableBlockPos $$7 = this.root.offset($$3, 0, $$4).mutable();
                            boolean $$8 = false;
                            int $$9 = this.pointingUp ? worldGenLevel0.m_6924_(Heightmap.Types.WORLD_SURFACE_WG, $$7.m_123341_(), $$7.m_123343_()) : Integer.MAX_VALUE;
                            for (int $$10 = 0; $$10 < $$6 && $$7.m_123342_() < $$9; $$10++) {
                                BlockPos $$11 = largeDripstoneFeatureWindOffsetter2.offset($$7);
                                if (DripstoneUtils.isEmptyOrWaterOrLava(worldGenLevel0, $$11)) {
                                    $$8 = true;
                                    Block $$12 = Blocks.DRIPSTONE_BLOCK;
                                    worldGenLevel0.m_7731_($$11, $$12.defaultBlockState(), 2);
                                } else if ($$8 && worldGenLevel0.m_8055_($$11).m_204336_(BlockTags.BASE_STONE_OVERWORLD)) {
                                    break;
                                }
                                $$7.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }
        }

        boolean isSuitableForWind(LargeDripstoneConfiguration largeDripstoneConfiguration0) {
            return this.radius >= largeDripstoneConfiguration0.minRadiusForWind && this.bluntness >= (double) largeDripstoneConfiguration0.minBluntnessForWind;
        }
    }

    static final class WindOffsetter {

        private final int originY;

        @Nullable
        private final Vec3 windSpeed;

        WindOffsetter(int int0, RandomSource randomSource1, FloatProvider floatProvider2) {
            this.originY = int0;
            float $$3 = floatProvider2.m_214084_(randomSource1);
            float $$4 = Mth.randomBetween(randomSource1, 0.0F, (float) Math.PI);
            this.windSpeed = new Vec3((double) (Mth.cos($$4) * $$3), 0.0, (double) (Mth.sin($$4) * $$3));
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        static LargeDripstoneFeature.WindOffsetter noWind() {
            return new LargeDripstoneFeature.WindOffsetter();
        }

        BlockPos offset(BlockPos blockPos0) {
            if (this.windSpeed == null) {
                return blockPos0;
            } else {
                int $$1 = this.originY - blockPos0.m_123342_();
                Vec3 $$2 = this.windSpeed.scale((double) $$1);
                return blockPos0.offset(Mth.floor($$2.x), 0, Mth.floor($$2.z));
            }
        }
    }
}