package io.github.lightman314.lightmanscurrency.api.notifications;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.events.NotificationEvent;
import io.github.lightman314.lightmanscurrency.network.message.data.SPacketSyncNotifications;
import io.github.lightman314.lightmanscurrency.network.message.notifications.SPacketChatNotification;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "lightmanscurrency")
public class NotificationSaveData extends SavedData {

    private final Map<UUID, NotificationData> playerNotifications = new HashMap();

    private NotificationSaveData() {
    }

    private NotificationSaveData(CompoundTag compound) {
        ListTag notificationData = compound.getList("PlayerNotifications", 10);
        for (int i = 0; i < notificationData.size(); i++) {
            CompoundTag tag = notificationData.getCompound(i);
            UUID id = tag.getUUID("Player");
            NotificationData data = NotificationData.loadFrom(tag);
            if (id != null && data != null) {
                this.playerNotifications.put(id, data);
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag notificationData = new ListTag();
        this.playerNotifications.forEach((id, data) -> {
            CompoundTag tag = data.save();
            tag.putUUID("Player", id);
            notificationData.add(tag);
        });
        compound.put("PlayerNotifications", notificationData);
        return compound;
    }

    private static NotificationSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(NotificationSaveData::new, NotificationSaveData::new, "lightmanscurrency_notification_data");
            }
        }
        return null;
    }

    public static NotificationData GetNotifications(Player player) {
        return player == null ? new NotificationData() : GetNotifications(player.m_20148_());
    }

    public static NotificationData GetNotifications(UUID playerID) {
        if (playerID == null) {
            return new NotificationData();
        } else {
            NotificationSaveData nsd = get();
            if (nsd != null) {
                if (!nsd.playerNotifications.containsKey(playerID)) {
                    nsd.playerNotifications.put(playerID, new NotificationData());
                    nsd.m_77762_();
                }
                return (NotificationData) nsd.playerNotifications.get(playerID);
            } else {
                return new NotificationData();
            }
        }
    }

    public static void MarkNotificationsDirty(UUID playerID) {
        NotificationSaveData nsd = get();
        if (nsd != null) {
            nsd.m_77762_();
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                ServerPlayer player = server.getPlayerList().getPlayer(playerID);
                if (player != null) {
                    new SPacketSyncNotifications(GetNotifications(playerID)).sendTo(player);
                }
            }
        }
    }

    public static void PushNotification(UUID playerID, Notification notification) {
        PushNotification(playerID, notification, true);
    }

    public static void PushNotification(UUID playerID, Notification notification, boolean pushToChat) {
        if (notification == null) {
            LightmansCurrency.LogError("Cannot push a null notification!");
        } else {
            NotificationData data = GetNotifications(playerID);
            if (data != null) {
                NotificationEvent.NotificationSent.Pre event = new NotificationEvent.NotificationSent.Pre(playerID, data, notification);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return;
                }
                data.addNotification(event.getNotification());
                MarkNotificationsDirty(playerID);
                MinecraftForge.EVENT_BUS.post(new NotificationEvent.NotificationSent.Post(playerID, data, event.getNotification()));
                if (pushToChat) {
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    if (server != null) {
                        ServerPlayer player = server.getPlayerList().getPlayer(playerID);
                        if (player != null) {
                            new SPacketChatNotification(notification).sendTo(player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void OnPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        NotificationData notifications = GetNotifications(event.getEntity());
        new SPacketSyncNotifications(notifications).sendTo(event.getEntity());
    }
}