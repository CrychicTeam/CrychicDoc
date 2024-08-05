package com.github.alexthe666.alexsmobs.client.sound;

import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SoundLaCucaracha extends AbstractTickableSoundInstance {

    private final EntityCockroach cockroach;

    public SoundLaCucaracha(EntityCockroach cockroach) {
        super(AMSoundRegistry.LA_CUCARACHA.get(), SoundSource.RECORDS, cockroach.m_217043_());
        this.cockroach = cockroach;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119575_ = this.cockroach.m_20185_();
        this.f_119576_ = this.cockroach.m_20186_();
        this.f_119577_ = this.cockroach.m_20189_();
    }

    @Override
    public boolean canPlaySound() {
        return !this.cockroach.m_20067_() && this.cockroach.hasMaracas() && this.cockroach.isDancing() && ClientProxy.COCKROACH_SOUND_MAP.get(this.cockroach.m_19879_()) == this;
    }

    public boolean isOnlyCockroach() {
        ObjectIterator var1 = ClientProxy.COCKROACH_SOUND_MAP.values().iterator();
        while (var1.hasNext()) {
            SoundLaCucaracha cucaracha = (SoundLaCucaracha) var1.next();
            if (cucaracha != this && this.distanceSq(cucaracha.f_119575_, cucaracha.f_119576_, cucaracha.f_119577_) < 16.0 && cucaracha.canPlaySound()) {
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
        if (!this.cockroach.m_213877_() && this.cockroach.m_6084_() && this.cockroach.isDancing() && this.cockroach.hasMaracas()) {
            this.f_119573_ = 1.0F;
            this.f_119574_ = 1.0F;
            this.f_119575_ = this.cockroach.m_20185_();
            this.f_119576_ = this.cockroach.m_20186_();
            this.f_119577_ = this.cockroach.m_20189_();
        } else {
            this.m_119609_();
            ClientProxy.COCKROACH_SOUND_MAP.remove(this.cockroach.m_19879_());
        }
    }
}