package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.TrilocarisEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class TrilocarisModel extends AdvancedEntityModel<TrilocarisEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox legs3;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rantennae;

    private final AdvancedModelBox lantennae;

    private final AdvancedModelBox lmandible;

    private final AdvancedModelBox rmandible;

    private final AdvancedModelBox legs;

    private final AdvancedModelBox legs2;

    private final AdvancedModelBox tailFlipper;

    private final AdvancedModelBox lflippers;

    private final AdvancedModelBox lflipper;

    private final AdvancedModelBox lflipper2;

    private final AdvancedModelBox lflipper3;

    private final AdvancedModelBox rflippers;

    private final AdvancedModelBox rflipper;

    private final AdvancedModelBox rflipper2;

    private final AdvancedModelBox rflipper3;

    private final AdvancedModelBox legs4;

    public TrilocarisModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.setTextureOffset(21, 18).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 2.0F, 5.0F, 0.0F, false);
        this.body.setTextureOffset(16, 8).addBox(0.0F, -2.0F, 0.0F, 0.0F, 1.0F, 5.0F, 0.0F, false);
        this.legs3 = new AdvancedModelBox(this);
        this.legs3.setRotationPoint(0.0F, 1.0F, 1.0F);
        this.body.addChild(this.legs3);
        this.legs3.setTextureOffset(0, 5).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 7).addBox(0.0F, -3.0F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(21, 9).addBox(-4.0F, -2.0F, -5.0F, 8.0F, 3.0F, 5.0F, 0.0F, false);
        this.rantennae = new AdvancedModelBox(this);
        this.rantennae.setRotationPoint(-1.5F, -2.0F, -5.0F);
        this.head.addChild(this.rantennae);
        this.rantennae.setTextureOffset(42, 5).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 5.0F, 0.0F, false);
        this.lantennae = new AdvancedModelBox(this);
        this.lantennae.setRotationPoint(1.5F, -2.0F, -5.0F);
        this.head.addChild(this.lantennae);
        this.lantennae.setTextureOffset(42, 5).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 4.0F, 5.0F, 0.0F, true);
        this.lmandible = new AdvancedModelBox(this);
        this.lmandible.setRotationPoint(3.0F, 0.5F, -5.0F);
        this.head.addChild(this.lmandible);
        this.lmandible.setTextureOffset(0, 22).addBox(-2.0F, -0.5F, -6.0F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        this.rmandible = new AdvancedModelBox(this);
        this.rmandible.setRotationPoint(-3.0F, 0.5F, -5.0F);
        this.head.addChild(this.rmandible);
        this.rmandible.setTextureOffset(0, 22).addBox(-1.0F, -0.5F, -6.0F, 3.0F, 1.0F, 7.0F, 0.0F, true);
        this.legs = new AdvancedModelBox(this);
        this.legs.setRotationPoint(0.0F, 1.0F, -3.0F);
        this.head.addChild(this.legs);
        this.legs.setTextureOffset(0, 5).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        this.legs2 = new AdvancedModelBox(this);
        this.legs2.setRotationPoint(0.0F, 1.0F, -1.0F);
        this.head.addChild(this.legs2);
        this.legs2.setTextureOffset(0, 5).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        this.tailFlipper = new AdvancedModelBox(this);
        this.tailFlipper.setRotationPoint(0.0F, 0.0F, 5.0F);
        this.body.addChild(this.tailFlipper);
        this.tailFlipper.setTextureOffset(0, 0).addBox(-6.0F, 0.0F, 0.0F, 12.0F, 0.0F, 5.0F, 0.0F, false);
        this.lflippers = new AdvancedModelBox(this);
        this.lflippers.setRotationPoint(3.0F, 1.0F, 3.0F);
        this.body.addChild(this.lflippers);
        this.lflipper = new AdvancedModelBox(this);
        this.lflipper.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.lflippers.addChild(this.lflipper);
        this.lflipper.setTextureOffset(0, 8).addBox(0.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);
        this.lflipper2 = new AdvancedModelBox(this);
        this.lflipper2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lflippers.addChild(this.lflipper2);
        this.lflipper2.setTextureOffset(0, 8).addBox(0.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);
        this.lflipper3 = new AdvancedModelBox(this);
        this.lflipper3.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.lflippers.addChild(this.lflipper3);
        this.lflipper3.setTextureOffset(0, 8).addBox(0.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, false);
        this.rflippers = new AdvancedModelBox(this);
        this.rflippers.setRotationPoint(-3.0F, 1.0F, 3.0F);
        this.body.addChild(this.rflippers);
        this.rflipper = new AdvancedModelBox(this);
        this.rflipper.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.rflippers.addChild(this.rflipper);
        this.rflipper.setTextureOffset(0, 8).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);
        this.rflipper2 = new AdvancedModelBox(this);
        this.rflipper2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rflippers.addChild(this.rflipper2);
        this.rflipper2.setTextureOffset(0, 8).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);
        this.rflipper3 = new AdvancedModelBox(this);
        this.rflipper3.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.rflippers.addChild(this.rflipper3);
        this.rflipper3.setTextureOffset(0, 8).addBox(-4.0F, 0.0F, -1.0F, 4.0F, 0.0F, 2.0F, 0.0F, true);
        this.legs4 = new AdvancedModelBox(this);
        this.legs4.setRotationPoint(0.0F, 1.0F, 3.0F);
        this.body.addChild(this.legs4);
        this.legs4.setTextureOffset(0, 5).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 3.0F, 0.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.head, this.tailFlipper, this.legs, this.legs2, this.legs3, this.legs4, this.rflipper, this.rflipper2, this.rflipper3, this.rflippers, this.lflipper, new AdvancedModelBox[] { this.lflipper2, this.lflipper3, this.lflippers, this.lantennae, this.rantennae, this.lmandible, this.rmandible });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(TrilocarisEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float groundProgress = entity.getGroundProgress(partialTicks);
        float swimProgress = 1.0F - groundProgress;
        float biteProgress = entity.getBiteProgress(partialTicks);
        float walkAmount = Math.min(limbSwingAmount * groundProgress * 10.0F, 1.0F);
        float swimAmount = limbSwingAmount * swimProgress;
        if (entity.f_20919_ > 0) {
            limbSwing = ageInTicks;
            limbSwingAmount = 1.0F;
        }
        this.progressPositionPrev(this.lflippers, swimProgress, 0.0F, 0.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rflippers, swimProgress, 0.0F, 0.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.legs, swimProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.legs2, swimProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.legs3, swimProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.legs4, swimProgress, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.legs, swimProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.legs2, swimProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.legs3, swimProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.legs4, swimProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rmandible, biteProgress, 0.0F, (float) Math.toRadians(40.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lmandible, biteProgress, 0.0F, (float) Math.toRadians(-40.0), 0.0F, 1.0F);
        this.walk(this.lantennae, 0.1F, 0.15F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.rantennae, walkSpeed, walkDegree * 0.1F, false, 1.0F, -0.7F, limbSwing, limbSwingAmount);
        this.walk(this.lantennae, walkSpeed, walkDegree * 0.1F, true, 1.0F, 0.7F, limbSwing, limbSwingAmount);
        this.walk(this.rantennae, 0.1F, 0.15F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.rmandible, 0.1F, 0.15F, true, 2.0F, 0.05F, ageInTicks, 1.0F);
        this.swing(this.lmandible, 0.1F, 0.15F, false, 2.0F, 0.05F, ageInTicks, 1.0F);
        float bodyBob = Math.abs((float) Math.cos((double) (ageInTicks * 0.1F))) * swimProgress;
        this.body.rotationPointY += bodyBob;
        this.legs.rotationPointY -= bodyBob * groundProgress;
        this.legs2.rotationPointY -= bodyBob * groundProgress;
        this.legs3.rotationPointY -= bodyBob * groundProgress;
        this.legs4.rotationPointY -= bodyBob * groundProgress;
        this.walk(this.legs, walkSpeed, walkDegree * 0.5F, true, 1.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.legs2, walkSpeed, walkDegree * 0.5F, true, 2.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.legs3, walkSpeed, walkDegree * 0.5F, true, 3.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.legs4, walkSpeed, walkDegree * 0.5F, true, 4.0F, -0.1F, limbSwing, walkAmount);
        this.walk(this.body, walkSpeed, walkDegree * 0.15F, false, 0.0F, 0.0F, limbSwing, swimAmount);
        this.flap(this.rflipper, walkSpeed, walkDegree, true, 3.0F, 0.05F, limbSwing, swimAmount);
        this.flap(this.rflipper2, walkSpeed, walkDegree, true, 2.0F, 0.05F, limbSwing, swimAmount);
        this.flap(this.rflipper3, walkSpeed, walkDegree, true, 1.0F, 0.05F, limbSwing, swimAmount);
        this.flap(this.lflipper, walkSpeed, walkDegree, false, 3.0F, 0.05F, limbSwing, swimAmount);
        this.flap(this.lflipper2, walkSpeed, walkDegree, false, 2.0F, 0.05F, limbSwing, swimAmount);
        this.flap(this.lflipper3, walkSpeed, walkDegree, false, 1.0F, 0.05F, limbSwing, swimAmount);
        this.walk(this.tailFlipper, walkSpeed, walkDegree, false, 5.0F, 0.05F, limbSwing, swimAmount);
    }
}