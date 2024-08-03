package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class RidingMinecartSoundInstance extends AbstractTickableSoundInstance {

    private static final float VOLUME_MIN = 0.0F;

    private static final float VOLUME_MAX = 0.75F;

    private final Player player;

    private final AbstractMinecart minecart;

    private final boolean underwaterSound;

    public RidingMinecartSoundInstance(Player player0, AbstractMinecart abstractMinecart1, boolean boolean2) {
        super(boolean2 ? SoundEvents.MINECART_INSIDE_UNDERWATER : SoundEvents.MINECART_INSIDE, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.player = player0;
        this.minecart = abstractMinecart1;
        this.underwaterSound = boolean2;
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
    }

    @Override
    public boolean canPlaySound() {
        return !this.minecart.m_20067_();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.minecart.m_213877_() || !this.player.m_20159_() || this.player.m_20202_() != this.minecart) {
            this.m_119609_();
        } else if (this.underwaterSound != this.player.m_5842_()) {
            this.f_119573_ = 0.0F;
        } else {
            float $$0 = (float) this.minecart.m_20184_().horizontalDistance();
            if ($$0 >= 0.01F) {
                this.f_119573_ = Mth.clampedLerp(0.0F, 0.75F, $$0);
            } else {
                this.f_119573_ = 0.0F;
            }
        }
    }
}