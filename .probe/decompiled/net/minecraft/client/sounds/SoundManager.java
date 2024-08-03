package net.minecraft.client.sounds;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.client.Camera;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundEventRegistration;
import net.minecraft.client.resources.sounds.SoundEventRegistrationSerializer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.MultipliedFloats;
import org.slf4j.Logger;

public class SoundManager extends SimplePreparableReloadListener<SoundManager.Preparations> {

    public static final Sound EMPTY_SOUND = new Sound("minecraft:empty", ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), 1, Sound.Type.FILE, false, false, 16);

    public static final ResourceLocation INTENTIONALLY_EMPTY_SOUND_LOCATION = new ResourceLocation("minecraft", "intentionally_empty");

    public static final WeighedSoundEvents INTENTIONALLY_EMPTY_SOUND_EVENT = new WeighedSoundEvents(INTENTIONALLY_EMPTY_SOUND_LOCATION, null);

    public static final Sound INTENTIONALLY_EMPTY_SOUND = new Sound(INTENTIONALLY_EMPTY_SOUND_LOCATION.toString(), ConstantFloat.of(1.0F), ConstantFloat.of(1.0F), 1, Sound.Type.FILE, false, false, 16);

    static final Logger LOGGER = LogUtils.getLogger();

    private static final String SOUNDS_PATH = "sounds.json";

    private static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(Component.class, new Component.Serializer()).registerTypeAdapter(SoundEventRegistration.class, new SoundEventRegistrationSerializer()).create();

    private static final TypeToken<Map<String, SoundEventRegistration>> SOUND_EVENT_REGISTRATION_TYPE = new TypeToken<Map<String, SoundEventRegistration>>() {
    };

    private final Map<ResourceLocation, WeighedSoundEvents> registry = Maps.newHashMap();

    private final SoundEngine soundEngine;

    private final Map<ResourceLocation, Resource> soundCache = new HashMap();

    public SoundManager(Options options0) {
        this.soundEngine = new SoundEngine(this, options0, ResourceProvider.fromMap(this.soundCache));
    }

    protected SoundManager.Preparations prepare(ResourceManager resourceManager0, ProfilerFiller profilerFiller1) {
        SoundManager.Preparations $$2 = new SoundManager.Preparations();
        profilerFiller1.startTick();
        profilerFiller1.push("list");
        $$2.listResources(resourceManager0);
        profilerFiller1.pop();
        for (String $$3 : resourceManager0.getNamespaces()) {
            profilerFiller1.push($$3);
            try {
                for (Resource $$5 : resourceManager0.getResourceStack(new ResourceLocation($$3, "sounds.json"))) {
                    profilerFiller1.push($$5.sourcePackId());
                    try {
                        Reader $$6 = $$5.openAsReader();
                        try {
                            profilerFiller1.push("parse");
                            Map<String, SoundEventRegistration> $$7 = GsonHelper.fromJson(GSON, $$6, SOUND_EVENT_REGISTRATION_TYPE);
                            profilerFiller1.popPush("register");
                            for (Entry<String, SoundEventRegistration> $$8 : $$7.entrySet()) {
                                $$2.handleRegistration(new ResourceLocation($$3, (String) $$8.getKey()), (SoundEventRegistration) $$8.getValue());
                            }
                            profilerFiller1.pop();
                        } catch (Throwable var14) {
                            if ($$6 != null) {
                                try {
                                    $$6.close();
                                } catch (Throwable var13) {
                                    var14.addSuppressed(var13);
                                }
                            }
                            throw var14;
                        }
                        if ($$6 != null) {
                            $$6.close();
                        }
                    } catch (RuntimeException var15) {
                        LOGGER.warn("Invalid {} in resourcepack: '{}'", new Object[] { "sounds.json", $$5.sourcePackId(), var15 });
                    }
                    profilerFiller1.pop();
                }
            } catch (IOException var16) {
            }
            profilerFiller1.pop();
        }
        profilerFiller1.endTick();
        return $$2;
    }

    protected void apply(SoundManager.Preparations soundManagerPreparations0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2) {
        soundManagerPreparations0.apply(this.registry, this.soundCache, this.soundEngine);
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            for (ResourceLocation $$3 : this.registry.keySet()) {
                WeighedSoundEvents $$4 = (WeighedSoundEvents) this.registry.get($$3);
                if (!ComponentUtils.isTranslationResolvable($$4.getSubtitle()) && BuiltInRegistries.SOUND_EVENT.containsKey($$3)) {
                    LOGGER.error("Missing subtitle {} for sound event: {}", $$4.getSubtitle(), $$3);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            for (ResourceLocation $$5 : this.registry.keySet()) {
                if (!BuiltInRegistries.SOUND_EVENT.containsKey($$5)) {
                    LOGGER.debug("Not having sound event for: {}", $$5);
                }
            }
        }
        this.soundEngine.reload();
    }

    public List<String> getAvailableSoundDevices() {
        return this.soundEngine.getAvailableSoundDevices();
    }

    static boolean validateSoundResource(Sound sound0, ResourceLocation resourceLocation1, ResourceProvider resourceProvider2) {
        ResourceLocation $$3 = sound0.getPath();
        if (resourceProvider2.getResource($$3).isEmpty()) {
            LOGGER.warn("File {} does not exist, cannot add it to event {}", $$3, resourceLocation1);
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    public WeighedSoundEvents getSoundEvent(ResourceLocation resourceLocation0) {
        return (WeighedSoundEvents) this.registry.get(resourceLocation0);
    }

    public Collection<ResourceLocation> getAvailableSounds() {
        return this.registry.keySet();
    }

    public void queueTickingSound(TickableSoundInstance tickableSoundInstance0) {
        this.soundEngine.queueTickingSound(tickableSoundInstance0);
    }

    public void play(SoundInstance soundInstance0) {
        this.soundEngine.play(soundInstance0);
    }

    public void playDelayed(SoundInstance soundInstance0, int int1) {
        this.soundEngine.playDelayed(soundInstance0, int1);
    }

    public void updateSource(Camera camera0) {
        this.soundEngine.updateSource(camera0);
    }

    public void pause() {
        this.soundEngine.pause();
    }

    public void stop() {
        this.soundEngine.stopAll();
    }

    public void destroy() {
        this.soundEngine.destroy();
    }

    public void tick(boolean boolean0) {
        this.soundEngine.tick(boolean0);
    }

    public void resume() {
        this.soundEngine.resume();
    }

    public void updateSourceVolume(SoundSource soundSource0, float float1) {
        if (soundSource0 == SoundSource.MASTER && float1 <= 0.0F) {
            this.stop();
        }
        this.soundEngine.updateCategoryVolume(soundSource0, float1);
    }

    public void stop(SoundInstance soundInstance0) {
        this.soundEngine.stop(soundInstance0);
    }

    public boolean isActive(SoundInstance soundInstance0) {
        return this.soundEngine.isActive(soundInstance0);
    }

    public void addListener(SoundEventListener soundEventListener0) {
        this.soundEngine.addEventListener(soundEventListener0);
    }

    public void removeListener(SoundEventListener soundEventListener0) {
        this.soundEngine.removeEventListener(soundEventListener0);
    }

    public void stop(@Nullable ResourceLocation resourceLocation0, @Nullable SoundSource soundSource1) {
        this.soundEngine.stop(resourceLocation0, soundSource1);
    }

    public String getDebugString() {
        return this.soundEngine.getDebugString();
    }

    public void reload() {
        this.soundEngine.reload();
    }

    protected static class Preparations {

        final Map<ResourceLocation, WeighedSoundEvents> registry = Maps.newHashMap();

        private Map<ResourceLocation, Resource> soundCache = Map.of();

        void listResources(ResourceManager resourceManager0) {
            this.soundCache = Sound.SOUND_LISTER.listMatchingResources(resourceManager0);
        }

        void handleRegistration(ResourceLocation resourceLocation0, SoundEventRegistration soundEventRegistration1) {
            WeighedSoundEvents $$2 = (WeighedSoundEvents) this.registry.get(resourceLocation0);
            boolean $$3 = $$2 == null;
            if ($$3 || soundEventRegistration1.isReplace()) {
                if (!$$3) {
                    SoundManager.LOGGER.debug("Replaced sound event location {}", resourceLocation0);
                }
                $$2 = new WeighedSoundEvents(resourceLocation0, soundEventRegistration1.getSubtitle());
                this.registry.put(resourceLocation0, $$2);
            }
            ResourceProvider $$4 = ResourceProvider.fromMap(this.soundCache);
            for (final Sound $$5 : soundEventRegistration1.getSounds()) {
                final ResourceLocation $$6 = $$5.getLocation();
                Weighted<Sound> $$8;
                switch($$5.getType()) {
                    case FILE:
                        if (!SoundManager.validateSoundResource($$5, resourceLocation0, $$4)) {
                            continue;
                        }
                        $$8 = $$5;
                        break;
                    case SOUND_EVENT:
                        $$8 = new Weighted<Sound>() {

                            @Override
                            public int getWeight() {
                                WeighedSoundEvents $$0 = (WeighedSoundEvents) Preparations.this.registry.get($$6);
                                return $$0 == null ? 0 : $$0.getWeight();
                            }

                            public Sound getSound(RandomSource p_235261_) {
                                WeighedSoundEvents $$1 = (WeighedSoundEvents) Preparations.this.registry.get($$6);
                                if ($$1 == null) {
                                    return SoundManager.EMPTY_SOUND;
                                } else {
                                    Sound $$2 = $$1.getSound(p_235261_);
                                    return new Sound($$2.getLocation().toString(), new MultipliedFloats($$2.getVolume(), $$5.getVolume()), new MultipliedFloats($$2.getPitch(), $$5.getPitch()), $$5.getWeight(), Sound.Type.FILE, $$2.shouldStream() || $$5.shouldStream(), $$2.shouldPreload(), $$2.getAttenuationDistance());
                                }
                            }

                            @Override
                            public void preloadIfRequired(SoundEngine p_120438_) {
                                WeighedSoundEvents $$1 = (WeighedSoundEvents) Preparations.this.registry.get($$6);
                                if ($$1 != null) {
                                    $$1.preloadIfRequired(p_120438_);
                                }
                            }
                        };
                        break;
                    default:
                        throw new IllegalStateException("Unknown SoundEventRegistration type: " + $$5.getType());
                }
                $$2.addSound($$8);
            }
        }

        public void apply(Map<ResourceLocation, WeighedSoundEvents> mapResourceLocationWeighedSoundEvents0, Map<ResourceLocation, Resource> mapResourceLocationResource1, SoundEngine soundEngine2) {
            mapResourceLocationWeighedSoundEvents0.clear();
            mapResourceLocationResource1.clear();
            mapResourceLocationResource1.putAll(this.soundCache);
            for (Entry<ResourceLocation, WeighedSoundEvents> $$3 : this.registry.entrySet()) {
                mapResourceLocationWeighedSoundEvents0.put((ResourceLocation) $$3.getKey(), (WeighedSoundEvents) $$3.getValue());
                ((WeighedSoundEvents) $$3.getValue()).preloadIfRequired(soundEngine2);
            }
        }
    }
}