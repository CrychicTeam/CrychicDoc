package dev.ftb.mods.ftblibrary.icon;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.DefaultPlayerSkin;

public class FaceIcon extends Icon {

    private static final HashMap<UUID, FaceIcon> CACHE = new HashMap();

    public final GameProfile profile;

    public Icon skin;

    public Icon head;

    public Icon hat;

    public static FaceIcon getFace(GameProfile profile) {
        FaceIcon icon = (FaceIcon) CACHE.get(profile.getId());
        if (icon == null) {
            icon = new FaceIcon(profile);
            CACHE.put(profile.getId(), icon);
        }
        return icon;
    }

    private FaceIcon(GameProfile p) {
        this.profile = p;
        this.skin = new ImageIcon(DefaultPlayerSkin.getDefaultSkin(this.profile.getId()));
        this.head = this.skin.withUV(8.0F, 8.0F, 8.0F, 8.0F, 64.0F, 64.0F);
        this.hat = Icon.empty();
        Minecraft.getInstance().getSkinManager().registerSkins(this.profile, (type, resourceLocation, minecraftProfileTexture) -> {
            if (type == Type.SKIN) {
                this.skin = new ImageIcon(resourceLocation);
                this.head = this.skin.withUV(8.0F, 8.0F, 8.0F, 8.0F, 64.0F, 64.0F);
                this.hat = this.skin.withUV(40.0F, 8.0F, 8.0F, 8.0F, 64.0F, 64.0F);
            }
        }, true);
    }

    @Override
    public void draw(GuiGraphics poseStack, int x, int y, int w, int h) {
        this.head.draw(poseStack, x, y, w, h);
        this.hat.draw(poseStack, x, y, w, h);
    }
}