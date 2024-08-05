package net.minecraft.world.entity.projectile;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownTrident extends AbstractArrow {

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);

    private ItemStack tridentItem = new ItemStack(Items.TRIDENT);

    private boolean dealtDamage;

    public int clientSideReturnTridentTickCount;

    public ThrownTrident(EntityType<? extends ThrownTrident> entityTypeExtendsThrownTrident0, Level level1) {
        super(entityTypeExtendsThrownTrident0, level1);
    }

    public ThrownTrident(Level level0, LivingEntity livingEntity1, ItemStack itemStack2) {
        super(EntityType.TRIDENT, livingEntity1, level0);
        this.tridentItem = itemStack2.copy();
        this.f_19804_.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(itemStack2));
        this.f_19804_.set(ID_FOIL, itemStack2.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ID_LOYALTY, (byte) 0);
        this.f_19804_.define(ID_FOIL, false);
    }

    @Override
    public void tick() {
        if (this.f_36704_ > 4) {
            this.dealtDamage = true;
        }
        Entity $$0 = this.m_19749_();
        int $$1 = this.f_19804_.get(ID_LOYALTY);
        if ($$1 > 0 && (this.dealtDamage || this.m_36797_()) && $$0 != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.m_9236_().isClientSide && this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                    this.m_5552_(this.getPickupItem(), 0.1F);
                }
                this.m_146870_();
            } else {
                this.m_36790_(true);
                Vec3 $$2 = $$0.getEyePosition().subtract(this.m_20182_());
                this.m_20343_(this.m_20185_(), this.m_20186_() + $$2.y * 0.015 * (double) $$1, this.m_20189_());
                if (this.m_9236_().isClientSide) {
                    this.f_19791_ = this.m_20186_();
                }
                double $$3 = 0.05 * (double) $$1;
                this.m_20256_(this.m_20184_().scale(0.95).add($$2.normalize().scale($$3)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.m_5496_(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                this.clientSideReturnTridentTickCount++;
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity $$0 = this.m_19749_();
        return $$0 == null || !$$0.isAlive() ? false : !($$0 instanceof ServerPlayer) || !$$0.isSpectator();
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }

    public boolean isFoil() {
        return this.f_19804_.get(ID_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 vec0, Vec3 vec1) {
        return this.dealtDamage ? null : super.findHitEntity(vec0, vec1);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        Entity $$1 = entityHitResult0.getEntity();
        float $$2 = 8.0F;
        if ($$1 instanceof LivingEntity $$3) {
            $$2 += EnchantmentHelper.getDamageBonus(this.tridentItem, $$3.getMobType());
        }
        Entity $$4 = this.m_19749_();
        DamageSource $$5 = this.m_269291_().trident(this, (Entity) ($$4 == null ? this : $$4));
        this.dealtDamage = true;
        SoundEvent $$6 = SoundEvents.TRIDENT_HIT;
        if ($$1.hurt($$5, $$2)) {
            if ($$1.getType() == EntityType.ENDERMAN) {
                return;
            }
            if ($$1 instanceof LivingEntity $$7) {
                if ($$4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects($$7, $$4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) $$4, $$7);
                }
                this.m_7761_($$7);
            }
        }
        this.m_20256_(this.m_20184_().multiply(-0.01, -0.1, -0.01));
        float $$8 = 1.0F;
        if (this.m_9236_() instanceof ServerLevel && this.m_9236_().isThundering() && this.isChanneling()) {
            BlockPos $$9 = $$1.blockPosition();
            if (this.m_9236_().m_45527_($$9)) {
                LightningBolt $$10 = EntityType.LIGHTNING_BOLT.create(this.m_9236_());
                if ($$10 != null) {
                    $$10.m_20219_(Vec3.atBottomCenterOf($$9));
                    $$10.setCause($$4 instanceof ServerPlayer ? (ServerPlayer) $$4 : null);
                    this.m_9236_().m_7967_($$10);
                    $$6 = SoundEvents.TRIDENT_THUNDER;
                    $$8 = 5.0F;
                }
            }
        }
        this.m_5496_($$6, $$8, 1.0F);
    }

    public boolean isChanneling() {
        return EnchantmentHelper.hasChanneling(this.tridentItem);
    }

    @Override
    protected boolean tryPickup(Player player0) {
        return super.tryPickup(player0) || this.m_36797_() && this.m_150171_(player0) && player0.getInventory().add(this.getPickupItem());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player0) {
        if (this.m_150171_(player0) || this.m_19749_() == null) {
            super.playerTouch(player0);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("Trident", 10)) {
            this.tridentItem = ItemStack.of(compoundTag0.getCompound("Trident"));
        }
        this.dealtDamage = compoundTag0.getBoolean("DealtDamage");
        this.f_19804_.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.tridentItem));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.put("Trident", this.tridentItem.save(new CompoundTag()));
        compoundTag0.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tickDespawn() {
        int $$0 = this.f_19804_.get(ID_LOYALTY);
        if (this.f_36705_ != AbstractArrow.Pickup.ALLOWED || $$0 <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double double0, double double1, double double2) {
        return true;
    }
}