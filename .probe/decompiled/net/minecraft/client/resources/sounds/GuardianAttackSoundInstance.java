package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Guardian;

public class GuardianAttackSoundInstance extends AbstractTickableSoundInstance {

    private static final float VOLUME_MIN = 0.0F;

    private static final float VOLUME_SCALE = 1.0F;

    private static final float PITCH_MIN = 0.7F;

    private static final float PITCH_SCALE = 0.5F;

    private final Guardian guardian;

    public GuardianAttackSoundInstance(Guardian guardian0) {
        super(SoundEvents.GUARDIAN_ATTACK, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.guardian = guardian0;
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119578_ = true;
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.guardian.m_20067_();
    }

    @Override
    public void tick() {
        if (!this.guardian.m_213877_() && this.guardian.m_5448_() == null) {
            this.f_119575_ = (double) ((float) this.guardian.m_20185_());
            this.f_119576_ = (double) ((float) this.guardian.m_20186_());
            this.f_119577_ = (double) ((float) this.guardian.m_20189_());
            float $$0 = this.guardian.getAttackAnimationScale(0.0F);
            this.f_119573_ = 0.0F + 1.0F * $$0 * $$0;
            this.f_119574_ = 0.7F + 0.5F * $$0;
        } else {
            this.m_119609_();
        }
    }
}