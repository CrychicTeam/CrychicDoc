package com.simibubi.create.content.kinetics.belt;

import com.jozufozu.flywheel.light.LightListener;
import com.jozufozu.flywheel.light.LightUpdater;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.BeltInventory;
import com.simibubi.create.content.kinetics.belt.transport.BeltMovementHandler;
import com.simibubi.create.content.kinetics.belt.transport.BeltTunnelInteractionHandler;
import com.simibubi.create.content.kinetics.belt.transport.ItemHandlerBeltSegment;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.tunnel.BrassTunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;

public class BeltBlockEntity extends KineticBlockEntity {

    public Map<Entity, BeltMovementHandler.TransportedEntityInfo> passengers;

    public Optional<DyeColor> color;

    public int beltLength;

    public int index;

    public Direction lastInsert;

    public BeltBlockEntity.CasingType casing;

    public boolean covered;

    protected BlockPos controller = BlockPos.ZERO;

    protected BeltInventory inventory;

    protected LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();

    public CompoundTag trackerUpdateTag;

    @OnlyIn(Dist.CLIENT)
    public BeltBlockEntity.BeltLighter lighter;

    public BeltBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.casing = BeltBlockEntity.CasingType.NONE;
        this.color = Optional.empty();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::canInsertFrom).setInsertionHandler(this::tryInsertingFromSide).considerOccupiedWhen(this::isOccupied));
        behaviours.add(new TransportedItemStackHandlerBehaviour(this, this::applyToAllItems).withStackPlacement(this::getWorldPositionOf));
    }

    @Override
    public void tick() {
        if (this.beltLength == 0) {
            BeltBlock.initBelt(this.f_58857_, this.f_58858_);
        }
        super.tick();
        if (AllBlocks.BELT.has(this.f_58857_.getBlockState(this.f_58858_))) {
            this.initializeItemHandler();
            if (this.isController()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (this.beltLength > 0 && this.lighter == null) {
                        this.lighter = new BeltBlockEntity.BeltLighter();
                    }
                });
                this.invalidateRenderBoundingBox();
                this.getInventory().tick();
                if (this.getSpeed() != 0.0F) {
                    if (this.passengers == null) {
                        this.passengers = new HashMap();
                    }
                    List<Entity> toRemove = new ArrayList();
                    this.passengers.forEach((entity, info) -> {
                        boolean canBeTransported = BeltMovementHandler.canBeTransported(entity);
                        boolean leftTheBelt = info.getTicksSinceLastCollision() > (this.m_58900_().m_61143_(BeltBlock.SLOPE) != BeltSlope.HORIZONTAL ? 3 : 1);
                        if (canBeTransported && !leftTheBelt) {
                            info.tick();
                            BeltMovementHandler.transportEntity(this, entity, info);
                        } else {
                            toRemove.add(entity);
                        }
                    });
                    toRemove.forEach(this.passengers::remove);
                }
            }
        }
    }

    @Override
    public float calculateStressApplied() {
        return !this.isController() ? 0.0F : super.calculateStressApplied();
    }

    @Override
    public AABB createRenderBoundingBox() {
        return !this.isController() ? super.createRenderBoundingBox() : super.createRenderBoundingBox().inflate((double) (this.beltLength + 1));
    }

    protected void initializeItemHandler() {
        if (!this.f_58857_.isClientSide && !this.itemHandler.isPresent()) {
            if (this.beltLength != 0 && this.controller != null) {
                if (this.f_58857_.isLoaded(this.controller)) {
                    BlockEntity be = this.f_58857_.getBlockEntity(this.controller);
                    if (be != null && be instanceof BeltBlockEntity) {
                        BeltInventory inventory = ((BeltBlockEntity) be).getInventory();
                        if (inventory != null) {
                            IItemHandler handler = new ItemHandlerBeltSegment(inventory, this.index);
                            this.itemHandler = LazyOptional.of(() -> handler);
                        }
                    }
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.isItemHandlerCap(cap)) {
            return super.getCapability(cap, side);
        } else {
            if (!this.m_58901_() && !this.itemHandler.isPresent()) {
                this.initializeItemHandler();
            }
            return this.itemHandler.cast();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (this.isController()) {
            this.getInventory().ejectAll();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.itemHandler.invalidate();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (this.controller != null) {
            compound.put("Controller", NbtUtils.writeBlockPos(this.controller));
        }
        compound.putBoolean("IsController", this.isController());
        compound.putInt("Length", this.beltLength);
        compound.putInt("Index", this.index);
        NBTHelper.writeEnum(compound, "Casing", this.casing);
        compound.putBoolean("Covered", this.covered);
        if (this.color.isPresent()) {
            NBTHelper.writeEnum(compound, "Dye", (DyeColor) this.color.get());
        }
        if (this.isController()) {
            compound.put("Inventory", this.getInventory().write());
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        int prevBeltLength = this.beltLength;
        super.read(compound, clientPacket);
        if (compound.getBoolean("IsController")) {
            this.controller = this.f_58858_;
        }
        this.color = compound.contains("Dye") ? Optional.of((DyeColor) NBTHelper.readEnum(compound, "Dye", DyeColor.class)) : Optional.empty();
        if (!this.wasMoved) {
            if (!this.isController()) {
                this.controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));
            }
            this.trackerUpdateTag = compound;
            this.index = compound.getInt("Index");
            this.beltLength = compound.getInt("Length");
            if (prevBeltLength != this.beltLength) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    if (this.lighter != null) {
                        this.lighter.initializeLight();
                    }
                });
            }
        }
        if (this.isController()) {
            this.getInventory().read(compound.getCompound("Inventory"));
        }
        BeltBlockEntity.CasingType casingBefore = this.casing;
        boolean coverBefore = this.covered;
        this.casing = NBTHelper.readEnum(compound, "Casing", BeltBlockEntity.CasingType.class);
        this.covered = compound.getBoolean("Covered");
        if (clientPacket) {
            if (casingBefore != this.casing || coverBefore != this.covered) {
                if (!this.isVirtual()) {
                    this.requestModelDataUpdate();
                }
                if (this.m_58898_()) {
                    this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 16);
                }
            }
        }
    }

    @Override
    public void clearKineticInformation() {
        super.clearKineticInformation();
        this.beltLength = 0;
        this.index = 0;
        this.controller = null;
        this.trackerUpdateTag = new CompoundTag();
    }

    public boolean applyColor(DyeColor colorIn) {
        if (colorIn == null) {
            if (!this.color.isPresent()) {
                return false;
            }
        } else if (this.color.isPresent() && this.color.get() == colorIn) {
            return false;
        }
        if (this.f_58857_.isClientSide()) {
            return true;
        } else {
            for (BlockPos blockPos : BeltBlock.getBeltChain(this.f_58857_, this.getController())) {
                BeltBlockEntity belt = BeltHelper.getSegmentBE(this.f_58857_, blockPos);
                if (belt != null) {
                    belt.color = Optional.ofNullable(colorIn);
                    belt.m_6596_();
                    belt.sendData();
                }
            }
            return true;
        }
    }

    public BeltBlockEntity getControllerBE() {
        if (this.controller == null) {
            return null;
        } else if (!this.f_58857_.isLoaded(this.controller)) {
            return null;
        } else {
            BlockEntity be = this.f_58857_.getBlockEntity(this.controller);
            return be != null && be instanceof BeltBlockEntity ? (BeltBlockEntity) be : null;
        }
    }

    public void setController(BlockPos controller) {
        this.controller = controller;
    }

    public BlockPos getController() {
        return this.controller == null ? this.f_58858_ : this.controller;
    }

    public boolean isController() {
        return this.controller != null && this.f_58858_.m_123341_() == this.controller.m_123341_() && this.f_58858_.m_123342_() == this.controller.m_123342_() && this.f_58858_.m_123343_() == this.controller.m_123343_();
    }

    public float getBeltMovementSpeed() {
        return this.getSpeed() / 480.0F;
    }

    public float getDirectionAwareBeltMovementSpeed() {
        int offset = this.getBeltFacing().getAxisDirection().getStep();
        if (this.getBeltFacing().getAxis() == Direction.Axis.X) {
            offset *= -1;
        }
        return this.getBeltMovementSpeed() * (float) offset;
    }

    public boolean hasPulley() {
        return !AllBlocks.BELT.has(this.m_58900_()) ? false : this.m_58900_().m_61143_(BeltBlock.PART) != BeltPart.MIDDLE;
    }

    protected boolean isLastBelt() {
        if (this.getSpeed() == 0.0F) {
            return false;
        } else {
            Direction direction = this.getBeltFacing();
            if (this.m_58900_().m_61143_(BeltBlock.SLOPE) == BeltSlope.VERTICAL) {
                return false;
            } else {
                BeltPart part = (BeltPart) this.m_58900_().m_61143_(BeltBlock.PART);
                if (part == BeltPart.MIDDLE) {
                    return false;
                } else {
                    boolean movingPositively = this.getSpeed() > 0.0F == (direction.getAxisDirection().getStep() == 1) ^ direction.getAxis() == Direction.Axis.X;
                    return part == BeltPart.START ^ movingPositively;
                }
            }
        }
    }

    public Vec3i getMovementDirection(boolean firstHalf) {
        return this.getMovementDirection(firstHalf, false);
    }

    public Vec3i getBeltChainDirection() {
        return this.getMovementDirection(true, true);
    }

    protected Vec3i getMovementDirection(boolean firstHalf, boolean ignoreHalves) {
        if (this.getSpeed() == 0.0F) {
            return BlockPos.ZERO;
        } else {
            BlockState blockState = this.m_58900_();
            Direction beltFacing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
            BeltSlope slope = (BeltSlope) blockState.m_61143_(BeltBlock.SLOPE);
            BeltPart part = (BeltPart) blockState.m_61143_(BeltBlock.PART);
            Direction.Axis axis = beltFacing.getAxis();
            Direction movementFacing = Direction.get(axis == Direction.Axis.X ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE, axis);
            boolean notHorizontal = blockState.m_61143_(BeltBlock.SLOPE) != BeltSlope.HORIZONTAL;
            if (this.getSpeed() < 0.0F) {
                movementFacing = movementFacing.getOpposite();
            }
            Vec3i movement = movementFacing.getNormal();
            boolean slopeBeforeHalf = part == BeltPart.END == (beltFacing.getAxisDirection() == Direction.AxisDirection.POSITIVE);
            boolean onSlope = notHorizontal && (part == BeltPart.MIDDLE || slopeBeforeHalf == firstHalf || ignoreHalves);
            boolean movingUp = onSlope && slope == (movementFacing == beltFacing ? BeltSlope.UPWARD : BeltSlope.DOWNWARD);
            return !onSlope ? movement : new Vec3i(movement.getX(), movingUp ? 1 : -1, movement.getZ());
        }
    }

    public Direction getMovementFacing() {
        Direction.Axis axis = this.getBeltFacing().getAxis();
        return Direction.fromAxisAndDirection(axis, this.getBeltMovementSpeed() < 0.0F ^ axis == Direction.Axis.X ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE);
    }

    protected Direction getBeltFacing() {
        return (Direction) this.m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING);
    }

    public BeltInventory getInventory() {
        if (!this.isController()) {
            BeltBlockEntity controllerBE = this.getControllerBE();
            return controllerBE != null ? controllerBE.getInventory() : null;
        } else {
            if (this.inventory == null) {
                this.inventory = new BeltInventory(this);
            }
            return this.inventory;
        }
    }

    private void applyToAllItems(float maxDistanceFromCenter, Function<TransportedItemStack, TransportedItemStackHandlerBehaviour.TransportedResult> processFunction) {
        BeltBlockEntity controller = this.getControllerBE();
        if (controller != null) {
            BeltInventory inventory = controller.getInventory();
            if (inventory != null) {
                inventory.applyToEachWithin((float) this.index + 0.5F, maxDistanceFromCenter, processFunction);
            }
        }
    }

    private Vec3 getWorldPositionOf(TransportedItemStack transported) {
        BeltBlockEntity controllerBE = this.getControllerBE();
        return controllerBE == null ? Vec3.ZERO : BeltHelper.getVectorForOffset(controllerBE, transported.beltPosition);
    }

    public void setCasingType(BeltBlockEntity.CasingType type) {
        if (this.casing != type) {
            BlockState blockState = this.m_58900_();
            boolean shouldBlockHaveCasing = type != BeltBlockEntity.CasingType.NONE;
            if (this.f_58857_.isClientSide) {
                this.casing = type;
                this.f_58857_.setBlock(this.f_58858_, (BlockState) blockState.m_61124_(BeltBlock.CASING, shouldBlockHaveCasing), 0);
                this.requestModelDataUpdate();
                this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 16);
            } else {
                if (this.casing != BeltBlockEntity.CasingType.NONE) {
                    this.f_58857_.m_46796_(2001, this.f_58858_, Block.getId(this.casing == BeltBlockEntity.CasingType.ANDESITE ? AllBlocks.ANDESITE_CASING.getDefaultState() : AllBlocks.BRASS_CASING.getDefaultState()));
                }
                if ((Boolean) blockState.m_61143_(BeltBlock.CASING) != shouldBlockHaveCasing) {
                    KineticBlockEntity.switchToBlockState(this.f_58857_, this.f_58858_, (BlockState) blockState.m_61124_(BeltBlock.CASING, shouldBlockHaveCasing));
                }
                this.casing = type;
                this.m_6596_();
                this.sendData();
            }
        }
    }

    private boolean canInsertFrom(Direction side) {
        if (this.getSpeed() == 0.0F) {
            return false;
        } else {
            BlockState state = this.m_58900_();
            return !state.m_61138_(BeltBlock.SLOPE) || state.m_61143_(BeltBlock.SLOPE) != BeltSlope.SIDEWAYS && state.m_61143_(BeltBlock.SLOPE) != BeltSlope.VERTICAL ? this.getMovementFacing() != side.getOpposite() : false;
        }
    }

    private boolean isOccupied(Direction side) {
        BeltBlockEntity nextBeltController = this.getControllerBE();
        if (nextBeltController == null) {
            return true;
        } else {
            BeltInventory nextInventory = nextBeltController.getInventory();
            if (nextInventory == null) {
                return true;
            } else if (this.getSpeed() == 0.0F) {
                return true;
            } else {
                return this.getMovementFacing() == side.getOpposite() ? true : !nextInventory.canInsertAtFromSide(this.index, side);
            }
        }
    }

    private ItemStack tryInsertingFromSide(TransportedItemStack transportedStack, Direction side, boolean simulate) {
        BeltBlockEntity nextBeltController = this.getControllerBE();
        ItemStack inserted = transportedStack.stack;
        ItemStack empty = ItemStack.EMPTY;
        if (nextBeltController == null) {
            return inserted;
        } else {
            BeltInventory nextInventory = nextBeltController.getInventory();
            if (nextInventory == null) {
                return inserted;
            } else {
                if (this.f_58857_.getBlockEntity(this.f_58858_.above()) instanceof BrassTunnelBlockEntity tunnelBE && tunnelBE.hasDistributionBehaviour()) {
                    if (!tunnelBE.getStackToDistribute().isEmpty()) {
                        return inserted;
                    }
                    if (!tunnelBE.testFlapFilter(side.getOpposite(), inserted)) {
                        return inserted;
                    }
                    if (!simulate) {
                        BeltTunnelInteractionHandler.flapTunnel(nextInventory, this.index, side.getOpposite(), true);
                        tunnelBE.setStackToDistribute(inserted, side.getOpposite());
                    }
                    return empty;
                }
                if (this.isOccupied(side)) {
                    return inserted;
                } else if (simulate) {
                    return empty;
                } else {
                    transportedStack = transportedStack.copy();
                    transportedStack.beltPosition = (float) this.index + 0.5F - Math.signum(this.getDirectionAwareBeltMovementSpeed()) / 16.0F;
                    Direction movementFacing = this.getMovementFacing();
                    if (!side.getAxis().isVertical()) {
                        if (movementFacing != side) {
                            transportedStack.sideOffset = (float) side.getAxisDirection().getStep() * 0.35F;
                            if (side.getAxis() == Direction.Axis.X) {
                                transportedStack.sideOffset *= -1.0F;
                            }
                        } else {
                            transportedStack.beltPosition = this.getDirectionAwareBeltMovementSpeed() > 0.0F ? (float) this.index : (float) (this.index + 1);
                        }
                    }
                    transportedStack.prevSideOffset = transportedStack.sideOffset;
                    transportedStack.insertedAt = this.index;
                    transportedStack.insertedFrom = side;
                    transportedStack.prevBeltPosition = transportedStack.beltPosition;
                    BeltTunnelInteractionHandler.flapTunnel(nextInventory, this.index, side.getOpposite(), true);
                    nextInventory.addItem(transportedStack);
                    nextBeltController.m_6596_();
                    nextBeltController.sendData();
                    return empty;
                }
            }
        }
    }

    public ModelData getModelData() {
        return ModelData.builder().with(BeltModel.CASING_PROPERTY, this.casing).with(BeltModel.COVER_PROPERTY, this.covered).build();
    }

    @Override
    protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
        return state.m_61138_(BeltBlock.SLOPE) && (state.m_61143_(BeltBlock.SLOPE) == BeltSlope.UPWARD || state.m_61143_(BeltBlock.SLOPE) == BeltSlope.DOWNWARD);
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        if (target instanceof BeltBlockEntity && !connectedViaAxes) {
            return this.getController().equals(((BeltBlockEntity) target).getController()) ? 1.0F : 0.0F;
        } else {
            return 0.0F;
        }
    }

    public void invalidateItemHandler() {
        this.itemHandler.invalidate();
    }

    public boolean shouldRenderNormally() {
        if (this.f_58857_ == null) {
            return this.isController();
        } else {
            BlockState state = this.m_58900_();
            return state != null && state.m_61138_(BeltBlock.PART) && state.m_61143_(BeltBlock.PART) == BeltPart.START;
        }
    }

    public void setCovered(boolean blockCoveringBelt) {
        if (blockCoveringBelt != this.covered) {
            this.covered = blockCoveringBelt;
            this.notifyUpdate();
        }
    }

    @OnlyIn(Dist.CLIENT)
    class BeltLighter implements LightListener {

        private byte[] light;

        public BeltLighter() {
            this.initializeLight();
            LightUpdater.get(BeltBlockEntity.this.f_58857_).addListener(this);
        }

        public int lightSegments() {
            return this.light == null ? 0 : this.light.length / 2;
        }

        public int getPackedLight(int segment) {
            return this.light == null ? 0 : LightTexture.pack(this.light[segment * 2], this.light[segment * 2 + 1]);
        }

        public GridAlignedBB getVolume() {
            BlockPos endPos = BeltHelper.getPositionForOffset(BeltBlockEntity.this, BeltBlockEntity.this.beltLength - 1);
            GridAlignedBB bb = GridAlignedBB.from(BeltBlockEntity.this.f_58858_, endPos);
            bb.fixMinMax();
            return bb;
        }

        public boolean isListenerInvalid() {
            return BeltBlockEntity.this.f_58859_;
        }

        public void onLightUpdate(LightLayer type, ImmutableBox changed) {
            if (!BeltBlockEntity.this.f_58859_) {
                if (BeltBlockEntity.this.f_58857_ != null) {
                    GridAlignedBB beltVolume = this.getVolume();
                    if (beltVolume.intersects(changed)) {
                        if (type == LightLayer.BLOCK) {
                            this.updateBlockLight();
                        }
                        if (type == LightLayer.SKY) {
                            this.updateSkyLight();
                        }
                    }
                }
            }
        }

        private void initializeLight() {
            this.light = new byte[BeltBlockEntity.this.beltLength * 2];
            Vec3i vec = BeltBlockEntity.this.getBeltFacing().getNormal();
            BeltSlope slope = (BeltSlope) BeltBlockEntity.this.m_58900_().m_61143_(BeltBlock.SLOPE);
            int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(BeltBlockEntity.this.controller.m_123341_(), BeltBlockEntity.this.controller.m_123342_(), BeltBlockEntity.this.controller.m_123343_());
            for (int i = 0; i < BeltBlockEntity.this.beltLength * 2; i += 2) {
                this.light[i] = (byte) BeltBlockEntity.this.f_58857_.m_45517_(LightLayer.BLOCK, pos);
                this.light[i + 1] = (byte) BeltBlockEntity.this.f_58857_.m_45517_(LightLayer.SKY, pos);
                pos.move(vec.getX(), verticality, vec.getZ());
            }
        }

        private void updateBlockLight() {
            Vec3i vec = BeltBlockEntity.this.getBeltFacing().getNormal();
            BeltSlope slope = (BeltSlope) BeltBlockEntity.this.m_58900_().m_61143_(BeltBlock.SLOPE);
            int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(BeltBlockEntity.this.controller.m_123341_(), BeltBlockEntity.this.controller.m_123342_(), BeltBlockEntity.this.controller.m_123343_());
            for (int i = 0; i < BeltBlockEntity.this.beltLength * 2; i += 2) {
                this.light[i] = (byte) BeltBlockEntity.this.f_58857_.m_45517_(LightLayer.BLOCK, pos);
                pos.move(vec.getX(), verticality, vec.getZ());
            }
        }

        private void updateSkyLight() {
            Vec3i vec = BeltBlockEntity.this.getBeltFacing().getNormal();
            BeltSlope slope = (BeltSlope) BeltBlockEntity.this.m_58900_().m_61143_(BeltBlock.SLOPE);
            int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(BeltBlockEntity.this.controller.m_123341_(), BeltBlockEntity.this.controller.m_123342_(), BeltBlockEntity.this.controller.m_123343_());
            for (int i = 1; i < BeltBlockEntity.this.beltLength * 2; i += 2) {
                this.light[i] = (byte) BeltBlockEntity.this.f_58857_.m_45517_(LightLayer.SKY, pos);
                pos.move(vec.getX(), verticality, vec.getZ());
            }
        }
    }

    public static enum CasingType {

        NONE, ANDESITE, BRASS
    }
}