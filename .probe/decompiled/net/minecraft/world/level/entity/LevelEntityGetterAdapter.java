package net.minecraft.world.level.entity;

import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.phys.AABB;

public class LevelEntityGetterAdapter<T extends EntityAccess> implements LevelEntityGetter<T> {

    private final EntityLookup<T> visibleEntities;

    private final EntitySectionStorage<T> sectionStorage;

    public LevelEntityGetterAdapter(EntityLookup<T> entityLookupT0, EntitySectionStorage<T> entitySectionStorageT1) {
        this.visibleEntities = entityLookupT0;
        this.sectionStorage = entitySectionStorageT1;
    }

    @Nullable
    @Override
    public T get(int int0) {
        return this.visibleEntities.getEntity(int0);
    }

    @Nullable
    @Override
    public T get(UUID uUID0) {
        return this.visibleEntities.getEntity(uUID0);
    }

    @Override
    public Iterable<T> getAll() {
        return this.visibleEntities.getAllEntities();
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> entityTypeTestTU0, AbortableIterationConsumer<U> abortableIterationConsumerU1) {
        this.visibleEntities.getEntities(entityTypeTestTU0, abortableIterationConsumerU1);
    }

    @Override
    public void get(AABB aABB0, Consumer<T> consumerT1) {
        this.sectionStorage.getEntities(aABB0, AbortableIterationConsumer.forConsumer(consumerT1));
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> entityTypeTestTU0, AABB aABB1, AbortableIterationConsumer<U> abortableIterationConsumerU2) {
        this.sectionStorage.getEntities(entityTypeTestTU0, aABB1, abortableIterationConsumerU2);
    }
}