package net.minecraft.client.multiplayer.resolver;

import com.mojang.logging.LogUtils;
import java.util.Hashtable;
import java.util.Optional;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.slf4j.Logger;

@FunctionalInterface
public interface ServerRedirectHandler {

    Logger LOGGER = LogUtils.getLogger();

    ServerRedirectHandler EMPTY = p_171897_ -> Optional.empty();

    Optional<ServerAddress> lookupRedirect(ServerAddress var1);

    static ServerRedirectHandler createDnsSrvRedirectHandler() {
        DirContext $$2;
        try {
            String $$0 = "com.sun.jndi.dns.DnsContextFactory";
            Class.forName("com.sun.jndi.dns.DnsContextFactory");
            Hashtable<String, String> $$1 = new Hashtable();
            $$1.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            $$1.put("java.naming.provider.url", "dns:");
            $$1.put("com.sun.jndi.dns.timeout.retries", "1");
            $$2 = new InitialDirContext($$1);
        } catch (Throwable var3) {
            LOGGER.error("Failed to initialize SRV redirect resolved, some servers might not work", var3);
            return EMPTY;
        }
        return p_171900_ -> {
            if (p_171900_.getPort() == 25565) {
                try {
                    Attributes $$2x = $$2.getAttributes("_minecraft._tcp." + p_171900_.getHost(), new String[] { "SRV" });
                    Attribute $$3x = $$2x.get("srv");
                    if ($$3x != null) {
                        String[] $$4x = $$3x.get().toString().split(" ", 4);
                        return Optional.of(new ServerAddress($$4x[3], ServerAddress.parsePort($$4x[2])));
                    }
                } catch (Throwable var5) {
                }
            }
            return Optional.empty();
        };
    }
}