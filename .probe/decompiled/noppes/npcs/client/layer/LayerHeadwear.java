package noppes.npcs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.model.ModelHeadwear;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.client.model.Model2DRenderer;

public class LayerHeadwear extends LayerInterface implements LayerPreRender {

    private final ModelHeadwear headwear = new ModelHeadwear();

    public LayerHeadwear(LivingEntityRenderer render) {
        super(render);
    }

    @Override
    public void render(PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
        if (CustomNpcs.HeadWearType == 1 && this.npc.textureLocation != null) {
            float red = 1.0F;
            float blue = 1.0F;
            float green = 1.0F;
            if (this.npc.f_20916_ <= 0 && this.npc.f_20919_ <= 0) {
                int color = this.npc.display.getTint();
                red = (float) (color >> 16 & 0xFF) / 255.0F;
                green = (float) (color >> 8 & 0xFF) / 255.0F;
                blue = (float) (color & 0xFF) / 255.0F;
            }
            this.base.head.translateAndRotate(mStack);
            Model2DRenderer.textureOverride = this.npc.textureLocation;
            VertexConsumer ivertex = typeBuffer.getBuffer(RenderType.entityTranslucent(this.npc.textureLocation));
            this.headwear.render(mStack, ivertex, lightmapUV, OverlayTexture.NO_OVERLAY);
            Model2DRenderer.textureOverride = null;
        }
    }

    @Override
    public void rotate(PoseStack matrixStack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void preRender(EntityCustomNpc npc) {
        this.base.hat.visible = this.base.head.visible && CustomNpcs.HeadWearType != 1;
        if (!this.base.hat.visible) {
            this.headwear.config = null;
        }
    }
}