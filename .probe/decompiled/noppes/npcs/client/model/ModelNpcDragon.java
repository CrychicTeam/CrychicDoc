package noppes.npcs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelNpcDragon<T extends Entity> extends EntityModel<T> {

    private NopModelPart head;

    private NopModelPart neck;

    private NopModelPart jaw;

    private NopModelPart body;

    private NopModelPart leftWing;

    private NopModelPart leftWingTip;

    private NopModelPart leftFrontLeg;

    private NopModelPart leftFrontLegTip;

    private NopModelPart leftFrontFoot;

    private NopModelPart leftRearLeg;

    private NopModelPart leftRearLegTip;

    private NopModelPart leftRearFoot;

    private NopModelPart rightWing;

    private NopModelPart rightWingTip;

    private NopModelPart rightFrontLeg;

    private NopModelPart rightFrontLegTip;

    private NopModelPart rightFrontFoot;

    private NopModelPart rightRearLeg;

    private NopModelPart rightRearLegTip;

    private NopModelPart rightRearFoot;

    private float field_40317_s;

    private EntityNpcDragon entitydragon;

    private float animationPos;

    private float animationSpeed;

    public ModelNpcDragon() {
        float f = -16.0F;
        this.head = new NopModelPart(256, 256);
        this.head.addBox("upperlip", -6.0F, -1.0F, -24.0F, 12, 5, 16, 0.0F, 176, 44);
        this.head.addBox("upperhead", -8.0F, -8.0F, -10.0F, 16, 16, 16, 0.0F, 112, 30);
        this.head.mirror = true;
        this.head.addBox("scale", -5.0F, -12.0F, -4.0F, 2, 4, 6, 0.0F, 0, 0);
        this.head.addBox("nostril", -5.0F, -3.0F, -22.0F, 2, 2, 4, 0.0F, 112, 0);
        this.head.mirror = false;
        this.head.addBox("scale", 3.0F, -12.0F, -4.0F, 2, 4, 6, 0.0F, 0, 0);
        this.head.addBox("nostril", 3.0F, -3.0F, -22.0F, 2, 2, 4, 0.0F, 112, 0);
        this.jaw = new NopModelPart(256, 256);
        this.jaw.setPos(0.0F, 4.0F, -8.0F);
        this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16, 0.0F, 176, 65);
        this.head.addChild(this.jaw);
        this.neck = new NopModelPart(256, 256);
        this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10, 0.0F, 192, 104);
        this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6, 0.0F, 48, 0);
        this.body = new NopModelPart(256, 256);
        this.body.setPos(0.0F, 4.0F, 8.0F);
        this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64, 0.0F, 0, 0);
        this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12, 0.0F, 220, 53);
        this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12, 0.0F, 220, 53);
        this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12, 0.0F, 220, 53);
        this.leftWing = new NopModelPart(256, 256);
        this.leftWing.mirror = true;
        this.leftWing.setPos(12.0F, 5.0F, 2.0F);
        this.leftWing.addBox("bone", 0.0F, -4.0F, -4.0F, 56, 8, 8, 0.0F, 112, 88);
        this.leftWing.addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 88);
        this.leftWingTip = new NopModelPart(256, 256);
        this.leftWingTip.mirror = true;
        this.leftWingTip.setPos(56.0F, 0.0F, 0.0F);
        this.leftWingTip.addBox("bone", 0.0F, -2.0F, -2.0F, 56, 4, 4, 0.0F, 112, 136);
        this.leftWingTip.addBox("skin", 0.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 144);
        this.leftWing.addChild(this.leftWingTip);
        this.leftFrontLeg = new NopModelPart(256, 256);
        this.leftFrontLeg.setPos(12.0F, 20.0F, 2.0F);
        this.leftFrontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 0.0F, 112, 104);
        this.leftFrontLegTip = new NopModelPart(256, 256);
        this.leftFrontLegTip.setPos(0.0F, 20.0F, -1.0F);
        this.leftFrontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 0.0F, 226, 138);
        this.leftFrontLeg.addChild(this.leftFrontLegTip);
        this.leftFrontFoot = new NopModelPart(256, 256);
        this.leftFrontFoot.setPos(0.0F, 23.0F, 0.0F);
        this.leftFrontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 0.0F, 144, 104);
        this.leftFrontLegTip.addChild(this.leftFrontFoot);
        this.leftRearLeg = new NopModelPart(256, 256);
        this.leftRearLeg.setPos(16.0F, 16.0F, 42.0F);
        this.leftRearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0.0F, 0, 0);
        this.leftRearLegTip = new NopModelPart(256, 256);
        this.leftRearLegTip.setPos(0.0F, 32.0F, -4.0F);
        this.leftRearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 0.0F, 196, 0);
        this.leftRearLeg.addChild(this.leftRearLegTip);
        this.leftRearFoot = new NopModelPart(256, 256);
        this.leftRearFoot.setPos(0.0F, 31.0F, 4.0F);
        this.leftRearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 0.0F, 112, 0);
        this.leftRearLegTip.addChild(this.leftRearFoot);
        this.rightWing = new NopModelPart(256, 256);
        this.rightWing.setPos(-12.0F, 5.0F, 2.0F);
        this.rightWing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8, 0.0F, 112, 88);
        this.rightWing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 88);
        this.rightWingTip = new NopModelPart(256, 256);
        this.rightWingTip.setPos(-56.0F, 0.0F, 0.0F);
        this.rightWingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4, 0.0F, 112, 136);
        this.rightWingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56, 0.0F, -56, 144);
        this.rightWing.addChild(this.rightWingTip);
        this.rightFrontLeg = new NopModelPart(256, 256);
        this.rightFrontLeg.setPos(-12.0F, 20.0F, 2.0F);
        this.rightFrontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8, 0.0F, 112, 104);
        this.rightFrontLegTip = new NopModelPart(256, 256);
        this.rightFrontLegTip.setPos(0.0F, 20.0F, -1.0F);
        this.rightFrontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6, 0.0F, 226, 138);
        this.rightFrontLeg.addChild(this.rightFrontLegTip);
        this.rightFrontFoot = new NopModelPart(256, 256);
        this.rightFrontFoot.setPos(0.0F, 23.0F, 0.0F);
        this.rightFrontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16, 0.0F, 144, 104);
        this.rightFrontLegTip.addChild(this.rightFrontFoot);
        this.rightRearLeg = new NopModelPart(256, 256);
        this.rightRearLeg.setPos(-16.0F, 16.0F, 42.0F);
        this.rightRearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16, 0.0F, 0, 0);
        this.rightRearLegTip = new NopModelPart(256, 256);
        this.rightRearLegTip.setPos(0.0F, 32.0F, -4.0F);
        this.rightRearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12, 0.0F, 196, 0);
        this.rightRearLeg.addChild(this.rightRearLegTip);
        this.rightRearFoot = new NopModelPart(256, 256);
        this.rightRearFoot.setPos(0.0F, 31.0F, 4.0F);
        this.rightRearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24, 0.0F, 112, 0);
        this.rightRearLegTip.addChild(this.rightRearFoot);
    }

    @Override
    public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    @Override
    public void prepareMobModel(Entity entityliving, float animationPos, float animationSpeed, float f2) {
        this.field_40317_s = f2;
        this.entitydragon = (EntityNpcDragon) entityliving;
        this.animationPos = animationPos;
        this.animationSpeed = animationSpeed;
    }

    @Override
    public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
        mStack.pushPose();
        float f6 = this.entitydragon.prevAnimTime + (this.entitydragon.animTime - this.entitydragon.prevAnimTime) * this.field_40317_s;
        this.jaw.xRot = (float) (Math.sin((double) (f6 * (float) Math.PI * 2.0F)) + 1.0) * 0.2F;
        float f7 = (float) (Math.sin((double) (f6 * (float) Math.PI * 2.0F - 1.0F)) + 1.0);
        f7 = (f7 * f7 * 1.0F + f7 * 2.0F) * 0.05F;
        mStack.translate(0.0F, f7 - 2.0F, -3.0F);
        mStack.mulPose(Axis.XP.rotationDegrees(f7 * 2.0F));
        float f8 = -30.0F;
        float f9 = 22.0F;
        float f10 = 0.0F;
        float f11 = 1.5F;
        double[] ad = this.entitydragon.getMovementOffsets(6, this.field_40317_s);
        float f12 = this.func_40307_a(this.entitydragon.getMovementOffsets(5, this.field_40317_s)[0] - this.entitydragon.getMovementOffsets(10, this.field_40317_s)[0]);
        float f13 = this.func_40307_a(this.entitydragon.getMovementOffsets(5, this.field_40317_s)[0] + (double) (f12 / 2.0F));
        f8 += 2.0F;
        float f14 = 0.0F;
        float f15 = f6 * 3.141593F * 2.0F;
        f8 = 20.0F;
        f9 = -12.0F;
        for (int i = 0; i < 5; i++) {
            double[] ad3 = this.entitydragon.getMovementOffsets(5 - i, this.field_40317_s);
            f14 = (float) Math.cos((double) ((float) i * 0.45F + f15)) * 0.15F;
            this.neck.yRot = this.func_40307_a(ad3[0] - ad[0]) * (float) Math.PI / 180.0F * f11;
            this.neck.xRot = f14 + (float) (ad3[1] - ad[1]) * (float) Math.PI / 180.0F * f11 * 5.0F;
            this.neck.zRot = -this.func_40307_a(ad3[0] - (double) f13) * (float) Math.PI / 180.0F * f11;
            this.neck.y = f8;
            this.neck.z = f9;
            this.neck.x = f10;
            f8 = (float) ((double) f8 + Math.sin((double) this.neck.xRot) * 10.0);
            f9 = (float) ((double) f9 - Math.cos((double) this.neck.yRot) * Math.cos((double) this.neck.xRot) * 10.0);
            f10 = (float) ((double) f10 - Math.sin((double) this.neck.yRot) * Math.cos((double) this.neck.xRot) * 10.0);
            this.neck.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        }
        this.head.y = f8;
        this.head.z = f9;
        this.head.x = f10;
        double[] ad1 = this.entitydragon.getMovementOffsets(0, this.field_40317_s);
        this.head.yRot = this.func_40307_a(ad1[0] - ad[0]) * (float) Math.PI / 180.0F * 1.0F;
        this.head.zRot = -this.func_40307_a(ad1[0] - (double) f13) * (float) Math.PI / 180.0F * 1.0F;
        this.head.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        mStack.pushPose();
        mStack.translate(0.0F, 1.0F, 0.0F);
        if (this.entitydragon.m_20096_()) {
            mStack.mulPose(Axis.ZP.rotationDegrees(-f12 * f11 * 0.3F));
        } else {
            mStack.mulPose(Axis.ZP.rotationDegrees(-f12 * f11 * 1.0F));
        }
        mStack.translate(0.0F, -1.18F, 0.0F);
        this.body.zRot = 0.0F;
        this.body.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        if (this.entitydragon.m_20096_()) {
            this.leftWing.xRot = 0.25F;
            this.leftWing.yRot = -0.95F;
            this.leftWing.zRot = 0.5F;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.leftWingTip.zRot = 0.4F;
            this.rightWingTip.zRot = -0.4F;
            this.leftFrontLeg.xRot = this.rightFrontLeg.xRot = Mth.cos((float) ((double) (this.animationPos * 0.6662F) + Math.PI)) * 0.6F * this.animationSpeed + 0.45F + f7 * 0.5F;
            this.leftRearLeg.xRot = this.rightRearLeg.xRot = Mth.cos(this.animationPos * 0.6662F + 0.0F) * 0.6F * this.animationSpeed + 0.75F + f7 * 0.5F;
            this.leftFrontLegTip.xRot = this.rightFrontLegTip.xRot = -1.3F - f7 * 1.2F;
            this.leftFrontFoot.xRot = this.rightFrontFoot.xRot = 0.85F + f7 * 0.5F;
            this.leftRearLegTip.xRot = this.rightRearLegTip.xRot = -1.6F - f7 * 0.8F;
            this.leftRearLegTip.y = this.rightRearLegTip.y = 20.0F;
            this.leftRearLegTip.z = this.rightRearLegTip.z = 2.0F;
            this.leftRearFoot.xRot = this.rightRearFoot.xRot = 0.85F + f7 * 0.2F;
            this.leftFrontLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightFrontLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.leftRearLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightRearLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.leftWing.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightWing.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        } else {
            float f16 = f6 * (float) Math.PI * 2.0F;
            this.leftWing.xRot = 0.125F - (float) Math.cos((double) f16) * 0.2F;
            this.leftWing.yRot = -0.25F;
            this.leftWing.zRot = -((float) (Math.sin((double) f16) + 0.125)) * 0.8F;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.leftWingTip.zRot = (float) (Math.sin((double) (f16 + 2.0F)) + 0.5) * 0.75F;
            this.rightWingTip.zRot = -this.leftWingTip.zRot;
            this.leftRearLegTip.y = this.rightRearLegTip.y = 32.0F;
            this.leftRearLegTip.z = this.rightRearLegTip.z = -2.0F;
            this.leftRearLeg.xRot = this.rightRearLeg.xRot = 1.0F + f7 * 0.1F;
            this.leftRearLegTip.xRot = this.rightRearLegTip.xRot = 0.5F + f7 * 0.1F;
            this.leftRearFoot.xRot = this.rightRearFoot.xRot = 0.75F + f7 * 0.1F;
            this.leftFrontLeg.xRot = this.rightFrontLeg.xRot = 1.3F + f7 * 0.1F;
            this.leftFrontLegTip.xRot = this.rightFrontLegTip.xRot = -0.5F - f7 * 0.1F;
            this.leftFrontFoot.xRot = this.rightFrontFoot.xRot = 0.75F + f7 * 0.1F;
            this.leftWing.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightWing.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.leftFrontLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightFrontLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.leftRearLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
            this.rightRearLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        }
        mStack.popPose();
        f14 = -((float) Math.sin((double) (f6 * 3.141593F * 2.0F))) * 0.0F;
        f15 = f6 * (float) Math.PI * 2.0F;
        f8 = 10.0F;
        f9 = 60.0F;
        f10 = 0.0F;
        ad = this.entitydragon.getMovementOffsets(11, this.field_40317_s);
        for (int k = 0; k < 12; k++) {
            double[] ad2 = this.entitydragon.getMovementOffsets(12 + k, this.field_40317_s);
            f14 = (float) ((double) f14 + Math.sin((double) ((float) k * 0.45F + f15)) * 0.05F);
            this.neck.yRot = (this.func_40307_a(ad2[0] - ad[0]) * f11 + 180.0F) * (float) Math.PI / 180.0F;
            this.neck.xRot = f14 + (float) (ad2[1] - ad[1]) * (float) Math.PI / 180.0F * f11 * 5.0F;
            this.neck.zRot = this.func_40307_a(ad2[0] - (double) f13) * (float) Math.PI / 180.0F * f11;
            this.neck.y = f8;
            this.neck.z = f9;
            this.neck.x = f10;
            f8 = (float) ((double) f8 + Math.sin((double) this.neck.xRot) * 10.0);
            f9 = (float) ((double) f9 - Math.cos((double) this.neck.yRot) * Math.cos((double) this.neck.xRot) * 10.0);
            f10 = (float) ((double) f10 - Math.sin((double) this.neck.yRot) * Math.cos((double) this.neck.xRot) * 10.0);
            this.neck.render(mStack, iVertex, lightmapUV, packedOverlayIn);
        }
        mStack.popPose();
    }

    private float func_40307_a(double d) {
        while (d >= 180.0) {
            d -= 360.0;
        }
        while (d < -180.0) {
            d += 360.0;
        }
        return (float) d;
    }
}