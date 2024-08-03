package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

public class ModelChainTie extends BasicEntityModel<EntityChainTie> {

    public BasicModelPart knotRenderer;

    public ModelChainTie() {
        this(0, 0, 32, 32);
    }

    public ModelChainTie(int width, int height, int texWidth, int texHeight) {
        this.knotRenderer = new BasicModelPart(this, width, height);
        this.knotRenderer.addBox(-4.0F, 2.0F, -4.0F, 8.0F, 12.0F, 8.0F, 1.0F);
        this.knotRenderer.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void setupAnim(@NotNull EntityChainTie entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.knotRenderer.rotateAngleY = netHeadYaw * (float) (Math.PI / 180.0);
        this.knotRenderer.rotateAngleX = headPitch * (float) (Math.PI / 180.0);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.knotRenderer);
    }
}