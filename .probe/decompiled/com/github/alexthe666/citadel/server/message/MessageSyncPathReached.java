package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.client.render.pathfinding.PathfindingDebugRenderer;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class MessageSyncPathReached {

    public Set<BlockPos> reached = new HashSet();

    public MessageSyncPathReached(Set<BlockPos> reached) {
        this.reached = reached;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.reached.size());
        for (BlockPos node : this.reached) {
            buf.writeBlockPos(node);
        }
    }

    public static MessageSyncPathReached read(FriendlyByteBuf buf) {
        int size = buf.readInt();
        Set<BlockPos> reached = new HashSet();
        for (int i = 0; i < size; i++) {
            reached.add(buf.readBlockPos());
        }
        return new MessageSyncPathReached(reached);
    }

    public static class Handler {

        public static boolean handle(MessageSyncPathReached message, Supplier<NetworkEvent.Context> contextSupplier) {
            ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
                ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
                if (((NetworkEvent.Context) contextSupplier.get()).getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                    for (MNode node : PathfindingDebugRenderer.lastDebugNodesPath) {
                        if (message.reached.contains(node.pos)) {
                            node.setReachedByWorker(true);
                        }
                    }
                }
            });
            return true;
        }
    }
}