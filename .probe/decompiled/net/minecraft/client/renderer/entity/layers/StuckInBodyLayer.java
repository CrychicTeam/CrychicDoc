package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public abstract class StuckInBodyLayer<T extends LivingEntity, M extends PlayerModel<T>> extends RenderLayer<T, M> {

    public StuckInBodyLayer(LivingEntityRenderer<T, M> livingEntityRendererTM0) {
        super(livingEntityRendererTM0);
    }

    protected abstract int numStuck(T var1);

    protected abstract void renderStuckItem(PoseStack var1, MultiBufferSource var2, int var3, Entity var4, float var5, float var6, float var7, float var8);

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        int $$10 = this.numStuck(t3);
        RandomSource $$11 = RandomSource.create((long) t3.m_19879_());
        if ($$10 > 0) {
            for (int $$12 = 0; $$12 < $$10; $$12++) {
                poseStack0.pushPose();
                ModelPart $$13 = ((PlayerModel) this.m_117386_()).getRandomModelPart($$11);
                ModelPart.Cube $$14 = $$13.getRandomCube($$11);
                $$13.translateAndRotate(poseStack0);
                float $$15 = $$11.nextFloat();
                float $$16 = $$11.nextFloat();
                float $$17 = $$11.nextFloat();
                float $$18 = Mth.lerp($$15, $$14.minX, $$14.maxX) / 16.0F;
                float $$19 = Mth.lerp($$16, $$14.minY, $$14.maxY) / 16.0F;
                float $$20 = Mth.lerp($$17, $$14.minZ, $$14.maxZ) / 16.0F;
                poseStack0.translate($$18, $$19, $$20);
                $$15 = -1.0F * ($$15 * 2.0F - 1.0F);
                $$16 = -1.0F * ($$16 * 2.0F - 1.0F);
                $$17 = -1.0F * ($$17 * 2.0F - 1.0F);
                this.renderStuckItem(poseStack0, multiBufferSource1, int2, t3, $$15, $$16, $$17, float6);
                poseStack0.popPose();
            }
        }
    }
}