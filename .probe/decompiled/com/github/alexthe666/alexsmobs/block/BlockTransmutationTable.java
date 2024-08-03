package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.inventory.MenuTransmutationTable;
import com.github.alexthe666.alexsmobs.message.MessageUpdateTransmutablesToDisplay;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityTransmutationTable;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockTransmutationTable extends BaseEntityBlock implements AMSpecialRenderBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("alexsmobs.container.transmutation_table");

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape BASE_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);

    private static final VoxelShape ARMS_NS = Block.box(1.0, 5.0, 5.5, 15.0, 16.0, 10.5);

    private static final VoxelShape ARMS_EW = Block.box(5.5, 5.0, 1.0, 10.5, 16.0, 15.0);

    private static final VoxelShape NS_AABB = Shapes.or(BASE_AABB, ARMS_NS);

    private static final VoxelShape EW_AABB = Shapes.or(BASE_AABB, ARMS_EW);

    public BlockTransmutationTable() {
        super(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).mapColor(DyeColor.BLACK).noOcclusion().lightLevel(block -> 2).emissiveRendering((block, world, pos) -> true).sound(SoundType.STONE).strength(1.0F).requiresCorrectToolForDrops());
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return ((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.Z ? NS_AABB : EW_AABB;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityTransmutationTable(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.m_60750_(level, pos));
            player.awardStat(Stats.INTERACT_WITH_LOOM);
            if (level.getBlockEntity(pos) instanceof TileEntityTransmutationTable table) {
                AlexsMobs.sendMSGToAll(new MessageUpdateTransmutablesToDisplay(player.m_19879_(), table.getPossibility(0), table.getPossibility(1), table.getPossibility(2)));
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity te = level.getBlockEntity(pos);
        return new SimpleMenuProvider((i, inv, player) -> new MenuTransmutationTable(i, inv, ContainerLevelAccess.create(level, pos), player, te instanceof TileEntityTransmutationTable ? (TileEntityTransmutationTable) te : null), CONTAINER_TITLE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, AMTileEntityRegistry.TRANSMUTATION_TABLE.get(), TileEntityTransmutationTable::commonTick);
    }

    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (AMConfig.transmutingTableExplodes) {
            level.explode(null, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 3.0F, false, Level.ExplosionInteraction.BLOCK);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}