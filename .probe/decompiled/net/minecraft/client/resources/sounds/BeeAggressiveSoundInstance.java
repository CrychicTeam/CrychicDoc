package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Bee;

public class BeeAggressiveSoundInstance extends BeeSoundInstance {

    public BeeAggressiveSoundInstance(Bee bee0) {
        super(bee0, SoundEvents.BEE_LOOP_AGGRESSIVE, SoundSource.NEUTRAL);
        this.f_119579_ = 0;
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new BeeFlyingSoundInstance(this.f_119618_);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return !this.f_119618_.m_21660_();
    }
}