package dev.xkmc.modulargolems.content.entity.common;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class ResizedLayer<T extends AbstractGolemEntity<?, ?>, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final RenderLayer<T, M> actual;

    public ResizedLayer(RenderLayerParent<T, M> pRenderer, RenderLayer<T, M> actual) {
        super(pRenderer);
        this.actual = actual;
    }

    public void render(PoseStack pose, MultiBufferSource source, int light, T entity, float f0, float f1, float f2, float f3, float f4, float f5) {
        pose.pushPose();
        float r = entity.getScale();
        pose.translate(0.0, (double) (1.0F - r) * 1.501, 0.0);
        pose.scale(r, r, r);
        this.actual.render(pose, source, light, entity, f0, f1, f2, f3, f4, f5);
        pose.popPose();
    }
}