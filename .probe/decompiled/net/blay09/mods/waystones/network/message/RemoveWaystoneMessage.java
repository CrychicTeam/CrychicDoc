package net.blay09.mods.waystones.network.message;

import java.util.Objects;
import java.util.UUID;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class RemoveWaystoneMessage {

    private final UUID waystoneUid;

    public RemoveWaystoneMessage(UUID waystoneUid) {
        this.waystoneUid = waystoneUid;
    }

    public static void encode(RemoveWaystoneMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.waystoneUid);
    }

    public static RemoveWaystoneMessage decode(FriendlyByteBuf buf) {
        UUID waystoneUid = buf.readUUID();
        return new RemoveWaystoneMessage(waystoneUid);
    }

    public static void handle(ServerPlayer player, RemoveWaystoneMessage message) {
        WaystoneProxy waystone = new WaystoneProxy(player.server, message.waystoneUid);
        PlayerWaystoneManager.deactivateWaystone(player, waystone);
        if (waystone.isGlobal() && player.m_150110_().instabuild) {
            IWaystone backingWaystone = waystone.getBackingWaystone();
            if (backingWaystone instanceof Waystone) {
                ((Waystone) backingWaystone).setGlobal(false);
                ServerLevel targetWorld = ((MinecraftServer) Objects.requireNonNull(player.m_9236_().getServer())).getLevel(backingWaystone.getDimension());
                BlockPos pos = backingWaystone.getPos();
                BlockState state = targetWorld != null ? targetWorld.m_8055_(pos) : null;
                if (targetWorld == null || !(state.m_60734_() instanceof WaystoneBlock)) {
                    WaystoneManager.get(player.server).removeWaystone(backingWaystone);
                    PlayerWaystoneManager.removeKnownWaystone(player.server, backingWaystone);
                }
            }
        }
    }
}