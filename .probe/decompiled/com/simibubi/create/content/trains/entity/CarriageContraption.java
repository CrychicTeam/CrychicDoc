package com.simibubi.create.content.trains.entity;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.minecart.TrainCargoManager;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.tuple.Pair;

public class CarriageContraption extends Contraption {

    private Direction assemblyDirection;

    private boolean forwardControls;

    private boolean backwardControls;

    public Couple<Boolean> blazeBurnerConductors;

    public Map<BlockPos, Couple<Boolean>> conductorSeats;

    public ArrivalSoundQueue soundQueue;

    protected MountedStorageManager storageProxy;

    private int bogeys;

    private boolean sidewaysControls;

    private BlockPos secondBogeyPos;

    private List<BlockPos> assembledBlazeBurners;

    public int portalCutoffMin;

    public int portalCutoffMax;

    static final IItemHandlerModifiable fallbackItems = new ItemStackHandler();

    static final IFluidHandler fallbackFluids = new FluidTank(0);

    private Collection<BlockEntity> specialRenderedBEsOutsidePortal = new ArrayList();

    public CarriageContraption() {
        this.conductorSeats = new HashMap();
        this.assembledBlazeBurners = new ArrayList();
        this.blazeBurnerConductors = Couple.create(false, false);
        this.soundQueue = new ArrivalSoundQueue();
        this.portalCutoffMin = Integer.MIN_VALUE;
        this.portalCutoffMax = Integer.MAX_VALUE;
        this.storage = new TrainCargoManager();
    }

    public void setSoundQueueOffset(int offset) {
        this.soundQueue.offset = offset;
    }

    public CarriageContraption(Direction assemblyDirection) {
        this();
        this.assemblyDirection = assemblyDirection;
        this.bogeys = 0;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        if (!this.searchMovedStructure(world, pos, null)) {
            return false;
        } else if (this.blocks.size() <= 1) {
            return false;
        } else if (this.bogeys == 0) {
            return false;
        } else if (this.bogeys > 2) {
            throw new AssemblyException(Lang.translateDirect("train_assembly.too_many_bogeys", this.bogeys));
        } else if (this.sidewaysControls) {
            throw new AssemblyException(Lang.translateDirect("train_assembly.sideways_controls"));
        } else {
            for (BlockPos blazePos : this.assembledBlazeBurners) {
                for (Direction direction : Iterate.directionsInAxis(this.assemblyDirection.getAxis())) {
                    if (this.inControl(blazePos, direction)) {
                        this.blazeBurnerConductors.set(direction != this.assemblyDirection, true);
                    }
                }
            }
            for (BlockPos seatPos : this.getSeats()) {
                for (Direction directionx : Iterate.directionsInAxis(this.assemblyDirection.getAxis())) {
                    if (this.inControl(seatPos, directionx)) {
                        ((Couple) this.conductorSeats.computeIfAbsent(seatPos, p -> Couple.create(false, false))).set(directionx != this.assemblyDirection, true);
                    }
                }
            }
            return true;
        }
    }

    public boolean inControl(BlockPos pos, Direction direction) {
        BlockPos controlsPos = pos.relative(direction);
        if (!this.blocks.containsKey(controlsPos)) {
            return false;
        } else {
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) this.blocks.get(controlsPos);
            return !AllBlocks.TRAIN_CONTROLS.has(info.state()) ? false : info.state().m_61143_(ControlsBlock.f_54117_) == direction.getOpposite();
        }
    }

    public void swapStorageAfterAssembly(CarriageContraptionEntity cce) {
        Carriage carriage = cce.getCarriage();
        if (carriage.storage == null) {
            carriage.storage = (TrainCargoManager) this.storage;
            this.storage = new MountedStorageManager();
        }
        this.storageProxy = carriage.storage;
    }

    public void returnStorageForDisassembly(MountedStorageManager storage) {
        this.storage = storage;
    }

    @Override
    protected boolean isAnchoringBlockAt(BlockPos pos) {
        return false;
    }

    @Override
    protected Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (ArrivalSoundQueue.isPlayable(blockState)) {
            int anchorCoord = VecHelper.getCoordinate(this.anchor, this.assemblyDirection.getAxis());
            int posCoord = VecHelper.getCoordinate(pos, this.assemblyDirection.getAxis());
            this.soundQueue.add((posCoord - anchorCoord) * this.assemblyDirection.getAxisDirection().getStep(), this.toLocalPos(pos));
        }
        if (blockState.m_60734_() instanceof AbstractBogeyBlock<?> bogey) {
            boolean captureBE = bogey.captureBlockEntityForTrain();
            this.bogeys++;
            if (this.bogeys == 2) {
                this.secondBogeyPos = pos;
            }
            return Pair.of(new StructureTemplate.StructureBlockInfo(pos, blockState, captureBE ? this.getBlockEntityNBT(world, pos) : null), captureBE ? world.getBlockEntity(pos) : null);
        } else {
            if (AllBlocks.BLAZE_BURNER.has(blockState) && blockState.m_61143_(BlazeBurnerBlock.HEAT_LEVEL) != BlazeBurnerBlock.HeatLevel.NONE) {
                this.assembledBlazeBurners.add(this.toLocalPos(pos));
            }
            if (AllBlocks.TRAIN_CONTROLS.has(blockState)) {
                Direction facing = (Direction) blockState.m_61143_(ControlsBlock.f_54117_);
                if (facing.getAxis() != this.assemblyDirection.getAxis()) {
                    this.sidewaysControls = true;
                } else {
                    boolean forwards = facing == this.assemblyDirection;
                    if (forwards) {
                        this.forwardControls = true;
                    } else {
                        this.backwardControls = true;
                    }
                }
            }
            return super.capture(world, pos);
        }
    }

    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        NBTHelper.writeEnum(tag, "AssemblyDirection", this.getAssemblyDirection());
        tag.putBoolean("FrontControls", this.forwardControls);
        tag.putBoolean("BackControls", this.backwardControls);
        tag.putBoolean("FrontBlazeConductor", this.blazeBurnerConductors.getFirst());
        tag.putBoolean("BackBlazeConductor", this.blazeBurnerConductors.getSecond());
        ListTag list = NBTHelper.writeCompoundList(this.conductorSeats.entrySet(), e -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("Pos", NbtUtils.writeBlockPos((BlockPos) e.getKey()));
            compoundTag.putBoolean("Forward", (Boolean) ((Couple) e.getValue()).getFirst());
            compoundTag.putBoolean("Backward", (Boolean) ((Couple) e.getValue()).getSecond());
            return compoundTag;
        });
        tag.put("ConductorSeats", list);
        this.soundQueue.serialize(tag);
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag nbt, boolean spawnData) {
        this.assemblyDirection = NBTHelper.readEnum(nbt, "AssemblyDirection", Direction.class);
        this.forwardControls = nbt.getBoolean("FrontControls");
        this.backwardControls = nbt.getBoolean("BackControls");
        this.blazeBurnerConductors = Couple.create(nbt.getBoolean("FrontBlazeConductor"), nbt.getBoolean("BackBlazeConductor"));
        this.conductorSeats.clear();
        NBTHelper.iterateCompoundList(nbt.getList("ConductorSeats", 10), c -> this.conductorSeats.put(NbtUtils.readBlockPos(c.getCompound("Pos")), Couple.create(c.getBoolean("Forward"), c.getBoolean("Backward"))));
        this.soundQueue.deserialize(nbt);
        super.readNBT(world, nbt, spawnData);
    }

    @Override
    public boolean canBeStabilized(Direction facing, BlockPos localPos) {
        return false;
    }

    @Override
    protected MountedStorageManager getStorageForSpawnPacket() {
        return this.storageProxy;
    }

    @Override
    public ContraptionType getType() {
        return ContraptionType.CARRIAGE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ContraptionLighter<?> makeLighter() {
        return new NonStationaryLighter<>(this);
    }

    public Direction getAssemblyDirection() {
        return this.assemblyDirection;
    }

    public boolean hasForwardControls() {
        return this.forwardControls;
    }

    public boolean hasBackwardControls() {
        return this.backwardControls;
    }

    public BlockPos getSecondBogeyPos() {
        return this.secondBogeyPos;
    }

    @Override
    public Collection<StructureTemplate.StructureBlockInfo> getRenderedBlocks() {
        if (this.notInPortal()) {
            return super.getRenderedBlocks();
        } else {
            this.specialRenderedBEsOutsidePortal = new ArrayList();
            this.specialRenderedBlockEntities.stream().filter(be -> !this.isHiddenInPortal(be.getBlockPos())).forEach(this.specialRenderedBEsOutsidePortal::add);
            Collection<StructureTemplate.StructureBlockInfo> values = new ArrayList();
            for (Entry<BlockPos, StructureTemplate.StructureBlockInfo> entry : this.blocks.entrySet()) {
                BlockPos pos = (BlockPos) entry.getKey();
                if (this.withinVisible(pos)) {
                    values.add((StructureTemplate.StructureBlockInfo) entry.getValue());
                } else if (this.atSeam(pos)) {
                    values.add(new StructureTemplate.StructureBlockInfo(pos, Blocks.PURPLE_STAINED_GLASS.defaultBlockState(), null));
                }
            }
            return values;
        }
    }

    @Override
    public Collection<BlockEntity> getSpecialRenderedBEs() {
        return this.notInPortal() ? super.getSpecialRenderedBEs() : this.specialRenderedBEsOutsidePortal;
    }

    @Override
    public Optional<List<AABB>> getSimplifiedEntityColliders() {
        return this.notInPortal() ? super.getSimplifiedEntityColliders() : Optional.empty();
    }

    @Override
    public boolean isHiddenInPortal(BlockPos localPos) {
        return this.notInPortal() ? super.isHiddenInPortal(localPos) : !this.withinVisible(localPos) || this.atSeam(localPos);
    }

    public boolean notInPortal() {
        return this.portalCutoffMin == Integer.MIN_VALUE && this.portalCutoffMax == Integer.MAX_VALUE;
    }

    public boolean atSeam(BlockPos localPos) {
        Direction facing = this.assemblyDirection;
        Direction.Axis axis = facing.getClockWise().getAxis();
        int coord = axis.choose(localPos.m_123343_(), localPos.m_123342_(), localPos.m_123341_()) * -facing.getAxisDirection().getStep();
        return coord == this.portalCutoffMin || coord == this.portalCutoffMax;
    }

    public boolean withinVisible(BlockPos localPos) {
        Direction facing = this.assemblyDirection;
        Direction.Axis axis = facing.getClockWise().getAxis();
        int coord = axis.choose(localPos.m_123343_(), localPos.m_123342_(), localPos.m_123341_()) * -facing.getAxisDirection().getStep();
        return coord > this.portalCutoffMin && coord < this.portalCutoffMax;
    }

    @Override
    public IItemHandlerModifiable getSharedInventory() {
        return this.storageProxy == null ? fallbackItems : this.storageProxy.getItems();
    }

    @Override
    public IFluidHandler getSharedFluidTanks() {
        return this.storageProxy == null ? fallbackFluids : this.storageProxy.getFluids();
    }

    @Override
    public void handleContraptionFluidPacket(BlockPos localPos, FluidStack containedFluid) {
        this.storage.updateContainedFluid(localPos, containedFluid);
    }

    @Override
    public void tickStorage(AbstractContraptionEntity entity) {
        if (entity.m_9236_().isClientSide) {
            this.storage.entityTick(entity);
        } else if (this.storageProxy != null) {
            this.storageProxy.entityTick(entity);
        }
    }
}