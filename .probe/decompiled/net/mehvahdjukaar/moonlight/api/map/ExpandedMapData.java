package net.mehvahdjukaar.moonlight.api.map;

import java.util.Map;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface ExpandedMapData {

    @Internal
    Map<String, CustomMapDecoration> getCustomDecorations();

    @Internal
    Map<String, MapBlockMarker<?>> getCustomMarkers();

    @Internal
    Map<ResourceLocation, CustomMapData<?>> getCustomData();

    boolean toggleCustomDecoration(LevelAccessor var1, BlockPos var2);

    void resetCustomDecoration();

    int getVanillaDecorationSize();

    <M extends MapBlockMarker<?>> void addCustomMarker(M var1);

    boolean removeCustomMarker(String var1);

    MapItemSavedData copy();

    void setCustomDecorationsDirty();

    <H extends CustomMapData.DirtyCounter> void setCustomDataDirty(CustomMapData.Type<?> var1, Consumer<H> var2);
}