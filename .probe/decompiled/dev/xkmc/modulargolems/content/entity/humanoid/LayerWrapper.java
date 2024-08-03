package dev.xkmc.modulargolems.content.entity.humanoid;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class LayerWrapper<T extends HumanoidGolemEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final RenderLayer<T, M> actual;

    public LayerWrapper(RenderLayerParent<T, M> pRenderer, RenderLayer<T, M> actual) {
        super(pRenderer);
        this.actual = actual;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        pLivingEntity.rendering = true;
        this.actual.render(pPoseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTick, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        pLivingEntity.rendering = false;
    }
}