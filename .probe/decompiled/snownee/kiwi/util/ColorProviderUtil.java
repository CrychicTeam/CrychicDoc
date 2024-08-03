package snownee.kiwi.util;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.mixin.forge.BlockColorsAccess;
import snownee.kiwi.mixin.forge.ItemColorsAccess;

public class ColorProviderUtil {

    public static BlockColor delegate(Block block) {
        return new ColorProviderUtil.BlockDelegate(() -> {
            BlockColorsAccess blockColors = (BlockColorsAccess) Minecraft.getInstance().getBlockColors();
            return (BlockColor) blockColors.getBlockColors().get(ForgeRegistries.BLOCKS.getDelegateOrThrow(block));
        });
    }

    public static ItemColor delegate(Item item) {
        return new ColorProviderUtil.ItemDelegate(() -> {
            ItemColorsAccess itemColors = (ItemColorsAccess) Minecraft.getInstance().getItemColors();
            return (ItemColor) itemColors.getItemColors().get(ForgeRegistries.ITEMS.getDelegateOrThrow(item));
        });
    }

    public static ItemColor delegateItemFallback(Block block) {
        return new ColorProviderUtil.ItemDelegate(() -> {
            BlockColorsAccess blockColors = (BlockColorsAccess) Minecraft.getInstance().getBlockColors();
            BlockColor blockColor = (BlockColor) blockColors.getBlockColors().get(ForgeRegistries.BLOCKS.getDelegateOrThrow(block));
            return blockColor == null ? null : (stack, i) -> blockColor.getColor(block.defaultBlockState(), null, null, i);
        });
    }

    private static class BlockDelegate extends CachedSupplier<BlockColor> implements BlockColor {

        public BlockDelegate(Supplier<BlockColor> getter) {
            super(getter, ColorProviderUtil.Dummy.INSTANCE);
        }

        @Override
        public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
            return this.get().getColor(blockState, blockAndTintGetter, blockPos, i);
        }
    }

    public static class Dummy implements ItemColor, BlockColor {

        public static final ColorProviderUtil.Dummy INSTANCE = new ColorProviderUtil.Dummy();

        @Override
        public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
            return -1;
        }

        @Override
        public int getColor(ItemStack itemStack, int i) {
            return -1;
        }
    }

    private static class ItemDelegate extends CachedSupplier<ItemColor> implements ItemColor {

        public ItemDelegate(Supplier<ItemColor> getter) {
            super(getter, ColorProviderUtil.Dummy.INSTANCE);
        }

        @Override
        public int getColor(ItemStack itemStack, int i) {
            return this.get().getColor(itemStack, i);
        }
    }
}