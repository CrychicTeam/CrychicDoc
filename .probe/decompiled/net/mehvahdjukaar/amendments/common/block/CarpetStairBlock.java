package net.mehvahdjukaar.amendments.common.block;

import java.util.List;
import java.util.stream.IntStream;
import net.mehvahdjukaar.amendments.common.tile.CarpetedBlockTile;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.block.IRecolorable;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CarpetStairBlock extends ModStairBlock implements EntityBlock, IRecolorable {

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL;

    public static final BooleanProperty SOLID = ModBlockProperties.SOLID;

    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, -1.0, 16.0, 9.0, 16.0);

    protected static final VoxelShape OCTET_NPN = Block.box(0.0, 8.0, 0.0, 9.0, 17.0, 9.0);

    protected static final VoxelShape OCTET_NPP = Block.box(0.0, 8.0, 7.0, 9.0, 17.0, 16.0);

    protected static final VoxelShape OCTET_PPN = Block.box(7.0, 8.0, 0.0, 16.0, 17.0, 9.0);

    protected static final VoxelShape OCTET_PPP = Block.box(7.0, 8.0, 7.0, 16.0, 17.0, 16.0);

    protected static final VoxelShape[] BOTTOM_SHAPES = makeShapes();

    private static final int[] SHAPE_BY_STATE = new int[] { 12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8 };

    public CarpetStairBlock(Block block) {
        super(() -> block, BlockBehaviour.Properties.copy(block).lightLevel(state -> Math.max(0, (Integer) state.m_61143_(LIGHT_LEVEL))));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(SOLID, true)).m_61124_(LIGHT_LEVEL, 0));
    }

    private static VoxelShape[] makeShapes() {
        return (VoxelShape[]) IntStream.range(0, 16).mapToObj(CarpetStairBlock::makeStairShape).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int bitfield) {
        Direction dir = switch(bitfield % 4) {
            case 1 ->
                Direction.EAST;
            case 2 ->
                Direction.WEST;
            case 3 ->
                Direction.SOUTH;
            default ->
                Direction.NORTH;
        };
        VoxelShape voxelShape = MthUtils.rotateVoxelShape(BOTTOM_AABB, dir);
        if ((bitfield & 1) != 0) {
            voxelShape = Shapes.or(voxelShape, OCTET_NPN);
        }
        if ((bitfield & 2) != 0) {
            voxelShape = Shapes.or(voxelShape, OCTET_PPN);
        }
        if ((bitfield & 4) != 0) {
            voxelShape = Shapes.or(voxelShape, OCTET_NPP);
        }
        if ((bitfield & 8) != 0) {
            voxelShape = Shapes.or(voxelShape, OCTET_PPP);
        }
        return voxelShape;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            if (!level.m_5776_() && level.m_7702_(pos) instanceof CarpetedBlockTile te && level instanceof Level l) {
                Block.popResource(l, pos, te.getCarpet().m_60734_().asItem().getDefaultInstance());
                level.m_7731_(pos, (BlockState) te.getHeldBlock().m_60734_().withPropertiesOf(state).m_61124_(BlockStateProperties.WATERLOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(f_56842_) == Half.BOTTOM ? BOTTOM_SHAPES[SHAPE_BY_STATE[getShapeIndex(state)]] : super.m_5940_(state, level, pos, context);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_61143_(SOLID) ? super.m_7952_(state, level, pos) : Shapes.empty();
    }

    private static int getShapeIndex(BlockState state) {
        return ((StairsShape) state.m_61143_(f_56843_)).ordinal() * 4 + ((Direction) state.m_61143_(f_56841_)).get2DDataValue();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(LIGHT_LEVEL, SOLID);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
        if (worldIn.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_() && !(mimicState.m_60734_() instanceof CarpetStairBlock)) {
                return mimicState.m_60625_(player, worldIn, pos);
            }
        }
        return super.m_5880_(state, player, worldIn, pos);
    }

    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        if (world.m_7702_(pos) instanceof CarpetedBlockTile tile) {
            SoundType mixed = tile.getSoundType();
            if (mixed != null) {
                return mixed;
            }
        }
        return super.m_49962_(state);
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.m_6786_(level, pos, state);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        super.m_142387_(level, player, pos, state);
        if (level.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock(1);
            if (!mimicState.m_60795_()) {
                SoundType sound = mimicState.m_60827_();
                level.playSound(null, pos, sound.getBreakSound(), SoundSource.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, this.f_60446_.getPitch() * 0.8F);
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof IBlockHolder tile) {
            BlockState heldState = tile.getHeldBlock(0);
            BlockState carpet = tile.getHeldBlock(1);
            if (builder.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player && ForgeHelper.canHarvestBlock(heldState, builder.getLevel(), BlockPos.containing(builder.getParameter(LootContextParams.ORIGIN)), player)) {
                drops.addAll(heldState.m_287290_(builder));
            }
            drops.addAll(carpet.m_287290_(builder));
        }
        return drops;
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof CarpetedBlockTile tile) {
            if (target instanceof BlockHitResult hs && hs.getDirection() == Direction.UP) {
                return tile.getHeldBlock(1).m_60734_().getCloneItemStack(level, pos, state);
            }
            BlockState mimic = tile.getHeldBlock();
            return mimic.m_60734_().getCloneItemStack(level, pos, state);
        } else {
            return super.m_7397_(level, pos, state);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof CarpetedBlockTile tile) {
            BlockState mimic = tile.getHeldBlock();
            return mimic.m_60734_().getCloneItemStack(level, pos, state);
        } else {
            return super.m_7397_(level, pos, state);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CarpetedBlockTile(pPos, pState);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        BlockState newState = super.m_7417_(state, facing, facingState, world, currentPos, facingPos);
        if (world.m_7702_(currentPos) instanceof CarpetedBlockTile tile) {
            BlockState oldHeld = tile.getHeldBlock();
            CarpetedBlockTile otherTile = null;
            if (facingState.m_60713_((Block) ModRegistry.CARPET_STAIRS.get()) && world.m_7702_(facingPos) instanceof CarpetedBlockTile te2) {
                otherTile = te2;
                facingState = te2.getHeldBlock();
            }
            BlockState newHeld = oldHeld.m_60728_(facing, facingState, world, currentPos, facingPos);
            BlockState newFacing = facingState.m_60728_(facing.getOpposite(), newHeld, world, facingPos, currentPos);
            if (newFacing != facingState) {
                if (otherTile != null) {
                    otherTile.setHeldBlock(newFacing);
                    otherTile.m_6596_();
                } else {
                    world.m_7731_(facingPos, newFacing, 2);
                }
            }
            if (newHeld != oldHeld) {
                tile.setHeldBlock(newHeld);
            }
        }
        return newState;
    }

    @Override
    public boolean tryRecolor(Level level, BlockPos blockPos, BlockState blockState, @Nullable DyeColor dyeColor) {
        if (level.getBlockEntity(blockPos) instanceof CarpetedBlockTile tile) {
            BlockState c = tile.getHeldBlock();
            if (!c.m_60795_()) {
                Block otherCarpet = BlocksColorAPI.changeColor(c.m_60734_(), dyeColor);
                if (otherCarpet != null && !c.m_60713_(otherCarpet)) {
                    tile.setHeldBlock(otherCarpet.withPropertiesOf(c));
                    tile.m_6596_();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isDefaultColor(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.getBlockEntity(blockPos) instanceof CarpetedBlockTile tile) {
            BlockState c = tile.getHeldBlock();
            return BlocksColorAPI.isDefaultColor(c.m_60734_());
        } else {
            return false;
        }
    }
}