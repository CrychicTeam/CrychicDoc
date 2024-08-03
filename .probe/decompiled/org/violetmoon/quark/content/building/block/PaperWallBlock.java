package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaInheritedPaneBlock;

public class PaperWallBlock extends ZetaInheritedPaneBlock {

    public PaperWallBlock(IZetaBlock parent, String name) {
        super(parent, name, BlockBehaviour.Properties.copy(parent.getBlock()).lightLevel(b -> 0));
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 30;
    }

    @Override
    public int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }
}