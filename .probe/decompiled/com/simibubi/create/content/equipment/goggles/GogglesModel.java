package com.simibubi.create.content.equipment.goggles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.BakedModelWrapper;

public class GogglesModel extends BakedModelWrapper<BakedModel> {

    public GogglesModel(BakedModel template) {
        super(template);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext cameraItemDisplayContext, PoseStack mat, boolean leftHanded) {
        return cameraItemDisplayContext == ItemDisplayContext.HEAD ? AllPartialModels.GOGGLES.get().applyTransform(cameraItemDisplayContext, mat, leftHanded) : super.applyTransform(cameraItemDisplayContext, mat, leftHanded);
    }
}