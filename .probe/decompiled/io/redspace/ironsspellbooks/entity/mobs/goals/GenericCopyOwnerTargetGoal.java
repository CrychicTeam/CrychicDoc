package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class GenericCopyOwnerTargetGoal extends TargetGoal {

    private final OwnerGetter ownerGetter;

    public GenericCopyOwnerTargetGoal(PathfinderMob pMob, OwnerGetter ownerGetter) {
        super(pMob, false);
        this.ownerGetter = ownerGetter;
    }

    @Override
    public boolean canUse() {
        if (this.ownerGetter.get() instanceof Mob owner && owner.getTarget() != null && (!(owner.getTarget() instanceof MagicSummon summon) || summon.getSummoner() != owner)) {
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        LivingEntity target = ((Mob) this.ownerGetter.get()).getTarget();
        this.f_26135_.setTarget(target);
        this.f_26135_.m_6274_().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);
        super.start();
    }
}