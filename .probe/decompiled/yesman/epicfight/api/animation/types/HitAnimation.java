package yesman.epicfight.api.animation.types;

import java.util.Map;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class HitAnimation extends MainFrameAnimation {

    public HitAnimation(float convertTime, String path, Armature armature) {
        super(convertTime, path, armature);
        this.stateSpectrumBlueprint.clear().newTimePair(0.0F, Float.MAX_VALUE).addState(EntityState.TURNING_LOCKED, true).addState(EntityState.MOVEMENT_LOCKED, true).addState(EntityState.UPDATE_LIVING_MOTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).addState(EntityState.CAN_SKILL_EXECUTION, false).addState(EntityState.INACTION, true).addState(EntityState.HURT_LEVEL, 1);
    }

    @Override
    public void begin(LivingEntityPatch<?> entitypatch) {
        super.begin(entitypatch);
        entitypatch.cancelAnyAction();
    }

    @Override
    public void setLinkAnimation(Pose pose1, float convertTimeModifier, LivingEntityPatch<?> entitypatch, LinkAnimation dest) {
        dest.getTransfroms().clear();
        dest.setTotalTime(convertTimeModifier + this.convertTime);
        dest.setNextAnimation(this);
        Map<String, JointTransform> data1 = pose1.getJointTransformData();
        Map<String, JointTransform> data2 = super.getPoseByTime(entitypatch, 0.0F, 0.0F).getJointTransformData();
        Map<String, JointTransform> data3 = super.getPoseByTime(entitypatch, this.totalTime - 1.0E-5F, 0.0F).getJointTransformData();
        for (String jointName : data1.keySet()) {
            if (data1.containsKey(jointName) && data2.containsKey(jointName)) {
                Keyframe[] keyframes = new Keyframe[] { new Keyframe(0.0F, (JointTransform) data1.get(jointName)), new Keyframe(this.convertTime, (JointTransform) data2.get(jointName)), new Keyframe(this.convertTime + 0.033F, (JointTransform) data3.get(jointName)), new Keyframe(convertTimeModifier + this.convertTime, (JointTransform) data3.get(jointName)) };
                TransformSheet sheet = new TransformSheet(keyframes);
                dest.addSheet(jointName, sheet);
            }
        }
    }

    @Override
    public Pose getPoseByTime(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
        return super.getPoseByTime(entitypatch, this.getTotalTime() - 1.0E-6F, 0.0F);
    }
}