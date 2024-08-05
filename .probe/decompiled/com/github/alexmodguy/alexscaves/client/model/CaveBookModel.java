package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class CaveBookModel extends AdvancedEntityModel {

    private final AdvancedModelBox root;

    private final AdvancedModelBox spine;

    private final AdvancedModelBox lcover;

    private final AdvancedModelBox lpageStack;

    private final AdvancedModelBox rcover;

    private final AdvancedModelBox rpageStack;

    private final AdvancedModelBox rpageOpen;

    private final AdvancedModelBox page_flip;

    public CaveBookModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.spine = new AdvancedModelBox(this);
        this.spine.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.root.addChild(this.spine);
        this.spine.setTextureOffset(0, 49).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 16.0F, 0.0F, false);
        this.lcover = new AdvancedModelBox(this);
        this.lcover.setRotationPoint(0.5F, 0.0F, 0.0F);
        this.root.addChild(this.lcover);
        this.lcover.setTextureOffset(0, 32).addBox(-0.5F, -1.0F, -8.0F, 13.0F, 1.0F, 16.0F, 0.0F, false);
        this.lpageStack = new AdvancedModelBox(this);
        this.lpageStack.setRotationPoint(0.5F, 0.0F, 0.0F);
        this.lcover.addChild(this.lpageStack);
        this.lpageStack.setTextureOffset(0, 16).addBox(-1.0F, -3.0F, -7.0F, 12.0F, 2.0F, 14.0F, 0.01F, false);
        this.lpageStack.setTextureOffset(-16, 0).addBox(-1.0F, -4.0F, -8.0F, 13.0F, 1.0F, 16.0F, 0.01F, false);
        this.rcover = new AdvancedModelBox(this);
        this.rcover.setRotationPoint(-0.5F, 0.0F, 0.0F);
        this.root.addChild(this.rcover);
        this.rcover.setTextureOffset(0, 32).addBox(-12.5F, -1.0F, -8.0F, 13.0F, 1.0F, 16.0F, 0.0F, true);
        this.rpageStack = new AdvancedModelBox(this);
        this.rpageStack.setRotationPoint(-0.5F, 0.0F, 0.0F);
        this.rcover.addChild(this.rpageStack);
        this.rpageStack.setTextureOffset(0, 16).addBox(-11.0F, -3.0F, -7.0F, 12.0F, 2.0F, 14.0F, 0.0F, true);
        this.rpageStack.setTextureOffset(-16, 0).addBox(-12.0F, -4.0F, -8.0F, 13.0F, 1.0F, 16.0F, 0.0F, true);
        this.rpageOpen = new AdvancedModelBox(this);
        this.rpageOpen.setRotationPoint(0.5F, -3.0F, 0.0F);
        this.rcover.addChild(this.rpageOpen);
        this.setRotateAngle(this.rpageOpen, 0.0F, 0.0F, 0.1309F);
        this.page_flip = new AdvancedModelBox(this);
        this.page_flip.setRotationPoint(0.0F, -3.0F, 0.0F);
        this.root.addChild(this.page_flip);
        this.setRotateAngle(this.page_flip, 0.0F, 0.0F, -1.5708F);
        this.page_flip.setTextureOffset(-16, 0).addBox(0.0F, -1.0F, -8.0F, 13.0F, 1.0F, 16.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public void setupAnim(Entity entity, float openAmount, float pageAngle, float pageUp, float bookRotateX, float bookRotateY) {
        this.resetToDefaultPose();
        float close = 1.0F - openAmount;
        this.progressRotationPrev(this.lcover, close, 0.0F, 0.0F, (float) Math.toRadians(-90.0), 1.0F);
        this.progressRotationPrev(this.rcover, close, 0.0F, 0.0F, (float) Math.toRadians(90.0), 1.0F);
        this.progressPositionPrev(this.lcover, close, 1.75F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rcover, close, -1.75F, 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.root, close, 0.0F, 0.0F, (float) Math.toRadians(-90.0), 1.0F);
        this.root.rotateAngleX = this.root.rotateAngleX + (float) Math.toRadians((double) bookRotateX);
        this.root.rotateAngleZ = this.root.rotateAngleZ + (float) Math.toRadians((double) bookRotateY);
        this.lcover.setScale(1.0F + close * 0.01F, 1.0F + close * 0.01F, 1.0F + close * 0.01F);
        if (openAmount < 1.0F) {
            this.page_flip.showModel = false;
        } else {
            this.page_flip.showModel = true;
            this.page_flip.rotateAngleZ = this.lcover.rotateAngleZ - pageAngle;
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.rcover, this.lcover, this.rpageOpen, this.rpageStack, this.lpageStack, this.page_flip, this.spine);
    }

    public void mouseOver(float mouseLeanX, float mouseLeanY, float ageInTicks, float pageFlipAmount, boolean canGoLeft, boolean canGoRight) {
        float turnWobble = (float) ((Math.sin((double) (ageInTicks * 0.2F)) + 1.0) * 0.5) * (1.0F - pageFlipAmount);
        if (pageFlipAmount == 0.0F) {
            if (mouseLeanX < -0.75F && canGoLeft) {
                float clamped = Mth.clamp((mouseLeanX + 0.75F) * 8.0F, -1.0F, 0.0F);
                this.page_flip.rotateAngleZ = this.lcover.rotateAngleZ + clamped * (float) Math.toRadians((double) (turnWobble * 15.0F));
            }
            if (mouseLeanX > 0.75F && canGoRight) {
                float clamped = Mth.clamp((mouseLeanX - 0.75F) * 8.0F, 0.0F, 1.0F);
                this.page_flip.rotateAngleZ = this.lcover.rotateAngleZ + (float) Math.PI + clamped * (float) Math.toRadians((double) (turnWobble * 15.0F));
            }
        }
    }

    public void translateToPage(PoseStack poseStack, int kind) {
        this.root.translateAndRotate(poseStack);
        if (kind == 0) {
            this.lcover.translateAndRotate(poseStack);
            this.lpageStack.translateAndRotate(poseStack);
        } else if (kind == 1) {
            this.rcover.translateAndRotate(poseStack);
            this.rpageStack.translateAndRotate(poseStack);
        } else if (kind == 2) {
            this.page_flip.translateAndRotate(poseStack);
        }
    }
}