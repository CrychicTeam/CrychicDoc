package com.mna.sound;

import com.mna.tools.math.MathUtils;
import java.util.function.Predicate;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class PredicateLoopingSound extends AbstractTickableSoundInstance {

    private final Predicate<String> predicate;

    private final String id;

    public PredicateLoopingSound(SoundEvent soundIn, String id, Predicate<String> predicate) {
        super(soundIn, SoundSource.PLAYERS, RandomSource.createThreadSafe());
        this.predicate = predicate;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.id = id;
    }

    public PredicateLoopingSound setPosition(BlockPos pos) {
        this.f_119575_ = (double) pos.m_123341_();
        this.f_119576_ = (double) pos.m_123342_();
        this.f_119577_ = (double) pos.m_123343_();
        return this;
    }

    public PredicateLoopingSound setVolume(float volume) {
        this.f_119573_ = volume;
        return this;
    }

    @Override
    public void tick() {
        if (this.predicate == null || !this.predicate.test(this.id)) {
            this.f_119573_ = MathUtils.clamp01(this.f_119573_ - 0.1F);
            if (this.f_119573_ <= 0.0F) {
                this.m_119609_();
            }
        }
    }
}