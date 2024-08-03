package net.minecraft.client.resources.sounds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class ElytraOnPlayerSoundInstance extends AbstractTickableSoundInstance {

    public static final int DELAY = 20;

    private final LocalPlayer player;

    private int time;

    public ElytraOnPlayerSoundInstance(LocalPlayer localPlayer0) {
        super(SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = localPlayer0;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.1F;
    }

    @Override
    public void tick() {
        this.time++;
        if (!this.player.m_213877_() && (this.time <= 20 || this.player.m_21255_())) {
            this.f_119575_ = (double) ((float) this.player.m_20185_());
            this.f_119576_ = (double) ((float) this.player.m_20186_());
            this.f_119577_ = (double) ((float) this.player.m_20189_());
            float $$0 = (float) this.player.m_20184_().lengthSqr();
            if ((double) $$0 >= 1.0E-7) {
                this.f_119573_ = Mth.clamp($$0 / 4.0F, 0.0F, 1.0F);
            } else {
                this.f_119573_ = 0.0F;
            }
            if (this.time < 20) {
                this.f_119573_ = 0.0F;
            } else if (this.time < 40) {
                this.f_119573_ = this.f_119573_ * ((float) (this.time - 20) / 20.0F);
            }
            float $$1 = 0.8F;
            if (this.f_119573_ > 0.8F) {
                this.f_119574_ = 1.0F + (this.f_119573_ - 0.8F);
            } else {
                this.f_119574_ = 1.0F;
            }
        } else {
            this.m_119609_();
        }
    }
}