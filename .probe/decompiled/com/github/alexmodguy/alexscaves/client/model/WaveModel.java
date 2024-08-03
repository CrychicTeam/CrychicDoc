package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.WaveEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class WaveModel extends AdvancedEntityModel<WaveEntity> {

    private final AdvancedModelBox main;

    private final AdvancedModelBox top;

    public WaveModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.main.setTextureOffset(0, 28).addBox(-13.0F, -7.2919F, 0.9497F, 26.0F, 6.0F, 12.0F, -0.01F, false);
        this.top = new AdvancedModelBox(this);
        this.top.setRotationPoint(0.0F, -2.0F, -4.0F);
        this.main.addChild(this.top);
        this.setRotateAngle(this.top, 0.7854F, 0.0F, 0.0F);
        this.top.setTextureOffset(0, 0).addBox(-13.0F, -7.0F, 3.0F, 26.0F, 11.0F, 17.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.main);
    }

    public void setupAnim(WaveEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float f = (float) ((Math.sin((double) (ageInTicks * 0.1F)) + 1.0) * 0.2F);
        float waveScale = entity.getWaveScale();
        float stretch = Math.min(40.0F, ageInTicks) / 40.0F;
        float slam = entity.getSlamAmount(ageInTicks - (float) entity.activeWaveTicks) * 1.4F;
        this.progressRotationPrev(this.top, slam, (float) Math.toRadians(100.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.top, slam, 0.0F, -1.0F, 13.0F, 1.0F);
        this.top.rotateAngleX += f;
        this.top.rotationPointY += f * 8.0F;
        this.top.rotationPointZ += f * 2.0F;
        this.main.setScale(waveScale, waveScale, waveScale + stretch * 4.0F);
        this.top.setScale(waveScale, waveScale, waveScale + stretch * 1.0F);
        this.top.rotationPointY += stretch * 2.0F;
        this.top.rotationPointZ += stretch * 2.0F;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.main, this.top);
    }
}