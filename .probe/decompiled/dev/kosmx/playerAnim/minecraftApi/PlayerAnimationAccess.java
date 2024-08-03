package dev.kosmx.playerAnim.minecraftApi;

import dev.kosmx.playerAnim.api.IPlayer;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.impl.event.Event;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class PlayerAnimationAccess {

    public static final Event<PlayerAnimationAccess.AnimationRegister> REGISTER_ANIMATION_EVENT = new Event<>(PlayerAnimationAccess.AnimationRegister.class, listeners -> (player, animationStack) -> {
        for (PlayerAnimationAccess.AnimationRegister listener : listeners) {
            listener.registerAnimation(player, animationStack);
        }
    });

    public static AnimationStack getPlayerAnimLayer(AbstractClientPlayer player) throws IllegalArgumentException {
        if (player instanceof IPlayer) {
            return ((IPlayer) player).getAnimationStack();
        } else {
            throw new IllegalArgumentException(player + " is not a player or library mixins failed");
        }
    }

    public static PlayerAnimationAccess.PlayerAssociatedAnimationData getPlayerAssociatedData(@NotNull AbstractClientPlayer player) {
        if (player instanceof IAnimatedPlayer animatedPlayer) {
            return new PlayerAnimationAccess.PlayerAssociatedAnimationData(animatedPlayer);
        } else {
            throw new IllegalArgumentException(player + " is not a player or library mixins failed");
        }
    }

    @FunctionalInterface
    public interface AnimationRegister {

        void registerAnimation(@NotNull AbstractClientPlayer var1, @NotNull AnimationStack var2);
    }

    public static class PlayerAssociatedAnimationData {

        @NotNull
        private final IAnimatedPlayer player;

        public PlayerAssociatedAnimationData(@NotNull IAnimatedPlayer player) {
            this.player = player;
        }

        @Nullable
        public IAnimation get(@NotNull ResourceLocation id) {
            return this.player.playerAnimator_getAnimation(id);
        }

        @Nullable
        public IAnimation set(@NotNull ResourceLocation id, @Nullable IAnimation animation) {
            return this.player.playerAnimator_setAnimation(id, animation);
        }
    }
}