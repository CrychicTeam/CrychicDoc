package net.minecraftforge.network.filters;

import com.google.common.collect.ImmutableMap;
import io.netty.channel.ChannelPipeline;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkFilters {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, Function<Connection, VanillaPacketFilter>> instances = ImmutableMap.of("forge:vanilla_filter", (Function) manager -> new VanillaConnectionNetworkFilter(), "forge:forge_fixes", ForgeConnectionNetworkFilter::new);

    public static void injectIfNecessary(Connection manager) {
        ChannelPipeline pipeline = manager.channel().pipeline();
        if (pipeline.get("packet_handler") != null) {
            instances.forEach((key, filterFactory) -> {
                VanillaPacketFilter filter = (VanillaPacketFilter) filterFactory.apply(manager);
                if (filter.isNecessary(manager)) {
                    pipeline.addBefore("packet_handler", key, filter);
                    LOGGER.debug("Injected {} into {}", filter, manager);
                }
            });
        }
    }

    private NetworkFilters() {
    }
}