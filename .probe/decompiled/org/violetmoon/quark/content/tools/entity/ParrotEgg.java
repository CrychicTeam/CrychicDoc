package org.violetmoon.quark.content.tools.entity;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.tools.module.ParrotEggsModule;

public class ParrotEgg extends ThrowableItemProjectile {

    public static final int VARIANTS = 5;

    protected static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ParrotEgg.class, EntityDataSerializers.INT);

    private static final int EVENT_BREAK = 3;

    public ParrotEgg(EntityType<ParrotEgg> entityType, Level world) {
        super(entityType, world);
    }

    public ParrotEgg(Level world, double x, double y, double z) {
        super(ParrotEggsModule.parrotEggType, x, y, z, world);
    }

    public ParrotEgg(Level world, LivingEntity owner) {
        super(ParrotEggsModule.parrotEggType, owner, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(COLOR, 0);
    }

    public int getVariant() {
        return Mth.clamp(this.m_20088_().get(COLOR), 0, 4);
    }

    public void setVariant(int variant) {
        this.m_20088_().set(COLOR, Mth.clamp(variant, 0, 4));
    }

    @NotNull
    @Override
    protected Item getDefaultItem() {
        return (Item) ParrotEggsModule.parrotEggs.get(this.getVariant());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            Vec3 pos = this.m_20182_();
            double motion = 0.08;
            for (int i = 0; i < 8; i++) {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_7846_()), pos.x, pos.y, pos.z, ((double) this.f_19796_.nextFloat() - 0.5) * motion, ((double) this.f_19796_.nextFloat() - 0.5) * motion, ((double) this.f_19796_.nextFloat() - 0.5) * motion);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.m_5790_(entityHitResult);
        entityHitResult.getEntity().hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.m_6532_(hitResult);
        if (!this.m_9236_().isClientSide) {
            Parrot parrot = EntityType.PARROT.create(this.m_9236_());
            if (parrot != null) {
                parrot.setVariant(Parrot.Variant.byId(this.getVariant()));
                parrot.m_146762_(-24000);
                parrot.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                this.m_9236_().m_7967_(parrot);
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }
}