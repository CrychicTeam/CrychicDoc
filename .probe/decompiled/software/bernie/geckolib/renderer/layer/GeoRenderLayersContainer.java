package software.bernie.geckolib.renderer.layer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GeoRenderLayersContainer<T extends GeoAnimatable> {

    private final GeoRenderer<T> renderer;

    private final List<GeoRenderLayer<T>> layers = new ObjectArrayList();

    private boolean compiledLayers = false;

    public GeoRenderLayersContainer(GeoRenderer<T> renderer) {
        this.renderer = renderer;
    }

    public List<GeoRenderLayer<T>> getRenderLayers() {
        if (!this.compiledLayers) {
            this.fireCompileRenderLayersEvent();
        }
        return this.layers;
    }

    public void addLayer(GeoRenderLayer<T> layer) {
        this.layers.add(layer);
    }

    public void fireCompileRenderLayersEvent() {
        this.compiledLayers = true;
        this.renderer.fireCompileRenderLayersEvent();
    }
}