package com.mna.entities.boss.effects;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.Odin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public class Bifrost extends Entity {

    private static final Vec3 DOWN = new Vec3(0.0, -1.0, 0.0);

    private boolean summon = true;

    public Bifrost(EntityType<? extends Bifrost> type, Level world) {
        super(EntityInit.BIFROST.get(), world);
        this.m_20242_(true);
        this.m_6853_(false);
    }

    public Bifrost(Level worldIn, Vec3 position) {
        this(EntityInit.BIFROST.get(), worldIn);
        this.m_6034_(position.x(), position.y(), position.z());
    }

    public void spawnParticles(int particleCount, float partialTick) {
        for (int i = 0; i < particleCount; i++) {
            Vec3 pos;
            if (this.summon) {
                pos = this.m_20182_().add(DOWN.scale((double) partialTick * Math.random()));
            } else {
                pos = this.m_20182_().add(DOWN.scale((double) (-1.0F * partialTick) * Math.random()));
            }
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setColor((float) Math.random() * 255.0F, (float) Math.random() * 255.0F, (float) Math.random() * 255.0F).setScale(0.08F).setMaxAge(50), pos.x, this.summon ? pos.y : pos.y - 10.0, pos.z, 0.6F * Math.random() + 0.1F, -0.2F, 0.5);
            if (this.f_19797_ > 50) {
                Vec3 vel = new Vec3(Math.random() - 0.5, 0.0, Math.random() - 0.5);
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setColor((float) Math.random() * 255.0F, (float) Math.random() * 255.0F, (float) Math.random() * 255.0F).setScale(0.1F), pos.x + vel.x, pos.y - 10.0 + Math.random(), pos.z + vel.z, vel.x, vel.y, vel.z);
            }
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 2 && !this.m_9236_().isClientSide()) {
            this.m_5496_(SFX.Entity.Odin.BIFROST, 1.0F, 1.0F);
        }
        if (this.f_19797_ > 100) {
            if (this.summon && !this.m_9236_().isClientSide()) {
                Odin odin = new Odin(this.m_9236_());
                odin.m_6034_(this.m_20185_(), this.m_20186_() - 10.0, this.m_20189_());
                odin.setupSpawn();
                this.m_9236_().m_7967_(odin);
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.m_6034_(x, y, z);
        this.m_19915_(yaw, pitch);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.m_20191_().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 64.0 * m_20150_();
        return distance < d0 * d0;
    }

    public void spawnParticleBurst(String identifier, Float radius) {
        MAParticleType particle = ParticleInit.AIR_VELOCITY.get();
        int count = 100;
        double angleRads = 0.0;
        double step = (Math.PI * 2) / (double) count;
        for (int i = 0; i < count; i++) {
            angleRads += step;
            Vec3 dir = new Vec3(Math.cos(angleRads), Math.random() * 0.1, Math.sin(angleRads)).normalize();
            Vec3 vel = dir.scale((double) (0.3F * radius));
            dir = dir.scale(0.2);
            this.m_9236_().addParticle(new MAParticleType(particle).setColor((float) Math.random(), (float) Math.random(), (float) Math.random()), this.m_20185_() + dir.x, this.m_20186_() + 1.0 + Math.random(), this.m_20189_() + dir.z, vel.x, vel.y, vel.z);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("summon")) {
            this.summon = nbt.getBoolean("summon");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putBoolean("summon", this.summon);
    }

    public void setNoSummon() {
        this.summon = false;
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return true;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(15.0);
    }
}