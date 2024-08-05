package io.github.lightman314.lightmanscurrency.network.message.time;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import io.github.lightman314.lightmanscurrency.util.TimeUtil;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncTime extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncTime> HANDLER = new SPacketSyncTime.H();

    private final long time;

    public static void syncWith(@Nonnull PacketDistributor.PacketTarget player) {
        new SPacketSyncTime(0L).sendToTarget(player);
    }

    private SPacketSyncTime(long time) {
        this.time = time;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(TimeUtil.getCurrentTime());
    }

    private static class H extends CustomPacket.Handler<SPacketSyncTime> {

        @Nonnull
        public SPacketSyncTime decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncTime(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketSyncTime message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.setTimeDesync(message.time);
        }
    }
}