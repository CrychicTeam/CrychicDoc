package snownee.kiwi.contributor.impl.client.layer;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Locale;
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
import snownee.kiwi.contributor.client.CosmeticLayer;
import snownee.kiwi.contributor.impl.client.model.FoxTailModel;

@OnlyIn(Dist.CLIENT)
public class FoxTailLayer extends CosmeticLayer {

    private static final ResourceLocation FOX = new ResourceLocation("textures/entity/fox/fox.png");

    private static final ResourceLocation SNOW_FOX = new ResourceLocation("textures/entity/fox/snow_fox.png");

    private static final Supplier<LayerDefinition> definition = Suppliers.memoize(FoxTailModel::create);

    private final FoxTailModel<AbstractClientPlayer> modelFoxTail;

    public FoxTailLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRendererIn) {
        super(entityRendererIn);
        this.modelFoxTail = new FoxTailModel<>(entityRendererIn.getModel(), (LayerDefinition) definition.get());
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.m_20145_() && !entitylivingbaseIn.m_5803_()) {
            ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.CHEST);
            if (!(itemstack.getItem() instanceof ElytraItem)) {
                String name = entitylivingbaseIn.m_7755_().getString().toLowerCase(Locale.ENGLISH);
                ResourceLocation texture = !name.contains("snow") && !name.contains("xue") && !name.contains("yuki") ? FOX : SNOW_FOX;
                matrixStackIn.pushPose();
                this.modelFoxTail.f_102610_ = entitylivingbaseIn.m_6162_();
                this.modelFoxTail.setupAnim(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entitySolid(texture), false, false);
                this.modelFoxTail.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.popPose();
            }
        }
    }
}