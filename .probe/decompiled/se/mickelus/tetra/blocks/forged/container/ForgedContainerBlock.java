package se.mickelus.tetra.blocks.forged.container;

import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;

@ParametersAreNonnullByDefault
public class ForgedContainerBlock extends TetraWaterloggedBlock implements IInteractiveBlock, EntityBlock {

    public static final String identifier = "forged_container";

    public static final DirectionProperty facingProp = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty flippedProp = BooleanProperty.create("flipped");

    public static final BooleanProperty locked1Prop = BooleanProperty.create("locked1");

    public static final BooleanProperty locked2Prop = BooleanProperty.create("locked2");

    public static final BooleanProperty anyLockedProp = BooleanProperty.create("locked_any");

    public static final BooleanProperty openProp = BooleanProperty.create("open");

    public static final BlockInteraction[] interactions = new BlockInteraction[] { new BlockInteraction(TetraToolActions.hammer, 3, Direction.SOUTH, 5.0F, 7.0F, 2.0F, 5.0F, new PropertyMatcher().where(locked1Prop, Predicates.equalTo(true)).where(flippedProp, Predicates.equalTo(false)), (world, pos, blockState, player, hand, hitFace) -> breakLock(world, pos, player, 0, hand)), new BlockInteraction(TetraToolActions.hammer, 3, Direction.SOUTH, 11.0F, 13.0F, 2.0F, 5.0F, new PropertyMatcher().where(locked2Prop, Predicates.equalTo(true)).where(flippedProp, Predicates.equalTo(false)), (world, pos, blockState, player, hand, hitFace) -> breakLock(world, pos, player, 1, hand)), new BlockInteraction(TetraToolActions.hammer, 3, Direction.SOUTH, 17.0F, 19.0F, 2.0F, 5.0F, new PropertyMatcher().where(locked1Prop, Predicates.equalTo(true)).where(flippedProp, Predicates.equalTo(true)), (world, pos, blockState, player, hand, hitFace) -> breakLock(world, pos, player, 2, hand)), new BlockInteraction(TetraToolActions.hammer, 3, Direction.SOUTH, 23.0F, 25.0F, 2.0F, 5.0F, new PropertyMatcher().where(locked2Prop, Predicates.equalTo(true)).where(flippedProp, Predicates.equalTo(true)), (world, pos, blockState, player, hand, hitFace) -> breakLock(world, pos, player, 3, hand)), new BlockInteraction(TetraToolActions.pry, 1, Direction.SOUTH, 1.0F, 15.0F, 3.0F, 4.0F, new PropertyMatcher().where(anyLockedProp, Predicates.equalTo(false)).where(openProp, Predicates.equalTo(false)).where(flippedProp, Predicates.equalTo(false)), ForgedContainerBlock::open), new BlockInteraction(TetraToolActions.pry, 1, Direction.SOUTH, 15.0F, 28.0F, 3.0F, 4.0F, new PropertyMatcher().where(anyLockedProp, Predicates.equalTo(false)).where(openProp, Predicates.equalTo(false)).where(flippedProp, Predicates.equalTo(true)), ForgedContainerBlock::open) };

    private static final VoxelShape shapeZ1 = m_49796_(1.0, 0.0, -15.0, 15.0, 12.0, 15.0);

    private static final VoxelShape shapeZ2 = m_49796_(1.0, 0.0, 1.0, 15.0, 12.0, 31.0);

    private static final VoxelShape shapeX1 = m_49796_(-15.0, 0.0, 1.0, 15.0, 12.0, 15.0);

    private static final VoxelShape shapeX2 = m_49796_(1.0, 0.0, 1.0, 31.0, 12.0, 15.0);

    private static final VoxelShape shapeZ1Open = m_49796_(1.0, 0.0, -15.0, 15.0, 9.0, 15.0);

    private static final VoxelShape shapeZ2Open = m_49796_(1.0, 0.0, 1.0, 15.0, 9.0, 31.0);

    private static final VoxelShape shapeX1Open = m_49796_(-15.0, 0.0, 1.0, 15.0, 9.0, 15.0);

    private static final VoxelShape shapeX2Open = m_49796_(1.0, 0.0, 1.0, 31.0, 9.0, 15.0);

    public static RegistryObject<ForgedContainerBlock> instance;

    public ForgedContainerBlock() {
        super(ForgedBlockCommon.propertiesSolid);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(facingProp, Direction.EAST)).m_61124_(flippedProp, false)).m_61124_(openProp, true)).m_61124_(locked1Prop, false)).m_61124_(locked2Prop, false)).m_61124_(anyLockedProp, false));
    }

    private static boolean breakLock(Level world, BlockPos pos, @Nullable Player player, int index, @Nullable InteractionHand hand) {
        ForgedContainerBlockEntity te = (ForgedContainerBlockEntity) world.getBlockEntity(pos);
        if (te != null) {
            te.getOrDelegate().breakLock(player, index, hand);
        }
        return true;
    }

    private static boolean open(Level world, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, Direction facing) {
        ForgedContainerBlockEntity te = (ForgedContainerBlockEntity) world.getBlockEntity(pos);
        if (te != null) {
            te.getOrDelegate().open(player);
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientInit() {
        MenuScreens.register(ForgedContainerMenu.type.get(), ForgedContainerScreen::new);
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        packetHandler.registerPacket(ChangeCompartmentPacket.class, ChangeCompartmentPacket::new);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(ForgedBlockCommon.locationTooltip);
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState state, Direction face, Collection<ToolAction> tools) {
        return (BlockInteraction[]) Arrays.stream(interactions).filter(interaction -> interaction.isPotentialInteraction(world, pos, state, (Direction) state.m_61143_(facingProp), face, tools)).toArray(BlockInteraction[]::new);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult didInteract = BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
        if (didInteract != InteractionResult.SUCCESS) {
            if (!world.isClientSide) {
                TileEntityOptional.from(world, pos, ForgedContainerBlockEntity.class).ifPresent(te -> {
                    ForgedContainerBlockEntity delegate = te.getOrDelegate();
                    if (delegate.isOpen()) {
                        NetworkHooks.openScreen((ServerPlayer) player, delegate, delegate.m_58899_());
                    }
                });
            }
        } else {
            world.sendBlockUpdated(pos, state, state, 3);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!this.equals(newState.m_60734_())) {
            if ((Boolean) state.m_61143_(openProp) && !(Boolean) state.m_61143_(flippedProp)) {
                dropBlockInventory(this, world, pos, newState);
            } else {
                TileEntityOptional.from(world, pos, ForgedContainerBlockEntity.class).ifPresent(BlockEntity::m_7651_);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction facing = (Direction) state.m_61143_(facingProp);
        boolean flipped = (Boolean) state.m_61143_(flippedProp);
        if ((Boolean) state.m_61143_(openProp)) {
            if (flipped) {
                switch(facing) {
                    case NORTH:
                        return shapeX1Open;
                    case EAST:
                        return shapeZ1Open;
                    case SOUTH:
                        return shapeX2Open;
                    case WEST:
                        return shapeZ2Open;
                }
            } else {
                switch(facing) {
                    case NORTH:
                        return shapeX2Open;
                    case EAST:
                        return shapeZ2Open;
                    case SOUTH:
                        return shapeX1Open;
                    case WEST:
                        return shapeZ1Open;
                }
            }
        } else if (flipped) {
            switch(facing) {
                case NORTH:
                    return shapeX1;
                case EAST:
                    return shapeZ1;
                case SOUTH:
                    return shapeX2;
                case WEST:
                    return shapeZ2;
            }
        } else {
            switch(facing) {
                case NORTH:
                    return shapeX2;
                case EAST:
                    return shapeZ2;
                case SOUTH:
                    return shapeX1;
                case WEST:
                    return shapeZ1;
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(facingProp, flippedProp, locked1Prop, locked2Prop, anyLockedProp, openProp);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43725_().getBlockState(context.getClickedPos().relative(context.m_8125_().getClockWise())).m_60629_(context) ? (BlockState) super.getStateForPlacement(context).m_61124_(facingProp, context.m_8125_()) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction facing = (Direction) state.m_61143_(facingProp);
        world.setBlock(pos.relative(facing.getClockWise()), (BlockState) ((BlockState) this.m_49966_().m_61124_(flippedProp, true)).m_61124_(facingProp, facing), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        Direction pairedFacing = (Direction) state.m_61143_(facingProp);
        if ((Boolean) state.m_61143_(flippedProp)) {
            pairedFacing = pairedFacing.getCounterClockWise();
        } else {
            pairedFacing = pairedFacing.getClockWise();
        }
        if (pairedFacing == facing && !this.equals(facingState.m_60734_())) {
            return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        Direction facing = (Direction) state.m_61143_(facingProp);
        if (Rotation.CLOCKWISE_180.equals(rot) || Rotation.CLOCKWISE_90.equals(rot) && (Direction.NORTH.equals(facing) || Direction.SOUTH.equals(facing)) || Rotation.COUNTERCLOCKWISE_90.equals(rot) && (Direction.EAST.equals(facing) || Direction.WEST.equals(facing))) {
            state = (BlockState) state.m_61124_(flippedProp, (Boolean) state.m_61143_(flippedProp));
        }
        return (BlockState) state.m_61124_(facingProp, rot.rotate(facing));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(facingProp)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new ForgedContainerBlockEntity(blockPos0, blockState1);
    }
}