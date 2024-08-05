package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMurmurHead;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;

public class ModelMurmurHead extends AdvancedEntityModel<EntityMurmurHead> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox head;

    private final AdvancedModelBox backHair;

    private final AdvancedModelBox leftHair;

    private final AdvancedModelBox rightHair;

    public ModelMurmurHead() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.root.addChild(this.head);
        this.head.setTextureOffset(37, 41).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 9.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(0, 41).addBox(-4.5F, -8.0F, -4.5F, 9.0F, 9.0F, 9.0F, 0.2F, false);
        this.backHair = new AdvancedModelBox(this, "backHair");
        this.backHair.setRotationPoint(0.0F, -5.0F, 5.0F);
        this.head.addChild(this.backHair);
        this.backHair.setTextureOffset(49, 0).addBox(-5.5F, -2.0F, -1.5F, 11.0F, 20.0F, 3.0F, 0.0F, false);
        this.leftHair = new AdvancedModelBox(this, "leftHair");
        this.leftHair.setRotationPoint(4.5F, -5.0F, 1.0F);
        this.head.addChild(this.leftHair);
        this.leftHair.setTextureOffset(17, 60).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 16.0F, 5.0F, 0.0F, false);
        this.rightHair = new AdvancedModelBox(this, "rightHair");
        this.rightHair.setRotationPoint(-4.5F, -5.0F, 1.0F);
        this.head.addChild(this.rightHair);
        this.rightHair.setTextureOffset(17, 60).addBox(-1.0F, -2.0F, -2.5F, 2.0F, 16.0F, 5.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.leftHair, this.rightHair, this.backHair);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void animateHair(float ageInTicks) {
        float idleSpeed = 0.05F;
        float idleDegree = 0.1F;
        this.walk(this.backHair, idleSpeed, idleDegree * 0.5F, false, 0.0F, -0.05F, ageInTicks, 1.0F);
        this.flap(this.rightHair, idleSpeed, idleDegree * 0.5F, false, 1.0F, 0.05F, ageInTicks, 1.0F);
        this.flap(this.leftHair, idleSpeed, idleDegree * 0.5F, true, 1.0F, 0.05F, ageInTicks, 1.0F);
    }

    public void setupAnim(EntityMurmurHead entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float angerProgress = entity.prevAngerProgress + (entity.angerProgress - entity.prevAngerProgress) * partialTicks;
        if (ageInTicks > 5.0F) {
            float hairAnimateScale = Math.min(1.0F, (ageInTicks - 5.0F) / 10.0F);
            double d0 = Mth.lerp((double) partialTicks, entity.prevXHair, entity.xHair) - Mth.lerp((double) partialTicks, entity.f_19854_, entity.m_20185_());
            double d1 = Mth.lerp((double) partialTicks, entity.prevYHair, entity.yHair) - Mth.lerp((double) partialTicks, entity.f_19855_, entity.m_20186_());
            double d2 = Mth.lerp((double) partialTicks, entity.prevZHair, entity.zHair) - Mth.lerp((double) partialTicks, entity.f_19856_, entity.m_20189_());
            float f = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_);
            double d3 = (double) Mth.sin(f * (float) (Math.PI / 180.0));
            double d4 = (double) (-Mth.cos(f * (float) (Math.PI / 180.0)));
            float f1 = (float) d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F) * hairAnimateScale;
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F) * hairAnimateScale;
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;
            f3 = Mth.clamp(f3, -20.0F, 20.0F) * hairAnimateScale;
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }
            f1 += Mth.sin(Mth.lerp(partialTicks, entity.f_19867_, entity.f_19787_) * 6.0F) * 32.0F * 1.0F;
            float hairX = Maths.rad((double) (6.0F + f2 / 2.0F + f1 - 180.0F));
            float hairY = Maths.rad((double) (f3 / 2.0F));
            float hairZ = Maths.rad((double) (180.0F - f3 / 2.0F));
            this.backHair.rotateAngleX -= hairX;
            this.backHair.rotateAngleY -= hairY;
            this.backHair.rotateAngleZ -= hairZ;
            this.rightHair.rotateAngleX -= hairX;
            this.rightHair.rotateAngleY -= hairY;
            this.rightHair.rotateAngleZ -= hairZ;
            this.leftHair.rotateAngleX -= hairX;
            this.leftHair.rotateAngleY -= hairY;
            this.leftHair.rotateAngleZ -= hairZ;
        }
        this.animateHair(ageInTicks);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        this.progressRotationPrev(this.backHair, angerProgress, Maths.rad(-20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightHair, angerProgress, Maths.rad(-10.0), 0.0F, Maths.rad(25.0), 5.0F);
        this.progressRotationPrev(this.leftHair, angerProgress, Maths.rad(-10.0), 0.0F, Maths.rad(-25.0), 5.0F);
    }
}