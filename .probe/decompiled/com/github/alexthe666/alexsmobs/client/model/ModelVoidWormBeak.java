package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.tileentity.TileEntityVoidWormBeak;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class ModelVoidWormBeak extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox left;

    private final AdvancedModelBox right;

    public ModelVoidWormBeak() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.left = new AdvancedModelBox(this, "left");
        this.left.setPos(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.left);
        this.left.setTextureOffset(0, 0).addBox(-0.1F, -12.9F, -3.5F, 7.0F, 13.0F, 7.0F, -0.1F, false);
        this.right = new AdvancedModelBox(this, "right");
        this.right.setPos(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.right);
        this.right.setTextureOffset(0, 21).addBox(-7.0F, -13.0F, -3.5F, 7.0F, 13.0F, 7.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.left, this.right);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderBeak(TileEntityVoidWormBeak beak, float partialTick) {
        this.resetToDefaultPose();
        float amount = beak.getChompProgress(partialTick) * 0.2F;
        float ageInTicks = (float) beak.ticksExisted + partialTick;
        this.flap(this.left, 0.5F, 0.5F, false, 0.0F, 0.3F, ageInTicks, amount);
        this.flap(this.right, 0.5F, -0.5F, false, 0.0F, -0.3F, ageInTicks, amount);
        float rotation = Mth.cos(ageInTicks * 0.5F) * 0.5F * amount + 0.3F * amount;
        this.left.rotationPointY -= rotation * 4.5F;
        this.right.rotationPointY -= rotation * 4.5F;
    }
}