package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.NotNull;

public class ModelStonePlayer extends HumanoidModel<EntityStoneStatue> {

    public ModelStonePlayer(ModelPart modelPart0) {
        super(modelPart0);
    }

    public void setupAnim(@NotNull EntityStoneStatue entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
}