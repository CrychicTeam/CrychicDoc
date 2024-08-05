package com.mna.api.entities.construct.ai;

import com.mna.api.entities.construct.IConstruct;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public interface IMutexManager {

    boolean claimMutex(BlockPos var1, IConstruct<?> var2, ConstructAITask<?> var3);

    void releaseMutex(BlockPos var1, IConstruct<?> var2, ConstructAITask<?> var3);

    boolean claimMutex(Entity var1, IConstruct<?> var2, ConstructAITask<?> var3);

    void releaseMutex(Entity var1, IConstruct<?> var2, ConstructAITask<?> var3);

    void releaseAllMutexes(IConstruct<?> var1, ConstructAITask<?> var2);
}