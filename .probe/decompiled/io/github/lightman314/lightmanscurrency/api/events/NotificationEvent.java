package io.github.lightman314.lightmanscurrency.api.events;

import io.github.lightman314.lightmanscurrency.api.notifications.Notification;
import io.github.lightman314.lightmanscurrency.api.notifications.NotificationData;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class NotificationEvent extends Event {

    private final UUID playerID;

    private final NotificationData data;

    protected Notification notification;

    public UUID getPlayerID() {
        return this.playerID;
    }

    public NotificationData getData() {
        return this.data;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public NotificationEvent(UUID playerID, NotificationData data, Notification notification) {
        this.playerID = playerID;
        this.data = data;
        this.notification = notification;
    }

    @Cancelable
    public static class NotificationReceivedOnClient extends NotificationEvent {

        public NotificationReceivedOnClient(UUID playerID, NotificationData data, Notification notification) {
            super(playerID, data, notification);
        }
    }

    public static class NotificationSent extends NotificationEvent {

        protected NotificationSent(UUID playerID, NotificationData data, Notification notification) {
            super(playerID, data, notification);
        }

        public static class Post extends NotificationEvent.NotificationSent {

            public Post(UUID playerID, NotificationData data, Notification notification) {
                super(playerID, data, notification);
            }
        }

        @Cancelable
        public static class Pre extends NotificationEvent.NotificationSent {

            public Pre(UUID playerID, NotificationData data, Notification notification) {
                super(playerID, data, notification);
            }

            public void setNotification(@Nonnull Notification notification) {
                if (notification == null) {
                    throw new NullPointerException("Cannot set the notification to null. Cancel the event if you wish for no notification to be sent.");
                } else {
                    this.notification = notification;
                }
            }
        }
    }
}