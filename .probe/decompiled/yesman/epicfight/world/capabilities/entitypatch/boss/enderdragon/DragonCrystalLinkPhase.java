package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class DragonCrystalLinkPhase extends PatchedDragonPhase {

    public static final float STUN_SHIELD_AMOUNT = 20.0F;

    public static final int CHARGING_TICK = 158;

    private int chargingCount;

    private EndCrystal linkingCrystal;

    public DragonCrystalLinkPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void begin() {
        this.dragonpatch.<Animator>getAnimator().playAnimation(Animations.DRAGON_CRYSTAL_LINK, 0.0F);
        this.f_31176_.m_9236_().playLocalSound(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), EpicFightSounds.ENDER_DRAGON_CRYSTAL_LINK.get(), this.f_31176_.getSoundSource(), 10.0F, 1.0F, false);
        BlockPos blockpos = this.f_31176_.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.getLocation(new BlockPos(0, 0, 0)));
        List<EndCrystal> list = this.f_31176_.m_9236_().m_45976_(EndCrystal.class, new AABB(blockpos).inflate(200.0));
        EndCrystal nearestCrystal = null;
        double d0 = Double.MAX_VALUE;
        for (EndCrystal endcrystal : list) {
            double d1 = endcrystal.m_20280_(this.f_31176_);
            if (d1 < d0) {
                d0 = d1;
                nearestCrystal = endcrystal;
            }
        }
        this.linkingCrystal = nearestCrystal;
        this.chargingCount = 158;
        if (this.dragonpatch.isLogicalClient()) {
            double x = -45.0;
            double z = 0.0;
            Vec3 correction = this.f_31176_.m_20154_().multiply(2.0, 0.0, 2.0).subtract(0.0, 2.0, 0.0);
            Vec3 spawnPosition = this.f_31176_.m_20182_().subtract(correction);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    this.f_31176_.m_9236_().addAlwaysVisibleParticle(EpicFightParticles.FORCE_FIELD.get(), spawnPosition.x, spawnPosition.y, spawnPosition.z, x + 90.0 * (double) i, Double.longBitsToDouble((long) this.f_31176_.m_19879_()), z + 90.0 * (double) j);
                }
            }
        } else if (!this.dragonpatch.isLogicalClient()) {
            int shieldCorrection = this.getPlayersNearbyWithin(100.0).size() - 1;
            float stunShield = 20.0F + 15.0F * (float) shieldCorrection;
            this.dragonpatch.setMaxStunShield(stunShield);
            this.dragonpatch.setStunShield(stunShield);
        }
    }

    @Override
    public void end() {
        BlockPos blockpos = this.linkingCrystal.m_20183_();
        this.f_31176_.nearestCrystal = null;
        this.linkingCrystal = null;
        if (!this.dragonpatch.isLogicalClient()) {
            this.f_31176_.m_9236_().explode(null, (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), 6.0F, Level.ExplosionInteraction.BLOCK);
        }
    }

    @Override
    public float onHurt(DamageSource damagesource, float amount) {
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(damagesource.getEntity(), LivingEntityPatch.class);
        if (entitypatch != null && entitypatch.getEpicFightDamageSource() != null) {
            float impact = entitypatch.getEpicFightDamageSource().getImpact();
            this.dragonpatch.setStunShield(this.dragonpatch.getStunShield() - impact);
        }
        return amount;
    }

    @Override
    public void doClientTick() {
        super.doClientTick();
        this.f_31176_.growlTime = 200;
        this.chargingCount--;
        this.f_31176_.nearestCrystal = this.linkingCrystal;
    }

    @Override
    public void doServerTick() {
        this.chargingCount--;
        this.f_31176_.f_21363_ = 0;
        if (this.chargingCount > 0) {
            this.f_31176_.m_21153_(this.f_31176_.m_21223_() + 0.5F);
        }
    }

    public int getChargingCount() {
        return this.chargingCount;
    }

    public EndCrystal getLinkingCrystal() {
        return this.linkingCrystal;
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return PatchedPhases.CRYSTAL_LINK;
    }
}