package lio.playeranimatorapi;

import lio.playeranimatorapi.playeranims.PlayerAnimations;
import lio.playeranimatorapi.registry.AnimModifierRegistry;

public class ModInitClient {

    public static void init() {
        PlayerAnimations.init();
        AnimModifierRegistry.register();
    }
}