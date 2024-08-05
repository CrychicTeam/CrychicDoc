package team.lodestar.lodestone.systems.particle.screen;

import java.util.Objects;

public class ScreenParticleItemStackRetrievalKey {

    public final boolean isHotbarItem;

    public final boolean isRenderedAfterItem;

    public final int x;

    public final int y;

    public ScreenParticleItemStackRetrievalKey(boolean isHotbarItem, boolean isRenderedAfterItem, int x, int y) {
        this.isHotbarItem = isHotbarItem;
        this.isRenderedAfterItem = isRenderedAfterItem;
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ScreenParticleItemStackRetrievalKey key) ? false : key.isHotbarItem == this.isHotbarItem && key.isRenderedAfterItem == this.isRenderedAfterItem && key.x == this.x && key.y == this.y;
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.isHotbarItem, this.isRenderedAfterItem, this.x, this.y });
    }
}