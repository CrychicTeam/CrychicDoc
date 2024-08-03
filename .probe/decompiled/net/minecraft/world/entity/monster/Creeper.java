package net.minecraft.world.entity.monster;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class Creeper extends Monster implements PowerableMob {

    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData.defineId(Creeper.class, EntityDataSerializers.BOOLEAN);

    private int oldSwell;

    private int swell;

    private int maxSwell = 30;

    private int explosionRadius = 3;

    private int droppedSkulls;

    public Creeper(EntityType<? extends Creeper> entityTypeExtendsCreeper0, Level level1) {
        super(entityTypeExtendsCreeper0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new SwellGoal(this));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Ocelot.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Cat.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public int getMaxFallDistance() {
        return this.m_5448_() == null ? 3 : 3 + (int) (this.m_21223_() - 1.0F);
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        boolean $$3 = super.m_142535_(float0, float1, damageSource2);
        this.swell += (int) (float0 * 1.5F);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }
        return $$3;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_SWELL_DIR, -1);
        this.f_19804_.define(DATA_IS_POWERED, false);
        this.f_19804_.define(DATA_IS_IGNITED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        if (this.f_19804_.get(DATA_IS_POWERED)) {
            compoundTag0.putBoolean("powered", true);
        }
        compoundTag0.putShort("Fuse", (short) this.maxSwell);
        compoundTag0.putByte("ExplosionRadius", (byte) this.explosionRadius);
        compoundTag0.putBoolean("ignited", this.isIgnited());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.f_19804_.set(DATA_IS_POWERED, compoundTag0.getBoolean("powered"));
        if (compoundTag0.contains("Fuse", 99)) {
            this.maxSwell = compoundTag0.getShort("Fuse");
        }
        if (compoundTag0.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compoundTag0.getByte("ExplosionRadius");
        }
        if (compoundTag0.getBoolean("ignited")) {
            this.ignite();
        }
    }

    @Override
    public void tick() {
        if (this.m_6084_()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }
            int $$0 = this.getSwellDir();
            if ($$0 > 0 && this.swell == 0) {
                this.m_5496_(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.m_146850_(GameEvent.PRIME_FUSE);
            }
            this.swell += $$0;
            if (this.swell < 0) {
                this.swell = 0;
            }
            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }
        super.m_8119_();
    }

    @Override
    public void setTarget(@Nullable LivingEntity livingEntity0) {
        if (!(livingEntity0 instanceof Goat)) {
            super.m_6710_(livingEntity0);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource0, int int1, boolean boolean2) {
        super.m_7472_(damageSource0, int1, boolean2);
        Entity $$3 = damageSource0.getEntity();
        if ($$3 != this && $$3 instanceof Creeper $$4 && $$4.canDropMobsSkull()) {
            $$4.increaseDroppedSkulls();
            this.m_19998_(Items.CREEPER_HEAD);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        return true;
    }

    @Override
    public boolean isPowered() {
        return this.f_19804_.get(DATA_IS_POWERED);
    }

    public float getSwelling(float float0) {
        return Mth.lerp(float0, (float) this.oldSwell, (float) this.swell) / (float) (this.maxSwell - 2);
    }

    public int getSwellDir() {
        return this.f_19804_.get(DATA_SWELL_DIR);
    }

    public void setSwellDir(int int0) {
        this.f_19804_.set(DATA_SWELL_DIR, int0);
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        super.m_8038_(serverLevel0, lightningBolt1);
        this.f_19804_.set(DATA_IS_POWERED, true);
    }

    @Override
    protected InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent $$3 = $$2.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.m_9236_().playSound(player0, this.m_20185_(), this.m_20186_(), this.m_20189_(), $$3, this.m_5720_(), 1.0F, this.f_19796_.nextFloat() * 0.4F + 0.8F);
            if (!this.m_9236_().isClientSide) {
                this.ignite();
                if (!$$2.isDamageableItem()) {
                    $$2.shrink(1);
                } else {
                    $$2.hurtAndBreak(1, player0, p_32290_ -> p_32290_.m_21190_(interactionHand1));
                }
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.m_6071_(player0, interactionHand1);
        }
    }

    private void explodeCreeper() {
        if (!this.m_9236_().isClientSide) {
            float $$0 = this.isPowered() ? 2.0F : 1.0F;
            this.f_20890_ = true;
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), (float) this.explosionRadius * $$0, Level.ExplosionInteraction.MOB);
            this.m_146870_();
            this.spawnLingeringCloud();
        }
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> $$0 = this.m_21220_();
        if (!$$0.isEmpty()) {
            AreaEffectCloud $$1 = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_());
            $$1.setRadius(2.5F);
            $$1.setRadiusOnUse(-0.5F);
            $$1.setWaitTime(10);
            $$1.setDuration($$1.getDuration() / 2);
            $$1.setRadiusPerTick(-$$1.getRadius() / (float) $$1.getDuration());
            for (MobEffectInstance $$2 : $$0) {
                $$1.addEffect(new MobEffectInstance($$2));
            }
            this.m_9236_().m_7967_($$1);
        }
    }

    public boolean isIgnited() {
        return this.f_19804_.get(DATA_IS_IGNITED);
    }

    public void ignite() {
        this.f_19804_.set(DATA_IS_IGNITED, true);
    }

    public boolean canDropMobsSkull() {
        return this.isPowered() && this.droppedSkulls < 1;
    }

    public void increaseDroppedSkulls() {
        this.droppedSkulls++;
    }
}