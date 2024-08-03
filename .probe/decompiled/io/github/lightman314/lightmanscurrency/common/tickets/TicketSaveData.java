package io.github.lightman314.lightmanscurrency.common.tickets;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

public class TicketSaveData extends SavedData {

    private long nextID = 0L;

    private final Map<UUID, Long> convertedIDs = new HashMap();

    private TicketSaveData() {
    }

    private TicketSaveData(CompoundTag compound) {
        this.nextID = compound.getLong("NextID");
        if (compound.contains("ConvertedIDs")) {
            ListTag list = compound.getList("ConvertedIDs", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entry = list.getCompound(i);
                UUID uuid = entry.getUUID("UUID");
                long id = entry.getLong("ID");
                this.convertedIDs.put(uuid, id);
            }
        }
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag compound) {
        compound.putLong("NextID", this.nextID);
        ListTag list = new ListTag();
        this.convertedIDs.forEach((uuid, id) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("UUID", uuid);
            entry.putLong("ID", id);
            list.add(entry);
        });
        if (list.size() > 0) {
            compound.put("ConvertedIDs", list);
        }
        return compound;
    }

    private static TicketSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.overworld().getDataStorage().computeIfAbsent(TicketSaveData::new, TicketSaveData::new, "lightmanscurrency_ticket_data") : null;
    }

    private long createNextIDInternal() {
        long id = this.nextID++;
        this.m_77762_();
        return id;
    }

    public static long peekNextID() {
        TicketSaveData tsd = get();
        return tsd != null ? tsd.nextID : 0L;
    }

    public static long createNextID() {
        TicketSaveData tsd = get();
        return tsd != null ? tsd.createNextIDInternal() : 0L;
    }

    public static long getConvertedID(UUID oldID) {
        TicketSaveData tsd = get();
        if (tsd != null) {
            if (tsd.convertedIDs.containsKey(oldID)) {
                return (Long) tsd.convertedIDs.get(oldID);
            } else {
                tsd.convertedIDs.put(oldID, tsd.createNextIDInternal());
                tsd.m_77762_();
                return (Long) tsd.convertedIDs.get(oldID);
            }
        } else {
            return 0L;
        }
    }
}