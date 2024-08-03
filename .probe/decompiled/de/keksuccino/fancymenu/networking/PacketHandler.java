package de.keksuccino.fancymenu.networking;

import de.keksuccino.fancymenu.util.threading.MainThreadTaskExecutor;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Consumer<String> sendToServerDataConsumer = null;

    private static BiConsumer<ServerPlayer, String> sendToClientPlayerAndDataConsumer = null;

    public static <T extends Packet> void sendToServer(@NotNull T packet) {
        Objects.requireNonNull(sendToServerDataConsumer, "Tried to send packet to server too early! No logic set yet!");
        PacketCodec<T> codec = PacketRegistry.getCodecFor((T) Objects.requireNonNull(packet));
        if (codec != null) {
            try {
                sendToServerDataConsumer.accept((String) Objects.requireNonNull(codec.serialize(packet)));
            } catch (Exception var3) {
                LOGGER.error("[FANCYMENU] Failed to send packet to server!", var3);
            }
        } else {
            LOGGER.error("[FANCYMENU] No codec found for packet: " + packet.getClass(), new NullPointerException("Codec returned for packet was NULL!"));
        }
    }

    public static <T extends Packet> void sendToClient(@NotNull ServerPlayer toPlayer, @NotNull T packet) {
        Objects.requireNonNull(sendToClientPlayerAndDataConsumer, "Tried to send packet to client too early! No logic set yet!");
        PacketCodec<T> codec = PacketRegistry.getCodecFor((T) Objects.requireNonNull(packet));
        if (codec != null) {
            try {
                sendToClientPlayerAndDataConsumer.accept((ServerPlayer) Objects.requireNonNull(toPlayer), (String) Objects.requireNonNull(codec.serialize(packet)));
            } catch (Exception var4) {
                LOGGER.error("[FANCYMENU] Failed to send packet to client!", var4);
            }
        } else {
            LOGGER.error("[FANCYMENU] No codec found for packet: " + packet.getClass(), new NullPointerException("Codec returned for packet was NULL!"));
        }
    }

    public static void setSendToServerLogic(Consumer<String> dataConsumer) {
        sendToServerDataConsumer = dataConsumer;
    }

    public static void setSendToClientLogic(BiConsumer<ServerPlayer, String> playerAndDataConsumer) {
        sendToClientPlayerAndDataConsumer = playerAndDataConsumer;
    }

    public static void onPacketReceived(@Nullable ServerPlayer sender, @NotNull PacketHandler.PacketDirection direction, @NotNull String dataWithIdentifier) {
        if (dataWithIdentifier.contains(":")) {
            String[] dataSplit = dataWithIdentifier.split(":", 2);
            PacketCodec<?> codec = PacketRegistry.getCodec(dataSplit[0]);
            if (codec == null) {
                LOGGER.error("[FANCYMENU] No codec for packet data found with identifier: " + dataSplit[0], new NullPointerException("Codec returned for identifier was NULL!"));
            } else {
                if (direction == PacketHandler.PacketDirection.TO_CLIENT) {
                    Packet packet = deserializePacket(() -> (Packet) Objects.requireNonNull(codec.deserialize(dataSplit[1])));
                    if (packet != null) {
                        MainThreadTaskExecutor.executeInMainThread(() -> {
                            try {
                                packet.processPacket(sender);
                            } catch (Exception var3x) {
                                LOGGER.error("[FANCYMENU] Failed to process packet on client!", var3x);
                            }
                        }, MainThreadTaskExecutor.ExecuteTiming.POST_CLIENT_TICK);
                    }
                } else if (direction == PacketHandler.PacketDirection.TO_SERVER) {
                    if (sender != null) {
                        MinecraftServer server = sender.m_20194_();
                        if (server != null) {
                            Packet packet = deserializePacket(() -> (Packet) Objects.requireNonNull(codec.deserialize(dataSplit[1])));
                            if (packet != null) {
                                sender.m_20194_().execute(() -> {
                                    try {
                                        packet.processPacket(sender);
                                    } catch (Exception var3x) {
                                        LOGGER.error("[FANCYMENU] Failed to process packet on server!", var3x);
                                    }
                                });
                            }
                        } else {
                            LOGGER.error("[FANCYMENU] Failed to process packet on server!", new NullPointerException("Server instance of sender was NULL!"));
                        }
                    } else {
                        LOGGER.error("[FANCYMENU] Failed to process packet on server!", new NullPointerException("Sender was NULL!"));
                    }
                }
            }
        }
    }

    @Nullable
    protected static Packet deserializePacket(@NotNull Supplier<Packet> packetSupplier) {
        try {
            return (Packet) packetSupplier.get();
        } catch (Exception var2) {
            LOGGER.error("[FANCYMENU] Failed to deserialize packet!", var2);
            return null;
        }
    }

    public static enum PacketDirection {

        TO_SERVER, TO_CLIENT
    }
}