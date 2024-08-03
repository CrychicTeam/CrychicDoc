package lio.playeranimatorapi.playeranims;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lio.playeranimatorapi.data.PlayerAnimationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConditionalAnimations {

    private static Map<String, Function<PlayerAnimationData, ResourceLocation>> perModConditions = new HashMap();

    public static void addModConditions(String namespace, Function<PlayerAnimationData, ResourceLocation> function) {
        perModConditions.put(namespace, function);
    }

    public static ResourceLocation getAnimationForCurrentConditions(PlayerAnimationData data) {
        if (perModConditions.containsKey(data.animationID().getNamespace())) {
            return (ResourceLocation) ((Function) perModConditions.get(data.animationID().getNamespace())).apply(data);
        } else {
            AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getInstance().level.m_46003_(data.playerUUID());
            CustomModifierLayer animationContainer = (CustomModifierLayer) PlayerAnimationAccess.getPlayerAssociatedData(player).get(PlayerAnimations.animationLayerId);
            ResourceLocation currentAnim = animationContainer.currentAnim;
            ResourceLocation baseAnim = data.animationID();
            ResourceLocation runningAnim = data.animationID().withPath(data.animationID().getPath() + "_run");
            ResourceLocation crouchedAnim = data.animationID().withPath(data.animationID().getPath() + "_crouch");
            ResourceLocation crawlingAnim = data.animationID().withPath(data.animationID().getPath() + "_crawl");
            ResourceLocation swimmingAnim = data.animationID().withPath(data.animationID().getPath() + "_swim");
            Map<ResourceLocation, KeyframeAnimation> animations = PlayerAnimationRegistry.getAnimations();
            if (player.m_6047_() && currentAnim != crawlingAnim && animations.containsKey(crouchedAnim)) {
                return crouchedAnim;
            } else if (player.m_20143_() && currentAnim != crawlingAnim && animations.containsKey(crawlingAnim)) {
                return crawlingAnim;
            } else if (player.m_6067_() && currentAnim != swimmingAnim && animations.containsKey(swimmingAnim)) {
                return swimmingAnim;
            } else {
                return player.m_20142_() && currentAnim != runningAnim && animations.containsKey(runningAnim) ? runningAnim : baseAnim;
            }
        }
    }
}