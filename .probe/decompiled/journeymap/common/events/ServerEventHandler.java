package journeymap.common.events;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.nbt.PlayerData;
import journeymap.common.properties.GlobalProperties;
import journeymap.common.properties.PropertiesManager;
import journeymap.common.properties.ServerOption;
import journeymap.common.util.PermissionsManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerEventHandler {

    private static int playerUpdateTicksCount = 0;

    public void onServerTickEvent(Level world) {
        if (PropertiesManager.getInstance().getGlobalProperties().worldPlayerRadar.get().enabled()) {
            int playerUpdateTicks = PropertiesManager.getInstance().getGlobalProperties().worldPlayerRadarUpdateTime.get();
            playerUpdateTicksCount++;
            if (world != null && playerUpdateTicksCount >= playerUpdateTicks) {
                this.sendPlayersOnRadarToPlayers();
                playerUpdateTicksCount = 0;
            }
        }
    }

    public void onEntityJoinWorldEvent(Entity entity) {
        if (entity instanceof ServerPlayer) {
            this.sendConfigsToPlayer((ServerPlayer) entity);
        }
    }

    public void onPlayerLoggedInEvent(Player player) {
        if (player instanceof ServerPlayer) {
            Journeymap.getInstance().getDispatcher().sendHandshakePacket((ServerPlayer) player, Journeymap.JM_VERSION.toJson());
            this.sendConfigsToPlayer((ServerPlayer) player);
            PlayerData.getPlayerData().getPlayer((ServerPlayer) player);
        }
    }

    public void sendConfigsToPlayer(ServerPlayer player) {
        PermissionsManager.getInstance().sendPermissions(player);
    }

    private void sendPlayersOnRadarToPlayers() {
        GlobalProperties prop = PropertiesManager.getInstance().getGlobalProperties();
        ServerOption option = prop.worldPlayerRadar.get();
        for (ServerPlayer player : LoaderHooks.getServer().getPlayerList().getPlayers()) {
            boolean playerRadarEnabled = PropertiesManager.getInstance().getDimProperties(DimensionHelper.getDimension(player)).playerRadarEnabled.get();
            boolean receiverOp = Journeymap.isOp(player);
            if (option.enabled() && playerRadarEnabled || option.canOps() && receiverOp) {
                try {
                    this.sendPlayerTrackingData(player, receiverOp);
                } catch (ConcurrentModificationException var8) {
                }
            }
        }
    }

    private void sendPlayerTrackingData(ServerPlayer entityPlayerMP, boolean receiverOp) {
        List<ServerPlayer> serverPlayers = entityPlayerMP.server.getPlayerList().getPlayers();
        if (serverPlayers != null && serverPlayers.size() > 1) {
            GlobalProperties properties = PropertiesManager.getInstance().getGlobalProperties();
            for (ServerPlayer radarPlayer : serverPlayers) {
                boolean sameDimension = entityPlayerMP.m_9236_().dimension().equals(radarPlayer.m_9236_().dimension());
                boolean sneaking = radarPlayer.m_6144_();
                boolean invisible = radarPlayer.m_20177_(entityPlayerMP);
                boolean hideOp = properties.hideOps.get();
                boolean hideSpectators = properties.hideSpectators.get() && radarPlayer.isSpectator();
                boolean seeUnderground = this.seeUnderground(radarPlayer, receiverOp);
                boolean visible = sameDimension && (receiverOp && seeUnderground || !hideOp && !sneaking && !hideSpectators && !invisible && seeUnderground);
                visible = visible && this.isSelfHidden(radarPlayer, properties, receiverOp);
                if (visible) {
                    visible = entityPlayerMP.serverLevel().players().contains(radarPlayer);
                }
                UUID playerId = radarPlayer.m_20148_();
                if (!entityPlayerMP.m_20148_().equals(playerId) && entityPlayerMP.connection.isAcceptingMessages()) {
                    Journeymap.getInstance().getDispatcher().sendPlayerLocationPacket(entityPlayerMP, radarPlayer, visible);
                }
            }
        }
    }

    private boolean isSelfHidden(ServerPlayer radarPlayer, GlobalProperties properties, boolean receiverOp) {
        if (!properties.allowMultiplayerSettings.get().hasOption(receiverOp) && !ServerOption.OPS.equals(properties.allowMultiplayerSettings.get())) {
            return true;
        } else {
            PlayerData.Player player = PlayerData.getPlayerData().getPlayer(radarPlayer);
            boolean hiddenUnderground = player.isHiddenUnderground() && this.isUnderground(radarPlayer) && !Level.NETHER.equals(radarPlayer.m_9236_().dimension());
            return receiverOp || player.isVisible() && !hiddenUnderground;
        }
    }

    private boolean seeUnderground(ServerPlayer player, boolean isOp) {
        ServerOption seeUnderground = PropertiesManager.getInstance().getGlobalProperties().seeUndergroundPlayers.get();
        return !Level.NETHER.equals(player.m_9236_().dimension()) && !ServerOption.ALL.equals(seeUnderground) && this.isUnderground(player) ? seeUnderground.hasOption(isOp) : true;
    }

    private boolean isUnderground(ServerPlayer player) {
        return !player.m_9236_().m_45527_(BlockPos.containing(player.m_146892_()));
    }

    public void unloadPlayer(Entity entity, ServerLevel world) {
        if (entity instanceof ServerPlayer player) {
            for (ServerPlayer onlinePlayer : world.getServer().getPlayerList().getPlayers()) {
                if (!onlinePlayer.m_20148_().equals(player.m_20148_())) {
                    Journeymap.getInstance().getDispatcher().sendPlayerLocationPacket(player, onlinePlayer, false);
                }
            }
        }
    }
}