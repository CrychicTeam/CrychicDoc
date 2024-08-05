package net.blay09.mods.balm.forge.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkChannels {

    private static final String version = "1.0";

    private static final Map<String, SimpleChannel> channels = new ConcurrentHashMap();

    private static final Map<String, Predicate<String>> acceptedClientVersions = new ConcurrentHashMap();

    private static final Map<String, Predicate<String>> acceptedServerVersions = new ConcurrentHashMap();

    public static SimpleChannel get(String modId) {
        return (SimpleChannel) channels.computeIfAbsent(modId, key -> {
            ResourceLocation channelName = new ResourceLocation(key, "network");
            return NetworkRegistry.newSimpleChannel(channelName, () -> "1.0", it -> ((Predicate) acceptedClientVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck)).test(it), it -> ((Predicate) acceptedServerVersions.getOrDefault(modId, NetworkChannels::defaultVersionCheck)).test(it));
        });
    }

    public static void allowClientOnly(String modId) {
        acceptedClientVersions.put(modId, (Predicate) it -> true);
    }

    public static void allowServerOnly(String modId) {
        acceptedServerVersions.put(modId, (Predicate) it -> true);
    }

    private static boolean defaultVersionCheck(String it) {
        return it.equals("1.0");
    }
}