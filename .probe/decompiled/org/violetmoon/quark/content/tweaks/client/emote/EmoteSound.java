package org.violetmoon.quark.content.tweaks.client.emote;

import java.lang.ref.WeakReference;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EmoteSound extends AbstractSoundInstance implements TickableSoundInstance {

    protected boolean donePlaying;

    private final WeakReference<Player> player;

    private final EmoteTemplate template;

    private final boolean endWithSequence;

    public static void add(List<EmoteSound> allSounds, List<EmoteSound> sounds, Player player, EmoteTemplate template, ResourceLocation soundEvent, float volume, float pitch, boolean repeating, boolean endWithSequence) {
        EmoteSound emoteSound = new EmoteSound(player, template, soundEvent, volume, pitch, repeating, endWithSequence);
        sounds.add(emoteSound);
        allSounds.add(emoteSound);
        Minecraft.getInstance().getSoundManager().play(emoteSound);
    }

    public static void endAll(List<EmoteSound> sounds) {
        for (EmoteSound sound : sounds) {
            sound.donePlaying = true;
        }
    }

    public static void endSection(List<EmoteSound> sounds) {
        for (EmoteSound sound : sounds) {
            if (sound.endWithSequence) {
                sound.donePlaying = true;
            }
        }
    }

    public EmoteSound(Player player, EmoteTemplate template, ResourceLocation sound, float volume, float pitch, boolean repeating, boolean endWithSequence) {
        super(sound, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = new WeakReference(player);
        this.template = template;
        this.endWithSequence = endWithSequence;
        this.f_119573_ = volume;
        this.f_119574_ = pitch;
        if (repeating) {
            this.f_119578_ = true;
            this.f_119579_ = 0;
        }
    }

    @Override
    public void tick() {
        Player player = (Player) this.player.get();
        if (player != null && player.m_6084_()) {
            EmoteBase emote = EmoteHandler.getPlayerEmote(player);
            if (emote != null && emote.desc.template == this.template) {
                Vec3 pos = player.m_20182_();
                this.f_119575_ = (double) ((float) pos.x);
                this.f_119576_ = (double) ((float) pos.y);
                this.f_119577_ = (double) ((float) pos.z);
            } else {
                this.donePlaying = true;
            }
        } else {
            this.donePlaying = true;
        }
    }

    @Override
    public boolean isStopped() {
        return this.donePlaying;
    }
}