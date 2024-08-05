package net.mehvahdjukaar.supplementaries.common.items;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.items.BubbleBlockItemRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BubbleBlockItem extends BlockItem implements ICustomItemRendererProvider {

    public BubbleBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return BubbleBlockItemRenderer::new;
    }
}