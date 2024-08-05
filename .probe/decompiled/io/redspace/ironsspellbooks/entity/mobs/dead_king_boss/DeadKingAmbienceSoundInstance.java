package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DeadKingAmbienceSoundInstance extends AbstractTickableSoundInstance {

    public static final int rangeSqr = 2304;

    public static final int maxVolumeRangeSqr = 324;

    private static final float END_TRANSITION_TIME = 0.01F;

    final Vec3 vec3;

    boolean ending = false;

    boolean triggerEnd = false;

    protected DeadKingAmbienceSoundInstance(Vec3 vec3) {
        super(SoundRegistry.DEAD_KING_AMBIENCE.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.f_119580_ = SoundInstance.Attenuation.NONE;
        this.f_119578_ = true;
        this.f_119579_ = 0;
        this.f_119573_ = 0.0F;
        this.vec3 = vec3;
    }

    @Override
    public void tick() {
        if (this.triggerEnd) {
            if (!this.ending) {
                this.ending = true;
            }
            this.f_119573_ -= 0.01F;
        } else {
            MinecraftInstanceHelper.ifPlayerPresent(player -> {
                double d = player.m_20238_(this.vec3);
                this.f_119573_ = 1.0F - (float) Mth.clamp((d - 324.0) / 2304.0, 0.0, 1.0);
            });
        }
        if (this.f_119573_ <= 0.0F) {
            this.m_119609_();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public void triggerStop() {
        this.triggerEnd = true;
    }
}