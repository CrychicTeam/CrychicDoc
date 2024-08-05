package io.github.lightman314.lightmanscurrency.common.emergency_ejection;

import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.client.data.ClientEjectionData;
import io.github.lightman314.lightmanscurrency.network.message.emergencyejection.SPacketSyncEjectionData;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber
public class EjectionSaveData extends SavedData {

    private final List<EjectionData> emergencyEjectionData = new ArrayList();

    private EjectionSaveData() {
    }

    private EjectionSaveData(CompoundTag compound) {
        ListTag ejectionData = compound.getList("EmergencyEjectionData", 10);
        for (int i = 0; i < ejectionData.size(); i++) {
            try {
                EjectionData e = EjectionData.loadData(ejectionData.getCompound(i));
                if (e != null && !e.isEmpty()) {
                    this.emergencyEjectionData.add(e);
                }
            } catch (Throwable var5) {
                LightmansCurrency.LogError("Error loading ejection data entry " + i, var5);
            }
        }
        LightmansCurrency.LogDebug("Server loaded " + this.emergencyEjectionData.size() + " ejection data entries from file.");
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag ejectionData = new ListTag();
        this.emergencyEjectionData.forEach(data -> ejectionData.add(data.save()));
        compound.put("EmergencyEjectionData", ejectionData);
        return compound;
    }

    private static EjectionSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(EjectionSaveData::new, EjectionSaveData::new, "lightmanscurrency_ejection_data");
            }
        }
        return null;
    }

    public static List<EjectionData> GetEjectionData(boolean isClient) {
        if (isClient) {
            return ClientEjectionData.GetEjectionData();
        } else {
            EjectionSaveData esd = get();
            return esd != null ? new ArrayList(esd.emergencyEjectionData) : new ArrayList();
        }
    }

    public static List<EjectionData> GetValidEjectionData(boolean isClient, Player player) {
        List<EjectionData> ejectionData = GetEjectionData(isClient);
        return (List<EjectionData>) (ejectionData != null ? (List) ejectionData.stream().filter(e -> e.canAccess(player)).collect(Collectors.toList()) : new ArrayList());
    }

    @Deprecated
    public static void GiveOldEjectionData(EjectionData data) {
        EjectionSaveData esd = get();
        if (esd != null && data != null && !data.isEmpty()) {
            esd.emergencyEjectionData.add(data);
            MarkEjectionDataDirty();
        }
    }

    public static void HandleEjectionData(Level level, BlockPos pos, EjectionData data) {
        if (!level.isClientSide) {
            Objects.requireNonNull(data);
            if (data.getContainerSize() != 0) {
                if (LCConfig.SERVER.safelyEjectMachineContents.get() && !LCConfig.SERVER.anarchyMode.get()) {
                    EjectionSaveData esd = get();
                    if (esd != null) {
                        esd.emergencyEjectionData.add(data);
                        MarkEjectionDataDirty();
                    }
                } else {
                    InventoryUtil.dumpContents(level, pos, data);
                }
                data.pushNotificationToOwner();
            }
        }
    }

    public static void RemoveEjectionData(EjectionData data) {
        EjectionSaveData esd = get();
        if (esd != null) {
            Objects.requireNonNull(data);
            if (esd.emergencyEjectionData.contains(data)) {
                esd.emergencyEjectionData.remove(data);
                MarkEjectionDataDirty();
            }
        }
    }

    public static void MarkEjectionDataDirty() {
        EjectionSaveData esd = get();
        if (esd != null) {
            esd.m_77762_();
            CompoundTag compound = new CompoundTag();
            ListTag ejectionList = new ListTag();
            esd.emergencyEjectionData.forEach(data -> ejectionList.add(data.save()));
            compound.put("EmergencyEjectionData", ejectionList);
            new SPacketSyncEjectionData(compound).sendToAll();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EjectionSaveData esd = get();
        CompoundTag compound = new CompoundTag();
        ListTag ejectionList = new ListTag();
        esd.emergencyEjectionData.forEach(data -> ejectionList.add(data.save()));
        compound.put("EmergencyEjectionData", ejectionList);
        new SPacketSyncEjectionData(compound).sendTo(event.getEntity());
    }
}