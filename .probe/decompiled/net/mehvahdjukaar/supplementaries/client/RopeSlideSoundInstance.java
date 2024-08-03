package net.mehvahdjukaar.supplementaries.client;

import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class RopeSlideSoundInstance extends AbstractTickableSoundInstance {

    private final Player player;

    private int ropeTicks;

    public RopeSlideSoundInstance(Player player) {
        super((SoundEvent) ModSounds.ROPE_SLIDE.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.f_119575_ = this.player.m_20185_();
        this.f_119576_ = this.player.m_20186_();
        this.f_119577_ = this.player.m_20189_();
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
        this.ropeTicks = 0;
    }

    @Override
    public void tick() {
        if (!this.player.m_213877_()) {
            if (this.player.m_6147_() && (Boolean) CommonConfigs.Functional.ROPE_SLIDE.get()) {
                BlockState b = this.player.m_146900_();
                if (b.m_204336_(ModTags.FAST_FALL_ROPES)) {
                    this.f_119575_ = this.player.m_20185_();
                    this.f_119576_ = this.player.m_20186_();
                    this.f_119577_ = this.player.m_20189_();
                    float downwardSpeed = -((float) this.player.m_20184_().y);
                    float minPitch = 0.7F;
                    float maxPitch = 2.0F;
                    float speedScaling = 0.5F;
                    float newPitch = Mth.clamp(0.5F + downwardSpeed * speedScaling, 0.0F, maxPitch);
                    if (newPitch >= minPitch) {
                        this.ropeTicks++;
                        float minVolume = 0.0F;
                        float maxVolume = 1.0F;
                        float volumeScaling = 0.07F;
                        this.f_119574_ = newPitch;
                        this.f_119573_ = Mth.clamp((float) this.ropeTicks * volumeScaling, minVolume, maxVolume);
                        return;
                    }
                }
            }
            this.f_119574_ = 0.0F;
            this.f_119573_ = 0.0F;
            this.ropeTicks = 0;
        } else {
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.player.m_20067_();
    }
}