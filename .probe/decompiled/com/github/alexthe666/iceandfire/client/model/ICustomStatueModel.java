package com.github.alexthe666.iceandfire.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;

public interface ICustomStatueModel {

    void renderStatue(PoseStack var1, VertexConsumer var2, int var3, Entity var4);
}