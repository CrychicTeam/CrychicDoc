package com.simibubi.create.content.equipment.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class BacktankArmorLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public BacktankArmorLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    public void render(PoseStack ms, MultiBufferSource buffer, int light, LivingEntity entity, float yaw, float pitch, float pt, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (entity.m_20089_() != Pose.SLEEPING) {
            BacktankItem item = BacktankItem.getWornBy(entity);
            if (item != null) {
                if (this.m_117386_() instanceof HumanoidModel<?> model) {
                    RenderType renderType = Sheets.cutoutBlockSheet();
                    BlockState renderedState = (BlockState) item.getBlock().defaultBlockState().m_61124_(BacktankBlock.HORIZONTAL_FACING, Direction.SOUTH);
                    SuperByteBuffer backtank = CachedBufferer.block(renderedState);
                    SuperByteBuffer cogs = CachedBufferer.partial(BacktankRenderer.getCogsModel(renderedState), renderedState);
                    ms.pushPose();
                    model.body.translateAndRotate(ms);
                    ms.translate(-0.5F, 0.625F, 1.0F);
                    ms.scale(1.0F, -1.0F, -1.0F);
                    backtank.forEntityRender().light(light).renderInto(ms, buffer.getBuffer(renderType));
                    ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) cogs.centre()).rotateY(180.0)).unCentre()).translate(0.0, 0.40625, 0.6875).rotate(Direction.EAST, AngleHelper.rad((double) (2.0F * AnimationTickHolder.getRenderTime(entity.m_9236_()) % 360.0F)))).translate(0.0, -0.40625, -0.6875);
                    cogs.forEntityRender().light(light).renderInto(ms, buffer.getBuffer(renderType));
                    ms.popPose();
                }
            }
        }
    }

    public static void registerOnAll(EntityRenderDispatcher renderManager) {
        for (EntityRenderer<? extends Player> renderer : renderManager.getSkinMap().values()) {
            registerOn(renderer);
        }
        for (EntityRenderer<?> renderer : renderManager.renderers.values()) {
            registerOn(renderer);
        }
    }

    public static void registerOn(EntityRenderer<?> entityRenderer) {
        if (entityRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            if (livingRenderer.getModel() instanceof HumanoidModel) {
                BacktankArmorLayer<?, ?> layer = new BacktankArmorLayer<>((RenderLayerParent<LivingEntity, EntityModel<LivingEntity>>) livingRenderer);
                livingRenderer.addLayer(layer);
            }
        }
    }
}