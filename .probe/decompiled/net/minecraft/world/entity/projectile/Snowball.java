package net.minecraft.world.entity.projectile;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Snowball extends ThrowableItemProjectile {

    public Snowball(EntityType<? extends Snowball> entityTypeExtendsSnowball0, Level level1) {
        super(entityTypeExtendsSnowball0, level1);
    }

    public Snowball(Level level0, LivingEntity livingEntity1) {
        super(EntityType.SNOWBALL, livingEntity1, level0);
    }

    public Snowball(Level level0, double double1, double double2, double double3) {
        super(EntityType.SNOWBALL, double1, double2, double3, level0);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleOptions getParticle() {
        ItemStack $$0 = this.m_37454_();
        return (ParticleOptions) ($$0.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, $$0));
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 3) {
            ParticleOptions $$1 = this.getParticle();
            for (int $$2 = 0; $$2 < 8; $$2++) {
                this.m_9236_().addParticle($$1, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult0) {
        super.m_5790_(entityHitResult0);
        Entity $$1 = entityHitResult0.getEntity();
        int $$2 = $$1 instanceof Blaze ? 3 : 0;
        $$1.hurt(this.m_269291_().thrown(this, this.m_19749_()), (float) $$2);
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }
}