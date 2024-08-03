package lio.playeranimatorapi.playeranims;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import lio.liosmultiloaderutils.utils.NetworkManager;
import lio.liosmultiloaderutils.utils.Platform;
import lio.liosmultiloaderutils.utils.NetworkManager.Side;
import lio.playeranimatorapi.ModInit;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.liolib.ModGeckoLibUtilsClient;
import lio.playeranimatorapi.modifier.CommonModifier;
import lio.playeranimatorapi.registry.AnimModifierRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class PlayerAnimations {

    private static final Logger logger = LogManager.getLogger(ModInit.class);

    public static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    public static Map<ResourceLocation, Float> animLengthsMap;

    public static Map<ResourceLocation, ResourceLocation> geckoMap;

    public static final ResourceLocation playerAnimPacket = new ResourceLocation("liosplayeranimatorapi", "player_anim");

    public static final ResourceLocation playerAnimStopPacket = new ResourceLocation("liosplayeranimatorapi", "player_anim_stop");

    public static final ResourceLocation animationLayerId = new ResourceLocation("liosplayeranimatorapi", "factory");

    public static void init() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(animationLayerId, 42, player -> new CustomModifierLayer(player));
        NetworkManager.registerReceiver(Side.S2C, playerAnimPacket, (buf, context) -> receivePacket(buf.readUtf()));
        NetworkManager.registerReceiver(Side.S2C, playerAnimStopPacket, (buf, context) -> stopAnimation(buf.readUUID(), buf.readResourceLocation()));
    }

    public static void stopAnimation(UUID playerUUID, ResourceLocation animationID) {
        stopAnimation((AbstractClientPlayer) Minecraft.getInstance().level.m_46003_(playerUUID), animationID);
    }

    public static void stopAnimation(AbstractClientPlayer player, ResourceLocation animationID) {
        CustomModifierLayer animationContainer = getModifierLayer(player);
        if (animationContainer != null && animationContainer.isActive() && animationContainer.data.animationID().equals(animationID)) {
            animationContainer.animPlayer.stop();
            if (Platform.isModLoaded("liolib")) {
                ModGeckoLibUtilsClient.stopGeckoAnimation(player);
            }
        }
    }

    public static void receivePacket(String jsonData) {
        PlayerAnimationData data = (PlayerAnimationData) PlayerAnimationData.CODEC.parse(JsonOps.INSTANCE, (JsonElement) gson.fromJson(jsonData, JsonElement.class)).getOrThrow(true, logger::warn);
        AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.m_46003_(data.playerUUID());
        playAnimation(player, data);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data) {
        playAnimation(player, data, true);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data, boolean replaceTick) {
        playAnimation(player, data, data.parts(), data.modifiers(), data.fadeLength(), data.easeID(), data.firstPersonEnabled(), replaceTick);
    }

    public static void playAnimation(AbstractClientPlayer player, PlayerAnimationData data, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, boolean firstPersonEnabled, boolean replaceTick) {
        try {
            CustomModifierLayer animationContainer = getModifierLayer(player);
            ResourceLocation baseAnimationID = data.animationID();
            if (baseAnimationID.toString().equals("null:null")) {
                return;
            }
            if (animationContainer.data != null && animationContainer.data.important() && animationContainer.isActive() && !data.important()) {
                return;
            }
            animationContainer.setAnimationData(data);
            ResourceLocation animationID = ConditionalAnimations.getAnimationForCurrentConditions(data);
            animationContainer.setCurrentAnimationLocation(animationID);
            KeyframeAnimation anim = PlayerAnimationRegistry.getAnimation(animationID);
            if (replaceTick) {
                animationContainer.removeAllModifiers();
                if (modifiers != null) {
                    for (CommonModifier commonModifier : modifiers) {
                        if (AnimModifierRegistry.getModifiers().containsKey(commonModifier.ID)) {
                            try {
                                animationContainer.addModifier((AbstractModifier) ((BiFunction) AnimModifierRegistry.getModifiers().get(commonModifier.ID)).apply(animationContainer, commonModifier.data));
                            } catch (UnsupportedOperationException | NullPointerException var24) {
                                ModInit.LOGGER.error("Failed to apply modifier: " + commonModifier.ID + " :" + var24);
                            }
                        }
                    }
                }
            }
            KeyframeAnimation.AnimationBuilder builder = anim.mutableCopy();
            KeyframeAnimation.StateCollection body = builder.getPart("body");
            body.x.setEnabled(parts.body.x);
            body.y.setEnabled(parts.body.y);
            body.z.setEnabled(parts.body.z);
            body.pitch.setEnabled(parts.body.pitch);
            body.yaw.setEnabled(parts.body.yaw);
            body.roll.setEnabled(parts.body.roll);
            KeyframeAnimation.StateCollection head = builder.getPart("head");
            head.x.setEnabled(parts.head.x);
            head.y.setEnabled(parts.head.y);
            head.z.setEnabled(parts.head.z);
            head.pitch.setEnabled(parts.head.pitch);
            head.yaw.setEnabled(parts.head.yaw);
            head.roll.setEnabled(parts.head.roll);
            KeyframeAnimation.StateCollection torso = builder.getPart("torso");
            torso.x.setEnabled(parts.torso.x);
            torso.y.setEnabled(parts.torso.y);
            torso.z.setEnabled(parts.torso.z);
            torso.pitch.setEnabled(parts.torso.pitch);
            torso.yaw.setEnabled(parts.torso.yaw);
            torso.roll.setEnabled(parts.torso.roll);
            KeyframeAnimation.StateCollection rightArm = builder.getPart("rightArm");
            rightArm.x.setEnabled(parts.rightArm.x);
            rightArm.y.setEnabled(parts.rightArm.y);
            rightArm.z.setEnabled(parts.rightArm.z);
            rightArm.pitch.setEnabled(parts.rightArm.pitch);
            rightArm.yaw.setEnabled(parts.rightArm.yaw);
            rightArm.roll.setEnabled(parts.rightArm.roll);
            rightArm.bend.setEnabled(parts.rightArm.bend);
            rightArm.bendDirection.setEnabled(parts.rightArm.bendDirection);
            KeyframeAnimation.StateCollection leftArm = builder.getPart("leftArm");
            leftArm.x.setEnabled(parts.leftArm.x);
            leftArm.y.setEnabled(parts.leftArm.y);
            leftArm.z.setEnabled(parts.leftArm.z);
            leftArm.pitch.setEnabled(parts.leftArm.pitch);
            leftArm.yaw.setEnabled(parts.leftArm.yaw);
            leftArm.roll.setEnabled(parts.leftArm.roll);
            leftArm.bend.setEnabled(parts.leftArm.bend);
            leftArm.bendDirection.setEnabled(parts.leftArm.bendDirection);
            KeyframeAnimation.StateCollection rightLeg = builder.getPart("rightLeg");
            rightLeg.x.setEnabled(parts.rightLeg.x);
            rightLeg.y.setEnabled(parts.rightLeg.y);
            rightLeg.z.setEnabled(parts.rightLeg.z);
            rightLeg.pitch.setEnabled(parts.rightLeg.pitch);
            rightLeg.yaw.setEnabled(parts.rightLeg.yaw);
            rightLeg.roll.setEnabled(parts.rightLeg.roll);
            rightLeg.bend.setEnabled(parts.rightLeg.bend);
            rightLeg.bendDirection.setEnabled(parts.rightLeg.bendDirection);
            KeyframeAnimation.StateCollection leftLeg = builder.getPart("leftLeg");
            leftLeg.x.setEnabled(parts.leftLeg.x);
            leftLeg.y.setEnabled(parts.leftLeg.y);
            leftLeg.z.setEnabled(parts.leftLeg.z);
            leftLeg.pitch.setEnabled(parts.leftLeg.pitch);
            leftLeg.yaw.setEnabled(parts.leftLeg.yaw);
            leftLeg.roll.setEnabled(parts.leftLeg.roll);
            leftLeg.bend.setEnabled(parts.leftLeg.bend);
            leftLeg.bendDirection.setEnabled(parts.leftLeg.bendDirection);
            KeyframeAnimation.StateCollection rightItem = builder.getPart("rightItem");
            rightItem.x.setEnabled(parts.rightItem.x);
            rightItem.y.setEnabled(parts.rightItem.y);
            rightItem.z.setEnabled(parts.rightItem.z);
            rightItem.pitch.setEnabled(parts.rightItem.pitch);
            rightItem.yaw.setEnabled(parts.rightItem.yaw);
            rightItem.roll.setEnabled(parts.rightItem.roll);
            KeyframeAnimation.StateCollection leftItem = builder.getPart("leftItem");
            leftItem.x.setEnabled(parts.leftItem.x);
            leftItem.y.setEnabled(parts.leftItem.y);
            leftItem.z.setEnabled(parts.leftItem.z);
            leftItem.pitch.setEnabled(parts.leftItem.pitch);
            leftItem.yaw.setEnabled(parts.leftItem.yaw);
            leftItem.roll.setEnabled(parts.leftItem.roll);
            anim = builder.build();
            FirstPersonMode firstPersonMode = FirstPersonMode.DISABLED;
            if (firstPersonEnabled) {
                firstPersonMode = FirstPersonMode.THIRD_PERSON_MODEL;
            }
            if (!replaceTick) {
                KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(anim, animationContainer.animPlayer.getCurrentTick()).setFirstPersonMode(firstPersonMode);
                animationContainer.replaceAnimation(animPlayer);
            } else {
                KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(anim).setFirstPersonMode(firstPersonMode);
                if (fadeLength > 0 && easeID <= 35 && 0 <= easeID) {
                    animationContainer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeLength, getEase(easeID)), animPlayer);
                } else {
                    animationContainer.replaceAnimation(animPlayer);
                }
            }
            if (Platform.isModLoaded("liolib")) {
                ModGeckoLibUtilsClient.playGeckoAnimation(player, data, animationContainer.getSpeed());
            }
        } catch (NullPointerException var25) {
            logger.warn("Player Animator API failed to play player animation: " + var25);
        }
    }

    public static Ease getEase(int ID) {
        return 0 <= ID && ID <= 35 ? Ease.getEase((byte) ID) : Ease.LINEAR;
    }

    public static CustomModifierLayer getModifierLayer(AbstractClientPlayer player) {
        return (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(animationLayerId);
    }
}