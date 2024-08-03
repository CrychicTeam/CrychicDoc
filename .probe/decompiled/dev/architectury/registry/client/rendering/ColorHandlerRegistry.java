package dev.architectury.registry.client.rendering;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.client.rendering.forge.ColorHandlerRegistryImpl;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ColorHandlerRegistry {

    private ColorHandlerRegistry() {
    }

    public static void registerItemColors(ItemColor color, ItemLike... items) {
        Supplier<ItemLike>[] array = new Supplier[items.length];
        for (int i = 0; i < items.length; i++) {
            ItemLike item = (ItemLike) Objects.requireNonNull(items[i], "items[i] is null!");
            array[i] = () -> item;
        }
        registerItemColors(color, array);
    }

    public static void registerBlockColors(BlockColor color, Block... blocks) {
        Supplier<Block>[] array = new Supplier[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            Block block = (Block) Objects.requireNonNull(blocks[i], "blocks[i] is null!");
            array[i] = () -> block;
        }
        registerBlockColors(color, array);
    }

    @SafeVarargs
    @ExpectPlatform
    @Transformed
    public static void registerItemColors(ItemColor color, Supplier<? extends ItemLike>... items) {
        ColorHandlerRegistryImpl.registerItemColors(color, items);
    }

    @SafeVarargs
    @ExpectPlatform
    @Transformed
    public static void registerBlockColors(BlockColor color, Supplier<? extends Block>... blocks) {
        ColorHandlerRegistryImpl.registerBlockColors(color, blocks);
    }
}