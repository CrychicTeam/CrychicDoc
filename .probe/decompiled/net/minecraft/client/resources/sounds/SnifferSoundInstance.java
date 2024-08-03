package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

public class SnifferSoundInstance extends AbstractTickableSoundInstance {

    private static final float VOLUME = 1.0F;

    private static final float PITCH = 1.0F;

    private final Sniffer sniffer;

    public SnifferSoundInstance(Sniffer sniffer0) {
        super(SoundEvents.SNIFFER_DIGGING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.sniffer = sniffer0;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = false;
        this.f_119579_ = 0;
    }

    @Override
    public boolean canPlaySound() {
        return !this.sniffer.m_20067_();
    }

    @Override
    public void tick() {
        if (!this.sniffer.m_213877_() && this.sniffer.m_5448_() == null && this.sniffer.canPlayDiggingSound()) {
            this.f_119575_ = (double) ((float) this.sniffer.m_20185_());
            this.f_119576_ = (double) ((float) this.sniffer.m_20186_());
            this.f_119577_ = (double) ((float) this.sniffer.m_20189_());
            this.f_119573_ = 1.0F;
            this.f_119574_ = 1.0F;
        } else {
            this.m_119609_();
        }
    }
}