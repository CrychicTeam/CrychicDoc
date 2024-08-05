package org.violetmoon.zetaimplforge.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.client.ClientRegistryExtension;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class ForgeClientRegistryExtension extends ClientRegistryExtension {

    public ForgeClientRegistryExtension(Zeta z) {
        super(z);
    }

    @Override
    protected void doSetRenderLayer(Block block, RenderLayerRegistry.Layer layer) {
        ItemBlockRenderTypes.setRenderLayer(block, (RenderType) this.resolvedTypes.get(layer));
    }
}