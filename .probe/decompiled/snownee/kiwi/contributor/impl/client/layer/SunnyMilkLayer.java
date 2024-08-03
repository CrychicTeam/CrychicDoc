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
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.Kiwi;
import snownee.kiwi.contributor.client.CosmeticLayer;
import snownee.kiwi.contributor.impl.client.model.SunnyMilkModel;

@OnlyIn(Dist.CLIENT)
public class SunnyMilkLayer extends CosmeticLayer {

    private static final ResourceLocation TEXTURE = Kiwi.id("textures/reward/sunny_milk.png");

    private static final Supplier<LayerDefinition> definition = Suppliers.memoize(SunnyMilkModel::create);

    private final SunnyMilkModel<AbstractClientPlayer> model = new SunnyMilkModel<>((LayerDefinition) definition.get());

    public SunnyMilkLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.m_20145_() && !entitylivingbaseIn.m_5803_()) {
            ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.CHEST);
            if (!(itemstack.getItem() instanceof ElytraItem)) {
                matrixStackIn.pushPose();
                this.model.f_102610_ = entitylivingbaseIn.m_6162_();
                this.model.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityTranslucent(TEXTURE), false, false);
                ((PlayerModel) this.f_117344_.getModel()).f_102810_.translateAndRotate(matrixStackIn);
                this.model.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.popPose();
            }
        }
    }
}