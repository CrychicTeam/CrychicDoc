package me.jellysquid.mods.lithium.mixin.entity.data_tracker.use_arrays;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ SynchedEntityData.class })
public abstract class DataTrackerMixin {

    private static final int DEFAULT_ENTRY_COUNT = 10;

    private static final int GROW_FACTOR = 8;

    @Shadow
    @Final
    private ReadWriteLock lock;

    @Mutable
    @Shadow
    @Final
    private Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById;

    private SynchedEntityData.DataItem<?>[] entriesArray = new SynchedEntityData.DataItem[10];

    @Redirect(method = { "addTrackedData(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V" }, at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;put(ILjava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object onAddTrackedDataInsertMap(Int2ObjectMap<?> int2ObjectMap, int k, Object valueRaw) {
        SynchedEntityData.DataItem<?> v = (SynchedEntityData.DataItem<?>) valueRaw;
        SynchedEntityData.DataItem<?>[] storage = this.entriesArray;
        if (storage.length <= k) {
            int newSize = Math.min(k + 8, 256);
            this.entriesArray = storage = (SynchedEntityData.DataItem<?>[]) Arrays.copyOf(storage, newSize);
        }
        storage[k] = v;
        return this.itemsById.put(k, v);
    }

    @Overwrite
    private <T> SynchedEntityData.DataItem<T> getItem(EntityDataAccessor<T> data) {
        this.lock.readLock().lock();
        Object var4;
        try {
            SynchedEntityData.DataItem<?>[] array = this.entriesArray;
            int id = data.getId();
            if (id >= 0 && id < array.length) {
                return (SynchedEntityData.DataItem<T>) array[id];
            }
            var4 = null;
        } catch (Throwable var8) {
            throw onGetException(var8, data);
        } finally {
            this.lock.readLock().unlock();
        }
        return (SynchedEntityData.DataItem<T>) var4;
    }

    private static <T> ReportedException onGetException(Throwable cause, EntityDataAccessor<T> data) {
        CrashReport report = CrashReport.forThrowable(cause, "Getting synced entity data");
        CrashReportCategory section = report.addCategory("Synced entity data");
        section.setDetail("Data ID", data);
        return new ReportedException(report);
    }
}