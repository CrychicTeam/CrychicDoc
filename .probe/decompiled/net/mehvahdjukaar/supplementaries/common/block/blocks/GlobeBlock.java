package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.moonlight.api.block.IWashable;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.GlobeBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlobeBlock extends WaterBlock implements EntityBlock, IWashable {

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 15.0, 14.0);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final IntegerProperty ROTATION = ModBlockProperties.ROTATION_4;

    public GlobeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(POWERED, false)).m_61124_(ROTATION, 0)).m_61124_(FACING, Direction.NORTH));
    }

    public static void displayCurrentCoordinates(Level level, Player player, BlockPos pos) {
        String x = String.valueOf(pos.m_123341_());
        String z = String.valueOf(pos.m_123343_());
        if (!level.dimensionType().natural()) {
            x = ChatFormatting.OBFUSCATED + x + ChatFormatting.RESET;
            z = ChatFormatting.OBFUSCATED + z + ChatFormatting.RESET;
        }
        player.displayClientMessage(Component.translatable("message.supplementaries.compass", x, z), true);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        this.updatePower(state, worldIn, pos);
        if (stack.hasCustomHoverName() && worldIn.getBlockEntity(pos) instanceof GlobeBlockTile tile) {
            tile.setCustomName(stack.getHoverName());
        }
    }

    public void updatePower(BlockState state, Level leve, BlockPos pos) {
        boolean powered = leve.m_277086_(pos) > 0;
        if (powered != (Boolean) state.m_61143_(POWERED)) {
            leve.setBlock(pos, (BlockState) state.m_61124_(POWERED, powered), 4);
            if (powered) {
                leve.m_142346_(null, GameEvent.BLOCK_ACTIVATE, pos);
                leve.blockEvent(pos, state.m_60734_(), 1, 0);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof GlobeBlockTile tile) {
            if (player.m_21120_(handIn).getItem() instanceof ShearsItem) {
                tile.toggleShearing();
                tile.m_6596_();
                level.sendBlockUpdated(pos, state, state, 3);
                level.playSound(player, pos, SoundEvents.SHEEP_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level.isClientSide) {
                    level.addDestroyBlockEffect(pos, state);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (!level.isClientSide) {
                if (tile.isSpinningVeryFast() && player instanceof ServerPlayer serverPlayer) {
                    Advancement advancement = level.getServer().getAdvancements().getAdvancement(new ResourceLocation("supplementaries", "adventure/globe"));
                    if (advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
                        serverPlayer.getAdvancements().award(advancement, "unlock");
                    }
                }
                level.m_142346_(player, GameEvent.BLOCK_ACTIVATE, pos);
                level.blockEvent(pos, state.m_60734_(), 1, 0);
            } else if ((Boolean) ClientConfigs.Blocks.GLOBE_COORDINATES.get()) {
                displayCurrentCoordinates(level, player, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (MiscUtils.FESTIVITY.isEarthDay() && level.isClientSide) {
            int x = pos.m_123341_();
            int y = pos.m_123342_();
            int z = pos.m_123343_();
            for (int l = 0; l < 1; l++) {
                double d0 = (double) x + 0.5 + ((double) rand.nextFloat() - 0.5) * 0.625;
                double d1 = (double) y + 0.5 + ((double) rand.nextFloat() - 0.5) * 0.625;
                double d2 = (double) z + 0.5 + ((double) rand.nextFloat() - 0.5) * 0.625;
                level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, world, pos, neighborBlock, fromPos, moving);
        this.updatePower(state, world, pos);
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
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return facing == Direction.DOWN && !this.canSurvive(stateIn, worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return m_49863_(level, pos.below(), Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, POWERED, ROTATION);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, flag);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GlobeBlockTile(pPos, pState);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
        super.m_8133_(state, world, pos, eventID, eventParam);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(eventID, eventParam);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof GlobeBlockTile tile ? tile.getSignalPower() : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.GLOBE_TILE.get(), GlobeBlockTile::tick);
    }

    @Override
    public boolean tryWash(Level level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof GlobeBlockTile tile && tile.isSepia()) {
            level.setBlockAndUpdate(pos, ((Block) ModRegistry.GLOBE.get()).withPropertiesOf(state));
            return true;
        }
        return false;
    }
}