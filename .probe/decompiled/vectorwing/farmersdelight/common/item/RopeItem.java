package vectorwing.farmersdelight.common.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class RopeItem extends FuelBlockItem {

    public RopeItem(Block block, Item.Properties properties) {
        super(block, properties, 200);
    }

    @Nullable
    @Override
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.m_43725_();
        BlockState state = level.getBlockState(pos);
        Block block = this.m_40614_();
        if (state.m_60734_() != block) {
            return context;
        } else {
            Direction direction;
            if (context.m_7078_()) {
                direction = context.m_43719_();
            } else {
                direction = Direction.DOWN;
            }
            int i = 0;
            for (BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_()).move(direction); i < 256; i++) {
                state = level.getBlockState(blockpos$mutable);
                if (state.m_60734_() != this.m_40614_()) {
                    FluidState fluid = state.m_60819_();
                    if (!fluid.is(FluidTags.WATER) && !fluid.isEmpty()) {
                        return null;
                    }
                    if (state.m_60629_(context)) {
                        return BlockPlaceContext.at(context, blockpos$mutable, direction);
                    }
                    break;
                }
                if (direction != Direction.DOWN) {
                    return context;
                }
                blockpos$mutable.move(direction);
            }
            return null;
        }
    }

    @Override
    protected boolean mustSurvive() {
        return false;
    }
}