package net.minecraft.client.gui.spectator.categories;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.spectator.PlayerMenuItem;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;

public class TeleportToPlayerMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {

    private static final Comparator<PlayerInfo> PROFILE_ORDER = Comparator.comparing(p_253335_ -> p_253335_.getProfile().getId());

    private static final Component TELEPORT_TEXT = Component.translatable("spectatorMenu.teleport");

    private static final Component TELEPORT_PROMPT = Component.translatable("spectatorMenu.teleport.prompt");

    private final List<SpectatorMenuItem> items;

    public TeleportToPlayerMenuCategory() {
        this(Minecraft.getInstance().getConnection().getListedOnlinePlayers());
    }

    public TeleportToPlayerMenuCategory(Collection<PlayerInfo> collectionPlayerInfo0) {
        this.items = collectionPlayerInfo0.stream().filter(p_253336_ -> p_253336_.getGameMode() != GameType.SPECTATOR).sorted(PROFILE_ORDER).map(p_253334_ -> new PlayerMenuItem(p_253334_.getProfile())).toList();
    }

    @Override
    public List<SpectatorMenuItem> getItems() {
        return this.items;
    }

    @Override
    public Component getPrompt() {
        return TELEPORT_PROMPT;
    }

    @Override
    public void selectItem(SpectatorMenu spectatorMenu0) {
        spectatorMenu0.selectCategory(this);
    }

    @Override
    public Component getName() {
        return TELEPORT_TEXT;
    }

    @Override
    public void renderIcon(GuiGraphics guiGraphics0, float float1, int int2) {
        guiGraphics0.blit(SpectatorGui.SPECTATOR_LOCATION, 0, 0, 0.0F, 0.0F, 16, 16, 256, 256);
    }

    @Override
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }
}