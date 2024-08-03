package org.violetmoon.zeta.client;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.client.event.load.ZAddBlockColorHandlers;
import org.violetmoon.zeta.client.event.load.ZAddItemColorHandlers;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.registry.DyeablesRegistry;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.registry.ZetaRegistry;

public abstract class ClientRegistryExtension {

    protected final Zeta z;

    protected final ZetaRegistry registry;

    protected final Map<RenderLayerRegistry.Layer, RenderType> resolvedTypes = new EnumMap(RenderLayerRegistry.Layer.class);

    public ClientRegistryExtension(Zeta z) {
        this.z = z;
        this.registry = z.registry;
        this.resolvedTypes.put(RenderLayerRegistry.Layer.SOLID, RenderType.solid());
        this.resolvedTypes.put(RenderLayerRegistry.Layer.CUTOUT, RenderType.cutout());
        this.resolvedTypes.put(RenderLayerRegistry.Layer.CUTOUT_MIPPED, RenderType.cutoutMipped());
        this.resolvedTypes.put(RenderLayerRegistry.Layer.TRANSLUCENT, RenderType.translucent());
    }

    @LoadEvent
    public void registerItemColorHandlers(ZAddItemColorHandlers event) {
        DyeablesRegistry dyeables = this.z.dyeables;
        ClampedItemPropertyFunction isDyed = (stack, level, entity, i) -> dyeables.isDyed(stack) ? 1.0F : 0.0F;
        ItemColor color = (stack, layer) -> layer == 0 ? dyeables.getColor(stack) : 16777215;
        ResourceLocation isDyedId = new ResourceLocation("minecraft", this.z.modid + "_dyed");
        for (Item item : dyeables.dyeableConditions.keySet()) {
            ItemProperties.register(item, isDyedId, isDyed);
            event.register(color, item);
        }
    }

    @LoadEvent
    public void registerBlockColorsPost(ZAddBlockColorHandlers.Post event) {
        this.registry.finalizeBlockColors((block, name) -> {
            Function<Block, BlockColor> blockColorCreator = (Function<Block, BlockColor>) event.getNamedBlockColors().get(name);
            if (blockColorCreator == null) {
                this.z.log.error("Unknown block color creator {} used on block {}", name, block);
            } else {
                event.register((BlockColor) blockColorCreator.apply(block), new Block[] { block });
            }
        });
    }

    @LoadEvent
    public void registerItemColorsPost(ZAddItemColorHandlers.Post event) {
        this.registry.finalizeItemColors((item, name) -> {
            Function<Item, ItemColor> itemColorCreator = (Function<Item, ItemColor>) event.getNamedItemColors().get(name);
            if (itemColorCreator == null) {
                this.z.log.error("Unknown item color creator {} used on item {}", name, item);
            } else {
                event.register((ItemColor) itemColorCreator.apply(item), new ItemLike[] { item });
            }
        });
    }

    @LoadEvent
    public void registerRenderLayers(ZClientSetup event) {
        this.z.renderLayerRegistry.finalize(this::doSetRenderLayer);
    }

    protected abstract void doSetRenderLayer(Block var1, RenderLayerRegistry.Layer var2);
}