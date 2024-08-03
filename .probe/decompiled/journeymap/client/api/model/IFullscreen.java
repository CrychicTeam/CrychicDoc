package journeymap.client.api.model;

import journeymap.client.api.display.Context;
import journeymap.client.api.util.UIState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IFullscreen {

    void updateMapType(Context.MapType var1, Integer var2, ResourceKey<Level> var3);

    void toggleMapType();

    void zoomIn();

    void zoomOut();

    void centerOn(double var1, double var3);

    void close();

    UIState getUiState();

    Minecraft getMinecraft();

    Screen getScreen();
}