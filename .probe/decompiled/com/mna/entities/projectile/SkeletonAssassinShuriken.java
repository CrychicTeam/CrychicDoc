package com.mna.entities.projectile;

import com.mna.api.entities.DamageHelper;
import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class SkeletonAssassinShuriken extends AbstractArrow {

    private int ticksInGround = 0;

    public SkeletonAssassinShuriken(EntityType<? extends SkeletonAssassinShuriken> p_i231584_1_, Level p_i231584_2_) {
        super(p_i231584_1_, p_i231584_2_);
        this.m_36740_(SFX.Entity.SkeletonAssassin.SHURIKEN_IMPACT);
    }

    public SkeletonAssassinShuriken(Level world, LivingEntity entity) {
        super(EntityInit.SKELETON_ASSASSIN_SHURIKEN.get(), world);
        Vec3 position = entity.m_20182_().add(0.0, 1.0, 0.0);
        this.m_6021_(position.x, position.y, position.z);
        this.m_5602_(entity);
        this.f_36705_ = AbstractArrow.Pickup.DISALLOWED;
        this.m_36740_(SFX.Entity.SkeletonAssassin.SHURIKEN_IMPACT);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity entity1 = this.m_19749_();
        if (entity1 instanceof Player || entity.getType() != EntityInit.SKELETON_ASSASSIN.get() && entity.getType() != EntityInit.HULKING_ZOMBIE.get()) {
            int i = 4;
            DamageSource damagesource;
            if (entity1 != null && entity1 instanceof LivingEntity) {
                damagesource = DamageHelper.createSourcedType(DamageTypes.ARROW, this.m_9236_().registryAccess(), entity1, this);
                if (entity1 instanceof LivingEntity) {
                    ((LivingEntity) entity1).setLastHurtMob(entity);
                }
            } else {
                damagesource = DamageHelper.createSourcedType(DamageTypes.ARROW, this.m_9236_().registryAccess(), this);
            }
            boolean flag = entity.getType() == EntityType.ENDERMAN;
            int k = entity.getRemainingFireTicks();
            if (this.m_6060_() && !flag) {
                entity.setSecondsOnFire(5);
            }
            if (entity.hurt(damagesource, (float) i)) {
                if (flag) {
                    return;
                }
                if (entity instanceof LivingEntity livingentity && !this.m_9236_().isClientSide() && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity);
                }
                this.m_5496_(SFX.Entity.SkeletonAssassin.SHURIKEN_IMPACT, 1.0F, (float) (0.9 + Math.random() * 0.2));
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                entity.setRemainingFireTicks(k);
                this.m_20256_(this.m_20184_().scale(-0.1));
                this.m_146922_(this.m_146908_() + 180.0F);
                this.f_19859_ += 180.0F;
                if (!this.m_9236_().isClientSide() && this.m_20184_().lengthSqr() < 1.0E-7) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void tickDespawn() {
        this.ticksInGround++;
        if (this.ticksInGround >= 60) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public boolean isInGround() {
        return this.f_36703_;
    }
}