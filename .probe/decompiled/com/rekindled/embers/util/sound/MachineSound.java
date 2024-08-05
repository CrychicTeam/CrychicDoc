package com.rekindled.embers.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MachineSound extends AbstractTickableSoundInstance {

    protected BlockEntity boundTile;

    protected boolean donePlaying;

    protected int id;

    public MachineSound(BlockEntity tile, int id, SoundEvent soundIn, SoundSource categoryIn, boolean repeatIn, float volumeIn, float pitchIn, float xIn, float yIn, float zIn) {
        super(soundIn, categoryIn, tile.getLevel().getRandom());
        this.boundTile = tile;
        this.id = id;
        this.f_119573_ = volumeIn;
        this.f_119574_ = pitchIn;
        this.f_119575_ = (double) xIn;
        this.f_119576_ = (double) yIn;
        this.f_119577_ = (double) zIn;
        this.f_119578_ = repeatIn;
        this.f_119580_ = SoundInstance.Attenuation.LINEAR;
    }

    @Override
    public boolean isStopped() {
        return this.donePlaying;
    }

    @Override
    public void tick() {
        if (this.boundTile == null || this.boundTile.isRemoved()) {
            this.donePlaying = true;
        } else if (this.boundTile instanceof ISoundController controller) {
            if (!controller.shouldPlaySound(this.id)) {
                this.donePlaying = true;
            }
            this.f_119573_ = controller.getCurrentVolume(this.id, this.f_119573_);
            this.f_119574_ = controller.getCurrentPitch(this.id, this.f_119574_);
            if (this.donePlaying && controller.isSoundPlaying(this.id)) {
                controller.stopSound(this.id);
            }
        }
    }
}