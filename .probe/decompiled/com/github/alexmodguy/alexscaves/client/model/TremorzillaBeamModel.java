package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class TremorzillaBeamModel extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox base;

    private final AdvancedModelBox flame;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    private final AdvancedModelBox flame2;

    private final AdvancedModelBox cube_r3;

    private final AdvancedModelBox cube_r4;

    private final AdvancedModelBox flame3;

    private final AdvancedModelBox cube_r5;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox flame4;

    private final AdvancedModelBox cube_r7;

    private final AdvancedModelBox cube_r8;

    public TremorzillaBeamModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.base = new AdvancedModelBox(this);
        this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.base);
        this.base.setTextureOffset(0, 0).addBox(-11.0F, 0.0F, -11.0F, 22.0F, 0.0F, 22.0F, 0.0F, false);
        this.flame = new AdvancedModelBox(this);
        this.flame.setRotationPoint(0.0F, 0.0F, -11.0F);
        this.base.addChild(this.flame);
        this.cube_r1 = new AdvancedModelBox(this);
        this.cube_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame.addChild(this.cube_r1);
        this.setRotateAngle(this.cube_r1, 0.3927F, 0.0F, 0.0F);
        this.cube_r1.setTextureOffset(3, 38).addBox(-12.0F, -24.0F, 0.0F, 24.0F, 24.0F, 0.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this);
        this.cube_r2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame.addChild(this.cube_r2);
        this.setRotateAngle(this.cube_r2, 0.7854F, 0.0F, 0.0F);
        this.cube_r2.setTextureOffset(0, 22).addBox(-15.0F, -16.0F, 0.0F, 30.0F, 16.0F, 0.0F, 0.0F, false);
        this.flame2 = new AdvancedModelBox(this);
        this.flame2.setRotationPoint(0.0F, 0.0F, 11.0F);
        this.base.addChild(this.flame2);
        this.cube_r3 = new AdvancedModelBox(this);
        this.cube_r3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame2.addChild(this.cube_r3);
        this.setRotateAngle(this.cube_r3, -0.3927F, 0.0F, 0.0F);
        this.cube_r3.setTextureOffset(3, 38).addBox(-12.0F, -24.0F, 0.0F, 24.0F, 24.0F, 0.0F, 0.0F, true);
        this.cube_r4 = new AdvancedModelBox(this);
        this.cube_r4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame2.addChild(this.cube_r4);
        this.setRotateAngle(this.cube_r4, -0.7854F, 0.0F, 0.0F);
        this.cube_r4.setTextureOffset(0, 22).addBox(-15.0F, -16.0F, 0.0F, 30.0F, 16.0F, 0.0F, 0.0F, true);
        this.flame3 = new AdvancedModelBox(this);
        this.flame3.setRotationPoint(11.0F, 0.0F, 0.0F);
        this.base.addChild(this.flame3);
        this.cube_r5 = new AdvancedModelBox(this);
        this.cube_r5.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame3.addChild(this.cube_r5);
        this.setRotateAngle(this.cube_r5, 0.0F, 0.0F, 0.3927F);
        this.cube_r5.setTextureOffset(3, 14).addBox(0.0F, -24.0F, -13.0F, 0.0F, 24.0F, 24.0F, 0.0F, true);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame3.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 0.0F, 0.0F, 0.7854F);
        this.cube_r6.setTextureOffset(0, -8).addBox(0.0F, -16.0F, -15.0F, 0.0F, 16.0F, 30.0F, 0.0F, true);
        this.flame4 = new AdvancedModelBox(this);
        this.flame4.setRotationPoint(-11.0F, 0.0F, 0.0F);
        this.base.addChild(this.flame4);
        this.cube_r7 = new AdvancedModelBox(this);
        this.cube_r7.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame4.addChild(this.cube_r7);
        this.setRotateAngle(this.cube_r7, 0.0F, 0.0F, -0.3927F);
        this.cube_r7.setTextureOffset(3, 14).addBox(0.0F, -24.0F, -12.0F, 0.0F, 24.0F, 24.0F, 0.0F, false);
        this.cube_r8 = new AdvancedModelBox(this);
        this.cube_r8.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.flame4.addChild(this.cube_r8);
        this.setRotateAngle(this.cube_r8, 0.0F, 0.0F, -0.7854F);
        this.cube_r8.setTextureOffset(0, -8).addBox(0.0F, -16.0F, -15.0F, 0.0F, 16.0F, 30.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.cube_r1, this.cube_r2, this.cube_r3, this.cube_r4, this.cube_r5, this.cube_r6, this.cube_r7, this.cube_r8, this.base, this.flame, this.flame2, new AdvancedModelBox[] { this.flame3, this.flame4 });
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(Entity entity, float v, float v1, float v2, float v3, float v4) {
        this.resetToDefaultPose();
    }
}