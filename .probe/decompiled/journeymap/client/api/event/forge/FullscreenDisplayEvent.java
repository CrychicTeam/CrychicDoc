package journeymap.client.api.event.forge;

import journeymap.client.api.display.CustomToolBarBuilder;
import journeymap.client.api.display.ThemeButtonDisplay;
import journeymap.client.api.model.IFullscreen;
import net.minecraftforge.eventbus.api.Event;

public class FullscreenDisplayEvent extends Event {

    private final IFullscreen fullscreen;

    private final ThemeButtonDisplay themeButtonDisplay;

    private FullscreenDisplayEvent(IFullscreen fullscreen, ThemeButtonDisplay themeButtonDisplay) {
        this.fullscreen = fullscreen;
        this.themeButtonDisplay = themeButtonDisplay;
    }

    public IFullscreen getFullscreen() {
        return this.fullscreen;
    }

    public ThemeButtonDisplay getThemeButtonDisplay() {
        return this.themeButtonDisplay;
    }

    public static class AddonButtonDisplayEvent extends FullscreenDisplayEvent {

        public AddonButtonDisplayEvent(IFullscreen fullscreen, ThemeButtonDisplay themeButtonDisplay) {
            super(fullscreen, themeButtonDisplay);
        }
    }

    @Deprecated
    public static class CustomToolbarEvent extends Event {

        private final CustomToolBarBuilder customToolBarBuilder;

        private final IFullscreen fullscreen;

        public CustomToolbarEvent(IFullscreen fullscreen, CustomToolBarBuilder customToolBarBuilder) {
            this.fullscreen = fullscreen;
            this.customToolBarBuilder = customToolBarBuilder;
        }

        public CustomToolBarBuilder getCustomToolBarBuilder() {
            return this.customToolBarBuilder;
        }

        public IFullscreen getFullscreen() {
            return this.fullscreen;
        }
    }

    @Deprecated
    public static class MapTypeButtonDisplayEvent extends FullscreenDisplayEvent {

        public MapTypeButtonDisplayEvent(IFullscreen fullscreen, ThemeButtonDisplay themeButtonDisplay) {
            super(fullscreen, themeButtonDisplay);
        }
    }
}