package dev.xkmc.l2weaponry.content.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.init.data.TagGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ThrownWeaponRenderer<T extends BaseThrownWeaponEntity<T>> extends EntityRenderer<T> {

    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/trident.png");

    public ThrownWeaponRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(T entity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        if (!entity.getItem().is(TagGen.JAVELIN)) {
            pMatrixStack.translate(0.0, 0.25, 0.0);
        }
        pMatrixStack.scale(1.47F, 1.47F, 1.47F);
        pMatrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, entity.f_19859_, entity.m_146908_()) - 90.0F));
        pMatrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, entity.f_19860_, entity.m_146909_()) - 90.0F));
        if (entity.getItem().is(TagGen.JAVELIN)) {
            pMatrixStack.translate(0.0, -0.4, 0.0);
        } else {
            pMatrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        pMatrixStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
        ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
        BakedModel model = ir.getModel(entity.getItem(), entity.m_9236_(), null, entity.m_19879_());
        ir.render(entity.getItem(), ItemDisplayContext.GROUND, false, pMatrixStack, pBuffer, pPackedLight, OverlayTexture.NO_OVERLAY, model);
        pMatrixStack.popPose();
        super.render(entity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return TRIDENT_LOCATION;
    }
}