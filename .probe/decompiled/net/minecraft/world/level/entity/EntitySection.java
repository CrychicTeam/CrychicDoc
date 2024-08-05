package net.minecraft.world.level.entity;

import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.stream.Stream;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public class EntitySection<T extends EntityAccess> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ClassInstanceMultiMap<T> storage;

    private Visibility chunkStatus;

    public EntitySection(Class<T> classT0, Visibility visibility1) {
        this.chunkStatus = visibility1;
        this.storage = new ClassInstanceMultiMap<>(classT0);
    }

    public void add(T t0) {
        this.storage.add(t0);
    }

    public boolean remove(T t0) {
        return this.storage.remove(t0);
    }

    public AbortableIterationConsumer.Continuation getEntities(AABB aABB0, AbortableIterationConsumer<T> abortableIterationConsumerT1) {
        for (T $$2 : this.storage) {
            if ($$2.getBoundingBox().intersects(aABB0) && abortableIterationConsumerT1.accept($$2).shouldAbort()) {
                return AbortableIterationConsumer.Continuation.ABORT;
            }
        }
        return AbortableIterationConsumer.Continuation.CONTINUE;
    }

    public <U extends T> AbortableIterationConsumer.Continuation getEntities(EntityTypeTest<T, U> entityTypeTestTU0, AABB aABB1, AbortableIterationConsumer<? super U> abortableIterationConsumerSuperU2) {
        Collection<? extends T> $$3 = this.storage.find(entityTypeTestTU0.getBaseClass());
        if ($$3.isEmpty()) {
            return AbortableIterationConsumer.Continuation.CONTINUE;
        } else {
            for (T $$4 : $$3) {
                U $$5 = (U) entityTypeTestTU0.tryCast($$4);
                if ($$5 != null && $$4.getBoundingBox().intersects(aABB1) && abortableIterationConsumerSuperU2.accept($$5).shouldAbort()) {
                    return AbortableIterationConsumer.Continuation.ABORT;
                }
            }
            return AbortableIterationConsumer.Continuation.CONTINUE;
        }
    }

    public boolean isEmpty() {
        return this.storage.isEmpty();
    }

    public Stream<T> getEntities() {
        return this.storage.stream();
    }

    public Visibility getStatus() {
        return this.chunkStatus;
    }

    public Visibility updateChunkStatus(Visibility visibility0) {
        Visibility $$1 = this.chunkStatus;
        this.chunkStatus = visibility0;
        return $$1;
    }

    @VisibleForDebug
    public int size() {
        return this.storage.size();
    }
}