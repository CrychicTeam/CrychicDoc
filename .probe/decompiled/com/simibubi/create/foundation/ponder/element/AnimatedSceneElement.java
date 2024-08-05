package com.simibubi.create.foundation.ponder.element;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public abstract class AnimatedSceneElement extends PonderSceneElement {

    protected Vec3 fadeVec;

    protected LerpedFloat fade = LerpedFloat.linear().startWithValue(0.0);

    public void forceApplyFade(float fade) {
        this.fade.startWithValue((double) fade);
    }

    public void setFade(float fade) {
        this.fade.setValue((double) fade);
    }

    public void setFadeVec(Vec3 fadeVec) {
        this.fadeVec = fadeVec;
    }

    @Override
    public final void renderFirst(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float pt) {
        ms.pushPose();
        float currentFade = this.applyFade(ms, pt);
        this.renderFirst(world, buffer, ms, currentFade, pt);
        ms.popPose();
    }

    @Override
    public final void renderLayer(PonderWorld world, MultiBufferSource buffer, RenderType type, PoseStack ms, float pt) {
        ms.pushPose();
        float currentFade = this.applyFade(ms, pt);
        this.renderLayer(world, buffer, type, ms, currentFade, pt);
        ms.popPose();
    }

    @Override
    public final void renderLast(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float pt) {
        ms.pushPose();
        float currentFade = this.applyFade(ms, pt);
        this.renderLast(world, buffer, ms, currentFade, pt);
        ms.popPose();
    }

    protected float applyFade(PoseStack ms, float pt) {
        float currentFade = this.fade.getValue(pt);
        if (this.fadeVec != null) {
            TransformStack.cast(ms).translate(this.fadeVec.scale((double) (-1.0F + currentFade)));
        }
        return currentFade;
    }

    protected void renderLayer(PonderWorld world, MultiBufferSource buffer, RenderType type, PoseStack ms, float fade, float pt) {
    }

    protected void renderFirst(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
    }

    protected void renderLast(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
    }

    protected int lightCoordsFromFade(float fade) {
        int light = 15728880;
        if (fade != 1.0F) {
            light = (int) Mth.lerp(fade, 5.0F, 15.0F);
            light = LightTexture.pack(light, light);
        }
        return light;
    }
}