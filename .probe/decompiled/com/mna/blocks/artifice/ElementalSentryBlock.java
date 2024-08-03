package com.mna.blocks.artifice;

import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.ElementalSentryTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ElementalSentryBlock extends WaterloggableBlock implements ICutoutBlock, EntityBlock {

    public static final BooleanProperty SHOOTING = BooleanProperty.create("shooting");

    public static final BooleanProperty TARGET_OVERRIDE = BooleanProperty.create("targeting_overridden");

    protected static final VoxelShape COLLISION_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final Direction[] offsets = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

    private final Affinity shootAffinity;

    public ElementalSentryBlock(Affinity shootAffinity) {
        super(BlockBehaviour.Properties.of().noOcclusion().strength(6.0F, 30.0F).noCollission(), false);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(SHOOTING, false)).m_61124_(TARGET_OVERRIDE, false));
        this.shootAffinity = shootAffinity;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof ElementalSentryTile && placer instanceof Player) {
            ElementalSentryTile teas = (ElementalSentryTile) te;
            teas.setOwner((Player) placer);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        for (Direction dir : offsets) {
            BlockState offsetState = context.m_43725_().getBlockState(context.getClickedPos().offset(dir.getNormal()));
            if (offsetState.m_60734_() instanceof SeerStoneBlock) {
                state = (BlockState) state.m_61124_(TARGET_OVERRIDE, true);
                break;
            }
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SHOOTING);
        builder.add(TARGET_OVERRIDE);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean broken) {
        if (!world.isClientSide) {
            boolean found = false;
            for (Direction dir : offsets) {
                BlockState offsetState = world.getBlockState(pos.offset(dir.getNormal()));
                if (offsetState.m_60734_() instanceof SeerStoneBlock) {
                    found = true;
                    break;
                }
            }
            if (!found && (Boolean) state.m_61143_(TARGET_OVERRIDE)) {
                world.setBlock(pos, (BlockState) state.m_61124_(TARGET_OVERRIDE, false), 3);
            } else if (found && !(Boolean) state.m_61143_(TARGET_OVERRIDE)) {
                world.setBlock(pos, (BlockState) state.m_61124_(TARGET_OVERRIDE, true), 3);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return te instanceof ElementalSentryTile && ((ElementalSentryTile) te).hasTarget() ? 15 : 0;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElementalSentryTile(pos, state, this.shootAffinity);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.ELEMENTAL_SENTRY.get() ? (world1, pos, state1, te) -> ElementalSentryTile.Tick(world1, pos, state1, (ElementalSentryTile) te) : null;
    }
}