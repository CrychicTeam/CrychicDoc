package net.mehvahdjukaar.supplementaries.client.renderers.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import net.mehvahdjukaar.supplementaries.common.entities.RedMerchantEntity;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.resources.ResourceLocation;

public class RedMerchantRenderer extends MobRenderer<RedMerchantEntity, VillagerModel<RedMerchantEntity>> {

    public RedMerchantRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ClientRegistry.RED_MERCHANT_MODEL)), 0.5F);
        this.m_115326_(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.m_115326_(new CrossedArmsItemLayer<>(this, context.getItemInHandRenderer()));
        ((VillagerModel) this.m_7200_()).getHead().y = ((VillagerModel) this.m_7200_()).getHead().y + 1.0F - 0.9375F;
    }

    public ResourceLocation getTextureLocation(RedMerchantEntity entity) {
        return entity.m_5446_().getString().toLowerCase(Locale.ROOT).equals("morshu") ? ModTextures.ORANGE_MERCHANT : (MiscUtils.FESTIVITY.isChristmas() ? ModTextures.RED_MERCHANT_CHRISTMAS : ModTextures.RED_MERCHANT);
    }

    protected void scale(RedMerchantEntity entity, PoseStack matrixStack, float ticks) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public static LayerDefinition createMesh() {
        MeshDefinition meshdefinition = VillagerModel.createBodyModel();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F), PartPose.ZERO);
        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        hat.addOrReplaceChild("hat_rim", CubeListBuilder.create().texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F), PartPose.rotation((float) (-Math.PI / 2), 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}