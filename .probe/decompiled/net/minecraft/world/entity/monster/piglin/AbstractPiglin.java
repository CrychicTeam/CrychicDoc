package net.minecraft.world.entity.monster.piglin;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public abstract class AbstractPiglin extends Monster {

    protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);

    protected static final int CONVERSION_TIME = 300;

    protected static final float PIGLIN_EYE_HEIGHT = 1.79F;

    protected int timeInOverworld;

    public AbstractPiglin(EntityType<? extends AbstractPiglin> entityTypeExtendsAbstractPiglin0, Level level1) {
        super(entityTypeExtendsAbstractPiglin0, level1);
        this.m_21553_(true);
        this.applyOpenDoorsAbility();
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    private void applyOpenDoorsAbility() {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation) this.m_21573_()).setCanOpenDoors(true);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 1.79F;
    }

    protected abstract boolean canHunt();

    public void setImmuneToZombification(boolean boolean0) {
        this.m_20088_().set(DATA_IMMUNE_TO_ZOMBIFICATION, boolean0);
    }

    protected boolean isImmuneToZombification() {
        return this.m_20088_().get(DATA_IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_IMMUNE_TO_ZOMBIFICATION, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        if (this.isImmuneToZombification()) {
            compoundTag0.putBoolean("IsImmuneToZombification", true);
        }
        compoundTag0.putInt("TimeInOverworld", this.timeInOverworld);
    }

    @Override
    public double getMyRidingOffset() {
        return this.m_6162_() ? -0.05 : -0.45;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.setImmuneToZombification(compoundTag0.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = compoundTag0.getInt("TimeInOverworld");
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.isConverting()) {
            this.timeInOverworld++;
        } else {
            this.timeInOverworld = 0;
        }
        if (this.timeInOverworld > 300) {
            this.playConvertedSound();
            this.finishConversion((ServerLevel) this.m_9236_());
        }
    }

    public boolean isConverting() {
        return !this.m_9236_().dimensionType().piglinSafe() && !this.isImmuneToZombification() && !this.m_21525_();
    }

    protected void finishConversion(ServerLevel serverLevel0) {
        ZombifiedPiglin $$1 = (ZombifiedPiglin) this.m_21406_(EntityType.ZOMBIFIED_PIGLIN, true);
        if ($$1 != null) {
            $$1.m_7292_(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }

    public boolean isAdult() {
        return !this.m_6162_();
    }

    public abstract PiglinArmPose getArmPose();

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return (LivingEntity) this.f_20939_.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    protected boolean isHoldingMeleeWeapon() {
        return this.m_21205_().getItem() instanceof TieredItem;
    }

    @Override
    public void playAmbientSound() {
        if (PiglinAi.isIdle(this)) {
            super.m_8032_();
        }
    }

    @Override
    protected void sendDebugPackets() {
        super.m_8025_();
        DebugPackets.sendEntityBrain(this);
    }

    protected abstract void playConvertedSound();
}