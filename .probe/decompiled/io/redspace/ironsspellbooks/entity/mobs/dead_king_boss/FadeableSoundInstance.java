package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class FadeableSoundInstance extends AbstractTickableSoundInstance {

    boolean starting = false;

    private int transitionTicks;

    private boolean triggerEnd = false;

    private static final int START_TRANSITION_TIME = 40;

    private static final int END_TRANSITION_TIME = 40;

    protected FadeableSoundInstance(SoundEvent soundEvent, SoundSource source, boolean loop) {
        super(soundEvent, source, SoundInstance.createUnseededRandom());
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119578_ = loop;
        this.f_119579_ = 0;
        this.f_119573_ = 1.0F;
        this.starting = false;
    }

    @Override
    public void tick() {
        if (this.transitionTicks > 0) {
            this.transitionTicks--;
        }
        if (this.starting) {
            this.f_119573_ = 1.0F - (float) this.transitionTicks / 40.0F;
            if (this.transitionTicks == 0) {
                this.starting = false;
            }
        }
        if (this.triggerEnd) {
            this.f_119573_ = (float) this.transitionTicks / 40.0F;
            if (this.transitionTicks == 0) {
                this.m_119609_();
            }
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public void triggerStop() {
        this.triggerEnd = true;
        if (this.f_119573_ < 1.0F) {
            this.transitionTicks = (int) (40.0F * this.f_119573_);
        } else {
            this.transitionTicks = 40;
        }
    }

    public void triggerStart() {
        this.triggerEnd = false;
        if (this.f_119573_ < 1.0F) {
            this.transitionTicks = (int) (40.0F * this.f_119573_);
        } else {
            this.transitionTicks = 40;
        }
        this.starting = true;
    }
}