package dev.kosmx.playerAnim.api;

import dev.kosmx.playerAnim.api.layered.AnimationStack;

public abstract class AnimUtils {

    @Deprecated
    public static boolean disableFirstPersonAnim = true;

    public static AnimationStack getPlayerAnimLayer(Object player) throws IllegalArgumentException {
        if (player instanceof IPlayer) {
            return ((IPlayer) player).getAnimationStack();
        } else {
            throw new IllegalArgumentException(player + " is not a player or library mixins failed");
        }
    }
}