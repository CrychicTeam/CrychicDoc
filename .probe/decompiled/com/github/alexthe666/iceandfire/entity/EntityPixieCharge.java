package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityPixieCharge extends Fireball {

    public int ticksInAir;

    private final float[] rgb;

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn) {
        super(t, worldIn);
        this.rgb = EntityPixie.PARTICLE_RGB[this.f_19796_.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }

    public EntityPixieCharge(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn);
    }

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn, double posX, double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(t, posX, posY, posZ, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.f_36813_ = accelX / d0 * 0.07;
        this.f_36814_ = accelY / d0 * 0.07;
        this.f_36815_ = accelZ / d0 * 0.07;
        this.rgb = EntityPixie.PARTICLE_RGB[this.f_19796_.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }

    public EntityPixieCharge(EntityType<? extends Fireball> t, Level worldIn, Player shooter, double accelX, double accelY, double accelZ) {
        super(t, shooter, accelX, accelY, accelZ, worldIn);
        double d0 = Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.f_36813_ = accelX / d0 * 0.07;
        this.f_36814_ = accelY / d0 * 0.07;
        this.f_36815_ = accelZ / d0 * 0.07;
        this.rgb = EntityPixie.PARTICLE_RGB[this.f_19796_.nextInt(EntityPixie.PARTICLE_RGB.length - 1)];
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        Entity shootingEntity = this.m_19749_();
        if (this.m_9236_().isClientSide) {
            for (int i = 0; i < 5; i++) {
                IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.m_20185_() + this.f_19796_.nextDouble() * 0.15F * (double) (this.f_19796_.nextBoolean() ? -1 : 1), this.m_20186_() + this.f_19796_.nextDouble() * 0.15F * (double) (this.f_19796_.nextBoolean() ? -1 : 1), this.m_20189_() + this.f_19796_.nextDouble() * 0.15F * (double) (this.f_19796_.nextBoolean() ? -1 : 1), (double) this.rgb[0], (double) this.rgb[1], (double) this.rgb[2]);
            }
        }
        this.m_20095_();
        if (this.f_19797_ > 30) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.m_9236_().isClientSide || (shootingEntity == null || shootingEntity.isAlive()) && this.m_9236_().m_46805_(this.m_20183_())) {
            this.m_6075_();
            if (this.shouldBurn()) {
                this.m_20254_(1);
            }
            this.ticksInAir++;
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.m_5603_(x$0));
            if (raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            Vec3 vector3d = this.m_20184_();
            double d0 = this.m_20185_() + vector3d.x;
            double d1 = this.m_20186_() + vector3d.y;
            double d2 = this.m_20189_() + vector3d.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.m_6884_();
            this.m_20256_(vector3d.add(this.f_36813_, this.f_36814_, this.f_36815_).scale((double) f));
            this.f_36813_ *= 0.95F;
            this.f_36814_ *= 0.95F;
            this.f_36815_ *= 0.95F;
            this.m_5997_(this.f_36813_, this.f_36814_, this.f_36815_);
            this.ticksInAir++;
            if (this.m_20069_()) {
                for (int i = 0; i < 4; i++) {
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE, this.m_20185_() - this.m_20184_().x * 0.25, this.m_20186_() - this.m_20184_().y * 0.25, this.m_20189_() - this.m_20184_().z * 0.25, this.m_20184_().x, this.m_20184_().y, this.m_20184_().z);
                }
            }
            this.m_6034_(d0, d1, d2);
            this.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        boolean flag = false;
        Entity shootingEntity = this.m_19749_();
        if (!this.m_9236_().isClientSide && movingObject.getType() == HitResult.Type.ENTITY && !((EntityHitResult) movingObject).getEntity().is(shootingEntity)) {
            Entity entity = ((EntityHitResult) movingObject).getEntity();
            if (shootingEntity != null && shootingEntity.equals(entity)) {
                flag = true;
            } else {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100, 0));
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                    entity.hurt(this.m_9236_().damageSources().indirectMagic(shootingEntity, null), 5.0F);
                }
                if (this.m_9236_().isClientSide) {
                    for (int i = 0; i < 20; i++) {
                        IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.m_20185_() + this.f_19796_.nextDouble() * 1.0 * (double) (this.f_19796_.nextBoolean() ? -1 : 1), this.m_20186_() + this.f_19796_.nextDouble() * 1.0 * (double) (this.f_19796_.nextBoolean() ? -1 : 1), this.m_20189_() + this.f_19796_.nextDouble() * 1.0 * (double) (this.f_19796_.nextBoolean() ? -1 : 1), (double) this.rgb[0], (double) this.rgb[1], (double) this.rgb[2]);
                    }
                }
                if ((shootingEntity == null || !(shootingEntity instanceof Player) || !((Player) shootingEntity).isCreative()) && this.f_19796_.nextInt(3) == 0) {
                    this.m_5552_(new ItemStack(IafItemRegistry.PIXIE_DUST.get(), 1), 0.45F);
                }
            }
            if (!flag && this.f_19797_ > 4) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }
}