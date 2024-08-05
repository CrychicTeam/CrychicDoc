package io.redspace.ironsspellbooks.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class GenericCustomArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public GeoBone leggingTorsoLayerBone = null;

    public ResourceLocation getTextureLocation(T animatable) {
        return super.getTextureLocation(animatable);
    }

    public GenericCustomArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void scaleModelForBaby(PoseStack poseStack, T animatable, float partialTick, boolean isReRender) {
    }

    @Nullable
    public GeoBone getLeggingTorsoLayerBone() {
        return (GeoBone) this.model.getBone("armorLeggingTorsoLayer").orElse(null);
    }

    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel != bakedModel) {
            this.leggingTorsoLayerBone = this.getLeggingTorsoLayerBone();
        }
        super.grabRelevantBones(bakedModel);
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        super.applyBoneVisibilityBySlot(currentSlot);
        if (currentSlot == EquipmentSlot.LEGS) {
            this.setBoneVisible(this.leggingTorsoLayerBone, true);
        }
    }

    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        super.applyBoneVisibilityByPart(currentSlot, currentPart, model);
        if (currentPart == model.body && currentSlot == EquipmentSlot.LEGS) {
            this.setBoneVisible(this.leggingTorsoLayerBone, true);
        }
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);
        if (this.leggingTorsoLayerBone != null) {
            ModelPart bodyPart = baseModel.body;
            RenderUtils.matchModelPartRot(bodyPart, this.leggingTorsoLayerBone);
            this.leggingTorsoLayerBone.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.setBoneVisible(this.leggingTorsoLayerBone, pVisible);
    }
}