package dev.shadowsoffire.placebo.patreon.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.shadowsoffire.placebo.patreon.PatreonUtils;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class WingLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public WingLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> playerModelIn) {
        super(playerModelIn);
    }

    public void render(PoseStack stack, MultiBufferSource buf, int packedLightIn, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!WingsManager.DISABLED.contains(player.m_20148_())) {
            PatreonUtils.WingType type = WingsManager.getType(player.m_20148_());
            if (type != null) {
                stack.pushPose();
                stack.translate(0.0, type.yOffset, 0.0);
                ((IWingModel) type.model.get()).render(stack, buf, packedLightIn, player, partialTicks, (ResourceLocation) type.textureGetter.apply(player), (PlayerModel<AbstractClientPlayer>) this.m_117386_());
                stack.popPose();
            }
        }
    }
}