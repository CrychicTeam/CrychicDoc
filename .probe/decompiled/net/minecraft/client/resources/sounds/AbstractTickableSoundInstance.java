package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public abstract class AbstractTickableSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {

    private boolean stopped;

    protected AbstractTickableSoundInstance(SoundEvent soundEvent0, SoundSource soundSource1, RandomSource randomSource2) {
        super(soundEvent0, soundSource1, randomSource2);
    }

    @Override
    public boolean isStopped() {
        return this.stopped;
    }

    protected final void stop() {
        this.stopped = true;
        this.f_119578_ = false;
    }
}