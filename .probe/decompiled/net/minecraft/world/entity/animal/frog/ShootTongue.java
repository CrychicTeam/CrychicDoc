package net.minecraft.world.entity.animal.frog;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class ShootTongue extends Behavior<Frog> {

    public static final int TIME_OUT_DURATION = 100;

    public static final int CATCH_ANIMATION_DURATION = 6;

    public static final int TONGUE_ANIMATION_DURATION = 10;

    private static final float EATING_DISTANCE = 1.75F;

    private static final float EATING_MOVEMENT_FACTOR = 0.75F;

    public static final int UNREACHABLE_TONGUE_TARGETS_COOLDOWN_DURATION = 100;

    public static final int MAX_UNREACHBLE_TONGUE_TARGETS_IN_MEMORY = 5;

    private int eatAnimationTimer;

    private int calculatePathCounter;

    private final SoundEvent tongueSound;

    private final SoundEvent eatSound;

    private Vec3 itemSpawnPos;

    private ShootTongue.State state = ShootTongue.State.DONE;

    public ShootTongue(SoundEvent soundEvent0, SoundEvent soundEvent1) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 100);
        this.tongueSound = soundEvent0;
        this.eatSound = soundEvent1;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Frog frog1) {
        LivingEntity $$2 = (LivingEntity) frog1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        boolean $$3 = this.canPathfindToTarget(frog1, $$2);
        if (!$$3) {
            frog1.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            this.addUnreachableTargetToMemory(frog1, $$2);
        }
        return $$3 && frog1.m_20089_() != Pose.CROAKING && Frog.canEat($$2);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Frog frog1, long long2) {
        return frog1.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.state != ShootTongue.State.DONE && !frog1.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING);
    }

    protected void start(ServerLevel serverLevel0, Frog frog1, long long2) {
        LivingEntity $$3 = (LivingEntity) frog1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        BehaviorUtils.lookAtEntity(frog1, $$3);
        frog1.setTongueTarget($$3);
        frog1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget($$3.m_20182_(), 2.0F, 0));
        this.calculatePathCounter = 10;
        this.state = ShootTongue.State.MOVE_TO_TARGET;
    }

    protected void stop(ServerLevel serverLevel0, Frog frog1, long long2) {
        frog1.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        frog1.eraseTongueTarget();
        frog1.m_20124_(Pose.STANDING);
    }

    private void eatEntity(ServerLevel serverLevel0, Frog frog1) {
        serverLevel0.m_6269_(null, frog1, this.eatSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
        Optional<Entity> $$2 = frog1.getTongueTarget();
        if ($$2.isPresent()) {
            Entity $$3 = (Entity) $$2.get();
            if ($$3.isAlive()) {
                frog1.m_7327_($$3);
                if (!$$3.isAlive()) {
                    $$3.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    protected void tick(ServerLevel serverLevel0, Frog frog1, long long2) {
        LivingEntity $$3 = (LivingEntity) frog1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        frog1.setTongueTarget($$3);
        switch(this.state) {
            case MOVE_TO_TARGET:
                if ($$3.m_20270_(frog1) < 1.75F) {
                    serverLevel0.m_6269_(null, frog1, this.tongueSound, SoundSource.NEUTRAL, 2.0F, 1.0F);
                    frog1.m_20124_(Pose.USING_TONGUE);
                    $$3.m_20256_($$3.m_20182_().vectorTo(frog1.m_20182_()).normalize().scale(0.75));
                    this.itemSpawnPos = $$3.m_20182_();
                    this.eatAnimationTimer = 0;
                    this.state = ShootTongue.State.CATCH_ANIMATION;
                } else if (this.calculatePathCounter <= 0) {
                    frog1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget($$3.m_20182_(), 2.0F, 0));
                    this.calculatePathCounter = 10;
                } else {
                    this.calculatePathCounter--;
                }
                break;
            case CATCH_ANIMATION:
                if (this.eatAnimationTimer++ >= 6) {
                    this.state = ShootTongue.State.EAT_ANIMATION;
                    this.eatEntity(serverLevel0, frog1);
                }
                break;
            case EAT_ANIMATION:
                if (this.eatAnimationTimer >= 10) {
                    this.state = ShootTongue.State.DONE;
                } else {
                    this.eatAnimationTimer++;
                }
            case DONE:
        }
    }

    private boolean canPathfindToTarget(Frog frog0, LivingEntity livingEntity1) {
        Path $$2 = frog0.m_21573_().createPath(livingEntity1, 0);
        return $$2 != null && $$2.getDistToTarget() < 1.75F;
    }

    private void addUnreachableTargetToMemory(Frog frog0, LivingEntity livingEntity1) {
        List<UUID> $$2 = (List<UUID>) frog0.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(ArrayList::new);
        boolean $$3 = !$$2.contains(livingEntity1.m_20148_());
        if ($$2.size() == 5 && $$3) {
            $$2.remove(0);
        }
        if ($$3) {
            $$2.add(livingEntity1.m_20148_());
        }
        frog0.getBrain().setMemoryWithExpiry(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS, $$2, 100L);
    }

    static enum State {

        MOVE_TO_TARGET, CATCH_ANIMATION, EAT_ANIMATION, DONE
    }
}