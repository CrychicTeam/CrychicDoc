package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructEntityAreaTask;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.entities.constructs.animated.Construct;
import java.util.Collection;
import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructDuel extends ConstructEntityAreaTask<Construct, ConstructDuel> {

    private int targetRate = 20;

    private long targetCounter = 0L;

    private boolean lastTickTargetDefeated = false;

    public ConstructDuel(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, Construct.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.getConstruct().getConstructData().getBanner().isEmpty()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.duel.no_banner", new Object[0]), false);
            this.forceFail();
            return false;
        } else {
            return super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.canUse();
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        LivingEntity target = this.getConstructAsEntity().m_5448_();
        if (target != null && (!(target instanceof Construct) || ((Construct) target).isDefeated() || !target.isAlive())) {
            if (!(c.m_5448_() instanceof Construct)) {
                c.m_6710_(null);
                return;
            }
            Construct targetConstruct = (Construct) c.m_5448_();
            if (targetConstruct.isDefeated()) {
                c.m_6710_(null);
                this.pushDuelDiagnosticWithTarget("mna.constructs.feedback.duel.takedown", c.m_5448_());
                this.getConstruct().setHappy(300);
                this.lastTickTargetDefeated = true;
                return;
            }
            if (!targetConstruct.m_6084_() || !targetConstruct.isDueling()) {
                c.m_6710_(null);
                return;
            }
            if (this.construct.isDefeated()) {
                this.forceFail();
            } else {
                this.doAttack();
            }
        } else if (this.construct.getConstructData().isAnyCapabilityEnabled(ConstructCapability.MELEE_ATTACK, ConstructCapability.RANGED_ATTACK, ConstructCapability.FLUID_DISPENSE, ConstructCapability.CAST_SPELL)) {
            this.targetCounter++;
            if (this.targetCounter >= (long) this.targetRate) {
                this.locateTarget();
                if (this.lastTickTargetDefeated && !this.construct.isDefeated()) {
                    this.exitCode = 0;
                    return;
                }
            }
        }
        this.lastTickTargetDefeated = false;
    }

    @Override
    public void start() {
        super.m_8056_();
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.duel.start", new Object[0]), false);
    }

    protected boolean entityPredicate(Construct candidate) {
        ItemStack myBanner = this.construct.getConstructData().getBanner();
        if (candidate.isDefeated() || !candidate.m_6084_()) {
            return false;
        } else if (!candidate.isDueling()) {
            return false;
        } else {
            ItemStack theirBanner = candidate.getConstructData().getBanner();
            return !ItemStack.isSameItemSameTags(theirBanner, myBanner);
        }
    }

    protected Construct selectTarget(Collection<Construct> entities) {
        AbstractGolem c = this.getConstructAsEntity();
        Construct target = (Construct) entities.iterator().next();
        c.m_6710_(target);
        this.setMoveTarget(c.m_5448_());
        this.doMove();
        this.pushDuelDiagnosticWithTarget("mna.constructs.feedback.duel.target_acquire", c.m_5448_());
        return target;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.DUEL);
    }

    public ConstructDuel duplicate() {
        return new ConstructDuel(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void onTaskSet() {
        this.lastTickTargetDefeated = false;
        super.onTaskSet();
    }

    @Override
    protected String getAreaIdentifier() {
        return "duel.area";
    }

    private void pushDuelDiagnosticWithTarget(String key, LivingEntity target) {
        if (target != null) {
            Component opponentName = target.m_7770_();
            if (opponentName == null) {
                opponentName = Component.translatable("mna.constructs.feedback.target.no_name");
            }
            this.pushDiagnosticMessage(this.translate(key, new Object[] { opponentName }), false);
        }
    }
}