package me.jellysquid.mods.lithium.mixin.entity.collisions.unpushable_cramming;

import java.util.ArrayList;
import java.util.Iterator;
import me.jellysquid.mods.lithium.common.entity.pushable.BlockCachingEntity;
import me.jellysquid.mods.lithium.common.entity.pushable.EntityPushablePredicate;
import me.jellysquid.mods.lithium.common.entity.pushable.PushableEntityClassGroup;
import me.jellysquid.mods.lithium.common.util.collections.ReferenceMaskedList;
import me.jellysquid.mods.lithium.common.world.ClimbingMobCachingSection;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntitySection.class })
public abstract class EntityTrackingSectionMixin<T extends EntityAccess> implements ClimbingMobCachingSection {

    @Shadow
    @Final
    private ClassInstanceMultiMap<T> storage;

    @Shadow
    private Visibility chunkStatus;

    @Unique
    private ReferenceMaskedList<Entity> pushableEntities;

    @Override
    public AbortableIterationConsumer.Continuation collectPushableEntities(Level world, Entity except, AABB box, EntityPushablePredicate<? super Entity> entityPushablePredicate, ArrayList<Entity> entities) {
        Iterator<?> entityIterator;
        if (this.pushableEntities != null) {
            entityIterator = this.pushableEntities.iterator();
        } else {
            entityIterator = this.storage.iterator();
        }
        int i = 0;
        int j = 0;
        while (entityIterator.hasNext()) {
            Entity entity = (Entity) entityIterator.next();
            if (entity.getBoundingBox().intersects(box) && !entity.isSpectator() && entity != except && !(entity instanceof EnderDragon)) {
                i++;
                if (entityPushablePredicate.test(entity)) {
                    j++;
                    entities.add(entity);
                }
            }
        }
        if (this.pushableEntities == null && i >= 25 && i >= j * 2) {
            this.startFilteringPushableEntities();
        }
        return AbortableIterationConsumer.Continuation.CONTINUE;
    }

    private void startFilteringPushableEntities() {
        this.pushableEntities = new ReferenceMaskedList<>();
        for (T entity : this.storage) {
            this.onStartClimbingCachingEntity((Entity) entity);
        }
    }

    private void stopFilteringPushableEntities() {
        this.pushableEntities = null;
    }

    @Override
    public void onEntityModifiedCachedBlock(BlockCachingEntity entity, BlockState newBlockState) {
        if (this.pushableEntities == null) {
            entity.lithiumSetClimbingMobCachingSectionUpdateBehavior(false);
        } else {
            this.updatePushabilityOnCachedStateChange(entity, newBlockState);
        }
    }

    private void updatePushabilityOnCachedStateChange(BlockCachingEntity entity, BlockState newBlockState) {
        boolean visible = entityPushableHeuristic(newBlockState);
        this.pushableEntities.setVisible((Entity) entity, visible);
    }

    private void onStartClimbingCachingEntity(Entity entity) {
        Class<? extends Entity> entityClass = entity.getClass();
        if (PushableEntityClassGroup.MAYBE_PUSHABLE.contains(entityClass)) {
            this.pushableEntities.add(entity);
            boolean shouldTrackBlockChanges = PushableEntityClassGroup.CACHABLE_UNPUSHABILITY.contains(entityClass);
            if (shouldTrackBlockChanges) {
                BlockCachingEntity blockCachingEntity = (BlockCachingEntity) entity;
                this.updatePushabilityOnCachedStateChange(blockCachingEntity, blockCachingEntity.getCachedFeetBlockState());
                blockCachingEntity.lithiumSetClimbingMobCachingSectionUpdateBehavior(true);
            }
        }
    }

    @Inject(method = { "add(Lnet/minecraft/world/entity/EntityLike;)V" }, at = { @At("RETURN") })
    private void onEntityAdded(T entityLike, CallbackInfo ci) {
        if (this.pushableEntities != null) {
            if (!this.chunkStatus.isAccessible()) {
                this.stopFilteringPushableEntities();
            } else {
                this.onStartClimbingCachingEntity((Entity) entityLike);
                if (this.pushableEntities.totalSize() > this.storage.size()) {
                    this.stopFilteringPushableEntities();
                }
            }
        }
    }

    @Inject(method = { "remove(Lnet/minecraft/world/entity/EntityLike;)Z" }, at = { @At("RETURN") })
    private void onEntityRemoved(T entityLike, CallbackInfoReturnable<Boolean> cir) {
        if (this.pushableEntities != null) {
            if (!this.chunkStatus.isAccessible()) {
                this.stopFilteringPushableEntities();
            } else {
                this.pushableEntities.remove((Entity) entityLike);
            }
        }
    }

    private static boolean entityPushableHeuristic(BlockState cachedFeetBlockState) {
        return cachedFeetBlockState == null || !cachedFeetBlockState.m_204336_(BlockTags.CLIMBABLE);
    }
}