package net.minecraft.world.level;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface EntityGetter {

    List<Entity> getEntities(@Nullable Entity var1, AABB var2, Predicate<? super Entity> var3);

    <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> var1, AABB var2, Predicate<? super T> var3);

    default <T extends Entity> List<T> getEntitiesOfClass(Class<T> classT0, AABB aABB1, Predicate<? super T> predicateSuperT2) {
        return this.getEntities(EntityTypeTest.forClass(classT0), aABB1, predicateSuperT2);
    }

    List<? extends Player> players();

    default List<Entity> getEntities(@Nullable Entity entity0, AABB aABB1) {
        return this.getEntities(entity0, aABB1, EntitySelector.NO_SPECTATORS);
    }

    default boolean isUnobstructed(@Nullable Entity entity0, VoxelShape voxelShape1) {
        if (voxelShape1.isEmpty()) {
            return true;
        } else {
            for (Entity $$2 : this.getEntities(entity0, voxelShape1.bounds())) {
                if (!$$2.isRemoved() && $$2.blocksBuilding && (entity0 == null || !$$2.isPassengerOfSameVehicle(entity0)) && Shapes.joinIsNotEmpty(voxelShape1, Shapes.create($$2.getBoundingBox()), BooleanOp.AND)) {
                    return false;
                }
            }
            return true;
        }
    }

    default <T extends Entity> List<T> getEntitiesOfClass(Class<T> classT0, AABB aABB1) {
        return this.getEntitiesOfClass(classT0, aABB1, EntitySelector.NO_SPECTATORS);
    }

    default List<VoxelShape> getEntityCollisions(@Nullable Entity entity0, AABB aABB1) {
        if (aABB1.getSize() < 1.0E-7) {
            return List.of();
        } else {
            Predicate<Entity> $$2 = entity0 == null ? EntitySelector.CAN_BE_COLLIDED_WITH : EntitySelector.NO_SPECTATORS.and(entity0::m_7337_);
            List<Entity> $$3 = this.getEntities(entity0, aABB1.inflate(1.0E-7), $$2);
            if ($$3.isEmpty()) {
                return List.of();
            } else {
                Builder<VoxelShape> $$4 = ImmutableList.builderWithExpectedSize($$3.size());
                for (Entity $$5 : $$3) {
                    $$4.add(Shapes.create($$5.getBoundingBox()));
                }
                return $$4.build();
            }
        }
    }

    @Nullable
    default Player getNearestPlayer(double double0, double double1, double double2, double double3, @Nullable Predicate<Entity> predicateEntity4) {
        double $$5 = -1.0;
        Player $$6 = null;
        for (Player $$7 : this.players()) {
            if (predicateEntity4 == null || predicateEntity4.test($$7)) {
                double $$8 = $$7.m_20275_(double0, double1, double2);
                if ((double3 < 0.0 || $$8 < double3 * double3) && ($$5 == -1.0 || $$8 < $$5)) {
                    $$5 = $$8;
                    $$6 = $$7;
                }
            }
        }
        return $$6;
    }

    @Nullable
    default Player getNearestPlayer(Entity entity0, double double1) {
        return this.getNearestPlayer(entity0.getX(), entity0.getY(), entity0.getZ(), double1, false);
    }

    @Nullable
    default Player getNearestPlayer(double double0, double double1, double double2, double double3, boolean boolean4) {
        Predicate<Entity> $$5 = boolean4 ? EntitySelector.NO_CREATIVE_OR_SPECTATOR : EntitySelector.NO_SPECTATORS;
        return this.getNearestPlayer(double0, double1, double2, double3, $$5);
    }

    default boolean hasNearbyAlivePlayer(double double0, double double1, double double2, double double3) {
        for (Player $$4 : this.players()) {
            if (EntitySelector.NO_SPECTATORS.test($$4) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test($$4)) {
                double $$5 = $$4.m_20275_(double0, double1, double2);
                if (double3 < 0.0 || $$5 < double3 * double3) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    default Player getNearestPlayer(TargetingConditions targetingConditions0, LivingEntity livingEntity1) {
        return this.getNearestEntity(this.players(), targetingConditions0, livingEntity1, livingEntity1.m_20185_(), livingEntity1.m_20186_(), livingEntity1.m_20189_());
    }

    @Nullable
    default Player getNearestPlayer(TargetingConditions targetingConditions0, LivingEntity livingEntity1, double double2, double double3, double double4) {
        return this.getNearestEntity(this.players(), targetingConditions0, livingEntity1, double2, double3, double4);
    }

    @Nullable
    default Player getNearestPlayer(TargetingConditions targetingConditions0, double double1, double double2, double double3) {
        return this.getNearestEntity(this.players(), targetingConditions0, null, double1, double2, double3);
    }

    @Nullable
    default <T extends LivingEntity> T getNearestEntity(Class<? extends T> classExtendsT0, TargetingConditions targetingConditions1, @Nullable LivingEntity livingEntity2, double double3, double double4, double double5, AABB aABB6) {
        return this.getNearestEntity(this.getEntitiesOfClass(classExtendsT0, aABB6, p_186454_ -> true), targetingConditions1, livingEntity2, double3, double4, double5);
    }

    @Nullable
    default <T extends LivingEntity> T getNearestEntity(List<? extends T> listExtendsT0, TargetingConditions targetingConditions1, @Nullable LivingEntity livingEntity2, double double3, double double4, double double5) {
        double $$6 = -1.0;
        T $$7 = null;
        for (T $$8 : listExtendsT0) {
            if (targetingConditions1.test(livingEntity2, $$8)) {
                double $$9 = $$8.m_20275_(double3, double4, double5);
                if ($$6 == -1.0 || $$9 < $$6) {
                    $$6 = $$9;
                    $$7 = $$8;
                }
            }
        }
        return $$7;
    }

    default List<Player> getNearbyPlayers(TargetingConditions targetingConditions0, LivingEntity livingEntity1, AABB aABB2) {
        List<Player> $$3 = Lists.newArrayList();
        for (Player $$4 : this.players()) {
            if (aABB2.contains($$4.m_20185_(), $$4.m_20186_(), $$4.m_20189_()) && targetingConditions0.test(livingEntity1, $$4)) {
                $$3.add($$4);
            }
        }
        return $$3;
    }

    default <T extends LivingEntity> List<T> getNearbyEntities(Class<T> classT0, TargetingConditions targetingConditions1, LivingEntity livingEntity2, AABB aABB3) {
        List<T> $$4 = this.getEntitiesOfClass(classT0, aABB3, p_186450_ -> true);
        List<T> $$5 = Lists.newArrayList();
        for (T $$6 : $$4) {
            if (targetingConditions1.test(livingEntity2, $$6)) {
                $$5.add($$6);
            }
        }
        return $$5;
    }

    @Nullable
    default Player getPlayerByUUID(UUID uUID0) {
        for (int $$1 = 0; $$1 < this.players().size(); $$1++) {
            Player $$2 = (Player) this.players().get($$1);
            if (uUID0.equals($$2.m_20148_())) {
                return $$2;
            }
        }
        return null;
    }
}