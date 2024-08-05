package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.RopeKnotBlockTile;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RopeKnotBlock extends AbstractRopeKnotBlock implements IRopeConnection {

    public static final VoxelShape SIDE_SHAPE = Block.box(6.0, 9.0, 0.0, 10.0, 13.0, 10.0);

    public RopeKnotBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RopeKnotBlockTile(pPos, pState);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        BlockState newState = (BlockState) state.m_61124_((Property) RopeBlock.FACING_TO_PROPERTY_MAP.get(facing), this.shouldConnectToFace(state, facingState, facingPos, facing, world));
        if (world.m_7702_(currentPos) instanceof RopeKnotBlockTile tile) {
            BlockState oldHeld = tile.getHeldBlock();
            RopeKnotBlockTile otherTile = null;
            if (facingState.m_60713_((Block) ModRegistry.ROPE_KNOT.get()) && world.m_7702_(facingPos) instanceof RopeKnotBlockTile te2) {
                otherTile = te2;
                facingState = te2.getHeldBlock();
            }
            BlockState newHeld = null;
            if (newHeld == null) {
                newHeld = oldHeld.m_60728_(facing, facingState, world, currentPos, facingPos);
            }
            if (!(facingState.m_60734_() instanceof IRopeConnection)) {
                BlockState newFacing = facingState.m_60728_(facing.getOpposite(), newHeld, world, facingPos, currentPos);
                if (newFacing != facingState) {
                    if (otherTile != null) {
                        otherTile.setHeldBlock(newFacing);
                        otherTile.setChanged();
                    } else {
                        world.m_7731_(facingPos, newFacing, 2);
                    }
                }
            }
            ModBlockProperties.PostType type = ModBlockProperties.PostType.get(newHeld);
            if (newHeld != oldHeld) {
                tile.setHeldBlock(newHeld);
                tile.setChanged();
            }
            if (newState != state) {
                tile.recalculateShapes(newState);
            }
            if (type != null) {
                newState = (BlockState) newState.m_61124_(POST_TYPE, type);
            }
        }
        return newState;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.m_21120_(hand).getItem() instanceof ShearsItem) {
            if (!world.isClientSide && world.getBlockEntity(pos) instanceof RopeKnotBlockTile tile) {
                m_49840_(world, pos, new ItemStack((ItemLike) ModRegistry.ROPE.get()));
                world.playSound(null, pos, SoundEvents.SNOW_GOLEM_SHEAR, SoundSource.PLAYERS, 0.8F, 1.3F);
                world.setBlock(pos, tile.getHeldBlock(), 3);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof RopeKnotBlockTile tile) {
            BlockState mimic = tile.getHeldBlock();
            return mimic.m_60734_().getCloneItemStack(level, pos, state);
        } else {
            return super.m_7397_(level, pos, state);
        }
    }

    @Override
    public boolean canSideAcceptConnection(BlockState state, Direction direction) {
        return state.m_61143_(AbstractRopeKnotBlock.AXIS) == Direction.Axis.Y ? direction.getAxis() != Direction.Axis.Y : direction.getAxis() == Direction.Axis.Y;
    }

    @Override
    public VoxelShape getSideShape() {
        return SIDE_SHAPE;
    }
}