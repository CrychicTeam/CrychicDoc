package net.minecraft.world.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownEgg extends ThrowableItemProjectile {

    public ThrownEgg(EntityType<? extends ThrownEgg> entityTypeExtendsThrownEgg0, Level level1) {
        super(entityTypeExtendsThrownEgg0, level1);
    }

    public ThrownEgg(Level level0, LivingEntity livingEntity1) {
        super(EntityType.EGG, livingEntity1, level0);
    }

    public ThrownEgg(Level level0, double double1, double double2, double double3) {
        super(EntityType.EGG, double1, double2, double3, level0);
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 3) {
            double $$1 = 0.08;
            for (int $$2 = 0; $$2 < 8; $$2++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08, ((double) this.f_19796_.nextFloat() - 0.5) * 0.08);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        entityHitResult0.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
            if (this.f_19796_.nextInt(8) == 0) {
                int $$1 = 1;
                if (this.f_19796_.nextInt(32) == 0) {
                    $$1 = 4;
                }
                for (int $$2 = 0; $$2 < $$1; $$2++) {
                    Chicken $$3 = EntityType.CHICKEN.create(this.m_9236_());
                    if ($$3 != null) {
                        $$3.m_146762_(-24000);
                        $$3.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                        this.m_9236_().m_7967_($$3);
                    }
                }
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EGG;
    }
}