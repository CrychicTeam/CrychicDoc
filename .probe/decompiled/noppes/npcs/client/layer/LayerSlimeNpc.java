package noppes.npcs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import noppes.npcs.client.model.ModelNpcSlime;
import noppes.npcs.entity.EntityNpcSlime;

public class LayerSlimeNpc<T extends EntityNpcSlime> extends RenderLayer<T, ModelNpcSlime<T>> {

    private final LivingEntityRenderer renderer;

    private final EntityModel slimeModel = new ModelNpcSlime(0);

    public LayerSlimeNpc(LivingEntityRenderer renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!living.m_20145_()) {
            ((ModelNpcSlime) this.m_117386_()).m_102624_(this.slimeModel);
            this.slimeModel.prepareMobModel(living, limbSwing, limbSwingAmount, partialTicks);
            this.slimeModel.setupAnim(living, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(this.m_117347_(living)));
            this.slimeModel.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(living, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}