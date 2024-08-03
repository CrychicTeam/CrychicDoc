package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateAnchor;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;

public class ModelEndPirateAnchorChain extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox chain;

    public ModelEndPirateAnchorChain() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.chain = new AdvancedModelBox(this, "chain");
        this.chain.setRotationPoint(0.0F, -12.5F, 0.0F);
        this.root.addChild(this.chain);
        this.chain.setTextureOffset(0, 7).addBox(-5.5F, 9.5F, -1.5F, 11.0F, 3.0F, 3.0F, 0.0F, false);
        this.chain.setTextureOffset(0, 0).addBox(-5.5F, -1.5F, -1.5F, 11.0F, 3.0F, 3.0F, 0.0F, false);
        this.chain.setTextureOffset(13, 14).addBox(2.5F, 1.5F, -1.5F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.chain.setTextureOffset(0, 14).addBox(-5.5F, 1.5F, -1.5F, 3.0F, 8.0F, 3.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.chain);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderAnchor(TileEntityEndPirateAnchor anchor, float partialTick, boolean east) {
        this.resetToDefaultPose();
    }
}