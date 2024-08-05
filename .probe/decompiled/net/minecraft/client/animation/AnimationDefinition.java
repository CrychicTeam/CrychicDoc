package net.minecraft.client.animation;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.utils.Lists;

public record AnimationDefinition(float f_232255_, boolean f_232256_, Map<String, List<AnimationChannel>> f_232257_) {

    private final float lengthInSeconds;

    private final boolean looping;

    private final Map<String, List<AnimationChannel>> boneAnimations;

    public AnimationDefinition(float f_232255_, boolean f_232256_, Map<String, List<AnimationChannel>> f_232257_) {
        this.lengthInSeconds = f_232255_;
        this.looping = f_232256_;
        this.boneAnimations = f_232257_;
    }

    public static class Builder {

        private final float length;

        private final Map<String, List<AnimationChannel>> animationByBone = Maps.newHashMap();

        private boolean looping;

        public static AnimationDefinition.Builder withLength(float float0) {
            return new AnimationDefinition.Builder(float0);
        }

        private Builder(float float0) {
            this.length = float0;
        }

        public AnimationDefinition.Builder looping() {
            this.looping = true;
            return this;
        }

        public AnimationDefinition.Builder addAnimation(String string0, AnimationChannel animationChannel1) {
            ((List) this.animationByBone.computeIfAbsent(string0, p_232278_ -> Lists.newArrayList())).add(animationChannel1);
            return this;
        }

        public AnimationDefinition build() {
            return new AnimationDefinition(this.length, this.looping, this.animationByBone);
        }
    }
}