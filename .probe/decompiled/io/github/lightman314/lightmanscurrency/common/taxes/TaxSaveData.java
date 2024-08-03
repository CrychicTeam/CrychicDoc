package io.github.lightman314.lightmanscurrency.common.taxes;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.client.data.ClientTaxData;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.tax.SPacketRemoveTax;
import io.github.lightman314.lightmanscurrency.network.message.tax.SPacketSyncClientTax;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "lightmanscurrency")
public class TaxSaveData extends SavedData {

    public static final long SERVER_TAX_ID = -9L;

    private long nextID = 0L;

    private final Map<Long, TaxEntry> entries = new HashMap();

    private TaxSaveData() {
    }

    private TaxSaveData(CompoundTag compound) {
        this.nextID = compound.getLong("NextID");
        ListTag list = compound.getList("TaxEntries", 10);
        for (int i = 0; i < list.size(); i++) {
            TaxEntry entry = new TaxEntry();
            entry.load(list.getCompound(i));
            if (entry.getID() >= 0L || entry.isServerEntry()) {
                this.entries.put(entry.getID(), entry.unlock());
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        compound.putLong("NextID", this.nextID);
        ListTag entryList = new ListTag();
        this.entries.forEach((id, entry) -> {
            CompoundTag entryTag = entry.save();
            if (entryTag != null) {
                entryList.add(entryTag);
            }
        });
        compound.put("TaxEntries", entryList);
        return compound;
    }

    private static TaxSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.overworld().getDataStorage().computeIfAbsent(TaxSaveData::new, TaxSaveData::new, "lightmanscurrency_tax_data") : null;
    }

    public static List<TaxEntry> GetAllTaxEntries(boolean isClient) {
        if (isClient) {
            return ClientTaxData.GetAllTaxEntries();
        } else {
            TaxSaveData data = get();
            return data != null ? ImmutableList.copyOf(data.entries.values()) : ImmutableList.of();
        }
    }

    @Nullable
    public static TaxEntry GetTaxEntry(long id, boolean isClient) {
        if (isClient) {
            return ClientTaxData.GetEntry(id);
        } else {
            TaxSaveData data = get();
            return data != null ? (TaxEntry) data.entries.get(id) : null;
        }
    }

    @Nullable
    public static TaxEntry GetServerTaxEntry(boolean isClient) {
        if (isClient) {
            return ClientTaxData.GetEntry(-9L);
        } else {
            TaxSaveData data = get();
            if (data != null) {
                TaxEntry entry = (TaxEntry) data.entries.get(-9L);
                if (entry == null) {
                    entry = new TaxEntry(-9L, null, null);
                    data.entries.put(-9L, entry);
                    MarkTaxEntryDirty(-9L, entry.save());
                }
                return entry;
            } else {
                return null;
            }
        }
    }

    public static void MarkTaxEntryDirty(long id, CompoundTag syncData) {
        if (id < 0L && id != -9L) {
            LightmansCurrency.LogWarning("Attempted to mark a Tax Entry as changed, but is has no defined ID!");
        } else {
            TaxSaveData data = get();
            if (data != null) {
                data.m_77762_();
                syncData.putLong("ID", id);
                new SPacketSyncClientTax(syncData).sendToAll();
            }
        }
    }

    public static long CreateAndRegister(@Nullable BlockEntity spawnBE, @Nullable Player player) {
        TaxSaveData data = get();
        if (data != null) {
            long id = data.nextID++;
            TaxEntry entry = new TaxEntry(id, spawnBE, player);
            data.entries.put(id, entry.unlock());
            MarkTaxEntryDirty(id, entry.save());
            return id;
        } else {
            return -1L;
        }
    }

    public static void RemoveEntry(long id) {
        TaxSaveData data = get();
        if (data != null && data.entries.containsKey(id)) {
            data.entries.remove(id);
            data.m_77762_();
            new SPacketRemoveTax(id).sendToAll();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        TaxSaveData data = get();
        if (data != null) {
            PacketDistributor.PacketTarget target = LightmansCurrencyPacketHandler.getTarget(event.getEntity());
            data.entries.forEach((id, entry) -> new SPacketSyncClientTax(entry.save()).sendToTarget(target));
        }
    }
}