package com.simibubi.create.content.redstone.nixieTube;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NixieTubeBlock extends DoubleFaceAttachedBlock implements IBE<NixieTubeBlockEntity>, IWrenchable, SimpleWaterloggedBlock, ISpecialBlockItemRequirement {

    protected final DyeColor color;

    public NixieTubeBlock(BlockBehaviour.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACE, DoubleFaceAttachedBlock.DoubleAttachFace.FLOOR)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else {
            ItemStack heldItem = player.m_21120_(hand);
            NixieTubeBlockEntity nixie = this.getBlockEntity(world, pos);
            if (nixie == null) {
                return InteractionResult.PASS;
            } else if (heldItem.isEmpty()) {
                if (nixie.reactsToRedstone()) {
                    return InteractionResult.PASS;
                } else {
                    nixie.clearCustomText();
                    this.updateDisplayedRedstoneValue(state, world, pos);
                    return InteractionResult.SUCCESS;
                }
            } else {
                boolean display = heldItem.getItem() == Items.NAME_TAG && heldItem.hasCustomHoverName() || AllBlocks.CLIPBOARD.isIn(heldItem);
                DyeColor dye = DyeColor.getColor(heldItem);
                if (!display && dye == null) {
                    return InteractionResult.PASS;
                } else {
                    CompoundTag tag = heldItem.getTagElement("display");
                    String tagElement = tag != null && tag.contains("Name", 8) ? tag.getString("Name") : null;
                    if (AllBlocks.CLIPBOARD.isIn(heldItem)) {
                        List<ClipboardEntry> entries = ClipboardEntry.getLastViewedEntries(heldItem);
                        int i = 0;
                        if (i < entries.size()) {
                            tagElement = Component.Serializer.toJson(((ClipboardEntry) entries.get(i)).text);
                        }
                    }
                    if (world.isClientSide) {
                        return InteractionResult.SUCCESS;
                    } else {
                        String tagUsed = tagElement;
                        walkNixies(world, pos, (currentPos, rowPosition) -> {
                            if (display) {
                                this.withBlockEntityDo(world, currentPos, be -> be.displayCustomText(tagUsed, rowPosition));
                            }
                            if (dye != null) {
                                world.setBlockAndUpdate(currentPos, withColor(state, dye));
                            }
                        });
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
    }

    public static void walkNixies(LevelAccessor world, BlockPos start, BiConsumer<BlockPos, Integer> callback) {
        BlockState state = world.m_8055_(start);
        if (state.m_60734_() instanceof NixieTubeBlock) {
            BlockPos currentPos = start;
            Direction left = ((Direction) state.m_61143_(f_54117_)).getOpposite();
            if (state.m_61143_(FACE) == DoubleFaceAttachedBlock.DoubleAttachFace.WALL) {
                left = Direction.UP;
            }
            if (state.m_61143_(FACE) == DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED) {
                left = Direction.DOWN;
            }
            Direction right = left.getOpposite();
            while (true) {
                BlockPos nextPos = currentPos.relative(left);
                if (!areNixieBlocksEqual(world.m_8055_(nextPos), state)) {
                    int index = 0;
                    while (true) {
                        callback.accept(currentPos, index);
                        BlockPos nextPosx = currentPos.relative(right);
                        if (!areNixieBlocksEqual(world.m_8055_(nextPosx), state)) {
                            return;
                        }
                        currentPos = nextPosx;
                        index++;
                    }
                }
                currentPos = nextPos;
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder.add(FACE, f_54117_, BlockStateProperties.WATERLOGGED));
    }

    @Override
    public void onRemove(BlockState p_196243_1_, Level p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_, boolean p_196243_5_) {
        if (!(p_196243_4_.m_60734_() instanceof NixieTubeBlock)) {
            p_196243_2_.removeBlockEntity(p_196243_3_);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return AllBlocks.ORANGE_NIXIE_TUBE.asStack();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, ((NixieTubeBlock) AllBlocks.ORANGE_NIXIE_TUBE.get()).m_5456_());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = (Direction) pState.m_61143_(f_54117_);
        switch((DoubleFaceAttachedBlock.DoubleAttachFace) pState.m_61143_(FACE)) {
            case CEILING:
                return AllShapes.NIXIE_TUBE_CEILING.get(facing.getClockWise().getAxis());
            case FLOOR:
                return AllShapes.NIXIE_TUBE.get(facing.getClockWise().getAxis());
            default:
                return AllShapes.NIXIE_TUBE_WALL.get(facing);
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return this.color != DyeColor.ORANGE ? ((NixieTubeBlock) AllBlocks.ORANGE_NIXIE_TUBE.get()).getCloneItemStack(state, target, world, pos, player) : super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        } else {
            if (state.m_61143_(FACE) != DoubleFaceAttachedBlock.DoubleAttachFace.WALL && state.m_61143_(FACE) != DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED) {
                state = (BlockState) state.m_61124_(f_54117_, ((Direction) state.m_61143_(f_54117_)).getClockWise());
            }
            return (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_, boolean p_220069_6_) {
        if (!worldIn.isClientSide) {
            if (!worldIn.m_183326_().willTickThisTick(pos, this)) {
                worldIn.m_186460_(pos, this, 0);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        this.updateDisplayedRedstoneValue(state, worldIn, pos);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_60734_() != oldState.m_60734_() && !isMoving) {
            this.updateDisplayedRedstoneValue(state, worldIn, pos);
        }
    }

    private void updateDisplayedRedstoneValue(BlockState state, Level worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            this.withBlockEntityDo(worldIn, pos, be -> {
                if (be.reactsToRedstone()) {
                    be.updateRedstoneStrength(this.getPower(worldIn, pos));
                }
            });
        }
    }

    static boolean isValidBlock(BlockGetter world, BlockPos pos, boolean above) {
        BlockState state = world.getBlockState(pos.above(above ? 1 : -1));
        return !state.m_60808_(world, pos).isEmpty();
    }

    private int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        for (Direction direction : Iterate.directions) {
            power = Math.max(worldIn.m_277185_(pos.relative(direction), direction), power);
        }
        for (Direction direction : Iterate.directions) {
            power = Math.max(worldIn.m_277185_(pos.relative(direction), Direction.UP), power);
        }
        return power;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side != null;
    }

    @Override
    public Class<NixieTubeBlockEntity> getBlockEntityClass() {
        return NixieTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends NixieTubeBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends NixieTubeBlockEntity>) AllBlockEntityTypes.NIXIE_TUBE.get();
    }

    public DyeColor getColor() {
        return this.color;
    }

    public static boolean areNixieBlocksEqual(BlockState blockState, BlockState otherState) {
        if (!(blockState.m_60734_() instanceof NixieTubeBlock)) {
            return false;
        } else {
            return !(otherState.m_60734_() instanceof NixieTubeBlock) ? false : withColor(blockState, DyeColor.WHITE) == withColor(otherState, DyeColor.WHITE);
        }
    }

    public static BlockState withColor(BlockState state, DyeColor color) {
        return (BlockState) ((BlockState) ((BlockState) (color == DyeColor.ORANGE ? AllBlocks.ORANGE_NIXIE_TUBE : AllBlocks.NIXIE_TUBES.get(color)).getDefaultState().m_61124_(f_54117_, (Direction) state.m_61143_(f_54117_))).m_61124_(BlockStateProperties.WATERLOGGED, (Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED))).m_61124_(FACE, (DoubleFaceAttachedBlock.DoubleAttachFace) state.m_61143_(FACE));
    }

    public static DyeColor colorOf(BlockState blockState) {
        return blockState.m_60734_() instanceof NixieTubeBlock ? ((NixieTubeBlock) blockState.m_60734_()).color : DyeColor.ORANGE;
    }

    public static Direction getFacing(BlockState sideState) {
        return getConnectedDirection(sideState);
    }
}