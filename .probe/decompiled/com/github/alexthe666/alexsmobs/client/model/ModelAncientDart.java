package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ModelAncientDart extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox main;

    private final AdvancedModelBox feathers;

    private final AdvancedModelBox cube_r1;

    private final AdvancedModelBox cube_r2;

    public ModelAncientDart() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 0.0F, 0.0F);
        this.main = new AdvancedModelBox(this, "main");
        this.main.setPos(0.0F, -1.0F, 0.0F);
        this.root.addChild(this.main);
        this.main.setTextureOffset(11, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        this.main.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -5.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        this.feathers = new AdvancedModelBox(this, "feathers");
        this.feathers.setPos(0.0F, 1.0F, 1.0F);
        this.main.addChild(this.feathers);
        this.cube_r1 = new AdvancedModelBox(this, "cube_r1");
        this.cube_r1.setPos(0.0F, -1.0F, 0.5F);
        this.feathers.addChild(this.cube_r1);
        this.setRotationAngle(this.cube_r1, 0.0F, 0.0F, 0.7854F);
        this.cube_r1.setTextureOffset(0, 6).addBox(0.0F, -1.5F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);
        this.cube_r2 = new AdvancedModelBox(this, "cube_r2");
        this.cube_r2.setPos(0.0F, -1.0F, 0.5F);
        this.feathers.addChild(this.cube_r2);
        this.setRotationAngle(this.cube_r2, 0.0F, 0.0F, -0.7854F);
        this.cube_r2.setTextureOffset(7, 6).addBox(0.0F, -1.5F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, false);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.main, this.feathers, this.cube_r1, this.cube_r2);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(Entity entity, float v, float v1, float v2, float v3, float v4) {
    }

    public void setRotationAngle(AdvancedModelBox modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}