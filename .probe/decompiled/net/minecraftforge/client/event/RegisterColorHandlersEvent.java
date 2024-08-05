package net.minecraftforge.client.event;

import com.google.common.collect.ImmutableList.Builder;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class RegisterColorHandlersEvent extends Event implements IModBusEvent {

    @Internal
    protected RegisterColorHandlersEvent() {
    }

    public static class Block extends RegisterColorHandlersEvent {

        private final BlockColors blockColors;

        @Internal
        public Block(BlockColors blockColors) {
            this.blockColors = blockColors;
        }

        public BlockColors getBlockColors() {
            return this.blockColors;
        }

        public void register(BlockColor blockColor, net.minecraft.world.level.block.Block... blocks) {
            this.blockColors.register(blockColor, blocks);
        }
    }

    public static class ColorResolvers extends RegisterColorHandlersEvent {

        private final Builder<ColorResolver> builder;

        @Internal
        public ColorResolvers(Builder<ColorResolver> builder) {
            this.builder = builder;
        }

        public void register(ColorResolver resolver) {
            this.builder.add(resolver);
        }
    }

    public static class Item extends RegisterColorHandlersEvent {

        private final ItemColors itemColors;

        private final BlockColors blockColors;

        @Internal
        public Item(ItemColors itemColors, BlockColors blockColors) {
            this.itemColors = itemColors;
            this.blockColors = blockColors;
        }

        public ItemColors getItemColors() {
            return this.itemColors;
        }

        public BlockColors getBlockColors() {
            return this.blockColors;
        }

        public void register(ItemColor itemColor, ItemLike... items) {
            this.itemColors.register(itemColor, items);
        }
    }
}