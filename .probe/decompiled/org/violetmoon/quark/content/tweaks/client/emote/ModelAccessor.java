package org.violetmoon.quark.content.tweaks.client.emote;

import aurelienribon.tweenengine.TweenAccessor;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelAccessor implements TweenAccessor<HumanoidModel<?>> {

    public static final ModelAccessor INSTANCE = new ModelAccessor();

    private static final int ROT_X = 0;

    private static final int ROT_Y = 1;

    private static final int ROT_Z = 2;

    protected static final int MODEL_PROPS = 3;

    protected static final int BODY_PARTS = 7;

    protected static final int STATE_COUNT = 21;

    public static final int HEAD = 0;

    public static final int BODY = 3;

    public static final int RIGHT_ARM = 6;

    public static final int LEFT_ARM = 9;

    public static final int RIGHT_LEG = 12;

    public static final int LEFT_LEG = 15;

    public static final int MODEL = 18;

    public static final int HEAD_X = 0;

    public static final int HEAD_Y = 1;

    public static final int HEAD_Z = 2;

    public static final int BODY_X = 3;

    public static final int BODY_Y = 4;

    public static final int BODY_Z = 5;

    public static final int RIGHT_ARM_X = 6;

    public static final int RIGHT_ARM_Y = 7;

    public static final int RIGHT_ARM_Z = 8;

    public static final int LEFT_ARM_X = 9;

    public static final int LEFT_ARM_Y = 10;

    public static final int LEFT_ARM_Z = 11;

    public static final int RIGHT_LEG_X = 12;

    public static final int RIGHT_LEG_Y = 13;

    public static final int RIGHT_LEG_Z = 14;

    public static final int LEFT_LEG_X = 15;

    public static final int LEFT_LEG_Y = 16;

    public static final int LEFT_LEG_Z = 17;

    public static final int MODEL_X = 18;

    public static final int MODEL_Y = 19;

    public static final int MODEL_Z = 20;

    private final Map<HumanoidModel<?>, float[]> MODEL_VALUES = new WeakHashMap();

    public void resetModel(HumanoidModel<?> model) {
        this.MODEL_VALUES.remove(model);
    }

    public int getValues(HumanoidModel<?> target, int tweenType, float[] returnValues) {
        int axis = tweenType % 3;
        int bodyPart = tweenType - axis;
        if (bodyPart == 18) {
            if (!this.MODEL_VALUES.containsKey(target)) {
                returnValues[0] = 0.0F;
                return 1;
            } else {
                float[] values = (float[]) this.MODEL_VALUES.get(target);
                returnValues[0] = values[axis];
                return 1;
            }
        } else {
            ModelPart model = this.getBodyPart(target, bodyPart);
            if (model == null) {
                return 0;
            } else {
                switch(axis) {
                    case 0:
                        returnValues[0] = model.xRot;
                        break;
                    case 1:
                        returnValues[0] = model.yRot;
                        break;
                    case 2:
                        returnValues[0] = model.zRot;
                }
                return 1;
            }
        }
    }

    private ModelPart getBodyPart(HumanoidModel<?> model, int part) {
        return switch(part) {
            case 0 ->
                model.head;
            default ->
                null;
            case 3 ->
                model.body;
            case 6 ->
                model.rightArm;
            case 9 ->
                model.leftArm;
            case 12 ->
                model.rightLeg;
            case 15 ->
                model.leftLeg;
        };
    }

    public void setValues(HumanoidModel<?> target, int tweenType, float[] newValues) {
        int axis = tweenType % 3;
        int bodyPart = tweenType - axis;
        if (bodyPart == 18) {
            float[] values = (float[]) this.MODEL_VALUES.computeIfAbsent(target, k -> new float[3]);
            values[axis] = newValues[0];
        } else {
            ModelPart model = this.getBodyPart(target, bodyPart);
            this.messWithModel(target, model, axis, newValues[0]);
        }
    }

    private void messWithModel(HumanoidModel<?> biped, ModelPart part, int axis, float val) {
        this.setPartAxis(part, axis, val);
        if (biped instanceof PlayerModel) {
            this.messWithPlayerModel((PlayerModel<?>) biped, part, axis, val);
        }
    }

    private void messWithPlayerModel(PlayerModel<?> biped, ModelPart part, int axis, float val) {
        if (part == biped.f_102808_) {
            this.setPartAxis(biped.f_102809_, axis, val);
        } else if (part == biped.f_102812_) {
            this.setPartAxis(biped.leftSleeve, axis, val);
        } else if (part == biped.f_102811_) {
            this.setPartAxis(biped.rightSleeve, axis, val);
        } else if (part == biped.f_102814_) {
            this.setPartAxis(biped.leftPants, axis, val);
        } else if (part == biped.f_102813_) {
            this.setPartAxis(biped.rightPants, axis, val);
        } else if (part == biped.f_102810_) {
            this.setPartAxis(biped.jacket, axis, val);
        }
    }

    private void setPartAxis(ModelPart part, int axis, float val) {
        if (part != null) {
            switch(axis) {
                case 0:
                    part.xRot = val;
                    break;
                case 1:
                    part.yRot = val;
                    break;
                case 2:
                    part.zRot = val;
            }
        }
    }
}