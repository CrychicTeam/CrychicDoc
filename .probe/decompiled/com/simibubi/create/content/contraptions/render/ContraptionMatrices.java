package com.simibubi.create.content.contraptions.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;

public class ContraptionMatrices {

    private final PoseStack modelViewProjection = new PoseStack();

    private final PoseStack viewProjection = new PoseStack();

    private final PoseStack model = new PoseStack();

    private final Matrix4f world = new Matrix4f();

    private final Matrix4f light = new Matrix4f();

    private boolean ready;

    public void setup(PoseStack viewProjection, AbstractContraptionEntity entity) {
        float partialTicks = AnimationTickHolder.getPartialTicks();
        this.viewProjection.pushPose();
        transform(this.viewProjection, viewProjection);
        this.model.pushPose();
        entity.applyLocalTransforms(this.model, partialTicks);
        this.modelViewProjection.pushPose();
        transform(this.modelViewProjection, viewProjection);
        transform(this.modelViewProjection, this.model);
        translateToEntity(this.world, entity, partialTicks);
        this.light.set(this.world);
        this.light.mul(this.model.last().pose());
        this.ready = true;
    }

    public void clear() {
        clearStack(this.modelViewProjection);
        clearStack(this.viewProjection);
        clearStack(this.model);
        this.world.identity();
        this.light.identity();
        this.ready = false;
    }

    public PoseStack getModelViewProjection() {
        return this.modelViewProjection;
    }

    public PoseStack getViewProjection() {
        return this.viewProjection;
    }

    public PoseStack getModel() {
        return this.model;
    }

    public Matrix4f getWorld() {
        return this.world;
    }

    public Matrix4f getLight() {
        return this.light;
    }

    public boolean isReady() {
        return this.ready;
    }

    public static void transform(PoseStack ms, PoseStack transform) {
        ms.last().pose().mul(transform.last().pose());
        ms.last().normal().mul(transform.last().normal());
    }

    public static void translateToEntity(Matrix4f matrix, Entity entity, float partialTicks) {
        double x = Mth.lerp((double) partialTicks, entity.xOld, entity.getX());
        double y = Mth.lerp((double) partialTicks, entity.yOld, entity.getY());
        double z = Mth.lerp((double) partialTicks, entity.zOld, entity.getZ());
        matrix.setTranslation((float) x, (float) y, (float) z);
    }

    public static void clearStack(PoseStack ms) {
        while (!ms.clear()) {
            ms.popPose();
        }
    }
}