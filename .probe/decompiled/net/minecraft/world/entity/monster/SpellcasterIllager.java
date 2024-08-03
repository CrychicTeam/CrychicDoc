package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public abstract class SpellcasterIllager extends AbstractIllager {

    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID = SynchedEntityData.defineId(SpellcasterIllager.class, EntityDataSerializers.BYTE);

    protected int spellCastingTickCount;

    private SpellcasterIllager.IllagerSpell currentSpell = SpellcasterIllager.IllagerSpell.NONE;

    protected SpellcasterIllager(EntityType<? extends SpellcasterIllager> entityTypeExtendsSpellcasterIllager0, Level level1) {
        super(entityTypeExtendsSpellcasterIllager0, level1);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_SPELL_CASTING_ID, (byte) 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.spellCastingTickCount = compoundTag0.getInt("SpellTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putInt("SpellTicks", this.spellCastingTickCount);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return AbstractIllager.IllagerArmPose.SPELLCASTING;
        } else {
            return this.m_37888_() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    public boolean isCastingSpell() {
        return this.m_9236_().isClientSide ? this.f_19804_.get(DATA_SPELL_CASTING_ID) > 0 : this.spellCastingTickCount > 0;
    }

    public void setIsCastingSpell(SpellcasterIllager.IllagerSpell spellcasterIllagerIllagerSpell0) {
        this.currentSpell = spellcasterIllagerIllagerSpell0;
        this.f_19804_.set(DATA_SPELL_CASTING_ID, (byte) spellcasterIllagerIllagerSpell0.id);
    }

    protected SpellcasterIllager.IllagerSpell getCurrentSpell() {
        return !this.m_9236_().isClientSide ? this.currentSpell : SpellcasterIllager.IllagerSpell.byId(this.f_19804_.get(DATA_SPELL_CASTING_ID));
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.spellCastingTickCount > 0) {
            this.spellCastingTickCount--;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_9236_().isClientSide && this.isCastingSpell()) {
            SpellcasterIllager.IllagerSpell $$0 = this.getCurrentSpell();
            double $$1 = $$0.spellColor[0];
            double $$2 = $$0.spellColor[1];
            double $$3 = $$0.spellColor[2];
            float $$4 = this.f_20883_ * (float) (Math.PI / 180.0) + Mth.cos((float) this.f_19797_ * 0.6662F) * 0.25F;
            float $$5 = Mth.cos($$4);
            float $$6 = Mth.sin($$4);
            this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + (double) $$5 * 0.6, this.m_20186_() + 1.8, this.m_20189_() + (double) $$6 * 0.6, $$1, $$2, $$3);
            this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() - (double) $$5 * 0.6, this.m_20186_() + 1.8, this.m_20189_() - (double) $$6 * 0.6, $$1, $$2, $$3);
        }
    }

    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }

    protected abstract SoundEvent getCastingSoundEvent();

    protected static enum IllagerSpell {

        NONE(0, 0.0, 0.0, 0.0),
        SUMMON_VEX(1, 0.7, 0.7, 0.8),
        FANGS(2, 0.4, 0.3, 0.35),
        WOLOLO(3, 0.7, 0.5, 0.2),
        DISAPPEAR(4, 0.3, 0.3, 0.8),
        BLINDNESS(5, 0.1, 0.1, 0.2);

        private static final IntFunction<SpellcasterIllager.IllagerSpell> BY_ID = ByIdMap.continuous(p_263091_ -> p_263091_.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        final int id;

        final double[] spellColor;

        private IllagerSpell(int p_33754_, double p_33755_, double p_33756_, double p_33757_) {
            this.id = p_33754_;
            this.spellColor = new double[] { p_33755_, p_33756_, p_33757_ };
        }

        public static SpellcasterIllager.IllagerSpell byId(int p_33759_) {
            return (SpellcasterIllager.IllagerSpell) BY_ID.apply(p_33759_);
        }
    }

    protected class SpellcasterCastingSpellGoal extends Goal {

        public SpellcasterCastingSpellGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return SpellcasterIllager.this.getSpellCastingTime() > 0;
        }

        @Override
        public void start() {
            super.start();
            SpellcasterIllager.this.f_21344_.stop();
        }

        @Override
        public void stop() {
            super.stop();
            SpellcasterIllager.this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
        }

        @Override
        public void tick() {
            if (SpellcasterIllager.this.m_5448_() != null) {
                SpellcasterIllager.this.m_21563_().setLookAt(SpellcasterIllager.this.m_5448_(), (float) SpellcasterIllager.this.m_8085_(), (float) SpellcasterIllager.this.m_8132_());
            }
        }
    }

    protected abstract class SpellcasterUseSpellGoal extends Goal {

        protected int attackWarmupDelay;

        protected int nextAttackTickCount;

        @Override
        public boolean canUse() {
            LivingEntity $$0 = SpellcasterIllager.this.m_5448_();
            if ($$0 == null || !$$0.isAlive()) {
                return false;
            } else {
                return SpellcasterIllager.this.isCastingSpell() ? false : SpellcasterIllager.this.f_19797_ >= this.nextAttackTickCount;
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity $$0 = SpellcasterIllager.this.m_5448_();
            return $$0 != null && $$0.isAlive() && this.attackWarmupDelay > 0;
        }

        @Override
        public void start() {
            this.attackWarmupDelay = this.m_183277_(this.getCastWarmupTime());
            SpellcasterIllager.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = SpellcasterIllager.this.f_19797_ + this.getCastingInterval();
            SoundEvent $$0 = this.getSpellPrepareSound();
            if ($$0 != null) {
                SpellcasterIllager.this.m_5496_($$0, 1.0F, 1.0F);
            }
            SpellcasterIllager.this.setIsCastingSpell(this.getSpell());
        }

        @Override
        public void tick() {
            this.attackWarmupDelay--;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                SpellcasterIllager.this.m_5496_(SpellcasterIllager.this.getCastingSoundEvent(), 1.0F, 1.0F);
            }
        }

        protected abstract void performSpellCasting();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract SpellcasterIllager.IllagerSpell getSpell();
    }
}