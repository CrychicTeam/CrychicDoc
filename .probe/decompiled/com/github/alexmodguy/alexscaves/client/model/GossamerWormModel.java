package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.GossamerWormEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class GossamerWormModel extends AdvancedEntityModel<GossamerWormEntity> {

    private final AdvancedModelBox head;

    private final AdvancedModelBox antennae;

    private final AdvancedModelBox antennae2;

    private final AdvancedModelBox segment;

    private final AdvancedModelBox rflipperFront;

    private final AdvancedModelBox rflipperBack;

    private final AdvancedModelBox lflipperFront;

    private final AdvancedModelBox lflipperBack;

    private final AdvancedModelBox segment2;

    private final AdvancedModelBox rflipperFront2;

    private final AdvancedModelBox rflipperBack2;

    private final AdvancedModelBox lflipperFront2;

    private final AdvancedModelBox lflipperBack2;

    private final AdvancedModelBox segment3;

    private final AdvancedModelBox rflipperFront3;

    private final AdvancedModelBox rflipperBack3;

    private final AdvancedModelBox lflipperFront3;

    private final AdvancedModelBox lflipperBack3;

    private final AdvancedModelBox segment4;

    private final AdvancedModelBox rflipperFront4;

    private final AdvancedModelBox rflipperBack4;

    private final AdvancedModelBox lflipperFront4;

    private final AdvancedModelBox lflipperBack4;

    private final AdvancedModelBox segment5;

    private final AdvancedModelBox rflipperBack5;

    private final AdvancedModelBox lflipperBack5;

    private final AdvancedModelBox rflipperFront5;

    private final AdvancedModelBox lflipperFront5;

    private final AdvancedModelBox segment6;

    private final AdvancedModelBox rflipperBack6;

    private final AdvancedModelBox lflipperBack6;

    private final AdvancedModelBox rflipperFront6;

    private final AdvancedModelBox lflipperFront6;

    private final AdvancedModelBox segment7;

    private final AdvancedModelBox rflipperBack7;

    private final AdvancedModelBox lflipperBack7;

    private final AdvancedModelBox rflipperFront7;

    private final AdvancedModelBox lflipperFront7;

    private final AdvancedModelBox segment8;

    private final AdvancedModelBox segment9;

    public boolean straighten;

    public GossamerWormModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 23.0F, -5.75F);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -1.0F, -5.25F, 8.0F, 2.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(0, 44).addBox(-8.0F, 0.0F, -13.25F, 16.0F, 0.0F, 8.0F, 0.0F, false);
        this.antennae = new AdvancedModelBox(this);
        this.antennae.setRotationPoint(4.0F, 0.0F, -4.25F);
        this.head.addChild(this.antennae);
        this.antennae.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -1.0F, 21.0F, 0.0F, 30.0F, 0.0F, false);
        this.antennae2 = new AdvancedModelBox(this);
        this.antennae2.setRotationPoint(-4.0F, 0.0F, -4.25F);
        this.head.addChild(this.antennae2);
        this.antennae2.setTextureOffset(0, 0).addBox(-19.0F, 0.0F, -1.0F, 21.0F, 0.0F, 30.0F, 0.0F, true);
        this.segment = new AdvancedModelBox(this);
        this.segment.setRotationPoint(0.0F, 0.0F, -0.75F);
        this.head.addChild(this.segment);
        this.segment.setTextureOffset(58, 70).addBox(-2.0F, -1.0F, 0.5F, 4.0F, 2.0F, 14.0F, 0.0F, false);
        this.rflipperFront = new AdvancedModelBox(this);
        this.rflipperFront.setRotationPoint(2.0F, 0.0F, 4.5F);
        this.segment.addChild(this.rflipperFront);
        this.rflipperFront.setTextureOffset(74, 68).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperBack = new AdvancedModelBox(this);
        this.rflipperBack.setRotationPoint(2.0F, 0.0F, 11.5F);
        this.segment.addChild(this.rflipperBack);
        this.rflipperBack.setTextureOffset(68, 48).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront = new AdvancedModelBox(this);
        this.lflipperFront.setRotationPoint(-2.0F, 0.0F, 4.5F);
        this.segment.addChild(this.lflipperFront);
        this.lflipperFront.setTextureOffset(68, 42).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack = new AdvancedModelBox(this);
        this.lflipperBack.setRotationPoint(-2.0F, 0.0F, 11.5F);
        this.segment.addChild(this.lflipperBack);
        this.lflipperBack.setTextureOffset(66, 24).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment2 = new AdvancedModelBox(this);
        this.segment2.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment.addChild(this.segment2);
        this.segment2.setTextureOffset(58, 70).addBox(-2.0F, -1.0F, 0.5F, 4.0F, 2.0F, 14.0F, 0.0F, false);
        this.rflipperFront2 = new AdvancedModelBox(this);
        this.rflipperFront2.setRotationPoint(2.0F, 0.0F, 4.5F);
        this.segment2.addChild(this.rflipperFront2);
        this.rflipperFront2.setTextureOffset(66, 18).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperBack2 = new AdvancedModelBox(this);
        this.rflipperBack2.setRotationPoint(2.0F, 0.0F, 11.5F);
        this.segment2.addChild(this.rflipperBack2);
        this.rflipperBack2.setTextureOffset(66, 18).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront2 = new AdvancedModelBox(this);
        this.lflipperFront2.setRotationPoint(-2.0F, 0.0F, 4.5F);
        this.segment2.addChild(this.lflipperFront2);
        this.lflipperFront2.setTextureOffset(66, 6).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack2 = new AdvancedModelBox(this);
        this.lflipperBack2.setRotationPoint(-2.0F, 0.0F, 11.5F);
        this.segment2.addChild(this.lflipperBack2);
        this.lflipperBack2.setTextureOffset(66, 6).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment3 = new AdvancedModelBox(this);
        this.segment3.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment2.addChild(this.segment3);
        this.segment3.setTextureOffset(58, 70).addBox(-2.0F, -1.0F, 0.5F, 4.0F, 2.0F, 14.0F, 0.0F, false);
        this.rflipperFront3 = new AdvancedModelBox(this);
        this.rflipperFront3.setRotationPoint(2.0F, 0.0F, 4.5F);
        this.segment3.addChild(this.rflipperFront3);
        this.rflipperFront3.setTextureOffset(66, 18).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperBack3 = new AdvancedModelBox(this);
        this.rflipperBack3.setRotationPoint(2.0F, 0.0F, 11.5F);
        this.segment3.addChild(this.rflipperBack3);
        this.rflipperBack3.setTextureOffset(66, 18).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront3 = new AdvancedModelBox(this);
        this.lflipperFront3.setRotationPoint(-2.0F, 0.0F, 4.5F);
        this.segment3.addChild(this.lflipperFront3);
        this.lflipperFront3.setTextureOffset(66, 6).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack3 = new AdvancedModelBox(this);
        this.lflipperBack3.setRotationPoint(-2.0F, 0.0F, 11.5F);
        this.segment3.addChild(this.lflipperBack3);
        this.lflipperBack3.setTextureOffset(66, 6).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment4 = new AdvancedModelBox(this);
        this.segment4.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment3.addChild(this.segment4);
        this.segment4.setTextureOffset(58, 70).addBox(-2.0F, -1.0F, 0.5F, 4.0F, 2.0F, 14.0F, 0.0F, false);
        this.rflipperFront4 = new AdvancedModelBox(this);
        this.rflipperFront4.setRotationPoint(2.0F, 0.0F, 4.5F);
        this.segment4.addChild(this.rflipperFront4);
        this.rflipperFront4.setTextureOffset(68, 48).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperBack4 = new AdvancedModelBox(this);
        this.rflipperBack4.setRotationPoint(2.0F, 0.0F, 11.5F);
        this.segment4.addChild(this.rflipperBack4);
        this.rflipperBack4.setTextureOffset(68, 48).addBox(0.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront4 = new AdvancedModelBox(this);
        this.lflipperFront4.setRotationPoint(-2.0F, 0.0F, 4.5F);
        this.segment4.addChild(this.lflipperFront4);
        this.lflipperFront4.setTextureOffset(66, 24).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack4 = new AdvancedModelBox(this);
        this.lflipperBack4.setRotationPoint(-2.0F, 0.0F, 11.5F);
        this.segment4.addChild(this.lflipperBack4);
        this.lflipperBack4.setTextureOffset(66, 24).addBox(-17.0F, 0.0F, -3.0F, 17.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment5 = new AdvancedModelBox(this);
        this.segment5.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment4.addChild(this.segment5);
        this.segment5.setTextureOffset(88, 86).addBox(-1.0F, -0.5F, 0.5F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        this.rflipperBack5 = new AdvancedModelBox(this);
        this.rflipperBack5.setRotationPoint(1.0F, 0.0F, 11.5F);
        this.segment5.addChild(this.rflipperBack5);
        this.rflipperBack5.setTextureOffset(74, 68).addBox(-0.25F, 0.0F, -3.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack5 = new AdvancedModelBox(this);
        this.lflipperBack5.setRotationPoint(-1.0F, 0.0F, 11.5F);
        this.segment5.addChild(this.lflipperBack5);
        this.lflipperBack5.setTextureOffset(69, 42).addBox(-15.75F, 0.0F, -3.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperFront5 = new AdvancedModelBox(this);
        this.rflipperFront5.setRotationPoint(1.0F, 0.0F, 4.5F);
        this.segment5.addChild(this.rflipperFront5);
        this.rflipperFront5.setTextureOffset(74, 68).addBox(-0.25F, 0.0F, -3.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront5 = new AdvancedModelBox(this);
        this.lflipperFront5.setRotationPoint(-1.0F, 0.0F, 4.5F);
        this.segment5.addChild(this.lflipperFront5);
        this.lflipperFront5.setTextureOffset(69, 42).addBox(-15.75F, 0.0F, -3.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment6 = new AdvancedModelBox(this);
        this.segment6.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment5.addChild(this.segment6);
        this.segment6.setTextureOffset(88, 86).addBox(-1.0F, -0.5F, 0.5F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        this.rflipperBack6 = new AdvancedModelBox(this);
        this.rflipperBack6.setRotationPoint(1.0F, 0.0F, 10.0F);
        this.segment6.addChild(this.rflipperBack6);
        this.rflipperBack6.setTextureOffset(88, 80).addBox(-0.25F, 0.0F, -1.5F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack6 = new AdvancedModelBox(this);
        this.lflipperBack6.setRotationPoint(-1.0F, 0.0F, 10.0F);
        this.segment6.addChild(this.lflipperBack6);
        this.lflipperBack6.setTextureOffset(64, 86).addBox(-15.75F, 0.0F, -1.5F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperFront6 = new AdvancedModelBox(this);
        this.rflipperFront6.setRotationPoint(1.0F, 0.0F, 3.0F);
        this.segment6.addChild(this.rflipperFront6);
        this.rflipperFront6.setTextureOffset(32, 86).addBox(-0.25F, 0.0F, -1.5F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront6 = new AdvancedModelBox(this);
        this.lflipperFront6.setRotationPoint(-1.0F, 0.0F, 3.5F);
        this.segment6.addChild(this.lflipperFront6);
        this.lflipperFront6.setTextureOffset(84, 54).addBox(-15.75F, 0.0F, -2.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment7 = new AdvancedModelBox(this);
        this.segment7.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment6.addChild(this.segment7);
        this.segment7.setTextureOffset(88, 86).addBox(-1.0F, -0.5F, 0.5F, 2.0F, 1.0F, 14.0F, 0.0F, false);
        this.rflipperBack7 = new AdvancedModelBox(this);
        this.rflipperBack7.setRotationPoint(1.0F, 0.0F, 10.0F);
        this.segment7.addChild(this.rflipperBack7);
        this.rflipperBack7.setTextureOffset(0, 84).addBox(-0.25F, 0.0F, -1.5F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperBack7 = new AdvancedModelBox(this);
        this.lflipperBack7.setRotationPoint(-1.0F, 0.0F, 10.0F);
        this.segment7.addChild(this.lflipperBack7);
        this.lflipperBack7.setTextureOffset(82, 36).addBox(-15.75F, 0.0F, -1.5F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.rflipperFront7 = new AdvancedModelBox(this);
        this.rflipperFront7.setRotationPoint(1.0F, 0.0F, 3.5F);
        this.segment7.addChild(this.rflipperFront7);
        this.rflipperFront7.setTextureOffset(82, 30).addBox(-0.25F, 0.0F, -2.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.lflipperFront7 = new AdvancedModelBox(this);
        this.lflipperFront7.setRotationPoint(-1.0F, 0.0F, 3.5F);
        this.segment7.addChild(this.lflipperFront7);
        this.lflipperFront7.setTextureOffset(74, 74).addBox(-15.75F, 0.0F, -2.0F, 16.0F, 0.0F, 6.0F, 0.0F, false);
        this.segment8 = new AdvancedModelBox(this);
        this.segment8.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment7.addChild(this.segment8);
        this.segment8.setTextureOffset(20, 30).addBox(-5.0F, 0.0F, 0.5F, 10.0F, 0.0F, 14.0F, 0.0F, false);
        this.segment9 = new AdvancedModelBox(this);
        this.segment9.setRotationPoint(0.0F, 0.0F, 14.0F);
        this.segment8.addChild(this.segment9);
        this.segment9.setTextureOffset(0, 30).addBox(-5.0F, 0.0F, 0.5F, 10.0F, 0.0F, 14.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.head, this.antennae, this.antennae2, this.segment, this.rflipperFront, this.rflipperBack, this.lflipperFront, this.lflipperBack, this.segment2, this.rflipperFront2, this.rflipperBack2, this.lflipperFront2, new AdvancedModelBox[] { this.lflipperBack2, this.segment3, this.rflipperFront3, this.rflipperBack3, this.lflipperFront3, this.lflipperBack3, this.segment4, this.rflipperFront4, this.rflipperBack4, this.lflipperFront4, this.lflipperBack4, this.segment5, this.rflipperBack5, this.lflipperBack5, this.rflipperFront5, this.lflipperFront5, this.segment6, this.rflipperBack6, this.lflipperBack6, this.rflipperFront6, this.lflipperFront6, this.segment7, this.rflipperBack7, this.lflipperBack7, this.rflipperFront7, this.lflipperFront7, this.segment8, this.segment9 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.head);
    }

    public void setupAnim(GossamerWormEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float swimSpeed = 0.5F;
        float swimDegree = 0.5F;
        float squishAmount = entity.getSquishProgress(partialTicks);
        float swimAmount = 1.0F - squishAmount;
        float clampedYaw = netHeadYaw / (180.0F / (float) Math.PI);
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI);
        float fishPitch = entity.getFishPitch(partialTicks);
        this.walk(this.head, 0.1F, 0.15F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.segment, 0.1F, 0.15F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.segment2, 0.1F, 0.05F, false, 1.0F, 0.0F, ageInTicks, swimAmount);
        this.walk(this.segment3, 0.1F, 0.05F, true, 2.0F, 0.0F, ageInTicks, swimAmount);
        this.walk(this.segment4, 0.1F, 0.05F, false, 3.0F, 0.0F, ageInTicks, swimAmount);
        this.walk(this.segment5, 0.1F, 0.05F, true, 4.0F, 0.0F, ageInTicks, swimAmount);
        this.walk(this.segment6, 0.1F, 0.05F, false, 5.0F, 0.0F, ageInTicks, swimAmount);
        this.walk(this.segment7, 0.1F, 0.05F, true, 6.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.antennae, 0.1F, 0.15F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.antennae2, 0.1F, 0.15F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.segment, 0.8F, 0.05F, false, 3.0F, 0.05F, ageInTicks, squishAmount);
        this.walk(this.segment3, 0.8F, 0.15F, false, 2.0F, 0.05F, ageInTicks, squishAmount);
        this.walk(this.segment5, 0.8F, 0.05F, false, 1.0F, 0.05F, ageInTicks, squishAmount);
        this.walk(this.segment7, 0.8F, 0.05F, false, 0.0F, 0.05F, ageInTicks, squishAmount);
        this.swing(this.rflipperFront, swimSpeed, swimDegree, true, 0.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront, swimSpeed, swimDegree, true, 0.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack, swimSpeed, swimDegree, true, 1.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack, swimSpeed, swimDegree, true, 1.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront2, swimSpeed, swimDegree, true, 2.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront2, swimSpeed, swimDegree, true, 2.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack2, swimSpeed, swimDegree, true, 3.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack2, swimSpeed, swimDegree, true, 3.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront3, swimSpeed, swimDegree, true, 4.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront3, swimSpeed, swimDegree, true, 4.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack3, swimSpeed, swimDegree, true, 5.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack3, swimSpeed, swimDegree, true, 5.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront4, swimSpeed, swimDegree, true, 6.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront4, swimSpeed, swimDegree, true, 6.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack4, swimSpeed, swimDegree, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack4, swimSpeed, swimDegree, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront5, swimSpeed, swimDegree, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront5, swimSpeed, swimDegree, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack5, swimSpeed, swimDegree, true, 8.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack5, swimSpeed, swimDegree, true, 8.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront6, swimSpeed, swimDegree, true, 9.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront6, swimSpeed, swimDegree, true, 9.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack6, swimSpeed, swimDegree, true, 10.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack6, swimSpeed, swimDegree, true, 10.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperFront7, swimSpeed, swimDegree, true, 11.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperFront7, swimSpeed, swimDegree, true, 11.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.rflipperBack7, swimSpeed, swimDegree, true, 12.0F, 0.0F, ageInTicks, swimAmount);
        this.swing(this.lflipperBack7, swimSpeed, swimDegree, true, 12.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront, swimSpeed, swimDegree * 0.5F, true, 0.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront, swimSpeed, swimDegree * 0.5F, true, 0.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack, swimSpeed, swimDegree * 0.5F, true, 1.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack, swimSpeed, swimDegree * 0.5F, true, 1.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront2, swimSpeed, swimDegree * 0.5F, true, 2.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront2, swimSpeed, swimDegree * 0.5F, true, 2.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack2, swimSpeed, swimDegree * 0.5F, true, 3.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack2, swimSpeed, swimDegree * 0.5F, true, 3.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront3, swimSpeed, swimDegree * 0.5F, true, 4.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront3, swimSpeed, swimDegree * 0.5F, true, 4.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack3, swimSpeed, swimDegree * 0.5F, true, 5.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack3, swimSpeed, swimDegree * 0.5F, true, 5.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront4, swimSpeed, swimDegree * 0.5F, true, 6.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront4, swimSpeed, swimDegree * 0.5F, true, 6.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack4, swimSpeed, swimDegree * 0.5F, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack4, swimSpeed, swimDegree * 0.5F, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront5, swimSpeed, swimDegree * 0.5F, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront5, swimSpeed, swimDegree * 0.5F, true, 7.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack5, swimSpeed, swimDegree * 0.5F, true, 8.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack5, swimSpeed, swimDegree * 0.5F, true, 8.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront6, swimSpeed, swimDegree * 0.5F, true, 9.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront6, swimSpeed, swimDegree * 0.5F, true, 9.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack6, swimSpeed, swimDegree * 0.5F, true, 10.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack6, swimSpeed, swimDegree * 0.5F, true, 10.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperFront7, swimSpeed, swimDegree * 0.5F, true, 11.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperFront7, swimSpeed, swimDegree * 0.5F, true, 11.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.rflipperBack7, swimSpeed, swimDegree * 0.5F, true, 12.0F, 0.0F, ageInTicks, swimAmount);
        this.flap(this.lflipperBack7, swimSpeed, swimDegree * 0.5F, true, 12.0F, 0.0F, ageInTicks, swimAmount);
        if (!this.straighten) {
            double defaultX = (double) Mth.wrapDegrees(fishPitch);
            double defaultY = (double) Mth.wrapDegrees(entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTicks);
            double tail1X = (double) entity.getTrailTransformation(10, 0, partialTicks) - defaultX;
            double tail1Y = (double) entity.getTrailTransformation(10, 1, partialTicks) - defaultY;
            double tail2X = (double) entity.getTrailTransformation(20, 0, partialTicks) - defaultX - tail1X;
            double tail2Y = (double) entity.getTrailTransformation(20, 1, partialTicks) - defaultY - tail1Y;
            double tail3X = (double) entity.getTrailTransformation(30, 0, partialTicks) - defaultX - tail2X;
            double tail3Y = (double) entity.getTrailTransformation(30, 1, partialTicks) - defaultY - tail2Y;
            double tail4X = (double) entity.getTrailTransformation(40, 0, partialTicks) - defaultX - tail3X;
            double tail4Y = (double) entity.getTrailTransformation(40, 1, partialTicks) - defaultY - tail3Y;
            double tail5X = (double) entity.getTrailTransformation(50, 0, partialTicks) - defaultX - tail4X;
            double tail5Y = (double) entity.getTrailTransformation(50, 1, partialTicks) - defaultY - tail4Y;
            this.head.rotateAngleX = (float) ((double) this.head.rotateAngleX + Math.toRadians((double) fishPitch));
            this.segment2.rotateAngleY = (float) ((double) this.segment2.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail1Y) * 1.0));
            this.segment3.rotateAngleY = (float) ((double) this.segment3.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail2Y) * 0.35F));
            this.segment4.rotateAngleY = (float) ((double) this.segment4.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail2Y) * 0.35F));
            this.segment5.rotateAngleY = (float) ((double) this.segment5.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail3Y) * 0.4F));
            this.segment6.rotateAngleY = (float) ((double) this.segment6.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail4Y) * 0.4F));
            this.segment7.rotateAngleY = (float) ((double) this.segment7.rotateAngleY + Math.toRadians(Mth.wrapDegrees(tail5Y) * 0.4F));
            this.segment2.rotateAngleX = (float) ((double) this.segment2.rotateAngleX + Math.toRadians(tail1X * 1.0));
            this.segment3.rotateAngleX = (float) ((double) this.segment3.rotateAngleX + Math.toRadians(tail2X * 0.5));
            this.segment4.rotateAngleX = (float) ((double) this.segment4.rotateAngleX + Math.toRadians(tail2X * 0.5));
            this.segment5.rotateAngleX = (float) ((double) this.segment5.rotateAngleX + Math.toRadians(tail3X * 0.35F));
            this.segment6.rotateAngleX = (float) ((double) this.segment6.rotateAngleX + Math.toRadians(tail4X * 0.25));
            this.segment7.rotateAngleX = (float) ((double) this.segment7.rotateAngleX + Math.toRadians(tail5X * 0.25));
        }
    }
}