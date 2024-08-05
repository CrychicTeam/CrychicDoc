package net.minecraft.client.model.geom;

import net.minecraft.resources.ResourceLocation;

public final class ModelLayerLocation {

    private final ResourceLocation model;

    private final String layer;

    public ModelLayerLocation(ResourceLocation resourceLocation0, String string1) {
        this.model = resourceLocation0;
        this.layer = string1;
    }

    public ResourceLocation getModel() {
        return this.model;
    }

    public String getLayer() {
        return this.layer;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof ModelLayerLocation $$1) ? false : this.model.equals($$1.model) && this.layer.equals($$1.layer);
        }
    }

    public int hashCode() {
        int $$0 = this.model.hashCode();
        return 31 * $$0 + this.layer.hashCode();
    }

    public String toString() {
        return this.model + "#" + this.layer;
    }
}