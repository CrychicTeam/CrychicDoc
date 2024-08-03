package net.minecraft.client.gui.screens.social;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.UserApiService;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

public class PlayerSocialManager {

    private final Minecraft minecraft;

    private final Set<UUID> hiddenPlayers = Sets.newHashSet();

    private final UserApiService service;

    private final Map<String, UUID> discoveredNamesToUUID = Maps.newHashMap();

    private boolean onlineMode;

    private CompletableFuture<?> pendingBlockListRefresh = CompletableFuture.completedFuture(null);

    public PlayerSocialManager(Minecraft minecraft0, UserApiService userApiService1) {
        this.minecraft = minecraft0;
        this.service = userApiService1;
    }

    public void hidePlayer(UUID uUID0) {
        this.hiddenPlayers.add(uUID0);
    }

    public void showPlayer(UUID uUID0) {
        this.hiddenPlayers.remove(uUID0);
    }

    public boolean shouldHideMessageFrom(UUID uUID0) {
        return this.isHidden(uUID0) || this.isBlocked(uUID0);
    }

    public boolean isHidden(UUID uUID0) {
        return this.hiddenPlayers.contains(uUID0);
    }

    public void startOnlineMode() {
        this.onlineMode = true;
        this.pendingBlockListRefresh = this.pendingBlockListRefresh.thenRunAsync(this.service::refreshBlockList, Util.ioPool());
    }

    public void stopOnlineMode() {
        this.onlineMode = false;
    }

    public boolean isBlocked(UUID uUID0) {
        if (!this.onlineMode) {
            return false;
        } else {
            this.pendingBlockListRefresh.join();
            return this.service.isBlockedPlayer(uUID0);
        }
    }

    public Set<UUID> getHiddenPlayers() {
        return this.hiddenPlayers;
    }

    public UUID getDiscoveredUUID(String string0) {
        return (UUID) this.discoveredNamesToUUID.getOrDefault(string0, Util.NIL_UUID);
    }

    public void addPlayer(PlayerInfo playerInfo0) {
        GameProfile $$1 = playerInfo0.getProfile();
        if ($$1.isComplete()) {
            this.discoveredNamesToUUID.put($$1.getName(), $$1.getId());
        }
        if (this.minecraft.screen instanceof SocialInteractionsScreen $$3) {
            $$3.onAddPlayer(playerInfo0);
        }
    }

    public void removePlayer(UUID uUID0) {
        if (this.minecraft.screen instanceof SocialInteractionsScreen $$2) {
            $$2.onRemovePlayer(uUID0);
        }
    }
}