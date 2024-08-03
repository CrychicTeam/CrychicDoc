package net.minecraftforge.common.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class SoundDefinition {

    private final List<SoundDefinition.Sound> sounds = new ArrayList();

    private boolean replace = false;

    private String subtitle = null;

    private SoundDefinition() {
    }

    public static SoundDefinition definition() {
        return new SoundDefinition();
    }

    public SoundDefinition replace(boolean replace) {
        this.replace = replace;
        return this;
    }

    public SoundDefinition subtitle(@Nullable String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public SoundDefinition with(SoundDefinition.Sound sound) {
        this.sounds.add(sound);
        return this;
    }

    public SoundDefinition with(SoundDefinition.Sound... sounds) {
        this.sounds.addAll(Arrays.asList(sounds));
        return this;
    }

    List<SoundDefinition.Sound> soundList() {
        return this.sounds;
    }

    JsonObject serialize() {
        if (this.sounds.isEmpty()) {
            throw new IllegalStateException("Unable to serialize a sound definition that has no sounds!");
        } else {
            JsonObject object = new JsonObject();
            if (this.replace) {
                object.addProperty("replace", true);
            }
            if (this.subtitle != null) {
                object.addProperty("subtitle", this.subtitle);
            }
            JsonArray sounds = new JsonArray();
            this.sounds.stream().map(SoundDefinition.Sound::serialize).forEach(sounds::add);
            object.add("sounds", sounds);
            return object;
        }
    }

    public static final class Sound {

        private static final SoundDefinition.SoundType DEFAULT_TYPE = SoundDefinition.SoundType.SOUND;

        private static final float DEFAULT_VOLUME = 1.0F;

        private static final float DEFAULT_PITCH = 1.0F;

        private static final int DEFAULT_WEIGHT = 1;

        private static final boolean DEFAULT_STREAM = false;

        private static final int DEFAULT_ATTENUATION_DISTANCE = 16;

        private static final boolean DEFAULT_PRELOAD = false;

        private final ResourceLocation name;

        private final SoundDefinition.SoundType type;

        private float volume = 1.0F;

        private float pitch = 1.0F;

        private int weight = 1;

        private boolean stream = false;

        private int attenuationDistance = 16;

        private boolean preload = false;

        private Sound(ResourceLocation name, SoundDefinition.SoundType type) {
            this.name = name;
            this.type = type;
        }

        public static SoundDefinition.Sound sound(ResourceLocation name, SoundDefinition.SoundType type) {
            return new SoundDefinition.Sound(name, type);
        }

        public SoundDefinition.Sound volume(double volume) {
            return this.volume((float) volume);
        }

        public SoundDefinition.Sound volume(float volume) {
            if (volume <= 0.0F) {
                throw new IllegalArgumentException("Volume must be positive for sound " + this.name + ", but instead got " + volume);
            } else {
                this.volume = volume;
                return this;
            }
        }

        public SoundDefinition.Sound pitch(double pitch) {
            return this.pitch((float) pitch);
        }

        public SoundDefinition.Sound pitch(float pitch) {
            if (pitch <= 0.0F) {
                throw new IllegalArgumentException("Pitch must be positive for sound " + this.name + ", but instead got " + pitch);
            } else {
                this.pitch = pitch;
                return this;
            }
        }

        public SoundDefinition.Sound weight(int weight) {
            if (weight <= 0) {
                throw new IllegalArgumentException("Weight has to be a positive number in sound " + this.name + ", but instead got " + weight);
            } else {
                this.weight = weight;
                return this;
            }
        }

        public SoundDefinition.Sound stream() {
            return this.stream(true);
        }

        public SoundDefinition.Sound stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        public SoundDefinition.Sound attenuationDistance(int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }

        public SoundDefinition.Sound preload() {
            return this.preload(true);
        }

        public SoundDefinition.Sound preload(boolean preload) {
            this.preload = preload;
            return this;
        }

        ResourceLocation name() {
            return this.name;
        }

        SoundDefinition.SoundType type() {
            return this.type;
        }

        JsonElement serialize() {
            if (this.canBeInShortForm()) {
                return new JsonPrimitive(this.stripMcPrefix(this.name));
            } else {
                JsonObject object = new JsonObject();
                object.addProperty("name", this.stripMcPrefix(this.name));
                if (this.type != DEFAULT_TYPE) {
                    object.addProperty("type", this.type.jsonString);
                }
                if (this.volume != 1.0F) {
                    object.addProperty("volume", this.volume);
                }
                if (this.pitch != 1.0F) {
                    object.addProperty("pitch", this.pitch);
                }
                if (this.weight != 1) {
                    object.addProperty("weight", this.weight);
                }
                if (this.stream) {
                    object.addProperty("stream", this.stream);
                }
                if (this.preload) {
                    object.addProperty("preload", this.preload);
                }
                if (this.attenuationDistance != 16) {
                    object.addProperty("attenuation_distance", this.attenuationDistance);
                }
                return object;
            }
        }

        private boolean canBeInShortForm() {
            return this.type == DEFAULT_TYPE && this.volume == 1.0F && this.pitch == 1.0F && this.weight == 1 && !this.stream && this.attenuationDistance == 16 && !this.preload;
        }

        private String stripMcPrefix(ResourceLocation name) {
            return "minecraft".equals(name.getNamespace()) ? name.getPath() : name.toString();
        }
    }

    public static enum SoundType {

        SOUND("sound"), EVENT("event");

        private final String jsonString;

        private SoundType(String jsonString) {
            this.jsonString = jsonString;
        }
    }
}