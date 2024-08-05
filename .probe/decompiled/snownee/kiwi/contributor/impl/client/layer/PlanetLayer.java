package snownee.kiwi.contributor.impl.client.layer;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.function.Supplier;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.Kiwi;
import snownee.kiwi.contributor.client.CosmeticLayer;
import snownee.kiwi.contributor.impl.client.model.PlanetModel;

@OnlyIn(Dist.CLIENT)
public class PlanetLayer extends CosmeticLayer {

    private static final ResourceLocation TEXTURE = Kiwi.id("textures/reward/planet.png");

    private static final Supplier<LayerDefinition> definition = Suppliers.memoize(PlanetModel::create);

    private final PlanetModel<AbstractClientPlayer> modelPlanet = new PlanetModel<>((LayerDefinition) definition.get());

    public PlanetLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.m_20145_()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, -0.6, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-ageInTicks));
            matrixStackIn.scale(1.2F, 1.2F, 1.2F);
            this.modelPlanet.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityTranslucent(TEXTURE), false, false);
            this.modelPlanet.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}