package team.lodestar.lodestone.systems.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class LodestoneBlockEntitySoundInstance<T extends LodestoneBlockEntity> extends AbstractTickableSoundInstance {

    public T blockEntity;

    public LodestoneBlockEntitySoundInstance(T blockEntity, SoundEvent soundEvent, float volume, float pitch) {
        super(soundEvent, SoundSource.BLOCKS, LodestoneLib.RANDOM);
        this.blockEntity = blockEntity;
        this.f_119573_ = volume;
        this.f_119574_ = pitch;
        this.f_119579_ = 0;
        this.f_119578_ = true;
    }

    @Override
    public void tick() {
        if (this.blockEntity.m_58901_()) {
            this.m_119609_();
        }
    }
}