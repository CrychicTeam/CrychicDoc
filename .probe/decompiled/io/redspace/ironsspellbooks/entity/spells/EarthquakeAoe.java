package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class EarthquakeAoe extends AoeEntity implements AntiMagicSusceptible {

    public static Map<UUID, EarthquakeAoe> clientEarthquakeOrigins = new HashMap();

    private CameraShakeData cameraShakeData;

    private int slownessAmplifier;

    int waveAnim = -1;

    public EarthquakeAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.reapplicationDelay = 25;
        this.setCircular();
    }

    public EarthquakeAoe(Level level) {
        this(EntityRegistry.EARTHQUAKE_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        SpellDamageSource damageSource = SpellRegistry.EARTHQUAKE_SPELL.get().getDamageSource(this, this.m_19749_());
        DamageSources.ignoreNextKnockback(target);
        if (target.hurt(damageSource, this.getDamage())) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, this.slownessAmplifier));
            target.m_20256_(target.m_20184_().add(0.0, 0.5, 0.0));
            target.f_19864_ = true;
        }
    }

    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }

    public void setSlownessAmplifier(int slownessAmplifier) {
        this.slownessAmplifier = slownessAmplifier;
    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ == 1) {
            this.createScreenShake();
        }
        if (this.f_19797_ % 20 == 1) {
            this.m_5496_(SoundRegistry.EARTHQUAKE_LOOP.get(), 2.0F, 0.9F + this.f_19796_.nextFloat() * 0.15F);
        }
        if (this.f_19797_ % this.reapplicationDelay == 1) {
            this.waveAnim = 0;
            this.m_5496_(SoundRegistry.EARTHQUAKE_IMPACT.get(), 1.5F, 0.9F + this.f_19796_.nextFloat() * 0.2F);
        }
        if (!this.f_19853_.isClientSide) {
            float radius = this.getRadius();
            Level level = this.f_19853_;
            int intensity = Math.min((int) (radius * radius * 0.09F), 15);
            for (int i = 0; i < intensity; i++) {
                Vec3 vec3 = this.m_20182_().add(this.uniformlyDistributedPointInRadius(radius));
                BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, vec3, 4)).below();
                Utils.createTremorBlock(level, blockPos, 0.1F + this.f_19796_.nextFloat() * 0.2F);
            }
            if (this.waveAnim >= 0) {
                float circumference = (float) (this.waveAnim * 2) * 3.14F;
                int blocks = Mth.clamp((int) circumference, 0, 25);
                float anglePerBlock = 360.0F / (float) blocks;
                for (int i = 0; i < blocks; i++) {
                    Vec3 vec3 = new Vec3((double) ((float) this.waveAnim * Mth.cos(anglePerBlock * (float) i)), 0.0, (double) ((float) this.waveAnim * Mth.sin(anglePerBlock * (float) i)));
                    BlockPos blockPos = BlockPos.containing(Utils.moveToRelativeGroundLevel(level, this.m_20182_().add(vec3), 4)).below();
                    Utils.createTremorBlock(level, blockPos, 0.1F + this.f_19796_.nextFloat() * 0.2F);
                }
                if ((float) (this.waveAnim++) >= radius) {
                    this.waveAnim = -1;
                    if (this.f_19797_ + this.reapplicationDelay >= this.duration) {
                        this.m_146870_();
                    }
                }
            }
        }
    }

    @Override
    protected boolean canHitTargetForGroundContext(LivingEntity target) {
        return true;
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0.0, 5.0, 0.0);
    }

    protected void createScreenShake() {
        if (!this.f_19853_.isClientSide && !this.m_213877_()) {
            this.cameraShakeData = new CameraShakeData(this.duration - this.f_19797_, this.m_20182_(), 15.0F);
            CameraShakeManager.addCameraShake(this.cameraShakeData);
        }
    }

    protected Vec3 uniformlyDistributedPointInRadius(float r) {
        float distance = r * (1.0F - this.f_19796_.nextFloat() * this.f_19796_.nextFloat());
        float theta = this.f_19796_.nextFloat() * 6.282F;
        return new Vec3((double) (distance * Mth.cos(theta)), 0.2F, (double) (distance * Mth.sin(theta)));
    }

    @Override
    public void remove(Entity.RemovalReason pReason) {
        super.m_142687_(pReason);
        if (!this.f_19853_.isClientSide) {
            CameraShakeManager.removeCameraShake(this.cameraShakeData);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 3.0F);
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.m_146870_();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Slowness", this.slownessAmplifier);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.slownessAmplifier = pCompound.getInt("Slowness");
        this.createScreenShake();
    }
}