package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateAnchorWinch;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class ModelEndPirateAnchorWinch extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox chains;

    public ModelEndPirateAnchorWinch() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.chains = new AdvancedModelBox(this, "chains");
        this.chains.setRotationPoint(0.0F, -8.0F, 0.0F);
        this.root.addChild(this.chains);
        this.chains.setTextureOffset(0, 33).addBox(-8.0F, -5.0F, -5.0F, 16.0F, 10.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.chains);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderAnchor(TileEntityEndPirateAnchorWinch anchor, float partialTick, boolean east) {
        this.resetToDefaultPose();
        float timeWinching = (float) anchor.windCounter + partialTick;
        float f = anchor.getWindProgress(partialTick);
        float f1 = anchor.isWindingUp() ? 1.0F : -1.0F;
        if (anchor.isWinching()) {
            this.chains.rotateAngleX = timeWinching * 0.2F * f * f1;
            anchor.clientRoll = this.chains.rotateAngleX;
        } else {
            float rollDeg = (float) Mth.wrapDegrees(Math.toDegrees((double) anchor.clientRoll));
            this.chains.rotateAngleX = f1 * f * 0.2F * Maths.rad((double) rollDeg);
        }
    }

    public void animateStack(ItemStack itemStackIn) {
        this.resetToDefaultPose();
    }
}