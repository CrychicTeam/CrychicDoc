package io.github.lightman314.lightmanscurrency.network.message.data;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncNotifications extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncNotifications> HANDLER = new SPacketSyncNotifications.H();

    public NotificationData data;

    public SPacketSyncNotifications(NotificationData data) {
        this.data = data;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.data.save());
    }

    private static class H extends CustomPacket.Handler<SPacketSyncNotifications> {

        @Nonnull
        public SPacketSyncNotifications decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncNotifications(NotificationData.loadFrom(buffer.readAnySizeNbt()));
        }

        protected void handle(@Nonnull SPacketSyncNotifications message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.updateNotifications(message.data);
        }
    }
}