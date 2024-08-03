package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityStradpole;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;

public class ModelStradpole extends AdvancedEntityModel<EntityStradpole> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox hair_left;

    private final AdvancedModelBox hair_right;

    private final AdvancedModelBox tail;

    public ModelStradpole() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -4.0F, 0.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.hair_left = new AdvancedModelBox(this, "hair_left");
        this.hair_left.setPos(4.0F, -4.0F, 0.0F);
        this.body.addChild(this.hair_left);
        this.setRotationAngle(this.hair_left, 0.0F, 0.0F, 1.1345F);
        this.hair_left.setTextureOffset(0, 17).addBox(0.0F, 0.0F, -3.0F, 9.0F, 0.0F, 8.0F, 0.0F, false);
        this.hair_right = new AdvancedModelBox(this, "hair_right");
        this.hair_right.setPos(-4.0F, -4.0F, 0.0F);
        this.body.addChild(this.hair_right);
        this.setRotationAngle(this.hair_right, 0.0F, 0.0F, -1.1345F);
        this.hair_right.setTextureOffset(0, 17).addBox(-9.0F, 0.0F, -3.0F, 9.0F, 0.0F, 8.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, 0.0F, 4.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(24, 24).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 14.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityStradpole entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 1.0F;
        float walkDegree = 0.4F;
        float idleSpeed = 0.1F;
        float idleDegree = 0.25F;
        this.flap(this.hair_right, idleSpeed, idleDegree, true, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.hair_left, idleSpeed, idleDegree, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.body, walkSpeed, walkDegree * 0.2F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.body, walkSpeed, walkDegree * 0.4F, true, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed * 1.4F, walkDegree * 2.0F, false, 2.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.2F, new AdvancedModelBox[] { this.body });
        float partialTick = Minecraft.getInstance().getFrameTime();
        float birdPitch = entity.prevSwimPitch + (entity.swimPitch - entity.prevSwimPitch) * partialTick;
        this.body.rotateAngleX += birdPitch * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.tail, this.body, this.hair_left, this.hair_right);
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}