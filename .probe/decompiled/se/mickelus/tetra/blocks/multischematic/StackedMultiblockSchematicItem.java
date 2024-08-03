package se.mickelus.tetra.blocks.multischematic;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StackedMultiblockSchematicItem extends BaseMultiblockSchematicItem {

    Block ruinedBlock;

    public StackedMultiblockSchematicItem(MultiblockSchematicBlock block, Block ruinedBlock) {
        super(block, block);
        this.ruinedBlock = ruinedBlock;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.addAll(this.getTooltip());
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        if (context.m_43723_() != null && context.m_43723_().isCreative() && context.m_43723_().m_6047_()) {
            BlockState ruinedState = this.ruinedBlock.getStateForPlacement(context);
            return ruinedState != null && this.m_40610_(context, ruinedState) ? ruinedState : null;
        } else {
            return super.m_5965_(context);
        }
    }
}