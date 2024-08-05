package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

public class RotateSceneInstruction extends PonderInstruction {

    private float xRot;

    private float yRot;

    private boolean relative;

    public RotateSceneInstruction(float xRot, float yRot, boolean relative) {
        this.xRot = xRot;
        this.yRot = yRot;
        this.relative = relative;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void tick(PonderScene scene) {
        PonderScene.SceneTransform transform = scene.getTransform();
        float targetX = this.relative ? transform.xRotation.getChaseTarget() + this.xRot : this.xRot;
        float targetY = this.relative ? transform.yRotation.getChaseTarget() + this.yRot : this.yRot;
        transform.xRotation.chase((double) targetX, 0.1F, LerpedFloat.Chaser.EXP);
        transform.yRotation.chase((double) targetY, 0.1F, LerpedFloat.Chaser.EXP);
    }
}