package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntitySpectre;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelSpectre extends AdvancedEntityModel<EntitySpectre> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox spine;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox wing_left;

    private final AdvancedModelBox wing_right;

    private final AdvancedModelBox wing_left_p;

    private final AdvancedModelBox wing_right_p;

    public ModelSpectre() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -0.5F, 0.0F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, 0.0F, -0.7854F, 0.0F);
        this.body.setTextureOffset(43, 0).addBox(-12.0F, -5.5F, -12.0F, 24.0F, 6.0F, 24.0F, 0.0F, false);
        this.spine = new AdvancedModelBox(this, "spine");
        this.spine.setPos(0.0F, -5.5F, 0.0F);
        this.body.addChild(this.spine);
        this.setRotationAngle(this.spine, 0.0F, 0.7854F, 0.0F);
        this.spine.setTextureOffset(0, 0).addBox(0.0F, -3.0F, -14.0F, 0.0F, 8.0F, 42.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 1.0F, 28.0F);
        this.spine.addChild(this.tail);
        this.tail.setTextureOffset(76, 31).addBox(0.0F, -6.0F, 0.0F, 0.0F, 11.0F, 27.0F, 0.0F, false);
        this.wing_left_p = new AdvancedModelBox(this, "wing_left_p");
        this.wing_left_p.setPos(12.0F, -2.5F, -12.0F);
        this.body.addChild(this.wing_left_p);
        this.wing_left = new AdvancedModelBox(this, "wing_left");
        this.wing_left_p.addChild(this.wing_left);
        this.wing_left.setTextureOffset(76, 76).addBox(0.0F, -1.5F, 0.0F, 26.0F, 3.0F, 23.0F, 0.0F, false);
        this.wing_right_p = new AdvancedModelBox(this, "wing_right_p");
        this.wing_right_p.setPos(-12.0F, -2.5F, 12.0F);
        this.body.addChild(this.wing_right_p);
        this.setRotationAngle(this.wing_right_p, 0.0F, 1.5708F, 0.0F);
        this.wing_right = new AdvancedModelBox(this, "wing_right");
        this.wing_right_p.addChild(this.wing_right);
        this.wing_right.setTextureOffset(0, 51).addBox(-26.0F, -1.5F, 0.0F, 26.0F, 3.0F, 23.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.wing_right_p, this.wing_left_p, this.wing_left, this.wing_right, this.spine, this.tail);
    }

    public void setupAnim(EntitySpectre entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flySpeed = 0.2F;
        float flyDegree = 0.6F;
        this.swing(this.spine, flySpeed, flyDegree * 0.05F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail, flySpeed, flyDegree * 0.7F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.wing_left, flySpeed, flyDegree * 0.85F, true, 7.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.wing_right, flySpeed, flyDegree * 0.85F, false, 7.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.root, flySpeed, flyDegree * 0.15F, true, 7.3F, 0.0F, ageInTicks, 1.0F);
        float partialTick = Minecraft.getInstance().getFrameTime();
        float birdPitch = entity.prevBirdPitch + (entity.birdPitch - entity.prevBirdPitch) * partialTick;
        this.root.rotateAngleX += birdPitch * (float) (Math.PI / 180.0);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}