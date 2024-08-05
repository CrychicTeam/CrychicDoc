package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class RaygunModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox handle;

    private final AdvancedModelBox trigger;

    private final AdvancedModelBox main;

    private final AdvancedModelBox barrel;

    private final AdvancedModelBox nose;

    private final AdvancedModelBox ring;

    private final AdvancedModelBox ring2;

    private final AdvancedModelBox ring3;

    private final AdvancedModelBox ball;

    private final AdvancedModelBox grip;

    public RaygunModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.handle = new AdvancedModelBox(this);
        this.handle.setRotationPoint(0.0F, -3.0F, 6.5F);
        this.root.addChild(this.handle);
        this.handle.setTextureOffset(14, 0).addBox(-1.0F, -3.0F, -0.5F, 2.0F, 6.0F, 3.0F, 0.0F, false);
        this.handle.setTextureOffset(0, 0).addBox(-1.0F, -3.0F, -0.5F, 2.0F, 6.0F, 3.0F, 0.25F, false);
        this.trigger = new AdvancedModelBox(this);
        this.trigger.setRotationPoint(0.0F, -1.5F, -3.5F);
        this.handle.addChild(this.trigger);
        this.trigger.setTextureOffset(0, 8).addBox(0.0F, -2.5F, -1.0F, 0.0F, 5.0F, 4.0F, 0.0F, false);
        this.main = new AdvancedModelBox(this);
        this.main.setRotationPoint(0.0F, 3.0F, -2.5F);
        this.handle.addChild(this.main);
        this.main.setTextureOffset(18, 6).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 6.0F, 6.0F, 0.25F, false);
        this.main.setTextureOffset(0, 0).addBox(0.0F, -15.0F, -8.0F, 0.0F, 5.0F, 18.0F, 0.0F, false);
        this.main.setTextureOffset(24, 23).addBox(-3.0F, -12.0F, -2.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        this.barrel = new AdvancedModelBox(this);
        this.barrel.setRotationPoint(0.0F, -9.0F, -6.0F);
        this.main.addChild(this.barrel);
        this.barrel.setTextureOffset(20, 35).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 6.0F, 0.0F, false);
        this.barrel.setTextureOffset(0, 35).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 6.0F, 0.25F, false);
        this.nose = new AdvancedModelBox(this);
        this.nose.setRotationPoint(0.0F, 9.0F, 6.0F);
        this.barrel.addChild(this.nose);
        this.nose.setTextureOffset(36, 14).addBox(-0.5F, -9.5F, -14.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        this.ring = new AdvancedModelBox(this);
        this.ring.setRotationPoint(0.0F, -9.0F, -14.0F);
        this.nose.addChild(this.ring);
        this.ring.setTextureOffset(42, 7).addBox(-3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.ring2 = new AdvancedModelBox(this);
        this.ring2.setRotationPoint(0.0F, -9.0F, -13.0F);
        this.nose.addChild(this.ring2);
        this.ring2.setTextureOffset(42, 7).addBox(-3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.ring3 = new AdvancedModelBox(this);
        this.ring3.setRotationPoint(0.0F, -9.0F, -12.0F);
        this.nose.addChild(this.ring3);
        this.ring3.setTextureOffset(42, 7).addBox(-3.5F, -3.5F, 1.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        this.ball = new AdvancedModelBox(this);
        this.ball.setRotationPoint(0.0F, -9.0F, -16.0F);
        this.nose.addChild(this.ball);
        this.ball.setTextureOffset(8, 12).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.grip = new AdvancedModelBox(this);
        this.grip.setRotationPoint(0.0F, -3.0F, -8.0F);
        this.handle.addChild(this.grip);
        this.grip.setTextureOffset(48, 37).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 6.0F, 0.25F, false);
        this.grip.setTextureOffset(48, 27).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(Entity entity, float useAmount, float ageInTicks, float unused, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.handle, this.grip, this.ball, this.barrel, this.nose, this.trigger, this.main, this.ring, this.ring2, this.ring3);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }
}