package net.minecraft.world.effect;

import com.google.common.collect.ComparisonChain;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;

public class MobEffectInstance implements Comparable<MobEffectInstance> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int INFINITE_DURATION = -1;

    private final MobEffect effect;

    private int duration;

    private int amplifier;

    private boolean ambient;

    private boolean visible;

    private boolean showIcon;

    @Nullable
    private MobEffectInstance hiddenEffect;

    private final Optional<MobEffectInstance.FactorData> factorData;

    public MobEffectInstance(MobEffect mobEffect0) {
        this(mobEffect0, 0, 0);
    }

    public MobEffectInstance(MobEffect mobEffect0, int int1) {
        this(mobEffect0, int1, 0);
    }

    public MobEffectInstance(MobEffect mobEffect0, int int1, int int2) {
        this(mobEffect0, int1, int2, false, true);
    }

    public MobEffectInstance(MobEffect mobEffect0, int int1, int int2, boolean boolean3, boolean boolean4) {
        this(mobEffect0, int1, int2, boolean3, boolean4, boolean4);
    }

    public MobEffectInstance(MobEffect mobEffect0, int int1, int int2, boolean boolean3, boolean boolean4, boolean boolean5) {
        this(mobEffect0, int1, int2, boolean3, boolean4, boolean5, null, mobEffect0.createFactorData());
    }

    public MobEffectInstance(MobEffect mobEffect0, int int1, int int2, boolean boolean3, boolean boolean4, boolean boolean5, @Nullable MobEffectInstance mobEffectInstance6, Optional<MobEffectInstance.FactorData> optionalMobEffectInstanceFactorData7) {
        this.effect = mobEffect0;
        this.duration = int1;
        this.amplifier = int2;
        this.ambient = boolean3;
        this.visible = boolean4;
        this.showIcon = boolean5;
        this.hiddenEffect = mobEffectInstance6;
        this.factorData = optionalMobEffectInstanceFactorData7;
    }

    public MobEffectInstance(MobEffectInstance mobEffectInstance0) {
        this.effect = mobEffectInstance0.effect;
        this.factorData = this.effect.createFactorData();
        this.setDetailsFrom(mobEffectInstance0);
    }

    public Optional<MobEffectInstance.FactorData> getFactorData() {
        return this.factorData;
    }

    void setDetailsFrom(MobEffectInstance mobEffectInstance0) {
        this.duration = mobEffectInstance0.duration;
        this.amplifier = mobEffectInstance0.amplifier;
        this.ambient = mobEffectInstance0.ambient;
        this.visible = mobEffectInstance0.visible;
        this.showIcon = mobEffectInstance0.showIcon;
    }

    public boolean update(MobEffectInstance mobEffectInstance0) {
        if (this.effect != mobEffectInstance0.effect) {
            LOGGER.warn("This method should only be called for matching effects!");
        }
        int $$1 = this.duration;
        boolean $$2 = false;
        if (mobEffectInstance0.amplifier > this.amplifier) {
            if (mobEffectInstance0.isShorterDurationThan(this)) {
                MobEffectInstance $$3 = this.hiddenEffect;
                this.hiddenEffect = new MobEffectInstance(this);
                this.hiddenEffect.hiddenEffect = $$3;
            }
            this.amplifier = mobEffectInstance0.amplifier;
            this.duration = mobEffectInstance0.duration;
            $$2 = true;
        } else if (this.isShorterDurationThan(mobEffectInstance0)) {
            if (mobEffectInstance0.amplifier == this.amplifier) {
                this.duration = mobEffectInstance0.duration;
                $$2 = true;
            } else if (this.hiddenEffect == null) {
                this.hiddenEffect = new MobEffectInstance(mobEffectInstance0);
            } else {
                this.hiddenEffect.update(mobEffectInstance0);
            }
        }
        if (!mobEffectInstance0.ambient && this.ambient || $$2) {
            this.ambient = mobEffectInstance0.ambient;
            $$2 = true;
        }
        if (mobEffectInstance0.visible != this.visible) {
            this.visible = mobEffectInstance0.visible;
            $$2 = true;
        }
        if (mobEffectInstance0.showIcon != this.showIcon) {
            this.showIcon = mobEffectInstance0.showIcon;
            $$2 = true;
        }
        return $$2;
    }

    private boolean isShorterDurationThan(MobEffectInstance mobEffectInstance0) {
        return !this.isInfiniteDuration() && (this.duration < mobEffectInstance0.duration || mobEffectInstance0.isInfiniteDuration());
    }

    public boolean isInfiniteDuration() {
        return this.duration == -1;
    }

    public boolean endsWithin(int int0) {
        return !this.isInfiniteDuration() && this.duration <= int0;
    }

    public int mapDuration(Int2IntFunction intIntFunction0) {
        return !this.isInfiniteDuration() && this.duration != 0 ? intIntFunction0.applyAsInt(this.duration) : this.duration;
    }

    public MobEffect getEffect() {
        return this.effect;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public boolean showIcon() {
        return this.showIcon;
    }

    public boolean tick(LivingEntity livingEntity0, Runnable runnable1) {
        if (this.hasRemainingDuration()) {
            int $$2 = this.isInfiniteDuration() ? livingEntity0.f_19797_ : this.duration;
            if (this.effect.isDurationEffectTick($$2, this.amplifier)) {
                this.applyEffect(livingEntity0);
            }
            this.tickDownDuration();
            if (this.duration == 0 && this.hiddenEffect != null) {
                this.setDetailsFrom(this.hiddenEffect);
                this.hiddenEffect = this.hiddenEffect.hiddenEffect;
                runnable1.run();
            }
        }
        this.factorData.ifPresent(p_267917_ -> p_267917_.tick(this));
        return this.hasRemainingDuration();
    }

    private boolean hasRemainingDuration() {
        return this.isInfiniteDuration() || this.duration > 0;
    }

    private int tickDownDuration() {
        if (this.hiddenEffect != null) {
            this.hiddenEffect.tickDownDuration();
        }
        return this.duration = this.mapDuration(p_267916_ -> p_267916_ - 1);
    }

    public void applyEffect(LivingEntity livingEntity0) {
        if (this.hasRemainingDuration()) {
            this.effect.applyEffectTick(livingEntity0, this.amplifier);
        }
    }

    public String getDescriptionId() {
        return this.effect.getDescriptionId();
    }

    public String toString() {
        String $$0;
        if (this.amplifier > 0) {
            $$0 = this.getDescriptionId() + " x " + (this.amplifier + 1) + ", Duration: " + this.describeDuration();
        } else {
            $$0 = this.getDescriptionId() + ", Duration: " + this.describeDuration();
        }
        if (!this.visible) {
            $$0 = $$0 + ", Particles: false";
        }
        if (!this.showIcon) {
            $$0 = $$0 + ", Show Icon: false";
        }
        return $$0;
    }

    private String describeDuration() {
        return this.isInfiniteDuration() ? "infinite" : Integer.toString(this.duration);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof MobEffectInstance $$1) ? false : this.duration == $$1.duration && this.amplifier == $$1.amplifier && this.ambient == $$1.ambient && this.effect.equals($$1.effect);
        }
    }

    public int hashCode() {
        int $$0 = this.effect.hashCode();
        $$0 = 31 * $$0 + this.duration;
        $$0 = 31 * $$0 + this.amplifier;
        return 31 * $$0 + (this.ambient ? 1 : 0);
    }

    public CompoundTag save(CompoundTag compoundTag0) {
        compoundTag0.putInt("Id", MobEffect.getId(this.getEffect()));
        this.writeDetailsTo(compoundTag0);
        return compoundTag0;
    }

    private void writeDetailsTo(CompoundTag compoundTag0) {
        compoundTag0.putByte("Amplifier", (byte) this.getAmplifier());
        compoundTag0.putInt("Duration", this.getDuration());
        compoundTag0.putBoolean("Ambient", this.isAmbient());
        compoundTag0.putBoolean("ShowParticles", this.isVisible());
        compoundTag0.putBoolean("ShowIcon", this.showIcon());
        if (this.hiddenEffect != null) {
            CompoundTag $$1 = new CompoundTag();
            this.hiddenEffect.save($$1);
            compoundTag0.put("HiddenEffect", $$1);
        }
        this.factorData.ifPresent(p_216903_ -> MobEffectInstance.FactorData.CODEC.encodeStart(NbtOps.INSTANCE, p_216903_).resultOrPartial(LOGGER::error).ifPresent(p_216906_ -> compoundTag0.put("FactorCalculationData", p_216906_)));
    }

    @Nullable
    public static MobEffectInstance load(CompoundTag compoundTag0) {
        int $$1 = compoundTag0.getInt("Id");
        MobEffect $$2 = MobEffect.byId($$1);
        return $$2 == null ? null : loadSpecifiedEffect($$2, compoundTag0);
    }

    private static MobEffectInstance loadSpecifiedEffect(MobEffect mobEffect0, CompoundTag compoundTag1) {
        int $$2 = compoundTag1.getByte("Amplifier");
        int $$3 = compoundTag1.getInt("Duration");
        boolean $$4 = compoundTag1.getBoolean("Ambient");
        boolean $$5 = true;
        if (compoundTag1.contains("ShowParticles", 1)) {
            $$5 = compoundTag1.getBoolean("ShowParticles");
        }
        boolean $$6 = $$5;
        if (compoundTag1.contains("ShowIcon", 1)) {
            $$6 = compoundTag1.getBoolean("ShowIcon");
        }
        MobEffectInstance $$7 = null;
        if (compoundTag1.contains("HiddenEffect", 10)) {
            $$7 = loadSpecifiedEffect(mobEffect0, compoundTag1.getCompound("HiddenEffect"));
        }
        Optional<MobEffectInstance.FactorData> $$8;
        if (compoundTag1.contains("FactorCalculationData", 10)) {
            $$8 = MobEffectInstance.FactorData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag1.getCompound("FactorCalculationData"))).resultOrPartial(LOGGER::error);
        } else {
            $$8 = Optional.empty();
        }
        return new MobEffectInstance(mobEffect0, $$3, Math.max($$2, 0), $$4, $$5, $$6, $$7, $$8);
    }

    public int compareTo(MobEffectInstance mobEffectInstance0) {
        int $$1 = 32147;
        return (this.getDuration() <= 32147 || mobEffectInstance0.getDuration() <= 32147) && (!this.isAmbient() || !mobEffectInstance0.isAmbient()) ? ComparisonChain.start().compareFalseFirst(this.isAmbient(), mobEffectInstance0.isAmbient()).compareFalseFirst(this.isInfiniteDuration(), mobEffectInstance0.isInfiniteDuration()).compare(this.getDuration(), mobEffectInstance0.getDuration()).compare(this.getEffect().getColor(), mobEffectInstance0.getEffect().getColor()).result() : ComparisonChain.start().compare(this.isAmbient(), mobEffectInstance0.isAmbient()).compare(this.getEffect().getColor(), mobEffectInstance0.getEffect().getColor()).result();
    }

    public static class FactorData {

        public static final Codec<MobEffectInstance.FactorData> CODEC = RecordCodecBuilder.create(p_216933_ -> p_216933_.group(ExtraCodecs.NON_NEGATIVE_INT.fieldOf("padding_duration").forGetter(p_216945_ -> p_216945_.paddingDuration), Codec.FLOAT.fieldOf("factor_start").orElse(0.0F).forGetter(p_216943_ -> p_216943_.factorStart), Codec.FLOAT.fieldOf("factor_target").orElse(1.0F).forGetter(p_216941_ -> p_216941_.factorTarget), Codec.FLOAT.fieldOf("factor_current").orElse(0.0F).forGetter(p_216939_ -> p_216939_.factorCurrent), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks_active").orElse(0).forGetter(p_267918_ -> p_267918_.ticksActive), Codec.FLOAT.fieldOf("factor_previous_frame").orElse(0.0F).forGetter(p_216935_ -> p_216935_.factorPreviousFrame), Codec.BOOL.fieldOf("had_effect_last_tick").orElse(false).forGetter(p_216929_ -> p_216929_.hadEffectLastTick)).apply(p_216933_, MobEffectInstance.FactorData::new));

        private final int paddingDuration;

        private float factorStart;

        private float factorTarget;

        private float factorCurrent;

        private int ticksActive;

        private float factorPreviousFrame;

        private boolean hadEffectLastTick;

        public FactorData(int int0, float float1, float float2, float float3, int int4, float float5, boolean boolean6) {
            this.paddingDuration = int0;
            this.factorStart = float1;
            this.factorTarget = float2;
            this.factorCurrent = float3;
            this.ticksActive = int4;
            this.factorPreviousFrame = float5;
            this.hadEffectLastTick = boolean6;
        }

        public FactorData(int int0) {
            this(int0, 0.0F, 1.0F, 0.0F, 0, 0.0F, false);
        }

        public void tick(MobEffectInstance mobEffectInstance0) {
            this.factorPreviousFrame = this.factorCurrent;
            boolean $$1 = !mobEffectInstance0.endsWithin(this.paddingDuration);
            this.ticksActive++;
            if (this.hadEffectLastTick != $$1) {
                this.hadEffectLastTick = $$1;
                this.ticksActive = 0;
                this.factorStart = this.factorCurrent;
                this.factorTarget = $$1 ? 1.0F : 0.0F;
            }
            float $$2 = Mth.clamp((float) this.ticksActive / (float) this.paddingDuration, 0.0F, 1.0F);
            this.factorCurrent = Mth.lerp($$2, this.factorStart, this.factorTarget);
        }

        public float getFactor(LivingEntity livingEntity0, float float1) {
            if (livingEntity0.m_213877_()) {
                this.factorPreviousFrame = this.factorCurrent;
            }
            return Mth.lerp(float1, this.factorPreviousFrame, this.factorCurrent);
        }
    }
}