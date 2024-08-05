package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public abstract class MobRenderer<T extends Mob, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {

    public static final int LEASH_RENDER_STEPS = 24;

    public MobRenderer(EntityRendererProvider.Context entityRendererProviderContext0, M m1, float float2) {
        super(entityRendererProviderContext0, m1, float2);
    }

    protected boolean shouldShowName(T t0) {
        return super.shouldShowName(t0) && (t0.m_6052_() || t0.m_8077_() && t0 == this.f_114476_.crosshairPickEntity);
    }

    public boolean shouldRender(T t0, Frustum frustum1, double double2, double double3, double double4) {
        if (super.m_5523_(t0, frustum1, double2, double3, double4)) {
            return true;
        } else {
            Entity $$5 = t0.getLeashHolder();
            return $$5 != null ? frustum1.isVisible($$5.getBoundingBoxForCulling()) : false;
        }
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
        Entity $$6 = t0.getLeashHolder();
        if ($$6 != null) {
            this.renderLeash(t0, float2, poseStack3, multiBufferSource4, $$6);
        }
    }

    private <E extends Entity> void renderLeash(T t0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, E e4) {
        poseStack2.pushPose();
        Vec3 $$5 = e4.getRopeHoldPosition(float1);
        double $$6 = (double) (Mth.lerp(float1, t0.f_20884_, t0.f_20883_) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
        Vec3 $$7 = t0.m_245894_(float1);
        double $$8 = Math.cos($$6) * $$7.z + Math.sin($$6) * $$7.x;
        double $$9 = Math.sin($$6) * $$7.z - Math.cos($$6) * $$7.x;
        double $$10 = Mth.lerp((double) float1, t0.f_19854_, t0.m_20185_()) + $$8;
        double $$11 = Mth.lerp((double) float1, t0.f_19855_, t0.m_20186_()) + $$7.y;
        double $$12 = Mth.lerp((double) float1, t0.f_19856_, t0.m_20189_()) + $$9;
        poseStack2.translate($$8, $$7.y, $$9);
        float $$13 = (float) ($$5.x - $$10);
        float $$14 = (float) ($$5.y - $$11);
        float $$15 = (float) ($$5.z - $$12);
        float $$16 = 0.025F;
        VertexConsumer $$17 = multiBufferSource3.getBuffer(RenderType.leash());
        Matrix4f $$18 = poseStack2.last().pose();
        float $$19 = Mth.invSqrt($$13 * $$13 + $$15 * $$15) * 0.025F / 2.0F;
        float $$20 = $$15 * $$19;
        float $$21 = $$13 * $$19;
        BlockPos $$22 = BlockPos.containing(t0.m_20299_(float1));
        BlockPos $$23 = BlockPos.containing(e4.getEyePosition(float1));
        int $$24 = this.m_6086_(t0, $$22);
        int $$25 = this.f_114476_.getRenderer(e4).getBlockLightLevel(e4, $$23);
        int $$26 = t0.m_9236_().m_45517_(LightLayer.SKY, $$22);
        int $$27 = t0.m_9236_().m_45517_(LightLayer.SKY, $$23);
        for (int $$28 = 0; $$28 <= 24; $$28++) {
            addVertexPair($$17, $$18, $$13, $$14, $$15, $$24, $$25, $$26, $$27, 0.025F, 0.025F, $$20, $$21, $$28, false);
        }
        for (int $$29 = 24; $$29 >= 0; $$29--) {
            addVertexPair($$17, $$18, $$13, $$14, $$15, $$24, $$25, $$26, $$27, 0.025F, 0.0F, $$20, $$21, $$29, true);
        }
        poseStack2.popPose();
    }

    private static void addVertexPair(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3, float float4, int int5, int int6, int int7, int int8, float float9, float float10, float float11, float float12, int int13, boolean boolean14) {
        float $$15 = (float) int13 / 24.0F;
        int $$16 = (int) Mth.lerp($$15, (float) int5, (float) int6);
        int $$17 = (int) Mth.lerp($$15, (float) int7, (float) int8);
        int $$18 = LightTexture.pack($$16, $$17);
        float $$19 = int13 % 2 == (boolean14 ? 1 : 0) ? 0.7F : 1.0F;
        float $$20 = 0.5F * $$19;
        float $$21 = 0.4F * $$19;
        float $$22 = 0.3F * $$19;
        float $$23 = float2 * $$15;
        float $$24 = float3 > 0.0F ? float3 * $$15 * $$15 : float3 - float3 * (1.0F - $$15) * (1.0F - $$15);
        float $$25 = float4 * $$15;
        vertexConsumer0.vertex(matrixF1, $$23 - float11, $$24 + float10, $$25 + float12).color($$20, $$21, $$22, 1.0F).uv2($$18).endVertex();
        vertexConsumer0.vertex(matrixF1, $$23 + float11, $$24 + float9 - float10, $$25 - float12).color($$20, $$21, $$22, 1.0F).uv2($$18).endVertex();
    }
}