package fr.frinn.custommachinery.common.util;

import dev.architectury.event.events.common.TickEvent;
import fr.frinn.custommachinery.api.machine.MachineTile;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

public class MachineList {

    private static final List<WeakReference<MachineTile>> LOADED_MACHINES = Collections.synchronizedList(new ArrayList());

    private static boolean needRefresh = false;

    public static void addMachine(MachineTile tile) {
        if (tile.m_58904_() != null && !tile.m_58904_().isClientSide()) {
            LOADED_MACHINES.add(new WeakReference(tile));
        }
    }

    public static void refreshAllMachines() {
        getLoadedMachines().forEach(tile -> tile.refreshMachine(null));
    }

    public static void setNeedRefresh() {
        needRefresh = true;
    }

    public static Optional<MachineTile> findNearest(Player player, @Nullable ResourceLocation machine, int radius) {
        return getLoadedMachines().stream().filter(tile -> tile.m_58904_() == player.m_9236_() && tile.m_58899_().m_123314_(player.m_20183_(), (double) radius) && (machine == null || machine.equals(tile.getMachine().getId()))).min(Comparator.comparingInt(tile -> tile.m_58899_().m_123333_(player.m_20183_())));
    }

    public static Optional<MachineTile> findInSameChunk(MachineTile machine) {
        return getLoadedMachines().stream().filter(tile -> tile != machine && tile.m_58904_() == machine.m_58904_() && new ChunkPos(tile.m_58899_()).equals(new ChunkPos(machine.m_58899_()))).findFirst();
    }

    public static List<MachineTile> getLoadedMachines() {
        Iterator<WeakReference<MachineTile>> iterator = LOADED_MACHINES.iterator();
        List<MachineTile> loadedMachines = new ArrayList();
        while (iterator.hasNext()) {
            MachineTile tile = (MachineTile) ((WeakReference) iterator.next()).get();
            if (tile != null && !tile.m_58901_()) {
                loadedMachines.add(tile);
            } else {
                iterator.remove();
            }
        }
        return loadedMachines;
    }

    private static void serverTick(MinecraftServer server) {
        if (needRefresh) {
            needRefresh = false;
            refreshAllMachines();
        }
    }

    static {
        TickEvent.ServerLevelTick.SERVER_POST.register(MachineList::serverTick);
    }
}