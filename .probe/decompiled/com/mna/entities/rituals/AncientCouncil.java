package com.mna.entities.rituals;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.LivingUtilityEntity;
import com.mna.factions.Factions;
import com.mna.tools.math.MathUtils;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class AncientCouncil extends LivingUtilityEntity {

    public static final byte STATE_WAITING_PLAYER = 0;

    public static final byte STATE_IMBUING = 1;

    private float radiant;

    private float radiantLift;

    private float beam;

    private float centerRadiant;

    public AncientCouncil(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
        this.stateMachine.addSequenceEntry("imbue", 0).onComplete(() -> {
            this.setCurrentAnimation("imbue_stage_1");
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.ANCIENT_IMBUE_1, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            }
        });
        this.stateMachine.addSequenceEntry("imbue", 220).onComplete(() -> {
            this.setCurrentAnimation("imbue_stage_2");
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.ANCIENT_IMBUE_2, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            } else if (this.caster != null) {
                this.caster.m_7292_(new MobEffectInstance(EffectInit.LIFT.get(), 180, 1));
            }
        }).onTick(c -> {
            this.animationPct = (float) c.intValue() / 220.0F;
            this.radiant = MathUtils.clamp01((float) (c - 120) / 100.0F);
        });
        this.stateMachine.addSequenceEntry("imbue", 165).onComplete(() -> {
            this.setCurrentAnimation("imbue_stage_3");
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.ANCIENT_IMBUE_3, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            } else if (this.caster != null) {
                this.caster.m_7292_(new MobEffectInstance(EffectInit.LIFT.get(), 280, 2));
            }
        }).onTick(c -> {
            this.animationPct = (float) c.intValue() / 165.0F;
            this.radiantLift = this.animationPct;
        });
        this.stateMachine.addSequenceEntry("imbue", 260).onComplete(() -> {
            this.setCurrentAnimation("");
            if (this.caster != null && this.caster.m_20270_(this) < 10.0F) {
                if (this.m_9236_().isClientSide()) {
                    for (int i = 0; i < 75; i++) {
                        this.m_9236_().addParticle(new MAParticleType(ParticleInit.ARCANE.get()), this.caster.m_20185_(), this.caster.m_20186_() + 1.0, this.caster.m_20189_(), -0.25 + Math.random() * 0.5, 0.15F, -0.25 + Math.random() * 0.5);
                    }
                } else {
                    IPlayerProgression progression = (IPlayerProgression) this.caster.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (progression != null && progression.getTier() < 5) {
                        if (progression.getAlliedFaction() == null) {
                            progression.setAlliedFaction(Factions.COUNCIL, this.caster);
                            this.caster.m_213846_(Component.translatable("event.mna.faction_ally_ancients"));
                        }
                        if (progression.getAlliedFaction() == Factions.COUNCIL) {
                            progression.setTier(progression.getTier() + 1, this.caster);
                            this.caster.m_213846_(Component.translatable("mna:progresscondition.advanced", progression.getTier()));
                        }
                    }
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            this.centerRadiant = 0.0F;
            this.beam = 0.0F;
            this.radiant = 0.0F;
        }).onTick(c -> {
            this.animationPct = (float) c.intValue() / 260.0F;
            this.centerRadiant = MathUtils.clamp01(((float) c.intValue() - 30.0F) / 180.0F);
            this.beam = MathUtils.clamp01((float) c.intValue() / 40.0F);
        });
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ >= 100) {
            if (this.caster == null) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            byte state = this.getState();
            if (state == 0) {
                List<Entity> centerCollisions = this.m_9236_().getEntities(this, this.m_20191_(), e -> true);
                if (centerCollisions.contains(this.caster)) {
                    this.caster.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    if (!this.m_9236_().isClientSide()) {
                        this.caster.m_7292_(new MobEffectInstance(EffectInit.LIFT.get(), 240));
                    }
                    if (!this.m_9236_().isClientSide()) {
                        this.setState((byte) 1);
                    }
                    this.stateMachine.runSequence("imbue");
                }
            } else {
                this.stateMachine.tick();
            }
            if (!this.m_9236_().isClientSide() && (state == 0 && this.f_19797_ > 700 || this.caster == null || this.caster.m_20270_(this) > 10.0F)) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key == STATE && this.getState() == 1) {
            this.stateMachine.runSequence("imbue");
        }
        super.m_7350_(key);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setState((byte) 0);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.m_9236_().isClientSide()) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Event.Ritual.ANCIENT_SUMMON, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide()) {
            int numWizards = 6;
            float angleRadians = 0.0F;
            for (int a = 0; a < numWizards; a++) {
                for (int i = 0; i < 125; i++) {
                    double radius = 2.75 + Math.random() * 0.5;
                    double x = this.m_20185_() - 0.5 + Math.random() + radius * Math.cos((double) angleRadians);
                    double z = this.m_20189_() - 0.5 + Math.random() + radius * Math.sin((double) angleRadians);
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.ARCANE.get()), x, this.m_20186_() + Math.random() * 0.5, z, 0.0, 0.02 + Math.random() * 0.2F, 0.0);
                }
                angleRadians = (float) ((double) angleRadians + (double) (360.0F / (float) numWizards / 180.0F) * Math.PI);
            }
        }
        this.disableFlightFor(this.caster);
    }

    public float getRadiantPct() {
        return this.radiant;
    }

    public float getBeamPct() {
        return this.beam;
    }

    public float getCenterPct() {
        return this.centerRadiant;
    }

    public float getRadiantLift() {
        return this.radiantLift;
    }
}