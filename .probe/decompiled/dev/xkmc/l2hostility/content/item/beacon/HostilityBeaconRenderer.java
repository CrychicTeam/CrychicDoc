package dev.xkmc.l2hostility.content.item.beacon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class HostilityBeaconRenderer implements BlockEntityRenderer<HostilityBeaconBlockEntity> {

    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");

    public static final int MAX_RENDER_Y = 1024;

    public HostilityBeaconRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    public void render(HostilityBeaconBlockEntity be, float pTick, PoseStack pose, MultiBufferSource source, int light, int overlay) {
        long time = be.m_58904_().getGameTime();
        List<HostilityBeaconBlockEntity.Section> list = be.getBeamSections();
        int y = 0;
        for (int k = 0; k < list.size(); k++) {
            HostilityBeaconBlockEntity.Section sec = (HostilityBeaconBlockEntity.Section) list.get(k);
            renderBeaconBeam(pose, source, pTick, time, y, k == list.size() - 1 ? 1024 : sec.getHeight(), sec.getColor());
            y += sec.getHeight();
        }
    }

    private static void renderBeaconBeam(PoseStack pose, MultiBufferSource source, float pTick, long time, int y0, int y1, float[] color) {
        renderBeaconBeam(pose, source, BEAM_LOCATION, pTick, 1.0F, time, y0, y1, color, 0.2F, 0.25F);
    }

    public static void renderBeaconBeam(PoseStack pose, MultiBufferSource source, ResourceLocation id, float pTick, float float0, long time, int y0, int y1, float[] color, float float1, float float2) {
        int i = y0 + y1;
        pose.pushPose();
        pose.translate(0.5, 0.0, 0.5);
        float f = (float) Math.floorMod(time, 40) + pTick;
        float f1 = y1 < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
        float f3 = color[0];
        float f4 = color[1];
        float f5 = color[2];
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        float f9 = -float1;
        float f12 = -float1;
        float f15 = -1.0F + f2;
        float f16 = (float) y1 * float0 * (0.5F / float1) + f15;
        renderPart(pose, source.getBuffer(RenderType.beaconBeam(id, false)), f3, f4, f5, 1.0F, y0, i, 0.0F, float1, float1, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        pose.popPose();
        float f6 = -float2;
        float f7 = -float2;
        float f8 = -float2;
        f9 = -float2;
        f15 = -1.0F + f2;
        f16 = (float) y1 * float0 + f15;
        renderPart(pose, source.getBuffer(RenderType.beaconBeam(id, true)), f3, f4, f5, 0.125F, y0, i, f6, f7, float2, f8, f9, float2, float2, float2, 0.0F, 1.0F, f16, f15);
        pose.popPose();
    }

    private static void renderPart(PoseStack poseStack0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float float5, int int6, int int7, float float8, float float9, float float10, float float11, float float12, float float13, float float14, float float15, float float16, float float17, float float18, float float19) {
        PoseStack.Pose posestack$pose = poseStack0.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        renderQuad(matrix4f, matrix3f, vertexConsumer1, float2, float3, float4, float5, int6, int7, float8, float9, float10, float11, float16, float17, float18, float19);
        renderQuad(matrix4f, matrix3f, vertexConsumer1, float2, float3, float4, float5, int6, int7, float14, float15, float12, float13, float16, float17, float18, float19);
        renderQuad(matrix4f, matrix3f, vertexConsumer1, float2, float3, float4, float5, int6, int7, float10, float11, float14, float15, float16, float17, float18, float19);
        renderQuad(matrix4f, matrix3f, vertexConsumer1, float2, float3, float4, float5, int6, int7, float12, float13, float8, float9, float16, float17, float18, float19);
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

    public boolean shouldRenderOffScreen(HostilityBeaconBlockEntity hostilityBeaconBlockEntity0) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRender(HostilityBeaconBlockEntity hostilityBeaconBlockEntity0, Vec3 vec1) {
        return Vec3.atCenterOf(hostilityBeaconBlockEntity0.m_58899_()).multiply(1.0, 0.0, 1.0).closerThan(vec1.multiply(1.0, 0.0, 1.0), (double) this.getViewDistance());
    }
}