package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

public class SkilletBlock extends BaseEntityBlock {

    public static final int MINIMUM_COOKING_TIME = 60;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty SUPPORT = BooleanProperty.create("support");

    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0);

    protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0, -1.0, 0.0, 16.0, 0.0, 16.0));

    public SkilletBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(SUPPORT, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity) {
            if (!level.isClientSide) {
                ItemStack heldStack = player.m_21120_(hand);
                EquipmentSlot heldSlot = hand.equals(InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                if (heldStack.isEmpty()) {
                    ItemStack extractedStack = skilletEntity.removeItem();
                    if (!player.isCreative()) {
                        player.setItemSlot(heldSlot, extractedStack);
                    }
                    return InteractionResult.SUCCESS;
                }
                ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
                if (remainderStack.getCount() != heldStack.getCount()) {
                    if (!player.isCreative()) {
                        player.setItemSlot(heldSlot, remainderStack);
                    }
                    level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof SkilletBlockEntity) {
                Containers.dropItemStack(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), ((SkilletBlockEntity) tileEntity).getInventory().getStackInSlot(0));
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ((Boolean) state.m_61143_(SUPPORT)).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_())).m_61124_(SUPPORT, this.getTrayState(context.m_43725_(), context.getClickedPos()));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        return facing.getAxis().equals(Direction.Axis.Y) ? (BlockState) state.m_61124_(SUPPORT, this.getTrayState(world, currentPos)) : state;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.m_7397_(level, pos, state);
        SkilletBlockEntity skilletEntity = (SkilletBlockEntity) level.getBlockEntity(pos);
        CompoundTag nbt = new CompoundTag();
        if (skilletEntity != null) {
            skilletEntity.writeSkilletItem(nbt);
        }
        if (!nbt.isEmpty()) {
            stack = ItemStack.of(nbt.getCompound("Skillet"));
        }
        return stack;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SUPPORT);
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity && skilletEntity.isCooking()) {
            double x = (double) pos.m_123341_() + 0.5;
            double y = (double) pos.m_123342_();
            double z = (double) pos.m_123343_() + 0.5;
            if (rand.nextInt(10) == 0) {
                level.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.SKILLET.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        return level.isClientSide ? m_152132_(blockEntity, ModBlockEntityTypes.SKILLET.get(), SkilletBlockEntity::animationTick) : m_152132_(blockEntity, ModBlockEntityTypes.SKILLET.get(), SkilletBlockEntity::cookingTick);
    }

    private boolean getTrayState(LevelAccessor world, BlockPos pos) {
        return world.m_8055_(pos.below()).m_204336_(ModTags.TRAY_HEAT_SOURCES);
    }

    public static int getSkilletCookingTime(int originalCookingTime, int fireAspectLevel) {
        int cookingTime = originalCookingTime > 0 ? originalCookingTime : 600;
        int cookingSeconds = cookingTime / 20;
        float cookingTimeReduction = 0.2F;
        if (fireAspectLevel > 0) {
            cookingTimeReduction = (float) ((double) cookingTimeReduction - (double) fireAspectLevel * 0.05);
        }
        int result = (int) ((float) cookingSeconds * cookingTimeReduction) * 20;
        return Mth.clamp(result, 60, originalCookingTime);
    }
}