package de.keksuccino.fancymenu.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketRegistry {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, PacketCodec<?>> CODECS = new HashMap();

    private static boolean registrationsAllowed = true;

    public static void register(@NotNull PacketCodec<?> codec) {
        if (!registrationsAllowed) {
            throw new RuntimeException("Tried to register PacketCodec too late! PacketCodecs need to get registered as early as possible!");
        } else {
            if (CODECS.containsKey(Objects.requireNonNull(codec.getPacketIdentifier()))) {
                LOGGER.warn("[FANCYMENU] PacketCodec with identifier '" + codec.getPacketIdentifier() + "' already registered! Overriding codec!");
            }
            CODECS.put(codec.getPacketIdentifier(), codec);
        }
    }

    @NotNull
    public static List<PacketCodec<?>> getCodecs() {
        return new ArrayList(CODECS.values());
    }

    @Nullable
    public static PacketCodec<?> getCodec(@NotNull String identifier) {
        return (PacketCodec<?>) CODECS.get(identifier);
    }

    @Nullable
    public static <T extends Packet> PacketCodec<T> getCodecFor(@NotNull T packet) {
        try {
            for (PacketCodec<?> codec : CODECS.values()) {
                if (codec.getType() == packet.getClass()) {
                    return (PacketCodec<T>) codec;
                }
            }
        } catch (Exception var3) {
            LOGGER.error("[FANCYMENU] Failed to get codec for packet!", var3);
        }
        return null;
    }

    public static void endRegistrationPhase() {
        registrationsAllowed = false;
    }
}