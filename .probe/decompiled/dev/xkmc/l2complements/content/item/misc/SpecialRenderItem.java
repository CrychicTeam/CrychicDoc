package dev.xkmc.l2complements.content.item.misc;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class SpecialRenderItem extends TooltipItem {

    public SpecialRenderItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties, sup);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(LCBEWLR.EXTENSIONS);
    }
}