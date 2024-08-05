package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Collections;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.block.WaterBlock;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends WaterBlock implements EntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0), Block.box(5.0, 14.0, 5.0, 11.0, 16.0, 11.0));

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final IntegerProperty LIGHT_LEVEL = ModBlockProperties.LIGHT_LEVEL_0_15;

    public JarBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> (Integer) state.m_61143_(LIGHT_LEVEL)));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIGHT_LEVEL, 0)).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false));
    }

    public int getJarLiquidColor(BlockPos pos, LevelReader world) {
        return world.m_7702_(pos) instanceof JarBlockTile tile ? tile.fluidHolder.getCachedParticleColor(world, pos) : 16777215;
    }

    @Nullable
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        int color = this.getJarLiquidColor(pos, world);
        if (color == -1) {
            return null;
        } else {
            float r = (float) (color >> 16 & 0xFF) / 255.0F;
            float g = (float) (color >> 8 & 0xFF) / 255.0F;
            float b = (float) (color & 0xFF) / 255.0F;
            return new float[] { r, g, b };
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof JarBlockTile tile && tile.isAccessibleBy(player)) {
            if (tile.handleInteraction(player, handIn, worldIn, pos)) {
                if (!worldIn.isClientSide()) {
                    tile.m_6596_();
                }
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
            if ((Boolean) CommonConfigs.Functional.JAR_CAPTURE.get()) {
                return tile.mobContainer.onInteract(worldIn, pos, player, handIn);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (worldIn.getBlockEntity(pos) instanceof JarBlockTile tile) {
            if (stack.hasCustomHoverName()) {
                tile.m_58638_(stack.getHoverName());
            }
            BlockUtil.addOptionalOwnership(placer, tile);
        }
    }

    public ItemStack getJarItem(JarBlockTile te) {
        ItemStack returnStack = new ItemStack(this);
        if (te.hasContent()) {
            CompoundTag compoundTag = te.m_187482_();
            if (compoundTag.contains("Owner")) {
                compoundTag.remove("Owner");
            }
            if (!compoundTag.isEmpty()) {
                returnStack.addTagElement("BlockEntityTag", compoundTag);
            }
        }
        if (te.m_8077_()) {
            returnStack.setHoverName(te.m_7770_());
        }
        return returnStack;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof JarBlockTile tile) {
            ItemStack itemstack = this.getJarItem(tile);
            return Collections.singletonList(itemstack);
        } else {
            return super.m_49635_(state, builder);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return level.getBlockEntity(pos) instanceof JarBlockTile tile ? this.getJarItem(tile) : super.m_7397_(level, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIGHT_LEVEL, FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new JarBlockTile(pPos, pState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof JarBlockTile tile) {
            if (!tile.m_7983_()) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer(tile);
            }
            if (!tile.fluidHolder.isEmpty()) {
                return tile.fluidHolder.getComparatorOutput();
            }
            if (!tile.mobContainer.isEmpty()) {
                return 15;
            }
        }
        return 0;
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, (BlockEntityType) ModRegistry.JAR_TILE.get(), JarBlockTile::tick);
    }
}