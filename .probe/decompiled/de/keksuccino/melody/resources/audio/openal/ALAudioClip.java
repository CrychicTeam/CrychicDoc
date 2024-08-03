package de.keksuccino.melody.resources.audio.openal;

import de.keksuccino.melody.resources.audio.AudioClip;
import de.keksuccino.melody.resources.audio.MinecraftSoundSettingsObserver;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL10;

public class ALAudioClip implements AudioClip {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final int source;

    @NotNull
    protected SoundSource soundChannel = SoundSource.MASTER;

    protected float volume = 1.0F;

    protected long volumeListenerId;

    protected boolean closeQuietly = false;

    protected volatile boolean closed = false;

    @Nullable
    public static ALAudioClip of(@NotNull ALAudioBuffer completeStaticDataBuffer) {
        Objects.requireNonNull(completeStaticDataBuffer);
        ALAudioClip clip = create();
        if (clip != null) {
            try {
                clip.setStaticBuffer(completeStaticDataBuffer);
                return clip;
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static ALAudioClip create() {
        int[] audioSource = new int[1];
        AL10.alGenSources(audioSource);
        try {
            ALErrorHandler.checkOpenAlError();
            return new ALAudioClip(audioSource[0]);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    protected ALAudioClip(int source) {
        this.source = source;
        this.volumeListenerId = MinecraftSoundSettingsObserver.registerVolumeListener((soundSource, newVolume) -> {
            if (soundSource == this.soundChannel) {
                this.tryUpdateVolume();
            }
        });
        this.tryUpdateVolume();
    }

    public int getState() throws ALException {
        if (this.closed) {
            return 4116;
        } else {
            int state = AL10.alGetSourcei(this.source, 4112);
            ALErrorHandler.checkOpenAlError();
            return state;
        }
    }

    @Override
    public void play() throws ALException {
        if (!this.closed) {
            if (!this.isPlaying()) {
                AL10.alSourcePlay(this.source);
            }
            ALErrorHandler.checkOpenAlError();
        }
    }

    @Override
    public boolean isPlaying() throws ALException {
        return this.getState() == 4114;
    }

    @Override
    public void stop() throws ALException {
        if (!this.closed) {
            if (!this.isStopped()) {
                AL10.alSourceStop(this.source);
                ALErrorHandler.checkOpenAlError();
            }
        }
    }

    public boolean isStopped() throws ALException {
        return this.getState() == 4116;
    }

    @Override
    public void pause() throws ALException {
        if (!this.closed) {
            if (this.isPlaying()) {
                AL10.alSourcePause(this.source);
                ALErrorHandler.checkOpenAlError();
            }
        }
    }

    @Override
    public void resume() throws ALException {
        if (!this.closed) {
            if (this.isPaused()) {
                AL10.alSourcePlay(this.source);
                ALErrorHandler.checkOpenAlError();
            }
        }
    }

    @Override
    public boolean isPaused() throws ALException {
        return this.getState() == 4115;
    }

    public void setLooping(boolean looping) throws ALException {
        if (!this.closed) {
            AL10.alSourcei(this.source, 4103, looping ? 1 : 0);
            ALErrorHandler.checkOpenAlError();
        }
    }

    public boolean isLooping() throws ALException {
        if (this.closed) {
            return false;
        } else {
            boolean loop = AL10.alGetSourcei(this.source, 4103) == 1;
            ALErrorHandler.checkOpenAlError();
            return loop;
        }
    }

    @Override
    public void setVolume(float volume) throws ALException {
        if (!this.closed) {
            if (volume > 1.0F) {
                volume = 1.0F;
            }
            if (volume < 0.0F) {
                volume = 0.0F;
            }
            this.volume = volume;
            float actualVolume = this.volume;
            if (this.soundChannel != SoundSource.MASTER) {
                float soundSourceVolume = Minecraft.getInstance().options.getSoundSourceVolume(this.soundChannel);
                actualVolume *= soundSourceVolume;
            }
            AL10.alSourcef(this.source, 4106, Math.min(1.0F, Math.max(0.0F, actualVolume)));
            ALErrorHandler.checkOpenAlError();
        }
    }

    @Override
    public float getVolume() {
        return this.closed ? 0.0F : this.volume;
    }

    public void tryUpdateVolume() {
        try {
            this.setVolume(this.volume);
        } catch (Exception var2) {
        }
    }

    @Override
    public void setSoundChannel(@NotNull SoundSource channel) {
        this.soundChannel = (SoundSource) Objects.requireNonNull(channel);
        this.tryUpdateVolume();
    }

    @NotNull
    @Override
    public SoundSource getSoundChannel() {
        return this.soundChannel;
    }

    public void setStaticBuffer(@NotNull ALAudioBuffer completeDataBuffer) throws ALException {
        if (!this.closed) {
            Integer buffer = completeDataBuffer.getSource();
            if (buffer != null) {
                AL10.alSourcei(this.source, 4105, buffer);
                ALErrorHandler.checkOpenAlError();
            }
        }
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            MinecraftSoundSettingsObserver.unregisterVolumeListener(this.volumeListenerId);
            if (this.isValidOpenAlSource()) {
                AL10.alSourceStop(this.source);
                if (this.closeQuietly) {
                    ALErrorHandler.getOpenAlError();
                } else {
                    ALErrorHandler.checkAndPrintOpenAlError();
                }
                AL10.alDeleteSources(new int[] { this.source });
                if (this.closeQuietly) {
                    ALErrorHandler.getOpenAlError();
                } else {
                    ALErrorHandler.checkAndPrintOpenAlError();
                }
            }
        }
    }

    public void closeQuietly() {
        if (!this.closed) {
            this.closeQuietly = true;
            IOUtils.closeQuietly(this);
        }
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    public boolean isValidOpenAlSource() {
        return AL10.alIsSource(this.source);
    }
}