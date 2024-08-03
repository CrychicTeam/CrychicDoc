package net.minecraft.client.resources.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class UnderwaterAmbientSoundInstances {

    public static class SubSound extends AbstractTickableSoundInstance {

        private final LocalPlayer player;

        protected SubSound(LocalPlayer localPlayer0, SoundEvent soundEvent1) {
            super(soundEvent1, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.player = localPlayer0;
            this.f_119578_ = false;
            this.f_119579_ = 0;
            this.f_119573_ = 1.0F;
            this.f_119582_ = true;
        }

        @Override
        public void tick() {
            if (this.player.m_213877_() || !this.player.isUnderWater()) {
                this.m_119609_();
            }
        }
    }

    public static class UnderwaterAmbientSoundInstance extends AbstractTickableSoundInstance {

        public static final int FADE_DURATION = 40;

        private final LocalPlayer player;

        private int fade;

        public UnderwaterAmbientSoundInstance(LocalPlayer localPlayer0) {
            super(SoundEvents.AMBIENT_UNDERWATER_LOOP, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.player = localPlayer0;
            this.f_119578_ = true;
            this.f_119579_ = 0;
            this.f_119573_ = 1.0F;
            this.f_119582_ = true;
        }

        @Override
        public void tick() {
            if (!this.player.m_213877_() && this.fade >= 0) {
                if (this.player.isUnderWater()) {
                    this.fade++;
                } else {
                    this.fade -= 2;
                }
                this.fade = Math.min(this.fade, 40);
                this.f_119573_ = Math.max(0.0F, Math.min((float) this.fade / 40.0F, 1.0F));
            } else {
                this.m_119609_();
            }
        }
    }
}