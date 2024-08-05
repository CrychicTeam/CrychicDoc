package dev.xkmc.modulargolems.content.entity.humanoid.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;

public class ClientProfileManager {

    private static final Map<String, GameProfile> CACHE = new TreeMap();

    @Nullable
    public static SpecialRenderProfile get(String name) {
        GameProfile profile = getProfile(name);
        if (profile == null) {
            return null;
        } else {
            SkinManager skins = Minecraft.getInstance().getSkinManager();
            MinecraftProfileTexture skin = (MinecraftProfileTexture) skins.getInsecureSkinInformation(profile).get(Type.SKIN);
            if (skin == null) {
                return null;
            } else {
                String skinModel = skin.getMetadata("model");
                boolean slim = skinModel != null && skinModel.equals("slim");
                ResourceLocation texture = skins.getInsecureSkinLocation(profile);
                return texture.equals(new ResourceLocation("missingno")) ? null : new SpecialRenderProfile(slim, texture);
            }
        }
    }

    @Nullable
    private static GameProfile getProfile(String name) {
        if (!CACHE.containsKey(name)) {
            CACHE.put(name, null);
            SkullBlockEntity.updateGameprofile(new GameProfile(null, name), e -> CACHE.put(name, e));
        }
        return (GameProfile) CACHE.get(name);
    }
}