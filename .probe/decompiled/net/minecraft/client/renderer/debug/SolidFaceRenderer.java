package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;

public class SolidFaceRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    public SolidFaceRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Matrix4f $$5 = poseStack0.last().pose();
        BlockGetter $$6 = this.minecraft.player.m_9236_();
        BlockPos $$7 = BlockPos.containing(double2, double3, double4);
        for (BlockPos $$8 : BlockPos.betweenClosed($$7.offset(-6, -6, -6), $$7.offset(6, 6, 6))) {
            BlockState $$9 = $$6.getBlockState($$8);
            if (!$$9.m_60713_(Blocks.AIR)) {
                VoxelShape $$10 = $$9.m_60808_($$6, $$8);
                for (AABB $$11 : $$10.toAabbs()) {
                    AABB $$12 = $$11.move($$8).inflate(0.002);
                    float $$13 = (float) ($$12.minX - double2);
                    float $$14 = (float) ($$12.minY - double3);
                    float $$15 = (float) ($$12.minZ - double4);
                    float $$16 = (float) ($$12.maxX - double2);
                    float $$17 = (float) ($$12.maxY - double3);
                    float $$18 = (float) ($$12.maxZ - double4);
                    float $$19 = 1.0F;
                    float $$20 = 0.0F;
                    float $$21 = 0.0F;
                    float $$22 = 0.5F;
                    if ($$9.m_60783_($$6, $$8, Direction.WEST)) {
                        VertexConsumer $$23 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$23.vertex($$5, $$13, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$23.vertex($$5, $$13, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$23.vertex($$5, $$13, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$23.vertex($$5, $$13, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    if ($$9.m_60783_($$6, $$8, Direction.SOUTH)) {
                        VertexConsumer $$24 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$24.vertex($$5, $$13, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$24.vertex($$5, $$13, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$24.vertex($$5, $$16, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$24.vertex($$5, $$16, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    if ($$9.m_60783_($$6, $$8, Direction.EAST)) {
                        VertexConsumer $$25 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$25.vertex($$5, $$16, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$25.vertex($$5, $$16, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$25.vertex($$5, $$16, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$25.vertex($$5, $$16, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    if ($$9.m_60783_($$6, $$8, Direction.NORTH)) {
                        VertexConsumer $$26 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$26.vertex($$5, $$16, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$26.vertex($$5, $$16, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$26.vertex($$5, $$13, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$26.vertex($$5, $$13, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    if ($$9.m_60783_($$6, $$8, Direction.DOWN)) {
                        VertexConsumer $$27 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$27.vertex($$5, $$13, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$27.vertex($$5, $$16, $$14, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$27.vertex($$5, $$13, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$27.vertex($$5, $$16, $$14, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                    if ($$9.m_60783_($$6, $$8, Direction.UP)) {
                        VertexConsumer $$28 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
                        $$28.vertex($$5, $$13, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$28.vertex($$5, $$13, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$28.vertex($$5, $$16, $$17, $$15).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                        $$28.vertex($$5, $$16, $$17, $$18).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                    }
                }
            }
        }
    }
}