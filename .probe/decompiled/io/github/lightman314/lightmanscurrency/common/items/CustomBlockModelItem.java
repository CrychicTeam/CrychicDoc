package io.github.lightman314.lightmanscurrency.common.items;

import io.github.lightman314.lightmanscurrency.client.renderer.LCItemRenderer;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class CustomBlockModelItem extends BlockItem {

    public CustomBlockModelItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(LCItemRenderer.USE_LC_RENDERER);
    }
}