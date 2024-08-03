package net.mehvahdjukaar.supplementaries.common.entities;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class CannonballEntity extends ImprovedProjectileEntity {

    protected CannonballEntity(EntityType<? extends ThrowableItemProjectile> type, Level world) {
        super(type, world);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    protected float getGravity() {
        return super.m_7139_();
    }

    @Override
    protected float getDeceleration() {
        return super.getDeceleration();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        float radius = 4.0F;
        Explosion exp = this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), radius, Level.ExplosionInteraction.TNT);
        super.m_8060_(result);
    }
}