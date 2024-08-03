package noppes.npcs.blocks;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BlockInterface extends BaseEntityBlock implements EntityBlock {

    protected BlockInterface(BlockBehaviour.Properties properties) {
        super(properties);
    }
}