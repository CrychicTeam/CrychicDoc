package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.TephraExplosion;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class ExtinctionSpearEntity extends AbstractArrow {

    private boolean dealtDamage;

    private ItemStack spearItem = new ItemStack(ACItemRegistry.EXTINCTION_SPEAR.get());

    private static final EntityDataAccessor<Boolean> WIGGLING = SynchedEntityData.defineId(ExtinctionSpearEntity.class, EntityDataSerializers.BOOLEAN);

    private int ticksWiggling = 0;

    public ExtinctionSpearEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public ExtinctionSpearEntity(Level level, LivingEntity shooter, ItemStack itemStack) {
        super(ACEntityRegistry.EXTINCTION_SPEAR.get(), shooter, level);
        this.spearItem = itemStack.copy();
    }

    public ExtinctionSpearEntity(Level level, double x, double y, double z) {
        super(ACEntityRegistry.EXTINCTION_SPEAR.get(), x, y, z, level);
    }

    public ExtinctionSpearEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.EXTINCTION_SPEAR.get(), level);
        this.m_20011_(this.m_142242_());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(WIGGLING, false);
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.spearItem;
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
        return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
    }

    @Override
    public void tick() {
        super.tick();
        Entity entity = this.m_19749_();
        if ((this.f_36703_ || this.m_36797_()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.m_9236_().isClientSide && this.f_36705_ == AbstractArrow.Pickup.ALLOWED) {
                    this.m_5552_(this.getPickupItem(), 0.1F);
                }
                this.m_146870_();
            } else if (this.isWiggling()) {
                if (this.ticksWiggling++ > 20) {
                    this.setWiggling(false);
                    this.explode();
                }
            } else {
                Vec3 vec3 = entity.getEyePosition().subtract(this.m_20182_());
                if (!this.m_36797_()) {
                    this.m_20256_(Vec3.ZERO);
                }
                this.m_36790_(true);
                if (this.m_9236_().isClientSide) {
                    this.f_19791_ = this.m_20186_();
                }
                double d0 = 0.3;
                this.m_20256_(this.m_20184_().scale(0.95).add(vec3.normalize().scale(d0)));
            }
        }
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            Vec3 center = this.m_20182_().add(0.0, 0.25, 0.0);
            Vec3 vec3x = this.m_20184_().scale(-0.2F);
            this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.TEPHRA_FLAME.get(), true, center.x, center.y, center.z, vec3x.x, vec3x.y, vec3x.z);
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        Entity entity = this.m_19749_();
        return entity != null && !entity.equals(player) ? false : super.tryPickup(player);
    }

    private void explode() {
        TephraExplosion explosion = new TephraExplosion(this.m_9236_(), this.m_19749_(), this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 1.5F, Explosion.BlockInteraction.KEEP);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.setWiggling(true);
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.m_19749_();
        return entity != null && entity.isAlive() ? !(entity instanceof ServerPlayer) || !entity.isSpectator() : false;
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float f = 10.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.getPickupItem(), livingentity.getMobType());
        }
        Entity entity1 = this.m_19749_();
        DamageSource damagesource = this.m_269291_().trident(this, (Entity) (entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = ACSoundRegistry.EXTINCTION_SPEAR_HIT.get();
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            entity.setSecondsOnFire(5);
            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }
                this.m_7761_(livingentity1);
            }
            if (entity1 != null) {
                DinosaurSpiritEntity dinosaurSpirit = ACEntityRegistry.DINOSAUR_SPIRIT.get().create(this.m_9236_());
                dinosaurSpirit.m_6034_(entity.getX(), entity.getY() + (double) entity.getBbHeight(), entity.getZ());
                dinosaurSpirit.setDinosaurType(DinosaurSpiritEntity.DinosaurType.SUBTERRANODON);
                dinosaurSpirit.setPlayerUUID(entity1.getUUID());
                dinosaurSpirit.setAttackingEntityId(entity.getId());
                dinosaurSpirit.m_7618_(EntityAnchorArgument.Anchor.EYES, entity1.getEyePosition());
                dinosaurSpirit.setEnchantmentLevel(this.spearItem.getEnchantmentLevel(ACEnchantmentRegistry.PLUMMETING_FLIGHT.get()));
                this.m_5496_(ACSoundRegistry.EXTINCTION_SPEAR_SUMMON.get(), 1.0F, 1.0F);
                this.m_9236_().m_7967_(dinosaurSpirit);
            }
        }
        this.m_20256_(this.m_20184_().multiply(-0.01, -0.1, -0.01));
        float f1 = 1.0F;
        this.m_5496_(soundevent, f1, 1.0F);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ACSoundRegistry.LIMESTONE_SPEAR_HIT.get();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public boolean isWiggling() {
        return this.f_19804_.get(WIGGLING);
    }

    public void setWiggling(boolean wiggling) {
        this.f_19804_.set(WIGGLING, wiggling);
    }
}