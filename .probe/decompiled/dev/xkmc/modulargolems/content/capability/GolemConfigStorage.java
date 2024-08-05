package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.HashMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class GolemConfigStorage {

    public static Capability<GolemConfigStorage> CAPABILITY = CapabilityManager.get(new CapabilityToken<GolemConfigStorage>() {
    });

    private static GolemConfigStorage CACHE = null;

    public final Level level;

    @SerialField
    private final HashMap<UUID, GolemConfigEntry[]> storage = new HashMap();

    public static GolemConfigStorage get(Level level) {
        return level instanceof ServerLevel sl ? (GolemConfigStorage) sl.getServer().overworld().getCapability(CAPABILITY).resolve().get() : getClientCache(level);
    }

    private static GolemConfigStorage getClientCache(Level level) {
        if (CACHE == null || CACHE.level != level) {
            CACHE = new GolemConfigStorage(level);
        }
        return CACHE;
    }

    public GolemConfigStorage(Level level) {
        this.level = level;
    }

    public GolemConfigEntry getOrCreateStorage(UUID id, int color, Component comp) {
        color &= 15;
        GolemConfigEntry[] entries = (GolemConfigEntry[]) this.storage.computeIfAbsent(id, k -> new GolemConfigEntry[16]);
        if (entries[color] == null) {
            entries[color] = GolemConfigEntry.getDefault(id, color, comp);
        }
        return entries[color].init(id, color);
    }

    @Nullable
    public GolemConfigEntry getStorage(UUID id, int color) {
        if (color >= 0 && color <= 15) {
            GolemConfigEntry[] entries = (GolemConfigEntry[]) this.storage.computeIfAbsent(id, k -> new GolemConfigEntry[16]);
            GolemConfigEntry ans = entries[color];
            if (ans == null) {
                return null;
            } else {
                ans.init(id, color);
                return ans;
            }
        } else {
            return null;
        }
    }

    public void replaceStorage(GolemConfigEntry entry) {
        GolemConfigEntry[] entries = (GolemConfigEntry[]) this.storage.computeIfAbsent(entry.getID(), k -> new GolemConfigEntry[16]);
        entries[entry.getColor()] = entry.copyFrom(entries[entry.getColor()]);
    }

    public void init() {
    }

    public static void register() {
    }
}