package net.mehvahdjukaar.moonlight.api.set;

import java.util.Collection;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.core.set.BlockSetInternal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BlockSetAPI {

    public static <T extends BlockType> void registerBlockSetDefinition(BlockTypeRegistry<T> typeRegistry) {
        BlockSetInternal.registerBlockSetDefinition(typeRegistry);
    }

    public static <T extends BlockType> void addBlockTypeFinder(Class<T> type, BlockType.SetFinder<T> blockFinder) {
        BlockSetInternal.addBlockTypeFinder(type, blockFinder);
    }

    public static <T extends BlockType> void addBlockTypeRemover(Class<T> type, ResourceLocation id) {
        BlockSetInternal.addBlockTypeRemover(type, id);
    }

    public static <T extends BlockType> BlockTypeRegistry<T> getBlockSet(Class<T> type) {
        return BlockSetInternal.getBlockSet(type);
    }

    public static <T extends BlockType> void addDynamicBlockRegistration(BlockSetAPI.BlockTypeRegistryCallback<Block, T> registrationFunction, Class<T> blockType) {
        addDynamicRegistration(registrationFunction, blockType, BuiltInRegistries.BLOCK);
    }

    public static <T extends BlockType> void addDynamicItemRegistration(BlockSetAPI.BlockTypeRegistryCallback<Item, T> registrationFunction, Class<T> blockType) {
        addDynamicRegistration(registrationFunction, blockType, BuiltInRegistries.ITEM);
    }

    public static <T extends BlockType, E> void addDynamicRegistration(BlockSetAPI.BlockTypeRegistryCallback<E, T> registrationFunction, Class<T> blockType, Registry<E> registry) {
        BlockSetInternal.addDynamicRegistration(registrationFunction, blockType, registry);
    }

    public static Collection<BlockTypeRegistry<?>> getRegistries() {
        return BlockSetInternal.getRegistries();
    }

    @Nullable
    public static <T extends BlockType> BlockTypeRegistry<T> getTypeRegistry(Class<T> typeClass) {
        return BlockSetInternal.getRegistry(typeClass);
    }

    @Nullable
    public static <T extends BlockType> T getBlockTypeOf(ItemLike itemLike, Class<T> typeClass) {
        return BlockSetInternal.getBlockTypeOf(itemLike, typeClass);
    }

    @Nullable
    public static Object changeType(Object current, BlockType originalMat, BlockType destinationMat) {
        return BlockType.changeType(current, originalMat, destinationMat);
    }

    @Nullable
    public static Item changeItemType(Item current, BlockType originalMat, BlockType destinationMat) {
        return BlockType.changeItemType(current, originalMat, destinationMat);
    }

    @Nullable
    public static Block changeBlockType(Block current, BlockType originalMat, BlockType destinationMat) {
        return BlockType.changeBlockType(current, originalMat, destinationMat);
    }

    @FunctionalInterface
    public interface BlockTypeRegistryCallback<E, T extends BlockType> {

        void accept(Registrator<E> var1, Collection<T> var2);
    }
}