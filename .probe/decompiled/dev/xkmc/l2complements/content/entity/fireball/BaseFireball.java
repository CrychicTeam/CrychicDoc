package dev.xkmc.l2complements.content.entity.fireball;

import dev.xkmc.l2complements.content.entity.ISizedItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class BaseFireball<T extends BaseFireball<T>> extends Fireball implements ISizedItemEntity {

    public int lifetime = 200;

    private int life = 0;

    public BaseFireball(EntityType<T> type, Level level) {
        super(type, level);
    }

    public BaseFireball(EntityType<T> type, double x, double y, double z, double vx, double vy, double vz, Level level) {
        super(type, x, y, z, vx, vy, vz, level);
    }

    public BaseFireball(EntityType<T> type, LivingEntity owner, double vx, double vy, double vz, Level level) {
        super(type, owner, vx, vy, vz, level);
    }

    @Override
    public final Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected final void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide) {
            this.onHitAction(result.getLocation());
            this.m_146870_();
        }
    }

    @Override
    protected final void onHitEntity(EntityHitResult result) {
        super.m_5790_(result);
        if (!this.m_9236_().isClientSide) {
            this.onHitEntity(result.getEntity());
        }
    }

    @Override
    protected final void onHitBlock(BlockHitResult result) {
        super.m_8060_(result);
        if (!this.m_9236_().isClientSide) {
            this.onHitBlock(result.getBlockPos().relative(result.getDirection()));
        }
    }

    protected void onHitAction(Vec3 pos) {
    }

    protected void onHitEntity(Entity target) {
    }

    protected void onHitBlock(BlockPos pos) {
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide) {
            this.life++;
        }
        if (this.life >= this.lifetime) {
            this.m_146870_();
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public final boolean isPickable() {
        return false;
    }

    @Override
    public final boolean hurt(DamageSource source, float damage) {
        return false;
    }

    @Override
    public float getSize() {
        return 1.0F;
    }
}