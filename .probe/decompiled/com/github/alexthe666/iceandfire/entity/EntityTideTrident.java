package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityTideTrident extends ThrownTrident {

    private static final int ADDITIONALPIERCING = 2;

    private int entitiesHit = 0;

    public EntityTideTrident(EntityType<? extends ThrownTrident> type, Level worldIn) {
        super(type, worldIn);
        this.f_37555_ = new ItemStack(IafItemRegistry.TIDE_TRIDENT.get());
    }

    public EntityTideTrident(Level worldIn, LivingEntity thrower, ItemStack thrownStackIn) {
        this(IafEntityRegistry.TIDE_TRIDENT.get(), worldIn);
        this.m_6034_(thrower.m_20185_(), thrower.m_20188_() - 0.1F, thrower.m_20189_());
        this.m_5602_(thrower);
        this.f_37555_ = thrownStackIn;
        this.f_19804_.set(f_37558_, (byte) EnchantmentHelper.getLoyalty(thrownStackIn));
        this.f_19804_.set(f_37554_, thrownStackIn.hasFoil());
        int piercingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, thrownStackIn);
        this.m_36767_((byte) piercingLevel);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.f_37555_, livingentity.getMobType());
        }
        Entity entity1 = this.m_19749_();
        DamageSource damagesource = this.m_9236_().damageSources().trident(this, (Entity) (entity1 == null ? this : entity1));
        this.entitiesHit++;
        if (this.entitiesHit >= this.getMaxPiercing()) {
            this.f_37556_ = true;
        }
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }
                this.m_7761_(livingentity1);
            }
        }
        float f1 = 1.0F;
        if (this.m_9236_() instanceof ServerLevel && this.m_9236_().isThundering() && EnchantmentHelper.hasChanneling(this.f_37555_)) {
            BlockPos blockpos = entity.blockPosition();
            if (this.m_9236_().m_45527_(blockpos)) {
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(this.m_9236_());
                lightningboltentity.m_20219_(Vec3.atCenterOf(blockpos));
                lightningboltentity.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                this.m_9236_().m_7967_(lightningboltentity);
                soundevent = SoundEvents.TRIDENT_THUNDER;
                f1 = 5.0F;
            }
        }
        this.m_5496_(soundevent, f1, 1.0F);
    }

    private int getMaxPiercing() {
        return 2 + this.m_36796_();
    }
}