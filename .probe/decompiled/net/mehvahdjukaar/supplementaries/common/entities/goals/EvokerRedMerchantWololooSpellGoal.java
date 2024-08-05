package net.mehvahdjukaar.supplementaries.common.entities.goals;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.entities.RedMerchantEntity;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.npc.WanderingTrader;

public class EvokerRedMerchantWololooSpellGoal extends Goal {

    private final TargetingConditions selector = TargetingConditions.forNonCombat().range(16.0);

    private final Evoker evoker;

    private final ISuppEvoker suppEvoker;

    protected int attackWarmupDelay;

    protected int nextAttackTickCount;

    public EvokerRedMerchantWololooSpellGoal(Evoker evoker) {
        this.evoker = evoker;
        this.suppEvoker = (ISuppEvoker) evoker;
    }

    @Override
    public boolean canUse() {
        if (this.evoker.m_5448_() != null) {
            return false;
        } else if (this.evoker.m_33736_()) {
            return false;
        } else if (this.evoker.f_19797_ < this.nextAttackTickCount) {
            return false;
        } else if (!PlatHelper.isMobGriefingOn(this.evoker.m_9236_(), this.evoker)) {
            return false;
        } else {
            List<WanderingTrader> list = this.evoker.m_9236_().m_45971_(WanderingTrader.class, this.selector, this.evoker, this.evoker.m_20191_().inflate(16.0, 4.0, 16.0));
            if (list.isEmpty()) {
                return false;
            } else {
                ((ISuppEvoker) this.evoker).supplementaries$setCustomWololoo((LivingEntity) list.get(this.evoker.m_217043_().nextInt(list.size())));
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.suppEvoker.supplementaries$getCustomWololoo() != null && this.attackWarmupDelay > 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.suppEvoker.supplementaries$setCustomWololoo(null);
    }

    protected void performSpellCasting() {
        LivingEntity entity = this.suppEvoker.supplementaries$getCustomWololoo();
        if (entity != null && entity.isAlive() && ForgeHelper.canLivingConvert(entity, (EntityType<? extends LivingEntity>) ModEntities.RED_MERCHANT.get(), timer -> {
        }) && !entity.m_213877_()) {
            RedMerchantEntity mob = (RedMerchantEntity) ((EntityType) ModEntities.RED_MERCHANT.get()).create(entity.m_9236_());
            if (mob != null) {
                CompoundTag tag = new CompoundTag();
                entity.m_20240_(tag);
                tag.remove("Offers");
                mob.m_20258_(tag);
                entity.m_146870_();
                mob.m_21195_(MobEffects.INVISIBILITY);
                entity.m_9236_().m_7967_(mob);
            }
        }
    }

    protected int getCastWarmupTime() {
        return 40;
    }

    protected int getCastingTime() {
        return 60;
    }

    protected int getCastingInterval() {
        return 140;
    }

    protected SoundEvent getSpellPrepareSound() {
        return SoundEvents.EVOKER_PREPARE_WOLOLO;
    }

    protected SpellcasterIllager.IllagerSpell getSpell() {
        return SpellcasterIllager.IllagerSpell.WOLOLO;
    }

    @Override
    public void start() {
        this.attackWarmupDelay = this.m_183277_(this.getCastWarmupTime());
        this.suppEvoker.supplementaries$setSpellCastingTime(this.getCastingTime());
        this.nextAttackTickCount = this.evoker.f_19797_ + this.getCastingInterval();
        SoundEvent soundEvent = this.getSpellPrepareSound();
        if (soundEvent != null) {
            this.evoker.m_5496_(soundEvent, 1.0F, 1.0F);
        }
        this.evoker.m_33727_(this.getSpell());
    }

    @Override
    public void tick() {
        this.attackWarmupDelay--;
        if (this.attackWarmupDelay == 0) {
            this.performSpellCasting();
            this.evoker.m_5496_(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
        }
    }
}