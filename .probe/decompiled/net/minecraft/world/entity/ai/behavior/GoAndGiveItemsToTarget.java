package net.minecraft.world.entity.ai.behavior;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.allay.AllayAi;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GoAndGiveItemsToTarget<E extends LivingEntity & InventoryCarrier> extends Behavior<E> {

    private static final int CLOSE_ENOUGH_DISTANCE_TO_TARGET = 3;

    private static final int ITEM_PICKUP_COOLDOWN_AFTER_THROWING = 60;

    private final Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter;

    private final float speedModifier;

    public GoAndGiveItemsToTarget(Function<LivingEntity, Optional<PositionTracker>> functionLivingEntityOptionalPositionTracker0, float float1, int int2) {
        super(Map.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.REGISTERED), int2);
        this.targetPositionGetter = functionLivingEntityOptionalPositionTracker0;
        this.speedModifier = float1;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, E e1) {
        return this.canThrowItemToTarget(e1);
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return this.canThrowItemToTarget(e1);
    }

    @Override
    protected void start(ServerLevel serverLevel0, E e1, long long2) {
        ((Optional) this.targetPositionGetter.apply(e1)).ifPresent(p_217206_ -> BehaviorUtils.setWalkAndLookTargetMemories(e1, p_217206_, this.speedModifier, 3));
    }

    @Override
    protected void tick(ServerLevel serverLevel0, E e1, long long2) {
        Optional<PositionTracker> $$3 = (Optional<PositionTracker>) this.targetPositionGetter.apply(e1);
        if (!$$3.isEmpty()) {
            PositionTracker $$4 = (PositionTracker) $$3.get();
            double $$5 = $$4.currentPosition().distanceTo(e1.m_146892_());
            if ($$5 < 3.0) {
                ItemStack $$6 = e1.getInventory().removeItem(0, 1);
                if (!$$6.isEmpty()) {
                    throwItem(e1, $$6, getThrowPosition($$4));
                    if (e1 instanceof Allay $$7) {
                        AllayAi.getLikedPlayer($$7).ifPresent(p_217224_ -> this.triggerDropItemOnBlock($$4, $$6, p_217224_));
                    }
                    e1.getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, 60);
                }
            }
        }
    }

    private void triggerDropItemOnBlock(PositionTracker positionTracker0, ItemStack itemStack1, ServerPlayer serverPlayer2) {
        BlockPos $$3 = positionTracker0.currentBlockPosition().below();
        CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.trigger(serverPlayer2, $$3, itemStack1);
    }

    private boolean canThrowItemToTarget(E e0) {
        if (e0.getInventory().isEmpty()) {
            return false;
        } else {
            Optional<PositionTracker> $$1 = (Optional<PositionTracker>) this.targetPositionGetter.apply(e0);
            return $$1.isPresent();
        }
    }

    private static Vec3 getThrowPosition(PositionTracker positionTracker0) {
        return positionTracker0.currentPosition().add(0.0, 1.0, 0.0);
    }

    public static void throwItem(LivingEntity livingEntity0, ItemStack itemStack1, Vec3 vec2) {
        Vec3 $$3 = new Vec3(0.2F, 0.3F, 0.2F);
        BehaviorUtils.throwItem(livingEntity0, itemStack1, vec2, $$3, 0.2F);
        Level $$4 = livingEntity0.m_9236_();
        if ($$4.getGameTime() % 7L == 0L && $$4.random.nextDouble() < 0.9) {
            float $$5 = Util.<Float>getRandom(Allay.THROW_SOUND_PITCHES, $$4.getRandom());
            $$4.playSound(null, livingEntity0, SoundEvents.ALLAY_THROW, SoundSource.NEUTRAL, 1.0F, $$5);
        }
    }
}