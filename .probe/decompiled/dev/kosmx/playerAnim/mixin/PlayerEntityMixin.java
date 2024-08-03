package dev.kosmx.playerAnim.mixin;

import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import dev.kosmx.playerAnim.impl.animation.AnimationApplier;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Player.class })
public abstract class PlayerEntityMixin implements IAnimatedPlayer {

    @Unique
    private final Map<ResourceLocation, IAnimation> modAnimationData = new HashMap();

    @Unique
    private final AnimationStack animationStack = this.createAnimationStack();

    @Unique
    private final AnimationApplier animationApplier = new AnimationApplier(this.animationStack);

    @Unique
    private AnimationStack createAnimationStack() {
        AnimationStack stack = new AnimationStack();
        if (AbstractClientPlayer.class.isInstance(this)) {
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.prepareAnimations((AbstractClientPlayer) this, stack, this.modAnimationData);
            PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.invoker().registerAnimation((AbstractClientPlayer) this, stack);
        }
        return stack;
    }

    @Override
    public AnimationStack getAnimationStack() {
        return this.animationStack;
    }

    @Override
    public AnimationApplier playerAnimator_getAnimation() {
        return this.animationApplier;
    }

    @Nullable
    @Override
    public IAnimation playerAnimator_getAnimation(@NotNull ResourceLocation id) {
        return (IAnimation) this.modAnimationData.get(id);
    }

    @Nullable
    @Override
    public IAnimation playerAnimator_setAnimation(@NotNull ResourceLocation id, @Nullable IAnimation animation) {
        return animation == null ? (IAnimation) this.modAnimationData.remove(id) : (IAnimation) this.modAnimationData.put(id, animation);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void tick(CallbackInfo ci) {
        if (AbstractClientPlayer.class.isInstance(this)) {
            this.animationStack.tick();
        }
    }
}