package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.item.ParrotEggItem;
import java.util.Optional;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.registries.RegistryObject;

public class ThrownParrotEgg extends ThrowableItemProjectile {

    public ThrownParrotEgg(EntityType<? extends ThrownParrotEgg> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ThrownParrotEgg(Level pLevel, double pX, double pY, double pZ) {
        super(CrockPotEntities.PARROT_EGG.get(), pX, pY, pZ, pLevel);
    }

    public ThrownParrotEgg(Level pLevel, LivingEntity pShooter) {
        super(CrockPotEntities.PARROT_EGG.get(), pShooter, pLevel);
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 3) {
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.m_5790_(pResult);
        pResult.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.m_6532_(pResult);
        if (!this.m_9236_().isClientSide) {
            if (this.f_19796_.nextInt(16) == 0) {
                Optional.of(this.m_7846_().getItem()).filter(ParrotEggItem.class::isInstance).map(ParrotEggItem.class::cast).map(ParrotEggItem::getVariant).ifPresent(variant -> {
                    Parrot parrot = EntityType.PARROT.create(this.m_9236_());
                    if (parrot != null) {
                        parrot.setVariant(variant);
                        parrot.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                        this.m_9236_().m_7967_(parrot);
                    }
                });
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return (Item) ((RegistryObject) CrockPotItems.PARROT_EGGS.get(Parrot.Variant.RED_BLUE)).get();
    }
}