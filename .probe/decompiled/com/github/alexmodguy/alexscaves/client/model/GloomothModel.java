package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.GloomothEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;

public class GloomothModel extends AdvancedEntityModel<GloomothEntity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rantennae;

    private final AdvancedModelBox lantennae;

    private final AdvancedModelBox lWing;

    private final AdvancedModelBox lWing_b;

    private final AdvancedModelBox lWing_s;

    private final AdvancedModelBox rWing;

    private final AdvancedModelBox rWing_b;

    private final AdvancedModelBox rWing_s;

    private final AdvancedModelBox legs;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg2;

    private final AdvancedModelBox rleg2;

    private final AdvancedModelBox rleg3;

    private final AdvancedModelBox lleg3;

    public GloomothModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 17.5F, 0.0F);
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(31, 25).addBox(-2.5F, -2.5F, -1.0F, 5.0F, 5.0F, 7.0F, 0.0F, false);
        this.body.setTextureOffset(0, 32).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 7.0F, 5.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -1.0F, -5.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 4.0F, 3.0F, 0.0F, false);
        this.rantennae = new AdvancedModelBox(this);
        this.rantennae.setRotationPoint(-1.0F, -2.0F, -2.5F);
        this.head.addChild(this.rantennae);
        this.rantennae.setTextureOffset(22, 38).addBox(-7.5F, -7.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F, false);
        this.lantennae = new AdvancedModelBox(this);
        this.lantennae.setRotationPoint(1.0F, -2.0F, -2.5F);
        this.head.addChild(this.lantennae);
        this.lantennae.setTextureOffset(22, 38).addBox(-0.5F, -7.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F, true);
        this.lWing = new AdvancedModelBox(this);
        this.lWing.setRotationPoint(2.5F, -2.0F, -2.0F);
        this.body.addChild(this.lWing);
        this.lWing_b = new AdvancedModelBox(this);
        this.lWing_b.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lWing.addChild(this.lWing_b);
        this.lWing_b.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -12.0F, 13.0F, 0.0F, 16.0F, 0.0F, false);
        this.lWing_s = new AdvancedModelBox(this);
        this.lWing_s.setRotationPoint(0.0F, 0.25F, 0.0F);
        this.lWing.addChild(this.lWing_s);
        this.lWing_s.setTextureOffset(0, 16).addBox(0.0F, 0.0F, -2.0F, 11.0F, 0.0F, 16.0F, 0.0F, false);
        this.rWing = new AdvancedModelBox(this);
        this.rWing.setRotationPoint(-2.5F, -2.0F, -2.0F);
        this.body.addChild(this.rWing);
        this.rWing_b = new AdvancedModelBox(this);
        this.rWing_b.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rWing.addChild(this.rWing_b);
        this.rWing_b.setTextureOffset(0, 0).addBox(-12.5F, 0.0F, -12.0F, 13.0F, 0.0F, 16.0F, 0.0F, true);
        this.rWing_s = new AdvancedModelBox(this);
        this.rWing_s.setRotationPoint(0.0F, 0.25F, 0.0F);
        this.rWing.addChild(this.rWing_s);
        this.rWing_s.setTextureOffset(0, 16).addBox(-11.0F, 0.0F, -2.0F, 11.0F, 0.0F, 16.0F, 0.0F, true);
        this.legs = new AdvancedModelBox(this);
        this.legs.setRotationPoint(0.0F, 2.5F, -2.0F);
        this.root.addChild(this.legs);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(1.0F, 0.0F, -2.0F);
        this.legs.addChild(this.lleg);
        this.lleg.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-1.0F, 0.0F, -2.0F);
        this.legs.addChild(this.rleg);
        this.rleg.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
        this.lleg2 = new AdvancedModelBox(this);
        this.lleg2.setRotationPoint(1.0F, 0.0F, 0.0F);
        this.legs.addChild(this.lleg2);
        this.lleg2.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);
        this.rleg2 = new AdvancedModelBox(this);
        this.rleg2.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.legs.addChild(this.rleg2);
        this.rleg2.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
        this.rleg3 = new AdvancedModelBox(this);
        this.rleg3.setRotationPoint(-1.0F, 0.0F, 2.0F);
        this.legs.addChild(this.rleg3);
        this.rleg3.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);
        this.lleg3 = new AdvancedModelBox(this);
        this.lleg3.setRotationPoint(1.0F, 0.0F, 2.0F);
        this.legs.addChild(this.lleg3);
        this.lleg3.setTextureOffset(2, 11).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(GloomothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 2.0F;
        float walkDegree = 0.7F;
        float flySpeed = 0.6F;
        float flyDegree = 0.9F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.getFlyProgress(partialTick);
        float rollAmount = entity.getFlightRoll(partialTick) / (180.0F / (float) Math.PI) * flyProgress;
        float pitchAmount = entity.getFlightPitch(partialTick) / (180.0F / (float) Math.PI) * flyProgress;
        float groundedAmount = 1.0F - flyProgress;
        this.progressRotationPrev(this.lantennae, flyProgress, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rantennae, flyProgress, (float) Math.toRadians(40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, flyProgress, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, flyProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg2, flyProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg3, flyProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, flyProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg2, flyProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg3, flyProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.legs, flyProgress, 0.0F, -1.0F, 0.0F, 1.0F);
        this.walk(this.rantennae, 0.1F, 0.15F, true, 1.0F, -0.3F, ageInTicks, 1.0F);
        this.walk(this.lantennae, 0.1F, 0.15F, true, 1.0F, -0.3F, ageInTicks, 1.0F);
        this.flap(this.rantennae, 0.1F, 0.15F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.lantennae, 0.1F, 0.15F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.lleg, walkSpeed, walkDegree, false, 4.0F, 0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.walk(this.lleg2, walkSpeed, walkDegree, false, 2.5F, 0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.walk(this.lleg3, walkSpeed, walkDegree, false, 1.0F, 0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, true, 4.0F, -0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.walk(this.rleg2, walkSpeed, walkDegree, true, 2.5F, -0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.walk(this.rleg3, walkSpeed, walkDegree, true, 1.0F, -0.1F, limbSwing, limbSwingAmount * groundedAmount);
        this.flap(this.rWing, flySpeed * 1.5F, flyDegree, false, 1.5F, -0.1F, ageInTicks, flyProgress);
        this.flap(this.lWing, flySpeed * 1.5F, flyDegree, true, 1.5F, -0.1F, ageInTicks, flyProgress);
        this.flap(this.rWing_s, flySpeed * 1.5F, flyDegree * 0.45F, false, 2.0F, -0.1F, ageInTicks, flyProgress);
        this.flap(this.lWing_s, flySpeed * 1.5F, flyDegree * 0.45F, true, 2.0F, -0.1F, ageInTicks, flyProgress);
        this.bob(this.root, flySpeed, flyDegree * 2.0F, false, ageInTicks, flyProgress);
        this.root.rotateAngleX += pitchAmount;
        this.root.rotateAngleZ += rollAmount;
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.rantennae, this.lantennae, this.lWing, this.rWing, this.lWing_s, this.rWing_s, this.lWing_b, this.rWing_b, this.legs, new AdvancedModelBox[] { this.rleg, this.rleg2, this.rleg3, this.lleg, this.lleg2, this.lleg3 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }
}