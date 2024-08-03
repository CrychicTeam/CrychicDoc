package dev.architectury.registry.client.rendering.forge;

import com.google.common.collect.Lists;
import dev.architectury.platform.forge.EventBuses;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

public class ColorHandlerRegistryImpl {

    private static final List<Pair<ItemColor, Supplier<? extends ItemLike>[]>> ITEM_COLORS = Lists.newArrayList();

    private static final List<Pair<BlockColor, Supplier<? extends Block>[]>> BLOCK_COLORS = Lists.newArrayList();

    @SubscribeEvent
    public static void onItemColorEvent(RegisterColorHandlersEvent.Item event) {
        for (Pair<ItemColor, Supplier<? extends ItemLike>[]> pair : ITEM_COLORS) {
            event.register((ItemColor) pair.getLeft(), unpackItems((Supplier<? extends ItemLike>[]) pair.getRight()));
        }
    }

    @SubscribeEvent
    public static void onBlockColorEvent(RegisterColorHandlersEvent.Block event) {
        for (Pair<BlockColor, Supplier<? extends Block>[]> pair : BLOCK_COLORS) {
            event.register((BlockColor) pair.getLeft(), unpackBlocks((Supplier<? extends Block>[]) pair.getRight()));
        }
    }

    @SafeVarargs
    public static void registerItemColors(ItemColor itemColor, Supplier<? extends ItemLike>... items) {
        Objects.requireNonNull(itemColor, "color is null!");
        if (Minecraft.getInstance().getItemColors() == null) {
            ITEM_COLORS.add(Pair.of(itemColor, items));
        } else {
            Minecraft.getInstance().getItemColors().register(itemColor, unpackItems(items));
        }
    }

    @SafeVarargs
    public static void registerBlockColors(BlockColor blockColor, Supplier<? extends Block>... blocks) {
        Objects.requireNonNull(blockColor, "color is null!");
        if (Minecraft.getInstance().getBlockColors() == null) {
            BLOCK_COLORS.add(Pair.of(blockColor, blocks));
        } else {
            Minecraft.getInstance().getBlockColors().register(blockColor, unpackBlocks(blocks));
        }
    }

    private static ItemLike[] unpackItems(Supplier<? extends ItemLike>[] items) {
        ItemLike[] array = new ItemLike[items.length];
        for (int i = 0; i < items.length; i++) {
            array[i] = (ItemLike) Objects.requireNonNull((ItemLike) items[i].get());
        }
        return array;
    }

    private static Block[] unpackBlocks(Supplier<? extends Block>[] blocks) {
        Block[] array = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            array[i] = (Block) Objects.requireNonNull((Block) blocks[i].get());
        }
        return array;
    }

    static {
        EventBuses.onRegistered("architectury", bus -> bus.register(ColorHandlerRegistryImpl.class));
    }
}