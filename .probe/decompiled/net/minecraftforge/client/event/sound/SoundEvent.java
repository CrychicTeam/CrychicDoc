package net.minecraftforge.client.event.sound;

import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class SoundEvent extends Event {

    private final SoundEngine engine;

    @Internal
    protected SoundEvent(SoundEngine engine) {
        this.engine = engine;
    }

    public SoundEngine getEngine() {
        return this.engine;
    }

    public abstract static class SoundSourceEvent extends SoundEvent {

        private final SoundInstance sound;

        private final Channel channel;

        private final String name;

        @Internal
        protected SoundSourceEvent(SoundEngine engine, SoundInstance sound, Channel channel) {
            super(engine);
            this.name = sound.getLocation().getPath();
            this.sound = sound;
            this.channel = channel;
        }

        public SoundInstance getSound() {
            return this.sound;
        }

        public Channel getChannel() {
            return this.channel;
        }

        public String getName() {
            return this.name;
        }
    }
}