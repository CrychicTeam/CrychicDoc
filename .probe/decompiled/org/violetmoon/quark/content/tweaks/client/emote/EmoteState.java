package org.violetmoon.quark.content.tweaks.client.emote;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;

public class EmoteState {

    private float[] states = new float[0];

    private final EmoteBase emote;

    public EmoteState(EmoteBase emote) {
        this.emote = emote;
    }

    public void save(HumanoidModel<?> model) {
        float[] values = new float[1];
        for (int i = 0; i < 21; i++) {
            ModelAccessor.INSTANCE.getValues(model, i, values);
            this.states[i] = values[0];
        }
    }

    public void load(HumanoidModel<?> model) {
        if (this.states.length == 0) {
            this.states = new float[21];
        } else {
            float[] values = new float[1];
            for (int i = 0; i < 21; i++) {
                values[0] = this.states[i];
                int part = i / 3 * 3;
                if (this.emote.usesBodyPart(part)) {
                    ModelAccessor.INSTANCE.setValues(model, i, values);
                }
            }
        }
    }

    public void rotateAndOffset(PoseStack stack, Player player) {
        if (this.states.length != 0) {
            float rotX = this.states[18];
            float rotY = this.states[19];
            float rotZ = this.states[20];
            float height = player.m_20206_();
            stack.translate(0.0F, height / 2.0F, 0.0F);
            if (rotY != 0.0F) {
                stack.mulPose(Axis.YP.rotation(rotY));
            }
            if (rotX != 0.0F) {
                stack.mulPose(Axis.XP.rotation(rotX));
            }
            if (rotZ != 0.0F) {
                stack.mulPose(Axis.ZP.rotation(rotZ));
            }
            stack.translate(0.0F, -height / 2.0F, 0.0F);
        }
    }
}