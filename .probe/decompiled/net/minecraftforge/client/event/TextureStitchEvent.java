package net.minecraftforge.client.event;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TextureStitchEvent extends Event implements IModBusEvent {

    private final TextureAtlas atlas;

    @Internal
    public TextureStitchEvent(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    public static class Post extends TextureStitchEvent {

        @Internal
        public Post(TextureAtlas map) {
            super(map);
        }
    }
}