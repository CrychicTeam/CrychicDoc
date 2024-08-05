package dev.xkmc.l2weaponry.content.entity;

import com.google.common.collect.Lists;
import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2weaponry.content.item.base.IThrowableCallback;
import dev.xkmc.l2weaponry.init.registrate.LWEnchantments;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class BaseThrownWeaponEntity<T extends BaseThrownWeaponEntity<T>> extends AbstractArrow implements IEntityAdditionalSpawnData {

    private static final int LOWEST_HEIGHT = -32;

    private static final int MAX_DIST = 400;

    private static final int MAX_HOR_DIST = 100;

    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BaseThrownWeaponEntity.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BaseThrownWeaponEntity.class, EntityDataSerializers.BOOLEAN);

    private ItemStack item;

    public int remainingHit = 1;

    public int clientSideReturnTridentTickCount;

    public int slot;

    public float waterInertia = 0.6F;

    @Nullable
    public Entity targetCache;

    @Nullable
    private Vec3 origin;

    public BaseThrownWeaponEntity(EntityType<T> type, Level pLevel) {
        super(type, pLevel);
        this.setItem(new ItemStack(Items.TRIDENT));
    }

    public BaseThrownWeaponEntity(EntityType<T> type, Level pLevel, LivingEntity pShooter, ItemStack pStack, int slot) {
        super(type, pShooter, pLevel);
        this.setItem(pStack.copy());
        this.slot = slot;
        int loyalty = EnchantmentHelper.getLoyalty(pStack);
        if (pStack.getEnchantmentLevel((Enchantment) LWEnchantments.PROJECTION.get()) > 0) {
            loyalty = 0;
        }
        this.f_19804_.set(ID_LOYALTY, (byte) loyalty);
        this.f_19804_.set(ID_FOIL, pStack.hasFoil());
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public void setPierceLevel(byte lv) {
        super.setPierceLevel(lv);
        this.remainingHit = lv + 1;
    }

    private void tickEarlyReturn() {
        Entity entity = this.m_19749_();
        int loyal = this.f_19804_.get(ID_LOYALTY);
        if (this.m_20068_() && this.m_20184_().length() < 0.01) {
            this.remainingHit = 0;
            this.m_20242_(false);
        }
        if (entity != null && loyal > 0 && this.remainingHit > 0) {
            if (this.origin == null) {
                this.origin = this.m_20182_();
            } else if (this.m_20182_().y < (double) (this.m_9236_().m_141937_() + -32)) {
                this.remainingHit = 0;
            } else {
                Vec3 diff = this.m_20182_().subtract(this.origin);
                if (diff.horizontalDistance() > 100.0 || diff.length() > 400.0) {
                    this.remainingHit = 0;
                }
            }
        }
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
            this.remainingHit = 0;
        }
        this.tickEarlyReturn();
        Entity entity = this.m_19749_();
        int loyal = this.f_19804_.get(ID_LOYALTY);
        if (loyal > 0 && (this.remainingHit == 0 || this.m_36797_()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.m_9236_().isClientSide && this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                    this.m_5552_(this.getPickupItem(), 0.1F);
                }
                this.m_146870_();
            } else {
                this.m_36790_(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.m_20182_());
                this.m_20343_(this.m_20185_(), this.m_20186_() + vec3.y * 0.015 * (double) loyal, this.m_20189_());
                if (this.m_9236_().isClientSide) {
                    this.f_19791_ = this.m_20186_();
                }
                double d0 = 0.05 * (double) loyal;
                this.m_20256_(this.m_20184_().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.m_5496_(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                this.clientSideReturnTridentTickCount++;
            }
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.m_19749_();
        return entity != null && entity.isAlive() ? !(entity instanceof ServerPlayer) || !entity.isSpectator() : false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.item.copy();
    }

    public boolean isFoil() {
        return this.f_19804_.get(ID_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 pStartVec, Vec3 pEndVec) {
        return this.remainingHit == 0 ? null : super.findHitEntity(pStartVec, pEndVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity entity = pResult.getEntity();
        float damage = (float) this.m_36789_();
        if (entity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(this.item, livingentity.getMobType());
        }
        Entity owner = this.m_19749_();
        this.targetCache = entity;
        DamageSource damagesource = this.m_9236_().damageSources().trident(this, (Entity) (owner == null ? this : owner));
        this.targetCache = null;
        if (this.remainingHit > 0) {
            this.remainingHit--;
            if (this.m_36796_() > 0) {
                if (this.f_36701_ == null) {
                    this.f_36701_ = new IntOpenHashSet(this.m_36796_() + 1);
                }
                if (this.f_36702_ == null) {
                    this.f_36702_ = Lists.newArrayListWithCapacity(5);
                }
                this.f_36701_.add(entity.getId());
            }
        }
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity le) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(le, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, le);
                }
                this.m_7761_(le);
                if (!entity.isAlive() && this.f_36702_ != null) {
                    this.f_36702_.add(entity);
                }
                if (this.item.getItem() instanceof IThrowableCallback cb) {
                    cb.onHitEntity(this, this.item, le);
                }
            }
        }
        if (this.remainingHit == 0) {
            this.m_20256_(this.m_20184_().multiply(-0.01, -0.1, -0.01));
        }
        float f1 = 1.0F;
        this.m_5496_(soundevent, f1, 1.0F);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (this.item.getItem() instanceof IThrowableCallback cb) {
            cb.onHitBlock(this, this.item);
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (this.f_36705_ == AbstractArrow.Pickup.CREATIVE_ONLY) {
            return player.getAbilities().instabuild;
        } else {
            return this.f_36705_ != AbstractArrow.Pickup.ALLOWED && (!this.m_36797_() || !this.m_150171_(player)) ? false : this.addToPlayer(player, this.getPickupItem());
        }
    }

    protected boolean addToPlayer(Player player, ItemStack stack) {
        if (this.slot == 40) {
            if (player.m_21206_().isEmpty()) {
                player.m_21008_(InteractionHand.OFF_HAND, stack.copy());
                stack.setCount(0);
                return true;
            }
        } else if (player.getInventory().getItem(this.slot).isEmpty() && player.getInventory().add(this.slot, stack)) {
            return true;
        }
        return player.getInventory().add(stack);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (this.m_150171_(pEntity) || this.m_19749_() == null) {
            super.playerTouch(pEntity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Item", 10)) {
            this.setItem(ItemStack.of(pCompound.getCompound("Item")));
        }
        this.remainingHit = pCompound.getInt("RemainingHit");
        this.slot = pCompound.getInt("playerSlot");
        this.f_19804_.set(ID_LOYALTY, (byte) EnchantmentHelper.getLoyalty(this.item));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("Item", this.item.save(new CompoundTag()));
        pCompound.putInt("RemainingHit", this.remainingHit);
        pCompound.putInt("playerSlot", this.slot);
    }

    @Override
    public void tickDespawn() {
        int i = this.f_19804_.get(ID_LOYALTY);
        if (this.f_36705_ != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public final Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.item);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.setItem(buffer.readItem());
    }

    @Override
    protected float getWaterInertia() {
        return this.waterInertia;
    }

    private void setItem(ItemStack item) {
        this.item = item;
        if (item.getItem() instanceof TieredItem tier && tier.getTier() == LCMats.POSEIDITE.getTier()) {
            this.waterInertia = 0.99F;
            return;
        }
        this.waterInertia = 0.6F;
    }
}