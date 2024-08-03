package org.violetmoon.zeta.config;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.mod.ZetaMod;
import org.violetmoon.zeta.network.message.S2CUpdateFlag;

public class SyncedFlagHandler {

    private static ConfigFlagManager flagManager;

    private static List<String> sortedFlags;

    private static final WeakHashMap<PacketListener, Set<String>> flagsFromServer = new WeakHashMap();

    private static final WeakHashMap<ServerPlayer, Set<String>> flagsFromPlayers = new WeakHashMap();

    public static void setupFlagManager(ConfigFlagManager manager) {
        if (manager != null) {
            flagManager = manager;
            sortedFlags = (List<String>) manager.getAllFlags().stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static BitSet compileFlagInfo() {
        BitSet set = new BitSet();
        int i = 0;
        for (String flag : sortedFlags) {
            set.set(i++, flagManager.getFlag(flag));
        }
        return set;
    }

    public static int expectedLength() {
        return sortedFlags.size();
    }

    public static int expectedHash() {
        return sortedFlags.hashCode();
    }

    private static Set<String> decodeFlags(BitSet bitSet) {
        Set<String> enabledFlags = new HashSet();
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            enabledFlags.add((String) sortedFlags.get(i));
        }
        return enabledFlags;
    }

    public static void receiveFlagInfoFromPlayer(ServerPlayer player, BitSet bitSet) {
        flagsFromPlayers.put(player, decodeFlags(bitSet));
    }

    @OnlyIn(Dist.CLIENT)
    public static void receiveFlagInfoFromServer(BitSet bitSet) {
        flagsFromServer.put(Minecraft.getInstance().getConnection(), decodeFlags(bitSet));
    }

    @LoadEvent
    public static void sendFlagInfoToPlayers(ZConfigChanged event) {
        ZetaMod.ZETA.network.sendToPlayers(S2CUpdateFlag.createPacket(), flagsFromPlayers.keySet());
    }

    public static boolean getFlagForPlayer(ServerPlayer player, String flag) {
        Set<String> enabledFlags = (Set<String>) flagsFromPlayers.get(player);
        return enabledFlags == null ? flagManager.getFlag(flag) : enabledFlags.contains(flag);
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean getFlagForServer(String flag) {
        for (PacketListener listener : flagsFromServer.keySet()) {
            Set<String> enabledFlags = (Set<String>) flagsFromServer.get(listener);
            if (enabledFlags != null) {
                return enabledFlags.contains(flag);
            }
        }
        return flagManager.getFlag(flag);
    }
}