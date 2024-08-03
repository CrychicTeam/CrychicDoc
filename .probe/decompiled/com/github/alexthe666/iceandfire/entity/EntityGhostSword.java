package com.github.alexthe666.iceandfire.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityGhostSword extends AbstractArrow {

    private IntOpenHashSet piercedEntities;

    private List<Entity> hitEntities;

    private int knockbackStrength;

    public EntityGhostSword(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.m_36781_(9.0);
    }

    public EntityGhostSword(EntityType<? extends AbstractArrow> type, Level worldIn, double x, double y, double z, float r, float g, float b) {
        this(type, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(9.0);
    }

    public EntityGhostSword(EntityType<? extends AbstractArrow> type, Level worldIn, LivingEntity shooter, double dmg) {
        super(type, shooter, worldIn);
        this.m_36781_(dmg);
    }

    public EntityGhostSword(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.GHOST_SWORD.get(), worldIn);
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        super.tick();
        this.f_19794_ = true;
        float sqrt = Mth.sqrt((float) (this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z));
        if (sqrt < 0.1F && this.f_19797_ > 200) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        double d0 = 0.0;
        double d1 = 0.0;
        double d2 = 0.01;
        double x = this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_();
        double y = this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - (double) this.m_20206_();
        double z = this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_();
        float f = (this.m_20205_() + this.m_20206_() + this.m_20205_()) * 0.333F + 0.5F;
        if (this.particleDistSq(x, y, z) < (double) (f * f)) {
            this.m_9236_().addParticle(ParticleTypes.SNEEZE, x, y + 0.5, z, d0, d1, d2);
        }
        Vec3 vector3d = this.m_20184_();
        double f3 = vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, f3) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
        Vec3 vector3d2 = this.m_20182_();
        Vec3 vector3d3 = vector3d2.add(vector3d);
        HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(vector3d2, vector3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            vector3d3 = raytraceresult.getLocation();
        }
        while (!this.m_213877_()) {
            EntityHitResult entityraytraceresult = this.m_6351_(vector3d2, vector3d3);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }
            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) raytraceresult).getEntity();
                Entity entity1 = this.m_19749_();
                if (entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                if (raytraceresult.getType() != HitResult.Type.BLOCK) {
                    this.m_6532_(raytraceresult);
                }
                this.f_19812_ = true;
            }
            if (entityraytraceresult == null || this.m_36796_() <= 0) {
                break;
            }
            raytraceresult = null;
        }
    }

    public double particleDistSq(double toX, double toY, double toZ) {
        double d0 = this.m_20185_() - toX;
        double d1 = this.m_20186_() - toY;
        double d2 = this.m_20189_() - toZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void playSound(@NotNull SoundEvent soundIn, float volume, float pitch) {
        if (!this.m_20067_() && soundIn != SoundEvents.ARROW_HIT && soundIn != SoundEvents.ARROW_HIT_PLAYER) {
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), soundIn, this.m_5720_(), volume, pitch);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void setKnockback(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = (float) this.m_20184_().length();
        int i = Mth.ceil(Math.max((double) f * this.m_36789_(), 0.0));
        if (this.m_36796_() > 0) {
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }
            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }
            if (this.piercedEntities.size() >= this.m_36796_() + 1) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                return;
            }
            this.piercedEntities.add(entity.getId());
        }
        if (this.m_36792_()) {
            i += this.f_19796_.nextInt(i / 2 + 2);
        }
        Entity entity1 = this.m_19749_();
        DamageSource damagesource = this.m_9236_().damageSources().magic();
        if (entity1 != null && entity1 instanceof LivingEntity) {
            damagesource = this.m_9236_().damageSources().indirectMagic(this, entity1);
            ((LivingEntity) entity1).setLastHurtMob(entity);
        }
        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getRemainingFireTicks();
        if (this.m_6060_() && !flag) {
            entity.setSecondsOnFire(5);
        }
        if (entity.hurt(damagesource, (float) i)) {
            if (flag) {
                return;
            }
            if (entity instanceof LivingEntity livingentity) {
                if (this.knockbackStrength > 0) {
                    Vec3 vec3d = this.m_20184_().multiply(1.0, 0.0, 1.0).normalize().scale((double) this.knockbackStrength * 0.6);
                    if (vec3d.lengthSqr() > 0.0) {
                        livingentity.m_5997_(vec3d.x, 0.1, vec3d.z);
                    }
                }
                this.m_7761_(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof Player && entity1 instanceof ServerPlayer) {
                    ((ServerPlayer) entity1).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }
                if (!entity.isAlive() && this.hitEntities != null) {
                    this.hitEntities.add(livingentity);
                }
            }
            this.playSound(this.m_36784_(), 1.0F, 1.2F / (this.f_19796_.nextFloat() * 0.2F + 0.9F));
            if (this.m_36796_() <= 0) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else {
            this.m_20256_(this.m_20184_().scale(-0.1));
            if (!this.m_9236_().isClientSide && this.m_20184_().lengthSqr() < 1.0E-7) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }
}