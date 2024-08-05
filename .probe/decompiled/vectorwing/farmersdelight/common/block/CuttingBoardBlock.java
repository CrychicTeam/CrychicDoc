package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CuttingBoardBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);

    public CuttingBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoardEntity) {
            ItemStack heldStack = player.m_21120_(hand);
            ItemStack offhandStack = player.m_21206_();
            if (cuttingBoardEntity.isEmpty()) {
                if (!offhandStack.isEmpty()) {
                    if (hand.equals(InteractionHand.MAIN_HAND) && !offhandStack.is(ModTags.OFFHAND_EQUIPMENT) && !(heldStack.getItem() instanceof BlockItem)) {
                        return InteractionResult.PASS;
                    }
                    if (hand.equals(InteractionHand.OFF_HAND) && offhandStack.is(ModTags.OFFHAND_EQUIPMENT)) {
                        return InteractionResult.PASS;
                    }
                }
                if (heldStack.isEmpty()) {
                    return InteractionResult.PASS;
                }
                if (cuttingBoardEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                    level.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (!heldStack.isEmpty()) {
                    ItemStack boardStack = cuttingBoardEntity.getStoredItem().copy();
                    if (cuttingBoardEntity.processStoredItemUsingTool(heldStack, player)) {
                        spawnCuttingParticles(level, pos, boardStack, 5);
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.CONSUME;
                }
                if (hand.equals(InteractionHand.MAIN_HAND)) {
                    if (!player.isCreative()) {
                        if (!player.getInventory().add(cuttingBoardEntity.removeItem())) {
                            Containers.dropItemStack(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), cuttingBoardEntity.removeItem());
                        }
                    } else {
                        cuttingBoardEntity.removeItem();
                    }
                    level.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.25F, 0.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoard) {
                Containers.dropItemStack(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), cuttingBoard.getStoredItem());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState state) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return facing == Direction.DOWN && !stateIn.m_60710_(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos floorPos = pos.below();
        return m_49936_(level, floorPos) || m_49863_(level, floorPos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CuttingBoardBlockEntity) {
            return !((CuttingBoardBlockEntity) blockEntity).isEmpty() ? 15 : 0;
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.CUTTING_BOARD.get().create(pos, state);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(FACING, pRot.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    public static void spawnCuttingParticles(Level level, BlockPos pos, ItemStack stack, int count) {
        for (int i = 0; i < count; i++) {
            Vec3 vec3d = new Vec3(((double) level.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, ((double) level.random.nextFloat() - 0.5) * 0.1);
            if (level instanceof ServerLevel) {
                ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.1F), (double) ((float) pos.m_123343_() + 0.5F), 1, vec3d.x, vec3d.y + 0.05, vec3d.z, 0.0);
            } else {
                level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.1F), (double) ((float) pos.m_123343_() + 0.5F), vec3d.x, vec3d.y + 0.05, vec3d.z);
            }
        }
    }

    @EventBusSubscriber(modid = "farmersdelight", bus = Bus.FORGE)
    public static class ToolCarvingEvent {

        @SubscribeEvent
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack heldStack = player.m_21205_();
            BlockEntity tileEntity = level.getBlockEntity(event.getPos());
            if (player.isSecondaryUseActive() && !heldStack.isEmpty() && tileEntity instanceof CuttingBoardBlockEntity && (heldStack.getItem() instanceof TieredItem || heldStack.getItem() instanceof TridentItem || heldStack.getItem() instanceof ShearsItem)) {
                boolean success = ((CuttingBoardBlockEntity) tileEntity).carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack);
                if (success) {
                    level.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }
        }
    }
}