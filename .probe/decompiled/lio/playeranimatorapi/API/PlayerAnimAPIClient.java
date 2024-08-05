package lio.playeranimatorapi.API;

import java.util.List;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.modifier.CommonModifier;
import lio.playeranimatorapi.playeranims.PlayerAnimations;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PlayerAnimAPIClient {

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        playPlayerAnim(player, animationID, PlayerParts.allEnabled, null, -1, -1, false, false, true);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, boolean important) {
        playPlayerAnim(player, animationID, parts, modifiers, -1, -1, false, important, true);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, PlayerAnimationData data) {
        PlayerAnimations.playAnimation(player, data);
    }

    public static void playPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, boolean firstPersonEnabled, boolean important, boolean replaceTick) {
        PlayerAnimations.playAnimation(player, new PlayerAnimationData(player.m_20148_(), animationID, parts, modifiers, fadeLength, easeID, firstPersonEnabled, important), replaceTick);
    }

    public static void stopPlayerAnim(AbstractClientPlayer player, ResourceLocation animationID) {
        PlayerAnimations.stopAnimation(player, animationID);
    }
}