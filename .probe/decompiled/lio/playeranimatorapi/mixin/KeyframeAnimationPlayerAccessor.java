package lio.playeranimatorapi.mixin;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ KeyframeAnimationPlayer.class })
public interface KeyframeAnimationPlayerAccessor {

    @Accessor(remap = false)
    float getTickDelta();
}