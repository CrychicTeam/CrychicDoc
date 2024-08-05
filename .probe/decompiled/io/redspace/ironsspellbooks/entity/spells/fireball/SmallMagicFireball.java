package io.redspace.ironsspellbooks.entity.spells.fireball;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class SmallMagicFireball extends AbstractMagicProjectile implements IEntityAdditionalSpawnData {

    @Nullable
    Entity cachedHomingTarget;

    @Nullable
    UUID homingTargetUUID;

    public SmallMagicFireball(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(true);
    }

    public SmallMagicFireball(Level pLevel, LivingEntity pShooter) {
        this(EntityRegistry.SMALL_FIREBALL_PROJECTILE.get(), pLevel);
        this.m_5602_(pShooter);
    }

    public void shoot(Vec3 rotation, float inaccuracy) {
        double speed = rotation.length();
        Vec3 offset = Utils.getRandomVec3(1.0).normalize().scale((double) inaccuracy);
        Vec3 motion = rotation.normalize().add(offset).normalize().scale(speed);
        super.shoot(motion);
    }

    @Nullable
    public Entity getHomingTarget() {
        if (this.cachedHomingTarget != null && !this.cachedHomingTarget.isRemoved()) {
            return this.cachedHomingTarget;
        } else if (this.homingTargetUUID != null && this.f_19853_ instanceof ServerLevel) {
            this.cachedHomingTarget = ((ServerLevel) this.f_19853_).getEntity(this.homingTargetUUID);
            return this.cachedHomingTarget;
        } else {
            return null;
        }
    }

    public void setHomingTarget(LivingEntity entity) {
        this.homingTargetUUID = entity.m_20148_();
        this.cachedHomingTarget = entity;
    }

    @Override
    public void tick() {
        super.tick();
        Entity homingTarget = this.getHomingTarget();
        if (homingTarget != null && !this.doHomingTowards(homingTarget)) {
            this.homingTargetUUID = null;
            this.cachedHomingTarget = null;
        }
    }

    private boolean doHomingTowards(Entity entity) {
        if (entity.isRemoved()) {
            return false;
        } else {
            Vec3 motion = this.m_20184_();
            double speed = this.m_20184_().length();
            Vec3 delta = entity.getBoundingBox().getCenter().subtract(this.m_20182_()).add(entity.getDeltaMovement());
            float f = 0.08F;
            Vec3 newMotion = new Vec3(Mth.lerp((double) f, motion.x, delta.x), Mth.lerp((double) f, motion.y, delta.y), Mth.lerp((double) f, motion.z, delta.z)).normalize().scale(speed);
            this.m_20256_(newMotion);
            return this.f_19797_ <= 10 || !(newMotion.dot(delta) < 0.0);
        }
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() - vec3.x;
        double d1 = this.m_20186_() - vec3.y;
        double d2 = this.m_20189_() - vec3.z;
        int count = Mth.clamp((int) (vec3.lengthSqr() * 4.0), 1, 5);
        for (int i = 0; i < count; i++) {
            Vec3 random = Utils.getRandomVec3(0.1);
            float f = (float) i / (float) count;
            double x = Mth.lerp((double) f, d0, this.m_20185_());
            double y = Mth.lerp((double) f, d1, this.m_20186_());
            double z = Mth.lerp((double) f, d2, this.m_20189_());
            this.f_19853_.addParticle(ParticleHelper.EMBERS, x - random.x, y + 0.5 - random.y, z - random.z, random.x * 0.5, random.y * 0.5, random.z * 0.5);
        }
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 1.85F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (!this.f_19853_.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.m_19749_();
            DamageSources.applyDamage(target, this.damage, SpellRegistry.BLAZE_STORM_SPELL.get().getDamageSource(this, owner));
            if (target.getUUID().equals(this.homingTargetUUID)) {
                target.invulnerableTime = 0;
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.m_8060_(pResult);
        if (!this.f_19853_.isClientSide && ServerConfigs.SPELL_GREIFING.get()) {
            BlockPos blockpos = pResult.getBlockPos().relative(pResult.getDirection());
            if (this.f_19853_.m_46859_(blockpos)) {
                this.f_19853_.setBlockAndUpdate(blockpos, BaseFireBlock.getState(this.f_19853_, blockpos));
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.f_19853_.isClientSide) {
            this.m_146870_();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.homingTargetUUID != null) {
            tag.putUUID("homingTarget", this.homingTargetUUID);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("homingTarget", 11)) {
            this.homingTargetUUID = tag.getUUID("homingTarget");
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        IronsSpellbooks.LOGGER.debug("Smallmagicfireball.writespawndata: {}", this.homingTargetUUID);
        Entity owner = this.m_19749_();
        buffer.writeInt(owner == null ? 0 : owner.getId());
        Entity homingTarget = this.getHomingTarget();
        buffer.writeInt(homingTarget == null ? 0 : homingTarget.getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        Entity owner = this.f_19853_.getEntity(additionalData.readInt());
        if (owner != null) {
            this.m_5602_(owner);
        }
        Entity homingTarget = this.f_19853_.getEntity(additionalData.readInt());
        if (homingTarget != null) {
            this.cachedHomingTarget = homingTarget;
            this.homingTargetUUID = homingTarget.getUUID();
        }
        IronsSpellbooks.LOGGER.debug("Smallmagicfireball.readSpawnData: {}", this.homingTargetUUID);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}