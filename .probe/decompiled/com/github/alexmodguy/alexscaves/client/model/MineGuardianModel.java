package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MineGuardianModel extends AdvancedEntityModel<MineGuardianEntity> {

    private final AdvancedModelBox head;

    private final AdvancedModelBox head_r1;

    private final AdvancedModelBox eye;

    private final AdvancedModelBox spikepart0;

    private final AdvancedModelBox spikepart1;

    private final AdvancedModelBox spikepart2;

    private final AdvancedModelBox spikepart3;

    private final AdvancedModelBox spikepart4;

    private final AdvancedModelBox spikepart5;

    private final AdvancedModelBox spikepart6;

    private final AdvancedModelBox spikepart7;

    private final AdvancedModelBox spikepart8;

    private final AdvancedModelBox spikepart9;

    private final AdvancedModelBox spikepart10;

    private final AdvancedModelBox spikepart11;

    private final AdvancedModelBox spike0;

    private final AdvancedModelBox spike1;

    private final AdvancedModelBox spike2;

    private final AdvancedModelBox spike3;

    private final AdvancedModelBox spike4;

    private final AdvancedModelBox spike5;

    private final AdvancedModelBox spike6;

    private final AdvancedModelBox spike7;

    private final AdvancedModelBox spike8;

    private final AdvancedModelBox spike9;

    private final AdvancedModelBox spike10;

    private final AdvancedModelBox spike11;

    public MineGuardianModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.head.setTextureOffset(0, 0).addBox(-6.0F, -14.0F, -8.0F, 12.0F, 12.0F, 16.0F, 0.0F, false);
        this.head.setTextureOffset(0, 28).addBox(-8.0F, -14.0F, -6.0F, 2.0F, 12.0F, 12.0F, 0.0F, false);
        this.head.setTextureOffset(0, 28).addBox(6.0F, -14.0F, -6.0F, 2.0F, 12.0F, 12.0F, 0.0F, true);
        this.head.setTextureOffset(16, 40).addBox(-6.0F, -16.0F, -6.0F, 12.0F, 2.0F, 12.0F, 0.0F, true);
        this.head_r1 = new AdvancedModelBox(this);
        this.head_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.head_r1);
        this.setRotateAngle(this.head_r1, 0.0F, 0.0F, -3.1416F);
        this.head_r1.setTextureOffset(16, 38).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, true);
        this.head_r1.setTextureOffset(16, 40).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 2.0F, 12.0F, 0.0F, true);
        this.eye = new AdvancedModelBox(this);
        this.eye.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.eye);
        this.eye.setTextureOffset(8, 0).addBox(-1.0F, 15.0F, -8.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        this.spikepart0 = new AdvancedModelBox(this);
        this.spikepart0.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart0);
        this.setRotateAngle(this.spikepart0, 0.0F, 0.0F, 0.7854F);
        this.spikepart1 = new AdvancedModelBox(this);
        this.spikepart1.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart1);
        this.setRotateAngle(this.spikepart1, 0.0F, 0.0F, -0.7854F);
        this.spikepart2 = new AdvancedModelBox(this);
        this.spikepart2.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart2);
        this.setRotateAngle(this.spikepart2, 0.7854F, 0.0F, 0.0F);
        this.spikepart3 = new AdvancedModelBox(this);
        this.spikepart3.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart3);
        this.setRotateAngle(this.spikepart3, -0.7854F, 0.0F, 0.0F);
        this.spikepart4 = new AdvancedModelBox(this);
        this.spikepart4.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart4);
        this.setRotateAngle(this.spikepart4, 0.0F, 0.0F, 2.3562F);
        this.spikepart5 = new AdvancedModelBox(this);
        this.spikepart5.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart5);
        this.setRotateAngle(this.spikepart5, 0.0F, 0.0F, -2.3562F);
        this.spikepart6 = new AdvancedModelBox(this);
        this.spikepart6.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart6);
        this.setRotateAngle(this.spikepart6, 2.3562F, 0.0F, 0.0F);
        this.spikepart7 = new AdvancedModelBox(this);
        this.spikepart7.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart7);
        this.setRotateAngle(this.spikepart7, -2.3562F, 0.0F, 0.0F);
        this.spikepart8 = new AdvancedModelBox(this);
        this.spikepart8.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart8);
        this.setRotateAngle(this.spikepart8, 1.5708F, -0.7854F, 0.0F);
        this.spikepart9 = new AdvancedModelBox(this);
        this.spikepart9.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart9);
        this.setRotateAngle(this.spikepart9, 1.5708F, 0.7854F, 0.0F);
        this.spikepart10 = new AdvancedModelBox(this);
        this.spikepart10.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart10);
        this.setRotateAngle(this.spikepart10, 1.5708F, -2.3562F, 0.0F);
        this.spikepart11 = new AdvancedModelBox(this);
        this.spikepart11.setRotationPoint(0.0F, -24.0F, 0.0F);
        this.head.addChild(this.spikepart11);
        this.setRotateAngle(this.spikepart11, 1.5708F, 2.3562F, 0.0F);
        this.spike0 = new AdvancedModelBox(this);
        this.spikepart0.addChild(this.spike0);
        this.spike0.setTextureOffset(0, 0).addBox(10.25F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike1 = new AdvancedModelBox(this);
        this.spikepart1.addChild(this.spike1);
        this.spike1.setTextureOffset(0, 0).addBox(-12.25F, -4.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike2 = new AdvancedModelBox(this);
        this.spikepart2.addChild(this.spike2);
        this.spike2.setTextureOffset(0, 0).addBox(-1.0F, -4.5F, -12.25F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike3 = new AdvancedModelBox(this);
        this.spikepart3.addChild(this.spike3);
        this.spike3.setTextureOffset(0, 0).addBox(-1.0F, -4.5F, 10.5F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike4 = new AdvancedModelBox(this);
        this.spikepart4.addChild(this.spike4);
        this.spike4.setTextureOffset(0, 0).addBox(10.25F, -27.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike5 = new AdvancedModelBox(this);
        this.spikepart5.addChild(this.spike5);
        this.spike5.setTextureOffset(0, 0).addBox(-12.25F, -27.5F, -1.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike6 = new AdvancedModelBox(this);
        this.spikepart6.addChild(this.spike6);
        this.spike6.setTextureOffset(0, 0).addBox(-1.0F, -28.5F, -12.25F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike7 = new AdvancedModelBox(this);
        this.spikepart7.addChild(this.spike7);
        this.spike7.setTextureOffset(0, 0).addBox(-1.0F, -27.5F, 10.25F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike8 = new AdvancedModelBox(this);
        this.spikepart8.addChild(this.spike8);
        this.spike8.setTextureOffset(0, 0).addBox(-1.0F, -17.5F, -17.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike9 = new AdvancedModelBox(this);
        this.spikepart9.addChild(this.spike9);
        this.spike9.setTextureOffset(0, 0).addBox(-1.0F, -17.5F, -17.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike10 = new AdvancedModelBox(this);
        this.spikepart10.addChild(this.spike10);
        this.spike10.setTextureOffset(0, 0).addBox(-1.0F, -17.5F, -17.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.spike11 = new AdvancedModelBox(this);
        this.spikepart11.addChild(this.spike11);
        this.spike11.setTextureOffset(0, 0).addBox(-1.0F, -17.5F, -17.0F, 2.0F, 9.0F, 2.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.head, this.head_r1, this.eye, this.spikepart0, this.spikepart1, this.spikepart2, this.spikepart3, this.spikepart4, this.spikepart5, this.spikepart6, this.spikepart7, this.spikepart8, new AdvancedModelBox[] { this.spikepart9, this.spikepart10, this.spikepart11, this.spike0, this.spike1, this.spike2, this.spike3, this.spike4, this.spike5, this.spike6, this.spike7, this.spike8, this.spike9, this.spike10, this.spike11 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.head);
    }

    public void setupAnim(MineGuardianEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float explodeProgress = entity.getExplodeProgress(partialTicks);
        float scanProgress = entity.getScanProgress(partialTicks);
        this.head.setScale(1.0F + explodeProgress * 0.15F, 1.0F + explodeProgress * 0.15F, 1.0F + explodeProgress * 0.15F);
        this.flap(this.head, 3.0F, 0.3F, true, 1.0F, 0.0F, ageInTicks, explodeProgress);
        this.spike0.rotationPointY = this.spike0.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 0.0F, 1.0F, false) + 2.0F;
        this.spike1.rotationPointY = this.spike1.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 1.0F, 1.0F, false) + 2.0F;
        this.spike2.rotationPointY = this.spike2.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 2.0F, 1.0F, false) + 2.0F;
        this.spike3.rotationPointY = this.spike3.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 3.0F, 1.0F, false) + 2.0F;
        this.spike4.rotationPointY = this.spike4.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 4.0F, 1.0F, false) + 2.0F;
        this.spike5.rotationPointY = this.spike5.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 5.0F, 1.0F, false) + 2.0F;
        this.spike6.rotationPointY = this.spike6.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 6.0F, 1.0F, false) + 2.0F;
        this.spike7.rotationPointY = this.spike7.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 7.0F, 1.0F, false) + 2.0F;
        this.spike8.rotationPointY = this.spike8.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 8.0F, 1.0F, false) + 2.0F;
        this.spike9.rotationPointY = this.spike9.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 9.0F, 1.0F, false) + 2.0F;
        this.spike10.rotationPointY = this.spike10.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 10.0F, 1.0F, false) + 2.0F;
        this.spike11.rotationPointY = this.spike11.rotationPointY + ACMath.walkValue(ageInTicks, 1.0F, 0.1F + explodeProgress, 11.0F, 1.0F, false) + 2.0F;
        Entity look = Minecraft.getInstance().getCameraEntity();
        if (look != null) {
            Vec3 vector3d = look.getEyePosition(0.0F);
            Vec3 vector3d1 = entity.m_20299_(0.0F);
            Vec3 vector3d2 = entity.m_20252_(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(vector3d1.x - vector3d.x, 0.0, vector3d1.z - vector3d.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            double d2 = (double) (Mth.sqrt((float) Math.abs(d1)) * (float) Math.signum(d1));
            this.eye.rotationPointX = (float) ((double) this.eye.rotationPointX + (d2 * 2.0 - (double) this.head.rotateAngleZ) * (double) (1.0F - scanProgress));
        }
        this.eye.rotationPointX = (float) ((double) this.eye.rotationPointX + (double) scanProgress * Math.sin((double) (ageInTicks * 0.1F)) * 3.0);
    }

    public void translateToEye(PoseStack stack) {
        this.head.translateAndRotate(stack);
        this.eye.translateAndRotate(stack);
    }
}