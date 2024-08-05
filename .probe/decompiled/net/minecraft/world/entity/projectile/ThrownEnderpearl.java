package net.minecraft.world.entity.projectile;

import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownEnderpearl extends ThrowableItemProjectile {

    public ThrownEnderpearl(EntityType<? extends ThrownEnderpearl> entityTypeExtendsThrownEnderpearl0, Level level1) {
        super(entityTypeExtendsThrownEnderpearl0, level1);
    }

    public ThrownEnderpearl(Level level0, LivingEntity livingEntity1) {
        super(EntityType.ENDER_PEARL, livingEntity1, level0);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ENDER_PEARL;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        entityHitResult0.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        for (int $$1 = 0; $$1 < 32; $$1++) {
            this.m_9236_().addParticle(ParticleTypes.PORTAL, this.m_20185_(), this.m_20186_() + this.f_19796_.nextDouble() * 2.0, this.m_20189_(), this.f_19796_.nextGaussian(), 0.0, this.f_19796_.nextGaussian());
        }
        if (!this.m_9236_().isClientSide && !this.m_213877_()) {
            Entity $$2 = this.m_19749_();
            if ($$2 instanceof ServerPlayer $$3) {
                if ($$3.connection.isAcceptingMessages() && $$3.m_9236_() == this.m_9236_() && !$$3.m_5803_()) {
                    if (this.f_19796_.nextFloat() < 0.05F && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                        Endermite $$4 = EntityType.ENDERMITE.create(this.m_9236_());
                        if ($$4 != null) {
                            $$4.m_7678_($$2.getX(), $$2.getY(), $$2.getZ(), $$2.getYRot(), $$2.getXRot());
                            this.m_9236_().m_7967_($$4);
                        }
                    }
                    if ($$2.isPassenger()) {
                        $$3.dismountTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    } else {
                        $$2.teleportTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    }
                    $$2.resetFallDistance();
                    $$2.hurt(this.m_269291_().fall(), 5.0F);
                }
            } else if ($$2 != null) {
                $$2.teleportTo(this.m_20185_(), this.m_20186_(), this.m_20189_());
                $$2.resetFallDistance();
            }
            this.m_146870_();
        }
    }

    @Override
    public void tick() {
        Entity $$0 = this.m_19749_();
        if ($$0 instanceof Player && !$$0.isAlive()) {
            this.m_146870_();
        } else {
            super.m_8119_();
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel serverLevel0) {
        Entity $$1 = this.m_19749_();
        if ($$1 != null && $$1.level().dimension() != serverLevel0.m_46472_()) {
            this.m_5602_(null);
        }
        return super.m_5489_(serverLevel0);
    }
}