package org.violetmoon.zeta.event.load;

import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.advancement.AdvancementModifierRegistry;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;
import org.violetmoon.zeta.registry.BrewingRegistry;
import org.violetmoon.zeta.registry.CraftingExtensionsRegistry;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.registry.VariantRegistry;
import org.violetmoon.zeta.registry.ZetaRegistry;

public class ZRegister implements IZetaLoadEvent {

    public final Zeta zeta;

    public ZRegister(Zeta zeta) {
        this.zeta = zeta;
    }

    public ZetaRegistry getRegistry() {
        return this.zeta.registry;
    }

    public CraftingExtensionsRegistry getCraftingExtensionsRegistry() {
        return this.zeta.craftingExtensions;
    }

    public BrewingRegistry getBrewingRegistry() {
        return this.zeta.brewingRegistry;
    }

    public RenderLayerRegistry getRenderLayerRegistry() {
        return this.zeta.renderLayerRegistry;
    }

    public AdvancementModifierRegistry getAdvancementModifierRegistry() {
        return this.zeta.advancementModifierRegistry;
    }

    public VariantRegistry getVariantRegistry() {
        return this.zeta.variantRegistry;
    }

    public static class Post implements IZetaLoadEvent {
    }
}