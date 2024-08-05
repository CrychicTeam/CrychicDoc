package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;

public abstract class ACBoatModel extends AdvancedEntityModel<Boat> {

    public abstract AdvancedModelBox getWaterMask();

    public void setupPaddleAnims(Boat boat, AdvancedModelBox leftPaddle, AdvancedModelBox rightPaddle, float partialTicks) {
        animatePaddle(boat, 0, leftPaddle, partialTicks);
        animatePaddle(boat, 1, rightPaddle, partialTicks);
    }

    private static void animatePaddle(Boat boat, int i, AdvancedModelBox paddle, float partialTicks) {
        float f = boat.getRowingTime(i, partialTicks);
        float f1 = i == 1 ? -1.0F : 1.0F;
        paddle.rotateAngleZ = -f1 * Mth.clampedLerp((float) (-Math.PI / 3), (float) (-Math.PI / 12), (Mth.sin(-f) + 1.0F) / 2.0F);
        paddle.rotateAngleY = f1 * Mth.clampedLerp((float) (-Math.PI / 4), (float) (Math.PI / 4), (Mth.sin(-f + 1.0F) + 1.0F) / 2.0F);
    }
}