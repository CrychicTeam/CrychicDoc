package se.mickelus.tetra.client.model;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.client.model.IQuadTransformer;

public class QuadTransformerBuilder {

    private final Int2ObjectMap<List<IQuadTransformer>> transformers = new Int2ObjectOpenHashMap();

    public QuadTransformerBuilder add(int layer, IQuadTransformer transformer) {
        if (!this.transformers.containsKey(layer)) {
            this.transformers.put(layer, new ArrayList());
        }
        ((List) this.transformers.get(layer)).add(transformer);
        return this;
    }

    public Int2ObjectMap<List<IQuadTransformer>> get() {
        return this.transformers;
    }
}