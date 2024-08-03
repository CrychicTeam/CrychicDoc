package net.minecraftforge.network;

import com.google.common.net.InetAddresses;
import com.mojang.logging.LogUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.util.HttpUtil;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;

public class DualStackUtils {

    private static final String INITIAL_PREFER_IPv4_STACK = System.getProperty("java.net.preferIPv4Stack") == null ? "false" : System.getProperty("java.net.preferIPv4Stack");

    private static final String INITIAL_PREFER_IPv6_ADDRESSES = System.getProperty("java.net.preferIPv6Addresses") == null ? "false" : System.getProperty("java.net.preferIPv6Addresses");

    private static final Logger LOGGER = LogUtils.getLogger();

    @Internal
    public static void initialise() {
    }

    public static boolean checkIPv6(String hostAddress) {
        Optional<InetSocketAddress> hostAddr = ServerNameResolver.DEFAULT.resolveAddress(ServerAddress.parseString(hostAddress)).map(ResolvedServerAddress::m_142641_);
        return hostAddr.isPresent() ? checkIPv6(((InetSocketAddress) hostAddr.get()).getAddress()) : false;
    }

    public static boolean checkIPv6(InetAddress inetAddress) {
        String currentThreadName = Thread.currentThread().getName();
        boolean shouldLogDebug = !currentThreadName.contains("Server Pinger #");
        if (inetAddress instanceof Inet6Address addr) {
            if (shouldLogDebug) {
                LOGGER.debug("Detected IPv6 address: \"" + addr.getHostAddress() + "\"");
            }
            System.setProperty("java.net.preferIPv4Stack", "false");
            System.setProperty("java.net.preferIPv6Addresses", "true");
            return true;
        } else if (inetAddress instanceof Inet4Address addr) {
            if (shouldLogDebug) {
                LOGGER.debug("Detected IPv4 address: \"" + addr.getHostAddress() + "\"");
            }
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("java.net.preferIPv6Addresses", "false");
            return false;
        } else {
            if (shouldLogDebug) {
                String addr = inetAddress == null ? "null" : "\"" + inetAddress.getHostAddress() + "\"";
                LOGGER.debug("Unable to determine IP version of address: " + addr);
            }
            if (INITIAL_PREFER_IPv4_STACK.equalsIgnoreCase("false") && INITIAL_PREFER_IPv6_ADDRESSES.equalsIgnoreCase("true")) {
                if (shouldLogDebug) {
                    LOGGER.debug("Assuming IPv6 as Java was explicitly told to prefer it...");
                }
                System.setProperty("java.net.preferIPv4Stack", "false");
                System.setProperty("java.net.preferIPv6Addresses", "true");
                return true;
            } else {
                if (shouldLogDebug) {
                    LOGGER.debug("Assuming IPv4...");
                }
                System.setProperty("java.net.preferIPv4Stack", "true");
                System.setProperty("java.net.preferIPv6Addresses", "false");
                return false;
            }
        }
    }

    @Nullable
    public static InetAddress getLocalAddress() {
        InetAddress localAddr = new InetSocketAddress(HttpUtil.getAvailablePort()).getAddress();
        if (localAddr.isAnyLocalAddress()) {
            return localAddr;
        } else {
            try {
                return InetAddress.getByName("localhost");
            } catch (UnknownHostException var2) {
                return null;
            }
        }
    }

    public static String getMulticastGroup() {
        return checkIPv6(getLocalAddress()) ? "FF75:230::60" : "224.0.2.60";
    }

    public static void logInitialPreferences() {
        LOGGER.debug("Initial IPv4 stack preference: " + INITIAL_PREFER_IPv4_STACK);
        LOGGER.debug("Initial IPv6 addresses preference: " + INITIAL_PREFER_IPv6_ADDRESSES);
    }

    public static String getAddressString(SocketAddress address) {
        if (address instanceof InetSocketAddress inetAddress) {
            String formatted;
            if (inetAddress.isUnresolved()) {
                formatted = inetAddress.getHostName() + "/<unresolved>";
            } else {
                formatted = InetAddresses.toAddrString(inetAddress.getAddress());
                if (inetAddress.getAddress() instanceof Inet6Address) {
                    formatted = "[" + formatted + "]";
                }
                formatted = "/" + formatted;
            }
            return formatted + ":" + inetAddress.getPort();
        } else {
            return address.toString();
        }
    }
}