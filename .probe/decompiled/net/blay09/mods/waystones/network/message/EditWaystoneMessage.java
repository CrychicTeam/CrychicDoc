package net.blay09.mods.waystones.network.message;

import java.util.UUID;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class EditWaystoneMessage {

    private final UUID waystoneUid;

    private final String name;

    private final boolean isGlobal;

    public EditWaystoneMessage(UUID waystoneUid, String name, boolean isGlobal) {
        this.waystoneUid = waystoneUid;
        this.name = name;
        this.isGlobal = isGlobal;
    }

    public static void encode(EditWaystoneMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.waystoneUid);
        buf.writeUtf(message.name);
        buf.writeBoolean(message.isGlobal);
    }

    public static EditWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.readUUID();
        String name = buf.readUtf(255);
        boolean isGlobal = buf.readBoolean();
        return new EditWaystoneMessage(waystoneUid, name, isGlobal);
    }

    public static void handle(ServerPlayer player, EditWaystoneMessage message) {
        WaystoneProxy waystone = new WaystoneProxy(player.server, message.waystoneUid);
        WaystoneEditPermissions permissions = PlayerWaystoneManager.mayEditWaystone(player, player.m_9236_(), waystone);
        if (permissions == WaystoneEditPermissions.ALLOW) {
            BlockPos pos = waystone.getPos();
            if (!(player.m_20275_((double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F)) > 64.0)) {
                Waystone backingWaystone = (Waystone) waystone.getBackingWaystone();
                String legalName = makeNameLegal(player.server, message.name);
                backingWaystone.setName(legalName);
                if (PlayerWaystoneManager.mayEditGlobalWaystones(player)) {
                    if (!backingWaystone.isGlobal() && message.isGlobal) {
                        PlayerWaystoneManager.activeWaystoneForEveryone(player.server, backingWaystone);
                    }
                    backingWaystone.setGlobal(message.isGlobal);
                }
                WaystoneManager.get(player.server).m_77762_();
                WaystoneSyncManager.sendWaystoneUpdateToAll(player.server, backingWaystone);
                player.closeContainer();
            }
        }
    }

    private static String makeNameLegal(MinecraftServer server, String name) {
        String inventoryButtonMode = WaystonesConfig.getActive().inventoryButton.inventoryButton;
        return inventoryButtonMode.equals(name) && WaystoneManager.get(server).findWaystoneByName(name).isPresent() ? name + "*" : name;
    }
}