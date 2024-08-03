package net.mehvahdjukaar.moonlight.core.misc;

import java.util.Collection;
import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public interface IMapDataPacketExtension {

    void moonlight$sendCustomDecorations(Collection<CustomMapDecoration> var1);

    void moonlight$sendCustomMapDataTag(CompoundTag var1);

    CompoundTag moonlight$getCustomMapDataTag();

    MapItemSavedData.MapPatch moonlight$getColorPatch();

    ResourceKey<Level> moonlight$getDimension();
}