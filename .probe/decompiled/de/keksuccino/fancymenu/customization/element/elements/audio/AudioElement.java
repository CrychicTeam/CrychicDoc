package de.keksuccino.fancymenu.customization.element.elements.audio;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.MathUtils;
import de.keksuccino.fancymenu.util.Trio;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AudioElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final DrawableColor BACKGROUND_COLOR = DrawableColor.of(new Color(92, 166, 239));

    private static final long AUDIO_START_COOLDOWN_MS = 2000L;

    @NotNull
    protected AudioElement.PlayMode playMode = AudioElement.PlayMode.NORMAL;

    protected boolean loop = false;

    protected float volume = 1.0F;

    @NotNull
    protected SoundSource soundSource = SoundSource.MASTER;

    public List<AudioElement.AudioInstance> audios = new ArrayList();

    protected int currentAudioIndex = -1;

    @Nullable
    protected AudioElement.AudioInstance currentAudioInstance;

    protected IAudio currentAudio;

    protected boolean currentAudioStarted = false;

    protected long lastAudioStart = -1L;

    protected boolean cacheChecked = false;

    public AudioElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    protected List<Integer> getAlreadyPlayedShuffleAudios() {
        return this.getMemory().putPropertyIfAbsentAndGet("already_played_shuffle_audios", new ArrayList());
    }

    protected int getLastPlayedLoopShuffleAudio() {
        return this.getMemory().putPropertyIfAbsentAndGet("last_played_loop_shuffle_audio", -10000);
    }

    protected void setLastPlayedLoopShuffleAudio(int lastPlayedLoopShuffleAudio) {
        this.getMemory().putProperty("last_played_loop_shuffle_audio", lastPlayedLoopShuffleAudio);
    }

    public void renderTick() {
        if (!isEditor()) {
            boolean loadedFromCache = false;
            if (!this.cacheChecked) {
                if (AudioElementBuilder.CURRENT_AUDIO_CACHE.containsKey(this.getInstanceIdentifier())) {
                    int cached = (Integer) ((Trio) AudioElementBuilder.CURRENT_AUDIO_CACHE.get(this.getInstanceIdentifier())).getThird();
                    if (this.audios.size() - 1 >= cached) {
                        this.currentAudioIndex = cached;
                        loadedFromCache = true;
                    }
                }
                this.cacheChecked = true;
            }
            if (this.shouldRender()) {
                if (!loadedFromCache) {
                    this.pickNextAudio();
                }
            } else if (loadedFromCache) {
                ((IAudio) ((Trio) AudioElementBuilder.CURRENT_AUDIO_CACHE.get(this.getInstanceIdentifier())).getSecond()).stop();
                AudioElementBuilder.CURRENT_AUDIO_CACHE.remove(this.getInstanceIdentifier());
                if (this.currentAudio != null && this.currentAudio.isReady() && this.currentAudio.isPlaying()) {
                    this.currentAudio.stop();
                }
                return;
            }
            if (this.currentAudioIndex != -2) {
                if (this.currentAudioIndex != -1) {
                    if (this.currentAudioInstance == null && this.currentAudioIndex >= 0 && this.audios.size() - 1 >= this.currentAudioIndex) {
                        this.currentAudioInstance = (AudioElement.AudioInstance) this.audios.get(this.currentAudioIndex);
                        if (this.currentAudioInstance != null) {
                            this.currentAudio = this.currentAudioInstance.supplier.get();
                            if (this.currentAudio != null) {
                                AudioElementBuilder.CURRENT_AUDIO_CACHE.put(this.getInstanceIdentifier(), Trio.of(this.currentAudioInstance.supplier, this.currentAudio, this.currentAudioIndex));
                            }
                        }
                    }
                    if (this.currentAudio != null) {
                        if (!this.shouldRender()) {
                            if (this.currentAudio.isReady() && this.currentAudio.isPlaying()) {
                                this.currentAudio.stop();
                            }
                            return;
                        }
                        long now = System.currentTimeMillis();
                        if (loadedFromCache) {
                            if (this.currentAudio.isReady() && this.currentAudio.isPlaying()) {
                                this.lastAudioStart = now;
                                this.currentAudioStarted = true;
                            }
                            this.currentAudio.setVolume(this.volume);
                            this.currentAudio.setSoundChannel(this.soundSource);
                        }
                        boolean isOnCooldown = this.lastAudioStart + 2000L > now;
                        if (!isOnCooldown && this.currentAudioInstance != null && !this.currentAudioStarted && this.currentAudio.isReady() && !this.currentAudio.isPlaying()) {
                            this.lastAudioStart = now;
                            this.currentAudio.setVolume(this.volume);
                            this.currentAudio.setSoundChannel(this.soundSource);
                            this.currentAudio.play();
                            this.currentAudioStarted = true;
                        }
                    }
                }
            }
        }
    }

    protected void pickNextAudio() {
        if (!this.audios.isEmpty()) {
            if (this.currentAudioIndex != -2) {
                if (this.currentAudioInstance != null) {
                    if (this.currentAudio != null) {
                        long now = System.currentTimeMillis();
                        boolean isOnCooldown = this.lastAudioStart + 2000L > now;
                        if (!isOnCooldown && this.currentAudioStarted && (!this.currentAudio.isReady() || !this.currentAudio.isPlaying())) {
                            this.skipToNextAudio(false);
                        }
                    } else {
                        LOGGER.warn("[FANCYMENU] Audio element was unable to load audio track! Skipping to next track, because track was NULL: " + this.currentAudioInstance.supplier.getSourceWithPrefix());
                        this.skipToNextAudio(false);
                    }
                } else {
                    this.skipToNextAudio(false);
                }
            }
        }
    }

    public void skipToNextAudio(boolean forceRestartIfEndReached) {
        if (this.playMode == AudioElement.PlayMode.SHUFFLE) {
            List<Integer> indexes = this.buildShuffleIndexesList();
            if (this.loop && indexes.isEmpty() && !this.audios.isEmpty()) {
                this.getAlreadyPlayedShuffleAudios().clear();
                if (this.audios.size() == 1) {
                    this.setLastPlayedLoopShuffleAudio(-10000);
                }
                indexes = this.buildShuffleIndexesList();
            }
            if (!indexes.isEmpty()) {
                int pickedIndex = indexes.size() == 1 ? 0 : MathUtils.getRandomNumberInRange(0, indexes.size() - 1);
                this.currentAudioIndex = (Integer) indexes.get(pickedIndex);
                this.getAlreadyPlayedShuffleAudios().add(this.currentAudioIndex);
                if (this.loop) {
                    this.setLastPlayedLoopShuffleAudio(this.currentAudioIndex);
                }
            } else {
                this.currentAudioIndex = -2;
                this.clearCacheForElement();
            }
        } else {
            this.currentAudioIndex++;
            if (this.currentAudioIndex > this.audios.size() - 1) {
                this.currentAudioIndex = this.loop ? 0 : -2;
                if (this.currentAudioIndex == -2) {
                    this.clearCacheForElement();
                }
            }
        }
        if (this.currentAudio != null && this.currentAudio.isReady()) {
            this.currentAudio.stop();
        }
        this.currentAudioInstance = null;
        this.currentAudio = null;
        this.currentAudioStarted = false;
        if (this.currentAudioIndex == -2 && forceRestartIfEndReached) {
            this.resetAudioElementKeepAudios();
        }
    }

    @NotNull
    protected List<Integer> buildShuffleIndexesList() {
        List<Integer> indexes = new ArrayList();
        if (this.playMode != AudioElement.PlayMode.SHUFFLE) {
            return indexes;
        } else {
            int i = 0;
            for (AudioElement.AudioInstance ignored : this.audios) {
                indexes.add(i);
                i++;
            }
            indexes.removeIf(integer -> this.getAlreadyPlayedShuffleAudios().contains(integer));
            if (this.loop && this.getLastPlayedLoopShuffleAudio() != -10000 && indexes.contains(this.getLastPlayedLoopShuffleAudio())) {
                indexes.remove(this.getLastPlayedLoopShuffleAudio());
            }
            return indexes;
        }
    }

    public void resetAudioElementKeepAudios() {
        if (this.currentAudio != null && this.currentAudio.isReady()) {
            this.currentAudio.stop();
        }
        this.currentAudioInstance = null;
        this.currentAudio = null;
        this.currentAudioStarted = false;
        this.currentAudioIndex = -1;
        this.lastAudioStart = -1L;
        this.getMemory().clear();
        this.clearCacheForElement();
    }

    public void clearCacheForElement() {
        if (AudioElementBuilder.CURRENT_AUDIO_CACHE.containsKey(this.getInstanceIdentifier())) {
            Trio<ResourceSupplier<IAudio>, IAudio, Integer> cache = (Trio<ResourceSupplier<IAudio>, IAudio, Integer>) AudioElementBuilder.CURRENT_AUDIO_CACHE.get(this.getInstanceIdentifier());
            if (cache != null && cache.getSecond().isReady()) {
                cache.getSecond().stop();
            }
            AudioElementBuilder.CURRENT_AUDIO_CACHE.remove(this.getInstanceIdentifier());
        }
    }

    public void setLooping(boolean loop, boolean resetElement) {
        this.loop = loop;
        if (resetElement) {
            this.resetAudioElementKeepAudios();
        }
    }

    public boolean isLooping() {
        return this.loop;
    }

    public void setPlayMode(@NotNull AudioElement.PlayMode mode, boolean resetElement) {
        this.playMode = (AudioElement.PlayMode) Objects.requireNonNull(mode);
        if (resetElement) {
            this.resetAudioElementKeepAudios();
        }
    }

    @NotNull
    public AudioElement.PlayMode getPlayMode() {
        return this.playMode;
    }

    public void setVolume(float volume) {
        if (volume > 1.0F) {
            volume = 1.0F;
        }
        if (volume < 0.0F) {
            volume = 0.0F;
        }
        this.volume = volume;
        if (this.currentAudio != null) {
            this.currentAudio.setVolume(this.volume);
        }
    }

    public float getVolume() {
        return this.volume;
    }

    public void setSoundSource(@NotNull SoundSource soundSource) {
        this.soundSource = (SoundSource) Objects.requireNonNull(soundSource);
        if (this.currentAudio != null) {
            this.currentAudio.setSoundChannel(soundSource);
        }
    }

    @NotNull
    public SoundSource getSoundSource() {
        return this.soundSource;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderTick();
        if (this.shouldRender()) {
            if (isEditor()) {
                int x = this.getAbsoluteX();
                int y = this.getAbsoluteY();
                int w = this.getAbsoluteWidth();
                int h = this.getAbsoluteHeight();
                RenderSystem.enableBlend();
                graphics.fill(x, y, x + w, y + h, BACKGROUND_COLOR.getColorInt());
                graphics.enableScissor(x, y, x + w, y + h);
                graphics.drawCenteredString(Minecraft.getInstance().font, this.getDisplayName(), x + w / 2, y + h / 2 - 9 / 2, -1);
                graphics.disableScissor();
                RenderingUtils.resetShaderColor(graphics);
            }
        }
    }

    public static class AudioInstance {

        @NotNull
        ResourceSupplier<IAudio> supplier;

        public AudioInstance(@NotNull ResourceSupplier<IAudio> supplier) {
            this.supplier = (ResourceSupplier<IAudio>) Objects.requireNonNull(supplier);
        }

        public static void serializeAllToExistingContainer(@NotNull List<AudioElement.AudioInstance> instances, @NotNull PropertyContainer container) {
            int i = 0;
            for (AudioElement.AudioInstance instance : instances) {
                container.putProperty("audio_instance_" + i, instance.supplier.getSourceWithPrefix());
                i++;
            }
        }

        @NotNull
        public static List<AudioElement.AudioInstance> deserializeAllOfContainer(@NotNull PropertyContainer container) {
            List<AudioElement.AudioInstance> instances = new ArrayList();
            container.getProperties().forEach((key, value) -> {
                if (StringUtils.startsWith(key, "audio_instance_")) {
                    instances.add(new AudioElement.AudioInstance(ResourceSupplier.audio(value)));
                }
            });
            return instances;
        }
    }

    public static enum PlayMode implements LocalizedCycleEnum<AudioElement.PlayMode> {

        NORMAL("normal"), SHUFFLE("shuffle");

        private final String name;

        private PlayMode(@NotNull String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.elements.audio.play_mode";
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) LocalizedCycleEnum.WARNING_TEXT_STYLE.get();
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public AudioElement.PlayMode[] getValues() {
            return values();
        }

        @Nullable
        public AudioElement.PlayMode getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static AudioElement.PlayMode getByName(@NotNull String name) {
            for (AudioElement.PlayMode mode : values()) {
                if (mode.getName().equals(name)) {
                    return mode;
                }
            }
            return null;
        }
    }
}