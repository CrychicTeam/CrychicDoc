package yesman.epicfight.world.entity;

import java.util.List;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.StunType;

public class AreaEffectBreath extends AreaEffectCloud {

    private static final EntityDataAccessor<Boolean> DATA_HORIZONTAL = SynchedEntityData.defineId(AreaEffectBreath.class, EntityDataSerializers.BOOLEAN);

    private Vec3 initialFirePosition;

    public AreaEffectBreath(EntityType<? extends AreaEffectBreath> entityType, Level level) {
        super(entityType, level);
        this.m_19734_(5);
        this.m_19716_(new MobEffectInstance(MobEffects.HARM, 1, 1));
    }

    public AreaEffectBreath(Level level, double x, double y, double z) {
        this(EpicFightEntities.AREA_EFFECT_BREATH.get(), level);
        this.m_6034_(x, y, z);
        this.initialFirePosition = new Vec3(x, y, z);
    }

    @Override
    public void tick() {
        this.m_6478_(MoverType.SELF, this.m_20184_());
        if (!this.m_9236_().isClientSide()) {
            if (this.f_19797_ >= this.m_19748_()) {
                this.m_146870_();
                return;
            }
            float f = this.m_19743_();
            float radiusPerTick = this.m_146788_();
            if (radiusPerTick != 0.0F) {
                f += radiusPerTick;
                if (f < 0.5F) {
                    this.m_146870_();
                    return;
                }
                this.m_19712_(f);
            }
            this.f_19686_.entrySet().removeIf(p_146784_ -> this.f_19797_ >= (Integer) p_146784_.getValue());
            List<LivingEntity> list1 = this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_());
            if (!list1.isEmpty()) {
                for (LivingEntity livingentity : list1) {
                    if (!this.f_19686_.containsKey(livingentity) && livingentity.isAffectedByPotions()) {
                        double d8 = livingentity.m_20185_() - this.m_20185_();
                        double d1 = livingentity.m_20189_() - this.m_20189_();
                        double d3 = d8 * d8 + d1 * d1;
                        if (d3 <= (double) (f * f)) {
                            this.f_19686_.put(livingentity, this.f_19797_ + 3);
                            livingentity.f_19802_ = 0;
                            EpicFightDamageSources damageSources = EpicFightDamageSources.of(livingentity.m_9236_());
                            EpicFightDamageSource damageSource = damageSources.indirectMagic(this.m_19749_(), this).setAnimation(Animations.DUMMY_ANIMATION).setStunType(StunType.SHORT);
                            damageSource.setInitialPosition(this.initialFirePosition);
                            damageSource.setImpact(2.0F);
                            livingentity.hurt(damageSource, 3.0F);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_HORIZONTAL, true);
    }

    protected void setHorizontal(boolean setter) {
        this.m_20088_().set(DATA_HORIZONTAL, setter);
    }

    public boolean isHorizontal() {
        return this.m_20088_().get(DATA_HORIZONTAL);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        boolean horizontal = this.isHorizontal();
        float width = horizontal ? this.m_19743_() * 2.0F : 1.0F;
        float height = horizontal ? 5.0F : this.m_19743_() * 2.0F;
        return EntityDimensions.scalable(width, height);
    }
}