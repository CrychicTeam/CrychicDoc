package net.minecraft.world.entity.ai.behavior;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class BehaviorUtils {

    private BehaviorUtils() {
    }

    public static void lockGazeAndWalkToEachOther(LivingEntity livingEntity0, LivingEntity livingEntity1, float float2) {
        lookAtEachOther(livingEntity0, livingEntity1);
        setWalkAndLookTargetMemoriesToEachOther(livingEntity0, livingEntity1, float2);
    }

    public static boolean entityIsVisible(Brain<?> brain0, LivingEntity livingEntity1) {
        Optional<NearestVisibleLivingEntities> $$2 = brain0.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        return $$2.isPresent() && ((NearestVisibleLivingEntities) $$2.get()).contains(livingEntity1);
    }

    public static boolean targetIsValid(Brain<?> brain0, MemoryModuleType<? extends LivingEntity> memoryModuleTypeExtendsLivingEntity1, EntityType<?> entityType2) {
        return targetIsValid(brain0, memoryModuleTypeExtendsLivingEntity1, p_289317_ -> p_289317_.m_6095_() == entityType2);
    }

    private static boolean targetIsValid(Brain<?> brain0, MemoryModuleType<? extends LivingEntity> memoryModuleTypeExtendsLivingEntity1, Predicate<LivingEntity> predicateLivingEntity2) {
        return brain0.getMemory(memoryModuleTypeExtendsLivingEntity1).filter(predicateLivingEntity2).filter(LivingEntity::m_6084_).filter(p_186037_ -> entityIsVisible(brain0, p_186037_)).isPresent();
    }

    private static void lookAtEachOther(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        lookAtEntity(livingEntity0, livingEntity1);
        lookAtEntity(livingEntity1, livingEntity0);
    }

    public static void lookAtEntity(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        livingEntity0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity1, true));
    }

    private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity livingEntity0, LivingEntity livingEntity1, float float2) {
        int $$3 = 2;
        setWalkAndLookTargetMemories(livingEntity0, livingEntity1, float2, 2);
        setWalkAndLookTargetMemories(livingEntity1, livingEntity0, float2, 2);
    }

    public static void setWalkAndLookTargetMemories(LivingEntity livingEntity0, Entity entity1, float float2, int int3) {
        setWalkAndLookTargetMemories(livingEntity0, new EntityTracker(entity1, true), float2, int3);
    }

    public static void setWalkAndLookTargetMemories(LivingEntity livingEntity0, BlockPos blockPos1, float float2, int int3) {
        setWalkAndLookTargetMemories(livingEntity0, new BlockPosTracker(blockPos1), float2, int3);
    }

    public static void setWalkAndLookTargetMemories(LivingEntity livingEntity0, PositionTracker positionTracker1, float float2, int int3) {
        WalkTarget $$4 = new WalkTarget(positionTracker1, float2, int3);
        livingEntity0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, positionTracker1);
        livingEntity0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, $$4);
    }

    public static void throwItem(LivingEntity livingEntity0, ItemStack itemStack1, Vec3 vec2) {
        Vec3 $$3 = new Vec3(0.3F, 0.3F, 0.3F);
        throwItem(livingEntity0, itemStack1, vec2, $$3, 0.3F);
    }

    public static void throwItem(LivingEntity livingEntity0, ItemStack itemStack1, Vec3 vec2, Vec3 vec3, float float4) {
        double $$5 = livingEntity0.m_20188_() - (double) float4;
        ItemEntity $$6 = new ItemEntity(livingEntity0.m_9236_(), livingEntity0.m_20185_(), $$5, livingEntity0.m_20189_(), itemStack1);
        $$6.setThrower(livingEntity0.m_20148_());
        Vec3 $$7 = vec2.subtract(livingEntity0.m_20182_());
        $$7 = $$7.normalize().multiply(vec3.x, vec3.y, vec3.z);
        $$6.m_20256_($$7);
        $$6.setDefaultPickUpDelay();
        livingEntity0.m_9236_().m_7967_($$6);
    }

    public static SectionPos findSectionClosestToVillage(ServerLevel serverLevel0, SectionPos sectionPos1, int int2) {
        int $$3 = serverLevel0.sectionsToVillage(sectionPos1);
        return (SectionPos) SectionPos.cube(sectionPos1, int2).filter(p_186017_ -> serverLevel0.sectionsToVillage(p_186017_) < $$3).min(Comparator.comparingInt(serverLevel0::m_8828_)).orElse(sectionPos1);
    }

    public static boolean isWithinAttackRange(Mob mob0, LivingEntity livingEntity1, int int2) {
        if (mob0.m_21205_().getItem() instanceof ProjectileWeaponItem $$3 && mob0.canFireProjectileWeapon($$3)) {
            int $$4 = $$3.getDefaultProjectileRange() - int2;
            return mob0.m_19950_(livingEntity1, (double) $$4);
        }
        return mob0.isWithinMeleeAttackRange(livingEntity1);
    }

    public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(LivingEntity livingEntity0, LivingEntity livingEntity1, double double2) {
        Optional<LivingEntity> $$3 = livingEntity0.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
        if ($$3.isEmpty()) {
            return false;
        } else {
            double $$4 = livingEntity0.m_20238_(((LivingEntity) $$3.get()).m_20182_());
            double $$5 = livingEntity0.m_20238_(livingEntity1.m_20182_());
            return $$5 > $$4 + double2 * double2;
        }
    }

    public static boolean canSee(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        Brain<?> $$2 = livingEntity0.getBrain();
        return !$$2.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES) ? false : ((NearestVisibleLivingEntities) $$2.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).contains(livingEntity1);
    }

    public static LivingEntity getNearestTarget(LivingEntity livingEntity0, Optional<LivingEntity> optionalLivingEntity1, LivingEntity livingEntity2) {
        return optionalLivingEntity1.isEmpty() ? livingEntity2 : getTargetNearestMe(livingEntity0, (LivingEntity) optionalLivingEntity1.get(), livingEntity2);
    }

    public static LivingEntity getTargetNearestMe(LivingEntity livingEntity0, LivingEntity livingEntity1, LivingEntity livingEntity2) {
        Vec3 $$3 = livingEntity1.m_20182_();
        Vec3 $$4 = livingEntity2.m_20182_();
        return livingEntity0.m_20238_($$3) < livingEntity0.m_20238_($$4) ? livingEntity1 : livingEntity2;
    }

    public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(LivingEntity livingEntity0, MemoryModuleType<UUID> memoryModuleTypeUUID1) {
        Optional<UUID> $$2 = livingEntity0.getBrain().getMemory(memoryModuleTypeUUID1);
        return $$2.map(p_289315_ -> ((ServerLevel) livingEntity0.m_9236_()).getEntity(p_289315_)).map(p_186019_ -> p_186019_ instanceof LivingEntity $$1 ? $$1 : null);
    }

    @Nullable
    public static Vec3 getRandomSwimmablePos(PathfinderMob pathfinderMob0, int int1, int int2) {
        Vec3 $$3 = DefaultRandomPos.getPos(pathfinderMob0, int1, int2);
        int $$4 = 0;
        while ($$3 != null && !pathfinderMob0.m_9236_().getBlockState(BlockPos.containing($$3)).m_60647_(pathfinderMob0.m_9236_(), BlockPos.containing($$3), PathComputationType.WATER) && $$4++ < 10) {
            $$3 = DefaultRandomPos.getPos(pathfinderMob0, int1, int2);
        }
        return $$3;
    }

    public static boolean isBreeding(LivingEntity livingEntity0) {
        return livingEntity0.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
    }
}