package net.minecraft.client.multiplayer.resolver;

import com.google.common.net.HostAndPort;
import com.mojang.logging.LogUtils;
import java.net.IDN;
import org.slf4j.Logger;

public final class ServerAddress {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final HostAndPort hostAndPort;

    private static final ServerAddress INVALID = new ServerAddress(HostAndPort.fromParts("server.invalid", 25565));

    public ServerAddress(String string0, int int1) {
        this(HostAndPort.fromParts(string0, int1));
    }

    private ServerAddress(HostAndPort hostAndPort0) {
        this.hostAndPort = hostAndPort0;
    }

    public String getHost() {
        try {
            return IDN.toASCII(this.hostAndPort.getHost());
        } catch (IllegalArgumentException var2) {
            return "";
        }
    }

    public int getPort() {
        return this.hostAndPort.getPort();
    }

    public static ServerAddress parseString(String string0) {
        if (string0 == null) {
            return INVALID;
        } else {
            try {
                HostAndPort $$1 = HostAndPort.fromString(string0).withDefaultPort(25565);
                return $$1.getHost().isEmpty() ? INVALID : new ServerAddress($$1);
            } catch (IllegalArgumentException var2) {
                LOGGER.info("Failed to parse URL {}", string0, var2);
                return INVALID;
            }
        }
    }

    public static boolean isValidAddress(String string0) {
        try {
            HostAndPort $$1 = HostAndPort.fromString(string0);
            String $$2 = $$1.getHost();
            if (!$$2.isEmpty()) {
                IDN.toASCII($$2);
                return true;
            }
        } catch (IllegalArgumentException var3) {
        }
        return false;
    }

    static int parsePort(String string0) {
        try {
            return Integer.parseInt(string0.trim());
        } catch (Exception var2) {
            return 25565;
        }
    }

    public String toString() {
        return this.hostAndPort.toString();
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return object0 instanceof ServerAddress ? this.hostAndPort.equals(((ServerAddress) object0).hostAndPort) : false;
        }
    }

    public int hashCode() {
        return this.hostAndPort.hashCode();
    }
}