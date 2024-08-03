package io.redspace.ironsspellbooks.data;

import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class IronsDataStorage extends SavedData {

    public static IronsDataStorage INSTANCE;

    public static void init(DimensionDataStorage dimensionDataStorage) {
        if (dimensionDataStorage != null) {
            INSTANCE = dimensionDataStorage.computeIfAbsent(IronsDataStorage::load, IronsDataStorage::new, "irons_spellbooks_data");
        }
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        CompoundTag tag = new CompoundTag();
        tag.put("GuidingBoltManager", GuidingBoltManager.INSTANCE.serializeNBT());
        tag.put("PortalManager", PortalManager.INSTANCE.serializeNBT());
        return tag;
    }

    public static IronsDataStorage load(CompoundTag tag) {
        if (tag.contains("GuidingBoltManager", 10)) {
            GuidingBoltManager.INSTANCE.deserializeNBT(tag.getCompound("GuidingBoltManager"));
        }
        if (tag.contains("PortalManager", 10)) {
            PortalManager.INSTANCE.deserializeNBT(tag.getCompound("PortalManager"));
        }
        return new IronsDataStorage();
    }
}