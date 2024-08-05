package org.violetmoon.quark.addons.oddities.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.entity.TotemOfHoldingEntity;
import org.violetmoon.quark.base.QuarkClient;

public class TotemOfHoldingRenderer extends EntityRenderer<TotemOfHoldingEntity> {

    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation("quark", "extra/totem_of_holding", "inventory");

    public TotemOfHoldingRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(TotemOfHoldingEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        int deathTicks = entity.getDeathTicks();
        boolean dying = entity.isDying();
        float time = (float) QuarkClient.ticker.ticksInGame + partialTicks;
        float scale = !dying ? 1.0F : Math.max(0.0F, 40.0F - ((float) deathTicks + partialTicks)) / 40.0F;
        float rotation = time + (!dying ? 0.0F : ((float) deathTicks + partialTicks) * 5.0F);
        double translation = !dying ? Math.sin((double) time * 0.03) * 0.1 : (double) (((float) deathTicks + partialTicks) / 40.0F * 5.0F);
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        ModelManager modelManager = mc.getModelManager();
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        matrixStackIn.translate(0.0, translation, 0.0);
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.translate(-0.5, 0.0, -0.5);
        dispatcher.getModelRenderer().renderModel(matrixStackIn.last(), bufferIn.getBuffer(Sheets.cutoutBlockSheet()), null, modelManager.getModel(LOCATION_MODEL), 1.0F, 1.0F, 1.0F, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected int getBlockLightLevel(@NotNull TotemOfHoldingEntity entityIn, @NotNull BlockPos position) {
        return 15;
    }

    protected boolean shouldShowName(TotemOfHoldingEntity entity) {
        if (!entity.m_8077_()) {
            return false;
        } else {
            Minecraft mc = Minecraft.getInstance();
            return !mc.options.hideGui && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) mc.hitResult).getEntity() == entity;
        }
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull TotemOfHoldingEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}