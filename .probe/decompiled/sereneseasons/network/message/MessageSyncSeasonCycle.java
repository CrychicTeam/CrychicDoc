package sereneseasons.network.message;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import sereneseasons.handler.season.SeasonHandler;

public class MessageSyncSeasonCycle {

    public ResourceKey<Level> dimension;

    public int seasonCycleTicks;

    public MessageSyncSeasonCycle() {
    }

    public MessageSyncSeasonCycle(ResourceKey<Level> dimension, int seasonCycleTicks) {
        this.dimension = dimension;
        this.seasonCycleTicks = seasonCycleTicks;
    }

    public static void encode(MessageSyncSeasonCycle packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.dimension.location().toString());
        buf.writeInt(packet.seasonCycleTicks);
    }

    public static MessageSyncSeasonCycle decode(FriendlyByteBuf buf) {
        return new MessageSyncSeasonCycle(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(buf.readUtf())), buf.readInt());
    }

    public static class Handler {

        public static void handle(MessageSyncSeasonCycle packet, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
                if (Minecraft.getInstance().player != null) {
                    ResourceKey<Level> playerDimension = Minecraft.getInstance().player.m_9236_().dimension();
                    if (playerDimension.equals(packet.dimension)) {
                        SeasonHandler.clientSeasonCycleTicks.replace(playerDimension, packet.seasonCycleTicks);
                    }
                }
            });
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        }
    }
}