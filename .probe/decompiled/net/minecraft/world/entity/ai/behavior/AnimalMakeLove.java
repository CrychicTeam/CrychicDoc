package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Animal;

public class AnimalMakeLove extends Behavior<Animal> {

    private static final int BREED_RANGE = 3;

    private static final int MIN_DURATION = 60;

    private static final int MAX_DURATION = 110;

    private final EntityType<? extends Animal> partnerType;

    private final float speedModifier;

    private long spawnChildAtTime;

    public AnimalMakeLove(EntityType<? extends Animal> entityTypeExtendsAnimal0, float float1) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 110);
        this.partnerType = entityTypeExtendsAnimal0;
        this.speedModifier = float1;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Animal animal1) {
        return animal1.isInLove() && this.findValidBreedPartner(animal1).isPresent();
    }

    protected void start(ServerLevel serverLevel0, Animal animal1, long long2) {
        Animal $$3 = (Animal) this.findValidBreedPartner(animal1).get();
        animal1.m_6274_().setMemory(MemoryModuleType.BREED_TARGET, $$3);
        $$3.m_6274_().setMemory(MemoryModuleType.BREED_TARGET, animal1);
        BehaviorUtils.lockGazeAndWalkToEachOther(animal1, $$3, this.speedModifier);
        int $$4 = 60 + animal1.m_217043_().nextInt(50);
        this.spawnChildAtTime = long2 + (long) $$4;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Animal animal1, long long2) {
        if (!this.hasBreedTargetOfRightType(animal1)) {
            return false;
        } else {
            Animal $$3 = this.getBreedTarget(animal1);
            return $$3.m_6084_() && animal1.canMate($$3) && BehaviorUtils.entityIsVisible(animal1.m_6274_(), $$3) && long2 <= this.spawnChildAtTime;
        }
    }

    protected void tick(ServerLevel serverLevel0, Animal animal1, long long2) {
        Animal $$3 = this.getBreedTarget(animal1);
        BehaviorUtils.lockGazeAndWalkToEachOther(animal1, $$3, this.speedModifier);
        if (animal1.m_19950_($$3, 3.0)) {
            if (long2 >= this.spawnChildAtTime) {
                animal1.spawnChildFromBreeding(serverLevel0, $$3);
                animal1.m_6274_().eraseMemory(MemoryModuleType.BREED_TARGET);
                $$3.m_6274_().eraseMemory(MemoryModuleType.BREED_TARGET);
            }
        }
    }

    protected void stop(ServerLevel serverLevel0, Animal animal1, long long2) {
        animal1.m_6274_().eraseMemory(MemoryModuleType.BREED_TARGET);
        animal1.m_6274_().eraseMemory(MemoryModuleType.WALK_TARGET);
        animal1.m_6274_().eraseMemory(MemoryModuleType.LOOK_TARGET);
        this.spawnChildAtTime = 0L;
    }

    private Animal getBreedTarget(Animal animal0) {
        return (Animal) animal0.m_6274_().getMemory(MemoryModuleType.BREED_TARGET).get();
    }

    private boolean hasBreedTargetOfRightType(Animal animal0) {
        Brain<?> $$1 = animal0.m_6274_();
        return $$1.hasMemoryValue(MemoryModuleType.BREED_TARGET) && ((AgeableMob) $$1.getMemory(MemoryModuleType.BREED_TARGET).get()).m_6095_() == this.partnerType;
    }

    private Optional<? extends Animal> findValidBreedPartner(Animal animal0) {
        return ((NearestVisibleLivingEntities) animal0.m_6274_().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).findClosest(p_289312_ -> {
            if (p_289312_.m_6095_() == this.partnerType && p_289312_ instanceof Animal $$2 && animal0.canMate($$2)) {
                return true;
            }
            return false;
        }).map(Animal.class::cast);
    }
}