package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.Bee;

public class BeeFlyingSoundInstance extends BeeSoundInstance {

    public BeeFlyingSoundInstance(Bee bee0) {
        super(bee0, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new BeeAggressiveSoundInstance(this.f_119618_);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return this.f_119618_.m_21660_();
    }
}