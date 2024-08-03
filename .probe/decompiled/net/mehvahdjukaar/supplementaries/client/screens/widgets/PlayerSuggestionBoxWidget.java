package net.mehvahdjukaar.supplementaries.client.screens.widgets;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.StatueBlockTileRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PlayerSuggestionBoxWidget extends MultiLineEditBoxWidget {

    private static final Map<UUID, String> USERNAME_CACHE = new HashMap();

    private static final Component EMPTY_SEARCH = Component.translatable("gui.supplementaries.present.send").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);

    private final List<PlayerSuggestionBoxWidget.SimplePlayerEntry> allPlayers = new ArrayList();

    private final List<PlayerSuggestionBoxWidget.SimplePlayerEntry> filtered = new ArrayList();

    private PlayerSuggestionBoxWidget.SimplePlayerEntry selectedPlayer = null;

    @Nullable
    private String suggestion;

    private String fullSuggestion = "";

    public static void setUsernameCache(Map<UUID, String> usernameCache) {
        USERNAME_CACHE.clear();
        USERNAME_CACHE.putAll(usernameCache);
    }

    public PlayerSuggestionBoxWidget(Minecraft mc, int x, int y, int width, int height) {
        super(mc, x, y, width, height);
        Collection<UUID> onlinePlayers = mc.player.connection.getOnlinePlayerIds();
        for (UUID uuid : onlinePlayers) {
            PlayerInfo playerinfo = mc.player.connection.getPlayerInfo(uuid);
            if (playerinfo != null) {
                this.allPlayers.add(new PlayerSuggestionBoxWidget.SimplePlayerEntry(playerinfo));
            }
        }
        for (Entry<UUID, String> entry : USERNAME_CACHE.entrySet()) {
            if (!onlinePlayers.contains(entry.getKey())) {
                this.allPlayers.add(new PlayerSuggestionBoxWidget.SimplePlayerEntry((UUID) entry.getKey(), (String) entry.getValue()));
            }
        }
        this.filtered.addAll(this.allPlayers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.f_93624_) {
            if (this.canConsumeInput() && this.suggestion != null) {
                int x = this.m_252754_();
                MultiLineEditBoxWidget.DisplayCache cache = this.getDisplayCache();
                if (cache.lines.length > 0) {
                    x += this.font.width(cache.lines[0].contents);
                }
                graphics.drawString(this.font, this.suggestion, x, this.m_252907_(), -8355712, false);
            }
            if (this.getText().isEmpty()) {
                graphics.drawString(this.font, EMPTY_SEARCH, this.m_252754_(), this.m_252907_(), 0, false);
            } else if (this.selectedPlayer != null) {
                this.selectedPlayer.render(graphics, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, partialTicks);
            }
        }
    }

    @Override
    public boolean keyPressed(int key, int alt, int ctrl) {
        if (key == 32) {
            return true;
        } else if (key == 258 && this.canConsumeInput()) {
            if (!this.fullSuggestion.isEmpty()) {
                this.setText(this.fullSuggestion);
                this.moveCursorToEnd();
                this.clearDisplayCache();
            }
            return true;
        } else {
            return super.keyPressed(key, alt, ctrl);
        }
    }

    @Override
    public void onValueChanged() {
        this.updateFilteredEntries();
        String newValue = this.getText();
        this.selectedPlayer = null;
        String suggestion = "";
        this.fullSuggestion = "";
        if (!newValue.isEmpty()) {
            for (PlayerSuggestionBoxWidget.SimplePlayerEntry entry : this.filtered) {
                String name = entry.getName();
                if (this.fullSuggestion.isEmpty()) {
                    this.fullSuggestion = name;
                    suggestion = name.substring(newValue.length());
                }
                if (name.equalsIgnoreCase(newValue)) {
                    this.selectedPlayer = entry;
                    break;
                }
            }
        }
        this.setSuggestion(suggestion);
    }

    private void setSuggestion(@Nullable String suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public void setState(boolean hasItem, boolean packed) {
        super.setState(hasItem, packed);
        this.filtered.clear();
        if (!packed && hasItem) {
            this.m_93692_(true);
            this.filtered.addAll(this.allPlayers);
        }
    }

    private void updateFilteredEntries() {
        String filter = this.getText();
        if (filter == null) {
            filter = "";
        } else {
            filter = filter.toLowerCase(Locale.ROOT);
        }
        this.filtered.clear();
        String finalFilter = filter;
        this.filtered.addAll(this.allPlayers.stream().filter(s -> s.getName().toLowerCase(Locale.ROOT).startsWith(finalFilter)).toList());
    }

    public void addPlayer(PlayerInfo info) {
        this.allPlayers.removeIf(simplePlayerEntry -> simplePlayerEntry.getId().equals(info.getProfile().getId()));
        this.allPlayers.add(new PlayerSuggestionBoxWidget.SimplePlayerEntry(info));
        this.updateFilteredEntries();
        this.onValueChanged();
    }

    public void removePlayer(UUID id) {
        for (PlayerSuggestionBoxWidget.SimplePlayerEntry simplePlayerEntry : this.allPlayers) {
            if (simplePlayerEntry.getId().equals(id)) {
                simplePlayerEntry.setOnline(false);
                return;
            }
        }
    }

    public static class SimplePlayerEntry {

        private static final int SKIN_SIZE = 8;

        private final Supplier<ResourceLocation> skinGetter;

        private GameProfile profile;

        private boolean isOnline;

        public SimplePlayerEntry(PlayerInfo playerInfo) {
            this.profile = playerInfo.getProfile();
            this.skinGetter = playerInfo::m_105337_;
            this.isOnline = true;
        }

        public SimplePlayerEntry(UUID id, String lastName) {
            GameProfile profile = new GameProfile(id, lastName);
            this.skinGetter = () -> StatueBlockTileRenderer.getPlayerSkin(this.profile);
            if (!profile.isComplete() || !profile.getProperties().containsKey("textures")) {
                synchronized (this) {
                    this.profile = profile;
                }
                SkullBlockEntity.updateGameprofile(this.profile, gameProfile -> this.profile = gameProfile);
            }
            this.isOnline = false;
        }

        public void setOnline(boolean online) {
            this.isOnline = online;
        }

        public UUID getId() {
            return this.profile.getId();
        }

        public String getName() {
            return this.profile.getName();
        }

        public void render(GuiGraphics graphics, int x, int y, int width, int height, float pPartialTicks) {
            int i = x + width - 4;
            float c = this.isOnline ? 1.0F : 0.5F;
            RenderSystem.setShaderColor(1.0F, c, c, 1.0F);
            ResourceLocation resourceLocation = (ResourceLocation) this.skinGetter.get();
            graphics.blit(resourceLocation, i, y, 8, 8, 8.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.enableBlend();
            graphics.blit(resourceLocation, i, y, 8, 8, 40.0F, 8.0F, 8, 8, 64, 64);
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}