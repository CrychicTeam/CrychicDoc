package net.mehvahdjukaar.amendments.common.block;

import java.util.List;
import net.mehvahdjukaar.amendments.common.tile.CeilingBannerBlockTile;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.moonlight.api.map.ExpandedMapData;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CeilingBannerBlock extends AbstractBannerBlock {

    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape SHAPE_X = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_Z = Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 9.0);

    private String descriptionId;

    public CeilingBannerBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(color, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(ATTACHED, false));
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        return BannerBlock.byColor(this.m_48674_()).m_49635_(blockState, builder);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState above = world.m_8055_(pos.above());
        return state.m_61143_(ATTACHED) ? this.canAttach(state, above) : above.m_280296_();
    }

    private boolean canAttach(BlockState state, BlockState above) {
        return CompatHandler.SUPPLEMENTARIES && SuppCompat.canBannerAttachToRope(state, above);
    }

    @Override
    public BlockState updateShape(BlockState myState, Direction direction, BlockState otherState, LevelAccessor world, BlockPos myPos, BlockPos otherPos) {
        return direction == Direction.UP && !myState.m_60710_(world, myPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(myState, direction, myState, world, myPos, otherPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return ((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.m_43719_() == Direction.DOWN) {
            BlockState blockstate = this.m_49966_();
            LevelReader world = context.m_43725_();
            BlockPos blockpos = context.getClickedPos();
            blockstate = (BlockState) blockstate.m_61124_(FACING, context.m_8125_().getOpposite());
            boolean attached = this.canAttach(blockstate, world.m_8055_(blockpos.above()));
            blockstate = (BlockState) blockstate.m_61124_(ATTACHED, attached);
            if (blockstate.m_60710_(world, blockpos)) {
                return blockstate;
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ATTACHED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CeilingBannerBlockTile(pPos, pState, this.m_48674_());
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName() && pLevel.getBlockEntity(pPos) instanceof CeilingBannerBlockTile tile) {
            tile.setCustomName(pStack.getHoverName());
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.m_21120_(pHand);
        Item item = itemstack.getItem();
        if (item instanceof MapItem) {
            if (!pLevel.isClientSide && MapItem.getSavedData(itemstack, pLevel) instanceof ExpandedMapData data) {
                data.toggleCustomDecoration(pLevel, pPos);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            return super.m_6227_(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
    }

    @Override
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = BlocksColorAPI.getColoredBlock("banner", this.m_48674_()).getDescriptionId();
        }
        return this.descriptionId;
    }
}