package net.minecraft.client.resources.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class EntityBoundSoundInstance extends AbstractTickableSoundInstance {

    private final Entity entity;

    public EntityBoundSoundInstance(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3, Entity entity4, long long5) {
        super(soundEvent0, soundSource1, RandomSource.create(long5));
        this.f_119573_ = float2;
        this.f_119574_ = float3;
        this.entity = entity4;
        this.f_119575_ = (double) ((float) this.entity.getX());
        this.f_119576_ = (double) ((float) this.entity.getY());
        this.f_119577_ = (double) ((float) this.entity.getZ());
    }

    @Override
    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }

    @Override
    public void tick() {
        if (this.entity.isRemoved()) {
            this.m_119609_();
        } else {
            this.f_119575_ = (double) ((float) this.entity.getX());
            this.f_119576_ = (double) ((float) this.entity.getY());
            this.f_119577_ = (double) ((float) this.entity.getZ());
        }
    }
}