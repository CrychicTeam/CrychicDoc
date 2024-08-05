package journeymap.client.api.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.awt.geom.Rectangle2D.Double;
import javax.annotation.Nullable;
import journeymap.client.api.display.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public final class UIState {

    public final Context.UI ui;

    public final boolean active;

    public final ResourceKey<Level> dimension;

    public final int zoom;

    public final Context.MapType mapType;

    public final BlockPos mapCenter;

    public final Integer chunkY;

    public final AABB blockBounds;

    public final Double displayBounds;

    public final double blockSize;

    public UIState(Context.UI ui, boolean active, ResourceKey<Level> dimension, int zoom, @Nullable Context.MapType mapType, @Nullable BlockPos mapCenter, @Nullable Integer chunkY, @Nullable AABB blockBounds, @Nullable Double displayBounds) {
        this.ui = ui;
        this.active = active;
        this.dimension = dimension;
        this.zoom = zoom;
        this.mapType = mapType;
        this.mapCenter = mapCenter;
        this.chunkY = chunkY;
        this.blockBounds = blockBounds;
        this.displayBounds = displayBounds;
        this.blockSize = Math.pow(2.0, (double) zoom);
    }

    public static UIState newInactive(Context.UI ui, Minecraft minecraft) {
        BlockPos center = minecraft.level == null ? new BlockPos(0, 68, 0) : minecraft.level.m_220360_();
        return new UIState(ui, false, Level.OVERWORLD, 0, Context.MapType.Day, center, null, null, null);
    }

    public static UIState newInactive(UIState priorState) {
        return new UIState(priorState.ui, false, priorState.dimension, priorState.zoom, priorState.mapType, priorState.mapCenter, priorState.chunkY, priorState.blockBounds, priorState.displayBounds);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            UIState mapState = (UIState) o;
            return Objects.equal(this.active, mapState.active) && Objects.equal(this.dimension, mapState.dimension) && Objects.equal(this.zoom, mapState.zoom) && Objects.equal(this.ui, mapState.ui) && Objects.equal(this.mapType, mapState.mapType) && Objects.equal(this.displayBounds, mapState.displayBounds);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.ui, this.active, this.dimension, this.zoom, this.mapType, this.displayBounds });
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("ui", this.ui).add("active", this.active).add("dimension", this.dimension).add("mapType", this.mapType).add("zoom", this.zoom).add("mapCenter", this.mapCenter).add("chunkY", this.chunkY).add("blockBounds", this.blockBounds).add("displayBounds", this.displayBounds).add("blockSize", this.blockSize).toString();
    }
}