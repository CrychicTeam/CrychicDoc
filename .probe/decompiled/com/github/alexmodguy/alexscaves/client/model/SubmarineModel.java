package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class SubmarineModel extends AdvancedEntityModel<SubmarineEntity> {

    private final AdvancedModelBox hull;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox leftpropeller;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rightpropeller;

    private final AdvancedModelBox llever;

    private final AdvancedModelBox rlever;

    private final AdvancedModelBox watermask;

    private final AdvancedModelBox motor;

    private final AdvancedModelBox backpropeller;

    private final AdvancedModelBox periscope;

    private final AdvancedModelBox seat;

    public SubmarineModel() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.hull = new AdvancedModelBox(this);
        this.hull.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.hull.setTextureOffset(103, 0).addBox(-8.0F, -26.0F, 7.0F, 16.0F, 6.0F, 16.0F, 0.0F, false);
        this.hull.setTextureOffset(0, 66).addBox(-20.0F, -22.0F, -7.0F, 40.0F, 46.0F, 32.0F, 0.0F, false);
        this.hull.setTextureOffset(152, 161).addBox(13.0F, 8.0F, -30.0F, 7.0F, 16.0F, 23.0F, 0.0F, false);
        this.hull.setTextureOffset(92, 161).addBox(-20.0F, 8.0F, -30.0F, 7.0F, 16.0F, 23.0F, 0.0F, false);
        this.hull.setTextureOffset(154, 0).addBox(-18.0F, -20.0F, -35.0F, 36.0F, 28.0F, 28.0F, 0.0F, false);
        this.hull.setTextureOffset(112, 66).addBox(-20.0F, 8.0F, -37.0F, 40.0F, 16.0F, 7.0F, 0.0F, false);
        this.hull.setTextureOffset(251, 189).addBox(-6.0F, 8.0F, -30.0F, 12.0F, 16.0F, 6.0F, 0.0F, false);
        this.hull.setTextureOffset(0, 0).addBox(-13.0F, 15.0F, -30.0F, 26.0F, 15.0F, 51.0F, 0.0F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(22.5F, 25.5F, -7.5F);
        this.hull.addChild(this.rarm);
        this.rarm.setTextureOffset(255, 91).addBox(-9.5F, -9.5F, -43.5F, 19.0F, 19.0F, 51.0F, 0.0F, false);
        this.leftpropeller = new AdvancedModelBox(this);
        this.leftpropeller.setRotationPoint(0.0F, 0.0F, -43.5F);
        this.rarm.addChild(this.leftpropeller);
        this.leftpropeller.setTextureOffset(189, 165).addBox(-9.5F, -9.5F, -3.0F, 19.0F, 19.0F, 0.0F, 0.0F, false);
        this.leftpropeller.setTextureOffset(103, 22).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(-22.5F, 25.5F, -25.5F);
        this.hull.addChild(this.larm);
        this.larm.setTextureOffset(255, 91).addBox(-9.5F, -9.5F, -25.5F, 19.0F, 19.0F, 51.0F, 0.0F, true);
        this.rightpropeller = new AdvancedModelBox(this);
        this.rightpropeller.setRotationPoint(0.0F, 0.0F, -25.5F);
        this.larm.addChild(this.rightpropeller);
        this.rightpropeller.setTextureOffset(189, 165).addBox(-9.5F, -9.5F, -3.0F, 19.0F, 19.0F, 0.0F, 0.0F, false);
        this.rightpropeller.setTextureOffset(103, 22).addBox(-2.5F, -2.5F, -6.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
        this.llever = new AdvancedModelBox(this);
        this.llever.setRotationPoint(-7.0F, 15.25F, -16.0F);
        this.hull.addChild(this.llever);
        this.llever.setTextureOffset(265, 72).addBox(-1.0F, -11.25F, -1.0F, 2.0F, 16.0F, 2.0F, 0.0F, true);
        this.rlever = new AdvancedModelBox(this);
        this.rlever.setRotationPoint(7.0F, 15.0F, -16.0F);
        this.hull.addChild(this.rlever);
        this.rlever.setTextureOffset(265, 72).addBox(-1.0F, -11.0F, -1.0F, 2.0F, 16.0F, 2.0F, 0.0F, true);
        this.watermask = new AdvancedModelBox(this);
        this.watermask.setRotationPoint(0.0F, 5.0F, 0.0F);
        this.hull.addChild(this.watermask);
        this.watermask.setTextureOffset(0, 214).addBox(-18.0F, 0.0F, -35.0F, 36.0F, 52.0F, 28.0F, 0.0F, false);
        this.motor = new AdvancedModelBox(this);
        this.motor.setRotationPoint(0.0F, 1.0F, 25.0F);
        this.hull.addChild(this.motor);
        this.motor.setTextureOffset(0, 0).addBox(-9.0F, -8.0F, 0.0F, 18.0F, 18.0F, 7.0F, 0.0F, false);
        this.motor.setTextureOffset(153, 211).addBox(-14.0F, -13.0F, 7.0F, 28.0F, 28.0F, 18.0F, 0.0F, false);
        this.backpropeller = new AdvancedModelBox(this);
        this.backpropeller.setRotationPoint(0.0F, 1.0F, 7.0F);
        this.motor.addChild(this.backpropeller);
        this.backpropeller.setTextureOffset(0, 25).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 12.0F, 0.0F, false);
        this.backpropeller.setTextureOffset(0, 188).addBox(-13.0F, -13.0F, 9.0F, 26.0F, 26.0F, 0.0F, 0.0F, false);
        this.periscope = new AdvancedModelBox(this);
        this.periscope.setRotationPoint(0.0F, -22.0F, 3.0F);
        this.hull.addChild(this.periscope);
        this.periscope.setTextureOffset(0, 66).addBox(-3.0F, -20.0F, -3.0F, 6.0F, 20.0F, 6.0F, 0.0F, false);
        this.periscope.setTextureOffset(199, 59).addBox(-3.0F, -20.0F, -10.0F, 6.0F, 6.0F, 7.0F, 0.25F, false);
        this.periscope.setTextureOffset(103, 35).addBox(-3.0F, -20.0F, -10.0F, 6.0F, 6.0F, 7.0F, 0.0F, false);
        this.seat = new AdvancedModelBox(this);
        this.seat.setRotationPoint(0.0F, 13.5F, -11.0F);
        this.hull.addChild(this.seat);
        this.seat.setTextureOffset(165, 93).addBox(-4.0F, -1.5F, -4.0F, 8.0F, 3.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.hull);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.hull, this.rarm, this.larm, this.seat, this.leftpropeller, this.rightpropeller, this.llever, this.rlever, this.watermask, this.motor, this.backpropeller, this.periscope, new AdvancedModelBox[0]);
    }

    public AdvancedModelBox getWaterMask() {
        return this.watermask;
    }

    public void setupAnim(SubmarineEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float leftPropellerRot = Mth.wrapDegrees(entity.getLeftPropellerRot(partialTicks));
        float rightPropellerRot = Mth.wrapDegrees(entity.getRightPropellerRot(partialTicks));
        float backPropellerRot = Mth.wrapDegrees(entity.getBackPropellerRot(partialTicks));
        float shake = Math.max((float) entity.shakeTime - partialTicks, 0.0F) / 10.0F;
        Entity controllingPlayer = entity.m_146895_();
        this.rightpropeller.rotateAngleZ = (float) ((double) this.rightpropeller.rotateAngleZ + Math.toRadians((double) leftPropellerRot));
        this.leftpropeller.rotateAngleZ = (float) ((double) this.leftpropeller.rotateAngleZ + Math.toRadians((double) rightPropellerRot));
        this.backpropeller.rotateAngleZ = (float) ((double) this.backpropeller.rotateAngleZ + Math.toRadians((double) backPropellerRot));
        if (controllingPlayer instanceof LivingEntity living) {
            float subYaw = 180.0F - entity.m_5675_(partialTicks);
            float headYaw = 180.0F + living.yHeadRotO + (living.getYHeadRot() - living.yHeadRotO) * partialTicks;
            this.periscope.rotateAngleY = (float) ((double) this.periscope.rotateAngleY + Math.toRadians((double) (subYaw + headYaw)));
        }
        this.hull.rotateAngleX = (float) ((double) this.hull.rotateAngleX + Math.sin((double) (ageInTicks * 0.7F + 1.0F)) * (double) shake * 0.05F);
        this.hull.rotateAngleZ = (float) ((double) this.hull.rotateAngleZ + Math.sin((double) (ageInTicks * 0.7F)) * (double) shake * 0.1F);
        if (entity.getDamageLevel() < 4) {
            this.rarm.showModel = true;
        } else {
            this.hull.rotateAngleZ = (float) ((double) this.hull.rotateAngleZ + Math.toRadians(10.0));
            this.rarm.showModel = false;
        }
    }

    public void setupWaterMask(SubmarineEntity entity, float partialTicks) {
        float xRot = (float) Math.toRadians((double) (-entity.m_5686_(partialTicks)));
        Vec3 vec3 = new Vec3(0.0, (double) entity.getWaterHeight(), 0.0).xRot(xRot);
        this.watermask.rotateAngleX = this.hull.rotateAngleX;
        this.watermask.rotateAngleZ = this.hull.rotateAngleZ;
        this.watermask.rotationPointY -= 7.0F;
        this.watermask.setScale(1.0F, 0.25F, 1.0F);
    }
}