package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CrossbowAttack<E extends Mob & CrossbowAttackMob, T extends LivingEntity> extends Behavior<E> {

    private static final int TIMEOUT = 1200;

    private int attackDelay;

    private CrossbowAttack.CrossbowState crossbowState = CrossbowAttack.CrossbowState.UNCHARGED;

    public CrossbowAttack() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 1200);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, E e1) {
        LivingEntity $$2 = getAttackTarget(e1);
        return e1.m_21055_(Items.CROSSBOW) && BehaviorUtils.canSee(e1, $$2) && BehaviorUtils.isWithinAttackRange(e1, $$2, 0);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return e1.m_6274_().hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && this.checkExtraStartConditions(serverLevel0, e1);
    }

    protected void tick(ServerLevel serverLevel0, E e1, long long2) {
        LivingEntity $$3 = getAttackTarget(e1);
        this.lookAtTarget(e1, $$3);
        this.crossbowAttack(e1, $$3);
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
        if (e1.m_6117_()) {
            e1.m_5810_();
        }
        if (e1.m_21055_(Items.CROSSBOW)) {
            e1.setChargingCrossbow(false);
            CrossbowItem.setCharged(e1.m_21211_(), false);
        }
    }

    private void crossbowAttack(E e0, LivingEntity livingEntity1) {
        if (this.crossbowState == CrossbowAttack.CrossbowState.UNCHARGED) {
            e0.m_6672_(ProjectileUtil.getWeaponHoldingHand(e0, Items.CROSSBOW));
            this.crossbowState = CrossbowAttack.CrossbowState.CHARGING;
            e0.setChargingCrossbow(true);
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.CHARGING) {
            if (!e0.m_6117_()) {
                this.crossbowState = CrossbowAttack.CrossbowState.UNCHARGED;
            }
            int $$2 = e0.m_21252_();
            ItemStack $$3 = e0.m_21211_();
            if ($$2 >= CrossbowItem.getChargeDuration($$3)) {
                e0.m_21253_();
                this.crossbowState = CrossbowAttack.CrossbowState.CHARGED;
                this.attackDelay = 20 + e0.m_217043_().nextInt(20);
                e0.setChargingCrossbow(false);
            }
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.CHARGED) {
            this.attackDelay--;
            if (this.attackDelay == 0) {
                this.crossbowState = CrossbowAttack.CrossbowState.READY_TO_ATTACK;
            }
        } else if (this.crossbowState == CrossbowAttack.CrossbowState.READY_TO_ATTACK) {
            e0.performRangedAttack(livingEntity1, 1.0F);
            ItemStack $$4 = e0.m_21120_(ProjectileUtil.getWeaponHoldingHand(e0, Items.CROSSBOW));
            CrossbowItem.setCharged($$4, false);
            this.crossbowState = CrossbowAttack.CrossbowState.UNCHARGED;
        }
    }

    private void lookAtTarget(Mob mob0, LivingEntity livingEntity1) {
        mob0.m_6274_().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(livingEntity1, true));
    }

    private static LivingEntity getAttackTarget(LivingEntity livingEntity0) {
        return (LivingEntity) livingEntity0.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    static enum CrossbowState {

        UNCHARGED, CHARGING, CHARGED, READY_TO_ATTACK
    }
}