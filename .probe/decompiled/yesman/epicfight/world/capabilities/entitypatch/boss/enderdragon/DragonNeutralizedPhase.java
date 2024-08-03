package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;

public class DragonNeutralizedPhase extends PatchedDragonPhase {

    public DragonNeutralizedPhase(EnderDragon dragon) {
        super(dragon);
    }

    @Override
    public void begin() {
        this.dragonpatch.<Animator>getAnimator().playAnimation(Animations.DRAGON_NEUTRALIZED, 0.0F);
        if (this.dragonpatch.isLogicalClient()) {
            Minecraft.getInstance().getSoundManager().stop(EpicFightSounds.ENDER_DRAGON_CRYSTAL_LINK.get().getLocation(), SoundSource.HOSTILE);
            this.f_31176_.m_9236_().addParticle(EpicFightParticles.FORCE_FIELD_END.get(), this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return PatchedPhases.NEUTRALIZED;
    }

    @Override
    public boolean isSitting() {
        return true;
    }
}