package com.simibubi.create.content.equipment.potatoCannon;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class PotatoProjectileEntity extends AbstractHurtingProjectile implements IEntityAdditionalSpawnData {

    protected PotatoCannonProjectileType type;

    protected ItemStack stack = ItemStack.EMPTY;

    protected Entity stuckEntity;

    protected Vec3 stuckOffset;

    protected PotatoProjectileRenderMode stuckRenderer;

    protected double stuckFallSpeed;

    protected float additionalDamageMult = 1.0F;

    protected float additionalKnockback = 0.0F;

    protected float recoveryChance = 0.0F;

    public PotatoProjectileEntity(EntityType<? extends AbstractHurtingProjectile> type, Level world) {
        super(type, world);
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }

    public PotatoCannonProjectileType getProjectileType() {
        if (this.type == null) {
            this.type = (PotatoCannonProjectileType) PotatoProjectileTypeManager.getTypeForStack(this.stack).orElse(BuiltinPotatoProjectileTypes.FALLBACK);
        }
        return this.type;
    }

    public void setEnchantmentEffectsFromCannon(ItemStack cannon) {
        int power = cannon.getEnchantmentLevel(Enchantments.POWER_ARROWS);
        int punch = cannon.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
        int flame = cannon.getEnchantmentLevel(Enchantments.FLAMING_ARROWS);
        int recovery = cannon.getEnchantmentLevel((Enchantment) AllEnchantments.POTATO_RECOVERY.get());
        if (power > 0) {
            this.additionalDamageMult = 1.0F + (float) power * 0.2F;
        }
        if (punch > 0) {
            this.additionalKnockback = (float) punch * 0.5F;
        }
        if (flame > 0) {
            this.m_20254_(100);
        }
        if (recovery > 0) {
            this.recoveryChance = 0.125F + (float) recovery * 0.125F;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.stack = ItemStack.of(nbt.getCompound("Item"));
        this.additionalDamageMult = nbt.getFloat("AdditionalDamage");
        this.additionalKnockback = nbt.getFloat("AdditionalKnockback");
        this.recoveryChance = nbt.getFloat("Recovery");
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.put("Item", this.stack.serializeNBT());
        nbt.putFloat("AdditionalDamage", this.additionalDamageMult);
        nbt.putFloat("AdditionalKnockback", this.additionalKnockback);
        nbt.putFloat("Recovery", this.recoveryChance);
        super.addAdditionalSaveData(nbt);
    }

    public Entity getStuckEntity() {
        if (this.stuckEntity == null) {
            return null;
        } else {
            return !this.stuckEntity.isAlive() ? null : this.stuckEntity;
        }
    }

    public void setStuckEntity(Entity stuckEntity) {
        this.stuckEntity = stuckEntity;
        this.stuckOffset = this.m_20182_().subtract(stuckEntity.position());
        this.stuckRenderer = new PotatoProjectileRenderMode.StuckToEntity(this.stuckOffset);
        this.stuckFallSpeed = 0.0;
        this.m_20256_(Vec3.ZERO);
    }

    public PotatoProjectileRenderMode getRenderMode() {
        return this.getStuckEntity() != null ? this.stuckRenderer : this.getProjectileType().getRenderMode();
    }

    @Override
    public void tick() {
        PotatoCannonProjectileType projectileType = this.getProjectileType();
        Entity stuckEntity = this.getStuckEntity();
        if (stuckEntity != null) {
            if (this.m_20186_() < stuckEntity.getY() - 0.1) {
                this.pop(this.m_20182_());
                this.m_6074_();
            } else {
                this.stuckFallSpeed = this.stuckFallSpeed + 0.007 * (double) projectileType.getGravityMultiplier();
                this.stuckOffset = this.stuckOffset.add(0.0, -this.stuckFallSpeed, 0.0);
                Vec3 pos = stuckEntity.position().add(this.stuckOffset);
                this.m_6034_(pos.x, pos.y, pos.z);
            }
        } else {
            this.m_20256_(this.m_20184_().add(0.0, -0.05 * (double) projectileType.getGravityMultiplier(), 0.0).scale((double) projectileType.getDrag()));
        }
        super.tick();
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return new AirParticleData(1.0F, 10.0F);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult ray) {
        super.m_5790_(ray);
        if (this.getStuckEntity() == null) {
            Vec3 hit = ray.m_82450_();
            Entity target = ray.getEntity();
            PotatoCannonProjectileType projectileType = this.getProjectileType();
            float damage = (float) projectileType.getDamage() * this.additionalDamageMult;
            float knockback = projectileType.getKnockback() + this.additionalKnockback;
            Entity owner = this.m_19749_();
            if (target.isAlive()) {
                if (owner instanceof LivingEntity) {
                    ((LivingEntity) owner).setLastHurtMob(target);
                }
                if (target instanceof PotatoProjectileEntity ppe) {
                    if (this.f_19797_ < 10 && target.tickCount < 10) {
                        return;
                    }
                    if (ppe.getProjectileType() != this.getProjectileType()) {
                        if (owner instanceof Player p) {
                            AllAdvancements.POTATO_CANNON_COLLIDE.awardTo(p);
                        }
                        if (ppe.m_19749_() instanceof Player p) {
                            AllAdvancements.POTATO_CANNON_COLLIDE.awardTo(p);
                        }
                    }
                }
                this.pop(hit);
                if (!(target instanceof WitherBoss) || !((WitherBoss) target).isPowered()) {
                    if (!projectileType.preEntityHit(ray)) {
                        boolean targetIsEnderman = target.getType() == EntityType.ENDERMAN;
                        int k = target.getRemainingFireTicks();
                        if (this.m_6060_() && !targetIsEnderman) {
                            target.setSecondsOnFire(5);
                        }
                        boolean onServer = !this.m_9236_().isClientSide;
                        if (onServer && !target.hurt(this.causePotatoDamage(), damage)) {
                            target.setRemainingFireTicks(k);
                            this.m_6074_();
                        } else if (!targetIsEnderman) {
                            if (!projectileType.onEntityHit(ray) && onServer && this.f_19796_.nextDouble() <= (double) this.recoveryChance) {
                                this.recoverItem();
                            }
                            if (!(target instanceof LivingEntity livingentity)) {
                                playHitSound(this.m_9236_(), this.m_20182_());
                                this.m_6074_();
                            } else {
                                if (this.type.getReloadTicks() < 10) {
                                    livingentity.f_19802_ = this.type.getReloadTicks() + 10;
                                }
                                if (onServer && knockback > 0.0F) {
                                    Vec3 appliedMotion = this.m_20184_().multiply(1.0, 0.0, 1.0).normalize().scale((double) knockback * 0.6);
                                    if (appliedMotion.lengthSqr() > 0.0) {
                                        livingentity.m_5997_(appliedMotion.x, 0.1, appliedMotion.z);
                                    }
                                }
                                if (onServer && owner instanceof LivingEntity) {
                                    EnchantmentHelper.doPostHurtEffects(livingentity, owner);
                                    EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, livingentity);
                                }
                                if (livingentity != owner && livingentity instanceof Player && owner instanceof ServerPlayer && !this.m_20067_()) {
                                    ((ServerPlayer) owner).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                                }
                                if (onServer && owner instanceof ServerPlayer serverplayerentity && (!target.isAlive() && target.getType().getCategory() == MobCategory.MONSTER || target instanceof Player && target != owner)) {
                                    AllAdvancements.POTATO_CANNON.awardTo(serverplayerentity);
                                }
                                if (this.type.isSticky() && target.isAlive()) {
                                    this.setStuckEntity(target);
                                } else {
                                    this.m_6074_();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void recoverItem() {
        if (!this.stack.isEmpty()) {
            this.m_19983_(ItemHandlerHelper.copyStackWithSize(this.stack, 1));
        }
    }

    public static void playHitSound(Level world, Vec3 location) {
        AllSoundEvents.POTATO_HIT.playOnServer(world, BlockPos.containing(location));
    }

    public static void playLaunchSound(Level world, Vec3 location, float pitch) {
        AllSoundEvents.FWOOMP.playAt(world, location, 1.0F, pitch, true);
    }

    @Override
    protected void onHitBlock(BlockHitResult ray) {
        Vec3 hit = ray.m_82450_();
        this.pop(hit);
        if (!this.getProjectileType().onBlockHit(this.m_9236_(), ray) && !this.m_9236_().isClientSide && this.f_19796_.nextDouble() <= (double) this.recoveryChance) {
            this.recoverItem();
        }
        super.m_8060_(ray);
        this.m_6074_();
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amt) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } else if (this.m_6673_(source)) {
            return false;
        } else {
            this.pop(this.m_20182_());
            this.m_6074_();
            return true;
        }
    }

    private void pop(Vec3 hit) {
        if (!this.stack.isEmpty()) {
            for (int i = 0; i < 7; i++) {
                Vec3 m = VecHelper.offsetRandomly(Vec3.ZERO, this.f_19796_, 0.25F);
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.stack), hit.x, hit.y, hit.z, m.x, m.y, m.z);
            }
        }
        if (!this.m_9236_().isClientSide) {
            playHitSound(this.m_9236_(), this.m_20182_());
        }
    }

    private DamageSource causePotatoDamage() {
        return CreateDamageSources.potatoCannon(this.m_9236_(), this.m_19749_(), this);
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        return builder.sized(0.25F, 0.25F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        CompoundTag compound = new CompoundTag();
        this.addAdditionalSaveData(compound);
        buffer.writeNbt(compound);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.readAdditionalSaveData(additionalData.readNbt());
    }
}