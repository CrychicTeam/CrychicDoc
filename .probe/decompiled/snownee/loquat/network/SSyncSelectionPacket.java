package snownee.loquat.network;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import snownee.kiwi.network.KiwiPacket;
import snownee.kiwi.network.PacketHandler;
import snownee.loquat.client.ClientHooks;
import snownee.loquat.client.LoquatClient;
import snownee.loquat.core.select.PosSelection;
import snownee.loquat.core.select.SelectionManager;

@KiwiPacket(value = "sync_selection", dir = KiwiPacket.Direction.PLAY_TO_CLIENT)
public class SSyncSelectionPacket extends PacketHandler {

    public static SSyncSelectionPacket I;

    public static void sync(ServerPlayer player) {
        sync(player, "");
    }

    public static void sync(ServerPlayer player, String newZone) {
        I.send(player, buf -> {
            SelectionManager manager = SelectionManager.of(player);
            buf.writeVarInt(manager.getSelections().size());
            for (PosSelection selection : manager.getSelections()) {
                buf.writeBlockPos(selection.pos1);
                buf.writeBlockPos(selection.pos2);
            }
            buf.writeVarInt(manager.getSelectedAreas().size());
            for (UUID uuid : manager.getSelectedAreas()) {
                buf.writeUUID(uuid);
            }
            buf.writeUtf(newZone);
        });
    }

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> executor, FriendlyByteBuf buf, ServerPlayer sender) {
        Player player = ClientHooks.getPlayer();
        if (player == null) {
            return null;
        } else {
            int size = buf.readVarInt();
            List<PosSelection> selections = Lists.newArrayListWithExpectedSize(size);
            for (int i = 0; i < size; i++) {
                selections.add(new PosSelection(buf.readBlockPos(), buf.readBlockPos()));
            }
            size = buf.readVarInt();
            List<UUID> selectedAreas = Lists.newArrayListWithExpectedSize(size);
            for (int i = 0; i < size; i++) {
                selectedAreas.add(buf.readUUID());
            }
            String newZone = buf.readUtf();
            return (CompletableFuture<FriendlyByteBuf>) executor.apply((Runnable) () -> {
                SelectionManager manager = SelectionManager.of(player);
                manager.getSelections().clear();
                manager.getSelections().addAll(selections);
                manager.getSelectedAreas().clear();
                manager.getSelectedAreas().addAll(selectedAreas);
                if (!newZone.isEmpty()) {
                    LoquatClient.get().newZoneAdded(newZone);
                }
            });
        }
    }
}