package net.liopyu.animationjs.network.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnimationStateTracker {

    private static final Map<UUID, Boolean> animationStates = new HashMap();

    public static void setAnimationState(UUID playerUUID, boolean isActive) {
        animationStates.put(playerUUID, isActive);
    }

    public static boolean getAnimationState(UUID playerUUID) {
        return (Boolean) animationStates.getOrDefault(playerUUID, false);
    }
}