package noppes.npcs.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.server.SPacketGuiOpen;

public class BlockBuilder extends BlockInterface {

    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);

    public BlockBuilder() {
        super(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.STONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack currentItem = player.getInventory().getSelected();
            if (currentItem.getItem() == CustomItems.wand || currentItem.getItem() == CustomBlocks.builder_item) {
                SPacketGuiOpen.sendOpenGui(player, EnumGuiType.BuilderBlock, null, pos);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int var6 = Mth.floor((double) (context.m_43723_().m_146908_() / 90.0F) + 0.5) & 3;
        if (!context.m_43725_().isClientSide) {
            SPacketGuiOpen.sendOpenGui(context.m_43723_(), EnumGuiType.BuilderBlock, null, context.getClickedPos());
        }
        return (BlockState) this.m_49966_().m_61124_(ROTATION, var6);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBuilder(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (TileBuilder.DrawPos != null && TileBuilder.DrawPos.equals(pos)) {
            TileBuilder.SetDrawPos(null);
        }
        super.m_6810_(state, level, pos, newState, isMoving);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return m_152132_(type, CustomBlocks.tile_builder, TileBuilder::tick);
    }
}