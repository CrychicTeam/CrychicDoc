package dev.kosmx.playerAnim.impl;

import dev.kosmx.playerAnim.api.IPlayer;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface IAnimatedPlayer extends IPlayer {

    @Deprecated(forRemoval = true)
    default AnimationApplier getAnimation() {
        return this.playerAnimator_getAnimation();
    }

    AnimationApplier playerAnimator_getAnimation();

    @Nullable
    IAnimation playerAnimator_getAnimation(@NotNull ResourceLocation var1);

    @Nullable
    IAnimation playerAnimator_setAnimation(@NotNull ResourceLocation var1, @Nullable IAnimation var2);
}