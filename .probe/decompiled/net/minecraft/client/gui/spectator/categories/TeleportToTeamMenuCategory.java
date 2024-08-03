package net.minecraft.client.gui.spectator.categories;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuCategory;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class TeleportToTeamMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {

    private static final Component TELEPORT_TEXT = Component.translatable("spectatorMenu.team_teleport");

    private static final Component TELEPORT_PROMPT = Component.translatable("spectatorMenu.team_teleport.prompt");

    private final List<SpectatorMenuItem> items;

    public TeleportToTeamMenuCategory() {
        Minecraft $$0 = Minecraft.getInstance();
        this.items = createTeamEntries($$0, $$0.level.getScoreboard());
    }

    private static List<SpectatorMenuItem> createTeamEntries(Minecraft minecraft0, Scoreboard scoreboard1) {
        return scoreboard1.getPlayerTeams().stream().flatMap(p_260025_ -> TeleportToTeamMenuCategory.TeamSelectionItem.create(minecraft0, p_260025_).stream()).toList();
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
        guiGraphics0.blit(SpectatorGui.SPECTATOR_LOCATION, 0, 0, 16.0F, 0.0F, 16, 16, 256, 256);
    }

    @Override
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }

    static class TeamSelectionItem implements SpectatorMenuItem {

        private final PlayerTeam team;

        private final ResourceLocation iconSkin;

        private final List<PlayerInfo> players;

        private TeamSelectionItem(PlayerTeam playerTeam0, List<PlayerInfo> listPlayerInfo1, ResourceLocation resourceLocation2) {
            this.team = playerTeam0;
            this.players = listPlayerInfo1;
            this.iconSkin = resourceLocation2;
        }

        public static Optional<SpectatorMenuItem> create(Minecraft minecraft0, PlayerTeam playerTeam1) {
            List<PlayerInfo> $$2 = new ArrayList();
            for (String $$3 : playerTeam1.getPlayers()) {
                PlayerInfo $$4 = minecraft0.getConnection().getPlayerInfo($$3);
                if ($$4 != null && $$4.getGameMode() != GameType.SPECTATOR) {
                    $$2.add($$4);
                }
            }
            if ($$2.isEmpty()) {
                return Optional.empty();
            } else {
                GameProfile $$5 = ((PlayerInfo) $$2.get(RandomSource.create().nextInt($$2.size()))).getProfile();
                ResourceLocation $$6 = minecraft0.getSkinManager().getInsecureSkinLocation($$5);
                return Optional.of(new TeleportToTeamMenuCategory.TeamSelectionItem(playerTeam1, $$2, $$6));
            }
        }

        @Override
        public void selectItem(SpectatorMenu spectatorMenu0) {
            spectatorMenu0.selectCategory(new TeleportToPlayerMenuCategory(this.players));
        }

        @Override
        public Component getName() {
            return this.team.getDisplayName();
        }

        @Override
        public void renderIcon(GuiGraphics guiGraphics0, float float1, int int2) {
            Integer $$3 = this.team.getColor().getColor();
            if ($$3 != null) {
                float $$4 = (float) ($$3 >> 16 & 0xFF) / 255.0F;
                float $$5 = (float) ($$3 >> 8 & 0xFF) / 255.0F;
                float $$6 = (float) ($$3 & 0xFF) / 255.0F;
                guiGraphics0.fill(1, 1, 15, 15, Mth.color($$4 * float1, $$5 * float1, $$6 * float1) | int2 << 24);
            }
            guiGraphics0.setColor(float1, float1, float1, (float) int2 / 255.0F);
            PlayerFaceRenderer.draw(guiGraphics0, this.iconSkin, 2, 2, 12);
            guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}