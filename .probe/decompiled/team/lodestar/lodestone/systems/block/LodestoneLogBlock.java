package team.lodestar.lodestone.systems.block;

import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

public class LodestoneLogBlock extends RotatedPillarBlock {

    public final Supplier<Block> stripped;

    public LodestoneLogBlock(BlockBehaviour.Properties properties, Supplier<Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Nullable
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        return toolAction.equals(ToolActions.AXE_STRIP) ? (BlockState) ((Block) this.stripped.get()).defaultBlockState().m_61124_(f_55923_, (Direction.Axis) state.m_61143_(f_55923_)) : super.getToolModifiedState(state, context, toolAction, simulate);
    }
}