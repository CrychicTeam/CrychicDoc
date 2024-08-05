package lio.playeranimatorapi.liolib;

import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.playeranims.ConditionalAnimations;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import lio.playeranimatorapi.playeranims.PlayerAnimations;
import net.liopyu.liolib.core.animation.AnimatableManager;
import net.liopyu.liolib.core.animation.AnimationController;
import net.liopyu.liolib.core.animation.EasingType;
import net.liopyu.liolib.core.animation.RawAnimation;
import net.liopyu.liolib.core.animation.Animation.LoopType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

public class ModGeckoLibUtilsClient {

    public static PlayerAnimationRenderer currentPlayerRenderer;

    public static void playGeckoAnimation(AbstractClientPlayer player, PlayerAnimationData data, float speed) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId((long) player.m_19879_());
        AnimationController<AbstractClientPlayer> controller = (AnimationController<AbstractClientPlayer>) manager.getAnimationControllers().get("liosplayeranimatorapi");
        controller.triggerableAnim(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), RawAnimation.begin().then(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath(), LoopType.DEFAULT));
        controller.setAnimationSpeed((double) speed);
        controller.transitionLength(data.fadeLength() > -1 ? data.fadeLength() : 0);
        controller.tryTriggerAnimation(ConditionalAnimations.getAnimationForCurrentConditions(data).getPath());
    }

    public static void stopGeckoAnimation(AbstractClientPlayer player) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId((long) player.m_19879_());
        AnimationController<AbstractClientPlayer> controller = (AnimationController<AbstractClientPlayer>) manager.getAnimationControllers().get("liosplayeranimatorapi");
        controller.stop();
    }

    public static void tick(AbstractClientPlayer player, CustomModifierLayer animationContainer) {
        AnimatableManager<AbstractClientPlayer> manager = player.getAnimatableInstanceCache().getManagerForId((long) player.m_19879_());
        AnimationController<AbstractClientPlayer> controller = (AnimationController<AbstractClientPlayer>) manager.getAnimationControllers().get("liosplayeranimatorapi");
        if (!controller.isPlayingTriggeredAnimation()) {
            playGeckoAnimation(player, animationContainer.data, animationContainer.getSpeed());
        }
    }

    public static EasingType getEasingTypeForID(Player player) {
        CustomModifierLayer animationContainer = PlayerAnimations.getModifierLayer((AbstractClientPlayer) player);
        switch(animationContainer.data.easeID()) {
            case 0:
                return EasingType.LINEAR;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                return null;
            case 6:
                return EasingType.EASE_IN_SINE;
            case 7:
                return EasingType.EASE_OUT_SINE;
            case 8:
                return EasingType.EASE_IN_OUT_SINE;
            case 9:
                return EasingType.EASE_IN_CUBIC;
            case 10:
                return EasingType.EASE_OUT_CUBIC;
            case 11:
                return EasingType.EASE_IN_OUT_CUBIC;
            case 12:
                return EasingType.EASE_IN_QUAD;
            case 13:
                return EasingType.EASE_OUT_QUAD;
            case 14:
                return EasingType.EASE_IN_OUT_QUAD;
            case 15:
                return EasingType.EASE_IN_QUART;
            case 16:
                return EasingType.EASE_OUT_QUART;
            case 17:
                return EasingType.EASE_IN_OUT_QUART;
            case 18:
                return EasingType.EASE_IN_QUINT;
            case 19:
                return EasingType.EASE_OUT_QUINT;
            case 20:
                return EasingType.EASE_IN_OUT_QUINT;
            case 21:
                return EasingType.EASE_IN_EXPO;
            case 22:
                return EasingType.EASE_OUT_EXPO;
            case 23:
                return EasingType.EASE_IN_OUT_EXPO;
            case 24:
                return EasingType.EASE_IN_CIRC;
            case 25:
                return EasingType.EASE_OUT_CIRC;
            case 26:
                return EasingType.EASE_IN_OUT_CIRC;
            case 27:
                return EasingType.EASE_IN_BACK;
            case 28:
                return EasingType.EASE_OUT_BACK;
            case 29:
                return EasingType.EASE_IN_OUT_BACK;
            case 30:
                return EasingType.EASE_IN_ELASTIC;
            case 31:
                return EasingType.EASE_OUT_ELASTIC;
            case 32:
                return EasingType.EASE_IN_OUT_ELASTIC;
            case 33:
                return EasingType.EASE_IN_BOUNCE;
            case 34:
                return EasingType.EASE_OUT_BOUNCE;
            case 35:
                return EasingType.EASE_IN_OUT_BOUNCE;
        }
    }
}