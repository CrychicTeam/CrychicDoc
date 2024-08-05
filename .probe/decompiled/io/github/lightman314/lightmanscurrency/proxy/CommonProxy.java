package io.github.lightman314.lightmanscurrency.proxy;

import com.mojang.authlib.GameProfile;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import io.github.lightman314.lightmanscurrency.common.playertrading.ClientPlayerTrade;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

public class CommonProxy {

    public boolean isClient() {
        return false;
    }

    public void setupClient() {
    }

    public void clearClientTraders() {
    }

    public void updateTrader(CompoundTag compound) {
    }

    public void removeTrader(long traderID) {
    }

    public void clearTeams() {
    }

    public void updateTeam(CompoundTag compound) {
    }

    public void removeTeam(long teamID) {
    }

    public void clearBankAccounts() {
    }

    public void updateBankAccount(UUID player, CompoundTag compound) {
    }

    public void receiveEmergencyEjectionData(CompoundTag compound) {
    }

    public void updateNotifications(NotificationData data) {
    }

    public void receiveNotification(Notification notification) {
    }

    public void receiveSelectedBankAccount(BankReference selectedAccount) {
    }

    public void updateTaxEntries(CompoundTag compound) {
    }

    public void removeTaxEntry(long id) {
    }

    public void openNotificationScreen() {
    }

    public void openTeamManager() {
    }

    public void playCoinSound() {
    }

    public void createTeamResponse(long teamID) {
    }

    public long getTimeDesync() {
        return 0L;
    }

    public void setTimeDesync(long currentTime) {
    }

    public void loadAdminPlayers(List<UUID> serverAdminList) {
    }

    @Nonnull
    public Level safeGetDummyLevel() throws Exception {
        Level level = this.getDummyLevelFromServer();
        if (level != null) {
            return level;
        } else {
            throw new Exception("Could not get dummy level from server, as there is no active server!");
        }
    }

    @Nullable
    protected final Level getDummyLevelFromServer() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.overworld() : null;
    }

    public void loadPlayerTrade(ClientPlayerTrade trade) {
    }

    public void syncEventUnlocks(@Nonnull List<String> unlocks) {
    }

    public void sendClientMessage(@Nonnull Component message) {
    }

    public List<GameProfile> getPlayerList(boolean logicalClient) {
        if (logicalClient) {
            return new ArrayList();
        } else {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            return (List<GameProfile>) (server != null ? server.getPlayerList().getPlayers().stream().map(Player::m_36316_).toList() : new ArrayList());
        }
    }
}