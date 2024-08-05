package snownee.kiwi.contributor.impl.client.layer;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.Kiwi;
import snownee.kiwi.contributor.client.CosmeticLayer;
import snownee.kiwi.contributor.impl.client.model.SantaHatModel;

@OnlyIn(Dist.CLIENT)
public class SantaHatLayer extends CosmeticLayer {

    private static final ResourceLocation TEXTURE = Kiwi.id("textures/reward/santa.png");

    private static final Supplier<LayerDefinition> definition = Suppliers.memoize(SantaHatModel::create);

    private final SantaHatModel<AbstractClientPlayer> modelSantaHat;

    public SantaHatLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRendererIn) {
        super(entityRendererIn);
        this.modelSantaHat = new SantaHatModel<>(entityRendererIn.getModel(), (LayerDefinition) definition.get());
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.m_20145_()) {
            ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.HEAD);
            if (itemstack.isEmpty()) {
                matrixStackIn.pushPose();
                this.modelSantaHat.f_102610_ = entitylivingbaseIn.m_6162_();
                this.modelSantaHat.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entitySolid(TEXTURE), false, false);
                this.modelSantaHat.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.popPose();
            }
        }
    }
}