package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.RadgillEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class RadgillModel extends AdvancedEntityModel<RadgillEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox dorsal2;

    private final AdvancedModelBox tailfin;

    private final AdvancedModelBox dorsal;

    private final AdvancedModelBox bottom_fin;

    private final AdvancedModelBox lfin;

    private final AdvancedModelBox rfin;

    private final AdvancedModelBox jaw;

    public RadgillModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.body.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 9.0F, 11.0F, 0.0F, false);
        this.body.setTextureOffset(38, 40).addBox(-1.5F, -6.0F, -5.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.body.setTextureOffset(26, 40).addBox(1.75F, -5.0F, -5.99F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.body.setTextureOffset(34, 17).addBox(-4.75F, -5.0F, -5.99F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.body.setTextureOffset(23, 0).addBox(-3.0F, -4.0F, -11.0F, 6.0F, 4.0F, 5.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -1.0F, 4.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(18, 20).addBox(-2.0F, -3.0F, 0.5F, 4.0F, 6.0F, 8.0F, 0.0F, false);
        this.dorsal2 = new AdvancedModelBox(this);
        this.dorsal2.setRotationPoint(0.0F, -3.0F, 3.0F);
        this.tail.addChild(this.dorsal2);
        this.dorsal2.setTextureOffset(20, 28).addBox(0.0F, -6.0F, -0.5F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        this.tailfin = new AdvancedModelBox(this);
        this.tailfin.setRotationPoint(0.0F, 0.0F, 8.5F);
        this.tail.addChild(this.tailfin);
        this.tailfin.setTextureOffset(5, 34).addBox(0.0F, -4.0F, -4.0F, 0.0F, 10.0F, 14.0F, 0.0F, false);
        this.dorsal = new AdvancedModelBox(this);
        this.dorsal.setRotationPoint(0.0F, -4.0F, -0.5F);
        this.body.addChild(this.dorsal);
        this.dorsal.setTextureOffset(32, 28).addBox(0.0F, -6.0F, -0.5F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        this.bottom_fin = new AdvancedModelBox(this);
        this.bottom_fin.setRotationPoint(0.0F, 4.0F, 3.0F);
        this.body.addChild(this.bottom_fin);
        this.bottom_fin.setTextureOffset(0, 22).addBox(0.0F, -1.0F, -1.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
        this.lfin = new AdvancedModelBox(this);
        this.lfin.setRotationPoint(3.0F, 2.5F, -1.0F);
        this.body.addChild(this.lfin);
        this.setRotateAngle(this.lfin, 0.0F, -0.7854F, 0.0F);
        this.lfin.setTextureOffset(34, 9).addBox(0.0F, -2.5F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, false);
        this.rfin = new AdvancedModelBox(this);
        this.rfin.setRotationPoint(-3.0F, 2.5F, -1.0F);
        this.body.addChild(this.rfin);
        this.setRotateAngle(this.rfin, 0.0F, 0.7854F, 0.0F);
        this.rfin.setTextureOffset(34, 9).addBox(-6.0F, -2.5F, 0.0F, 6.0F, 8.0F, 0.0F, 0.0F, true);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 5.0F, -6.0F);
        this.body.addChild(this.jaw);
        this.jaw.setTextureOffset(0, 34).addBox(-3.5F, -6.0F, -5.0F, 7.0F, 6.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.dorsal, this.dorsal2, this.tailfin, this.jaw, this.bottom_fin, this.rfin, this.lfin);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(RadgillEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float swimSpeed = 0.5F;
        float swimDegree = 0.5F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float landProgress = entity.getLandProgress(partialTick);
        float pitchAmount = entity.getFishPitch(partialTick) / (180.0F / (float) Math.PI);
        this.progressRotationPrev(this.body, landProgress, 0.0F, 0.0F, (float) Math.toRadians(-90.0), 1.0F);
        this.walk(this.jaw, 0.1F, 0.2F, false, 1.0F, -0.2F, ageInTicks, 1.0F);
        this.walk(this.body, 0.1F, 0.1F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.lfin, 0.1F, 0.2F, false, -1.0F, -0.2F, ageInTicks, 1.0F);
        this.swing(this.rfin, 0.1F, 0.2F, true, -1.0F, -0.2F, ageInTicks, 1.0F);
        this.bob(this.body, 0.1F, 1.0F, false, ageInTicks, 1.0F);
        this.swing(this.body, swimSpeed, swimDegree * 0.2F, false, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, swimSpeed, swimDegree, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tailfin, swimSpeed, swimDegree, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.rfin, swimSpeed, swimDegree * 0.9F, true, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.swing(this.lfin, swimSpeed, swimDegree * 0.9F, false, 0.0F, 0.2F, limbSwing, limbSwingAmount);
        this.body.rotateAngleX += pitchAmount;
    }
}