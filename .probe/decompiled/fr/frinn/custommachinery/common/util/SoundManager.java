package fr.frinn.custommachinery.common.util;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SoundManager {

    private final BlockPos pos;

    @Nullable
    private SoundInstance sound;

    public SoundManager(BlockPos pos) {
        this.pos = pos;
    }

    @Nullable
    public ResourceLocation getSoundID() {
        return (ResourceLocation) this.getSound().map(SoundInstance::m_7904_).orElse(null);
    }

    public Optional<SoundInstance> getSound() {
        return Optional.ofNullable(this.sound);
    }

    public void setSound(@Nullable SoundEvent sound) {
        this.stop();
        if (sound == null) {
            this.sound = null;
        } else {
            this.sound = new SimpleSoundInstance(sound, SoundSource.BLOCKS, 1.0F, 1.0F, RandomSource.create(), this.pos);
            this.play();
        }
    }

    public boolean isPlaying() {
        return (Boolean) this.getSound().map(sound -> Minecraft.getInstance().getSoundManager().isActive(sound)).orElse(false);
    }

    public void play() {
        this.getSound().ifPresent(sound -> Minecraft.getInstance().getSoundManager().play(sound));
    }

    public void stop() {
        this.getSound().ifPresent(sound -> Minecraft.getInstance().getSoundManager().stop(sound));
    }
}