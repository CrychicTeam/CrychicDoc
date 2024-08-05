package com.github.alexthe666.alexsmobs.client.sound;

import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SoundBearMusicBox extends AbstractTickableSoundInstance {

    private final EntityGrizzlyBear bear;

    public SoundBearMusicBox(EntityGrizzlyBear bear) {
        super(AMSoundRegistry.APRIL_FOOLS_MUSIC_BOX.get(), SoundSource.RECORDS, bear.m_217043_());
        this.bear = bear;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119575_ = this.bear.m_20185_();
        this.f_119576_ = this.bear.m_20186_();
        this.f_119577_ = this.bear.m_20189_();
    }

    @Override
    public boolean canPlaySound() {
        return this.bear.getAprilFoolsFlag() == 4 && ClientProxy.BEAR_MUSIC_BOX_SOUND_MAP.get(this.bear.m_19879_()) == this;
    }

    public boolean isOnlyMusicBox() {
        ObjectIterator var1 = ClientProxy.BEAR_MUSIC_BOX_SOUND_MAP.values().iterator();
        while (var1.hasNext()) {
            SoundBearMusicBox s = (SoundBearMusicBox) var1.next();
            if (s != this && this.distanceSq(s.f_119575_, s.f_119576_, s.f_119577_) < 16.0 && s.canPlaySound()) {
                return false;
            }
        }
        return true;
    }

    public double distanceSq(double p_218140_1_, double p_218140_3_, double p_218140_5_) {
        double lvt_10_1_ = this.m_7772_() - p_218140_1_;
        double lvt_12_1_ = this.m_7780_() - p_218140_3_;
        double lvt_14_1_ = this.m_7778_() - p_218140_5_;
        return lvt_10_1_ * lvt_10_1_ + lvt_12_1_ * lvt_12_1_ + lvt_14_1_ * lvt_14_1_;
    }

    @Override
    public void tick() {
        if (!this.bear.m_213877_() && this.bear.m_6084_() && this.bear.getAprilFoolsFlag() == 4) {
            this.f_119573_ = 3.0F;
            this.f_119574_ = 1.0F;
            this.f_119575_ = this.bear.m_20185_();
            this.f_119576_ = this.bear.m_20186_();
            this.f_119577_ = this.bear.m_20189_();
        } else {
            this.m_119609_();
            ClientProxy.BEAR_MUSIC_BOX_SOUND_MAP.remove(this.bear.m_19879_());
        }
    }
}