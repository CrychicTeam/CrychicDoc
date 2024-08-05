package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;

public class ArrowLayer<T extends LivingEntity, M extends PlayerModel<T>> extends StuckInBodyLayer<T, M> {

    private final EntityRenderDispatcher dispatcher;

    public ArrowLayer(EntityRendererProvider.Context entityRendererProviderContext0, LivingEntityRenderer<T, M> livingEntityRendererTM1) {
        super(livingEntityRendererTM1);
        this.dispatcher = entityRendererProviderContext0.getEntityRenderDispatcher();
    }

    @Override
    protected int numStuck(T t0) {
        return t0.getArrowCount();
    }

    @Override
    protected void renderStuckItem(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Entity entity3, float float4, float float5, float float6, float float7) {
        float $$8 = Mth.sqrt(float4 * float4 + float6 * float6);
        Arrow $$9 = new Arrow(entity3.level(), entity3.getX(), entity3.getY(), entity3.getZ());
        $$9.m_146922_((float) (Math.atan2((double) float4, (double) float6) * 180.0F / (float) Math.PI));
        $$9.m_146926_((float) (Math.atan2((double) float5, (double) $$8) * 180.0F / (float) Math.PI));
        $$9.f_19859_ = $$9.m_146908_();
        $$9.f_19860_ = $$9.m_146909_();
        this.dispatcher.render($$9, 0.0, 0.0, 0.0, 0.0F, float7, poseStack0, multiBufferSource1, int2);
    }
}