package net.minecraft.client.resources.sounds;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;

public class BiomeAmbientSoundsHandler implements AmbientSoundHandler {

    private static final int LOOP_SOUND_CROSS_FADE_TIME = 40;

    private static final float SKY_MOOD_RECOVERY_RATE = 0.001F;

    private final LocalPlayer player;

    private final SoundManager soundManager;

    private final BiomeManager biomeManager;

    private final RandomSource random;

    private final Object2ObjectArrayMap<Biome, BiomeAmbientSoundsHandler.LoopSoundInstance> loopSounds = new Object2ObjectArrayMap();

    private Optional<AmbientMoodSettings> moodSettings = Optional.empty();

    private Optional<AmbientAdditionsSettings> additionsSettings = Optional.empty();

    private float moodiness;

    @Nullable
    private Biome previousBiome;

    public BiomeAmbientSoundsHandler(LocalPlayer localPlayer0, SoundManager soundManager1, BiomeManager biomeManager2) {
        this.random = localPlayer0.m_9236_().getRandom();
        this.player = localPlayer0;
        this.soundManager = soundManager1;
        this.biomeManager = biomeManager2;
    }

    public float getMoodiness() {
        return this.moodiness;
    }

    @Override
    public void tick() {
        this.loopSounds.values().removeIf(AbstractTickableSoundInstance::m_7801_);
        Biome $$0 = this.biomeManager.getNoiseBiomeAtPosition(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_()).value();
        if ($$0 != this.previousBiome) {
            this.previousBiome = $$0;
            this.moodSettings = $$0.getAmbientMood();
            this.additionsSettings = $$0.getAmbientAdditions();
            this.loopSounds.values().forEach(BiomeAmbientSoundsHandler.LoopSoundInstance::m_119659_);
            $$0.getAmbientLoop().ifPresent(p_263342_ -> this.loopSounds.compute($$0, (p_174924_, p_174925_) -> {
                if (p_174925_ == null) {
                    p_174925_ = new BiomeAmbientSoundsHandler.LoopSoundInstance((SoundEvent) p_263342_.value());
                    this.soundManager.play(p_174925_);
                }
                p_174925_.fadeIn();
                return p_174925_;
            }));
        }
        this.additionsSettings.ifPresent(p_119648_ -> {
            if (this.random.nextDouble() < p_119648_.getTickChance()) {
                this.soundManager.play(SimpleSoundInstance.forAmbientAddition(p_119648_.getSoundEvent().value()));
            }
        });
        this.moodSettings.ifPresent(p_274718_ -> {
            Level $$1 = this.player.m_9236_();
            int $$2 = p_274718_.getBlockSearchExtent() * 2 + 1;
            BlockPos $$3 = BlockPos.containing(this.player.m_20185_() + (double) this.random.nextInt($$2) - (double) p_274718_.getBlockSearchExtent(), this.player.m_20188_() + (double) this.random.nextInt($$2) - (double) p_274718_.getBlockSearchExtent(), this.player.m_20189_() + (double) this.random.nextInt($$2) - (double) p_274718_.getBlockSearchExtent());
            int $$4 = $$1.m_45517_(LightLayer.SKY, $$3);
            if ($$4 > 0) {
                this.moodiness = this.moodiness - (float) $$4 / (float) $$1.m_7469_() * 0.001F;
            } else {
                this.moodiness = this.moodiness - (float) ($$1.m_45517_(LightLayer.BLOCK, $$3) - 1) / (float) p_274718_.getTickDelay();
            }
            if (this.moodiness >= 1.0F) {
                double $$5 = (double) $$3.m_123341_() + 0.5;
                double $$6 = (double) $$3.m_123342_() + 0.5;
                double $$7 = (double) $$3.m_123343_() + 0.5;
                double $$8 = $$5 - this.player.m_20185_();
                double $$9 = $$6 - this.player.m_20188_();
                double $$10 = $$7 - this.player.m_20189_();
                double $$11 = Math.sqrt($$8 * $$8 + $$9 * $$9 + $$10 * $$10);
                double $$12 = $$11 + p_274718_.getSoundPositionOffset();
                SimpleSoundInstance $$13 = SimpleSoundInstance.forAmbientMood(p_274718_.getSoundEvent().value(), this.random, this.player.m_20185_() + $$8 / $$11 * $$12, this.player.m_20188_() + $$9 / $$11 * $$12, this.player.m_20189_() + $$10 / $$11 * $$12);
                this.soundManager.play($$13);
                this.moodiness = 0.0F;
            } else {
                this.moodiness = Math.max(this.moodiness, 0.0F);
            }
        });
    }

    public static class LoopSoundInstance extends AbstractTickableSoundInstance {

        private int fadeDirection;

        private int fade;

        public LoopSoundInstance(SoundEvent soundEvent0) {
            super(soundEvent0, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
            this.f_119578_ = true;
            this.f_119579_ = 0;
            this.f_119573_ = 1.0F;
            this.f_119582_ = true;
        }

        @Override
        public void tick() {
            if (this.fade < 0) {
                this.m_119609_();
            }
            this.fade = this.fade + this.fadeDirection;
            this.f_119573_ = Mth.clamp((float) this.fade / 40.0F, 0.0F, 1.0F);
        }

        public void fadeOut() {
            this.fade = Math.min(this.fade, 40);
            this.fadeDirection = -1;
        }

        public void fadeIn() {
            this.fade = Math.max(0, this.fade);
            this.fadeDirection = 1;
        }
    }
}