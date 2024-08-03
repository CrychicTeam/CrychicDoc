package net.minecraftforge.common.data;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class SoundDefinitionsProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final PackOutput output;

    private final String modId;

    private final ExistingFileHelper helper;

    private final Map<String, SoundDefinition> sounds = new LinkedHashMap();

    protected SoundDefinitionsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        this.output = output;
        this.modId = modId;
        this.helper = helper;
    }

    public abstract void registerSounds();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.sounds.clear();
        this.registerSounds();
        this.validate();
        return !this.sounds.isEmpty() ? this.save(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modId).resolve("sounds.json")) : CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Sound Definitions";
    }

    protected static SoundDefinition definition() {
        return SoundDefinition.definition();
    }

    protected static SoundDefinition.Sound sound(ResourceLocation name, SoundDefinition.SoundType type) {
        return SoundDefinition.Sound.sound(name, type);
    }

    protected static SoundDefinition.Sound sound(ResourceLocation name) {
        return sound(name, SoundDefinition.SoundType.SOUND);
    }

    protected static SoundDefinition.Sound sound(String name, SoundDefinition.SoundType type) {
        return sound(new ResourceLocation(name), type);
    }

    protected static SoundDefinition.Sound sound(String name) {
        return sound(new ResourceLocation(name));
    }

    protected void add(Supplier<SoundEvent> soundEvent, SoundDefinition definition) {
        this.add((SoundEvent) soundEvent.get(), definition);
    }

    protected void add(SoundEvent soundEvent, SoundDefinition definition) {
        this.add(soundEvent.getLocation(), definition);
    }

    protected void add(ResourceLocation soundEvent, SoundDefinition definition) {
        this.addSounds(soundEvent.getPath(), definition);
    }

    protected void add(String soundEvent, SoundDefinition definition) {
        this.add(new ResourceLocation(soundEvent), definition);
    }

    private void addSounds(String soundEvent, SoundDefinition definition) {
        if (this.sounds.put(soundEvent, definition) != null) {
            throw new IllegalStateException("Sound event '" + this.modId + ":" + soundEvent + "' already exists");
        }
    }

    private void validate() {
        List<String> notValid = this.sounds.entrySet().stream().filter(it -> !this.validate((String) it.getKey(), (SoundDefinition) it.getValue())).map(Entry::getKey).map(it -> this.modId + ":" + it).toList();
        if (!notValid.isEmpty()) {
            throw new IllegalStateException("Found invalid sound events: " + notValid);
        }
    }

    private boolean validate(String name, SoundDefinition def) {
        return def.soundList().stream().allMatch(it -> this.validate(name, it));
    }

    private boolean validate(String name, SoundDefinition.Sound sound) {
        switch(sound.type()) {
            case SOUND:
                return this.validateSound(name, sound.name());
            case EVENT:
                return this.validateEvent(name, sound.name());
            default:
                throw new IllegalArgumentException("The given sound '" + sound.name() + "' does not have a valid type: expected either SOUND or EVENT, but found " + sound.type());
        }
    }

    private boolean validateSound(String soundName, ResourceLocation name) {
        boolean valid = this.helper.exists(name, PackType.CLIENT_RESOURCES, ".ogg", "sounds");
        if (!valid) {
            String path = name.getNamespace() + ":sounds/" + name.getPath() + ".ogg";
            LOGGER.warn("Unable to find corresponding OGG file '{}' for sound event '{}'", path, soundName);
        }
        return valid;
    }

    private boolean validateEvent(String soundName, ResourceLocation name) {
        boolean valid = this.sounds.containsKey(soundName) || ForgeRegistries.SOUND_EVENTS.containsKey(name);
        if (!valid) {
            LOGGER.warn("Unable to find event '{}' referenced from '{}'", name, soundName);
        }
        return valid;
    }

    private CompletableFuture<?> save(CachedOutput cache, Path targetFile) {
        return DataProvider.saveStable(cache, this.mapToJson(this.sounds), targetFile);
    }

    private JsonObject mapToJson(Map<String, SoundDefinition> map) {
        JsonObject obj = new JsonObject();
        map.forEach((k, v) -> obj.add(k, v.serialize()));
        return obj;
    }
}