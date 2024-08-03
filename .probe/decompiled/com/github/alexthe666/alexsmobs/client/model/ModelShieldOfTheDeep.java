package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

public class ModelShieldOfTheDeep extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox shield;

    private final AdvancedModelBox handle;

    public ModelShieldOfTheDeep() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.shield = new AdvancedModelBox(this, "shield");
        this.shield.setPos(-2.0F, 16.0F, 0.0F);
        this.shield.setTextureOffset(0, 0).addBox(-1.0F, -4.0F, -6.0F, 1.0F, 12.0F, 12.0F, 0.0F, false);
        this.shield.setTextureOffset(17, 15).addBox(-3.0F, -3.0F, -5.0F, 2.0F, 10.0F, 10.0F, 0.0F, false);
        this.shield.setTextureOffset(27, 0).addBox(-4.0F, -1.0F, -3.0F, 3.0F, 6.0F, 6.0F, 0.0F, false);
        this.handle = new AdvancedModelBox(this, "handle");
        this.handle.setPos(8.0F, 8.0F, -8.0F);
        this.shield.addChild(this.handle);
        this.handle.setTextureOffset(0, 25).addBox(-8.0F, -8.5F, 7.0F, 5.0F, 5.0F, 2.0F, 0.0F, false);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.handle, this.shield);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.shield.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.shield);
    }
}