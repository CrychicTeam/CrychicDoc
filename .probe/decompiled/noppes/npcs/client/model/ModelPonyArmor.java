package noppes.npcs.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.model.NopModelPart;

public class ModelPonyArmor extends EntityModel {

    private boolean rainboom;

    public NopModelPart head;

    public NopModelPart Body;

    public NopModelPart BodyBack;

    public NopModelPart rightarm;

    public NopModelPart LeftArm;

    public NopModelPart RightLeg;

    public NopModelPart LeftLeg;

    public NopModelPart rightarm2;

    public NopModelPart LeftArm2;

    public NopModelPart RightLeg2;

    public NopModelPart LeftLeg2;

    public boolean isPegasus = false;

    public boolean isUnicorn = false;

    public boolean isSleeping = false;

    public boolean isFlying = false;

    public boolean isGlow = false;

    public boolean isSneak = false;

    public boolean aimedBow;

    public int heldItemRight;

    public ModelPonyArmor(float f) {
        this.init(f, 0.0F);
    }

    public void init(float strech, float f) {
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        this.head = new NopModelPart(64, 32, 0, 0);
        this.head.addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 8.0F, strech);
        this.head.setPos(f2, f3, f4);
        float f5 = 0.0F;
        float f6 = 0.0F;
        float f7 = 0.0F;
        this.Body = new NopModelPart(64, 32, 16, 16);
        this.Body.addBox(-4.0F, 4.0F, -2.0F, 8.0F, 8.0F, 4.0F, strech);
        this.Body.setPos(f5, f6 + f, f7);
        this.BodyBack = new NopModelPart(64, 32, 0, 0);
        this.BodyBack.addBox(-4.0F, 4.0F, 6.0F, 8.0F, 8.0F, 8.0F, strech);
        this.BodyBack.setPos(f5, f6 + f, f7);
        this.rightarm = new NopModelPart(64, 32, 0, 16);
        this.rightarm.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech);
        this.rightarm.setPos(-3.0F, 8.0F + f, 0.0F);
        this.LeftArm = new NopModelPart(64, 32, 0, 16);
        this.LeftArm.mirror = true;
        this.LeftArm.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech);
        this.LeftArm.setPos(3.0F, 8.0F + f, 0.0F);
        this.RightLeg = new NopModelPart(64, 32, 0, 16);
        this.RightLeg.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech);
        this.RightLeg.setPos(-3.0F, 0.0F + f, 0.0F);
        this.LeftLeg = new NopModelPart(64, 32, 0, 16);
        this.LeftLeg.mirror = true;
        this.LeftLeg.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech);
        this.LeftLeg.setPos(3.0F, 0.0F + f, 0.0F);
        this.rightarm2 = new NopModelPart(64, 32, 0, 16);
        this.rightarm2.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech * 0.5F);
        this.rightarm2.setPos(-3.0F, 8.0F + f, 0.0F);
        this.LeftArm2 = new NopModelPart(64, 32, 0, 16);
        this.LeftArm2.mirror = true;
        this.LeftArm2.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech * 0.5F);
        this.LeftArm2.setPos(3.0F, 8.0F + f, 0.0F);
        this.RightLeg2 = new NopModelPart(64, 32, 0, 16);
        this.RightLeg2.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech * 0.5F);
        this.RightLeg2.setPos(-3.0F, 0.0F + f, 0.0F);
        this.LeftLeg2 = new NopModelPart(64, 32, 0, 16);
        this.LeftLeg2.mirror = true;
        this.LeftLeg2.addBox(-2.0F, 4.0F, -2.0F, 4.0F, 12.0F, 4.0F, strech * 0.5F);
        this.LeftLeg2.setPos(3.0F, 0.0F + f, 0.0F);
    }

    @Override
    public void setupAnim(Entity entity, float aniPosition, float aniSpeed, float age, float yHead, float xHead) {
        EntityNPCInterface npc = (EntityNPCInterface) entity;
        if (!this.f_102609_) {
            this.f_102609_ = npc.currentAnimation == 1;
        }
        if (this.isSneak && (npc.currentAnimation == 7 || npc.currentAnimation == 2)) {
            this.isSneak = false;
        }
        this.rainboom = false;
        float f6;
        float f7;
        if (this.isSleeping) {
            f6 = 1.4F;
            f7 = 0.1F;
        } else {
            f6 = yHead / (float) (180.0 / Math.PI);
            f7 = xHead / (float) (180.0 / Math.PI);
        }
        this.head.yRot = f6;
        this.head.xRot = f7;
        float f8;
        float f9;
        float f10;
        float f11;
        if (this.isFlying && this.isPegasus) {
            if (aniSpeed < 0.9999F) {
                this.rainboom = false;
                f8 = Mth.sin(0.0F - aniSpeed * 0.5F);
                f9 = Mth.sin(0.0F - aniSpeed * 0.5F);
                f10 = Mth.sin(aniSpeed * 0.5F);
                f11 = Mth.sin(aniSpeed * 0.5F);
            } else {
                this.rainboom = true;
                f8 = 4.712F;
                f9 = 4.712F;
                f10 = 1.571F;
                f11 = 1.571F;
            }
            this.rightarm.yRot = 0.2F;
            this.LeftArm.yRot = -0.2F;
            this.RightLeg.yRot = -0.2F;
            this.LeftLeg.yRot = 0.2F;
            this.rightarm2.yRot = 0.2F;
            this.LeftArm2.yRot = -0.2F;
            this.RightLeg2.yRot = -0.2F;
            this.LeftLeg2.yRot = 0.2F;
        } else {
            f8 = Mth.cos(aniPosition * 0.6662F + 3.141593F) * 0.6F * aniSpeed;
            f9 = Mth.cos(aniPosition * 0.6662F) * 0.6F * aniSpeed;
            f10 = Mth.cos(aniPosition * 0.6662F) * 0.3F * aniSpeed;
            f11 = Mth.cos(aniPosition * 0.6662F + 3.141593F) * 0.3F * aniSpeed;
            this.rightarm.yRot = 0.0F;
            this.LeftArm.yRot = 0.0F;
            this.RightLeg.yRot = 0.0F;
            this.LeftLeg.yRot = 0.0F;
            this.rightarm2.yRot = 0.0F;
            this.LeftArm2.yRot = 0.0F;
            this.RightLeg2.yRot = 0.0F;
            this.LeftLeg2.yRot = 0.0F;
        }
        if (this.isSleeping) {
            f8 = 4.712F;
            f9 = 4.712F;
            f10 = 1.571F;
            f11 = 1.571F;
        }
        this.rightarm.xRot = f8;
        this.LeftArm.xRot = f9;
        this.RightLeg.xRot = f10;
        this.LeftLeg.xRot = f11;
        this.rightarm.zRot = 0.0F;
        this.LeftArm.zRot = 0.0F;
        this.rightarm2.xRot = f8;
        this.LeftArm2.xRot = f9;
        this.RightLeg2.xRot = f10;
        this.LeftLeg2.xRot = f11;
        this.rightarm2.zRot = 0.0F;
        this.LeftArm2.zRot = 0.0F;
        if (this.heldItemRight != 0 && !this.rainboom && !this.isUnicorn) {
            this.rightarm.xRot = this.rightarm.xRot * 0.5F - 0.3141593F;
            this.rightarm2.xRot = this.rightarm2.xRot * 0.5F - 0.3141593F;
        }
        float f13 = Mth.sin(this.Body.yRot) * 5.0F;
        float f14 = Mth.cos(this.Body.yRot) * 5.0F;
        float f15 = 4.0F;
        if (this.isSneak && !this.isFlying) {
            f15 = 0.0F;
        }
        if (this.isSleeping) {
            f15 = 2.6F;
        }
        if (this.rainboom) {
            this.rightarm.z = f13 + 2.0F;
            this.rightarm2.z = f13 + 2.0F;
            this.LeftArm.z = 0.0F - f13 + 2.0F;
            this.LeftArm2.z = 0.0F - f13 + 2.0F;
        } else {
            this.rightarm.z = f13 + 1.0F;
            this.rightarm2.z = f13 + 1.0F;
            this.LeftArm.z = 0.0F - f13 + 1.0F;
            this.LeftArm2.z = 0.0F - f13 + 1.0F;
        }
        this.rightarm.x = 0.0F - f14 - 1.0F + f15;
        this.rightarm2.x = 0.0F - f14 - 1.0F + f15;
        this.LeftArm.x = f14 + 1.0F - f15;
        this.LeftArm2.x = f14 + 1.0F - f15;
        this.RightLeg.x = 0.0F - f14 - 1.0F + f15;
        this.RightLeg2.x = 0.0F - f14 - 1.0F + f15;
        this.LeftLeg.x = f14 + 1.0F - f15;
        this.LeftLeg2.x = f14 + 1.0F - f15;
        this.rightarm.yRot = this.rightarm.yRot + this.Body.yRot;
        this.rightarm2.yRot = this.rightarm2.yRot + this.Body.yRot;
        this.LeftArm.yRot = this.LeftArm.yRot + this.Body.yRot;
        this.LeftArm2.yRot = this.LeftArm2.yRot + this.Body.yRot;
        this.LeftArm.xRot = this.LeftArm.xRot + this.Body.yRot;
        this.LeftArm2.xRot = this.LeftArm2.xRot + this.Body.yRot;
        this.rightarm.y = 8.0F;
        this.LeftArm.y = 8.0F;
        this.RightLeg.y = 4.0F;
        this.LeftLeg.y = 4.0F;
        this.rightarm2.y = 8.0F;
        this.LeftArm2.y = 8.0F;
        this.RightLeg2.y = 4.0F;
        this.LeftLeg2.y = 4.0F;
        if (this.isSneak && !this.isFlying) {
            float f17 = 0.4F;
            float f22 = 7.0F;
            float f27 = -4.0F;
            this.Body.xRot = f17;
            this.Body.y = f22;
            this.Body.z = f27;
            this.BodyBack.xRot = f17;
            this.BodyBack.y = f22;
            this.BodyBack.z = f27;
            this.RightLeg.xRot -= 0.0F;
            this.LeftLeg.xRot -= 0.0F;
            this.rightarm.xRot -= 0.4F;
            this.LeftArm.xRot -= 0.4F;
            this.RightLeg.z = 10.0F;
            this.LeftLeg.z = 10.0F;
            this.RightLeg.y = 7.0F;
            this.LeftLeg.y = 7.0F;
            this.RightLeg2.xRot -= 0.0F;
            this.LeftLeg2.xRot -= 0.0F;
            this.rightarm2.xRot -= 0.4F;
            this.LeftArm2.xRot -= 0.4F;
            this.RightLeg2.z = 10.0F;
            this.LeftLeg2.z = 10.0F;
            this.RightLeg2.y = 7.0F;
            this.LeftLeg2.y = 7.0F;
            float f35;
            float f31;
            float f33;
            if (this.isSleeping) {
                f31 = 2.0F;
                f33 = -1.0F;
                f35 = 1.0F;
            } else {
                f31 = 6.0F;
                f33 = -2.0F;
                f35 = 0.0F;
            }
            this.head.y = f31;
            this.head.z = f33;
            this.head.x = f35;
        } else {
            float f18 = 0.0F;
            float f23 = 0.0F;
            float f28 = 0.0F;
            this.Body.xRot = f18;
            this.Body.y = f23;
            this.Body.z = f28;
            this.BodyBack.xRot = f18;
            this.BodyBack.y = f23;
            this.BodyBack.z = f28;
            this.RightLeg.z = 10.0F;
            this.LeftLeg.z = 10.0F;
            this.RightLeg.y = 8.0F;
            this.LeftLeg.y = 8.0F;
            this.RightLeg2.z = 10.0F;
            this.LeftLeg2.z = 10.0F;
            this.RightLeg2.y = 8.0F;
            this.LeftLeg2.y = 8.0F;
            float f36 = 0.0F;
            float f37 = 0.0F;
            this.head.y = f36;
            this.head.z = f37;
        }
        if (this.isSleeping) {
            this.rightarm.z += 6.0F;
            this.LeftArm.z += 6.0F;
            this.RightLeg.z -= 8.0F;
            this.LeftLeg.z -= 8.0F;
            this.rightarm.y += 2.0F;
            this.LeftArm.y += 2.0F;
            this.RightLeg.y += 2.0F;
            this.LeftLeg.y += 2.0F;
            this.rightarm2.z += 6.0F;
            this.LeftArm2.z += 6.0F;
            this.RightLeg2.z -= 8.0F;
            this.LeftLeg2.z -= 8.0F;
            this.rightarm2.y += 2.0F;
            this.LeftArm2.y += 2.0F;
            this.RightLeg2.y += 2.0F;
            this.LeftLeg2.y += 2.0F;
        }
        if (this.aimedBow && !this.isUnicorn) {
            float f20 = 0.0F;
            float f25 = 0.0F;
            this.rightarm.zRot = 0.0F;
            this.rightarm.yRot = -(0.1F - f20 * 0.6F) + this.head.yRot;
            this.rightarm.xRot = 4.712F + this.head.xRot;
            this.rightarm.xRot -= f20 * 1.2F - f25 * 0.4F;
            this.rightarm.zRot = this.rightarm.zRot + Mth.cos(age * 0.09F) * 0.05F + 0.05F;
            this.rightarm.xRot = this.rightarm.xRot + Mth.sin(age * 0.067F) * 0.05F;
            this.rightarm2.zRot = 0.0F;
            this.rightarm2.yRot = -(0.1F - f20 * 0.6F) + this.head.yRot;
            this.rightarm2.xRot = 4.712F + this.head.xRot;
            this.rightarm2.xRot -= f20 * 1.2F - f25 * 0.4F;
            this.rightarm2.zRot = this.rightarm2.zRot + Mth.cos(age * 0.09F) * 0.05F + 0.05F;
            this.rightarm2.xRot = this.rightarm2.xRot + Mth.sin(age * 0.067F) * 0.05F;
            this.rightarm.z++;
            this.rightarm2.z++;
        }
    }

    @Override
    public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.head.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.Body.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.BodyBack.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.LeftArm.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.rightarm.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.LeftLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.RightLeg.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.LeftArm2.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.rightarm2.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.LeftLeg2.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
        this.RightLeg2.render(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
    }
}