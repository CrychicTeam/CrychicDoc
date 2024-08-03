package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelKangaroo;
import com.github.alexthe666.alexsmobs.client.render.RenderKangaroo;
import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Quaternionf;

public class LayerKangarooArmor extends RenderLayer<EntityKangaroo, ModelKangaroo> {

    private static final Map<String, ResourceLocation> ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();

    private final HumanoidModel defaultBipedModel;

    private final RenderKangaroo renderer;

    public LayerKangarooArmor(RenderKangaroo render, EntityRendererProvider.Context context) {
        super(render);
        this.defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.renderer = render;
    }

    public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(58);
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, 1, type == null ? "" : String.format("_%s", type));
        s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation) ARMOR_TEXTURE_RES_MAP.get(s1);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s1);
            ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
        }
        return resourcelocation;
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityKangaroo roo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        if (roo.isRoger()) {
            ItemStack haloStack = new ItemStack(AMItemRegistry.HALO.get());
            matrixStackIn.pushPose();
            this.translateToHead(matrixStackIn);
            float f = 0.1F * (float) Math.sin((double) (((float) roo.f_19797_ + partialTicks) * 0.1F)) + (roo.m_6162_() ? 0.2F : 0.0F);
            matrixStackIn.translate(0.0F, -0.75F - f, -0.2F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(1.3F, 1.3F, 1.3F);
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            renderer.renderItem(roo, haloStack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
        if (!roo.m_6162_()) {
            matrixStackIn.pushPose();
            ItemStack itemstack = roo.getItemBySlot(EquipmentSlot.HEAD);
            if (itemstack.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) itemstack.getItem();
                if (itemstack.canEquip(EquipmentSlot.HEAD, roo)) {
                    HumanoidModel a = this.defaultBipedModel;
                    a = this.getArmorModelHook(roo, itemstack, EquipmentSlot.HEAD, a);
                    boolean notAVanillaModel = a != this.defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.HEAD);
                    this.translateToHead(matrixStackIn);
                    matrixStackIn.translate(0.0F, 0.015F, -0.05F);
                    if (itemstack.getItem() == AMItemRegistry.FEDORA.get()) {
                        matrixStackIn.translate(0.0F, 0.05F, 0.0F);
                    }
                    matrixStackIn.scale(0.7F, 0.7F, 0.7F);
                    boolean flag1 = itemstack.hasFoil();
                    if (armoritem instanceof DyeableLeatherItem) {
                        int i = ((DyeableLeatherItem) armoritem).getColor(itemstack);
                        float f = (float) (i >> 16 & 0xFF) / 255.0F;
                        float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
                        float f2 = (float) (i & 0xFF) / 255.0F;
                        this.renderHelmet(roo, matrixStackIn, bufferIn, packedLightIn, flag1, a, f, f1, f2, getArmorResource(roo, itemstack, EquipmentSlot.HEAD, null), notAVanillaModel);
                        this.renderHelmet(roo, matrixStackIn, bufferIn, packedLightIn, flag1, a, 1.0F, 1.0F, 1.0F, getArmorResource(roo, itemstack, EquipmentSlot.HEAD, "overlay"), notAVanillaModel);
                    } else {
                        this.renderHelmet(roo, matrixStackIn, bufferIn, packedLightIn, flag1, a, 1.0F, 1.0F, 1.0F, getArmorResource(roo, itemstack, EquipmentSlot.HEAD, null), notAVanillaModel);
                    }
                }
            } else {
                this.translateToHead(matrixStackIn);
                matrixStackIn.translate(0.0, -0.2, -0.1F);
                matrixStackIn.mulPose(new Quaternionf().rotateX((float) Math.PI));
                matrixStackIn.mulPose(new Quaternionf().rotateY((float) Math.PI));
                matrixStackIn.scale(1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, roo.m_9236_(), 0);
            }
            matrixStackIn.popPose();
            matrixStackIn.pushPose();
            itemstack = roo.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) itemstack.getItem();
                if (armoritem.getEquipmentSlot() == EquipmentSlot.CHEST) {
                    HumanoidModel ax = this.defaultBipedModel;
                    ax = this.getArmorModelHook(roo, itemstack, EquipmentSlot.CHEST, ax);
                    boolean notAVanillaModelx = ax != this.defaultBipedModel;
                    this.setModelSlotVisible(ax, EquipmentSlot.CHEST);
                    this.translateToChest(matrixStackIn);
                    matrixStackIn.translate(0.0F, 0.25F, 0.0F);
                    matrixStackIn.scale(1.0F, 1.0F, 1.0F);
                    boolean flag1 = itemstack.hasFoil();
                    if (armoritem instanceof DyeableLeatherItem) {
                        int i = ((DyeableLeatherItem) armoritem).getColor(itemstack);
                        float f = (float) (i >> 16 & 0xFF) / 255.0F;
                        float f1 = (float) (i >> 8 & 0xFF) / 255.0F;
                        float f2 = (float) (i & 0xFF) / 255.0F;
                        this.renderChestplate(roo, matrixStackIn, bufferIn, packedLightIn, flag1, ax, f, f1, f2, getArmorResource(roo, itemstack, EquipmentSlot.CHEST, null), notAVanillaModelx);
                        this.renderChestplate(roo, matrixStackIn, bufferIn, packedLightIn, flag1, ax, 1.0F, 1.0F, 1.0F, getArmorResource(roo, itemstack, EquipmentSlot.CHEST, "overlay"), notAVanillaModelx);
                    } else {
                        this.renderChestplate(roo, matrixStackIn, bufferIn, packedLightIn, flag1, ax, 1.0F, 1.0F, 1.0F, getArmorResource(roo, itemstack, EquipmentSlot.CHEST, null), notAVanillaModelx);
                    }
                }
            }
            matrixStackIn.popPose();
        }
        matrixStackIn.popPose();
    }

    private void translateToHead(PoseStack matrixStackIn) {
        this.translateToChest(matrixStackIn);
        ((ModelKangaroo) this.renderer.m_7200_()).neck.translateAndRotate(matrixStackIn);
        ((ModelKangaroo) this.renderer.m_7200_()).head.translateAndRotate(matrixStackIn);
    }

    private void translateToChest(PoseStack matrixStackIn) {
        ((ModelKangaroo) this.renderer.m_7200_()).root.translateAndRotate(matrixStackIn);
        ((ModelKangaroo) this.renderer.m_7200_()).body.translateAndRotate(matrixStackIn);
        ((ModelKangaroo) this.renderer.m_7200_()).chest.translateAndRotate(matrixStackIn);
    }

    private void renderChestplate(EntityKangaroo entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, float red, float green, float blue, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        ((ModelKangaroo) this.renderer.m_7200_()).m_102624_(modelIn);
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * Minecraft.getInstance().getFrameTime();
        modelIn.body.xRot = (float) (Math.PI / 2);
        modelIn.body.yRot = 0.0F;
        modelIn.body.zRot = 0.0F;
        modelIn.body.x = 0.0F;
        modelIn.body.y = 0.25F;
        modelIn.body.z = -7.6F;
        modelIn.rightArm.x = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotationPointX;
        modelIn.rightArm.y = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotationPointY;
        modelIn.rightArm.z = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotationPointZ;
        modelIn.rightArm.xRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotateAngleX;
        modelIn.rightArm.yRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotateAngleY;
        modelIn.rightArm.zRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotateAngleZ;
        modelIn.leftArm.x = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotationPointX;
        modelIn.leftArm.y = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotationPointY;
        modelIn.leftArm.z = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotationPointZ;
        modelIn.leftArm.xRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotateAngleX;
        modelIn.leftArm.yRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotateAngleY;
        modelIn.leftArm.zRot = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotateAngleZ;
        modelIn.leftArm.y = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotationPointY - 4.0F + sitProgress * 0.25F;
        modelIn.rightArm.y = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotationPointY - 4.0F + sitProgress * 0.25F;
        modelIn.leftArm.z = ((ModelKangaroo) this.renderer.m_7200_()).arm_left.rotationPointZ - 0.5F;
        modelIn.rightArm.z = ((ModelKangaroo) this.renderer.m_7200_()).arm_right.rotationPointZ - 0.5F;
        modelIn.body.visible = false;
        modelIn.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        modelIn.body.visible = true;
        modelIn.rightArm.visible = false;
        modelIn.leftArm.visible = false;
        matrixStackIn.pushPose();
        matrixStackIn.scale(1.1F, 1.65F, 1.1F);
        modelIn.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        matrixStackIn.popPose();
        modelIn.rightArm.visible = true;
        modelIn.leftArm.visible = true;
    }

    private void renderHelmet(EntityKangaroo entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, float red, float green, float blue, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        ((ModelKangaroo) this.renderer.m_7200_()).m_102624_(modelIn);
        modelIn.head.xRot = 0.0F;
        modelIn.head.yRot = 0.0F;
        modelIn.head.zRot = 0.0F;
        modelIn.hat.xRot = 0.0F;
        modelIn.hat.yRot = 0.0F;
        modelIn.hat.zRot = 0.0F;
        modelIn.head.x = 0.0F;
        modelIn.head.y = 0.0F;
        modelIn.head.z = 0.0F;
        modelIn.hat.x = 0.0F;
        modelIn.hat.y = 0.0F;
        modelIn.hat.z = 0.0F;
        modelIn.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    protected void setModelSlotVisible(HumanoidModel p_188359_1_, EquipmentSlot slotIn) {
        this.setModelVisible(p_188359_1_);
        switch(slotIn) {
            case HEAD:
                p_188359_1_.head.visible = true;
                p_188359_1_.hat.visible = true;
                break;
            case CHEST:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightArm.visible = true;
                p_188359_1_.leftArm.visible = true;
                break;
            case LEGS:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
                break;
            case FEET:
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
        }
    }

    protected void setModelVisible(HumanoidModel model) {
        model.setAllVisible(false);
    }

    protected HumanoidModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        Model basicModel = ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
        return basicModel instanceof HumanoidModel ? (HumanoidModel) basicModel : model;
    }
}