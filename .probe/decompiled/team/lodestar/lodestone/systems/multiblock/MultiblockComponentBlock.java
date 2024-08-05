package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.world.level.block.state.BlockBehaviour;
import team.lodestar.lodestone.registry.common.LodestoneBlockEntityRegistry;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;

public class MultiblockComponentBlock extends LodestoneEntityBlock<MultiBlockComponentEntity> implements ILodestoneMultiblockComponent {

    public MultiblockComponentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.setBlockEntity(LodestoneBlockEntityRegistry.MULTIBLOCK_COMPONENT);
    }
}