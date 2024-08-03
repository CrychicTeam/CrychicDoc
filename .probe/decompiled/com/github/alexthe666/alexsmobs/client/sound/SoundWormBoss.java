package com.github.alexthe666.alexsmobs.client.sound;

import com.github.alexthe666.alexsmobs.ClientProxy;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class SoundWormBoss extends AbstractTickableSoundInstance {

    private final EntityVoidWorm voidWorm;

    private int ticksExisted = 0;

    public SoundWormBoss(EntityVoidWorm worm) {
        super(AMSoundRegistry.MUSIC_WORMBOSS.get(), SoundSource.RECORDS, worm.m_217043_());
        this.voidWorm = worm;
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119575_ = this.voidWorm.m_20185_();
        this.f_119576_ = this.voidWorm.m_20186_();
        this.f_119577_ = this.voidWorm.m_20189_();
    }

    @Override
    public boolean canPlaySound() {
        return !this.voidWorm.m_20067_() && ClientProxy.WORMBOSS_SOUND_MAP.get(this.voidWorm.m_19879_()) == this;
    }

    public boolean isNearest() {
        float dist = 400.0F;
        ObjectIterator var2 = ClientProxy.WORMBOSS_SOUND_MAP.values().iterator();
        while (var2.hasNext()) {
            SoundWormBoss wormBoss = (SoundWormBoss) var2.next();
            if (wormBoss != this && this.distanceSq(wormBoss.f_119575_, wormBoss.f_119576_, wormBoss.f_119577_) < (double) (dist * dist) && wormBoss.canPlaySound()) {
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
        if (this.ticksExisted % 100 == 0) {
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
        if (!this.voidWorm.m_213877_() && this.voidWorm.m_6084_()) {
            this.f_119573_ = 1.0F;
            this.f_119574_ = 1.0F;
            this.f_119575_ = this.voidWorm.m_20185_();
            this.f_119576_ = this.voidWorm.m_20186_();
            this.f_119577_ = this.voidWorm.m_20189_();
        } else {
            this.m_119609_();
            ClientProxy.WORMBOSS_SOUND_MAP.remove(this.voidWorm.m_19879_());
        }
        this.ticksExisted++;
    }
}