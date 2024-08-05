package io.redspace.ironsspellbooks.entity.spells.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;

public class ShieldTrimLayer extends RenderLayer<ShieldEntity, ShieldModel> {

    private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/shield/shield_trim.png");

    private final ShieldTrimModel model;

    public ShieldTrimLayer(RenderLayerParent<ShieldEntity, ShieldModel> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.model = new ShieldTrimModel(context.bakeLayer(ShieldTrimModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShieldEntity entity, float pLimbSwing, float pLimbSwingAmount, float partialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Vec2 offset = ShieldRenderer.getEnergySwirlOffset(entity, partialTicks, 3456);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.energySwirl(TEXTURE, 0.0F, 0.0F));
        this.model.renderToBuffer(poseStack, consumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.45F);
    }
}