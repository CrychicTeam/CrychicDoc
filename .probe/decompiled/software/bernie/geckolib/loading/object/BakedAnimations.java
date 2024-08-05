package software.bernie.geckolib.loading.object;

import java.util.Map;
import javax.annotation.Nullable;
import software.bernie.geckolib.core.animation.Animation;

public record BakedAnimations(Map<String, Animation> animations) {

    @Nullable
    public Animation getAnimation(String name) {
        return (Animation) this.animations.get(name);
    }
}