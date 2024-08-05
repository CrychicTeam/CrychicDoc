package org.violetmoon.zeta.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import net.minecraft.world.level.block.Block;

public class RenderLayerRegistry {

    protected Map<Block, RenderLayerRegistry.Layer> mapping = new HashMap();

    protected Map<Block, Block> inheritances = new HashMap();

    public void put(Block block, RenderLayerRegistry.Layer layer) {
        if (this.mapping == null) {
            throw new IllegalStateException("Already finalized RenderLayerRegistry");
        } else {
            this.mapping.put(block, layer);
        }
    }

    public void mock(Block block, Block inheritFrom) {
        if (this.inheritances == null) {
            throw new IllegalStateException("Already finalized RenderLayerRegistry");
        } else {
            this.inheritances.put(block, inheritFrom);
        }
    }

    public void finalize(BiConsumer<Block, RenderLayerRegistry.Layer> action) {
        for (Block b : this.inheritances.keySet()) {
            Block inheritFrom = (Block) this.inheritances.get(b);
            RenderLayerRegistry.Layer layer = (RenderLayerRegistry.Layer) this.mapping.get(inheritFrom);
            if (layer != null) {
                this.mapping.put(b, layer);
            }
        }
        this.mapping.forEach(action);
        this.mapping = null;
        this.inheritances = null;
    }

    public static enum Layer {

        SOLID, CUTOUT, CUTOUT_MIPPED, TRANSLUCENT
    }
}