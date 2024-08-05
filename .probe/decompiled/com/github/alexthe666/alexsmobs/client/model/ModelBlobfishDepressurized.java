package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBlobfish;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelBlobfishDepressurized extends AdvancedEntityModel<EntityBlobfish> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail_fin;

    private final AdvancedModelBox fin_left;

    private final AdvancedModelBox fin_right;

    public ModelBlobfishDepressurized() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -2.5F, 1.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -3.5F, -9.0F, 8.0F, 6.0F, 9.0F, 0.0F, false);
        this.nose = new AdvancedModelBox(this, "nose");
        this.nose.setPos(0.0F, -0.5F, -9.0F);
        this.body.addChild(this.nose);
        this.nose.setTextureOffset(0, 0).addBox(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 0.75F, 0.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 16).addBox(-3.0F, -3.25F, 0.0F, 6.0F, 5.0F, 4.0F, 0.0F, false);
        this.tail_fin = new AdvancedModelBox(this, "tail_fin");
        this.tail_fin.setPos(0.0F, -0.25F, 4.0F);
        this.tail.addChild(this.tail_fin);
        this.tail_fin.setTextureOffset(16, 21).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F, false);
        this.fin_left = new AdvancedModelBox(this, "fin_left");
        this.fin_left.setPos(4.0F, 2.0F, -4.0F);
        this.body.addChild(this.fin_left);
        this.setRotationAngle(this.fin_left, -1.5708F, 0.0F, 0.0F);
        this.fin_left.setTextureOffset(0, 4).addBox(0.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        this.fin_right = new AdvancedModelBox(this, "fin_right");
        this.fin_right.setPos(-4.0F, 2.0F, -4.0F);
        this.body.addChild(this.fin_right);
        this.setRotationAngle(this.fin_right, -1.5708F, 0.0F, 0.0F);
        this.fin_right.setTextureOffset(0, 4).addBox(-3.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.fin_left, this.fin_right, this.tail, this.tail_fin, this.nose);
    }

    public void setupAnim(EntityBlobfish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        if (!entity.m_20096_()) {
            this.body.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
        }
        float swimSpeed = 1.5F;
        float swimDegree = 0.85F;
        this.swing(this.tail, swimSpeed, swimDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail_fin, swimSpeed, swimDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.flap(this.fin_left, swimSpeed, swimDegree, false, 3.0F, -0.3F, limbSwing, limbSwingAmount);
        this.flap(this.fin_right, swimSpeed, swimDegree, true, 3.0F, -0.3F, limbSwing, limbSwingAmount);
        float lvt_6_1_ = Mth.lerp(Minecraft.getInstance().getFrameTime(), entity.prevSquishFactor, entity.squishFactor);
        float lvt_7_1_ = 1.0F / (lvt_6_1_ + 1.0F);
        float squishScale = 1.0F / lvt_7_1_;
        this.body.setScale(1.0F, squishScale, 1.0F);
        this.body.rotationPointY += lvt_6_1_ * -5.0F;
        this.body.setShouldScaleChildren(true);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}