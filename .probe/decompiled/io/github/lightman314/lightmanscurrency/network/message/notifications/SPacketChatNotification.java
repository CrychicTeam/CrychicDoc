package io.github.lightman314.lightmanscurrency.network.message.notifications;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationAPI;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketChatNotification extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketChatNotification> HANDLER = new SPacketChatNotification.H();

    public Notification notification;

    public SPacketChatNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.notification.save());
    }

    private static class H extends CustomPacket.Handler<SPacketChatNotification> {

        @Nonnull
        public SPacketChatNotification decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketChatNotification(NotificationAPI.loadNotification(buffer.readAnySizeNbt()));
        }

        protected void handle(@Nonnull SPacketChatNotification message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.receiveNotification(message.notification);
        }
    }
}