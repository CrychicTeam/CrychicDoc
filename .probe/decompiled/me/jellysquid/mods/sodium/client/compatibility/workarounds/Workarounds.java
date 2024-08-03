package me.jellysquid.mods.sodium.client.compatibility.workarounds;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import me.jellysquid.mods.sodium.client.compatibility.environment.OSInfo;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterInfo;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterProbe;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Workarounds {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-Workarounds");

    private static final AtomicReference<Set<Workarounds.Reference>> ACTIVE_WORKAROUNDS = new AtomicReference(EnumSet.noneOf(Workarounds.Reference.class));

    public static void init() {
        Set<Workarounds.Reference> workarounds = findNecessaryWorkarounds();
        if (!workarounds.isEmpty()) {
            LOGGER.warn("Embeddium has applied one or more workarounds to prevent crashes or other issues on your system: [{}]", workarounds.stream().map(Enum::name).collect(Collectors.joining(", ")));
            LOGGER.warn("This is not necessarily an issue, but it may result in certain features or optimizations being disabled. You can sometimes fix these issues by upgrading your graphics driver.");
        }
        ACTIVE_WORKAROUNDS.set(workarounds);
    }

    private static Set<Workarounds.Reference> findNecessaryWorkarounds() {
        EnumSet<Workarounds.Reference> workarounds = EnumSet.noneOf(Workarounds.Reference.class);
        OSInfo.OS operatingSystem = OSInfo.getOS();
        Collection<GraphicsAdapterInfo> graphicsAdapters = GraphicsAdapterProbe.getAdapters();
        if (isUsingNvidiaGraphicsCard(operatingSystem, graphicsAdapters)) {
            workarounds.add(Workarounds.Reference.NVIDIA_THREADED_OPTIMIZATIONS);
        }
        if (operatingSystem == OSInfo.OS.LINUX) {
            String session = System.getenv("XDG_SESSION_TYPE");
            if (session == null) {
                LOGGER.warn("Unable to determine desktop session type because the environment variable XDG_SESSION_TYPE is not set! Your user session may not be configured correctly.");
            }
            if (Objects.equals(session, "wayland")) {
                workarounds.add(Workarounds.Reference.NO_ERROR_CONTEXT_UNSUPPORTED);
            }
        }
        return Collections.unmodifiableSet(workarounds);
    }

    private static boolean isUsingNvidiaGraphicsCard(OSInfo.OS operatingSystem, Collection<GraphicsAdapterInfo> adapters) {
        return (operatingSystem == OSInfo.OS.WINDOWS || operatingSystem == OSInfo.OS.LINUX) && adapters.stream().anyMatch(adapter -> adapter.vendor() == GraphicsAdapterVendor.NVIDIA);
    }

    public static boolean isWorkaroundEnabled(Workarounds.Reference id) {
        return ((Set) ACTIVE_WORKAROUNDS.get()).contains(id);
    }

    public static enum Reference {

        NVIDIA_THREADED_OPTIMIZATIONS, NO_ERROR_CONTEXT_UNSUPPORTED
    }
}