package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.warden.Warden;

public class WardenEmissiveLayer<T extends Warden, M extends WardenModel<T>> extends RenderLayer<T, M> {

    private final ResourceLocation texture;

    private final WardenEmissiveLayer.AlphaFunction<T> alphaFunction;

    private final WardenEmissiveLayer.DrawSelector<T, M> drawSelector;

    public WardenEmissiveLayer(RenderLayerParent<T, M> renderLayerParentTM0, ResourceLocation resourceLocation1, WardenEmissiveLayer.AlphaFunction<T> wardenEmissiveLayerAlphaFunctionT2, WardenEmissiveLayer.DrawSelector<T, M> wardenEmissiveLayerDrawSelectorTM3) {
        super(renderLayerParentTM0);
        this.texture = resourceLocation1;
        this.alphaFunction = wardenEmissiveLayerAlphaFunctionT2;
        this.drawSelector = wardenEmissiveLayerDrawSelectorTM3;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (!t3.m_20145_()) {
            this.onlyDrawSelectedParts();
            VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.entityTranslucentEmissive(this.texture));
            ((WardenModel) this.m_117386_()).m_7695_(poseStack0, $$10, int2, LivingEntityRenderer.getOverlayCoords(t3, 0.0F), 1.0F, 1.0F, 1.0F, this.alphaFunction.apply(t3, float6, float7));
            this.resetDrawForAllParts();
        }
    }

    private void onlyDrawSelectedParts() {
        List<ModelPart> $$0 = this.drawSelector.getPartsToDraw((M) this.m_117386_());
        ((WardenModel) this.m_117386_()).root().getAllParts().forEach(p_234918_ -> p_234918_.skipDraw = true);
        $$0.forEach(p_234916_ -> p_234916_.skipDraw = false);
    }

    private void resetDrawForAllParts() {
        ((WardenModel) this.m_117386_()).root().getAllParts().forEach(p_234913_ -> p_234913_.skipDraw = false);
    }

    public interface AlphaFunction<T extends Warden> {

        float apply(T var1, float var2, float var3);
    }

    public interface DrawSelector<T extends Warden, M extends EntityModel<T>> {

        List<ModelPart> getPartsToDraw(M var1);
    }
}