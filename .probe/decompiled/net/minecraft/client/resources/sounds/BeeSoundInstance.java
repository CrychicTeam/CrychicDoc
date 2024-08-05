package net.minecraft.client.resources.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Bee;

public abstract class BeeSoundInstance extends AbstractTickableSoundInstance {

    private static final float VOLUME_MIN = 0.0F;

    private static final float VOLUME_MAX = 1.2F;

    private static final float PITCH_MIN = 0.0F;

    protected final Bee bee;

    private boolean hasSwitched;

    public BeeSoundInstance(Bee bee0, SoundEvent soundEvent1, SoundSource soundSource2) {
        super(soundEvent1, soundSource2, SoundInstance.createUnseededRandom());
        this.bee = bee0;
        this.f_119575_ = (double) ((float) bee0.m_20185_());
        this.f_119576_ = (double) ((float) bee0.m_20186_());
        this.f_119577_ = (double) ((float) bee0.m_20189_());
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
    }

    @Override
    public void tick() {
        boolean $$0 = this.shouldSwitchSounds();
        if ($$0 && !this.m_7801_()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(this.getAlternativeSoundInstance());
            this.hasSwitched = true;
        }
        if (!this.bee.m_213877_() && !this.hasSwitched) {
            this.f_119575_ = (double) ((float) this.bee.m_20185_());
            this.f_119576_ = (double) ((float) this.bee.m_20186_());
            this.f_119577_ = (double) ((float) this.bee.m_20189_());
            float $$1 = (float) this.bee.m_20184_().horizontalDistance();
            if ($$1 >= 0.01F) {
                this.f_119574_ = Mth.lerp(Mth.clamp($$1, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.f_119573_ = Mth.lerp(Mth.clamp($$1, 0.0F, 0.5F), 0.0F, 1.2F);
            } else {
                this.f_119574_ = 0.0F;
                this.f_119573_ = 0.0F;
            }
        } else {
            this.m_119609_();
        }
    }

    private float getMinPitch() {
        return this.bee.m_6162_() ? 1.1F : 0.7F;
    }

    private float getMaxPitch() {
        return this.bee.m_6162_() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.bee.m_20067_();
    }

    protected abstract AbstractTickableSoundInstance getAlternativeSoundInstance();

    protected abstract boolean shouldSwitchSounds();
}