package noppes.npcs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelNPCGolem extends EntityModel {

    public NopModelPart head;

    public NopModelPart hat;

    public NopModelPart body;

    public NopModelPart rightArm;

    public NopModelPart leftArm;

    public NopModelPart rightLeg;

    public NopModelPart leftLeg;

    private NopModelPart bipedLowerBody;

    public ModelNPCGolem(float scale) {
        this.init(0.0F, 0.0F);
    }

    public void init(float f, float f1) {
        short short1 = 128;
        short short2 = 128;
        float f2 = -7.0F;
        this.head = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.head.setPos(0.0F, f2, -2.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, f);
        this.head.texOffs(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F, f);
        this.hat = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.hat.setPos(0.0F, f2, -2.0F);
        this.hat.texOffs(0, 85).addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F, f + 0.5F);
        this.body = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.body.setPos(0.0F, 0.0F + f2, 0.0F);
        this.body.texOffs(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F, f + 0.2F);
        this.body.texOffs(0, 21).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 8.0F, 11.0F, f);
        this.bipedLowerBody = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.bipedLowerBody.setPos(0.0F, 0.0F + f2, 0.0F);
        this.bipedLowerBody.texOffs(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, f + 0.5F);
        this.bipedLowerBody.texOffs(30, 70).addBox(-4.5F, 6.0F, -3.0F, 9.0F, 9.0F, 6.0F, f + 0.4F);
        this.rightArm = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.rightArm.setPos(0.0F, f2, 0.0F);
        this.rightArm.texOffs(60, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, f + 0.2F);
        this.rightArm.texOffs(80, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 20.0F, 6.0F, f);
        this.rightArm.texOffs(100, 21).addBox(-13.0F, -2.5F, -3.0F, 4.0F, 20.0F, 6.0F, f + 1.0F);
        this.leftArm = new NopModelPart(128, 128).setTexSize(short1, short2);
        this.leftArm.setPos(0.0F, f2, 0.0F);
        this.leftArm.texOffs(60, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F, f + 0.2F);
        this.leftArm.texOffs(80, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 20.0F, 6.0F, f);
        this.leftArm.texOffs(100, 58).addBox(9.0F, -2.5F, -3.0F, 4.0F, 20.0F, 6.0F, f + 1.0F);
        this.leftLeg = new NopModelPart(64, 64, 0, 22).setTexSize(short1, short2);
        this.leftLeg.setPos(-4.0F, 18.0F + f2, 0.0F);
        this.leftLeg.texOffs(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, f);
        this.rightLeg = new NopModelPart(64, 64, 0, 22).setTexSize(short1, short2);
        this.rightLeg.mirror = true;
        this.rightLeg.texOffs(60, 0).addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F, f);
        this.rightLeg.setPos(5.0F, 18.0F + f2, 0.0F);
    }

    @Override
    public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.head.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.hat.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.body.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.rightArm.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.leftArm.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.rightLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.leftLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.bipedLowerBody.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(Entity entity, float par1, float limbSwingAmount, float par3, float par4, float par5) {
        EntityNPCInterface npc = (EntityNPCInterface) entity;
        this.f_102609_ = npc.m_20159_();
        this.head.yRot = par4 / (180.0F / (float) Math.PI);
        this.head.xRot = par5 / (180.0F / (float) Math.PI);
        this.hat.yRot = this.head.yRot;
        this.hat.xRot = this.head.xRot;
        this.leftLeg.xRot = -1.5F * this.func_78172_a(par1, 13.0F) * limbSwingAmount;
        this.rightLeg.xRot = 1.5F * this.func_78172_a(par1, 13.0F) * limbSwingAmount;
        this.leftLeg.yRot = 0.0F;
        this.rightLeg.yRot = 0.0F;
        float f6 = Mth.sin(this.f_102608_ * (float) Math.PI);
        float f7 = Mth.sin((16.0F - (1.0F - this.f_102608_) * (1.0F - this.f_102608_)) * (float) Math.PI);
        if ((double) this.f_102608_ > 0.0) {
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f6 * 0.6F);
            this.leftArm.yRot = 0.1F - f6 * 0.6F;
            this.rightArm.xRot = 0.0F;
            this.leftArm.xRot = 0.0F;
            this.rightArm.xRot = (float) (-Math.PI / 2);
            this.leftArm.xRot = (float) (-Math.PI / 2);
            this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
            this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
        } else {
            this.rightArm.xRot = (-0.2F + 1.5F * this.func_78172_a(par1, 13.0F)) * limbSwingAmount;
            this.leftArm.xRot = (-0.2F - 1.5F * this.func_78172_a(par1, 13.0F)) * limbSwingAmount;
            this.body.yRot = 0.0F;
            this.rightArm.yRot = 0.0F;
            this.leftArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
        }
        if (this.f_102609_) {
            this.rightArm.xRot += (float) (-Math.PI / 5);
            this.leftArm.xRot += (float) (-Math.PI / 5);
            this.leftLeg.xRot = (float) (-Math.PI * 2.0 / 5.0);
            this.rightLeg.xRot = (float) (-Math.PI * 2.0 / 5.0);
            this.leftLeg.yRot = (float) (Math.PI / 10);
            this.rightLeg.yRot = (float) (-Math.PI / 10);
        }
    }

    private float func_78172_a(float par1, float limbSwingAmount) {
        return (Math.abs(par1 % limbSwingAmount - limbSwingAmount * 0.5F) - limbSwingAmount * 0.25F) / (limbSwingAmount * 0.25F);
    }
}