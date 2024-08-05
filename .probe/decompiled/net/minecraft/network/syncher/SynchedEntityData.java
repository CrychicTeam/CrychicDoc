package net.minecraft.network.syncher;

import com.mojang.logging.LogUtils;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;

public class SynchedEntityData {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Object2IntMap<Class<? extends Entity>> ENTITY_ID_POOL = new Object2IntOpenHashMap();

    private static final int MAX_ID_VALUE = 254;

    private final Entity entity;

    private final Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = new Int2ObjectOpenHashMap();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private boolean isDirty;

    public SynchedEntityData(Entity entity0) {
        this.entity = entity0;
    }

    public static <T> EntityDataAccessor<T> defineId(Class<? extends Entity> classExtendsEntity0, EntityDataSerializer<T> entityDataSerializerT1) {
        if (LOGGER.isDebugEnabled()) {
            try {
                Class<?> $$2 = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
                if (!$$2.equals(classExtendsEntity0)) {
                    LOGGER.debug("defineId called for: {} from {}", new Object[] { classExtendsEntity0, $$2, new RuntimeException() });
                }
            } catch (ClassNotFoundException var5) {
            }
        }
        int $$3;
        if (ENTITY_ID_POOL.containsKey(classExtendsEntity0)) {
            $$3 = ENTITY_ID_POOL.getInt(classExtendsEntity0) + 1;
        } else {
            int $$4 = 0;
            Class<?> $$5 = classExtendsEntity0;
            while ($$5 != Entity.class) {
                $$5 = $$5.getSuperclass();
                if (ENTITY_ID_POOL.containsKey($$5)) {
                    $$4 = ENTITY_ID_POOL.getInt($$5) + 1;
                    break;
                }
            }
            $$3 = $$4;
        }
        if ($$3 > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + $$3 + "! (Max is 254)");
        } else {
            ENTITY_ID_POOL.put(classExtendsEntity0, $$3);
            return entityDataSerializerT1.createAccessor($$3);
        }
    }

    public <T> void define(EntityDataAccessor<T> entityDataAccessorT0, T t1) {
        int $$2 = entityDataAccessorT0.getId();
        if ($$2 > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + $$2 + "! (Max is 254)");
        } else if (this.itemsById.containsKey($$2)) {
            throw new IllegalArgumentException("Duplicate id value for " + $$2 + "!");
        } else if (EntityDataSerializers.getSerializedId(entityDataAccessorT0.getSerializer()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + entityDataAccessorT0.getSerializer() + " for " + $$2 + "!");
        } else {
            this.createDataItem(entityDataAccessorT0, t1);
        }
    }

    private <T> void createDataItem(EntityDataAccessor<T> entityDataAccessorT0, T t1) {
        SynchedEntityData.DataItem<T> $$2 = new SynchedEntityData.DataItem<>(entityDataAccessorT0, t1);
        this.lock.writeLock().lock();
        this.itemsById.put(entityDataAccessorT0.getId(), $$2);
        this.lock.writeLock().unlock();
    }

    public <T> boolean hasItem(EntityDataAccessor<T> entityDataAccessorT0) {
        return this.itemsById.containsKey(entityDataAccessorT0.getId());
    }

    private <T> SynchedEntityData.DataItem<T> getItem(EntityDataAccessor<T> entityDataAccessorT0) {
        this.lock.readLock().lock();
        SynchedEntityData.DataItem<T> $$1;
        try {
            $$1 = (SynchedEntityData.DataItem<T>) this.itemsById.get(entityDataAccessorT0.getId());
        } catch (Throwable var9) {
            CrashReport $$3 = CrashReport.forThrowable(var9, "Getting synched entity data");
            CrashReportCategory $$4 = $$3.addCategory("Synched entity data");
            $$4.setDetail("Data ID", entityDataAccessorT0);
            throw new ReportedException($$3);
        } finally {
            this.lock.readLock().unlock();
        }
        return $$1;
    }

    public <T> T get(EntityDataAccessor<T> entityDataAccessorT0) {
        return this.getItem(entityDataAccessorT0).getValue();
    }

    public <T> void set(EntityDataAccessor<T> entityDataAccessorT0, T t1) {
        this.set(entityDataAccessorT0, t1, false);
    }

    public <T> void set(EntityDataAccessor<T> entityDataAccessorT0, T t1, boolean boolean2) {
        SynchedEntityData.DataItem<T> $$3 = this.getItem(entityDataAccessorT0);
        if (boolean2 || ObjectUtils.notEqual(t1, $$3.getValue())) {
            $$3.setValue(t1);
            this.entity.onSyncedDataUpdated(entityDataAccessorT0);
            $$3.setDirty(true);
            this.isDirty = true;
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    @Nullable
    public List<SynchedEntityData.DataValue<?>> packDirty() {
        List<SynchedEntityData.DataValue<?>> $$0 = null;
        if (this.isDirty) {
            this.lock.readLock().lock();
            ObjectIterator var2 = this.itemsById.values().iterator();
            while (var2.hasNext()) {
                SynchedEntityData.DataItem<?> $$1 = (SynchedEntityData.DataItem<?>) var2.next();
                if ($$1.isDirty()) {
                    $$1.setDirty(false);
                    if ($$0 == null) {
                        $$0 = new ArrayList();
                    }
                    $$0.add($$1.value());
                }
            }
            this.lock.readLock().unlock();
        }
        this.isDirty = false;
        return $$0;
    }

    @Nullable
    public List<SynchedEntityData.DataValue<?>> getNonDefaultValues() {
        List<SynchedEntityData.DataValue<?>> $$0 = null;
        this.lock.readLock().lock();
        ObjectIterator var2 = this.itemsById.values().iterator();
        while (var2.hasNext()) {
            SynchedEntityData.DataItem<?> $$1 = (SynchedEntityData.DataItem<?>) var2.next();
            if (!$$1.isSetToDefault()) {
                if ($$0 == null) {
                    $$0 = new ArrayList();
                }
                $$0.add($$1.value());
            }
        }
        this.lock.readLock().unlock();
        return $$0;
    }

    public void assignValues(List<SynchedEntityData.DataValue<?>> listSynchedEntityDataDataValue0) {
        this.lock.writeLock().lock();
        try {
            for (SynchedEntityData.DataValue<?> $$1 : listSynchedEntityDataDataValue0) {
                SynchedEntityData.DataItem<?> $$2 = (SynchedEntityData.DataItem<?>) this.itemsById.get($$1.id);
                if ($$2 != null) {
                    this.assignValue($$2, $$1);
                    this.entity.onSyncedDataUpdated($$2.getAccessor());
                }
            }
        } finally {
            this.lock.writeLock().unlock();
        }
        this.entity.onSyncedDataUpdated(listSynchedEntityDataDataValue0);
    }

    private <T> void assignValue(SynchedEntityData.DataItem<T> synchedEntityDataDataItemT0, SynchedEntityData.DataValue<?> synchedEntityDataDataValue1) {
        if (!Objects.equals(synchedEntityDataDataValue1.serializer(), synchedEntityDataDataItemT0.accessor.getSerializer())) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Invalid entity data item type for field %d on entity %s: old=%s(%s), new=%s(%s)", synchedEntityDataDataItemT0.accessor.getId(), this.entity, synchedEntityDataDataItemT0.value, synchedEntityDataDataItemT0.value.getClass(), synchedEntityDataDataValue1.value, synchedEntityDataDataValue1.value.getClass()));
        } else {
            synchedEntityDataDataItemT0.setValue((T) synchedEntityDataDataValue1.value);
        }
    }

    public boolean isEmpty() {
        return this.itemsById.isEmpty();
    }

    public static class DataItem<T> {

        final EntityDataAccessor<T> accessor;

        T value;

        private final T initialValue;

        private boolean dirty;

        public DataItem(EntityDataAccessor<T> entityDataAccessorT0, T t1) {
            this.accessor = entityDataAccessorT0;
            this.initialValue = t1;
            this.value = t1;
        }

        public EntityDataAccessor<T> getAccessor() {
            return this.accessor;
        }

        public void setValue(T t0) {
            this.value = t0;
        }

        public T getValue() {
            return this.value;
        }

        public boolean isDirty() {
            return this.dirty;
        }

        public void setDirty(boolean boolean0) {
            this.dirty = boolean0;
        }

        public boolean isSetToDefault() {
            return this.initialValue.equals(this.value);
        }

        public SynchedEntityData.DataValue<T> value() {
            return SynchedEntityData.DataValue.create(this.accessor, this.value);
        }
    }

    public static record DataValue<T>(int f_252469_, EntityDataSerializer<T> f_252511_, T f_252525_) {

        private final int id;

        private final EntityDataSerializer<T> serializer;

        private final T value;

        public DataValue(int f_252469_, EntityDataSerializer<T> f_252511_, T f_252525_) {
            this.id = f_252469_;
            this.serializer = f_252511_;
            this.value = f_252525_;
        }

        public static <T> SynchedEntityData.DataValue<T> create(EntityDataAccessor<T> p_254543_, T p_254138_) {
            EntityDataSerializer<T> $$2 = p_254543_.getSerializer();
            return new SynchedEntityData.DataValue<>(p_254543_.getId(), $$2, $$2.copy(p_254138_));
        }

        public void write(FriendlyByteBuf p_253709_) {
            int $$1 = EntityDataSerializers.getSerializedId(this.serializer);
            if ($$1 < 0) {
                throw new EncoderException("Unknown serializer type " + this.serializer);
            } else {
                p_253709_.writeByte(this.id);
                p_253709_.writeVarInt($$1);
                this.serializer.write(p_253709_, this.value);
            }
        }

        public static SynchedEntityData.DataValue<?> read(FriendlyByteBuf p_254314_, int p_254356_) {
            int $$2 = p_254314_.readVarInt();
            EntityDataSerializer<?> $$3 = EntityDataSerializers.getSerializer($$2);
            if ($$3 == null) {
                throw new DecoderException("Unknown serializer type " + $$2);
            } else {
                return read(p_254314_, p_254356_, $$3);
            }
        }

        private static <T> SynchedEntityData.DataValue<T> read(FriendlyByteBuf p_254224_, int p_253899_, EntityDataSerializer<T> p_254222_) {
            return new SynchedEntityData.DataValue<>(p_253899_, p_254222_, p_254222_.read(p_254224_));
        }
    }
}