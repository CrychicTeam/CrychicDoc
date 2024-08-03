package io.redspace.ironsspellbooks.entity.spells.flame_strike;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class FlameStrike extends AoeEntity {

    private static final EntityDataAccessor<Boolean> DATA_MIRRORED = SynchedEntityData.defineId(FlameStrike.class, EntityDataSerializers.BOOLEAN);

    LivingEntity target;

    public final int ticksPerFrame = 2;

    public final int deathTime = 8;

    public FlameStrike(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FlameStrike(Level level, boolean mirrored) {
        this(EntityRegistry.FLAME_STRIKE.get(), level);
        if (mirrored) {
            this.m_20088_().set(DATA_MIRRORED, true);
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    @Override
    public void tick() {
        if (!this.f_19803_) {
            this.checkHits();
            this.f_19803_ = true;
        }
        if (this.f_19797_ >= 8) {
            this.m_146870_();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_MIRRORED, false);
    }

    public boolean isMirrored() {
        return this.m_20088_().get(DATA_MIRRORED);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}