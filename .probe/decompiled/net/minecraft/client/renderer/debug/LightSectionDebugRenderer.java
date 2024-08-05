package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.time.Duration;
import java.time.Instant;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class LightSectionDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final Duration REFRESH_INTERVAL = Duration.ofMillis(500L);

    private static final int RADIUS = 10;

    private static final Vector4f LIGHT_AND_BLOCKS_COLOR = new Vector4f(1.0F, 1.0F, 0.0F, 0.25F);

    private static final Vector4f LIGHT_ONLY_COLOR = new Vector4f(0.25F, 0.125F, 0.0F, 0.125F);

    private final Minecraft minecraft;

    private final LightLayer lightLayer;

    private Instant lastUpdateTime = Instant.now();

    @Nullable
    private LightSectionDebugRenderer.SectionData data;

    public LightSectionDebugRenderer(Minecraft minecraft0, LightLayer lightLayer1) {
        this.minecraft = minecraft0;
        this.lightLayer = lightLayer1;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Instant $$5 = Instant.now();
        if (this.data == null || Duration.between(this.lastUpdateTime, $$5).compareTo(REFRESH_INTERVAL) > 0) {
            this.lastUpdateTime = $$5;
            this.data = new LightSectionDebugRenderer.SectionData(this.minecraft.level.m_5518_(), SectionPos.of(this.minecraft.player.m_20183_()), 10, this.lightLayer);
        }
        renderEdges(poseStack0, this.data.lightAndBlocksShape, this.data.minPos, multiBufferSource1, double2, double3, double4, LIGHT_AND_BLOCKS_COLOR);
        renderEdges(poseStack0, this.data.lightShape, this.data.minPos, multiBufferSource1, double2, double3, double4, LIGHT_ONLY_COLOR);
        VertexConsumer $$6 = multiBufferSource1.getBuffer(RenderType.debugSectionQuads());
        renderFaces(poseStack0, this.data.lightAndBlocksShape, this.data.minPos, $$6, double2, double3, double4, LIGHT_AND_BLOCKS_COLOR);
        renderFaces(poseStack0, this.data.lightShape, this.data.minPos, $$6, double2, double3, double4, LIGHT_ONLY_COLOR);
    }

    private static void renderFaces(PoseStack poseStack0, DiscreteVoxelShape discreteVoxelShape1, SectionPos sectionPos2, VertexConsumer vertexConsumer3, double double4, double double5, double double6, Vector4f vectorF7) {
        discreteVoxelShape1.forAllFaces((p_282087_, p_283360_, p_282854_, p_282233_) -> {
            int $$11 = p_283360_ + sectionPos2.m_123341_();
            int $$12 = p_282854_ + sectionPos2.m_123342_();
            int $$13 = p_282233_ + sectionPos2.m_123343_();
            renderFace(poseStack0, vertexConsumer3, p_282087_, double4, double5, double6, $$11, $$12, $$13, vectorF7);
        });
    }

    private static void renderEdges(PoseStack poseStack0, DiscreteVoxelShape discreteVoxelShape1, SectionPos sectionPos2, MultiBufferSource multiBufferSource3, double double4, double double5, double double6, Vector4f vectorF7) {
        discreteVoxelShape1.forAllEdges((p_283441_, p_283631_, p_282083_, p_281900_, p_281481_, p_283547_) -> {
            int $$13 = p_283441_ + sectionPos2.m_123341_();
            int $$14 = p_283631_ + sectionPos2.m_123342_();
            int $$15 = p_282083_ + sectionPos2.m_123343_();
            int $$16 = p_281900_ + sectionPos2.m_123341_();
            int $$17 = p_281481_ + sectionPos2.m_123342_();
            int $$18 = p_283547_ + sectionPos2.m_123343_();
            VertexConsumer $$19 = multiBufferSource3.getBuffer(RenderType.debugLineStrip(1.0));
            renderEdge(poseStack0, $$19, double4, double5, double6, $$13, $$14, $$15, $$16, $$17, $$18, vectorF7);
        }, true);
    }

    private static void renderFace(PoseStack poseStack0, VertexConsumer vertexConsumer1, Direction direction2, double double3, double double4, double double5, int int6, int int7, int int8, Vector4f vectorF9) {
        float $$10 = (float) ((double) SectionPos.sectionToBlockCoord(int6) - double3);
        float $$11 = (float) ((double) SectionPos.sectionToBlockCoord(int7) - double4);
        float $$12 = (float) ((double) SectionPos.sectionToBlockCoord(int8) - double5);
        float $$13 = $$10 + 16.0F;
        float $$14 = $$11 + 16.0F;
        float $$15 = $$12 + 16.0F;
        float $$16 = vectorF9.x();
        float $$17 = vectorF9.y();
        float $$18 = vectorF9.z();
        float $$19 = vectorF9.w();
        Matrix4f $$20 = poseStack0.last().pose();
        switch(direction2) {
            case DOWN:
                vertexConsumer1.vertex($$20, $$10, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
                break;
            case UP:
                vertexConsumer1.vertex($$20, $$10, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                break;
            case NORTH:
                vertexConsumer1.vertex($$20, $$10, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                break;
            case SOUTH:
                vertexConsumer1.vertex($$20, $$10, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                break;
            case WEST:
                vertexConsumer1.vertex($$20, $$10, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$10, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                break;
            case EAST:
                vertexConsumer1.vertex($$20, $$13, $$11, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$12).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$14, $$15).color($$16, $$17, $$18, $$19).endVertex();
                vertexConsumer1.vertex($$20, $$13, $$11, $$15).color($$16, $$17, $$18, $$19).endVertex();
        }
    }

    private static void renderEdge(PoseStack poseStack0, VertexConsumer vertexConsumer1, double double2, double double3, double double4, int int5, int int6, int int7, int int8, int int9, int int10, Vector4f vectorF11) {
        float $$12 = (float) ((double) SectionPos.sectionToBlockCoord(int5) - double2);
        float $$13 = (float) ((double) SectionPos.sectionToBlockCoord(int6) - double3);
        float $$14 = (float) ((double) SectionPos.sectionToBlockCoord(int7) - double4);
        float $$15 = (float) ((double) SectionPos.sectionToBlockCoord(int8) - double2);
        float $$16 = (float) ((double) SectionPos.sectionToBlockCoord(int9) - double3);
        float $$17 = (float) ((double) SectionPos.sectionToBlockCoord(int10) - double4);
        Matrix4f $$18 = poseStack0.last().pose();
        vertexConsumer1.vertex($$18, $$12, $$13, $$14).color(vectorF11.x(), vectorF11.y(), vectorF11.z(), 1.0F).endVertex();
        vertexConsumer1.vertex($$18, $$15, $$16, $$17).color(vectorF11.x(), vectorF11.y(), vectorF11.z(), 1.0F).endVertex();
    }

    static final class SectionData {

        final DiscreteVoxelShape lightAndBlocksShape;

        final DiscreteVoxelShape lightShape;

        final SectionPos minPos;

        SectionData(LevelLightEngine levelLightEngine0, SectionPos sectionPos1, int int2, LightLayer lightLayer3) {
            int $$4 = int2 * 2 + 1;
            this.lightAndBlocksShape = new BitSetDiscreteVoxelShape($$4, $$4, $$4);
            this.lightShape = new BitSetDiscreteVoxelShape($$4, $$4, $$4);
            for (int $$5 = 0; $$5 < $$4; $$5++) {
                for (int $$6 = 0; $$6 < $$4; $$6++) {
                    for (int $$7 = 0; $$7 < $$4; $$7++) {
                        SectionPos $$8 = SectionPos.of(sectionPos1.x() + $$7 - int2, sectionPos1.y() + $$6 - int2, sectionPos1.z() + $$5 - int2);
                        LayerLightSectionStorage.SectionType $$9 = levelLightEngine0.getDebugSectionType(lightLayer3, $$8);
                        if ($$9 == LayerLightSectionStorage.SectionType.LIGHT_AND_DATA) {
                            this.lightAndBlocksShape.fill($$7, $$6, $$5);
                            this.lightShape.fill($$7, $$6, $$5);
                        } else if ($$9 == LayerLightSectionStorage.SectionType.LIGHT_ONLY) {
                            this.lightShape.fill($$7, $$6, $$5);
                        }
                    }
                }
            }
            this.minPos = SectionPos.of(sectionPos1.x() - int2, sectionPos1.y() - int2, sectionPos1.z() - int2);
        }
    }
}