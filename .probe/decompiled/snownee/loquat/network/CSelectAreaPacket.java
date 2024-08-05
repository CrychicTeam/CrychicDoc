package snownee.loquat.network;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import snownee.kiwi.network.KiwiPacket;
import snownee.kiwi.network.PacketHandler;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.SelectionManager;

@KiwiPacket(value = "select_area", dir = KiwiPacket.Direction.PLAY_TO_SERVER)
public class CSelectAreaPacket extends PacketHandler {

    public static CSelectAreaPacket I;

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> executor, FriendlyByteBuf buf, ServerPlayer sender) {
        if (!((ServerPlayer) Objects.requireNonNull(sender)).m_20310_(2)) {
            return null;
        } else {
            AreaManager manager = AreaManager.of(sender.serverLevel());
            boolean select = buf.readBoolean();
            Area area = manager.get(buf.readUUID());
            if (area == null) {
                return null;
            } else {
                SelectionManager selections = SelectionManager.of(sender);
                if (select) {
                    selections.getSelectedAreas().add(area.getUuid());
                } else {
                    selections.getSelectedAreas().remove(area.getUuid());
                }
                return (CompletableFuture<FriendlyByteBuf>) executor.apply((Runnable) () -> SSyncSelectionPacket.sync(sender));
            }
        }
    }

    public static void send(boolean select, UUID uuid) {
        I.sendToServer(buf -> {
            buf.writeBoolean(select);
            buf.writeUUID(uuid);
        });
    }
}