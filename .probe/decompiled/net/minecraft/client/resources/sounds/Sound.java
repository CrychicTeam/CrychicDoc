package net.minecraft.client.resources.sounds;

import javax.annotation.Nullable;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.Weighted;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.SampledFloat;

public class Sound implements Weighted<Sound> {

    public static final FileToIdConverter SOUND_LISTER = new FileToIdConverter("sounds", ".ogg");

    private final ResourceLocation location;

    private final SampledFloat volume;

    private final SampledFloat pitch;

    private final int weight;

    private final Sound.Type type;

    private final boolean stream;

    private final boolean preload;

    private final int attenuationDistance;

    public Sound(String string0, SampledFloat sampledFloat1, SampledFloat sampledFloat2, int int3, Sound.Type soundType4, boolean boolean5, boolean boolean6, int int7) {
        this.location = new ResourceLocation(string0);
        this.volume = sampledFloat1;
        this.pitch = sampledFloat2;
        this.weight = int3;
        this.type = soundType4;
        this.stream = boolean5;
        this.preload = boolean6;
        this.attenuationDistance = int7;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public ResourceLocation getPath() {
        return SOUND_LISTER.idToFile(this.location);
    }

    public SampledFloat getVolume() {
        return this.volume;
    }

    public SampledFloat getPitch() {
        return this.pitch;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    public Sound getSound(RandomSource randomSource0) {
        return this;
    }

    @Override
    public void preloadIfRequired(SoundEngine soundEngine0) {
        if (this.preload) {
            soundEngine0.requestPreload(this);
        }
    }

    public Sound.Type getType() {
        return this.type;
    }

    public boolean shouldStream() {
        return this.stream;
    }

    public boolean shouldPreload() {
        return this.preload;
    }

    public int getAttenuationDistance() {
        return this.attenuationDistance;
    }

    public String toString() {
        return "Sound[" + this.location + "]";
    }

    public static enum Type {

        FILE("file"), SOUND_EVENT("event");

        private final String name;

        private Type(String p_119809_) {
            this.name = p_119809_;
        }

        @Nullable
        public static Sound.Type getByName(String p_119811_) {
            for (Sound.Type $$1 : values()) {
                if ($$1.name.equals(p_119811_)) {
                    return $$1;
                }
            }
            return null;
        }
    }
}