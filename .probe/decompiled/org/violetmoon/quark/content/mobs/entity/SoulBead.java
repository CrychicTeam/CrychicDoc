package org.violetmoon.quark.content.mobs.entity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.violetmoon.quark.base.handler.QuarkSounds;

public class SoulBead extends Entity {

    private static final EntityDataAccessor<Integer> TARGET_X = SynchedEntityData.defineId(SoulBead.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TARGET_Z = SynchedEntityData.defineId(SoulBead.class, EntityDataSerializers.INT);

    private int liveTicks = 0;

    private static final String TAG_TARGET_X = "targetX";

    private static final String TAG_TARGET_Z = "targetZ";

    public SoulBead(EntityType<? extends SoulBead> type, Level worldIn) {
        super(type, worldIn);
    }

    public void setTarget(int x, int z) {
        this.f_19804_.set(TARGET_X, x);
        this.f_19804_.set(TARGET_Z, z);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(TARGET_X, 0);
        this.f_19804_.define(TARGET_Z, 0);
    }

    @Override
    public void tick() {
        super.tick();
        double posSpread = 0.4;
        double scale = 0.08;
        double rotateSpread = 1.5;
        double rise = 0.025;
        int maxLiveTime = 6000;
        int particles = 20;
        double trigArg = (double) this.liveTicks * 0.32;
        if (maxLiveTime - this.liveTicks < particles) {
            particles = maxLiveTime - this.liveTicks;
        }
        double posX = this.m_20185_();
        double posY = this.m_20186_();
        double posZ = this.m_20189_();
        Vec3 vec = new Vec3((double) this.f_19804_.get(TARGET_X).intValue(), posY, (double) this.f_19804_.get(TARGET_Z).intValue()).subtract(posX, posY, posZ).normalize().scale(scale);
        double bpx = posX + vec.x * (double) this.liveTicks + Math.cos(trigArg) * rotateSpread;
        double bpy = posY + vec.y * (double) this.liveTicks + (double) this.liveTicks * rise;
        double bpz = posZ + vec.z * (double) this.liveTicks + Math.sin(trigArg) * rotateSpread;
        for (int i = 0; i < particles; i++) {
            double px = bpx + (Math.random() - 0.5) * posSpread;
            double py = bpy + (Math.random() - 0.5) * posSpread;
            double pz = bpz + (Math.random() - 0.5) * posSpread;
            this.m_9236_().addParticle(new DustParticleOptions(new Vector3f(0.2F, 0.12F, 0.1F), 1.0F), px, py, pz, 0.0, 0.0, 0.0);
            if (Math.random() < 0.05) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SOUL_SAND.defaultBlockState()), px, py, pz, 0.0, 0.0, 0.0);
            }
        }
        if (Math.random() < 0.1) {
            this.m_9236_().playSound(null, bpx, bpy, bpz, QuarkSounds.ENTITY_SOUL_BEAD_IDLE, SoundSource.PLAYERS, 0.2F, 1.0F);
        }
        this.liveTicks++;
        if (this.liveTicks > maxLiveTime) {
            this.m_6089_();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        this.f_19804_.set(TARGET_X, compound.getInt("targetX"));
        this.f_19804_.set(TARGET_Z, compound.getInt("targetZ"));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
        compound.putInt("targetX", this.f_19804_.get(TARGET_X));
        compound.putInt("targetZ", this.f_19804_.get(TARGET_Z));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}