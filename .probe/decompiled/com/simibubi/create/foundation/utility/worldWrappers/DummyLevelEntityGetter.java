package com.simibubi.create.foundation.utility.worldWrappers;

import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;

public class DummyLevelEntityGetter<T extends EntityAccess> implements LevelEntityGetter<T> {

    @Override
    public T get(int int0) {
        return null;
    }

    @Override
    public T get(UUID pUuid) {
        return null;
    }

    @Override
    public Iterable<T> getAll() {
        return Collections.emptyList();
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> entityTypeTestTU0, AbortableIterationConsumer<U> abortableIterationConsumerU1) {
    }

    @Override
    public void get(AABB aABB0, Consumer<T> consumerT1) {
    }

    @Override
    public <U extends T> void get(EntityTypeTest<T, U> entityTypeTestTU0, AABB aABB1, AbortableIterationConsumer<U> abortableIterationConsumerU2) {
    }
}