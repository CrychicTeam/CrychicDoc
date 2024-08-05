package journeymap.client.event.forge;

import journeymap.client.event.handlers.TextureAtlasHandler;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTextureStitchedEvent implements ForgeEventHandlerManager.EventHandler {

    private final TextureAtlasHandler textureAtlasHandler = new TextureAtlasHandler();

    @SubscribeEvent
    public void onTextureStitched(TextureStitchEvent.Post event) {
        this.textureAtlasHandler.onTextureStitched();
    }
}