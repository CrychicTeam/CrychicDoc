package com.simibubi.create.content.logistics.funnel;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.redstone.smartObserver.SmartObserverBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class FunnelBlockEntity extends SmartBlockEntity implements IHaveHoveringInformation {

    private FilteringBehaviour filtering;

    private InvManipulationBehaviour invManipulation;

    private VersionedInventoryTrackerBehaviour invVersionTracker;

    private int extractionCooldown = 0;

    private WeakReference<ItemEntity> lastObserved;

    LerpedFloat flap = this.createChasingFlap();

    static final AABB coreBB = new AABB(VecHelper.CENTER_OF_ORIGIN, VecHelper.CENTER_OF_ORIGIN).inflate(0.75);

    public FunnelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public FunnelBlockEntity.Mode determineCurrentMode() {
        BlockState state = this.m_58900_();
        if (!FunnelBlock.isFunnel(state)) {
            return FunnelBlockEntity.Mode.INVALID;
        } else if ((Boolean) state.m_61145_(BlockStateProperties.POWERED).orElse(false)) {
            return FunnelBlockEntity.Mode.PAUSED;
        } else if (state.m_60734_() instanceof BeltFunnelBlock) {
            BeltFunnelBlock.Shape shape = (BeltFunnelBlock.Shape) state.m_61143_(BeltFunnelBlock.SHAPE);
            if (shape == BeltFunnelBlock.Shape.PULLING) {
                return FunnelBlockEntity.Mode.TAKING_FROM_BELT;
            } else if (shape == BeltFunnelBlock.Shape.PUSHING) {
                return FunnelBlockEntity.Mode.PUSHING_TO_BELT;
            } else {
                BeltBlockEntity belt = BeltHelper.getSegmentBE(this.f_58857_, this.f_58858_.below());
                if (belt != null) {
                    return belt.getMovementFacing() == state.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING) ? FunnelBlockEntity.Mode.PUSHING_TO_BELT : FunnelBlockEntity.Mode.TAKING_FROM_BELT;
                } else {
                    return FunnelBlockEntity.Mode.INVALID;
                }
            }
        } else if (state.m_60734_() instanceof FunnelBlock) {
            return state.m_61143_(FunnelBlock.EXTRACTING) ? FunnelBlockEntity.Mode.EXTRACT : FunnelBlockEntity.Mode.COLLECT;
        } else {
            return FunnelBlockEntity.Mode.INVALID;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.flap.tickChaser();
        FunnelBlockEntity.Mode mode = this.determineCurrentMode();
        if (!this.f_58857_.isClientSide) {
            if (mode == FunnelBlockEntity.Mode.PAUSED) {
                this.extractionCooldown = 0;
            }
            if (mode != FunnelBlockEntity.Mode.TAKING_FROM_BELT) {
                if (this.extractionCooldown > 0) {
                    this.extractionCooldown--;
                } else {
                    if (mode == FunnelBlockEntity.Mode.PUSHING_TO_BELT) {
                        this.activateExtractingBeltFunnel();
                    }
                    if (mode == FunnelBlockEntity.Mode.EXTRACT) {
                        this.activateExtractor();
                    }
                }
            }
        }
    }

    private void activateExtractor() {
        if (!this.invVersionTracker.stillWaiting(this.invManipulation)) {
            BlockState blockState = this.m_58900_();
            Direction facing = AbstractFunnelBlock.getFunnelFacing(blockState);
            if (facing != null) {
                boolean trackingEntityPresent = true;
                AABB area = this.getEntityOverflowScanningArea();
                if (this.lastObserved == null) {
                    trackingEntityPresent = false;
                } else {
                    ItemEntity lastEntity = (ItemEntity) this.lastObserved.get();
                    if (lastEntity == null || !lastEntity.m_6084_() || !lastEntity.m_20191_().intersects(area)) {
                        trackingEntityPresent = false;
                        this.lastObserved = null;
                    }
                }
                if (!trackingEntityPresent) {
                    int amountToExtract = this.getAmountToExtract();
                    ItemHelper.ExtractionCountMode mode = this.getModeToExtract();
                    ItemStack stack = this.invManipulation.simulate().extract(mode, amountToExtract);
                    if (stack.isEmpty()) {
                        this.invVersionTracker.awaitNewVersion(this.invManipulation);
                    } else {
                        Iterator outputPos = this.f_58857_.m_45976_(ItemEntity.class, area).iterator();
                        if (outputPos.hasNext()) {
                            ItemEntity itemEntity = (ItemEntity) outputPos.next();
                            this.lastObserved = new WeakReference(itemEntity);
                        } else {
                            stack = this.invManipulation.extract(mode, amountToExtract);
                            if (!stack.isEmpty()) {
                                this.flap(false);
                                this.onTransfer(stack);
                                Vec3 outputPosx = VecHelper.getCenterOf(this.f_58858_);
                                boolean vertical = facing.getAxis().isVertical();
                                boolean up = facing == Direction.UP;
                                Vec3 var16 = outputPosx.add(Vec3.atLowerCornerOf(facing.getNormal()).scale(vertical ? (up ? 0.15F : 0.5) : 0.25));
                                if (!vertical) {
                                    var16 = var16.subtract(0.0, 0.45F, 0.0);
                                }
                                Vec3 motion = Vec3.ZERO;
                                if (up) {
                                    motion = new Vec3(0.0, 0.25, 0.0);
                                }
                                ItemEntity item = new ItemEntity(this.f_58857_, var16.x, var16.y, var16.z, stack.copy());
                                item.setDefaultPickUpDelay();
                                item.m_20256_(motion);
                                this.f_58857_.m_7967_(item);
                                this.lastObserved = new WeakReference(item);
                                this.startCooldown();
                            }
                        }
                    }
                }
            }
        }
    }

    private AABB getEntityOverflowScanningArea() {
        Direction facing = AbstractFunnelBlock.getFunnelFacing(this.m_58900_());
        AABB bb = coreBB.move(this.f_58858_);
        return facing != null && facing != Direction.UP ? bb.expandTowards(0.0, -1.0, 0.0) : bb;
    }

    private void activateExtractingBeltFunnel() {
        if (!this.invVersionTracker.stillWaiting(this.invManipulation)) {
            BlockState blockState = this.m_58900_();
            Direction facing = (Direction) blockState.m_61143_(BeltFunnelBlock.HORIZONTAL_FACING);
            DirectBeltInputBehaviour inputBehaviour = BlockEntityBehaviour.get(this.f_58857_, this.f_58858_.below(), DirectBeltInputBehaviour.TYPE);
            if (inputBehaviour != null) {
                if (inputBehaviour.canInsertFromSide(facing)) {
                    if (!inputBehaviour.isOccupied(facing)) {
                        int amountToExtract = this.getAmountToExtract();
                        ItemHelper.ExtractionCountMode mode = this.getModeToExtract();
                        MutableBoolean deniedByInsertion = new MutableBoolean(false);
                        ItemStack stack = this.invManipulation.extract(mode, amountToExtract, s -> {
                            ItemStack handleInsertion = inputBehaviour.handleInsertion(s, facing, true);
                            if (handleInsertion.isEmpty()) {
                                return true;
                            } else {
                                deniedByInsertion.setTrue();
                                return false;
                            }
                        });
                        if (stack.isEmpty()) {
                            if (deniedByInsertion.isFalse()) {
                                this.invVersionTracker.awaitNewVersion(this.invManipulation.getInventory());
                            }
                        } else {
                            this.flap(false);
                            this.onTransfer(stack);
                            inputBehaviour.handleInsertion(stack, facing, false);
                            this.startCooldown();
                        }
                    }
                }
            }
        }
    }

    public int getAmountToExtract() {
        if (!this.supportsAmountOnFilter()) {
            return 64;
        } else {
            int amountToExtract = this.invManipulation.getAmountFromFilter();
            if (!this.filtering.isActive()) {
                amountToExtract = 1;
            }
            return amountToExtract;
        }
    }

    public ItemHelper.ExtractionCountMode getModeToExtract() {
        return this.supportsAmountOnFilter() && this.filtering.isActive() ? this.invManipulation.getModeFromFilter() : ItemHelper.ExtractionCountMode.UPTO;
    }

    private int startCooldown() {
        return this.extractionCooldown = AllConfigs.server().logistics.defaultExtractionTimer.get();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.invManipulation = new InvManipulationBehaviour(this, (w, p, s) -> new BlockFace(p, AbstractFunnelBlock.getFunnelFacing(s).getOpposite()));
        behaviours.add(this.invManipulation);
        behaviours.add(this.invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
        this.filtering = new FilteringBehaviour(this, new FunnelFilterSlotPositioning());
        this.filtering.showCountWhen(this::supportsAmountOnFilter);
        this.filtering.onlyActiveWhen(this::supportsFiltering);
        this.filtering.withCallback($ -> this.invVersionTracker.reset());
        behaviours.add(this.filtering);
        behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(this::supportsDirectBeltInput).setInsertionHandler(this::handleDirectBeltInput));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.FUNNEL });
    }

    private boolean supportsAmountOnFilter() {
        BlockState blockState = this.m_58900_();
        boolean beltFunnelsupportsAmount = false;
        if (blockState.m_60734_() instanceof BeltFunnelBlock) {
            BeltFunnelBlock.Shape shape = (BeltFunnelBlock.Shape) blockState.m_61143_(BeltFunnelBlock.SHAPE);
            if (shape == BeltFunnelBlock.Shape.PUSHING) {
                beltFunnelsupportsAmount = true;
            } else {
                beltFunnelsupportsAmount = BeltHelper.getSegmentBE(this.f_58857_, this.f_58858_.below()) != null;
            }
        }
        boolean extractor = blockState.m_60734_() instanceof FunnelBlock && (Boolean) blockState.m_61143_(FunnelBlock.EXTRACTING);
        return beltFunnelsupportsAmount || extractor;
    }

    private boolean supportsDirectBeltInput(Direction side) {
        BlockState blockState = this.m_58900_();
        if (blockState == null) {
            return false;
        } else if (!(blockState.m_60734_() instanceof FunnelBlock)) {
            return false;
        } else {
            return blockState.m_61143_(FunnelBlock.EXTRACTING) ? false : FunnelBlock.getFunnelFacing(blockState) == Direction.UP;
        }
    }

    private boolean supportsFiltering() {
        BlockState blockState = this.m_58900_();
        return AllBlocks.BRASS_BELT_FUNNEL.has(blockState) || AllBlocks.BRASS_FUNNEL.has(blockState);
    }

    private ItemStack handleDirectBeltInput(TransportedItemStack stack, Direction side, boolean simulate) {
        ItemStack inserted = stack.stack;
        if (!this.filtering.test(inserted)) {
            return inserted;
        } else if (this.determineCurrentMode() == FunnelBlockEntity.Mode.PAUSED) {
            return inserted;
        } else {
            if (simulate) {
                this.invManipulation.simulate();
            }
            if (!simulate) {
                this.onTransfer(inserted);
            }
            return this.invManipulation.insert(inserted);
        }
    }

    public void flap(boolean inward) {
        if (!this.f_58857_.isClientSide) {
            AllPackets.getChannel().send(this.packetTarget(), new FunnelFlapPacket(this, inward));
        } else {
            this.flap.setValue(inward ? 1.0 : -1.0);
            AllSoundEvents.FUNNEL_FLAP.playAt(this.f_58857_, this.f_58858_, 1.0F, 1.0F, true);
        }
    }

    public boolean hasFlap() {
        BlockState blockState = this.m_58900_();
        return AbstractFunnelBlock.getFunnelFacing(blockState).getAxis().isHorizontal();
    }

    public float getFlapOffset() {
        BlockState blockState = this.m_58900_();
        if (!(blockState.m_60734_() instanceof BeltFunnelBlock)) {
            return -0.0625F;
        } else {
            switch((BeltFunnelBlock.Shape) blockState.m_61143_(BeltFunnelBlock.SHAPE)) {
                case RETRACTED:
                default:
                    return 0.0F;
                case EXTENDED:
                    return 0.5F;
                case PULLING:
                case PUSHING:
                    return -0.125F;
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("TransferCooldown", this.extractionCooldown);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.extractionCooldown = compound.getInt("TransferCooldown");
        if (clientPacket) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
        }
    }

    public void onTransfer(ItemStack stack) {
        ((SmartObserverBlock) AllBlocks.SMART_OBSERVER.get()).onFunnelTransfer(this.f_58857_, this.f_58858_, stack);
        this.award(AllAdvancements.FUNNEL);
    }

    private LerpedFloat createChasingFlap() {
        return LerpedFloat.linear().startWithValue(0.25).chase(0.0, 0.05F, LerpedFloat.Chaser.EXP);
    }

    static enum Mode {

        INVALID,
        PAUSED,
        COLLECT,
        PUSHING_TO_BELT,
        TAKING_FROM_BELT,
        EXTRACT
    }
}