package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LiquidBlockRenderer {

    private static final float MAX_FLUID_HEIGHT = 0.8888889F;

    private final TextureAtlasSprite[] lavaIcons = new TextureAtlasSprite[2];

    private final TextureAtlasSprite[] waterIcons = new TextureAtlasSprite[2];

    private TextureAtlasSprite waterOverlay;

    protected void setupSprites() {
        this.lavaIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.LAVA.defaultBlockState()).getParticleIcon();
        this.lavaIcons[1] = ModelBakery.LAVA_FLOW.sprite();
        this.waterIcons[0] = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(Blocks.WATER.defaultBlockState()).getParticleIcon();
        this.waterIcons[1] = ModelBakery.WATER_FLOW.sprite();
        this.waterOverlay = ModelBakery.WATER_OVERLAY.sprite();
    }

    private static boolean isNeighborSameFluid(FluidState fluidState0, FluidState fluidState1) {
        return fluidState1.getType().isSame(fluidState0.getType());
    }

    private static boolean isFaceOccludedByState(BlockGetter blockGetter0, Direction direction1, float float2, BlockPos blockPos3, BlockState blockState4) {
        if (blockState4.m_60815_()) {
            VoxelShape $$5 = Shapes.box(0.0, 0.0, 0.0, 1.0, (double) float2, 1.0);
            VoxelShape $$6 = blockState4.m_60768_(blockGetter0, blockPos3);
            return Shapes.blockOccudes($$5, $$6, direction1);
        } else {
            return false;
        }
    }

    private static boolean isFaceOccludedByNeighbor(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2, float float3, BlockState blockState4) {
        return isFaceOccludedByState(blockGetter0, direction2, float3, blockPos1.relative(direction2), blockState4);
    }

    private static boolean isFaceOccludedBySelf(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Direction direction3) {
        return isFaceOccludedByState(blockGetter0, direction3.getOpposite(), 1.0F, blockPos1, blockState2);
    }

    public static boolean shouldRenderFace(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1, FluidState fluidState2, BlockState blockState3, Direction direction4, FluidState fluidState5) {
        return !isFaceOccludedBySelf(blockAndTintGetter0, blockPos1, blockState3, direction4) && !isNeighborSameFluid(fluidState2, fluidState5);
    }

    public void tesselate(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1, VertexConsumer vertexConsumer2, BlockState blockState3, FluidState fluidState4) {
        boolean $$5 = fluidState4.is(FluidTags.LAVA);
        TextureAtlasSprite[] $$6 = $$5 ? this.lavaIcons : this.waterIcons;
        int $$7 = $$5 ? 16777215 : BiomeColors.getAverageWaterColor(blockAndTintGetter0, blockPos1);
        float $$8 = (float) ($$7 >> 16 & 0xFF) / 255.0F;
        float $$9 = (float) ($$7 >> 8 & 0xFF) / 255.0F;
        float $$10 = (float) ($$7 & 0xFF) / 255.0F;
        BlockState $$11 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.DOWN));
        FluidState $$12 = $$11.m_60819_();
        BlockState $$13 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.UP));
        FluidState $$14 = $$13.m_60819_();
        BlockState $$15 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.NORTH));
        FluidState $$16 = $$15.m_60819_();
        BlockState $$17 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.SOUTH));
        FluidState $$18 = $$17.m_60819_();
        BlockState $$19 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.WEST));
        FluidState $$20 = $$19.m_60819_();
        BlockState $$21 = blockAndTintGetter0.m_8055_(blockPos1.relative(Direction.EAST));
        FluidState $$22 = $$21.m_60819_();
        boolean $$23 = !isNeighborSameFluid(fluidState4, $$14);
        boolean $$24 = shouldRenderFace(blockAndTintGetter0, blockPos1, fluidState4, blockState3, Direction.DOWN, $$12) && !isFaceOccludedByNeighbor(blockAndTintGetter0, blockPos1, Direction.DOWN, 0.8888889F, $$11);
        boolean $$25 = shouldRenderFace(blockAndTintGetter0, blockPos1, fluidState4, blockState3, Direction.NORTH, $$16);
        boolean $$26 = shouldRenderFace(blockAndTintGetter0, blockPos1, fluidState4, blockState3, Direction.SOUTH, $$18);
        boolean $$27 = shouldRenderFace(blockAndTintGetter0, blockPos1, fluidState4, blockState3, Direction.WEST, $$20);
        boolean $$28 = shouldRenderFace(blockAndTintGetter0, blockPos1, fluidState4, blockState3, Direction.EAST, $$22);
        if ($$23 || $$24 || $$28 || $$27 || $$25 || $$26) {
            float $$29 = blockAndTintGetter0.getShade(Direction.DOWN, true);
            float $$30 = blockAndTintGetter0.getShade(Direction.UP, true);
            float $$31 = blockAndTintGetter0.getShade(Direction.NORTH, true);
            float $$32 = blockAndTintGetter0.getShade(Direction.WEST, true);
            Fluid $$33 = fluidState4.getType();
            float $$34 = this.getHeight(blockAndTintGetter0, $$33, blockPos1, blockState3, fluidState4);
            float $$35;
            float $$36;
            float $$37;
            float $$38;
            if ($$34 >= 1.0F) {
                $$35 = 1.0F;
                $$36 = 1.0F;
                $$37 = 1.0F;
                $$38 = 1.0F;
            } else {
                float $$39 = this.getHeight(blockAndTintGetter0, $$33, blockPos1.north(), $$15, $$16);
                float $$40 = this.getHeight(blockAndTintGetter0, $$33, blockPos1.south(), $$17, $$18);
                float $$41 = this.getHeight(blockAndTintGetter0, $$33, blockPos1.east(), $$21, $$22);
                float $$42 = this.getHeight(blockAndTintGetter0, $$33, blockPos1.west(), $$19, $$20);
                $$35 = this.calculateAverageHeight(blockAndTintGetter0, $$33, $$34, $$39, $$41, blockPos1.relative(Direction.NORTH).relative(Direction.EAST));
                $$36 = this.calculateAverageHeight(blockAndTintGetter0, $$33, $$34, $$39, $$42, blockPos1.relative(Direction.NORTH).relative(Direction.WEST));
                $$37 = this.calculateAverageHeight(blockAndTintGetter0, $$33, $$34, $$40, $$41, blockPos1.relative(Direction.SOUTH).relative(Direction.EAST));
                $$38 = this.calculateAverageHeight(blockAndTintGetter0, $$33, $$34, $$40, $$42, blockPos1.relative(Direction.SOUTH).relative(Direction.WEST));
            }
            double $$47 = (double) (blockPos1.m_123341_() & 15);
            double $$48 = (double) (blockPos1.m_123342_() & 15);
            double $$49 = (double) (blockPos1.m_123343_() & 15);
            float $$50 = 0.001F;
            float $$51 = $$24 ? 0.001F : 0.0F;
            if ($$23 && !isFaceOccludedByNeighbor(blockAndTintGetter0, blockPos1, Direction.UP, Math.min(Math.min($$36, $$38), Math.min($$37, $$35)), $$13)) {
                $$36 -= 0.001F;
                $$38 -= 0.001F;
                $$37 -= 0.001F;
                $$35 -= 0.001F;
                Vec3 $$52 = fluidState4.getFlow(blockAndTintGetter0, blockPos1);
                float $$54;
                float $$56;
                float $$58;
                float $$60;
                float $$55;
                float $$57;
                float $$59;
                float $$61;
                if ($$52.x == 0.0 && $$52.z == 0.0) {
                    TextureAtlasSprite $$53 = $$6[0];
                    $$54 = $$53.getU(0.0);
                    $$55 = $$53.getV(0.0);
                    $$56 = $$54;
                    $$57 = $$53.getV(16.0);
                    $$58 = $$53.getU(16.0);
                    $$59 = $$57;
                    $$60 = $$58;
                    $$61 = $$55;
                } else {
                    TextureAtlasSprite $$62 = $$6[1];
                    float $$63 = (float) Mth.atan2($$52.z, $$52.x) - (float) (Math.PI / 2);
                    float $$64 = Mth.sin($$63) * 0.25F;
                    float $$65 = Mth.cos($$63) * 0.25F;
                    float $$66 = 8.0F;
                    $$54 = $$62.getU((double) (8.0F + (-$$65 - $$64) * 16.0F));
                    $$55 = $$62.getV((double) (8.0F + (-$$65 + $$64) * 16.0F));
                    $$56 = $$62.getU((double) (8.0F + (-$$65 + $$64) * 16.0F));
                    $$57 = $$62.getV((double) (8.0F + ($$65 + $$64) * 16.0F));
                    $$58 = $$62.getU((double) (8.0F + ($$65 + $$64) * 16.0F));
                    $$59 = $$62.getV((double) (8.0F + ($$65 - $$64) * 16.0F));
                    $$60 = $$62.getU((double) (8.0F + ($$65 - $$64) * 16.0F));
                    $$61 = $$62.getV((double) (8.0F + (-$$65 - $$64) * 16.0F));
                }
                float $$75 = ($$54 + $$56 + $$58 + $$60) / 4.0F;
                float $$76 = ($$55 + $$57 + $$59 + $$61) / 4.0F;
                float $$77 = $$6[0].uvShrinkRatio();
                $$54 = Mth.lerp($$77, $$54, $$75);
                $$56 = Mth.lerp($$77, $$56, $$75);
                $$58 = Mth.lerp($$77, $$58, $$75);
                $$60 = Mth.lerp($$77, $$60, $$75);
                $$55 = Mth.lerp($$77, $$55, $$76);
                $$57 = Mth.lerp($$77, $$57, $$76);
                $$59 = Mth.lerp($$77, $$59, $$76);
                $$61 = Mth.lerp($$77, $$61, $$76);
                int $$78 = this.getLightColor(blockAndTintGetter0, blockPos1);
                float $$79 = $$30 * $$8;
                float $$80 = $$30 * $$9;
                float $$81 = $$30 * $$10;
                this.vertex(vertexConsumer2, $$47 + 0.0, $$48 + (double) $$36, $$49 + 0.0, $$79, $$80, $$81, $$54, $$55, $$78);
                this.vertex(vertexConsumer2, $$47 + 0.0, $$48 + (double) $$38, $$49 + 1.0, $$79, $$80, $$81, $$56, $$57, $$78);
                this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$37, $$49 + 1.0, $$79, $$80, $$81, $$58, $$59, $$78);
                this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$35, $$49 + 0.0, $$79, $$80, $$81, $$60, $$61, $$78);
                if (fluidState4.shouldRenderBackwardUpFace(blockAndTintGetter0, blockPos1.above())) {
                    this.vertex(vertexConsumer2, $$47 + 0.0, $$48 + (double) $$36, $$49 + 0.0, $$79, $$80, $$81, $$54, $$55, $$78);
                    this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$35, $$49 + 0.0, $$79, $$80, $$81, $$60, $$61, $$78);
                    this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$37, $$49 + 1.0, $$79, $$80, $$81, $$58, $$59, $$78);
                    this.vertex(vertexConsumer2, $$47 + 0.0, $$48 + (double) $$38, $$49 + 1.0, $$79, $$80, $$81, $$56, $$57, $$78);
                }
            }
            if ($$24) {
                float $$82 = $$6[0].getU0();
                float $$83 = $$6[0].getU1();
                float $$84 = $$6[0].getV0();
                float $$85 = $$6[0].getV1();
                int $$86 = this.getLightColor(blockAndTintGetter0, blockPos1.below());
                float $$87 = $$29 * $$8;
                float $$88 = $$29 * $$9;
                float $$89 = $$29 * $$10;
                this.vertex(vertexConsumer2, $$47, $$48 + (double) $$51, $$49 + 1.0, $$87, $$88, $$89, $$82, $$85, $$86);
                this.vertex(vertexConsumer2, $$47, $$48 + (double) $$51, $$49, $$87, $$88, $$89, $$82, $$84, $$86);
                this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$51, $$49, $$87, $$88, $$89, $$83, $$84, $$86);
                this.vertex(vertexConsumer2, $$47 + 1.0, $$48 + (double) $$51, $$49 + 1.0, $$87, $$88, $$89, $$83, $$85, $$86);
            }
            int $$90 = this.getLightColor(blockAndTintGetter0, blockPos1);
            for (Direction $$91 : Direction.Plane.HORIZONTAL) {
                float $$92;
                float $$93;
                double $$94;
                double $$96;
                double $$95;
                double $$97;
                boolean $$98;
                switch($$91) {
                    case NORTH:
                        $$92 = $$36;
                        $$93 = $$35;
                        $$94 = $$47;
                        $$95 = $$47 + 1.0;
                        $$96 = $$49 + 0.001F;
                        $$97 = $$49 + 0.001F;
                        $$98 = $$25;
                        break;
                    case SOUTH:
                        $$92 = $$37;
                        $$93 = $$38;
                        $$94 = $$47 + 1.0;
                        $$95 = $$47;
                        $$96 = $$49 + 1.0 - 0.001F;
                        $$97 = $$49 + 1.0 - 0.001F;
                        $$98 = $$26;
                        break;
                    case WEST:
                        $$92 = $$38;
                        $$93 = $$36;
                        $$94 = $$47 + 0.001F;
                        $$95 = $$47 + 0.001F;
                        $$96 = $$49 + 1.0;
                        $$97 = $$49;
                        $$98 = $$27;
                        break;
                    default:
                        $$92 = $$35;
                        $$93 = $$37;
                        $$94 = $$47 + 1.0 - 0.001F;
                        $$95 = $$47 + 1.0 - 0.001F;
                        $$96 = $$49;
                        $$97 = $$49 + 1.0;
                        $$98 = $$28;
                }
                if ($$98 && !isFaceOccludedByNeighbor(blockAndTintGetter0, blockPos1, $$91, Math.max($$92, $$93), blockAndTintGetter0.m_8055_(blockPos1.relative($$91)))) {
                    BlockPos $$120 = blockPos1.relative($$91);
                    TextureAtlasSprite $$121 = $$6[1];
                    if (!$$5) {
                        Block $$122 = blockAndTintGetter0.m_8055_($$120).m_60734_();
                        if ($$122 instanceof HalfTransparentBlock || $$122 instanceof LeavesBlock) {
                            $$121 = this.waterOverlay;
                        }
                    }
                    float $$123 = $$121.getU(0.0);
                    float $$124 = $$121.getU(8.0);
                    float $$125 = $$121.getV((double) ((1.0F - $$92) * 16.0F * 0.5F));
                    float $$126 = $$121.getV((double) ((1.0F - $$93) * 16.0F * 0.5F));
                    float $$127 = $$121.getV(8.0);
                    float $$128 = $$91.getAxis() == Direction.Axis.Z ? $$31 : $$32;
                    float $$129 = $$30 * $$128 * $$8;
                    float $$130 = $$30 * $$128 * $$9;
                    float $$131 = $$30 * $$128 * $$10;
                    this.vertex(vertexConsumer2, $$94, $$48 + (double) $$92, $$96, $$129, $$130, $$131, $$123, $$125, $$90);
                    this.vertex(vertexConsumer2, $$95, $$48 + (double) $$93, $$97, $$129, $$130, $$131, $$124, $$126, $$90);
                    this.vertex(vertexConsumer2, $$95, $$48 + (double) $$51, $$97, $$129, $$130, $$131, $$124, $$127, $$90);
                    this.vertex(vertexConsumer2, $$94, $$48 + (double) $$51, $$96, $$129, $$130, $$131, $$123, $$127, $$90);
                    if ($$121 != this.waterOverlay) {
                        this.vertex(vertexConsumer2, $$94, $$48 + (double) $$51, $$96, $$129, $$130, $$131, $$123, $$127, $$90);
                        this.vertex(vertexConsumer2, $$95, $$48 + (double) $$51, $$97, $$129, $$130, $$131, $$124, $$127, $$90);
                        this.vertex(vertexConsumer2, $$95, $$48 + (double) $$93, $$97, $$129, $$130, $$131, $$124, $$126, $$90);
                        this.vertex(vertexConsumer2, $$94, $$48 + (double) $$92, $$96, $$129, $$130, $$131, $$123, $$125, $$90);
                    }
                }
            }
        }
    }

    private float calculateAverageHeight(BlockAndTintGetter blockAndTintGetter0, Fluid fluid1, float float2, float float3, float float4, BlockPos blockPos5) {
        if (!(float4 >= 1.0F) && !(float3 >= 1.0F)) {
            float[] $$6 = new float[2];
            if (float4 > 0.0F || float3 > 0.0F) {
                float $$7 = this.getHeight(blockAndTintGetter0, fluid1, blockPos5);
                if ($$7 >= 1.0F) {
                    return 1.0F;
                }
                this.addWeightedHeight($$6, $$7);
            }
            this.addWeightedHeight($$6, float2);
            this.addWeightedHeight($$6, float4);
            this.addWeightedHeight($$6, float3);
            return $$6[0] / $$6[1];
        } else {
            return 1.0F;
        }
    }

    private void addWeightedHeight(float[] float0, float float1) {
        if (float1 >= 0.8F) {
            float0[0] += float1 * 10.0F;
            float0[1] += 10.0F;
        } else if (float1 >= 0.0F) {
            float0[0] += float1;
            float0[1]++;
        }
    }

    private float getHeight(BlockAndTintGetter blockAndTintGetter0, Fluid fluid1, BlockPos blockPos2) {
        BlockState $$3 = blockAndTintGetter0.m_8055_(blockPos2);
        return this.getHeight(blockAndTintGetter0, fluid1, blockPos2, $$3, $$3.m_60819_());
    }

    private float getHeight(BlockAndTintGetter blockAndTintGetter0, Fluid fluid1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4) {
        if (fluid1.isSame(fluidState4.getType())) {
            BlockState $$5 = blockAndTintGetter0.m_8055_(blockPos2.above());
            return fluid1.isSame($$5.m_60819_().getType()) ? 1.0F : fluidState4.getOwnHeight();
        } else {
            return !blockState3.m_280296_() ? 0.0F : -1.0F;
        }
    }

    private void vertex(VertexConsumer vertexConsumer0, double double1, double double2, double double3, float float4, float float5, float float6, float float7, float float8, int int9) {
        vertexConsumer0.vertex(double1, double2, double3).color(float4, float5, float6, 1.0F).uv(float7, float8).uv2(int9).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private int getLightColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        int $$2 = LevelRenderer.getLightColor(blockAndTintGetter0, blockPos1);
        int $$3 = LevelRenderer.getLightColor(blockAndTintGetter0, blockPos1.above());
        int $$4 = $$2 & 0xFF;
        int $$5 = $$3 & 0xFF;
        int $$6 = $$2 >> 16 & 0xFF;
        int $$7 = $$3 >> 16 & 0xFF;
        return ($$4 > $$5 ? $$4 : $$5) | ($$6 > $$7 ? $$6 : $$7) << 16;
    }
}