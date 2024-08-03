package yesman.epicfight.api.animation.types;

import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class MovementAnimation extends StaticAnimation {

    public MovementAnimation(boolean isRepeat, String path, Armature armature) {
        super(0.15F, isRepeat, path, armature);
    }

    public MovementAnimation(float convertTime, boolean isRepeat, String path, Armature armature) {
        super(convertTime, isRepeat, path, armature);
    }

    @Override
    public float getPlaySpeed(LivingEntityPatch<?> entitypatch) {
        float movementSpeed = 1.0F;
        if (Math.abs(entitypatch.getOriginal().walkAnimation.speed() - entitypatch.getOriginal().walkAnimation.speed(1.0F)) < 0.007F) {
            movementSpeed *= entitypatch.getOriginal().walkAnimation.speed() * 1.16F;
        }
        return movementSpeed;
    }

    @Override
    public boolean canBePlayedReverse() {
        return true;
    }
}