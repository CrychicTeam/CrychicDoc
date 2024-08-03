package net.minecraft.world.entity.animal;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class Pufferfish extends AbstractFish {

    private static final EntityDataAccessor<Integer> PUFF_STATE = SynchedEntityData.defineId(Pufferfish.class, EntityDataSerializers.INT);

    int inflateCounter;

    int deflateTimer;

    private static final Predicate<LivingEntity> SCARY_MOB = p_289442_ -> p_289442_ instanceof Player && ((Player) p_289442_).isCreative() ? false : p_289442_.m_6095_() == EntityType.AXOLOTL || p_289442_.getMobType() != MobType.WATER;

    static final TargetingConditions targetingConditions = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().selector(SCARY_MOB);

    public static final int STATE_SMALL = 0;

    public static final int STATE_MID = 1;

    public static final int STATE_FULL = 2;

    public Pufferfish(EntityType<? extends Pufferfish> entityTypeExtendsPufferfish0, Level level1) {
        super(entityTypeExtendsPufferfish0, level1);
        this.m_6210_();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PUFF_STATE, 0);
    }

    public int getPuffState() {
        return this.f_19804_.get(PUFF_STATE);
    }

    public void setPuffState(int int0) {
        this.f_19804_.set(PUFF_STATE, int0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (PUFF_STATE.equals(entityDataAccessor0)) {
            this.m_6210_();
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("PuffState", this.getPuffState());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setPuffState(Math.min(compoundTag0.getInt("PuffState"), 2));
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.PUFFERFISH_BUCKET);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(1, new Pufferfish.PufferfishPuffGoal(this));
    }

    @Override
    public void tick() {
        if (!this.m_9236_().isClientSide && this.m_6084_() && this.m_21515_()) {
            if (this.inflateCounter > 0) {
                if (this.getPuffState() == 0) {
                    this.m_5496_(SoundEvents.PUFFER_FISH_BLOW_UP, this.m_6121_(), this.m_6100_());
                    this.setPuffState(1);
                } else if (this.inflateCounter > 40 && this.getPuffState() == 1) {
                    this.m_5496_(SoundEvents.PUFFER_FISH_BLOW_UP, this.m_6121_(), this.m_6100_());
                    this.setPuffState(2);
                }
                this.inflateCounter++;
            } else if (this.getPuffState() != 0) {
                if (this.deflateTimer > 60 && this.getPuffState() == 2) {
                    this.m_5496_(SoundEvents.PUFFER_FISH_BLOW_OUT, this.m_6121_(), this.m_6100_());
                    this.setPuffState(1);
                } else if (this.deflateTimer > 100 && this.getPuffState() == 1) {
                    this.m_5496_(SoundEvents.PUFFER_FISH_BLOW_OUT, this.m_6121_(), this.m_6100_());
                    this.setPuffState(0);
                }
                this.deflateTimer++;
            }
        }
        super.m_8119_();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.m_6084_() && this.getPuffState() > 0) {
            for (Mob $$1 : this.m_9236_().m_6443_(Mob.class, this.m_20191_().inflate(0.3), p_149013_ -> targetingConditions.test(this, p_149013_))) {
                if ($$1.m_6084_()) {
                    this.touch($$1);
                }
            }
        }
    }

    private void touch(Mob mob0) {
        int $$1 = this.getPuffState();
        if (mob0.m_6469_(this.m_269291_().mobAttack(this), (float) (1 + $$1))) {
            mob0.m_147207_(new MobEffectInstance(MobEffects.POISON, 60 * $$1, 0), this);
            this.m_5496_(SoundEvents.PUFFER_FISH_STING, 1.0F, 1.0F);
        }
    }

    @Override
    public void playerTouch(Player player0) {
        int $$1 = this.getPuffState();
        if (player0 instanceof ServerPlayer && $$1 > 0 && player0.hurt(this.m_269291_().mobAttack(this), (float) (1 + $$1))) {
            if (!this.m_20067_()) {
                ((ServerPlayer) player0).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0F));
            }
            player0.m_147207_(new MobEffectInstance(MobEffects.POISON, 60 * $$1, 0), this);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.PUFFER_FISH_FLOP;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return super.m_6972_(pose0).scale(getScale(this.getPuffState()));
    }

    private static float getScale(int int0) {
        switch(int0) {
            case 0:
                return 0.5F;
            case 1:
                return 0.7F;
            default:
                return 1.0F;
        }
    }

    static class PufferfishPuffGoal extends Goal {

        private final Pufferfish fish;

        public PufferfishPuffGoal(Pufferfish pufferfish0) {
            this.fish = pufferfish0;
        }

        @Override
        public boolean canUse() {
            List<LivingEntity> $$0 = this.fish.m_9236_().m_6443_(LivingEntity.class, this.fish.m_20191_().inflate(2.0), p_149015_ -> Pufferfish.targetingConditions.test(this.fish, p_149015_));
            return !$$0.isEmpty();
        }

        @Override
        public void start() {
            this.fish.inflateCounter = 1;
            this.fish.deflateTimer = 0;
        }

        @Override
        public void stop() {
            this.fish.inflateCounter = 0;
        }
    }
}