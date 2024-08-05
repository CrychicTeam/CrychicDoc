package net.minecraft.network.protocol;

import com.mojang.logging.LogUtils;
import net.minecraft.network.PacketListener;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.BlockableEventLoop;
import org.slf4j.Logger;

public class PacketUtils {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> packetT0, T t1, ServerLevel serverLevel2) throws RunningOnDifferentThreadException {
        ensureRunningOnSameThread(packetT0, t1, serverLevel2.getServer());
    }

    public static <T extends PacketListener> void ensureRunningOnSameThread(Packet<T> packetT0, T t1, BlockableEventLoop<?> blockableEventLoop2) throws RunningOnDifferentThreadException {
        if (!blockableEventLoop2.isSameThread()) {
            blockableEventLoop2.executeIfPossible(() -> {
                if (t1.isAcceptingMessages()) {
                    try {
                        packetT0.handle(t1);
                    } catch (Exception var3) {
                        if (t1.shouldPropagateHandlingExceptions()) {
                            throw var3;
                        }
                        LOGGER.error("Failed to handle packet {}, suppressing error", packetT0, var3);
                    }
                } else {
                    LOGGER.debug("Ignoring packet due to disconnection: {}", packetT0);
                }
            });
            throw RunningOnDifferentThreadException.RUNNING_ON_DIFFERENT_THREAD;
        }
    }
}