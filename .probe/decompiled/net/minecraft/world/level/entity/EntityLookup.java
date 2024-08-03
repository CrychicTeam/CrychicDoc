package net.minecraft.world.level.entity;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.util.AbortableIterationConsumer;
import org.slf4j.Logger;

public class EntityLookup<T extends EntityAccess> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Int2ObjectMap<T> byId = new Int2ObjectLinkedOpenHashMap();

    private final Map<UUID, T> byUuid = Maps.newHashMap();

    public <U extends T> void getEntities(EntityTypeTest<T, U> entityTypeTestTU0, AbortableIterationConsumer<U> abortableIterationConsumerU1) {
        ObjectIterator var3 = this.byId.values().iterator();
        while (var3.hasNext()) {
            T $$2 = (T) var3.next();
            U $$3 = (U) entityTypeTestTU0.tryCast($$2);
            if ($$3 != null && abortableIterationConsumerU1.accept($$3).shouldAbort()) {
                return;
            }
        }
    }

    public Iterable<T> getAllEntities() {
        return Iterables.unmodifiableIterable(this.byId.values());
    }

    public void add(T t0) {
        UUID $$1 = t0.getUUID();
        if (this.byUuid.containsKey($$1)) {
            LOGGER.warn("Duplicate entity UUID {}: {}", $$1, t0);
        } else {
            this.byUuid.put($$1, t0);
            this.byId.put(t0.getId(), t0);
        }
    }

    public void remove(T t0) {
        this.byUuid.remove(t0.getUUID());
        this.byId.remove(t0.getId());
    }

    @Nullable
    public T getEntity(int int0) {
        return (T) this.byId.get(int0);
    }

    @Nullable
    public T getEntity(UUID uUID0) {
        return (T) this.byUuid.get(uUID0);
    }

    public int count() {
        return this.byUuid.size();
    }
}