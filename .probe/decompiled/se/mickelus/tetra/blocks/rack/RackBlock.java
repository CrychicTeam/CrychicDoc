package se.mickelus.tetra.blocks.rack;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import org.joml.Vector3f;
import se.mickelus.mutil.util.ItemHandlerWrapper;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.blocks.IToolProviderBlock;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.properties.IToolProvider;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class RackBlock extends TetraWaterloggedBlock implements EntityBlock, IToolProviderBlock {

    public static final String identifier = "rack";

    public static final DirectionProperty facingProp = HorizontalDirectionalBlock.FACING;

    private static final Map<Direction, VoxelShape> shapes = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0, 11.0, 14.0, 16.0, 14.0, 16.0), Direction.SOUTH, Block.box(0.0, 11.0, 0.0, 16.0, 14.0, 2.0), Direction.WEST, Block.box(14.0, 11.0, 0.0, 16.0, 14.0, 16.0), Direction.EAST, Block.box(0.0, 11.0, 0.0, 2.0, 14.0, 16.0)));

    @ObjectHolder(registryName = "block", value = "tetra:rack")
    public static RackBlock instance;

    public RackBlock() {
        super(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.WOOD));
    }

    private static double getHitX(Direction facing, AABB boundingBox, double hitX, double hitY, double hitZ) {
        switch(facing) {
            case NORTH:
                return boundingBox.maxX - hitX;
            case SOUTH:
                return hitX - boundingBox.minX;
            case WEST:
                return hitZ - boundingBox.minZ;
            case EAST:
                return boundingBox.maxZ - hitZ;
            default:
                return 0.0;
        }
    }

    @Override
    public void clientInit() {
        BlockEntityRenderers.register(RackTile.type, RackTESR::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(facingProp);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction facing = (Direction) blockState.m_61143_(facingProp);
        AABB boundingBox = blockState.m_60808_(player.m_9236_(), pos).bounds();
        if (facing == hit.getDirection()) {
            Vec3 hitVec = hit.m_82450_();
            int slot = getHitX(facing, boundingBox, (double) ((float) hitVec.x - (float) pos.m_123341_()), (double) ((float) hitVec.y - (float) pos.m_123342_()), (double) ((float) hitVec.z - (float) pos.m_123343_())) > 0.5 ? 1 : 0;
            TileEntityOptional.from(world, pos, RackTile.class).ifPresent(tile -> tile.slotInteract(slot, player, hand));
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return Direction.Axis.Y != context.m_43719_().getAxis() ? (BlockState) Optional.of((BlockState) this.m_49966_().m_61124_(facingProp, context.m_43719_())).filter(blockState -> blockState.m_60710_(context.m_43725_(), context.getClickedPos())).orElse(null) : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction facing = (Direction) state.m_61143_(facingProp);
        BlockPos offsetPos = pos.relative(facing.getOpposite());
        return worldIn.m_8055_(offsetPos).m_60783_(worldIn, offsetPos, facing);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        dropBlockInventory(this, world, pos, newState);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.m_61143_(facingProp) && !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (VoxelShape) shapes.get(state.m_61143_(facingProp));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(facingProp, rot.rotate((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(facingProp)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Component.translatable("block.tetra.rack.description").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }

    @Override
    public boolean canProvideTools(Level world, BlockPos pos, BlockPos targetPos) {
        return true;
    }

    @Override
    public Collection<ToolAction> getTools(Level world, BlockPos pos, BlockState blockState) {
        return (Collection<ToolAction>) ((LazyOptional) Optional.ofNullable(world.getBlockEntity(pos)).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).map(ItemHandlerWrapper::new).map(PropertyHelper::getInventoryTools).orElseGet(Collections::emptySet);
    }

    @Override
    public int getToolLevel(Level world, BlockPos pos, BlockState blockState, ToolAction toolAction) {
        return (Integer) ((LazyOptional) Optional.ofNullable(world.getBlockEntity(pos)).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).map(ItemHandlerWrapper::new).map(inv -> PropertyHelper.getInventoryToolLevel(inv, toolAction)).orElse(-1);
    }

    @Override
    public ItemStack onCraftConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        Optional<Container> optional = ((LazyOptional) Optional.ofNullable(world.getBlockEntity(pos)).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).map(ItemHandlerWrapper::new);
        if (optional.isPresent() && player != null) {
            Container inventory = (Container) optional.orElse(null);
            ItemStack providerStack = PropertyHelper.getInventoryProvidingItemStack(inventory, requiredTool, requiredLevel);
            if (!providerStack.isEmpty()) {
                if (consumeResources) {
                    this.spawnConsumeParticle(world, pos, blockState, inventory, providerStack);
                }
                return ((IToolProvider) providerStack.getItem()).onCraftConsume(providerStack, targetStack, player, requiredTool, requiredLevel, consumeResources);
            }
        }
        return null;
    }

    @Override
    public ItemStack onActionConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        Optional<ItemHandlerWrapper> optional = ((LazyOptional) Optional.ofNullable(world.getBlockEntity(pos)).map(te -> te.getCapability(ForgeCapabilities.ITEM_HANDLER)).orElse(LazyOptional.empty())).map(ItemHandlerWrapper::new);
        if (optional.isPresent() && player != null) {
            Container inventory = (Container) optional.orElse(null);
            ItemStack providerStack = PropertyHelper.getInventoryProvidingItemStack(inventory, requiredTool, requiredLevel);
            if (!providerStack.isEmpty()) {
                if (consumeResources) {
                    this.spawnConsumeParticle(world, pos, blockState, inventory, providerStack);
                }
                return ((IToolProvider) providerStack.getItem()).onActionConsume(providerStack, targetStack, player, requiredTool, requiredLevel, consumeResources);
            }
        }
        return null;
    }

    private void spawnConsumeParticle(Level world, BlockPos pos, BlockState blockState, Container inventory, ItemStack providerStack) {
        if (world instanceof ServerLevel) {
            Direction facing = (Direction) blockState.m_61143_(facingProp);
            Vec3 particlePos = Vec3.atLowerCornerOf(pos).add(0.5, 0.75, 0.5).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(-0.3));
            ItemStack firstSlot = inventory.getItem(0);
            firstSlot = (ItemStack) Optional.of(ItemUpgradeRegistry.instance.getReplacement(firstSlot)).filter(itemStack -> !itemStack.isEmpty()).orElse(firstSlot);
            if (ItemStack.matches(providerStack, firstSlot)) {
                particlePos = particlePos.add(Vec3.atLowerCornerOf(facing.getCounterClockWise().getNormal()).scale(-0.25));
            } else {
                particlePos = particlePos.add(Vec3.atLowerCornerOf(facing.getCounterClockWise().getNormal()).scale(0.25));
            }
            ((ServerLevel) world).sendParticles(new DustParticleOptions(new Vector3f(0.0F, 0.66F, 0.66F), 1.0F), particlePos.x(), particlePos.y(), particlePos.z(), 2, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new RackTile(blockPos0, blockState1);
    }
}