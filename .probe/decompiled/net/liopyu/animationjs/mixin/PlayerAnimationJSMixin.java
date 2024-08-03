package net.liopyu.animationjs.mixin;

import dev.kosmx.playerAnim.core.util.Ease;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import java.util.List;
import java.util.function.Consumer;
import lio.playeranimatorapi.ModInit;
import lio.playeranimatorapi.API.PlayerAnimAPI;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.modifier.CommonModifier;
import net.liopyu.animationjs.events.IAnimationTrigger;
import net.liopyu.animationjs.network.server.AnimationStateTracker;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ Player.class })
public abstract class PlayerAnimationJSMixin implements IAnimationTrigger {

    @Unique
    private static final Logger animatorJS$logger = LogManager.getLogger(ModInit.class);

    @Unique
    private transient Object animatorJS$player = this;

    @Unique
    public final Player animatorJS$objectPlayer = (Player) this.animatorJS$player;

    @Unique
    private int animatorJS$cooldown;

    @Unique
    private ResourceLocation animatorJS$currentLocation;

    @Unique
    private double animatorJS$prevX;

    @Unique
    private double animatorJS$prevY;

    @Unique
    private double animatorJS$prevZ;

    @Unique
    private boolean animatorJS$isMoving = false;

    @Info("Is the player currently in motion")
    @Override
    public boolean animatorJS$isMoving() {
        return this.animatorJS$isMoving;
    }

    @Override
    public void animatorJS$setIsMoving(boolean b) {
        this.animatorJS$isMoving = b;
    }

    @Override
    public double animatorJS$getPrevX() {
        return this.animatorJS$prevX;
    }

    @Override
    public double animatorJS$getPrevY() {
        return this.animatorJS$prevY;
    }

    @Override
    public double animatorJS$getPrevZ() {
        return this.animatorJS$prevZ;
    }

    @Override
    public void animatorJS$setPrevX(double d) {
        this.animatorJS$prevX = d;
    }

    @Override
    public void animatorJS$setPrevY(double d) {
        this.animatorJS$prevY = d;
    }

    @Override
    public void animatorJS$setPrevZ(double d) {
        this.animatorJS$prevZ = d;
    }

    @Override
    public int animatorJS$getCooldown() {
        return this.animatorJS$cooldown;
    }

    @Override
    public void animatorJS$setCooldown(int i) {
        this.animatorJS$cooldown = i;
    }

    @Unique
    @Info("Determines if a playerAnimator animation is currently playing")
    @Override
    public boolean animatorJS$isAnimActive() {
        return AnimationStateTracker.getAnimationState(this.animatorJS$objectPlayer.m_20148_());
    }

    @Unique
    private boolean animatorJS$canPlay(ResourceLocation aN) {
        if (this.animatorJS$currentLocation == null) {
            this.animatorJS$currentLocation = aN;
            return true;
        } else if (!this.animatorJS$isAnimActive()) {
            this.animatorJS$currentLocation = aN;
            return true;
        } else if (!this.animatorJS$currentLocation.toString().equals(aN.toString())) {
            this.animatorJS$currentLocation = aN;
            return true;
        } else {
            return false;
        }
    }

    @Info(value = "Used to trigger animations off a server player. This can be\ncalled from any server player object.\n\nExample Usage:\n```javascript\nevent.player.triggerAnimation(\"animationjs:waving\")\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json") })
    @Override
    public void animatorJS$triggerAnimation(Object animationName) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
        if (this.animatorJS$objectPlayer.m_9236_().isClientSide()) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Unable to play animations from client scripts. Please use server scripts or the AnimationJS.universalController() server event.");
        } else if (this.animatorJS$player instanceof ServerPlayer serverPlayer) {
            if (animName == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: triggerAnimation. Must be a ResourceLocation.");
                return;
            }
            ResourceLocation aN = (ResourceLocation) animName;
            if (this.animatorJS$canPlay(aN)) {
                PlayerAnimAPI.playPlayerAnim(serverPlayer.serverLevel(), serverPlayer, aN);
            }
        }
    }

    @Unique
    @Info(value = "Used to trigger animations off a server player. This can be\ncalled from any server player object with the extra option for animations to overlap themselves.\n\nExample Usage:\n```javascript\nevent.player.triggerAnimation(\"animationjs:waving\", true)\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "canOverlapSelf", value = "Boolean: Whether the animation can overlap itself if it's already playing") })
    @Override
    public void animatorJS$triggerAnimation(Object animationName, boolean canOverlapSelf) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
        if (this.animatorJS$objectPlayer.m_9236_().isClientSide()) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Unable to play animations from client scripts. Please use server scripts or the AnimationJS.universalController() server event.");
        } else if (this.animatorJS$player instanceof ServerPlayer serverPlayer) {
            if (animName == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: triggerAnimation. Must be a ResourceLocation.");
                return;
            }
            ServerLevel serverLevel = serverPlayer.serverLevel();
            ResourceLocation aN = (ResourceLocation) animName;
            if (canOverlapSelf) {
                PlayerAnimAPI.playPlayerAnim(serverLevel, serverPlayer, aN);
            } else if (this.animatorJS$canPlay(aN)) {
                PlayerAnimAPI.playPlayerAnim(serverLevel, serverPlayer, aN);
            }
        }
    }

    @Unique
    @Info(value = "Used to trigger animations off the server player with customizable animation data.\n\nExample Usage:\n```javascript\nevent.player.triggerAnimation(\"animationjs:waving\", 1, \"linear\", true, false);\n```\n", params = { @Param(name = "animationID", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "transitionLength", value = "int: Duration of the transition length in milliseconds"), @Param(name = "easeID", value = "String: ID of the easing function to use for animation easing from the {@link dev.kosmx.playerAnim.core.util.Ease} class"), @Param(name = "firstPersonEnabled", value = "boolean: Whether the animation should be visible in first-person view"), @Param(name = "important", value = "boolean: Whether the animation is important and should override other animations") })
    @Override
    public void animatorJS$triggerAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationID, "resourcelocation");
        if (this.animatorJS$objectPlayer.m_9236_().isClientSide()) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Unable to play animations from client scripts. Please use server scripts or the AnimationJS.universalController() server event.");
        } else if (this.animatorJS$player instanceof ServerPlayer serverPlayer) {
            if (animName == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: triggerAnimation. Must be a ResourceLocation.");
                return;
            }
            Object ease = AnimationJSHelperClass.convertObjectToDesired(easeID, "ease");
            if (ease == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid easeID in field: triggerAnimation. Must be an easing type. Example: \"LINEAR\"");
                return;
            }
            ResourceLocation aN = (ResourceLocation) animName;
            if (this.animatorJS$canPlay(aN)) {
                int easingID = ((Ease) ease).getId();
                ServerLevel serverLevel = serverPlayer.serverLevel();
                PlayerAnimationData data = new PlayerAnimationData(serverPlayer.m_20148_(), aN, PlayerParts.allEnabled, null, transitionLength, easingID, firstPersonEnabled, important);
                PlayerAnimAPI.playPlayerAnim(serverLevel, serverPlayer, data);
            }
        }
    }

    @Unique
    @Info(value = "Used to trigger animations off the server player with customizable animation data.\n\nExample Usage:\n```javascript\nevent.triggerAnimation(\"animationjs:waving\", 1, \"linear\", true, false, [\"playeranimatorapi:mirroronalthand\"], parts => {\n\tparts.leftArm.setEnabled(false)\n});\n```\n", params = { @Param(name = "animationID", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "transitionLength", value = "int: Duration of the transition length in milliseconds"), @Param(name = "easeID", value = "String: ID of the easing function to use for animation easing from the {@link dev.kosmx.playerAnim.core.util.Ease} class"), @Param(name = "firstPersonEnabled", value = "boolean: Whether the animation should be visible in first-person view"), @Param(name = "important", value = "boolean: Whether the animation is important and should override other animations"), @Param(name = "modifiers", value = "List<String>: List of modifiers to apply to the animation, can also be null"), @Param(name = "partsConsumer", value = "Consumer<PlayerParts>: Consumer to modify player parts such as part visibility, rotation ect.") })
    @Override
    public void animatorJS$triggerAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important, List<?> modifiers, Consumer<PlayerParts> partsConsumer) {
        if (this.animatorJS$objectPlayer.m_9236_().isClientSide()) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Unable to play animations from client scripts. Please use server scripts or the AnimationJS.universalController() server event.");
        } else if (this.animatorJS$player instanceof ServerPlayer serverPlayer) {
            Object animName = AnimationJSHelperClass.convertObjectToDesired(animationID, "resourcelocation");
            if (animName == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: triggerAnimation. Must be a ResourceLocation.");
                return;
            }
            Object ease = AnimationJSHelperClass.convertObjectToDesired(easeID, "ease");
            if (ease == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid easeID in field: triggerAnimation. Must be an easing type. Example: \"LINEAR\"");
                return;
            }
            Object modifierlist = AnimationJSHelperClass.convertObjectToDesired(modifiers, "modifierlist");
            if (modifierlist == null && modifiers != null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid modifiers in field: triggerAnimation. Must be a string list of modifiers or null.");
                return;
            }
            ResourceLocation aN = (ResourceLocation) animName;
            if (this.animatorJS$canPlay(aN)) {
                int easingID = ((Ease) ease).getId();
                PlayerParts playerParts = new PlayerParts();
                if (partsConsumer != null) {
                    try {
                        partsConsumer.accept(playerParts);
                    } catch (Exception var17) {
                        AnimationJSHelperClass.logServerErrorMessageOnceCatchable("[AnimationJS]: Error in AnimationJS.universalController for method startAnimation", var17);
                    }
                }
                ServerLevel serverLevel = serverPlayer.serverLevel();
                PlayerAnimationData data = new PlayerAnimationData(serverPlayer.m_20148_(), aN, playerParts, (List<CommonModifier>) modifierlist, transitionLength, easingID, firstPersonEnabled, important);
                PlayerAnimAPI.playPlayerAnim(serverLevel, serverPlayer, data);
            }
        }
    }

    @Unique
    @Info(value = "Used to stop a certain player animation.\n\nExample Usage:\n```javascript\nevent.stopAnimation(\"animationjs:waving\")\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json") })
    @Override
    public void animatorJS$stopAnimation(Object animationName) {
        if (this.animatorJS$objectPlayer.m_9236_().isClientSide()) {
            AnimationJSHelperClass.logClientErrorMessageOnce("[AnimationJS]: Unable to play animations from client scripts. Please use server scripts or the AnimationJS.universalController() server event.");
        } else if (this.animatorJS$player instanceof ServerPlayer serverPlayer) {
            Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
            if (animName == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: stopAnimation. Must be a ResourceLocation.");
                return;
            }
            ServerLevel serverLevel = serverPlayer.serverLevel();
            ResourceLocation aN = (ResourceLocation) animName;
            PlayerAnimAPI.stopPlayerAnim(serverLevel, serverPlayer, aN);
        }
    }
}