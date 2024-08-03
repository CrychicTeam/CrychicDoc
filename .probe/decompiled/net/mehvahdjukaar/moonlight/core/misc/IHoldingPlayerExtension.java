package net.mehvahdjukaar.moonlight.core.misc;

import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.map.CustomMapData;

public interface IHoldingPlayerExtension {

    void moonlight$setCustomMarkersDirty();

    <H extends CustomMapData.DirtyCounter> void moonlight$setCustomDataDirty(CustomMapData.Type<?> var1, Consumer<H> var2);
}