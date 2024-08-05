package yesman.epicfight.world.capabilities.entitypatch.boss;

import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.WitherGhostClone;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public class WitherGhostPatch extends MobPatch<WitherGhostClone> {

    public void onJoinWorld(WitherGhostClone original, EntityJoinLevelEvent event) {
        super.onJoinWorld(original, event);
        if (this.original.m_21515_()) {
            this.playAnimationSynchronized(Animations.WITHER_CHARGE, 0.0F);
        }
        if (this.isLogicalClient()) {
            this.playSound(SoundEvents.WITHER_AMBIENT, -0.1F, 0.1F);
        }
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.WITHER_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.WITHER_IDLE);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue(3.0);
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        this.currentLivingMotion = LivingMotions.IDLE;
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return null;
    }
}