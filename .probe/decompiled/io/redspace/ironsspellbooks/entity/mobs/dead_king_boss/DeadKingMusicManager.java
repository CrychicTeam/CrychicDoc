package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber({ Dist.CLIENT })
public class DeadKingMusicManager {

    @Nullable
    private static DeadKingMusicManager INSTANCE;

    static final SoundSource SOUND_SOURCE = SoundSource.RECORDS;

    static final int FIRST_PHASE_MELODY_LENGTH_MILIS = 28790;

    static final int INTRO_LENGTH_MILIS = 17600;

    DeadKingBoss boss;

    final SoundManager soundManager;

    FadeableSoundInstance beginSound;

    FadeableSoundInstance firstPhaseMelody;

    FadeableSoundInstance firstPhaseAccent;

    FadeableSoundInstance secondPhaseMelody;

    FadeableSoundInstance transitionMusic;

    Set<FadeableSoundInstance> layers = new HashSet();

    private int accentStage = 0;

    private long lastMilisPlayed;

    private boolean hasPlayedIntro;

    DeadKingBoss.Phases stage;

    boolean finishing = false;

    private DeadKingMusicManager(DeadKingBoss boss) {
        this.boss = boss;
        this.soundManager = Minecraft.getInstance().getSoundManager();
        this.stage = DeadKingBoss.Phases.values()[boss.getPhase()];
        this.beginSound = new FadeableSoundInstance(SoundRegistry.DEAD_KING_MUSIC_INTRO.get(), SOUND_SOURCE, false);
        this.firstPhaseMelody = new FadeableSoundInstance(SoundRegistry.DEAD_KING_FIRST_PHASE_MELODY.get(), SOUND_SOURCE, true);
        this.firstPhaseAccent = new FadeableSoundInstance(SoundRegistry.DEAD_KING_FIRST_PHASE_ACCENT_01.get(), SOUND_SOURCE, false);
        this.secondPhaseMelody = new FadeableSoundInstance(SoundRegistry.DEAD_KING_SECOND_PHASE_MELODY_ALT.get(), SOUND_SOURCE, true);
        this.transitionMusic = new FadeableSoundInstance(SoundRegistry.DEAD_KING_SUSPENSE.get(), SOUND_SOURCE, false);
        this.init();
    }

    private void init() {
        this.soundManager.stop(null, SoundSource.MUSIC);
        switch(this.stage) {
            case FirstPhase:
                this.addLayer(this.beginSound);
                this.lastMilisPlayed = System.currentTimeMillis();
                break;
            case FinalPhase:
                this.initSecondPhase();
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (INSTANCE != null && event.phase == TickEvent.Phase.START && !Minecraft.getInstance().isPaused()) {
            INSTANCE.tick();
        }
    }

    public static void createOrResumeInstance(DeadKingBoss boss) {
        if (INSTANCE != null && !INSTANCE.isDone()) {
            INSTANCE.triggerResume(boss);
        } else if (ClientConfigs.ENABLE_BOSS_MUSIC.get()) {
            INSTANCE = new DeadKingMusicManager(boss);
        }
    }

    public static void stop(DeadKingBoss boss) {
        if (INSTANCE != null && INSTANCE.boss.m_20148_().equals(boss.m_20148_())) {
            INSTANCE.stopLayers();
            INSTANCE.finishing = true;
        }
    }

    private void tick() {
        if (!this.isDone() && !this.finishing) {
            if (!this.boss.m_21224_() && !this.boss.m_213877_()) {
                DeadKingBoss.Phases bossPhase = DeadKingBoss.Phases.values()[this.boss.getPhase()];
                switch(bossPhase) {
                    case FirstPhase:
                        if (!this.hasPlayedIntro) {
                            if (!this.soundManager.isActive(this.beginSound) || this.lastMilisPlayed + 17600L < System.currentTimeMillis()) {
                                this.hasPlayedIntro = true;
                                this.layers.remove(this.beginSound);
                                this.initFirstPhase();
                            }
                        } else if (this.lastMilisPlayed + 57580L < System.currentTimeMillis()) {
                            this.playAccent(this.firstPhaseAccent);
                        }
                        break;
                    case FinalPhase:
                        if (this.stage != DeadKingBoss.Phases.FinalPhase) {
                            this.stage = DeadKingBoss.Phases.FinalPhase;
                            this.initSecondPhase();
                        }
                        break;
                    case Transitioning:
                        if (this.stage != DeadKingBoss.Phases.Transitioning) {
                            this.stage = DeadKingBoss.Phases.Transitioning;
                            this.stopLayers();
                            this.addLayer(this.transitionMusic);
                        }
                }
            } else {
                this.stopLayers();
                this.finishing = true;
            }
        }
    }

    private boolean isDone() {
        for (FadeableSoundInstance soundInstance : this.layers) {
            if (!soundInstance.m_7801_() && this.soundManager.isActive(soundInstance)) {
                return false;
            }
        }
        return true;
    }

    private void addLayer(FadeableSoundInstance soundInstance) {
        this.layers.stream().filter(sound -> sound.m_7801_() || !this.soundManager.isActive(sound)).toList().forEach(this.layers::remove);
        this.soundManager.play(soundInstance);
        this.layers.add(soundInstance);
    }

    private void playAccent(FadeableSoundInstance soundInstance) {
        this.accentStage++;
        this.lastMilisPlayed = System.currentTimeMillis();
        this.addLayer(soundInstance);
    }

    public void stopLayers() {
        this.layers.forEach(FadeableSoundInstance::triggerStop);
    }

    public static void hardStop() {
        if (INSTANCE != null) {
            INSTANCE.layers.forEach(INSTANCE.soundManager::m_120399_);
            INSTANCE = null;
        }
    }

    public void triggerResume(DeadKingBoss boss) {
        if (boss.m_20148_().equals(this.boss.m_20148_())) {
            this.boss = boss;
        }
        if (!this.boss.m_213877_()) {
            this.layers.forEach(sound -> {
                sound.triggerStart();
                if (!this.soundManager.isActive(sound)) {
                    this.soundManager.play(sound);
                }
            });
            this.finishing = false;
        }
    }

    private void initFirstPhase() {
        this.accentStage = 0;
        this.addLayer(this.firstPhaseMelody);
        this.playAccent(this.firstPhaseAccent);
    }

    private void initSecondPhase() {
        this.accentStage = 0;
        this.addLayer(this.secondPhaseMelody);
    }
}