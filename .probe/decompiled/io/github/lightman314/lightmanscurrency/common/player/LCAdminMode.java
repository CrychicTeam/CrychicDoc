package io.github.lightman314.lightmanscurrency.common.player;

import io.github.lightman314.lightmanscurrency.network.message.command.SPacketSyncAdminList;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class LCAdminMode {

    private static final List<UUID> adminPlayers = new ArrayList();

    public static boolean isAdminPlayer(@Nullable Player player) {
        return player != null && adminPlayers.contains(player.m_20148_()) && player.m_20310_(2);
    }

    public static void ToggleAdminPlayer(ServerPlayer player) {
        UUID playerID = player.m_20148_();
        if (adminPlayers.contains(playerID)) {
            adminPlayers.remove(playerID);
        } else {
            adminPlayers.add(playerID);
        }
        sendSyncPacket(PacketDistributor.ALL.noArg());
    }

    public static void sendSyncPacket(PacketDistributor.PacketTarget target) {
        new SPacketSyncAdminList(adminPlayers).sendToTarget(target);
    }

    public static void loadAdminPlayers(List<UUID> serverAdminList) {
        adminPlayers.clear();
        adminPlayers.addAll(serverAdminList);
    }
}