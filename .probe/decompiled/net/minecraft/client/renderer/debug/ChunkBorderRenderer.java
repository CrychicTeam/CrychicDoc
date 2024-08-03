package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.joml.Matrix4f;

public class ChunkBorderRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private static final int CELL_BORDER = FastColor.ARGB32.color(255, 0, 155, 155);

    private static final int YELLOW = FastColor.ARGB32.color(255, 255, 255, 0);

    public ChunkBorderRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Entity $$5 = this.minecraft.gameRenderer.getMainCamera().getEntity();
        float $$6 = (float) ((double) this.minecraft.level.m_141937_() - double3);
        float $$7 = (float) ((double) this.minecraft.level.m_151558_() - double3);
        ChunkPos $$8 = $$5.chunkPosition();
        float $$9 = (float) ((double) $$8.getMinBlockX() - double2);
        float $$10 = (float) ((double) $$8.getMinBlockZ() - double4);
        VertexConsumer $$11 = multiBufferSource1.getBuffer(RenderType.debugLineStrip(1.0));
        Matrix4f $$12 = poseStack0.last().pose();
        for (int $$13 = -16; $$13 <= 32; $$13 += 16) {
            for (int $$14 = -16; $$14 <= 32; $$14 += 16) {
                $$11.vertex($$12, $$9 + (float) $$13, $$6, $$10 + (float) $$14).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$13, $$6, $$10 + (float) $$14).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$13, $$7, $$10 + (float) $$14).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$13, $$7, $$10 + (float) $$14).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
            }
        }
        for (int $$15 = 2; $$15 < 16; $$15 += 2) {
            int $$16 = $$15 % 4 == 0 ? CELL_BORDER : YELLOW;
            $$11.vertex($$12, $$9 + (float) $$15, $$6, $$10).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$6, $$10).color($$16).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$7, $$10).color($$16).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$7, $$10).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$6, $$10 + 16.0F).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$6, $$10 + 16.0F).color($$16).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$7, $$10 + 16.0F).color($$16).endVertex();
            $$11.vertex($$12, $$9 + (float) $$15, $$7, $$10 + 16.0F).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }
        for (int $$17 = 2; $$17 < 16; $$17 += 2) {
            int $$18 = $$17 % 4 == 0 ? CELL_BORDER : YELLOW;
            $$11.vertex($$12, $$9, $$6, $$10 + (float) $$17).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9, $$6, $$10 + (float) $$17).color($$18).endVertex();
            $$11.vertex($$12, $$9, $$7, $$10 + (float) $$17).color($$18).endVertex();
            $$11.vertex($$12, $$9, $$7, $$10 + (float) $$17).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$6, $$10 + (float) $$17).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$6, $$10 + (float) $$17).color($$18).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$7, $$10 + (float) $$17).color($$18).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$7, $$10 + (float) $$17).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }
        for (int $$19 = this.minecraft.level.m_141937_(); $$19 <= this.minecraft.level.m_151558_(); $$19 += 2) {
            float $$20 = (float) ((double) $$19 - double3);
            int $$21 = $$19 % 8 == 0 ? CELL_BORDER : YELLOW;
            $$11.vertex($$12, $$9, $$20, $$10).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9, $$20, $$10).color($$21).endVertex();
            $$11.vertex($$12, $$9, $$20, $$10 + 16.0F).color($$21).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$20, $$10 + 16.0F).color($$21).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$20, $$10).color($$21).endVertex();
            $$11.vertex($$12, $$9, $$20, $$10).color($$21).endVertex();
            $$11.vertex($$12, $$9, $$20, $$10).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
        }
        $$11 = multiBufferSource1.getBuffer(RenderType.debugLineStrip(2.0));
        for (int $$22 = 0; $$22 <= 16; $$22 += 16) {
            for (int $$23 = 0; $$23 <= 16; $$23 += 16) {
                $$11.vertex($$12, $$9 + (float) $$22, $$6, $$10 + (float) $$23).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$22, $$6, $$10 + (float) $$23).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$22, $$7, $$10 + (float) $$23).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
                $$11.vertex($$12, $$9 + (float) $$22, $$7, $$10 + (float) $$23).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            }
        }
        for (int $$24 = this.minecraft.level.m_141937_(); $$24 <= this.minecraft.level.m_151558_(); $$24 += 16) {
            float $$25 = (float) ((double) $$24 - double3);
            $$11.vertex($$12, $$9, $$25, $$10).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            $$11.vertex($$12, $$9, $$25, $$10).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            $$11.vertex($$12, $$9, $$25, $$10 + 16.0F).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$25, $$10 + 16.0F).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            $$11.vertex($$12, $$9 + 16.0F, $$25, $$10).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            $$11.vertex($$12, $$9, $$25, $$10).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            $$11.vertex($$12, $$9, $$25, $$10).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
        }
    }
}