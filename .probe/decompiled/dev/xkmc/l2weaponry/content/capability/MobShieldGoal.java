package dev.xkmc.l2weaponry.content.capability;

import dev.xkmc.l2library.init.events.GeneralEventHandler;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.item.ItemStack;

public class MobShieldGoal extends Goal implements IShieldData {

    private final Mob mob;

    private double shieldDefense;

    public static MobShieldGoal getShieldGoal(Mob mob) {
        Optional<WrappedGoal> opt = mob.goalSelector.getRunningGoals().filter(e -> e.getGoal() instanceof MobShieldGoal).findFirst();
        if (opt.isPresent()) {
            return (MobShieldGoal) ((WrappedGoal) opt.get()).getGoal();
        } else {
            MobShieldGoal ans = new MobShieldGoal(mob);
            GeneralEventHandler.schedule(() -> mob.goalSelector.addGoal(0, ans));
            return ans;
        }
    }

    private MobShieldGoal(Mob mob) {
        this.mob = mob;
    }

    public boolean onBlock(ItemStack stack, BaseShieldItem item, boolean shouldDisable, LivingEntity target) {
        double strength = item.reflectImpl(stack, this.mob.m_9236_().damageSources().mobAttack(this.mob), this.mob.m_21133_(Attributes.ATTACK_DAMAGE), this, target);
        target.knockback(strength, this.mob.m_20185_() - target.m_20185_(), this.mob.m_20189_() - target.m_20189_());
        if (!shouldDisable && !item.lightWeight(stack)) {
            return false;
        } else {
            int cd = item.damageShieldImpl(this.mob, this, stack, shouldDisable ? 1.0 : -1.0);
            return cd > 0;
        }
    }

    public void onShieldDamage(ItemStack stack, BaseShieldItem item, double damage) {
        stack.getOrCreateTag().putInt("last_damage", (int) damage);
    }

    @Override
    public boolean canUse() {
        return this.shieldDefense > 0.0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.shieldDefense = Math.max(0.0, this.shieldDefense - 0.01);
    }

    @Override
    public double getShieldDefense() {
        return this.shieldDefense;
    }

    @Override
    public void setShieldDefense(double i) {
        this.shieldDefense = i;
    }

    @Override
    public boolean canReflect() {
        return this.mob.m_21051_((Attribute) LWItems.REFLECT_TIME.get()) != null && this.mob.m_21133_((Attribute) LWItems.REFLECT_TIME.get()) > 0.0;
    }

    @Override
    public double popRetain() {
        return 0.0;
    }

    @Override
    public int getReflectTimer() {
        return this.shieldDefense == 0.0 && this.canReflect() ? 1 : 0;
    }
}