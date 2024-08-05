package software.bernie.geckolib.core.animation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.keyframe.BoneAnimation;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;

public record Animation(String name, double length, Animation.LoopType loopType, BoneAnimation[] boneAnimations, Animation.Keyframes keyFrames) {

    static Animation generateWaitAnimation(double length) {
        return new Animation("internal.wait", length, Animation.LoopType.PLAY_ONCE, new BoneAnimation[0], new Animation.Keyframes(new SoundKeyframeData[0], new ParticleKeyframeData[0], new CustomInstructionKeyframeData[0]));
    }

    public static record Keyframes(SoundKeyframeData[] sounds, ParticleKeyframeData[] particles, CustomInstructionKeyframeData[] customInstructions) {
    }

    @FunctionalInterface
    public interface LoopType {

        Map<String, Animation.LoopType> LOOP_TYPES = new ConcurrentHashMap(4);

        Animation.LoopType DEFAULT = (animatable, controller, currentAnimation) -> currentAnimation.loopType().shouldPlayAgain(animatable, controller, currentAnimation);

        Animation.LoopType PLAY_ONCE = register("play_once", register("false", (animatable, controller, currentAnimation) -> false));

        Animation.LoopType HOLD_ON_LAST_FRAME = register("hold_on_last_frame", (animatable, controller, currentAnimation) -> {
            controller.animationState = AnimationController.State.PAUSED;
            return true;
        });

        Animation.LoopType LOOP = register("loop", register("true", (animatable, controller, currentAnimation) -> true));

        boolean shouldPlayAgain(GeoAnimatable var1, AnimationController<? extends GeoAnimatable> var2, Animation var3);

        static Animation.LoopType fromJson(JsonElement json) {
            if (json != null && json.isJsonPrimitive()) {
                JsonPrimitive primitive = json.getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    return primitive.getAsBoolean() ? LOOP : PLAY_ONCE;
                } else {
                    return primitive.isString() ? fromString(primitive.getAsString()) : PLAY_ONCE;
                }
            } else {
                return PLAY_ONCE;
            }
        }

        static Animation.LoopType fromString(String name) {
            return (Animation.LoopType) LOOP_TYPES.getOrDefault(name, PLAY_ONCE);
        }

        static Animation.LoopType register(String name, Animation.LoopType loopType) {
            LOOP_TYPES.put(name, loopType);
            return loopType;
        }
    }
}