package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BeaconRenderer implements BlockEntityRenderer<BeaconBlockEntity> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public static final int MAX_RENDER_Y = 1024;

    public BeaconRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
    }

    public void render(BeaconBlockEntity beaconBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        long $$6 = beaconBlockEntity0.m_58904_().getGameTime();
        List<BeaconBlockEntity.BeaconBeamSection> $$7 = beaconBlockEntity0.getBeamSections();
        int $$8 = 0;
        for (int $$9 = 0; $$9 < $$7.size(); $$9++) {
            BeaconBlockEntity.BeaconBeamSection $$10 = (BeaconBlockEntity.BeaconBeamSection) $$7.get($$9);
            renderBeaconBeam(poseStack2, multiBufferSource3, float1, $$6, $$8, $$9 == $$7.size() - 1 ? 1024 : $$10.getHeight(), $$10.getColor());
            $$8 += $$10.getHeight();
        }
    }

    private static void renderBeaconBeam(PoseStack poseStack0, MultiBufferSource multiBufferSource1, float float2, long long3, int int4, int int5, float[] float6) {
        renderBeaconBeam(poseStack0, multiBufferSource1, BEAM_LOCATION, float2, 1.0F, long3, int4, int5, float6, 0.2F, 0.25F);
    }

    public static void renderBeaconBeam(PoseStack poseStack0, MultiBufferSource multiBufferSource1, ResourceLocation resourceLocation2, float float3, float float4, long long5, int int6, int int7, float[] float8, float float9, float float10) {
        int $$11 = int6 + int7;
        poseStack0.pushPose();
        poseStack0.translate(0.5, 0.0, 0.5);
        float $$12 = (float) Math.floorMod(long5, 40) + float3;
        float $$13 = int7 < 0 ? $$12 : -$$12;
        float $$14 = Mth.frac($$13 * 0.2F - (float) Mth.floor($$13 * 0.1F));
        float $$15 = float8[0];
        float $$16 = float8[1];
        float $$17 = float8[2];
        poseStack0.pushPose();
        poseStack0.mulPose(Axis.YP.rotationDegrees($$12 * 2.25F - 45.0F));
        float $$18 = 0.0F;
        float $$21 = 0.0F;
        float $$22 = -float9;
        float $$23 = 0.0F;
        float $$24 = 0.0F;
        float $$25 = -float9;
        float $$26 = 0.0F;
        float $$27 = 1.0F;
        float $$28 = -1.0F + $$14;
        float $$29 = (float) int7 * float4 * (0.5F / float9) + $$28;
        renderPart(poseStack0, multiBufferSource1.getBuffer(RenderType.beaconBeam(resourceLocation2, false)), $$15, $$16, $$17, 1.0F, int6, $$11, 0.0F, float9, float9, 0.0F, $$22, 0.0F, 0.0F, $$25, 0.0F, 1.0F, $$29, $$28);
        poseStack0.popPose();
        $$18 = -float10;
        float $$31 = -float10;
        $$21 = -float10;
        $$22 = -float10;
        $$26 = 0.0F;
        $$27 = 1.0F;
        $$28 = -1.0F + $$14;
        $$29 = (float) int7 * float4 + $$28;
        renderPart(poseStack0, multiBufferSource1.getBuffer(RenderType.beaconBeam(resourceLocation2, true)), $$15, $$16, $$17, 0.125F, int6, $$11, $$18, $$31, float10, $$21, $$22, float10, float10, float10, 0.0F, 1.0F, $$29, $$28);
        poseStack0.popPose();
    }

    private static void renderPart(PoseStack poseStack0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float float5, int int6, int int7, float float8, float float9, float float10, float float11, float float12, float float13, float float14, float float15, float float16, float float17, float float18, float float19) {
        PoseStack.Pose $$20 = poseStack0.last();
        Matrix4f $$21 = $$20.pose();
        Matrix3f $$22 = $$20.normal();
        renderQuad($$21, $$22, vertexConsumer1, float2, float3, float4, float5, int6, int7, float8, float9, float10, float11, float16, float17, float18, float19);
        renderQuad($$21, $$22, vertexConsumer1, float2, float3, float4, float5, int6, int7, float14, float15, float12, float13, float16, float17, float18, float19);
        renderQuad($$21, $$22, vertexConsumer1, float2, float3, float4, float5, int6, int7, float10, float11, float14, float15, float16, float17, float18, float19);
        renderQuad($$21, $$22, vertexConsumer1, float2, float3, float4, float5, int6, int7, float12, float13, float8, float9, float16, float17, float18, float19);
    }

    private static void renderQuad(Matrix4f matrixF0, Matrix3f matrixF1, VertexConsumer vertexConsumer2, float float3, float float4, float float5, float float6, int int7, int int8, float float9, float float10, float float11, float float12, float float13, float float14, float float15, float float16) {
        addVertex(matrixF0, matrixF1, vertexConsumer2, float3, float4, float5, float6, int8, float9, float10, float14, float15);
        addVertex(matrixF0, matrixF1, vertexConsumer2, float3, float4, float5, float6, int7, float9, float10, float14, float16);
        addVertex(matrixF0, matrixF1, vertexConsumer2, float3, float4, float5, float6, int7, float11, float12, float13, float16);
        addVertex(matrixF0, matrixF1, vertexConsumer2, float3, float4, float5, float6, int8, float11, float12, float13, float15);
    }

    private static void addVertex(Matrix4f matrixF0, Matrix3f matrixF1, VertexConsumer vertexConsumer2, float float3, float float4, float float5, float float6, int int7, float float8, float float9, float float10, float float11) {
        vertexConsumer2.vertex(matrixF0, float8, (float) int7, float9).color(float3, float4, float5, float6).uv(float10, float11).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrixF1, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public boolean shouldRenderOffScreen(BeaconBlockEntity beaconBlockEntity0) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(BeaconBlockEntity beaconBlockEntity0, Vec3 vec1) {
        return Vec3.atCenterOf(beaconBlockEntity0.m_58899_()).multiply(1.0, 0.0, 1.0).closerThan(vec1.multiply(1.0, 0.0, 1.0), (double) this.getViewDistance());
    }
}