package com.mna.blocks.decoration;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.BookshelfTile;
import com.mna.blocks.utility.WaterloggableBlockWithOffset;
import com.mna.gui.containers.block.ContainerBookshelf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BookshelfBlock extends WaterloggableBlockWithOffset implements EntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable(RLoc.create("container.bookshelf").toString());

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BookshelfBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion(), false, new BlockPos(0, 1, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookshelfTile(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos, state);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof BookshelfTile) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider provider = this.getProvider(state, level, pos, player, hand, hitResult);
            if (provider == null) {
                return InteractionResult.SUCCESS;
            } else {
                NetworkHooks.openScreen((ServerPlayer) player, provider, pos);
                return InteractionResult.SUCCESS;
            }
        }
    }

    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && be instanceof BookshelfTile ? new SimpleMenuProvider((id, playerInv, user) -> new ContainerBookshelf(id, playerInv, (BookshelfTile) be), CONTAINER_TITLE) : null;
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return 1.0F;
    }
}