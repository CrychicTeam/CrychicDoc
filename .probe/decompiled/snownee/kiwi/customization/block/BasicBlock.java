package snownee.kiwi.customization.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BasicBlock extends Block implements CheckedWaterloggedBlock, KBlockUtils {

    public BasicBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}