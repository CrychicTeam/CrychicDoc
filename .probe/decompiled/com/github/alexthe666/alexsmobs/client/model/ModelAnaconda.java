package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityAnaconda;
import com.github.alexthe666.alexsmobs.entity.EntityAnacondaPart;
import com.github.alexthe666.alexsmobs.entity.util.AnacondaPartIndex;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ModelAnaconda<T extends LivingEntity> extends AdvancedEntityModel<T> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox part;

    private AdvancedModelBox jaw;

    public ModelAnaconda(AnacondaPartIndex index) {
        this.texWidth = 128;
        this.texHeight = 128;
        this.part = new AdvancedModelBox(this, "part");
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 21.0F, 0.0F);
        switch(index) {
            case HEAD:
                this.part.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.part.setTextureOffset(62, 32).addBox(-3.5F, -3.0F, -9.0F, 7.0F, 3.0F, 10.0F, 0.0F, false);
                this.part.setTextureOffset(67, 0).addBox(-3.5F, -1.0F, -9.0F, 7.0F, 0.0F, 10.0F, 0.0F, false);
                this.jaw = new AdvancedModelBox(this, "        jaw");
                this.jaw.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.jaw.setTextureOffset(52, 55).addBox(-3.5F, -1.0F, -9.0F, 7.0F, 4.0F, 10.0F, 0.0F, false);
                this.jaw.setTextureOffset(66, 11).addBox(-3.5F, 0.0F, -9.0F, 7.0F, 0.0F, 10.0F, 0.0F, false);
                this.part.addChild(this.jaw);
                break;
            case NECK:
                this.part.setRotationPoint(0.0F, 0.0F, 0.0F);
                this.part.setTextureOffset(33, 32).addBox(-3.0F, -3.0F, -8.0F, 6.0F, 6.0F, 16.0F, 0.0F, false);
                break;
            case BODY:
                this.part.setRotationPoint(0.0F, 0.0F, -8.0F);
                this.part.setTextureOffset(33, 8).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 7.0F, 16.0F, 0.0F, false);
                break;
            case TAIL:
                this.part.setRotationPoint(0.0F, 0.0F, -7.0F);
                this.part.setTextureOffset(29, 55).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 16.0F, 0.0F, false);
        }
        this.root.addChild(this.part);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float strangle = 0.0F;
        if (this.jaw != null && entity instanceof EntityAnaconda anaconda) {
            strangle = anaconda.getStrangleProgress(partialTick);
            this.progressPositionPrev(this.part, strangle, 0.0F, 4.0F, 0.0F, 5.0F);
            this.progressPositionPrev(this.jaw, strangle, 0.0F, 0.0F, 1.0F, 5.0F);
            this.progressRotationPrev(this.part, strangle, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.jaw, strangle, Maths.rad(160.0), 0.0F, 0.0F, 5.0F);
            this.part.rotateAngleY += netHeadYaw / (180.0F / (float) Math.PI);
            this.part.rotateAngleX = this.part.rotateAngleX + Math.min(0.0F, headPitch / (180.0F / (float) Math.PI));
            this.part.rotationPointX = this.part.rotationPointX + Mth.sin(limbSwing) * 2.0F * limbSwingAmount;
            this.walk(this.part, 0.7F, 0.2F, false, 1.0F, 0.05F, ageInTicks, strangle * 0.2F);
            this.walk(this.jaw, 0.7F, 0.4F, true, 1.0F, -0.05F, ageInTicks, strangle * 0.2F);
        } else if (entity instanceof EntityAnacondaPart partEntity) {
            float f = 1.01F;
            if (partEntity.getBodyIndex() % 2 == 1) {
                f = 1.0F;
            }
            float swell = partEntity.getSwellLerp(partialTick) * 0.15F;
            this.part.setScale(f + swell, f + swell, f);
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return this.jaw == null ? ImmutableList.of(this.root, this.part) : ImmutableList.of(this.root, this.part, this.jaw);
    }
}