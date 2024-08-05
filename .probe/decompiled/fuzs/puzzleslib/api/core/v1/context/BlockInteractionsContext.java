package fuzs.puzzleslib.api.core.v1.context;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockInteractionsContext {

    void registerStrippable(Block var1, Block... var2);

    void registerScrapeable(Block var1, Block... var2);

    void registerWaxable(Block var1, Block... var2);

    default void registerFlattenable(Block flattenedBlock, Block... unflattenedBlocks) {
        this.registerFlattenable(flattenedBlock.defaultBlockState(), unflattenedBlocks);
    }

    void registerFlattenable(BlockState var1, Block... var2);

    default void registerTillable(Block tilledBlock, Block... untilledBlocks) {
        this.registerTillable(tilledBlock.defaultBlockState(), null, true, untilledBlocks);
    }

    default void registerTillable(Block tilledBlock, @Nullable ItemLike droppedItem, Block... untilledBlocks) {
        this.registerTillable(tilledBlock.defaultBlockState(), droppedItem, false, untilledBlocks);
    }

    void registerTillable(BlockState var1, @Nullable ItemLike var2, boolean var3, Block... var4);
}