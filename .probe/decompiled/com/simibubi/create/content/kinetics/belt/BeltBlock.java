package com.simibubi.create.content.kinetics.belt;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.armor.DivingBootsItem;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;
import com.simibubi.create.content.kinetics.belt.transport.BeltTunnelInteractionHandler;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
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
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class BeltBlock extends HorizontalKineticBlock implements IBE<BeltBlockEntity>, ISpecialBlockItemRequirement, ITransformableBlock, ProperWaterloggedBlock {

    public static final Property<BeltSlope> SLOPE = EnumProperty.create("slope", BeltSlope.class);

    public static final Property<BeltPart> PART = EnumProperty.create("part", BeltPart.class);

    public static final BooleanProperty CASING = BooleanProperty.create("casing");

    public BeltBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(SLOPE, BeltSlope.HORIZONTAL)).m_61124_(PART, BeltPart.START)).m_61124_(CASING, false)).m_61124_(WATERLOGGED, false));
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new BeltBlock.RenderProperties());
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return super.areStatesKineticallyEquivalent(oldState, newState) && oldState.m_61143_(PART) == newState.m_61143_(PART);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() != this.getRotationAxis(state) ? false : (Boolean) this.getBlockEntityOptional(world, pos).map(BeltBlockEntity::hasPulley).orElse(false);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.m_61143_(SLOPE) == BeltSlope.SIDEWAYS ? Direction.Axis.Y : ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise().getAxis();
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return AllItems.BELT_CONNECTOR.asStack();
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof BeltBlockEntity && ((BeltBlockEntity) blockEntity).hasPulley()) {
            drops.addAll(AllBlocks.SHAFT.getDefaultState().m_287290_(builder));
        }
        return drops;
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack p_220062_4_, boolean b) {
        BeltBlockEntity controllerBE = BeltHelper.getControllerBE(worldIn, pos);
        if (controllerBE != null) {
            controllerBE.getInventory().ejectAll();
        }
    }

    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return false;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.m_5548_(worldIn, entityIn);
        BlockPos entityPosition = entityIn.blockPosition();
        BlockPos beltPos = null;
        if (AllBlocks.BELT.has(worldIn.getBlockState(entityPosition))) {
            beltPos = entityPosition;
        } else if (AllBlocks.BELT.has(worldIn.getBlockState(entityPosition.below()))) {
            beltPos = entityPosition.below();
        }
        if (beltPos != null) {
            if (worldIn instanceof Level) {
                this.entityInside(worldIn.getBlockState(beltPos), (Level) worldIn, beltPos, entityIn);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (canTransportObjects(state)) {
            if (entityIn instanceof Player player) {
                if (player.m_6144_()) {
                    return;
                }
                if (player.getAbilities().flying) {
                    return;
                }
            }
            if (!DivingBootsItem.isWornBy(entityIn)) {
                BeltBlockEntity belt = BeltHelper.getSegmentBE(worldIn, pos);
                if (belt != null) {
                    if (!(entityIn instanceof ItemEntity) || !entityIn.isAlive()) {
                        BeltBlockEntity controller = BeltHelper.getControllerBE(worldIn, pos);
                        if (controller != null && controller.passengers != null) {
                            if (controller.passengers.containsKey(entityIn)) {
                                BeltMovementHandler.TransportedEntityInfo info = (BeltMovementHandler.TransportedEntityInfo) controller.passengers.get(entityIn);
                                if (info.getTicksSinceLastCollision() != 0 || pos.equals(entityIn.blockPosition())) {
                                    info.refresh(pos, state);
                                }
                            } else {
                                controller.passengers.put(entityIn, new BeltMovementHandler.TransportedEntityInfo(pos, state));
                                entityIn.setOnGround(true);
                            }
                        }
                    } else if (!worldIn.isClientSide) {
                        if (!(entityIn.getDeltaMovement().y > 0.0)) {
                            if (entityIn.isAlive()) {
                                if (BeltTunnelInteractionHandler.getTunnelOnPosition(worldIn, pos) == null) {
                                    this.withBlockEntityDo(worldIn, pos, be -> {
                                        ItemEntity itemEntity = (ItemEntity) entityIn;
                                        IItemHandler handler = (IItemHandler) be.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
                                        if (handler != null) {
                                            ItemStack remainder = handler.insertItem(0, itemEntity.getItem().copy(), false);
                                            if (remainder.isEmpty()) {
                                                itemEntity.m_146870_();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean canTransportObjects(BlockState state) {
        if (!AllBlocks.BELT.has(state)) {
            return false;
        } else {
            BeltSlope slope = (BeltSlope) state.m_61143_(SLOPE);
            return slope != BeltSlope.VERTICAL && slope != BeltSlope.SIDEWAYS;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.m_6144_() && player.mayBuild()) {
            ItemStack heldItem = player.m_21120_(handIn);
            boolean isWrench = AllItems.WRENCH.isIn(heldItem);
            boolean isConnector = AllItems.BELT_CONNECTOR.isIn(heldItem);
            boolean isShaft = AllBlocks.SHAFT.isIn(heldItem);
            boolean isDye = heldItem.is(Tags.Items.DYES);
            boolean hasWater = GenericItemEmptying.emptyItem(world, heldItem, true).getFirst().getFluid().isSame(Fluids.WATER);
            boolean isHand = heldItem.isEmpty() && handIn == InteractionHand.MAIN_HAND;
            if (isDye || hasWater) {
                return this.onBlockEntityUse(world, pos, be -> be.applyColor(DyeColor.getColor(heldItem)) ? InteractionResult.SUCCESS : InteractionResult.PASS);
            } else if (isConnector) {
                return BeltSlicer.useConnector(state, world, pos, player, handIn, hit, new BeltSlicer.Feedback());
            } else if (isWrench) {
                return BeltSlicer.useWrench(state, world, pos, player, handIn, hit, new BeltSlicer.Feedback());
            } else {
                BeltBlockEntity belt = BeltHelper.getSegmentBE(world, pos);
                if (belt == null) {
                    return InteractionResult.PASS;
                } else {
                    if (isHand) {
                        BeltBlockEntity controllerBelt = belt.getControllerBE();
                        if (controllerBelt == null) {
                            return InteractionResult.PASS;
                        }
                        if (world.isClientSide) {
                            return InteractionResult.SUCCESS;
                        }
                        MutableBoolean success = new MutableBoolean(false);
                        controllerBelt.getInventory().applyToEachWithin((float) belt.index + 0.5F, 0.55F, transportedItemStack -> {
                            player.getInventory().placeItemBackInInventory(transportedItemStack.stack);
                            success.setTrue();
                            return TransportedItemStackHandlerBehaviour.TransportedResult.removeItem();
                        });
                        if (success.isTrue()) {
                            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
                        }
                    }
                    if (isShaft) {
                        if (state.m_61143_(PART) != BeltPart.MIDDLE) {
                            return InteractionResult.PASS;
                        } else if (world.isClientSide) {
                            return InteractionResult.SUCCESS;
                        } else {
                            if (!player.isCreative()) {
                                heldItem.shrink(1);
                            }
                            KineticBlockEntity.switchToBlockState(world, pos, (BlockState) state.m_61124_(PART, BeltPart.PULLEY));
                            return InteractionResult.SUCCESS;
                        }
                    } else if (AllBlocks.BRASS_CASING.isIn(heldItem)) {
                        this.withBlockEntityDo(world, pos, be -> be.setCasingType(BeltBlockEntity.CasingType.BRASS));
                        this.updateCoverProperty(world, pos, world.getBlockState(pos));
                        return InteractionResult.SUCCESS;
                    } else if (AllBlocks.ANDESITE_CASING.isIn(heldItem)) {
                        this.withBlockEntityDo(world, pos, be -> be.setCasingType(BeltBlockEntity.CasingType.ANDESITE));
                        this.updateCoverProperty(world, pos, world.getBlockState(pos));
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.PASS;
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if ((Boolean) state.m_61143_(CASING)) {
            if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(world, pos, be -> be.setCasingType(BeltBlockEntity.CasingType.NONE));
                return InteractionResult.SUCCESS;
            }
        } else if (state.m_61143_(PART) == BeltPart.PULLEY) {
            if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                KineticBlockEntity.switchToBlockState(world, pos, (BlockState) state.m_61124_(PART, BeltPart.MIDDLE));
                if (player != null && !player.isCreative()) {
                    player.getInventory().placeItemBackInInventory(AllBlocks.SHAFT.asStack());
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLOPE, PART, CASING, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
        return BlockPathTypes.RAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BeltShapes.getShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (state.m_60734_() != this) {
            return Shapes.empty();
        } else {
            VoxelShape shape = this.getShape(state, worldIn, pos, context);
            return !(context instanceof EntityCollisionContext) ? shape : (VoxelShape) this.getBlockEntityOptional(worldIn, pos).map(be -> {
                Entity entity = ((EntityCollisionContext) context).getEntity();
                if (entity == null) {
                    return shape;
                } else {
                    BeltBlockEntity controller = be.getControllerBE();
                    if (controller == null) {
                        return shape;
                    } else {
                        return controller.passengers != null && controller.passengers.containsKey(entity) ? shape : BeltShapes.getCollisionShape(state);
                    }
                }
            }).orElse(shape);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.m_61143_(CASING) ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public static void initBelt(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            if (!(world instanceof ServerLevel) || !(((ServerLevel) world).getChunkSource().getGenerator() instanceof DebugLevelSource)) {
                BlockState state = world.getBlockState(pos);
                if (AllBlocks.BELT.has(state)) {
                    int limit = 1000;
                    BlockPos currentPos = pos;
                    while (limit-- > 0) {
                        BlockState currentState = world.getBlockState(currentPos);
                        if (!AllBlocks.BELT.has(currentState)) {
                            world.m_46961_(pos, true);
                            return;
                        }
                        BlockPos nextSegmentPosition = nextSegmentPosition(currentState, currentPos, false);
                        if (nextSegmentPosition == null) {
                            break;
                        }
                        if (!world.isLoaded(nextSegmentPosition)) {
                            return;
                        }
                        currentPos = nextSegmentPosition;
                    }
                    int index = 0;
                    List<BlockPos> beltChain = getBeltChain(world, currentPos);
                    if (beltChain.size() < 2) {
                        world.m_46961_(currentPos, true);
                    } else {
                        for (BlockPos beltPos : beltChain) {
                            BlockEntity blockEntity = world.getBlockEntity(beltPos);
                            BlockState currentStatex = world.getBlockState(beltPos);
                            if (!(blockEntity instanceof BeltBlockEntity) || !AllBlocks.BELT.has(currentStatex)) {
                                world.m_46961_(currentPos, true);
                                return;
                            }
                            BeltBlockEntity be = (BeltBlockEntity) blockEntity;
                            be.setController(currentPos);
                            be.beltLength = beltChain.size();
                            be.index = index;
                            be.attachKinetics();
                            be.m_6596_();
                            be.sendData();
                            if (be.isController() && !canTransportObjects(currentStatex)) {
                                be.getInventory().ejectAll();
                            }
                            index++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.m_6810_(state, world, pos, newState, isMoving);
        if (!world.isClientSide) {
            if (state.m_60734_() != newState.m_60734_()) {
                if (!isMoving) {
                    for (boolean forward : Iterate.trueAndFalse) {
                        BlockPos currentPos = nextSegmentPosition(state, pos, forward);
                        if (currentPos != null) {
                            BlockState currentState = world.getBlockState(currentPos);
                            if (AllBlocks.BELT.has(currentState)) {
                                boolean hasPulley = false;
                                BlockEntity blockEntity = world.getBlockEntity(currentPos);
                                if (blockEntity instanceof BeltBlockEntity) {
                                    BeltBlockEntity belt = (BeltBlockEntity) blockEntity;
                                    if (belt.isController()) {
                                        belt.getInventory().ejectAll();
                                    }
                                    hasPulley = belt.hasPulley();
                                }
                                world.removeBlockEntity(currentPos);
                                BlockState shaftState = (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(BlockStateProperties.AXIS, this.getRotationAxis(currentState));
                                world.setBlock(currentPos, ProperWaterloggedBlock.withWater(world, hasPulley ? shaftState : Blocks.AIR.defaultBlockState(), currentPos), 3);
                                world.m_46796_(2001, currentPos, Block.getId(currentState));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction side, BlockState p_196271_3_, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        this.updateWater(world, state, pos);
        if (side.getAxis().isHorizontal()) {
            this.updateTunnelConnections(world, pos.above());
        }
        if (side == Direction.UP) {
            this.updateCoverProperty(world, pos, state);
        }
        return state;
    }

    public void updateCoverProperty(LevelAccessor world, BlockPos pos, BlockState state) {
        if (!world.m_5776_()) {
            if ((Boolean) state.m_61143_(CASING) && state.m_61143_(SLOPE) == BeltSlope.HORIZONTAL) {
                this.withBlockEntityDo(world, pos, bbe -> bbe.setCovered(isBlockCoveringBelt(world, pos.above())));
            }
        }
    }

    public static boolean isBlockCoveringBelt(LevelAccessor world, BlockPos pos) {
        BlockState blockState = world.m_8055_(pos);
        VoxelShape collisionShape = blockState.m_60812_(world, pos);
        if (collisionShape.isEmpty()) {
            return false;
        } else {
            AABB bounds = collisionShape.bounds();
            if (bounds.getXsize() < 0.5 || bounds.getZsize() < 0.5) {
                return false;
            } else if (bounds.minY > 0.0) {
                return false;
            } else if (AllBlocks.CRUSHING_WHEEL_CONTROLLER.has(blockState)) {
                return false;
            } else {
                return FunnelBlock.isFunnel(blockState) && FunnelBlock.getFunnelFacing(blockState) != Direction.UP ? false : !(blockState.m_60734_() instanceof BeltTunnelBlock);
            }
        }
    }

    private void updateTunnelConnections(LevelAccessor world, BlockPos pos) {
        Block tunnelBlock = world.m_8055_(pos).m_60734_();
        if (tunnelBlock instanceof BeltTunnelBlock) {
            ((BeltTunnelBlock) tunnelBlock).updateTunnel(world, pos);
        }
    }

    public static List<BlockPos> getBeltChain(Level world, BlockPos controllerPos) {
        List<BlockPos> positions = new LinkedList();
        BlockState blockState = world.getBlockState(controllerPos);
        if (!AllBlocks.BELT.has(blockState)) {
            return positions;
        } else {
            int limit = 1000;
            BlockPos current = controllerPos;
            while (limit-- > 0 && current != null) {
                BlockState state = world.getBlockState(current);
                if (!AllBlocks.BELT.has(state)) {
                    break;
                }
                positions.add(current);
                current = nextSegmentPosition(state, current, true);
            }
            return positions;
        }
    }

    public static BlockPos nextSegmentPosition(BlockState state, BlockPos pos, boolean forward) {
        Direction direction = (Direction) state.m_61143_(HORIZONTAL_FACING);
        BeltSlope slope = (BeltSlope) state.m_61143_(SLOPE);
        BeltPart part = (BeltPart) state.m_61143_(PART);
        int offset = forward ? 1 : -1;
        if ((part != BeltPart.END || !forward) && (part != BeltPart.START || forward)) {
            if (slope == BeltSlope.VERTICAL) {
                return pos.above(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? offset : -offset);
            } else {
                pos = pos.relative(direction, offset);
                return slope != BeltSlope.HORIZONTAL && slope != BeltSlope.SIDEWAYS ? pos.above(slope == BeltSlope.UPWARD ? offset : -offset) : pos;
            }
        } else {
            return null;
        }
    }

    @Override
    public Class<BeltBlockEntity> getBlockEntityClass() {
        return BeltBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BeltBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BeltBlockEntity>) AllBlockEntityTypes.BELT.get();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        List<ItemStack> required = new ArrayList();
        if (state.m_61143_(PART) != BeltPart.MIDDLE) {
            required.add(AllBlocks.SHAFT.asStack());
        }
        if (state.m_61143_(PART) == BeltPart.START) {
            required.add(AllItems.BELT_CONNECTOR.asStack());
        }
        return required.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, required);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        BlockState rotate = super.rotate(state, rot);
        if (state.m_61143_(SLOPE) != BeltSlope.VERTICAL) {
            return rotate;
        } else {
            if (((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxisDirection() != ((Direction) rotate.m_61143_(HORIZONTAL_FACING)).getAxisDirection()) {
                if (state.m_61143_(PART) == BeltPart.START) {
                    return (BlockState) rotate.m_61124_(PART, BeltPart.END);
                }
                if (state.m_61143_(PART) == BeltPart.END) {
                    return (BlockState) rotate.m_61124_(PART, BeltPart.START);
                }
            }
            return rotate;
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.m_6943_(state, transform.mirror);
        }
        return transform.rotationAxis == Direction.Axis.Y ? this.rotate(state, transform.rotation) : this.transformInner(state, transform);
    }

    protected BlockState transformInner(BlockState state, StructureTransform transform) {
        boolean halfTurn = transform.rotation == Rotation.CLOCKWISE_180;
        Direction initialDirection = (Direction) state.m_61143_(HORIZONTAL_FACING);
        boolean diagonal = state.m_61143_(SLOPE) == BeltSlope.DOWNWARD || state.m_61143_(SLOPE) == BeltSlope.UPWARD;
        if (!diagonal) {
            for (int i = 0; i < transform.rotation.ordinal(); i++) {
                Direction direction = (Direction) state.m_61143_(HORIZONTAL_FACING);
                BeltSlope slope = (BeltSlope) state.m_61143_(SLOPE);
                boolean vertical = slope == BeltSlope.VERTICAL;
                boolean horizontal = slope == BeltSlope.HORIZONTAL;
                boolean sideways = slope == BeltSlope.SIDEWAYS;
                Direction newDirection = direction.getOpposite();
                BeltSlope newSlope = BeltSlope.VERTICAL;
                if (vertical) {
                    if (direction.getAxis() == transform.rotationAxis) {
                        newDirection = direction.getCounterClockWise();
                        newSlope = BeltSlope.SIDEWAYS;
                    } else {
                        newSlope = BeltSlope.HORIZONTAL;
                        newDirection = direction;
                        if (direction.getAxis() == Direction.Axis.Z) {
                            newDirection = direction.getOpposite();
                        }
                    }
                }
                if (sideways) {
                    newDirection = direction;
                    if (direction.getAxis() == transform.rotationAxis) {
                        newSlope = BeltSlope.HORIZONTAL;
                    } else {
                        newDirection = direction.getCounterClockWise();
                    }
                }
                if (horizontal) {
                    newDirection = direction;
                    if (direction.getAxis() == transform.rotationAxis) {
                        newSlope = BeltSlope.SIDEWAYS;
                    } else if (direction.getAxis() != Direction.Axis.Z) {
                        newDirection = direction.getOpposite();
                    }
                }
                state = (BlockState) state.m_61124_(HORIZONTAL_FACING, newDirection);
                state = (BlockState) state.m_61124_(SLOPE, newSlope);
            }
        } else if (initialDirection.getAxis() != transform.rotationAxis) {
            for (int i = 0; i < transform.rotation.ordinal(); i++) {
                Direction directionx = (Direction) state.m_61143_(HORIZONTAL_FACING);
                Direction newDirectionx = directionx.getOpposite();
                BeltSlope slopex = (BeltSlope) state.m_61143_(SLOPE);
                boolean upward = slopex == BeltSlope.UPWARD;
                boolean downward = slopex == BeltSlope.DOWNWARD;
                if (directionx.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ downward ^ directionx.getAxis() == Direction.Axis.Z) {
                    state = (BlockState) state.m_61124_(SLOPE, upward ? BeltSlope.DOWNWARD : BeltSlope.UPWARD);
                } else {
                    state = (BlockState) state.m_61124_(HORIZONTAL_FACING, newDirectionx);
                }
            }
        } else if (halfTurn) {
            Direction directionx = (Direction) state.m_61143_(HORIZONTAL_FACING);
            Direction newDirectionx = directionx.getOpposite();
            BeltSlope slopex = (BeltSlope) state.m_61143_(SLOPE);
            boolean verticalx = slopex == BeltSlope.VERTICAL;
            if (diagonal) {
                state = (BlockState) state.m_61124_(SLOPE, slopex == BeltSlope.UPWARD ? BeltSlope.DOWNWARD : (slopex == BeltSlope.DOWNWARD ? BeltSlope.UPWARD : slopex));
            } else if (verticalx) {
                state = (BlockState) state.m_61124_(HORIZONTAL_FACING, newDirectionx);
            }
        }
        return state;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    public static class RenderProperties extends ReducedDestroyEffects implements MultiPosDestructionHandler {

        @Override
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            return level.m_7702_(pos) instanceof BeltBlockEntity belt ? new HashSet(BeltBlock.getBeltChain(level, belt.getController())) : null;
        }
    }
}