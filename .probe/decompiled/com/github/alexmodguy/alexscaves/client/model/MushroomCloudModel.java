package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class MushroomCloudModel extends AdvancedEntityModel {

    private final AdvancedModelBox mushroom_cloud;

    private final AdvancedModelBox lightBall;

    private final AdvancedModelBox lowerCloud;

    private final AdvancedModelBox lowerCloud_planes;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox plume;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox plumeBlast;

    private final AdvancedModelBox plumeRing;

    private final AdvancedModelBox upperCloud;

    private final AdvancedModelBox upperCloud1;

    private final AdvancedModelBox upperCloud_planes;

    private final AdvancedModelBox cube_r7;

    private final AdvancedModelBox cube_r8;

    private final AdvancedModelBox cube_r9;

    private final AdvancedModelBox cube_r10;

    private final AdvancedModelBox rings;

    private final AdvancedModelBox upperRing;

    private final AdvancedModelBox lowerRing;

    public MushroomCloudModel() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.mushroom_cloud = new AdvancedModelBox(this);
        this.mushroom_cloud.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.lightBall = new AdvancedModelBox(this);
        this.lightBall.setRotationPoint(0.0F, -19.0F, 0.0F);
        this.mushroom_cloud.addChild(this.lightBall);
        this.lightBall.setTextureOffset(300, 0).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, false);
        this.lowerCloud = new AdvancedModelBox(this);
        this.lowerCloud.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.lightBall.addChild(this.lowerCloud);
        this.lowerCloud.setTextureOffset(0, 0).addBox(-50.0F, -9.0F, -50.0F, 100.0F, 16.0F, 100.0F, 0.0F, false);
        this.lowerCloud_planes = new AdvancedModelBox(this);
        this.lowerCloud_planes.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lowerCloud.addChild(this.lowerCloud_planes);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(-16.0F, -20.0F, 16.0F);
        this.lowerCloud_planes.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.0F, 0.7854F, 0.0F);
        this.cube_r1.setTextureOffset(142, 342).addBox(-66.0F, -13.0F, 0.0F, 66.0F, 40.0F, 0.0F, 0.0F, true);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(16.0F, -20.0F, -16.0F);
        this.lowerCloud_planes.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.0F, 0.7854F, 0.0F);
        this.cube_r2.setTextureOffset(142, 342).addBox(0.0F, -13.0F, 0.0F, 66.0F, 40.0F, 0.0F, 0.0F, false);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(16.0F, -20.0F, 16.0F);
        this.lowerCloud_planes.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, 0.0F, -0.7854F, 0.0F);
        this.cube_r3.setTextureOffset(142, 342).addBox(0.0F, -13.0F, 0.0F, 66.0F, 40.0F, 0.0F, 0.0F, false);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(-16.0F, -20.0F, -16.0F);
        this.lowerCloud_planes.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, 0.0F, -0.7854F, 0.0F);
        this.cube_r4.setTextureOffset(142, 342).addBox(-66.0F, -13.0F, 0.0F, 66.0F, 40.0F, 0.0F, 0.0F, true);
        this.plume = new AdvancedModelBox(this);
        this.plume.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.lightBall.addChild(this.plume);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(0.0F, -44.8333F, 0.0F);
        this.plume.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 0.0F, -0.7854F, 0.0F);
        this.cube_r5.setTextureOffset(256, 220).addBox(-32.0F, -61.0F, 0.0F, 64.0F, 122.0F, 0.0F, 0.0F, false);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(0.0F, -44.8333F, 0.0F);
        this.plume.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 0.0F, 0.7854F, 0.0F);
        this.cube_r6.setTextureOffset(256, 220).addBox(-32.0F, -61.0F, 0.0F, 64.0F, 122.0F, 0.0F, 0.0F, false);
        this.plumeBlast = new AdvancedModelBox(this);
        this.plumeBlast.setRotationPoint(0.0F, -80.8333F, 0.0F);
        this.plume.addChild(this.plumeBlast);
        this.plumeBlast.setTextureOffset(240, 116).addBox(-18.0F, 0.0F, -18.0F, 36.0F, 29.0F, 36.0F, 0.0F, false);
        this.plumeRing = new AdvancedModelBox(this);
        this.plumeRing.setRotationPoint(0.0F, -35.8333F, 0.0F);
        this.plume.addChild(this.plumeRing);
        this.plumeRing.setTextureOffset(296, 440).addBox(-36.0F, 0.0F, -36.0F, 72.0F, 0.0F, 72.0F, 0.0F, false);
        this.upperCloud = new AdvancedModelBox(this);
        this.upperCloud.setRotationPoint(0.0F, -80.8333F, 0.0F);
        this.plume.addChild(this.upperCloud);
        this.upperCloud.setTextureOffset(0, 116).addBox(-40.0F, -24.0F, -40.0F, 80.0F, 24.0F, 80.0F, 0.0F, false);
        this.upperCloud1 = new AdvancedModelBox(this);
        this.upperCloud1.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.upperCloud.addChild(this.upperCloud1);
        this.upperCloud1.setTextureOffset(0, 220).addBox(-32.0F, -15.0F, -32.0F, 64.0F, 15.0F, 64.0F, 0.0F, false);
        this.upperCloud_planes = new AdvancedModelBox(this);
        this.upperCloud_planes.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.upperCloud.addChild(this.upperCloud_planes);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(16.0F, 112.0F, -16.0F);
        this.upperCloud_planes.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.0F, 0.7854F, 0.0F);
        this.cube_r7.setTextureOffset(0, 299).addBox(-23.0F, -152.0F, 0.0F, 71.0F, 69.0F, 0.0F, 0.0F, false);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(16.0F, 112.0F, 16.0F);
        this.upperCloud_planes.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, -0.7854F, 0.0F);
        this.cube_r8.setTextureOffset(0, 299).addBox(-23.0F, -152.0F, 0.0F, 71.0F, 69.0F, 0.0F, 0.0F, false);
        this.cube_r9 = new AdvancedModelBox(this);
        this.cube_r9.setRotationPoint(-16.0F, 112.0F, 16.0F);
        this.upperCloud_planes.addChild(this.cube_r9);
        this.setRotateAngle(this.cube_r9, 0.0F, 0.7854F, 0.0F);
        this.cube_r9.setTextureOffset(0, 299).addBox(-48.0F, -152.0F, 0.0F, 71.0F, 69.0F, 0.0F, 0.0F, true);
        this.cube_r10 = new AdvancedModelBox(this);
        this.cube_r10.setRotationPoint(-16.0F, 112.0F, -16.0F);
        this.upperCloud_planes.addChild(this.cube_r10);
        this.setRotateAngle(this.cube_r10, 0.0F, -0.7854F, 0.0F);
        this.cube_r10.setTextureOffset(0, 299).addBox(-48.0F, -152.0F, 0.0F, 71.0F, 69.0F, 0.0F, 0.0F, true);
        this.rings = new AdvancedModelBox(this);
        this.rings.setRotationPoint(0.0F, -55.0F, 0.0F);
        this.upperCloud.addChild(this.rings);
        this.upperRing = new AdvancedModelBox(this);
        this.upperRing.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.rings.addChild(this.upperRing);
        this.upperRing.setTextureOffset(296, 440).addBox(-36.0F, 0.0F, -36.0F, 72.0F, 0.0F, 72.0F, 0.0F, false);
        this.lowerRing = new AdvancedModelBox(this);
        this.lowerRing.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.rings.addChild(this.lowerRing);
        this.lowerRing.setTextureOffset(296, 440).addBox(-36.0F, 0.0F, -36.0F, 72.0F, 0.0F, 72.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.mushroom_cloud);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.rings, this.mushroom_cloud, this.lowerCloud, this.lowerCloud_planes, this.lightBall, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, new AdvancedModelBox[] { this.cube_r8, this.cube_r9, this.cube_r10, this.plume, this.plumeBlast, this.plumeRing, this.upperCloud, this.upperCloud_planes, this.upperCloud1, this.upperRing, this.lowerRing });
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    public void hideFireball(boolean fireball) {
        this.plume.showModel = fireball;
        this.lowerCloud.showModel = fireball;
        this.upperCloud.showModel = fireball;
    }

    public void animateParticle(float age, float life, float partialTicks) {
        this.resetToDefaultPose();
        float lerpedAge = age + partialTicks;
        float baseExpand1 = life + life * life;
        float upperExpand = this.offset(life, 0.3F, 0.4F, 1.5F);
        float plumeRingExpand = this.offset(life, 0.0F, 0.0F, 0.5F) * 45.0F;
        float lowerRingExpand = this.offset(life, 0.0F, 0.1F, 0.65F) * 20.0F;
        float upperRingExpand = this.offset(life, 0.0F, 0.2F, 0.65F) * 15.0F;
        this.plume.scaleChildren = true;
        this.upperCloud.scaleChildren = true;
        this.lowerCloud.scaleChildren = true;
        this.plume.setScale(1.0F, life * 1.5F, 1.0F);
        this.upperCloud.setScale(upperExpand, 1.0F, upperExpand);
        this.plumeRing.setScale(plumeRingExpand, 1.0F, plumeRingExpand);
        this.lowerRing.setScale(lowerRingExpand, 1.0F, lowerRingExpand);
        this.upperRing.setScale(upperRingExpand, 1.0F, upperRingExpand);
        this.lowerCloud.setScale(baseExpand1, baseExpand1, baseExpand1);
        this.plumeRing.rotateAngleY = (float) ((double) this.plumeRing.rotateAngleY + (double) lerpedAge * 0.1);
        this.lowerRing.rotateAngleY = (float) ((double) this.lowerRing.rotateAngleY - (double) lerpedAge * 0.2);
        this.upperRing.rotateAngleY = (float) ((double) this.upperRing.rotateAngleY + (double) lerpedAge * 0.1);
    }

    public float offset(float life, float forwards, float min, float max) {
        return Mth.clamp(life + forwards, min, max);
    }
}