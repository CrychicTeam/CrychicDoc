package lio.playeranimatorapi.liolib;

import java.util.HashMap;
import java.util.Map;
import lio.playeranimatorapi.playeranims.PlayerAnimations;
import net.liopyu.liolib.model.GeoModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class PlayerAnimationModel extends GeoModel<AbstractClientPlayer> {

    public static Map<String, ResourceLocation> resourceLocations = new HashMap();

    public ResourceLocation getModelResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":geo/player_animation/" + geckoResource.getPath() + ".geo.json");
    }

    public ResourceLocation getTextureResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":textures/player_animation/" + geckoResource.getPath() + ".png");
    }

    public ResourceLocation getAnimationResource(AbstractClientPlayer player) {
        ResourceLocation geckoResource = getCurrentGeckoResource(player);
        return getResourceLocation(geckoResource.getNamespace() + ":animations/player_animation/" + geckoResource.getPath() + ".animation.json");
    }

    public static ResourceLocation getCurrentGeckoResource(AbstractClientPlayer player) {
        ResourceLocation currentAnim = PlayerAnimations.getModifierLayer(player).currentAnim;
        return PlayerAnimations.geckoMap.containsKey(currentAnim) ? (ResourceLocation) PlayerAnimations.geckoMap.get(currentAnim) : null;
    }

    public static ResourceLocation getResourceLocation(String resource) {
        if (!resourceLocations.containsKey(resource)) {
            resourceLocations.put(resource, new ResourceLocation(resource));
        }
        return (ResourceLocation) resourceLocations.get(resource);
    }

    public boolean allResourcesExist(AbstractClientPlayer player) {
        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        return getCurrentGeckoResource(player) != null && manager.m_213713_(this.getModelResource(player)).isPresent() && manager.m_213713_(this.getTextureResource(player)).isPresent() && manager.m_213713_(this.getAnimationResource(player)).isPresent();
    }
}