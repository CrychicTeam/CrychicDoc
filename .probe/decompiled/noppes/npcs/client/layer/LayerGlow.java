package noppes.npcs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

@OnlyIn(Dist.CLIENT)
public class LayerGlow<T extends EntityNPCInterface, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public LayerGlow(RenderCustomNpc npcRenderer) {
        super(npcRenderer);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource typeBuffer, int packedLightIn, T npc, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!npc.display.getOverlayTexture().isEmpty()) {
            if (npc.textureGlowLocation == null) {
                npc.textureGlowLocation = new ResourceLocation(npc.display.getOverlayTexture());
            }
            VertexConsumer ivertexbuilder = typeBuffer.getBuffer(RenderType.entityCutoutNoCull(npc.textureGlowLocation));
            this.m_117386_().m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(npc, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}