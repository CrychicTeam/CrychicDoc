package io.github.lightman314.lightmanscurrency.network.message.notifications;

import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationAPI;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationCategory;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketFlagNotificationsSeen extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketFlagNotificationsSeen> HANDLER = new CPacketFlagNotificationsSeen.H();

    NotificationCategory category;

    public CPacketFlagNotificationsSeen(NotificationCategory category) {
        this.category = category;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.category.save());
    }

    private static class H extends CustomPacket.Handler<CPacketFlagNotificationsSeen> {

        @Nonnull
        public CPacketFlagNotificationsSeen decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketFlagNotificationsSeen(NotificationAPI.loadCategory(buffer.readAnySizeNbt()));
        }

        protected void handle(@Nonnull CPacketFlagNotificationsSeen message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                NotificationData data = NotificationSaveData.GetNotifications(sender);
                if (data != null && data.unseenNotification(message.category)) {
                    for (Notification n : data.getNotifications(message.category)) {
                        if (!n.wasSeen()) {
                            n.setSeen();
                        }
                    }
                    NotificationSaveData.MarkNotificationsDirty(sender.m_20148_());
                }
            }
        }
    }
}