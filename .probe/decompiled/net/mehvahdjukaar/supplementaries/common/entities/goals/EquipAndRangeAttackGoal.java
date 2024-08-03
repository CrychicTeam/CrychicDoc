package net.mehvahdjukaar.supplementaries.common.entities.goals;

import java.util.EnumSet;
import net.mehvahdjukaar.supplementaries.common.entities.RedMerchantEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class EquipAndRangeAttackGoal extends Goal {

    private final ItemStack item;

    private final RedMerchantEntity mob;

    private LivingEntity target;

    private int attackTime = -1;

    private final double speedModifier;

    private int seeTime;

    private final int cooldown;

    private final int attackIntervalMin;

    private final int attackIntervalMax;

    private final float attackRadius;

    private final float attackRadiusSqr;

    public EquipAndRangeAttackGoal(RedMerchantEntity mob, double speed, int cooldown, int minInt, int maxInt, float range, ItemStack item) {
        this.mob = mob;
        this.cooldown = cooldown;
        this.speedModifier = speed;
        this.attackIntervalMin = minInt;
        this.attackIntervalMax = maxInt;
        this.attackRadius = range;
        this.attackRadiusSqr = range * range;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.item = item;
    }

    @Override
    public boolean canUse() {
        if (this.mob.getAttackCooldown() > 0) {
            return false;
        } else {
            LivingEntity livingentity = this.mob.m_5448_();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.mob.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    @Override
    public void start() {
        this.mob.m_8061_(EquipmentSlot.MAINHAND, this.item.copy());
        super.start();
    }

    @Override
    public void tick() {
        double d0 = this.mob.m_20275_(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_());
        boolean flag = this.mob.m_21574_().hasLineOfSight(this.target);
        if (flag) {
            this.seeTime++;
        } else {
            this.seeTime = 0;
        }
        if (d0 <= (double) this.attackRadiusSqr && this.seeTime >= 5) {
            this.mob.m_21573_().stop();
        } else {
            this.mob.m_21573_().moveTo(this.target, this.speedModifier);
        }
        this.mob.m_21563_().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }
            float f = Mth.sqrt((float) d0) / this.attackRadius;
            float lvt_5_1_ = Mth.clamp(f, 0.1F, 1.0F);
            this.mob.performRangedAttack(this.target, lvt_5_1_);
            this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
            this.mob.setAttackCooldown(this.cooldown + this.mob.m_217043_().nextInt(20));
        } else if (this.attackTime < 0) {
            float f2 = Mth.sqrt((float) d0) / this.attackRadius;
            this.attackTime = Mth.floor(f2 * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
        }
    }
}