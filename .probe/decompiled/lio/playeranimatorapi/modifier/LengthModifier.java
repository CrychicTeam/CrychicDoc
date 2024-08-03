package lio.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import lio.playeranimatorapi.playeranims.PlayerAnimations;

public class LengthModifier extends SpeedModifier {

    public LengthModifier(CustomModifierLayer layer, float desiredLength) {
        super(1.0F);
        if (desiredLength > 0.0F) {
            this.speed = (Float) PlayerAnimations.animLengthsMap.get(layer.data.animationID()) / desiredLength;
        }
    }
}