package journeymap.client.event.handlers;

import journeymap.client.JourneymapClient;
import journeymap.client.task.main.EnsureCurrentColorsTask;
import journeymap.client.task.main.IMainThreadTask;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;

public class TextureAtlasHandler {

    IMainThreadTask task = new EnsureCurrentColorsTask();

    public void onTextureStitched() {
        try {
            TextureCache.reset();
            UIManager.INSTANCE.getMiniMap().reset();
            Fullscreen.state().requireRefresh();
            MiniMap.state().requireRefresh();
            JourneymapClient.getInstance().queueMainThreadTask(this.task);
        } catch (Exception var2) {
            Journeymap.getLogger().warn("Error queuing TextureAtlasHandlerTask: " + LogFormatter.toString(var2));
        }
    }
}