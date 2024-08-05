package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.OptionalInt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;

public class ItemFrameRenderer<T extends ItemFrame> extends EntityRenderer<T> {

    private static final ModelResourceLocation FRAME_LOCATION = ModelResourceLocation.vanilla("item_frame", "map=false");

    private static final ModelResourceLocation MAP_FRAME_LOCATION = ModelResourceLocation.vanilla("item_frame", "map=true");

    private static final ModelResourceLocation GLOW_FRAME_LOCATION = ModelResourceLocation.vanilla("glow_item_frame", "map=false");

    private static final ModelResourceLocation GLOW_MAP_FRAME_LOCATION = ModelResourceLocation.vanilla("glow_item_frame", "map=true");

    public static final int GLOW_FRAME_BRIGHTNESS = 5;

    public static final int BRIGHT_MAP_LIGHT_ADJUSTMENT = 30;

    private final ItemRenderer itemRenderer;

    private final BlockRenderDispatcher blockRenderer;

    public ItemFrameRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.itemRenderer = entityRendererProviderContext0.getItemRenderer();
        this.blockRenderer = entityRendererProviderContext0.getBlockRenderDispatcher();
    }

    protected int getBlockLightLevel(T t0, BlockPos blockPos1) {
        return t0.m_6095_() == EntityType.GLOW_ITEM_FRAME ? Math.max(5, super.getBlockLightLevel(t0, blockPos1)) : super.getBlockLightLevel(t0, blockPos1);
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
        poseStack3.pushPose();
        Direction $$6 = t0.m_6350_();
        Vec3 $$7 = this.getRenderOffset(t0, float2);
        poseStack3.translate(-$$7.x(), -$$7.y(), -$$7.z());
        double $$8 = 0.46875;
        poseStack3.translate((double) $$6.getStepX() * 0.46875, (double) $$6.getStepY() * 0.46875, (double) $$6.getStepZ() * 0.46875);
        poseStack3.mulPose(Axis.XP.rotationDegrees(t0.m_146909_()));
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F - t0.m_146908_()));
        boolean $$9 = t0.m_20145_();
        ItemStack $$10 = t0.getItem();
        if (!$$9) {
            ModelManager $$11 = this.blockRenderer.getBlockModelShaper().getModelManager();
            ModelResourceLocation $$12 = this.getFrameModelResourceLoc(t0, $$10);
            poseStack3.pushPose();
            poseStack3.translate(-0.5F, -0.5F, -0.5F);
            this.blockRenderer.getModelRenderer().renderModel(poseStack3.last(), multiBufferSource4.getBuffer(Sheets.solidBlockSheet()), null, $$11.getModel($$12), 1.0F, 1.0F, 1.0F, int5, OverlayTexture.NO_OVERLAY);
            poseStack3.popPose();
        }
        if (!$$10.isEmpty()) {
            OptionalInt $$13 = t0.getFramedMapId();
            if ($$9) {
                poseStack3.translate(0.0F, 0.0F, 0.5F);
            } else {
                poseStack3.translate(0.0F, 0.0F, 0.4375F);
            }
            int $$14 = $$13.isPresent() ? t0.getRotation() % 4 * 2 : t0.getRotation();
            poseStack3.mulPose(Axis.ZP.rotationDegrees((float) $$14 * 360.0F / 8.0F));
            if ($$13.isPresent()) {
                poseStack3.mulPose(Axis.ZP.rotationDegrees(180.0F));
                float $$15 = 0.0078125F;
                poseStack3.scale(0.0078125F, 0.0078125F, 0.0078125F);
                poseStack3.translate(-64.0F, -64.0F, 0.0F);
                MapItemSavedData $$16 = MapItem.getSavedData($$13.getAsInt(), t0.m_9236_());
                poseStack3.translate(0.0F, 0.0F, -1.0F);
                if ($$16 != null) {
                    int $$17 = this.getLightVal(t0, 15728850, int5);
                    Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack3, multiBufferSource4, $$13.getAsInt(), $$16, true, $$17);
                }
            } else {
                int $$18 = this.getLightVal(t0, 15728880, int5);
                poseStack3.scale(0.5F, 0.5F, 0.5F);
                this.itemRenderer.renderStatic($$10, ItemDisplayContext.FIXED, $$18, OverlayTexture.NO_OVERLAY, poseStack3, multiBufferSource4, t0.m_9236_(), t0.m_19879_());
            }
        }
        poseStack3.popPose();
    }

    private int getLightVal(T t0, int int1, int int2) {
        return t0.m_6095_() == EntityType.GLOW_ITEM_FRAME ? int1 : int2;
    }

    private ModelResourceLocation getFrameModelResourceLoc(T t0, ItemStack itemStack1) {
        boolean $$2 = t0.m_6095_() == EntityType.GLOW_ITEM_FRAME;
        if (itemStack1.is(Items.FILLED_MAP)) {
            return $$2 ? GLOW_MAP_FRAME_LOCATION : MAP_FRAME_LOCATION;
        } else {
            return $$2 ? GLOW_FRAME_LOCATION : FRAME_LOCATION;
        }
    }

    public Vec3 getRenderOffset(T t0, float float1) {
        return new Vec3((double) ((float) t0.m_6350_().getStepX() * 0.3F), -0.25, (double) ((float) t0.m_6350_().getStepZ() * 0.3F));
    }

    public ResourceLocation getTextureLocation(T t0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    protected boolean shouldShowName(T t0) {
        if (Minecraft.renderNames() && !t0.getItem().isEmpty() && t0.getItem().hasCustomHoverName() && this.f_114476_.crosshairPickEntity == t0) {
            double $$1 = this.f_114476_.distanceToSqr(t0);
            float $$2 = t0.m_20163_() ? 32.0F : 64.0F;
            return $$1 < (double) ($$2 * $$2);
        } else {
            return false;
        }
    }

    protected void renderNameTag(T t0, Component component1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4) {
        super.renderNameTag(t0, t0.getItem().getHoverName(), poseStack2, multiBufferSource3, int4);
    }
}