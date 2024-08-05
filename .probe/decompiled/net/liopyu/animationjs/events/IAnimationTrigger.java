package net.liopyu.animationjs.events;

import dev.latvian.mods.rhino.util.RemapForJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.List;
import java.util.function.Consumer;
import lio.playeranimatorapi.data.PlayerParts;

@RemapPrefixForJS("animatorJS$")
public interface IAnimationTrigger {

    void animatorJS$triggerAnimation(Object animationName);

    void animatorJS$triggerAnimation(Object animationName, boolean canOverlapSelf);

    void animatorJS$triggerAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important);

    void animatorJS$triggerAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important, List<?> modifiers, Consumer<PlayerParts> partsConsumer);

    void animatorJS$stopAnimation(Object animationName);

    boolean animatorJS$isMoving();

    @RemapForJS("isPlayingAnimation")
    boolean animatorJS$isAnimActive();

    void animatorJS$updateMovementBoolean();

    double animatorJS$getPrevX();

    double animatorJS$getPrevY();

    double animatorJS$getPrevZ();

    void animatorJS$setPrevX(double d);

    void animatorJS$setPrevY(double d);

    void animatorJS$setPrevZ(double d);

    int animatorJS$getCooldown();

    void animatorJS$setCooldown(int i);

    void animatorJS$setIsMoving(boolean b);
}