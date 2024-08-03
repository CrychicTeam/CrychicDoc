package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.MagnetronEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class MagnetronModel extends AdvancedEntityModel<MagnetronEntity> {

    private final AdvancedModelBox wheel;

    private final AdvancedModelBox headPivot;

    private final AdvancedModelBox head;

    private final AdvancedModelBox headExtra;

    public MagnetronModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.wheel = new AdvancedModelBox(this);
        this.wheel.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.wheel.setTextureOffset(28, 28).addBox(-3.0F, -8.0F, -8.0F, 6.0F, 4.0F, 16.0F, 0.0F, false);
        this.wheel.setTextureOffset(40, 60).addBox(-3.0F, -4.0F, -8.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        this.wheel.setTextureOffset(56, 22).addBox(-3.0F, -4.0F, 4.0F, 6.0F, 8.0F, 4.0F, 0.0F, false);
        this.wheel.setTextureOffset(0, 44).addBox(-3.0F, 4.0F, -8.0F, 6.0F, 4.0F, 16.0F, 0.0F, false);
        this.headPivot = new AdvancedModelBox(this);
        this.headPivot.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.head = new AdvancedModelBox(this);
        this.setRotationAngle(this.head, 0.3927F, 0.0F, 0.0F);
        this.headPivot.addChild(this.head);
        this.headExtra = new AdvancedModelBox(this);
        this.headExtra.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.head.addChild(this.headExtra);
        this.setRotationAngle(this.headExtra, 0.0F, -0.7854F, 0.0F);
        this.headExtra.setTextureOffset(52, 48).addBox(-2.0F, -5.0F, 18.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.headExtra.setTextureOffset(0, 22).addBox(-8.0F, -5.0F, 8.0F, 6.0F, 6.0F, 16.0F, 0.0F, false);
        this.headExtra.setTextureOffset(28, 48).addBox(18.0F, -5.0F, -2.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.headExtra.setTextureOffset(48, 0).addBox(8.0F, -5.0F, -8.0F, 16.0F, 6.0F, 6.0F, 0.0F, false);
        this.headExtra.setTextureOffset(0, 0).addBox(-8.0F, -5.0F, -8.0F, 16.0F, 6.0F, 16.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.wheel, this.headPivot);
    }

    public void setupAnim(MagnetronEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float formProgress = Math.min(1.0F, entity.getFormProgress(partialTick) * 10.0F);
        float rollProgress = 1.0F - formProgress;
        float rollLeanProgress = rollProgress * entity.getRollLeanProgress(partialTick);
        float timeRolling = (float) Math.toRadians((double) Mth.wrapDegrees(entity.getRollPosition(partialTick)));
        float bodyYaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float wheelYaw = (entity.getWheelYaw(partialTick) - bodyYaw) * rollProgress + 90.0F * formProgress;
        this.progressRotationPrev(this.head, rollLeanProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.headPivot, rollLeanProgress, 0.0F, -2.0F, -4.0F, 1.0F);
        this.bob(this.headPivot, 0.15F, 1.5F, false, ageInTicks, 1.0F);
        this.wheel.rotateAngleY = (float) Math.toRadians((double) wheelYaw);
        if (entity.isFormed()) {
            float var14 = (float) Mth.wrapDegrees(Math.toDegrees((double) entity.clientRoll));
        } else {
            this.wheel.rotateAngleX = timeRolling * rollProgress;
            entity.clientRoll = this.wheel.rotateAngleX;
            this.bob(this.wheel, 1.0F, 10.0F, true, timeRolling, rollProgress);
            this.bob(this.headPivot, 1.0F, 4.0F, true, timeRolling, rollProgress);
        }
        if (entity.m_6084_()) {
            this.wheel.showModel = true;
        } else {
            this.wheel.showModel = false;
        }
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.headPivot });
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.wheel, this.headPivot, this.head, this.headExtra);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}