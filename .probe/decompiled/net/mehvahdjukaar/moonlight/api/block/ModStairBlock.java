package net.mehvahdjukaar.moonlight.api.block;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModStairBlock extends StairBlock {

    private static final Field FORGE_BLOCK_SUPPLIER = PlatHelper.findField(StairBlock.class, "stateSupplier");

    public ModStairBlock(Supplier<Block> baseBlock, BlockBehaviour.Properties settings) {
        super(FORGE_BLOCK_SUPPLIER == null ? ((Block) Preconditions.checkNotNull((Block) baseBlock.get(), "Stairs block was given a null base block!")).defaultBlockState() : Blocks.AIR.defaultBlockState(), settings);
        if (FORGE_BLOCK_SUPPLIER != null) {
            FORGE_BLOCK_SUPPLIER.setAccessible(true);
            try {
                FORGE_BLOCK_SUPPLIER.set(this, (Supplier) () -> ((Block) baseBlock.get()).defaultBlockState());
            } catch (Exception var4) {
            }
        }
    }
}