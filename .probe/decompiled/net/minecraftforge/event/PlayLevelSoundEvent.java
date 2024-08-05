package net.minecraftforge.event;

import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Cancelable
public class PlayLevelSoundEvent extends Event {

    private final Level level;

    private final float originalVolume;

    private final float originalPitch;

    private Holder<SoundEvent> sound;

    private SoundSource source;

    private float newVolume;

    private float newPitch;

    public PlayLevelSoundEvent(@NotNull Level level, @NotNull Holder<SoundEvent> sound, @NotNull SoundSource source, float volume, float pitch) {
        this.level = level;
        this.sound = sound;
        this.source = source;
        this.originalVolume = volume;
        this.originalPitch = pitch;
        this.newVolume = volume;
        this.newPitch = pitch;
    }

    @NotNull
    public Level getLevel() {
        return this.level;
    }

    @Nullable
    public Holder<SoundEvent> getSound() {
        return this.sound;
    }

    public void setSound(@Nullable Holder<SoundEvent> sound) {
        this.sound = sound;
    }

    @NotNull
    public SoundSource getSource() {
        return this.source;
    }

    public void setSource(@NotNull SoundSource source) {
        Objects.requireNonNull(source, "Sound source cannot be null");
        this.source = source;
    }

    public float getOriginalVolume() {
        return this.originalVolume;
    }

    public float getOriginalPitch() {
        return this.originalPitch;
    }

    public float getNewVolume() {
        return this.newVolume;
    }

    public void setNewVolume(float newVolume) {
        this.newVolume = newVolume;
    }

    public float getNewPitch() {
        return this.newPitch;
    }

    public void setNewPitch(float newPitch) {
        this.newPitch = newPitch;
    }

    public static class AtEntity extends PlayLevelSoundEvent {

        private final Entity entity;

        public AtEntity(Entity entity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
            super(entity.level(), sound, source, volume, pitch);
            this.entity = entity;
        }

        public Entity getEntity() {
            return this.entity;
        }
    }

    public static class AtPosition extends PlayLevelSoundEvent {

        private final Vec3 position;

        public AtPosition(Level level, Vec3 position, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
            super(level, sound, source, volume, pitch);
            this.position = position;
        }

        public Vec3 getPosition() {
            return this.position;
        }
    }
}