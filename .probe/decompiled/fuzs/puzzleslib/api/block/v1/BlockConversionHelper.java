package fuzs.puzzleslib.api.block.v1;

import fuzs.puzzleslib.mixin.accessor.BlockAccessor;
import fuzs.puzzleslib.mixin.accessor.BlockItemAccessor;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class BlockConversionHelper {

    private BlockConversionHelper() {
    }

    public static void setBlockItemBlock(BlockItem item, Block block) {
        setItemForBlock(block, item);
        setBlockForItem(item, block);
    }

    public static void setItemForBlock(Block block, Item item) {
        Objects.requireNonNull(block, "block " + (item != null ? "for item '" + BuiltInRegistries.ITEM.getKey(item) + "' " : "") + "is null");
        Objects.requireNonNull(item, "item for block '" + BuiltInRegistries.BLOCK.getKey(block) + "' is null");
        Item.BY_BLOCK.put(block, item);
        ((BlockAccessor) block).puzzleslib$setItem(item);
    }

    public static void setBlockForItem(BlockItem item, Block block) {
        Objects.requireNonNull(item, "item " + (block != null ? "for block '" + BuiltInRegistries.BLOCK.getKey(block) + "' " : "") + "is null");
        Objects.requireNonNull(block, "block for item '" + BuiltInRegistries.ITEM.getKey(item) + "' is null");
        Block oldBlock = item.getBlock();
        if (oldBlock != null) {
            ((BlockAccessor) oldBlock).puzzleslib$setItem(item);
        }
        ((BlockItemAccessor) item).puzzleslib$setBlock(block);
    }

    public static void copyBoundTags(Block from, Block to) {
        Objects.requireNonNull(from, "source " + (to != null ? "for target '" + BuiltInRegistries.BLOCK.getKey(to) + "' " : "") + "is null");
        Objects.requireNonNull(to, "target for source '" + BuiltInRegistries.BLOCK.getKey(from) + "' is null");
        if (to.builtInRegistryHolder().tags().findAny().isPresent()) {
            throw new IllegalStateException("target block tags not empty");
        } else {
            List<TagKey<Block>> tagKeys = from.builtInRegistryHolder().tags().toList();
            to.builtInRegistryHolder().bindTags(tagKeys);
        }
    }
}