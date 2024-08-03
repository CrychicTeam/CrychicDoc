package net.minecraft.world.entity.animal.axolotl;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class PlayDead extends Behavior<Axolotl> {

    public PlayDead() {
        super(ImmutableMap.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT), 200);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Axolotl axolotl1) {
        return axolotl1.m_20072_();
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Axolotl axolotl1, long long2) {
        return axolotl1.m_20072_() && axolotl1.getBrain().hasMemoryValue(MemoryModuleType.PLAY_DEAD_TICKS);
    }

    protected void start(ServerLevel serverLevel0, Axolotl axolotl1, long long2) {
        Brain<Axolotl> $$3 = axolotl1.getBrain();
        $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
        $$3.eraseMemory(MemoryModuleType.LOOK_TARGET);
        axolotl1.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
    }
}