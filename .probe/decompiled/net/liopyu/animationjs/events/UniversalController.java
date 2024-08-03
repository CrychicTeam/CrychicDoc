package net.liopyu.animationjs.events;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.latvian.mods.kubejs.player.SimplePlayerEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import lio.playeranimatorapi.API.PlayerAnimAPI;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.modifier.CommonModifier;
import net.liopyu.animationjs.network.server.AnimationStateTracker;
import net.liopyu.animationjs.utils.AnimationJSHelperClass;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class UniversalController extends SimplePlayerEventJS {

    private transient ResourceLocation currentLocation;

    public UniversalController(Player p) {
        super(p);
    }

    private boolean canPlay(ResourceLocation aN, Player player) {
        if (this.currentLocation == null) {
            this.currentLocation = aN;
            return true;
        } else if (!this.isAnimActive(player)) {
            this.currentLocation = aN;
            return true;
        } else if (!this.currentLocation.toString().equals(aN.toString())) {
            this.currentLocation = aN;
            return true;
        } else {
            return false;
        }
    }

    public boolean isAnimActive(Player player) {
        UUID playerUUID = player.m_20148_();
        if (player.m_20194_().isDedicatedServer()) {
            return AnimationStateTracker.getAnimationState(playerUUID);
        } else {
            AbstractClientPlayer clientPlayer = AnimationJSHelperClass.getClientPlayerByUUID(playerUUID);
            if (clientPlayer != null) {
                ModifierLayer<IAnimation> anim = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(clientPlayer).get(new ResourceLocation("liosplayeranimatorapi", "factory"));
                return anim == null ? false : anim.isActive();
            } else {
                return false;
            }
        }
    }

    private ServerPlayer getServerPlayer() {
        Player var2 = this.getPlayer();
        return var2 instanceof ServerPlayer ? (ServerPlayer) var2 : null;
    }

    @Info(value = "Used to play animations on player tick.\n\nExample Usage:\n```javascript\nevent.startAnimation(\"animationjs:waving\")\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json") })
    public void startAnimation(Object animationName) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
        if (animName == null) {
            AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: startAnimation. Must be a ResourceLocation.");
        } else {
            ResourceLocation aN = (ResourceLocation) animName;
            if (this.canPlay(aN, this.getPlayer())) {
                PlayerAnimAPI.playPlayerAnim(this.getServerPlayer().serverLevel(), this.getServerPlayer(), aN);
            }
        }
    }

    @Info(value = "Used to play animations on player tick with the option\nto have animations overlap themselves when played.\n\nExample Usage:\n```javascript\nevent.startAnimation(\"animationjs:waving\", true)\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "canOverlapSelf", value = "Boolean: Whether the animation can overlap itself if it's already playing") })
    public void startAnimation(Object animationName, boolean canOverlapSelf) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
        if (animName == null) {
            AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: startAnimation. Must be a ResourceLocation.");
        } else {
            ServerLevel serverLevel = this.getServerPlayer().serverLevel();
            ResourceLocation aN = (ResourceLocation) animName;
            if (canOverlapSelf) {
                PlayerAnimAPI.playPlayerAnim(serverLevel, this.getServerPlayer(), aN);
            } else if (this.canPlay(aN, this.getPlayer())) {
                PlayerAnimAPI.playPlayerAnim(serverLevel, this.getServerPlayer(), aN);
            }
        }
    }

    @Info(value = "Used to play animations on player tick with customizable animation data.\n\nExample Usage:\n```javascript\nevent.startAnimation(\"animationjs:waving\", 1, \"linear\", true, false);\n```\n", params = { @Param(name = "animationID", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "transitionLength", value = "int: Duration of the transition length in milliseconds"), @Param(name = "easeID", value = "String: ID of the easing function to use for animation easing from the {@link dev.kosmx.playerAnim.core.util.Ease} class"), @Param(name = "firstPersonEnabled", value = "boolean: Whether the animation should be visible in first-person view"), @Param(name = "important", value = "boolean: Whether the animation is important and should override other animations") })
    public void startAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationID, "resourcelocation");
        if (animName == null) {
            AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: startAnimation. Must be a ResourceLocation.");
        } else {
            Object ease = AnimationJSHelperClass.convertObjectToDesired(easeID, "ease");
            if (ease == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid easeID in field: startAnimation. Must be an easing type. Example: \"LINEAR\"");
            } else {
                ResourceLocation aN = (ResourceLocation) animName;
                if (this.canPlay(aN, this.getPlayer())) {
                    int easingID = ((Ease) ease).getId();
                    ServerLevel serverLevel = this.getServerPlayer().serverLevel();
                    PlayerAnimationData data = new PlayerAnimationData(this.getServerPlayer().m_20148_(), aN, PlayerParts.allEnabled, null, transitionLength, easingID, firstPersonEnabled, important);
                    PlayerAnimAPI.playPlayerAnim(serverLevel, this.getServerPlayer(), data);
                }
            }
        }
    }

    @Info(value = "Used to play animations on player tick with customizable animation data.\n\nExample Usage:\n```javascript\nevent.startAnimation(\"animationjs:smith\", 1, \"linear\", true, false, [\"playeranimatorapi:headposboundcamera\"], parts => {\n\tparts.leftArm.setEnabled(false);\n});\n```\n", params = { @Param(name = "animationID", value = "ResourceLocation: The name of the animation specified in the json"), @Param(name = "transitionLength", value = "int: Duration of the transition length in milliseconds"), @Param(name = "easeID", value = "String: ID of the easing function to use for animation easing from the {@link dev.kosmx.playerAnim.core.util.Ease} class"), @Param(name = "firstPersonEnabled", value = "boolean: Whether the animation should be visible in first-person view"), @Param(name = "important", value = "boolean: Whether the animation is important and should override other animations"), @Param(name = "modifiers", value = "List<String>: List of modifiers to apply to the animation"), @Param(name = "partsConsumer", value = "Consumer<PlayerParts>: Consumer to modify player parts such as part visibility, rotation ect.") })
    public void startAnimation(Object animationID, int transitionLength, String easeID, boolean firstPersonEnabled, boolean important, List<?> modifiers, Consumer<PlayerParts> partsConsumer) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationID, "resourcelocation");
        if (animName == null) {
            AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: startAnimation. Must be a ResourceLocation.");
        } else {
            Object ease = AnimationJSHelperClass.convertObjectToDesired(easeID, "ease");
            if (ease == null) {
                AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid easeID in field: startAnimation. Must be an easing type. Example: \"LINEAR\"");
            } else {
                Object modifierlist = AnimationJSHelperClass.convertObjectToDesired(modifiers, "modifierlist");
                if (modifierlist == null && modifiers != null) {
                    AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid modifiers in field: startAnimation. Must be a string list of modifiers or null.");
                } else {
                    ResourceLocation aN = (ResourceLocation) animName;
                    if (this.canPlay(aN, this.getPlayer())) {
                        int easingID = ((Ease) ease).getId();
                        PlayerParts playerParts = new PlayerParts();
                        if (partsConsumer != null) {
                            try {
                                partsConsumer.accept(playerParts);
                            } catch (Exception var16) {
                                AnimationJSHelperClass.logServerErrorMessageOnceCatchable("[AnimationJS]: Error in AnimationJS.universalController for method startAnimation", var16);
                            }
                        }
                        ServerLevel serverLevel = this.getServerPlayer().serverLevel();
                        PlayerAnimationData data = new PlayerAnimationData(this.getServerPlayer().m_20148_(), aN, playerParts, (List<CommonModifier>) modifierlist, transitionLength, easingID, firstPersonEnabled, important);
                        PlayerAnimAPI.playPlayerAnim(serverLevel, this.getServerPlayer(), data);
                    }
                }
            }
        }
    }

    @Info(value = "Used to stop a certain player animation.\n\nExample Usage:\n```javascript\nevent.stopAnimation(\"animationjs:waving\")\n```\n", params = { @Param(name = "animationName", value = "ResourceLocation: The name of the animation specified in the json") })
    public void stopAnimation(Object animationName) {
        Object animName = AnimationJSHelperClass.convertObjectToDesired(animationName, "resourcelocation");
        if (animName == null) {
            AnimationJSHelperClass.logServerErrorMessageOnce("[AnimationJS]: Invalid animation name in field: stopAnimation. Must be a ResourceLocation.");
        } else {
            ServerLevel serverLevel = this.getServerPlayer().serverLevel();
            ResourceLocation aN = (ResourceLocation) animName;
            PlayerAnimAPI.stopPlayerAnim(serverLevel, this.getServerPlayer(), aN);
        }
    }
}