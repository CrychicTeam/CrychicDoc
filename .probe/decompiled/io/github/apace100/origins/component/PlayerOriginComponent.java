package io.github.apace100.origins.component;

import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import java.util.HashMap;

@Deprecated
public class PlayerOriginComponent implements OriginComponent {

    private final IOriginContainer wrapped;

    private final HashMap<OriginLayer, Origin> origins = new HashMap();

    public PlayerOriginComponent(IOriginContainer wrapped) {
        this.wrapped = wrapped;
    }

    @Deprecated
    @Override
    public boolean hasAllOrigins() {
        return this.wrapped.hasAllOrigins();
    }

    @Deprecated
    @Override
    public HashMap<OriginLayer, Origin> getOrigins() {
        this.origins.clear();
        this.wrapped.getOrigins().forEach((x, y) -> this.origins.put(OriginLayers.getLayer(x.location()), OriginRegistry.get(y.location())));
        return this.origins;
    }

    @Deprecated
    @Override
    public boolean hasOrigin(OriginLayer layer) {
        return this.wrapped.hasOrigin(layer.getWrapped());
    }

    @Deprecated
    @Override
    public Origin getOrigin(OriginLayer layer) {
        return OriginRegistry.get(this.wrapped.getOrigin(layer.getWrapped()).location());
    }

    @Deprecated
    @Override
    public boolean hadOriginBefore() {
        return this.wrapped.hasAllOrigins();
    }

    @Deprecated
    @Override
    public void setOrigin(OriginLayer layer, Origin origin) {
        this.wrapped.setOrigin(layer.getWrapped(), origin.getWrapped());
    }

    @Deprecated
    @Override
    public void sync() {
        OriginComponent.sync(this.wrapped.getOwner());
    }
}