package net.minecraft.client.multiplayer.resolver;

import com.mojang.logging.LogUtils;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import org.slf4j.Logger;

@FunctionalInterface
public interface ServerAddressResolver {

    Logger LOGGER = LogUtils.getLogger();

    ServerAddressResolver SYSTEM = p_171878_ -> {
        try {
            InetAddress $$1 = InetAddress.getByName(p_171878_.getHost());
            return Optional.of(ResolvedServerAddress.from(new InetSocketAddress($$1, p_171878_.getPort())));
        } catch (UnknownHostException var2) {
            LOGGER.debug("Couldn't resolve server {} address", p_171878_.getHost(), var2);
            return Optional.empty();
        }
    };

    Optional<ResolvedServerAddress> resolve(ServerAddress var1);
}