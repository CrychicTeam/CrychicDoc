package io.github.lightman314.lightmanscurrency.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.lightman314.lightmanscurrency.client.ModLayerDefinitions;
import io.github.lightman314.lightmanscurrency.client.model.ModelWallet;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.IWalletHandler;
import io.github.lightman314.lightmanscurrency.common.capability.wallet.WalletCapability;
import io.github.lightman314.lightmanscurrency.common.items.WalletItem;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WalletLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private final ModelWallet<T> model = new ModelWallet<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModLayerDefinitions.WALLET));

    public WalletLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int light, @Nonnull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        IWalletHandler handler = WalletCapability.getRenderWalletHandler(entity);
        if (handler != null && handler.visible()) {
            ItemStack wallet = handler.getWallet();
            if (wallet.getItem() instanceof WalletItem walletItem) {
                this.model.m_6839_(entity, limbSwing, limbSwingAmount, partialTicks);
                this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                this.m_117386_().copyPropertiesTo(this.model);
                VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource, this.model.m_103119_(walletItem.getModelTexture()), false, wallet.hasFoil());
                this.model.m_7695_(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public static LayerDefinition createLayer() {
        CubeDeformation cube = CubeDeformation.NONE;
        MeshDefinition mesh = HumanoidModel.createMesh(cube, 0.0F);
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild("wallet", CubeListBuilder.create().texOffs(0, 0).addBox(4.0F, 11.5F, -2.0F, 2.0F, 4.0F, 4.0F, cube), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 16);
    }
}