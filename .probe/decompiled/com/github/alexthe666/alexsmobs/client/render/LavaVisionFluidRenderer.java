package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public class LavaVisionFluidRenderer extends LiquidBlockRenderer {

    private static boolean isFaceOccludedByNeighbor(BlockGetter p_239283_0_, BlockPos p_239283_1_, Direction p_239283_2_, float p_239283_3_) {
        BlockPos blockpos = p_239283_1_.relative(p_239283_2_);
        BlockState blockstate = p_239283_0_.getBlockState(blockpos);
        return isFaceOccludedByState(p_239283_0_, p_239283_2_, p_239283_3_, blockpos, blockstate);
    }

    private static boolean isFaceOccludedByState(BlockGetter p_239284_0_, Direction p_239284_1_, float p_239284_2_, BlockPos p_239284_3_, BlockState p_239284_4_) {
        if (p_239284_4_.m_60815_()) {
            VoxelShape voxelshape = Shapes.box(0.0, 0.0, 0.0, 1.0, (double) p_239284_2_, 1.0);
            VoxelShape voxelshape1 = p_239284_4_.m_60768_(p_239284_0_, p_239284_3_);
            return Shapes.blockOccudes(voxelshape, voxelshape1, p_239284_1_);
        } else {
            return false;
        }
    }

    private static boolean isAdjacentFluidSameAs(BlockGetter worldIn, BlockPos pos, Direction side, FluidState state) {
        BlockPos blockpos = pos.relative(side);
        FluidState fluidstate = worldIn.getFluidState(blockpos);
        return fluidstate.getType().isSame(state.getType());
    }

    @Override
    public void tesselate(BlockAndTintGetter lightReaderIn, BlockPos posIn, VertexConsumer vertexBuilderIn, BlockState blockstateIn, FluidState fluidStateIn) {
        try {
            if (fluidStateIn.is(FluidTags.LAVA)) {
                TextureAtlasSprite[] atextureatlassprite = ForgeHooksClient.getFluidSprites(lightReaderIn, posIn, fluidStateIn);
                int i = IClientFluidTypeExtensions.of(fluidStateIn).getTintColor(fluidStateIn, lightReaderIn, posIn);
                float alpha = (float) AMConfig.lavaOpacity;
                float f = (float) (i >> 16 & 0xFF) / 255.0F;
                float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
                float f2 = (float) (i & 0xFF) / 255.0F;
                BlockState blockstate = lightReaderIn.m_8055_(posIn.relative(Direction.DOWN));
                FluidState fluidstate = blockstate.m_60819_();
                BlockState blockstate1 = lightReaderIn.m_8055_(posIn.relative(Direction.UP));
                FluidState fluidstate1 = blockstate1.m_60819_();
                BlockState blockstate2 = lightReaderIn.m_8055_(posIn.relative(Direction.NORTH));
                FluidState fluidstate2 = blockstate2.m_60819_();
                BlockState blockstate3 = lightReaderIn.m_8055_(posIn.relative(Direction.SOUTH));
                FluidState fluidstate3 = blockstate3.m_60819_();
                BlockState blockstate4 = lightReaderIn.m_8055_(posIn.relative(Direction.WEST));
                FluidState fluidstate4 = blockstate4.m_60819_();
                BlockState blockstate5 = lightReaderIn.m_8055_(posIn.relative(Direction.EAST));
                FluidState fluidstate5 = blockstate5.m_60819_();
                boolean flag1 = !isNeighborSameFluidVanilla(fluidStateIn, fluidstate1);
                boolean flag2 = m_203166_(lightReaderIn, posIn, fluidStateIn, blockstateIn, Direction.DOWN, fluidstate) && !isFaceOccludedByNeighborVanilla(lightReaderIn, posIn, Direction.DOWN, 0.8888889F, blockstate);
                boolean flag3 = m_203166_(lightReaderIn, posIn, fluidStateIn, blockstateIn, Direction.NORTH, fluidstate2);
                boolean flag4 = m_203166_(lightReaderIn, posIn, fluidStateIn, blockstateIn, Direction.SOUTH, fluidstate3);
                boolean flag5 = m_203166_(lightReaderIn, posIn, fluidStateIn, blockstateIn, Direction.WEST, fluidstate4);
                boolean flag6 = m_203166_(lightReaderIn, posIn, fluidStateIn, blockstateIn, Direction.EAST, fluidstate5);
                if (!flag1 && !flag2 && !flag6 && !flag5 && !flag3 && !flag4) {
                    return;
                }
                float f3 = lightReaderIn.getShade(Direction.DOWN, true);
                float f4 = lightReaderIn.getShade(Direction.UP, true);
                float f5 = lightReaderIn.getShade(Direction.NORTH, true);
                float f6 = lightReaderIn.getShade(Direction.WEST, true);
                Fluid fluid = fluidStateIn.getType();
                float f11 = this.getFluidHeight(lightReaderIn, posIn, fluid);
                float f7;
                float f8;
                float f9;
                float f10;
                if (f11 >= 1.0F) {
                    f7 = 1.0F;
                    f8 = 1.0F;
                    f9 = 1.0F;
                    f10 = 1.0F;
                } else {
                    float f12 = this.getHeight(lightReaderIn, fluid, posIn.north(), blockstate2, fluidstate2);
                    float f13 = this.getHeight(lightReaderIn, fluid, posIn.south(), blockstate3, fluidstate3);
                    float f14 = this.getHeight(lightReaderIn, fluid, posIn.east(), blockstate5, fluidstate5);
                    float f15 = this.getHeight(lightReaderIn, fluid, posIn.west(), blockstate4, fluidstate4);
                    f7 = this.calculateAverageHeight(lightReaderIn, fluid, f11, f12, f14, posIn.relative(Direction.NORTH).relative(Direction.EAST));
                    f8 = this.calculateAverageHeight(lightReaderIn, fluid, f11, f12, f15, posIn.relative(Direction.NORTH).relative(Direction.WEST));
                    f9 = this.calculateAverageHeight(lightReaderIn, fluid, f11, f13, f14, posIn.relative(Direction.SOUTH).relative(Direction.EAST));
                    f10 = this.calculateAverageHeight(lightReaderIn, fluid, f11, f13, f15, posIn.relative(Direction.SOUTH).relative(Direction.WEST));
                }
                double d1 = (double) (posIn.m_123341_() & 15);
                double d2 = (double) (posIn.m_123342_() & 15);
                double d0 = (double) (posIn.m_123343_() & 15);
                float f17 = flag2 ? 0.001F : 0.0F;
                if (flag1 && !isFaceOccludedByNeighborVanilla(lightReaderIn, posIn, Direction.UP, Math.min(Math.min(f8, f10), Math.min(f9, f7)), blockstate1)) {
                    f8 -= 0.001F;
                    f10 -= 0.001F;
                    f9 -= 0.001F;
                    f7 -= 0.001F;
                    Vec3 vec3 = fluidStateIn.getFlow(lightReaderIn, posIn);
                    float f18;
                    float f19;
                    float f20;
                    float f21;
                    float f22;
                    float f23;
                    float f24;
                    float f25;
                    if (vec3.x == 0.0 && vec3.z == 0.0) {
                        TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
                        f18 = textureatlassprite1.getU(0.0);
                        f22 = textureatlassprite1.getV(0.0);
                        f19 = f18;
                        f23 = textureatlassprite1.getV(16.0);
                        f20 = textureatlassprite1.getU(16.0);
                        f24 = f23;
                        f21 = f20;
                        f25 = f22;
                    } else {
                        TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
                        float f26 = (float) Mth.atan2(vec3.z, vec3.x) - (float) (Math.PI / 2);
                        float f27 = Mth.sin(f26) * 0.25F;
                        float f28 = Mth.cos(f26) * 0.25F;
                        f18 = textureatlassprite.getU((double) (8.0F + (-f28 - f27) * 16.0F));
                        f22 = textureatlassprite.getV((double) (8.0F + (-f28 + f27) * 16.0F));
                        f19 = textureatlassprite.getU((double) (8.0F + (-f28 + f27) * 16.0F));
                        f23 = textureatlassprite.getV((double) (8.0F + (f28 + f27) * 16.0F));
                        f20 = textureatlassprite.getU((double) (8.0F + (f28 + f27) * 16.0F));
                        f24 = textureatlassprite.getV((double) (8.0F + (f28 - f27) * 16.0F));
                        f21 = textureatlassprite.getU((double) (8.0F + (f28 - f27) * 16.0F));
                        f25 = textureatlassprite.getV((double) (8.0F + (-f28 - f27) * 16.0F));
                    }
                    float f49 = (f18 + f19 + f20 + f21) / 4.0F;
                    float f50 = (f22 + f23 + f24 + f25) / 4.0F;
                    float f51 = (float) atextureatlassprite[0].contents().width() / (atextureatlassprite[0].getU1() - atextureatlassprite[0].getU0());
                    float f52 = (float) atextureatlassprite[0].contents().height() / (atextureatlassprite[0].getV1() - atextureatlassprite[0].getV0());
                    float f53 = 4.0F / Math.max(f52, f51);
                    f18 = Mth.lerp(f53, f18, f49);
                    f19 = Mth.lerp(f53, f19, f49);
                    f20 = Mth.lerp(f53, f20, f49);
                    f21 = Mth.lerp(f53, f21, f49);
                    f22 = Mth.lerp(f53, f22, f50);
                    f23 = Mth.lerp(f53, f23, f50);
                    f24 = Mth.lerp(f53, f24, f50);
                    f25 = Mth.lerp(f53, f25, f50);
                    int j = this.getCombinedAverageLight(lightReaderIn, posIn);
                    float f30 = f4 * f;
                    float f31 = f4 * f1;
                    float f32 = f4 * f2;
                    this.vertexVanilla(vertexBuilderIn, d1 + 0.0, d2 + (double) f8, d0 + 0.0, f30, f31, f32, alpha, f18, f22, j);
                    this.vertexVanilla(vertexBuilderIn, d1 + 0.0, d2 + (double) f10, d0 + 1.0, f30, f31, f32, alpha, f19, f23, j);
                    this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f9, d0 + 1.0, f30, f31, f32, alpha, f20, f24, j);
                    this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f7, d0 + 0.0, f30, f31, f32, alpha, f21, f25, j);
                    if (fluidStateIn.shouldRenderBackwardUpFace(lightReaderIn, posIn.above())) {
                        this.vertexVanilla(vertexBuilderIn, d1 + 0.0, d2 + (double) f8, d0 + 0.0, f30, f31, f32, alpha, f18, f22, j);
                        this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f7, d0 + 0.0, f30, f31, f32, alpha, f21, f25, j);
                        this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f9, d0 + 1.0, f30, f31, f32, alpha, f20, f24, j);
                        this.vertexVanilla(vertexBuilderIn, d1 + 0.0, d2 + (double) f10, d0 + 1.0, f30, f31, f32, alpha, f19, f23, j);
                    }
                }
                if (flag2) {
                    float f40 = atextureatlassprite[0].getU0();
                    float f41 = atextureatlassprite[0].getU1();
                    float f42 = atextureatlassprite[0].getV0();
                    float f43 = atextureatlassprite[0].getV1();
                    int l = this.getCombinedAverageLight(lightReaderIn, posIn.below());
                    float f46 = f3 * f;
                    float f47 = f3 * f1;
                    float f48 = f3 * f2;
                    this.vertexVanilla(vertexBuilderIn, d1, d2 + (double) f17, d0 + 1.0, f46, f47, f48, alpha, f40, f43, l);
                    this.vertexVanilla(vertexBuilderIn, d1, d2 + (double) f17, d0, f46, f47, f48, alpha, f40, f42, l);
                    this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f17, d0, f46, f47, f48, alpha, f41, f42, l);
                    this.vertexVanilla(vertexBuilderIn, d1 + 1.0, d2 + (double) f17, d0 + 1.0, f46, f47, f48, alpha, f41, f43, l);
                }
                int k = this.getCombinedAverageLight(lightReaderIn, posIn);
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    float f44;
                    float f45;
                    double d3;
                    double d4;
                    double d5;
                    double d6;
                    boolean flag8;
                    switch(direction) {
                        case NORTH:
                            f44 = f8;
                            f45 = f7;
                            d3 = d1;
                            d5 = d1 + 1.0;
                            d4 = d0 + 0.001F;
                            d6 = d0 + 0.001F;
                            flag8 = flag3;
                            break;
                        case SOUTH:
                            f44 = f9;
                            f45 = f10;
                            d3 = d1 + 1.0;
                            d5 = d1;
                            d4 = d0 + 1.0 - 0.001F;
                            d6 = d0 + 1.0 - 0.001F;
                            flag8 = flag4;
                            break;
                        case WEST:
                            f44 = f10;
                            f45 = f8;
                            d3 = d1 + 0.001F;
                            d5 = d1 + 0.001F;
                            d4 = d0 + 1.0;
                            d6 = d0;
                            flag8 = flag5;
                            break;
                        default:
                            f44 = f7;
                            f45 = f9;
                            d3 = d1 + 1.0 - 0.001F;
                            d5 = d1 + 1.0 - 0.001F;
                            d4 = d0;
                            d6 = d0 + 1.0;
                            flag8 = flag6;
                    }
                    if (flag8 && !isFaceOccludedByNeighborVanilla(lightReaderIn, posIn, direction, Math.max(f44, f45), lightReaderIn.m_8055_(posIn.relative(direction)))) {
                        BlockPos blockpos = posIn.relative(direction);
                        TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
                        if (atextureatlassprite[2] != null && lightReaderIn.m_8055_(blockpos).shouldDisplayFluidOverlay(lightReaderIn, blockpos, fluidStateIn)) {
                            textureatlassprite2 = atextureatlassprite[2];
                        }
                        float f54 = textureatlassprite2.getU(0.0);
                        float f55 = textureatlassprite2.getU(8.0);
                        float f33 = textureatlassprite2.getV((double) ((1.0F - f44) * 16.0F * 0.5F));
                        float f34 = textureatlassprite2.getV((double) ((1.0F - f45) * 16.0F * 0.5F));
                        float f35 = textureatlassprite2.getV(8.0);
                        float f36 = direction.getAxis() == Direction.Axis.Z ? f5 : f6;
                        float f37 = f4 * f36 * f;
                        float f38 = f4 * f36 * f1;
                        float f39 = f4 * f36 * f2;
                        this.vertexVanilla(vertexBuilderIn, d3, d2 + (double) f44, d4, f37, f38, f39, alpha, f54, f33, k);
                        this.vertexVanilla(vertexBuilderIn, d5, d2 + (double) f45, d6, f37, f38, f39, alpha, f55, f34, k);
                        this.vertexVanilla(vertexBuilderIn, d5, d2 + (double) f17, d6, f37, f38, f39, alpha, f55, f35, k);
                        this.vertexVanilla(vertexBuilderIn, d3, d2 + (double) f17, d4, f37, f38, f39, alpha, f54, f35, k);
                    }
                }
                return;
            }
            super.tesselate(lightReaderIn, posIn, vertexBuilderIn, blockstateIn, fluidStateIn);
        } catch (Exception var72) {
        }
    }

    private void vertexVanilla(VertexConsumer vertexBuilderIn, double x, double y, double z, float red, float green, float blue, float alpha, float u, float v, int packedLight) {
        vertexBuilderIn.vertex(x, y, z).color(red, green, blue, alpha).uv(u, v).uv2(packedLight).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private int getCombinedAverageLight(BlockAndTintGetter lightReaderIn, BlockPos posIn) {
        int i = LevelRenderer.getLightColor(lightReaderIn, posIn);
        int j = LevelRenderer.getLightColor(lightReaderIn, posIn.above());
        int k = i & 0xFF;
        int l = j & 0xFF;
        int i1 = i >> 16 & 0xFF;
        int j1 = j >> 16 & 0xFF;
        return Math.max(k, l) | Math.max(i1, j1) << 16;
    }

    private float getFluidHeight(BlockGetter reader, BlockPos pos, Fluid fluidIn) {
        int i = 0;
        float f = 0.0F;
        for (int j = 0; j < 4; j++) {
            BlockPos blockpos = pos.offset(-(j & 1), 0, -(j >> 1 & 1));
            if (reader.getFluidState(blockpos.above()).getType().isSame(fluidIn)) {
                return 1.0F;
            }
            FluidState fluidstate = reader.getFluidState(blockpos);
            if (fluidstate.getType().isSame(fluidIn)) {
                float f1 = fluidstate.getHeight(reader, blockpos);
                if (f1 >= 0.8F) {
                    f += f1 * 10.0F;
                    i += 10;
                } else {
                    f += f1;
                    i++;
                }
            } else if (!reader.getBlockState(blockpos).m_280296_()) {
                i++;
            }
        }
        return f / (float) i;
    }

    private static boolean isNeighborSameFluidVanilla(FluidState fluidState0, FluidState fluidState1) {
        return fluidState1.getType().isSame(fluidState0.getType());
    }

    private static boolean isFaceOccludedByStateVanilla(BlockGetter blockGetter0, Direction direction1, float float2, BlockPos blockPos3, BlockState blockState4) {
        if (blockState4.m_60815_()) {
            VoxelShape voxelshape = Shapes.box(0.0, 0.0, 0.0, 1.0, (double) float2, 1.0);
            VoxelShape voxelshape1 = blockState4.m_60768_(blockGetter0, blockPos3);
            return Shapes.blockOccudes(voxelshape, voxelshape1, direction1);
        } else {
            return false;
        }
    }

    private static boolean isFaceOccludedByNeighborVanilla(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2, float float3, BlockState blockState4) {
        return isFaceOccludedByStateVanilla(blockGetter0, direction2, float3, blockPos1.relative(direction2), blockState4);
    }

    private static boolean isFaceOccludedBySelfVanilla(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Direction direction3) {
        return isFaceOccludedByStateVanilla(blockGetter0, direction3.getOpposite(), 1.0F, blockPos1, blockState2);
    }

    private float calculateAverageHeight(BlockAndTintGetter blockAndTintGetter0, Fluid fluid1, float float2, float float3, float float4, BlockPos blockPos5) {
        if (!(float4 >= 1.0F) && !(float3 >= 1.0F)) {
            float[] afloat = new float[2];
            if (float4 > 0.0F || float3 > 0.0F) {
                float f = this.getHeight(blockAndTintGetter0, fluid1, blockPos5);
                if (f >= 1.0F) {
                    return 1.0F;
                }
                this.addWeightedHeight(afloat, f);
            }
            this.addWeightedHeight(afloat, float2);
            this.addWeightedHeight(afloat, float4);
            this.addWeightedHeight(afloat, float3);
            return afloat[0] / afloat[1];
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
        BlockState blockstate = blockAndTintGetter0.m_8055_(blockPos2);
        return this.getHeight(blockAndTintGetter0, fluid1, blockPos2, blockstate, blockstate.m_60819_());
    }

    private float getHeight(BlockAndTintGetter blockAndTintGetter0, Fluid fluid1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4) {
        if (fluid1.isSame(fluidState4.getType())) {
            BlockState blockstate = blockAndTintGetter0.m_8055_(blockPos2.above());
            return fluid1.isSame(blockstate.m_60819_().getType()) ? 1.0F : fluidState4.getOwnHeight();
        } else {
            return !blockState3.m_280296_() ? 0.0F : -1.0F;
        }
    }
}