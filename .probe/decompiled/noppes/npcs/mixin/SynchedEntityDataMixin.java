package noppes.npcs.mixin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.network.syncher.SynchedEntityData;
import noppes.npcs.client.ISynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ SynchedEntityData.class })
public class SynchedEntityDataMixin implements ISynchedEntityData {

    @Shadow
    private final Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = new Int2ObjectOpenHashMap();

    @Shadow
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Nullable
    @Override
    public List<SynchedEntityData.DataItem<?>> getAll() {
        List<SynchedEntityData.DataItem<?>> list = null;
        this.lock.readLock().lock();
        ObjectIterator var2 = this.itemsById.values().iterator();
        while (var2.hasNext()) {
            SynchedEntityData.DataItem<?> dataitem = (SynchedEntityData.DataItem<?>) var2.next();
            if (list == null) {
                list = Lists.newArrayList();
            }
            list.add(new SynchedEntityData.DataItem<>(dataitem.getAccessor(), dataitem.value()));
        }
        this.lock.readLock().unlock();
        return list;
    }
}