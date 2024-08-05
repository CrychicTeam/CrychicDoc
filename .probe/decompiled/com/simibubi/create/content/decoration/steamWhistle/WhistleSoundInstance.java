package com.simibubi.create.content.decoration.steamWhistle;

import com.simibubi.create.AllSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class WhistleSoundInstance extends AbstractTickableSoundInstance {

    private boolean active;

    private int keepAlive;

    private WhistleBlock.WhistleSize size;

    public WhistleSoundInstance(WhistleBlock.WhistleSize size, BlockPos worldPosition) {
        super((size == WhistleBlock.WhistleSize.SMALL ? AllSoundEvents.WHISTLE_HIGH : (size == WhistleBlock.WhistleSize.MEDIUM ? AllSoundEvents.WHISTLE_MEDIUM : AllSoundEvents.WHISTLE_LOW)).getMainEvent(), SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.size = size;
        this.f_119578_ = true;
        this.active = true;
        this.f_119573_ = 0.05F;
        this.f_119579_ = 0;
        this.keepAlive();
        Vec3 v = Vec3.atCenterOf(worldPosition);
        this.f_119575_ = v.x;
        this.f_119576_ = v.y;
        this.f_119577_ = v.z;
    }

    public WhistleBlock.WhistleSize getOctave() {
        return this.size;
    }

    public void fadeOut() {
        this.active = false;
    }

    public void keepAlive() {
        this.keepAlive = 2;
    }

    public void setPitch(float pitch) {
        this.f_119574_ = pitch;
    }

    @Override
    public void tick() {
        if (this.active) {
            this.f_119573_ = Math.min(1.0F, this.f_119573_ + 0.25F);
            this.keepAlive--;
            if (this.keepAlive == 0) {
                this.fadeOut();
            }
        } else {
            this.f_119573_ = Math.max(0.0F, this.f_119573_ - 0.25F);
            if (this.f_119573_ == 0.0F) {
                this.m_119609_();
            }
        }
    }
}