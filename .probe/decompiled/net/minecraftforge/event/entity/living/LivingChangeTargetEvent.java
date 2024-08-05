package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingChangeTargetEvent extends LivingEvent {

    private final LivingChangeTargetEvent.ILivingTargetType targetType;

    private final LivingEntity originalTarget;

    private LivingEntity newTarget;

    public LivingChangeTargetEvent(LivingEntity entity, LivingEntity originalTarget, LivingChangeTargetEvent.ILivingTargetType targetType) {
        super(entity);
        this.originalTarget = originalTarget;
        this.newTarget = originalTarget;
        this.targetType = targetType;
    }

    public LivingEntity getNewTarget() {
        return this.newTarget;
    }

    public void setNewTarget(LivingEntity newTarget) {
        this.newTarget = newTarget;
    }

    public LivingChangeTargetEvent.ILivingTargetType getTargetType() {
        return this.targetType;
    }

    public LivingEntity getOriginalTarget() {
        return this.originalTarget;
    }

    public interface ILivingTargetType {
    }

    public static enum LivingTargetType implements LivingChangeTargetEvent.ILivingTargetType {

        MOB_TARGET, BEHAVIOR_TARGET
    }
}