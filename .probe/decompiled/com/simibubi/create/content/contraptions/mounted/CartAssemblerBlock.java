package com.simibubi.create.content.contraptions.mounted;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.redstone.rail.ControllerRailBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CartAssemblerBlock extends BaseRailBlock implements IBE<CartAssemblerBlockEntity>, IWrenchable, ISpecialBlockItemRequirement {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty BACKWARDS = BooleanProperty.create("backwards");

    public static final Property<RailShape> RAIL_SHAPE = EnumProperty.create("shape", RailShape.class, RailShape.EAST_WEST, RailShape.NORTH_SOUTH);

    public static final Property<CartAssembleRailType> RAIL_TYPE = EnumProperty.create("rail_type", CartAssembleRailType.class);

    public CartAssemblerBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(BACKWARDS, false)).m_61124_(RAIL_TYPE, CartAssembleRailType.POWERED_RAIL)).m_61124_(f_152149_, false));
    }

    public static BlockState createAnchor(BlockState state) {
        Direction.Axis axis = state.m_61143_(RAIL_SHAPE) == RailShape.NORTH_SOUTH ? Direction.Axis.Z : Direction.Axis.X;
        return (BlockState) AllBlocks.MINECART_ANCHOR.getDefaultState().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, axis);
    }

    private static Item getRailItem(BlockState state) {
        return ((CartAssembleRailType) state.m_61143_(RAIL_TYPE)).getItem();
    }

    public static BlockState getRailBlock(BlockState state) {
        BaseRailBlock railBlock = (BaseRailBlock) ((CartAssembleRailType) state.m_61143_(RAIL_TYPE)).getBlock();
        BlockState railState = (BlockState) railBlock.m_49966_().m_61124_(railBlock.getShapeProperty(), (RailShape) state.m_61143_(RAIL_SHAPE));
        if (railState.m_61138_(ControllerRailBlock.BACKWARDS)) {
            railState = (BlockState) railState.m_61124_(ControllerRailBlock.BACKWARDS, (Boolean) state.m_61143_(BACKWARDS));
        }
        return railState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RAIL_SHAPE, POWERED, RAIL_TYPE, BACKWARDS, f_152149_);
        super.m_7926_(builder);
    }

    public boolean canMakeSlopes(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return false;
    }

    public void onMinecartPass(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, AbstractMinecart cart) {
        if (canAssembleTo(cart)) {
            if (!world.isClientSide) {
                this.withBlockEntityDo(world, pos, be -> be.assembleNextTick(cart));
            }
        }
    }

    public static CartAssemblerBlock.CartAssemblerAction getActionForCart(BlockState state, AbstractMinecart cart) {
        CartAssembleRailType type = (CartAssembleRailType) state.m_61143_(RAIL_TYPE);
        boolean powered = (Boolean) state.m_61143_(POWERED);
        switch(type) {
            case ACTIVATOR_RAIL:
                return powered ? CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE : CartAssemblerBlock.CartAssemblerAction.PASS;
            case CONTROLLER_RAIL:
                return powered ? CartAssemblerBlock.CartAssemblerAction.ASSEMBLE_ACCELERATE_DIRECTIONAL : CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE_BRAKE;
            case DETECTOR_RAIL:
                return cart.m_20197_().isEmpty() ? CartAssemblerBlock.CartAssemblerAction.ASSEMBLE_ACCELERATE : CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE;
            case POWERED_RAIL:
                return powered ? CartAssemblerBlock.CartAssemblerAction.ASSEMBLE_ACCELERATE : CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE_BRAKE;
            case REGULAR:
                return powered ? CartAssemblerBlock.CartAssemblerAction.ASSEMBLE : CartAssemblerBlock.CartAssemblerAction.DISASSEMBLE;
            default:
                return CartAssemblerBlock.CartAssemblerAction.PASS;
        }
    }

    public static boolean canAssembleTo(AbstractMinecart cart) {
        return cart.canBeRidden() || cart instanceof MinecartFurnace || cart instanceof MinecartChest;
    }

    @Nonnull
    @Override
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult blockRayTraceResult) {
        ItemStack itemStack = player.m_21120_(hand);
        Item previousItem = getRailItem(state);
        Item heldItem = itemStack.getItem();
        if (heldItem != previousItem) {
            CartAssembleRailType newType = null;
            for (CartAssembleRailType type : CartAssembleRailType.values()) {
                if (heldItem == type.getItem()) {
                    newType = type;
                }
            }
            if (newType == null) {
                return InteractionResult.PASS;
            } else {
                world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                world.setBlockAndUpdate(pos, (BlockState) state.m_61124_(RAIL_TYPE, newType));
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.getInventory().placeItemBackInInventory(new ItemStack(previousItem));
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 2);
            }
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        }
    }

    @Nonnull
    @Override
    public Property<RailShape> getShapeProperty() {
        return RAIL_SHAPE;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return AllShapes.CART_ASSEMBLER.get(this.getRailAxis(state));
    }

    protected Direction.Axis getRailAxis(BlockState state) {
        return state.m_61143_(RAIL_SHAPE) == RailShape.NORTH_SOUTH ? Direction.Axis.Z : Direction.Axis.X;
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext) {
            Entity entity = ((EntityCollisionContext) context).getEntity();
            if (entity instanceof AbstractMinecart) {
                return Shapes.empty();
            }
            if (entity instanceof Player) {
                return AllShapes.CART_ASSEMBLER_PLAYER_COLLISION.get(this.getRailAxis(state));
            }
        }
        return Shapes.block();
    }

    @Nonnull
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public Class<CartAssemblerBlockEntity> getBlockEntityClass() {
        return CartAssemblerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CartAssemblerBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CartAssemblerBlockEntity>) AllBlockEntityTypes.CART_ASSEMBLER.get();
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, @Nonnull LevelReader world, @Nonnull BlockPos pos) {
        return false;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ArrayList<ItemStack> requiredItems = new ArrayList();
        requiredItems.add(new ItemStack(getRailItem(state)));
        requiredItems.add(new ItemStack(this.m_5456_()));
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, requiredItems);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        drops.addAll(getRailBlock(state).m_287290_(builder));
        return drops;
    }

    public List<ItemStack> getDropsNoRail(BlockState state, ServerLevel world, BlockPos pos, @Nullable BlockEntity p_220077_3_, @Nullable Entity p_220077_4_, ItemStack p_220077_5_) {
        return super.m_49635_(state, new LootParams.Builder(world).withParameter(LootContextParams.ORIGIN, Vec3.atLowerCornerOf(pos)).withParameter(LootContextParams.TOOL, p_220077_5_).withOptionalParameter(LootContextParams.THIS_ENTITY, p_220077_4_).withOptionalParameter(LootContextParams.BLOCK_ENTITY, p_220077_3_));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (player != null && !player.isCreative()) {
                this.getDropsNoRail(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand()).forEach(itemStack -> player.getInventory().placeItemBackInInventory(itemStack));
            }
            if (world instanceof ServerLevel) {
                state.m_222967_((ServerLevel) world, pos, ItemStack.EMPTY, true);
            }
            world.setBlockAndUpdate(pos, getRailBlock(state));
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockPos pos = context.getClickedPos();
            world.setBlock(pos, this.rotate(state, Rotation.CLOCKWISE_90), 3);
            world.updateNeighborsAt(pos.below(), this);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        } else {
            BlockState base = ((BlockState) ((BlockState) AllBlocks.CONTROLLER_RAIL.getDefaultState().m_61124_(ControllerRailBlock.SHAPE, (RailShape) state.m_61143_(RAIL_SHAPE))).m_61124_(ControllerRailBlock.BACKWARDS, (Boolean) state.m_61143_(BACKWARDS))).m_60717_(rotation);
            return (BlockState) ((BlockState) state.m_61124_(RAIL_SHAPE, (RailShape) base.m_61143_(ControllerRailBlock.SHAPE))).m_61124_(BACKWARDS, (Boolean) base.m_61143_(ControllerRailBlock.BACKWARDS));
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.NONE) {
            return state;
        } else {
            BlockState base = ((BlockState) ((BlockState) AllBlocks.CONTROLLER_RAIL.getDefaultState().m_61124_(ControllerRailBlock.SHAPE, (RailShape) state.m_61143_(RAIL_SHAPE))).m_61124_(ControllerRailBlock.BACKWARDS, (Boolean) state.m_61143_(BACKWARDS))).m_60715_(mirror);
            return (BlockState) state.m_61124_(BACKWARDS, (Boolean) base.m_61143_(ControllerRailBlock.BACKWARDS));
        }
    }

    public static Direction getHorizontalDirection(BlockState blockState) {
        if (!(blockState.m_60734_() instanceof CartAssemblerBlock)) {
            return Direction.SOUTH;
        } else {
            Direction pointingTo = getPointingTowards(blockState);
            return blockState.m_61143_(BACKWARDS) ? pointingTo.getOpposite() : pointingTo;
        }
    }

    private static Direction getPointingTowards(BlockState state) {
        switch((RailShape) state.m_61143_(RAIL_SHAPE)) {
            case EAST_WEST:
                return Direction.WEST;
            default:
                return Direction.NORTH;
        }
    }

    public static enum CartAssemblerAction {

        ASSEMBLE,
        DISASSEMBLE,
        ASSEMBLE_ACCELERATE,
        DISASSEMBLE_BRAKE,
        ASSEMBLE_ACCELERATE_DIRECTIONAL,
        PASS;

        public boolean shouldAssemble() {
            return this == ASSEMBLE || this == ASSEMBLE_ACCELERATE || this == ASSEMBLE_ACCELERATE_DIRECTIONAL;
        }

        public boolean shouldDisassemble() {
            return this == DISASSEMBLE || this == DISASSEMBLE_BRAKE;
        }
    }

    public static class MinecartAnchorBlock extends Block {

        public MinecartAnchorBlock(BlockBehaviour.Properties p_i48440_1_) {
            super(p_i48440_1_);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(BlockStateProperties.HORIZONTAL_AXIS);
            super.createBlockStateDefinition(builder);
        }

        @Nonnull
        @Override
        public VoxelShape getShape(@Nonnull BlockState p_220053_1_, @Nonnull BlockGetter p_220053_2_, @Nonnull BlockPos p_220053_3_, @Nonnull CollisionContext p_220053_4_) {
            return Shapes.empty();
        }
    }
}