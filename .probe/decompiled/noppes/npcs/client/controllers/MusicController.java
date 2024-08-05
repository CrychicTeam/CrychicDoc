package noppes.npcs.client.controllers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class MusicController {

    public static MusicController Instance;

    public SoundInstance playing;

    public ResourceLocation playingResource;

    public Entity playingEntity;

    public MusicController() {
        Instance = this;
    }

    public void stopMusic() {
        SoundManager handler = Minecraft.getInstance().getSoundManager();
        if (this.playing != null) {
            handler.stop(this.playing);
        }
        handler.stop(null, SoundSource.MUSIC);
        handler.stop(null, SoundSource.AMBIENT);
        handler.stop(null, SoundSource.RECORDS);
        this.playingResource = null;
        this.playingEntity = null;
        this.playing = null;
    }

    public void playStreaming(String music, Entity entity, boolean isLooping) {
        if (!this.isPlaying(music)) {
            this.stopMusic();
            this.playingEntity = entity;
            this.playingResource = new ResourceLocation(music);
            SoundManager handler = Minecraft.getInstance().getSoundManager();
            this.playing = new SimpleSoundInstance(this.playingResource, SoundSource.RECORDS, 4.0F, 1.0F, SoundInstance.createUnseededRandom(), isLooping, 0, SoundInstance.Attenuation.LINEAR, entity.getX(), entity.getY(), entity.getZ(), false);
            handler.play(this.playing);
        }
    }

    public void playMusic(String music, Entity entity, boolean isLooping) {
        if (!this.isPlaying(music)) {
            this.stopMusic();
            this.playingResource = new ResourceLocation(music);
            this.playingEntity = entity;
            SoundManager handler = Minecraft.getInstance().getSoundManager();
            this.playing = new SimpleSoundInstance(this.playingResource, SoundSource.MUSIC, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), isLooping, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, false);
            handler.play(this.playing);
        }
    }

    public boolean isPlaying(String music) {
        ResourceLocation resource = new ResourceLocation(music);
        return this.playingResource != null && this.playingResource.equals(resource) ? Minecraft.getInstance().getSoundManager().isActive(this.playing) : false;
    }

    public void playSound(SoundSource cat, String music, BlockPos pos, float volume, float pitch) {
        SimpleSoundInstance rec = new SimpleSoundInstance(new ResourceLocation(music), cat, volume, pitch, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, (double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F), false);
        Minecraft.getInstance().getSoundManager().play(rec);
    }
}