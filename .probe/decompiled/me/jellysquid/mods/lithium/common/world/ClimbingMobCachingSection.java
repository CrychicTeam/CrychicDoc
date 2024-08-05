package me.jellysquid.mods.lithium.common.world;

import java.util.ArrayList;
import me.jellysquid.mods.lithium.common.entity.pushable.BlockCachingEntity;
import me.jellysquid.mods.lithium.common.entity.pushable.EntityPushablePredicate;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public interface ClimbingMobCachingSection {

    AbortableIterationConsumer.Continuation collectPushableEntities(Level var1, Entity var2, AABB var3, EntityPushablePredicate<? super Entity> var4, ArrayList<Entity> var5);

    void onEntityModifiedCachedBlock(BlockCachingEntity var1, BlockState var2);
}