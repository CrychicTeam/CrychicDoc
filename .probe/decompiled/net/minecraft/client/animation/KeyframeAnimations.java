package net.minecraft.client.animation;

import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class KeyframeAnimations {

    public static void animate(HierarchicalModel<?> hierarchicalModel0, AnimationDefinition animationDefinition1, long long2, float float3, Vector3f vectorF4) {
        float $$5 = getElapsedSeconds(animationDefinition1, long2);
        for (Entry<String, List<AnimationChannel>> $$6 : animationDefinition1.boneAnimations().entrySet()) {
            Optional<ModelPart> $$7 = hierarchicalModel0.getAnyDescendantWithName((String) $$6.getKey());
            List<AnimationChannel> $$8 = (List<AnimationChannel>) $$6.getValue();
            $$7.ifPresent(p_232330_ -> $$8.forEach(p_288241_ -> {
                Keyframe[] $$5x = p_288241_.keyframes();
                int $$6x = Math.max(0, Mth.binarySearch(0, $$5x.length, p_232315_ -> $$5 <= $$5x[p_232315_].timestamp()) - 1);
                int $$7x = Math.min($$5x.length - 1, $$6x + 1);
                Keyframe $$8x = $$5x[$$6x];
                Keyframe $$9 = $$5x[$$7x];
                float $$10 = $$5 - $$8x.timestamp();
                float $$11;
                if ($$7x != $$6x) {
                    $$11 = Mth.clamp($$10 / ($$9.timestamp() - $$8x.timestamp()), 0.0F, 1.0F);
                } else {
                    $$11 = 0.0F;
                }
                $$9.interpolation().apply(vectorF4, $$11, $$5x, $$6x, $$7x, float3);
                p_288241_.target().apply(p_232330_, vectorF4);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition animationDefinition0, long long1) {
        float $$2 = (float) long1 / 1000.0F;
        return animationDefinition0.looping() ? $$2 % animationDefinition0.lengthInSeconds() : $$2;
    }

    public static Vector3f posVec(float float0, float float1, float float2) {
        return new Vector3f(float0, -float1, float2);
    }

    public static Vector3f degreeVec(float float0, float float1, float float2) {
        return new Vector3f(float0 * (float) (Math.PI / 180.0), float1 * (float) (Math.PI / 180.0), float2 * (float) (Math.PI / 180.0));
    }

    public static Vector3f scaleVec(double double0, double double1, double double2) {
        return new Vector3f((float) (double0 - 1.0), (float) (double1 - 1.0), (float) (double2 - 1.0));
    }
}