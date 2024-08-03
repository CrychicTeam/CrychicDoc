package dev.xkmc.l2backpack.content.restore;

import dev.xkmc.l2screentracker.screen.source.ItemSourceData;
import java.util.UUID;

public record DimensionSourceData(int color, int slot, UUID uuid) implements ItemSourceData<DimensionSourceData> {
}