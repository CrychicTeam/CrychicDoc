package snownee.loquat.network;

import com.google.common.collect.Lists;
import com.google.common.math.LongMath;
import java.util.Collection;
import java.util.List;
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

@KiwiPacket(value = "request_outlines", dir = KiwiPacket.Direction.PLAY_TO_SERVER)
public class CRequestOutlinesPacket extends PacketHandler {

    public static CRequestOutlinesPacket I;

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> executor, FriendlyByteBuf buf, ServerPlayer sender) {
        if (!((ServerPlayer) Objects.requireNonNull(sender)).m_20310_(2)) {
            return null;
        } else {
            long expire = LongMath.checkedAdd(buf.readVarLong(), sender.m_9236_().getGameTime());
            boolean all = buf.readBoolean();
            AreaManager manager = AreaManager.of(sender.serverLevel());
            List<Area> areas;
            if (all) {
                areas = manager.areas();
            } else {
                int size = buf.readVarInt();
                areas = Lists.newArrayList();
                for (int i = 0; i < size; i++) {
                    Area area = manager.get(buf.readUUID());
                    if (area != null) {
                        areas.add(area);
                    }
                }
            }
            return areas.isEmpty() ? null : (CompletableFuture) executor.apply((Runnable) () -> SOutlinesPacket.outlines(sender, expire, false, true, areas));
        }
    }

    public static void requestAll(long time) {
        I.sendToServer(buf -> {
            buf.writeVarLong(time);
            buf.writeBoolean(true);
        });
    }

    public static void request(long time, Collection<UUID> uuids) {
        I.sendToServer(buf -> {
            buf.writeVarLong(time);
            buf.writeBoolean(false);
            buf.writeVarInt(uuids.size());
            for (UUID uuid : uuids) {
                buf.writeUUID(uuid);
            }
        });
    }
}