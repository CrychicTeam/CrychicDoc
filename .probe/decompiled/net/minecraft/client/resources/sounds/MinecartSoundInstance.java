package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class MinecartSoundInstance extends AbstractTickableSoundInstance {

    private static final float VOLUME_MIN = 0.0F;

    private static final float VOLUME_MAX = 0.7F;

    private static final float PITCH_MIN = 0.0F;

    private static final float PITCH_MAX = 1.0F;

    private static final float PITCH_DELTA = 0.0025F;

    private final AbstractMinecart minecart;

    private float pitch = 0.0F;

    public MinecartSoundInstance(AbstractMinecart abstractMinecart0) {
        super(SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.minecart = abstractMinecart0;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
        this.f_119575_ = (double) ((float) abstractMinecart0.m_20185_());
        this.f_119576_ = (double) ((float) abstractMinecart0.m_20186_());
        this.f_119577_ = (double) ((float) abstractMinecart0.m_20189_());
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
        if (this.minecart.m_213877_()) {
            this.m_119609_();
        } else {
            this.f_119575_ = (double) ((float) this.minecart.m_20185_());
            this.f_119576_ = (double) ((float) this.minecart.m_20186_());
            this.f_119577_ = (double) ((float) this.minecart.m_20189_());
            float $$0 = (float) this.minecart.m_20184_().horizontalDistance();
            if ($$0 >= 0.01F) {
                this.pitch = Mth.clamp(this.pitch + 0.0025F, 0.0F, 1.0F);
                this.f_119573_ = Mth.lerp(Mth.clamp($$0, 0.0F, 0.5F), 0.0F, 0.7F);
            } else {
                this.pitch = 0.0F;
                this.f_119573_ = 0.0F;
            }
        }
    }
}