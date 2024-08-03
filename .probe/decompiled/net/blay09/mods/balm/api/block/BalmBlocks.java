package net.blay09.mods.balm.api.block;

import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

public interface BalmBlocks {

    BlockBehaviour.Properties blockProperties();

    DeferredObject<Block> registerBlock(Supplier<Block> var1, ResourceLocation var2);

    default DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return this.registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    DeferredObject<Item> registerBlockItem(Supplier<BlockItem> var1, ResourceLocation var2, @Nullable ResourceLocation var3);

    default void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        this.register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    void register(Supplier<Block> var1, Supplier<BlockItem> var2, ResourceLocation var3, @Nullable ResourceLocation var4);
}