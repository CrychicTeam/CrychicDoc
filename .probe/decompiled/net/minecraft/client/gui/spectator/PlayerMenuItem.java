package net.minecraft.client.gui.spectator;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import net.minecraft.resources.ResourceLocation;

public class PlayerMenuItem implements SpectatorMenuItem {

    private final GameProfile profile;

    private final ResourceLocation location;

    private final Component name;

    public PlayerMenuItem(GameProfile gameProfile0) {
        this.profile = gameProfile0;
        Minecraft $$1 = Minecraft.getInstance();
        this.location = $$1.getSkinManager().getInsecureSkinLocation(gameProfile0);
        this.name = Component.literal(gameProfile0.getName());
    }

    @Override
    public void selectItem(SpectatorMenu spectatorMenu0) {
        Minecraft.getInstance().getConnection().send(new ServerboundTeleportToEntityPacket(this.profile.getId()));
    }

    @Override
    public Component getName() {
        return this.name;
    }

    @Override
    public void renderIcon(GuiGraphics guiGraphics0, float float1, int int2) {
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, (float) int2 / 255.0F);
        PlayerFaceRenderer.draw(guiGraphics0, this.location, 2, 2, 12);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}