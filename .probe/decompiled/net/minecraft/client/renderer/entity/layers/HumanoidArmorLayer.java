package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;

public class HumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

    private final A innerModel;

    private final A outerModel;

    private final TextureAtlas armorTrimAtlas;

    public HumanoidArmorLayer(RenderLayerParent<T, M> renderLayerParentTM0, A a1, A a2, ModelManager modelManager3) {
        super(renderLayerParentTM0);
        this.innerModel = a1;
        this.outerModel = a2;
        this.armorTrimAtlas = modelManager3.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        this.renderArmorPiece(poseStack0, multiBufferSource1, t3, EquipmentSlot.CHEST, int2, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece(poseStack0, multiBufferSource1, t3, EquipmentSlot.LEGS, int2, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece(poseStack0, multiBufferSource1, t3, EquipmentSlot.FEET, int2, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece(poseStack0, multiBufferSource1, t3, EquipmentSlot.HEAD, int2, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack poseStack0, MultiBufferSource multiBufferSource1, T t2, EquipmentSlot equipmentSlot3, int int4, A a5) {
        ItemStack $$6 = t2.getItemBySlot(equipmentSlot3);
        if ($$6.getItem() instanceof ArmorItem $$7) {
            if ($$7.getEquipmentSlot() == equipmentSlot3) {
                ((HumanoidModel) this.m_117386_()).copyPropertiesTo(a5);
                this.setPartVisibility(a5, equipmentSlot3);
                boolean $$9 = this.usesInnerModel(equipmentSlot3);
                if ($$7 instanceof DyeableArmorItem $$10) {
                    int $$11 = $$10.m_41121_($$6);
                    float $$12 = (float) ($$11 >> 16 & 0xFF) / 255.0F;
                    float $$13 = (float) ($$11 >> 8 & 0xFF) / 255.0F;
                    float $$14 = (float) ($$11 & 0xFF) / 255.0F;
                    this.renderModel(poseStack0, multiBufferSource1, int4, $$7, a5, $$9, $$12, $$13, $$14, null);
                    this.renderModel(poseStack0, multiBufferSource1, int4, $$7, a5, $$9, 1.0F, 1.0F, 1.0F, "overlay");
                } else {
                    this.renderModel(poseStack0, multiBufferSource1, int4, $$7, a5, $$9, 1.0F, 1.0F, 1.0F, null);
                }
                ArmorTrim.getTrim(t2.m_9236_().registryAccess(), $$6).ifPresent(p_289638_ -> this.renderTrim($$7.getMaterial(), poseStack0, multiBufferSource1, int4, p_289638_, a5, $$9));
                if ($$6.hasFoil()) {
                    this.renderGlint(poseStack0, multiBufferSource1, int4, a5);
                }
            }
        }
    }

    protected void setPartVisibility(A a0, EquipmentSlot equipmentSlot1) {
        a0.setAllVisible(false);
        switch(equipmentSlot1) {
            case HEAD:
                a0.head.visible = true;
                a0.hat.visible = true;
                break;
            case CHEST:
                a0.body.visible = true;
                a0.rightArm.visible = true;
                a0.leftArm.visible = true;
                break;
            case LEGS:
                a0.body.visible = true;
                a0.rightLeg.visible = true;
                a0.leftLeg.visible = true;
                break;
            case FEET:
                a0.rightLeg.visible = true;
                a0.leftLeg.visible = true;
        }
    }

    private void renderModel(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, ArmorItem armorItem3, A a4, boolean boolean5, float float6, float float7, float float8, @Nullable String string9) {
        VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.armorCutoutNoCull(this.getArmorLocation(armorItem3, boolean5, string9)));
        a4.m_7695_(poseStack0, $$10, int2, OverlayTexture.NO_OVERLAY, float6, float7, float8, 1.0F);
    }

    private void renderTrim(ArmorMaterial armorMaterial0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, int int3, ArmorTrim armorTrim4, A a5, boolean boolean6) {
        TextureAtlasSprite $$7 = this.armorTrimAtlas.getSprite(boolean6 ? armorTrim4.innerTexture(armorMaterial0) : armorTrim4.outerTexture(armorMaterial0));
        VertexConsumer $$8 = $$7.wrap(multiBufferSource2.getBuffer(Sheets.armorTrimsSheet()));
        a5.m_7695_(poseStack1, $$8, int3, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, A a3) {
        a3.m_7695_(poseStack0, multiBufferSource1.getBuffer(RenderType.armorEntityGlint()), int2, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private A getArmorModel(EquipmentSlot equipmentSlot0) {
        return this.usesInnerModel(equipmentSlot0) ? this.innerModel : this.outerModel;
    }

    private boolean usesInnerModel(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.LEGS;
    }

    private ResourceLocation getArmorLocation(ArmorItem armorItem0, boolean boolean1, @Nullable String string2) {
        String $$3 = "textures/models/armor/" + armorItem0.getMaterial().getName() + "_layer_" + (boolean1 ? 2 : 1) + (string2 == null ? "" : "_" + string2) + ".png";
        return (ResourceLocation) ARMOR_LOCATION_CACHE.computeIfAbsent($$3, ResourceLocation::new);
    }
}