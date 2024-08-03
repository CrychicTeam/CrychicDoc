package dev.architectury.networking.forge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

@EventBusSubscriber(modid = "architectury")
public class NetworkManagerImpl {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation CHANNEL_ID = new ResourceLocation("architectury:network");

    static final ResourceLocation SYNC_IDS = new ResourceLocation("architectury:sync_ids");

    static final EventNetworkChannel CHANNEL = NetworkRegistry.newEventChannel(CHANNEL_ID, () -> "1", version -> true, version -> true);

    static final Map<ResourceLocation, NetworkManager.NetworkReceiver> S2C = Maps.newHashMap();

    static final Map<ResourceLocation, NetworkManager.NetworkReceiver> C2S = Maps.newHashMap();

    static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = Maps.newHashMap();

    static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = Maps.newHashMap();

    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();

    private static final Multimap<Player, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);

    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver receiver) {
        Objects.requireNonNull(id, "Cannot register receiver with a null ID!");
        packetTransformers = (List<PacketTransformer>) Objects.requireNonNullElse(packetTransformers, List.of());
        Objects.requireNonNull(receiver, "Cannot register a null receiver!");
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, packetTransformers, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, packetTransformers, receiver);
        }
    }

    public static Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buffer) {
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeResourceLocation(id);
        packetBuffer.writeBytes(buffer);
        return (side == NetworkManager.Side.C2S ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT).<Packet<?>>buildPacket(Pair.of(packetBuffer, 0), CHANNEL_ID).getThis();
    }

    public static void collectPackets(PacketSink sink, NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        PacketTransformer transformer = side == NetworkManager.Side.C2S ? (PacketTransformer) C2S_TRANSFORMERS.get(id) : (PacketTransformer) S2C_TRANSFORMERS.get(id);
        if (transformer != null) {
            transformer.outbound(side, id, buf, (side1, id1, buf1) -> sink.accept(toPacket(side1, id1, buf1)));
        } else {
            sink.accept(toPacket(side, id, buf));
        }
    }

    static <T extends NetworkEvent> Consumer<T> createPacketHandler(Class<T> clazz, Map<ResourceLocation, PacketTransformer> map) {
        return event -> {
            if (event.getClass() == clazz) {
                final NetworkEvent.Context context = (NetworkEvent.Context) event.getSource().get();
                if (!context.getPacketHandled()) {
                    FriendlyByteBuf buffer = event.getPayload();
                    if (buffer != null) {
                        ResourceLocation type = buffer.readResourceLocation();
                        PacketTransformer transformer = (PacketTransformer) map.get(type);
                        if (transformer != null) {
                            NetworkManager.Side side = context.getDirection().getReceptionSide() == LogicalSide.CLIENT ? NetworkManager.Side.S2C : NetworkManager.Side.C2S;
                            NetworkManager.PacketContext packetContext = new NetworkManager.PacketContext() {

                                @Override
                                public Player getPlayer() {
                                    return (Player) (this.getEnvironment() == Env.CLIENT ? this.getClientPlayer() : context.getSender());
                                }

                                @Override
                                public void queue(Runnable runnable) {
                                    context.enqueueWork(runnable);
                                }

                                @Override
                                public Env getEnvironment() {
                                    return context.getDirection().getReceptionSide() == LogicalSide.CLIENT ? Env.CLIENT : Env.SERVER;
                                }

                                private Player getClientPlayer() {
                                    return (Player) DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::getClientPlayer);
                                }
                            };
                            transformer.inbound(side, type, buffer, packetContext, (side1, id1, buf1) -> {
                                NetworkManager.NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? (NetworkManager.NetworkReceiver) C2S.get(id1) : (NetworkManager.NetworkReceiver) S2C.get(id1);
                                if (networkReceiver == null) {
                                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                                } else {
                                    networkReceiver.receive(buf1, packetContext);
                                }
                            });
                        } else {
                            LOGGER.error("Unknown message ID: " + type);
                        }
                        context.setPacketHandled(true);
                    }
                }
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerS2CReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver receiver) {
        LOGGER.info("Registering S2C receiver with id {}", id);
        S2C.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        S2C_TRANSFORMERS.put(id, transformer);
    }

    public static void registerC2SReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver receiver) {
        LOGGER.info("Registering C2S receiver with id {}", id);
        C2S.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        C2S_TRANSFORMERS.put(id, transformer);
    }

    public static boolean canServerReceive(ResourceLocation id) {
        return serverReceivables.contains(id);
    }

    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return clientReceivables.get(player).contains(id);
    }

    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        return NetworkHooks.getEntitySpawningPacket(entity);
    }

    static FriendlyByteBuf sendSyncPacket(Map<ResourceLocation, NetworkManager.NetworkReceiver> map) {
        List<ResourceLocation> availableIds = Lists.newArrayList(map.keySet());
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeInt(availableIds.size());
        for (ResourceLocation availableId : availableIds) {
            packetBuffer.writeResourceLocation(availableId);
        }
        return packetBuffer;
    }

    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.sendToPlayer((ServerPlayer) event.getEntity(), SYNC_IDS, sendSyncPacket(C2S));
    }

    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        clientReceivables.removeAll(event.getEntity());
    }

    static {
        CHANNEL.addListener(createPacketHandler(NetworkEvent.ClientCustomPayloadEvent.class, C2S_TRANSFORMERS));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        registerC2SReceiver(SYNC_IDS, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = (Set<ResourceLocation>) clientReceivables.get(context.getPlayer());
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
        });
    }
}