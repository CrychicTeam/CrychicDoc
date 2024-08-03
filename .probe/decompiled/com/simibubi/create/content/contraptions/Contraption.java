package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovement;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterMovementBehaviour;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsBlock;
import com.simibubi.create.content.contraptions.bearing.MechanicalBearingBlock;
import com.simibubi.create.content.contraptions.bearing.StabilizedContraption;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlock;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.chassis.AbstractChassisBlock;
import com.simibubi.create.content.contraptions.chassis.ChassisBlockEntity;
import com.simibubi.create.content.contraptions.chassis.StickerBlock;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonHeadBlock;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlock;
import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.EmptyLighter;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.content.kinetics.steamEngine.PoweredShaftBlockEntity;
import com.simibubi.create.content.logistics.crate.CreativeCrateBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.BBHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.ICoordinate;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.NBTProcessors;
import com.simibubi.create.foundation.utility.UniqueLinkedList;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.GameData;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Contraption {

    public Optional<List<AABB>> simplifiedEntityColliders;

    public AbstractContraptionEntity entity;

    public AABB bounds;

    public BlockPos anchor;

    public boolean stalled;

    public boolean hasUniversalCreativeCrate;

    public boolean disassembled;

    protected Map<BlockPos, StructureTemplate.StructureBlockInfo> blocks = new HashMap();

    protected List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> actors;

    protected Map<BlockPos, MovingInteractionBehaviour> interactors;

    protected List<ItemStack> disabledActors;

    protected List<AABB> superglue;

    protected List<BlockPos> seats = new ArrayList();

    protected Map<UUID, Integer> seatMapping;

    protected Map<UUID, BlockFace> stabilizedSubContraptions;

    protected MountedStorageManager storage;

    private Set<SuperGlueEntity> glueToRemove;

    private Map<BlockPos, Entity> initialPassengers;

    private List<BlockFace> pendingSubContraptions;

    private CompletableFuture<Void> simplifiedEntityColliderProvider;

    public Map<BlockPos, ModelData> modelData;

    public Map<BlockPos, BlockEntity> presentBlockEntities;

    public List<BlockEntity> maybeInstancedBlockEntities;

    public List<BlockEntity> specialRenderedBlockEntities;

    protected ContraptionWorld world;

    public boolean deferInvalidate;

    public Contraption() {
        this.actors = new ArrayList();
        this.disabledActors = new ArrayList();
        this.modelData = new HashMap();
        this.interactors = new HashMap();
        this.superglue = new ArrayList();
        this.seatMapping = new HashMap();
        this.glueToRemove = new HashSet();
        this.initialPassengers = new HashMap();
        this.presentBlockEntities = new HashMap();
        this.maybeInstancedBlockEntities = new ArrayList();
        this.specialRenderedBlockEntities = new ArrayList();
        this.pendingSubContraptions = new ArrayList();
        this.stabilizedSubContraptions = new HashMap();
        this.simplifiedEntityColliders = Optional.empty();
        this.storage = new MountedStorageManager();
    }

    public ContraptionWorld getContraptionWorld() {
        if (this.world == null) {
            this.world = new ContraptionWorld(this.entity.m_9236_(), this);
        }
        return this.world;
    }

    public abstract boolean assemble(Level var1, BlockPos var2) throws AssemblyException;

    public abstract boolean canBeStabilized(Direction var1, BlockPos var2);

    public abstract ContraptionType getType();

    protected boolean customBlockPlacement(LevelAccessor world, BlockPos pos, BlockState state) {
        return false;
    }

    protected boolean customBlockRemoval(LevelAccessor world, BlockPos pos, BlockState state) {
        return false;
    }

    protected boolean addToInitialFrontier(Level world, BlockPos pos, Direction forcedDirection, Queue<BlockPos> frontier) throws AssemblyException {
        return true;
    }

    public static Contraption fromNBT(Level world, CompoundTag nbt, boolean spawnData) {
        String type = nbt.getString("Type");
        Contraption contraption = ContraptionType.fromType(type);
        contraption.readNBT(world, nbt, spawnData);
        contraption.world = new ContraptionWorld(world, contraption);
        contraption.gatherBBsOffThread();
        return contraption;
    }

    public boolean searchMovedStructure(Level world, BlockPos pos, @Nullable Direction forcedDirection) throws AssemblyException {
        this.initialPassengers.clear();
        Queue<BlockPos> frontier = new UniqueLinkedList<BlockPos>();
        Set<BlockPos> visited = new HashSet();
        this.anchor = pos;
        if (this.bounds == null) {
            this.bounds = new AABB(BlockPos.ZERO);
        }
        if (!BlockMovementChecks.isBrittle(world.getBlockState(pos))) {
            frontier.add(pos);
        }
        if (!this.addToInitialFrontier(world, pos, forcedDirection, frontier)) {
            return false;
        } else {
            for (int limit = 100000; limit > 0; limit--) {
                if (frontier.isEmpty()) {
                    return true;
                }
                if (!this.moveBlock(world, forcedDirection, frontier, visited)) {
                    return false;
                }
            }
            throw AssemblyException.structureTooLarge();
        }
    }

    public void onEntityCreated(AbstractContraptionEntity entity) {
        this.entity = entity;
        for (BlockFace blockFace : this.pendingSubContraptions) {
            Direction face = blockFace.getFace();
            StabilizedContraption subContraption = new StabilizedContraption(face);
            Level world = entity.m_9236_();
            BlockPos pos = blockFace.getPos();
            try {
                if (!subContraption.assemble(world, pos)) {
                    continue;
                }
            } catch (AssemblyException var10) {
                continue;
            }
            subContraption.removeBlocksFromWorld(world, BlockPos.ZERO);
            OrientedContraptionEntity movedContraption = OrientedContraptionEntity.create(world, subContraption, face);
            BlockPos anchor = blockFace.getConnectedPos();
            movedContraption.m_6034_((double) ((float) anchor.m_123341_() + 0.5F), (double) anchor.m_123342_(), (double) ((float) anchor.m_123343_() + 0.5F));
            world.m_7967_(movedContraption);
            this.stabilizedSubContraptions.put(movedContraption.m_20148_(), new BlockFace(this.toLocalPos(pos), face));
        }
        this.storage.createHandlers();
        this.gatherBBsOffThread();
    }

    public void onEntityRemoved(AbstractContraptionEntity entity) {
        if (this.simplifiedEntityColliderProvider != null) {
            this.simplifiedEntityColliderProvider.cancel(false);
            this.simplifiedEntityColliderProvider = null;
        }
    }

    public void onEntityInitialize(Level world, AbstractContraptionEntity contraptionEntity) {
        if (!world.isClientSide) {
            for (OrientedContraptionEntity orientedCE : world.m_45976_(OrientedContraptionEntity.class, contraptionEntity.m_20191_().inflate(1.0))) {
                if (this.stabilizedSubContraptions.containsKey(orientedCE.m_20148_())) {
                    orientedCE.m_20329_(contraptionEntity);
                }
            }
            for (BlockPos seatPos : this.getSeats()) {
                Entity passenger = (Entity) this.initialPassengers.get(seatPos);
                if (passenger != null) {
                    int seatIndex = this.getSeats().indexOf(seatPos);
                    if (seatIndex != -1) {
                        contraptionEntity.addSittingPassenger(passenger, seatIndex);
                    }
                }
            }
        }
    }

    protected boolean moveBlock(Level world, @Nullable Direction forcedDirection, Queue<BlockPos> frontier, Set<BlockPos> visited) throws AssemblyException {
        BlockPos pos = (BlockPos) frontier.poll();
        if (pos == null) {
            return false;
        } else {
            visited.add(pos);
            if (world.m_151570_(pos)) {
                return true;
            } else if (!world.isLoaded(pos)) {
                throw AssemblyException.unloadedChunk(pos);
            } else if (this.isAnchoringBlockAt(pos)) {
                return true;
            } else {
                BlockState state = world.getBlockState(pos);
                if (!BlockMovementChecks.isMovementNecessary(state, world, pos)) {
                    return true;
                } else if (!this.movementAllowed(state, world, pos)) {
                    throw AssemblyException.unmovableBlock(pos, state);
                } else if (state.m_60734_() instanceof AbstractChassisBlock && !this.moveChassis(world, pos, forcedDirection, frontier, visited)) {
                    return false;
                } else {
                    if (AllBlocks.BELT.has(state)) {
                        this.moveBelt(pos, frontier, visited, state);
                    }
                    if (AllBlocks.WINDMILL_BEARING.has(state) && world.getBlockEntity(pos) instanceof WindmillBearingBlockEntity wbbe) {
                        wbbe.disassembleForMovement();
                    }
                    if (AllBlocks.GANTRY_CARRIAGE.has(state)) {
                        this.moveGantryPinion(world, pos, frontier, visited, state);
                    }
                    if (AllBlocks.GANTRY_SHAFT.has(state)) {
                        this.moveGantryShaft(world, pos, frontier, visited, state);
                    }
                    if (AllBlocks.STICKER.has(state) && (Boolean) state.m_61143_(StickerBlock.EXTENDED)) {
                        Direction offset = (Direction) state.m_61143_(StickerBlock.f_52588_);
                        BlockPos attached = pos.relative(offset);
                        if (!visited.contains(attached) && !BlockMovementChecks.isNotSupportive(world.getBlockState(attached), offset.getOpposite())) {
                            frontier.add(attached);
                        }
                    }
                    if (state.m_61138_(ChestBlock.TYPE) && state.m_61138_(ChestBlock.FACING) && state.m_61143_(ChestBlock.TYPE) != ChestType.SINGLE) {
                        Direction offset = ChestBlock.getConnectedDirection(state);
                        BlockPos attached = pos.relative(offset);
                        if (!visited.contains(attached)) {
                            frontier.add(attached);
                        }
                    }
                    if (state.m_60734_() instanceof AbstractBogeyBlock<?> bogey) {
                        for (Direction d : bogey.getStickySurfaces(world, pos, state)) {
                            if (!visited.contains(pos.relative(d))) {
                                frontier.add(pos.relative(d));
                            }
                        }
                    }
                    if (AllBlocks.MECHANICAL_BEARING.has(state)) {
                        this.moveBearing(pos, frontier, visited, state);
                    }
                    if (AllBlocks.WINDMILL_BEARING.has(state)) {
                        this.moveWindmillBearing(pos, frontier, visited, state);
                    }
                    if (state.m_60734_() instanceof SeatBlock) {
                        this.moveSeat(world, pos);
                    }
                    if (state.m_60734_() instanceof PulleyBlock) {
                        this.movePulley(world, pos, frontier, visited);
                    }
                    if (state.m_60734_() instanceof MechanicalPistonBlock && !this.moveMechanicalPiston(world, pos, frontier, visited, state)) {
                        return false;
                    } else {
                        if (MechanicalPistonBlock.isExtensionPole(state)) {
                            this.movePistonPole(world, pos, frontier, visited, state);
                        }
                        if (MechanicalPistonBlock.isPistonHead(state)) {
                            this.movePistonHead(world, pos, frontier, visited, state);
                        }
                        BlockPos posDown = pos.below();
                        BlockState stateBelow = world.getBlockState(posDown);
                        if (!visited.contains(posDown) && AllBlocks.CART_ASSEMBLER.has(stateBelow)) {
                            frontier.add(posDown);
                        }
                        for (Direction offset : Iterate.directions) {
                            BlockPos offsetPos = pos.relative(offset);
                            BlockState blockState = world.getBlockState(offsetPos);
                            if (!this.isAnchoringBlockAt(offsetPos)) {
                                if (!this.movementAllowed(blockState, world, offsetPos)) {
                                    if (offset == forcedDirection) {
                                        throw AssemblyException.unmovableBlock(pos, state);
                                    }
                                } else {
                                    boolean wasVisited = visited.contains(offsetPos);
                                    boolean faceHasGlue = SuperGlueEntity.isGlued(world, pos, offset, this.glueToRemove);
                                    boolean blockAttachedTowardsFace = BlockMovementChecks.isBlockAttachedTowards(blockState, world, offsetPos, offset.getOpposite());
                                    boolean brittle = BlockMovementChecks.isBrittle(blockState);
                                    boolean canStick = !brittle && state.canStickTo(blockState) && blockState.canStickTo(state);
                                    if (canStick) {
                                        if (state.m_60811_() == PushReaction.PUSH_ONLY || blockState.m_60811_() == PushReaction.PUSH_ONLY) {
                                            canStick = false;
                                        }
                                        if (BlockMovementChecks.isNotSupportive(state, offset)) {
                                            canStick = false;
                                        }
                                        if (BlockMovementChecks.isNotSupportive(blockState, offset.getOpposite())) {
                                            canStick = false;
                                        }
                                    }
                                    if (!wasVisited && (canStick || blockAttachedTowardsFace || faceHasGlue || offset == forcedDirection && !BlockMovementChecks.isNotSupportive(state, forcedDirection))) {
                                        frontier.add(offsetPos);
                                    }
                                }
                            }
                        }
                        this.addBlock(pos, this.capture(world, pos));
                        if (this.blocks.size() <= AllConfigs.server().kinetics.maxBlocksMoved.get()) {
                            return true;
                        } else {
                            throw AssemblyException.structureTooLarge();
                        }
                    }
                }
            }
        }
    }

    protected void movePistonHead(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        Direction direction = (Direction) state.m_61143_(MechanicalPistonHeadBlock.f_52588_);
        BlockPos offset = pos.relative(direction.getOpposite());
        if (!visited.contains(offset)) {
            BlockState blockState = world.getBlockState(offset);
            if (MechanicalPistonBlock.isExtensionPole(blockState) && ((Direction) blockState.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis() == direction.getAxis()) {
                frontier.add(offset);
            }
            if (blockState.m_60734_() instanceof MechanicalPistonBlock) {
                Direction pistonFacing = (Direction) blockState.m_61143_(MechanicalPistonBlock.FACING);
                if (pistonFacing == direction && blockState.m_61143_(MechanicalPistonBlock.STATE) == MechanicalPistonBlock.PistonState.EXTENDED) {
                    frontier.add(offset);
                }
            }
        }
        if (state.m_61143_(MechanicalPistonHeadBlock.TYPE) == PistonType.STICKY) {
            BlockPos attached = pos.relative(direction);
            if (!visited.contains(attached)) {
                frontier.add(attached);
            }
        }
    }

    protected void movePistonPole(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        for (Direction d : Iterate.directionsInAxis(((Direction) state.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis())) {
            BlockPos offset = pos.relative(d);
            if (!visited.contains(offset)) {
                BlockState blockState = world.getBlockState(offset);
                if (MechanicalPistonBlock.isExtensionPole(blockState) && ((Direction) blockState.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis() == d.getAxis()) {
                    frontier.add(offset);
                }
                if (MechanicalPistonBlock.isPistonHead(blockState) && ((Direction) blockState.m_61143_(MechanicalPistonHeadBlock.f_52588_)).getAxis() == d.getAxis()) {
                    frontier.add(offset);
                }
                if (blockState.m_60734_() instanceof MechanicalPistonBlock) {
                    Direction pistonFacing = (Direction) blockState.m_61143_(MechanicalPistonBlock.FACING);
                    if (pistonFacing == d || pistonFacing == d.getOpposite() && blockState.m_61143_(MechanicalPistonBlock.STATE) == MechanicalPistonBlock.PistonState.EXTENDED) {
                        frontier.add(offset);
                    }
                }
            }
        }
    }

    protected void moveGantryPinion(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        BlockPos offset = pos.relative((Direction) state.m_61143_(GantryCarriageBlock.FACING));
        if (!visited.contains(offset)) {
            frontier.add(offset);
        }
        Direction.Axis rotationAxis = ((IRotate) state.m_60734_()).getRotationAxis(state);
        for (Direction d : Iterate.directionsInAxis(rotationAxis)) {
            offset = pos.relative(d);
            BlockState offsetState = world.getBlockState(offset);
            if (AllBlocks.GANTRY_SHAFT.has(offsetState) && ((Direction) offsetState.m_61143_(GantryShaftBlock.FACING)).getAxis() == d.getAxis() && !visited.contains(offset)) {
                frontier.add(offset);
            }
        }
    }

    protected void moveGantryShaft(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        for (Direction d : Iterate.directions) {
            BlockPos offset = pos.relative(d);
            if (!visited.contains(offset)) {
                BlockState offsetState = world.getBlockState(offset);
                Direction facing = (Direction) state.m_61143_(GantryShaftBlock.FACING);
                if (d.getAxis() == facing.getAxis() && AllBlocks.GANTRY_SHAFT.has(offsetState) && offsetState.m_61143_(GantryShaftBlock.FACING) == facing) {
                    frontier.add(offset);
                } else if (AllBlocks.GANTRY_CARRIAGE.has(offsetState) && offsetState.m_61143_(GantryCarriageBlock.FACING) == d) {
                    frontier.add(offset);
                }
            }
        }
    }

    private void moveWindmillBearing(BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        Direction facing = (Direction) state.m_61143_(WindmillBearingBlock.FACING);
        BlockPos offset = pos.relative(facing);
        if (!visited.contains(offset)) {
            frontier.add(offset);
        }
    }

    private void moveBearing(BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        Direction facing = (Direction) state.m_61143_(MechanicalBearingBlock.FACING);
        if (!this.canBeStabilized(facing, pos.subtract(this.anchor))) {
            BlockPos offset = pos.relative(facing);
            if (!visited.contains(offset)) {
                frontier.add(offset);
            }
        } else {
            this.pendingSubContraptions.add(new BlockFace(pos, facing));
        }
    }

    private void moveBelt(BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) {
        BlockPos nextPos = BeltBlock.nextSegmentPosition(state, pos, true);
        BlockPos prevPos = BeltBlock.nextSegmentPosition(state, pos, false);
        if (nextPos != null && !visited.contains(nextPos)) {
            frontier.add(nextPos);
        }
        if (prevPos != null && !visited.contains(prevPos)) {
            frontier.add(prevPos);
        }
    }

    private void moveSeat(Level world, BlockPos pos) {
        BlockPos local = this.toLocalPos(pos);
        this.getSeats().add(local);
        List<SeatEntity> seatsEntities = world.m_45976_(SeatEntity.class, new AABB(pos));
        if (!seatsEntities.isEmpty()) {
            SeatEntity seat = (SeatEntity) seatsEntities.get(0);
            List<Entity> passengers = seat.m_20197_();
            if (!passengers.isEmpty()) {
                this.initialPassengers.put(local, (Entity) passengers.get(0));
            }
        }
    }

    private void movePulley(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited) {
        int limit = AllConfigs.server().kinetics.maxRopeLength.get();
        BlockPos ropePos = pos;
        while (limit-- >= 0) {
            ropePos = ropePos.below();
            if (!world.isLoaded(ropePos)) {
                break;
            }
            BlockState ropeState = world.getBlockState(ropePos);
            Block block = ropeState.m_60734_();
            if (!(block instanceof PulleyBlock.RopeBlock) && !(block instanceof PulleyBlock.MagnetBlock)) {
                if (!visited.contains(ropePos)) {
                    frontier.add(ropePos);
                }
                break;
            }
            this.addBlock(ropePos, this.capture(world, ropePos));
        }
    }

    private boolean moveMechanicalPiston(Level world, BlockPos pos, Queue<BlockPos> frontier, Set<BlockPos> visited, BlockState state) throws AssemblyException {
        Direction direction = (Direction) state.m_61143_(MechanicalPistonBlock.FACING);
        MechanicalPistonBlock.PistonState pistonState = (MechanicalPistonBlock.PistonState) state.m_61143_(MechanicalPistonBlock.STATE);
        if (pistonState == MechanicalPistonBlock.PistonState.MOVING) {
            return false;
        } else {
            BlockPos offset = pos.relative(direction.getOpposite());
            if (!visited.contains(offset)) {
                BlockState poleState = world.getBlockState(offset);
                if (AllBlocks.PISTON_EXTENSION_POLE.has(poleState) && ((Direction) poleState.m_61143_(PistonExtensionPoleBlock.f_52588_)).getAxis() == direction.getAxis()) {
                    frontier.add(offset);
                }
            }
            if (pistonState == MechanicalPistonBlock.PistonState.EXTENDED || MechanicalPistonBlock.isStickyPiston(state)) {
                offset = pos.relative(direction);
                if (!visited.contains(offset)) {
                    frontier.add(offset);
                }
            }
            return true;
        }
    }

    private boolean moveChassis(Level world, BlockPos pos, Direction movementDirection, Queue<BlockPos> frontier, Set<BlockPos> visited) {
        if (!(world.getBlockEntity(pos) instanceof ChassisBlockEntity chassis)) {
            return false;
        } else {
            chassis.addAttachedChasses(frontier, visited);
            List<BlockPos> includedBlockPositions = chassis.getIncludedBlockPositions(movementDirection, false);
            if (includedBlockPositions == null) {
                return false;
            } else {
                for (BlockPos blockPos : includedBlockPositions) {
                    if (!visited.contains(blockPos)) {
                        frontier.add(blockPos);
                    }
                }
                return true;
            }
        }
    }

    protected Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
        BlockState blockstate = world.getBlockState(pos);
        if (AllBlocks.REDSTONE_CONTACT.has(blockstate)) {
            blockstate = (BlockState) blockstate.m_61124_(RedstoneContactBlock.POWERED, true);
        }
        if (AllBlocks.POWERED_SHAFT.has(blockstate)) {
            blockstate = BlockHelper.copyProperties(blockstate, AllBlocks.SHAFT.getDefaultState());
        }
        if (blockstate.m_60734_() instanceof ControlsBlock && this.getType() == ContraptionType.CARRIAGE) {
            blockstate = (BlockState) blockstate.m_61124_(ControlsBlock.OPEN, true);
        }
        if (blockstate.m_61138_(SlidingDoorBlock.VISIBLE)) {
            blockstate = (BlockState) blockstate.m_61124_(SlidingDoorBlock.VISIBLE, false);
        }
        if (blockstate.m_60734_() instanceof ButtonBlock) {
            blockstate = (BlockState) blockstate.m_61124_(ButtonBlock.POWERED, false);
            world.m_186460_(pos, blockstate.m_60734_(), -1);
        }
        if (blockstate.m_60734_() instanceof PressurePlateBlock) {
            blockstate = (BlockState) blockstate.m_61124_(PressurePlateBlock.POWERED, false);
            world.m_186460_(pos, blockstate.m_60734_(), -1);
        }
        CompoundTag compoundnbt = this.getBlockEntityNBT(world, pos);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof PoweredShaftBlockEntity) {
            blockEntity = AllBlockEntityTypes.BRACKETED_KINETIC.create(pos, blockstate);
        }
        return Pair.of(new StructureTemplate.StructureBlockInfo(pos, blockstate, compoundnbt), blockEntity);
    }

    protected void addBlock(BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair) {
        StructureTemplate.StructureBlockInfo captured = (StructureTemplate.StructureBlockInfo) pair.getKey();
        BlockPos localPos = pos.subtract(this.anchor);
        StructureTemplate.StructureBlockInfo structureBlockInfo = new StructureTemplate.StructureBlockInfo(localPos, captured.state(), captured.nbt());
        if (this.blocks.put(localPos, structureBlockInfo) == null) {
            this.bounds = this.bounds.minmax(new AABB(localPos));
            BlockEntity be = (BlockEntity) pair.getValue();
            this.storage.addBlock(localPos, be);
            if (AllMovementBehaviours.getBehaviour(captured.state()) != null) {
                this.actors.add(MutablePair.of(structureBlockInfo, null));
            }
            MovingInteractionBehaviour interactionBehaviour = AllInteractionBehaviours.getBehaviour(captured.state());
            if (interactionBehaviour != null) {
                this.interactors.put(localPos, interactionBehaviour);
            }
            if (be instanceof CreativeCrateBlockEntity && ((CreativeCrateBlockEntity) be).getBehaviour(FilteringBehaviour.TYPE).getFilter().isEmpty()) {
                this.hasUniversalCreativeCrate = true;
            }
        }
    }

    @Nullable
    protected CompoundTag getBlockEntityNBT(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) {
            return null;
        } else {
            CompoundTag nbt = blockEntity.saveWithFullMetadata();
            nbt.remove("x");
            nbt.remove("y");
            nbt.remove("z");
            if ((blockEntity instanceof FluidTankBlockEntity || blockEntity instanceof ItemVaultBlockEntity) && nbt.contains("Controller")) {
                nbt.put("Controller", NbtUtils.writeBlockPos(this.toLocalPos(NbtUtils.readBlockPos(nbt.getCompound("Controller")))));
            }
            return nbt;
        }
    }

    protected BlockPos toLocalPos(BlockPos globalPos) {
        return globalPos.subtract(this.anchor);
    }

    protected boolean movementAllowed(BlockState state, Level world, BlockPos pos) {
        return BlockMovementChecks.isMovementAllowed(state, world, pos);
    }

    protected boolean isAnchoringBlockAt(BlockPos pos) {
        return pos.equals(this.anchor);
    }

    public void readNBT(Level world, CompoundTag nbt, boolean spawnData) {
        this.blocks.clear();
        this.presentBlockEntities.clear();
        this.specialRenderedBlockEntities.clear();
        Tag blocks = nbt.get("Blocks");
        boolean usePalettedDeserialization = blocks != null && blocks.getId() == 10 && ((CompoundTag) blocks).contains("Palette");
        this.readBlocksCompound(blocks, world, usePalettedDeserialization);
        this.actors.clear();
        nbt.getList("Actors", 10).forEach(c -> {
            CompoundTag comp = (CompoundTag) c;
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) this.blocks.get(NbtUtils.readBlockPos(comp.getCompound("Pos")));
            if (info != null) {
                MovementContext context = MovementContext.readNBT(world, info, comp, this);
                this.getActors().add(MutablePair.of(info, context));
            }
        });
        this.disabledActors = NBTHelper.readItemList(nbt.getList("DisabledActors", 10));
        for (ItemStack stack : this.disabledActors) {
            this.setActorsActive(stack, false);
        }
        this.superglue.clear();
        NBTHelper.iterateCompoundList(nbt.getList("Superglue", 10), c -> this.superglue.add(SuperGlueEntity.readBoundingBox(c)));
        this.seats.clear();
        NBTHelper.iterateCompoundList(nbt.getList("Seats", 10), c -> this.seats.add(NbtUtils.readBlockPos(c)));
        this.seatMapping.clear();
        NBTHelper.iterateCompoundList(nbt.getList("Passengers", 10), c -> this.seatMapping.put(NbtUtils.loadUUID(NBTHelper.getINBT(c, "Id")), c.getInt("Seat")));
        this.stabilizedSubContraptions.clear();
        NBTHelper.iterateCompoundList(nbt.getList("SubContraptions", 10), c -> this.stabilizedSubContraptions.put(c.getUUID("Id"), BlockFace.fromNBT(c.getCompound("Location"))));
        this.interactors.clear();
        NBTHelper.iterateCompoundList(nbt.getList("Interactors", 10), c -> {
            BlockPos pos = NbtUtils.readBlockPos(c.getCompound("Pos"));
            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo) this.getBlocks().get(pos);
            if (structureBlockInfo != null) {
                MovingInteractionBehaviour behaviour = AllInteractionBehaviours.getBehaviour(structureBlockInfo.state());
                if (behaviour != null) {
                    this.interactors.put(pos, behaviour);
                }
            }
        });
        this.storage.read(nbt, this.presentBlockEntities, spawnData);
        if (nbt.contains("BoundsFront")) {
            this.bounds = NBTHelper.readAABB(nbt.getList("BoundsFront", 5));
        }
        this.stalled = nbt.getBoolean("Stalled");
        this.hasUniversalCreativeCrate = nbt.getBoolean("BottomlessSupply");
        this.anchor = NbtUtils.readBlockPos(nbt.getCompound("Anchor"));
    }

    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Type", this.getType().id);
        CompoundTag blocksNBT = this.writeBlocksCompound();
        ListTag actorsNBT = new ListTag();
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> actor : this.getActors()) {
            MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) actor.left).state());
            if (behaviour != null) {
                CompoundTag compound = new CompoundTag();
                compound.put("Pos", NbtUtils.writeBlockPos(((StructureTemplate.StructureBlockInfo) actor.left).pos()));
                behaviour.writeExtraData((MovementContext) actor.right);
                ((MovementContext) actor.right).writeToNBT(compound);
                actorsNBT.add(compound);
            }
        }
        ListTag disabledActorsNBT = NBTHelper.writeItemList(this.disabledActors);
        ListTag superglueNBT = new ListTag();
        if (!spawnPacket) {
            for (AABB glueEntry : this.superglue) {
                CompoundTag c = new CompoundTag();
                SuperGlueEntity.writeBoundingBox(c, glueEntry);
                superglueNBT.add(c);
            }
        }
        (spawnPacket ? this.getStorageForSpawnPacket() : this.storage).write(nbt, spawnPacket);
        ListTag interactorNBT = new ListTag();
        for (BlockPos pos : this.interactors.keySet()) {
            CompoundTag c = new CompoundTag();
            c.put("Pos", NbtUtils.writeBlockPos(pos));
            interactorNBT.add(c);
        }
        nbt.put("Seats", NBTHelper.writeCompoundList(this.getSeats(), NbtUtils::m_129224_));
        nbt.put("Passengers", NBTHelper.writeCompoundList(this.getSeatMapping().entrySet(), e -> {
            CompoundTag tag = new CompoundTag();
            tag.put("Id", NbtUtils.createUUID((UUID) e.getKey()));
            tag.putInt("Seat", (Integer) e.getValue());
            return tag;
        }));
        nbt.put("SubContraptions", NBTHelper.writeCompoundList(this.stabilizedSubContraptions.entrySet(), e -> {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("Id", (UUID) e.getKey());
            tag.put("Location", ((BlockFace) e.getValue()).serializeNBT());
            return tag;
        }));
        nbt.put("Blocks", blocksNBT);
        nbt.put("Actors", actorsNBT);
        nbt.put("DisabledActors", disabledActorsNBT);
        nbt.put("Interactors", interactorNBT);
        nbt.put("Superglue", superglueNBT);
        nbt.put("Anchor", NbtUtils.writeBlockPos(this.anchor));
        nbt.putBoolean("Stalled", this.stalled);
        nbt.putBoolean("BottomlessSupply", this.hasUniversalCreativeCrate);
        if (this.bounds != null) {
            ListTag bb = NBTHelper.writeAABB(this.bounds);
            nbt.put("BoundsFront", bb);
        }
        return nbt;
    }

    protected MountedStorageManager getStorageForSpawnPacket() {
        return this.storage;
    }

    private CompoundTag writeBlocksCompound() {
        CompoundTag compound = new CompoundTag();
        HashMapPalette<BlockState> palette = new HashMapPalette<>(GameData.getBlockStateIDMap(), 16, (ix, s) -> {
            throw new IllegalStateException("Palette Map index exceeded maximum");
        });
        ListTag blockList = new ListTag();
        for (StructureTemplate.StructureBlockInfo block : this.blocks.values()) {
            int id = palette.idFor(block.state());
            CompoundTag c = new CompoundTag();
            c.putLong("Pos", block.pos().asLong());
            c.putInt("State", id);
            if (block.nbt() != null) {
                c.put("Data", block.nbt());
            }
            blockList.add(c);
        }
        ListTag paletteNBT = new ListTag();
        for (int i = 0; i < palette.getSize(); i++) {
            paletteNBT.add(NbtUtils.writeBlockState(palette.values.byId(i)));
        }
        compound.put("Palette", paletteNBT);
        compound.put("BlockList", blockList);
        return compound;
    }

    private void readBlocksCompound(Tag compound, Level world, boolean usePalettedDeserialization) {
        HolderGetter<Block> holderGetter = world.m_246945_(Registries.BLOCK);
        HashMapPalette<BlockState> palette = null;
        ListTag blockList;
        if (usePalettedDeserialization) {
            CompoundTag c = (CompoundTag) compound;
            palette = new HashMapPalette<>(GameData.getBlockStateIDMap(), 16, (ix, s) -> {
                throw new IllegalStateException("Palette Map index exceeded maximum");
            });
            ListTag list = c.getList("Palette", 10);
            palette.values.clear();
            for (int i = 0; i < list.size(); i++) {
                palette.values.add(NbtUtils.readBlockState(holderGetter, list.getCompound(i)));
            }
            blockList = c.getList("BlockList", 10);
        } else {
            blockList = (ListTag) compound;
        }
        HashMapPalette<BlockState> finalPalette = palette;
        blockList.forEach(e -> {
            CompoundTag c = (CompoundTag) e;
            StructureTemplate.StructureBlockInfo info = usePalettedDeserialization ? readStructureBlockInfo(c, finalPalette) : legacyReadStructureBlockInfo(c, holderGetter);
            this.blocks.put(info.pos(), info);
            if (world.isClientSide) {
                CompoundTag tag = info.nbt();
                if (tag != null) {
                    tag.putInt("x", info.pos().m_123341_());
                    tag.putInt("y", info.pos().m_123342_());
                    tag.putInt("z", info.pos().m_123343_());
                    BlockEntity be = BlockEntity.loadStatic(info.pos(), info.state(), tag);
                    if (be != null) {
                        be.setLevel(world);
                        this.modelData.put(info.pos(), be.getModelData());
                        if (be instanceof KineticBlockEntity kbe) {
                            kbe.setSpeed(0.0F);
                        }
                        be.getBlockState();
                        MovementBehaviour movementBehaviour = AllMovementBehaviours.getBehaviour(info.state());
                        if (movementBehaviour == null || !movementBehaviour.hasSpecialInstancedRendering()) {
                            this.maybeInstancedBlockEntities.add(be);
                        }
                        if (movementBehaviour == null || movementBehaviour.renderAsNormalBlockEntity()) {
                            this.presentBlockEntities.put(info.pos(), be);
                            this.specialRenderedBlockEntities.add(be);
                        }
                    }
                }
            }
        });
    }

    private static StructureTemplate.StructureBlockInfo readStructureBlockInfo(CompoundTag blockListEntry, HashMapPalette<BlockState> palette) {
        return new StructureTemplate.StructureBlockInfo(BlockPos.of(blockListEntry.getLong("Pos")), (BlockState) Objects.requireNonNull(palette.valueFor(blockListEntry.getInt("State"))), blockListEntry.contains("Data") ? blockListEntry.getCompound("Data") : null);
    }

    private static StructureTemplate.StructureBlockInfo legacyReadStructureBlockInfo(CompoundTag blockListEntry, HolderGetter<Block> holderGetter) {
        return new StructureTemplate.StructureBlockInfo(NbtUtils.readBlockPos(blockListEntry.getCompound("Pos")), NbtUtils.readBlockState(holderGetter, blockListEntry.getCompound("Block")), blockListEntry.contains("Data") ? blockListEntry.getCompound("Data") : null);
    }

    public void removeBlocksFromWorld(Level world, BlockPos offset) {
        this.storage.removeStorageFromWorld();
        this.glueToRemove.forEach(glue -> {
            this.superglue.add(glue.m_20191_().move(Vec3.atLowerCornerOf(offset.offset(this.anchor)).scale(-1.0)));
            glue.m_146870_();
        });
        List<BoundingBox> minimisedGlue = new ArrayList();
        for (int i = 0; i < this.superglue.size(); i++) {
            minimisedGlue.add(null);
        }
        for (boolean brittles : Iterate.trueAndFalse) {
            Iterator<StructureTemplate.StructureBlockInfo> iterator = this.blocks.values().iterator();
            while (iterator.hasNext()) {
                StructureTemplate.StructureBlockInfo block = (StructureTemplate.StructureBlockInfo) iterator.next();
                if (brittles == BlockMovementChecks.isBrittle(block.state())) {
                    for (int i = 0; i < this.superglue.size(); i++) {
                        AABB aabb = (AABB) this.superglue.get(i);
                        if (aabb != null && aabb.contains((double) block.pos().m_123341_() + 0.5, (double) block.pos().m_123342_() + 0.5, (double) block.pos().m_123343_() + 0.5)) {
                            if (minimisedGlue.get(i) == null) {
                                minimisedGlue.set(i, new BoundingBox(block.pos()));
                            } else {
                                minimisedGlue.set(i, BBHelper.encapsulate((BoundingBox) minimisedGlue.get(i), block.pos()));
                            }
                        }
                    }
                    BlockPos add = block.pos().offset(this.anchor).offset(offset);
                    if (!this.customBlockRemoval(world, add, block.state())) {
                        BlockState oldState = world.getBlockState(add);
                        Block blockIn = oldState.m_60734_();
                        boolean blockMismatch = block.state().m_60734_() != blockIn;
                        blockMismatch &= !AllBlocks.POWERED_SHAFT.is(blockIn) || !AllBlocks.SHAFT.has(block.state());
                        if (blockMismatch) {
                            iterator.remove();
                        }
                        world.removeBlockEntity(add);
                        int flags = 122;
                        if (blockIn instanceof SimpleWaterloggedBlock && oldState.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) oldState.m_61143_(BlockStateProperties.WATERLOGGED)) {
                            world.setBlock(add, Blocks.WATER.defaultBlockState(), flags);
                        } else {
                            world.setBlock(add, Blocks.AIR.defaultBlockState(), flags);
                        }
                    }
                }
            }
        }
        this.superglue.clear();
        for (BoundingBox box : minimisedGlue) {
            if (box != null) {
                AABB bb = new AABB((double) box.minX(), (double) box.minY(), (double) box.minZ(), (double) (box.maxX() + 1), (double) (box.maxY() + 1), (double) (box.maxZ() + 1));
                if (bb.getSize() > 1.01) {
                    this.superglue.add(bb);
                }
            }
        }
        for (StructureTemplate.StructureBlockInfo block : this.blocks.values()) {
            BlockPos add = block.pos().offset(this.anchor).offset(offset);
            int flags = 67;
            world.sendBlockUpdated(add, block.state(), Blocks.AIR.defaultBlockState(), flags);
            ServerLevel serverWorld = (ServerLevel) world;
            PoiTypes.forState(block.state()).ifPresent(poiType -> world.getServer().execute(() -> {
                serverWorld.getPoiManager().add(add, poiType);
                DebugPackets.sendPoiAddedPacket(serverWorld, add);
            }));
            world.markAndNotifyBlock(add, world.getChunkAt(add), block.state(), Blocks.AIR.defaultBlockState(), flags, 512);
            block.state().m_60758_(world, add, flags & -2);
        }
    }

    public void addBlocksToWorld(Level world, StructureTransform transform) {
        if (!this.disassembled) {
            this.disassembled = true;
            for (boolean nonBrittles : Iterate.trueAndFalse) {
                for (StructureTemplate.StructureBlockInfo block : this.blocks.values()) {
                    if (nonBrittles != BlockMovementChecks.isBrittle(block.state())) {
                        BlockPos targetPos = transform.apply(block.pos());
                        BlockState state = transform.apply(block.state());
                        if (!this.customBlockPlacement(world, targetPos, state)) {
                            if (nonBrittles) {
                                for (Direction face : Iterate.directions) {
                                    state = state.m_60728_(face, world.getBlockState(targetPos.relative(face)), world, targetPos, targetPos.relative(face));
                                }
                            }
                            BlockState blockState = world.getBlockState(targetPos);
                            if (blockState.m_60800_(world, targetPos) != -1.0F && (!state.m_60812_(world, targetPos).isEmpty() || blockState.m_60812_(world, targetPos).isEmpty())) {
                                if (state.m_60734_() instanceof SimpleWaterloggedBlock && state.m_61138_(BlockStateProperties.WATERLOGGED)) {
                                    FluidState FluidState = world.getFluidState(targetPos);
                                    state = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, FluidState.getType() == Fluids.WATER);
                                }
                                world.m_46961_(targetPos, true);
                                if (AllBlocks.SHAFT.has(state)) {
                                    state = ShaftBlock.pickCorrectShaftType(state, world, targetPos);
                                }
                                if (state.m_61138_(SlidingDoorBlock.VISIBLE)) {
                                    state = (BlockState) ((BlockState) state.m_61124_(SlidingDoorBlock.VISIBLE, !(Boolean) state.m_61143_(SlidingDoorBlock.f_52727_))).m_61124_(SlidingDoorBlock.f_52729_, false);
                                }
                                if (state.m_60713_(Blocks.SCULK_SHRIEKER)) {
                                    state = Blocks.SCULK_SHRIEKER.defaultBlockState();
                                }
                                world.setBlock(targetPos, state, 67);
                                boolean verticalRotation = transform.rotationAxis == null || transform.rotationAxis.isHorizontal();
                                verticalRotation = verticalRotation && transform.rotation != Rotation.NONE;
                                if (verticalRotation && (state.m_60734_() instanceof PulleyBlock.RopeBlock || state.m_60734_() instanceof PulleyBlock.MagnetBlock || state.m_60734_() instanceof DoorBlock)) {
                                    world.m_46961_(targetPos, true);
                                }
                                BlockEntity blockEntity = world.getBlockEntity(targetPos);
                                CompoundTag tag = block.nbt();
                                if (state.m_60713_(Blocks.SCULK_SENSOR) || state.m_60713_(Blocks.SCULK_SHRIEKER)) {
                                    tag = null;
                                }
                                if (blockEntity != null) {
                                    tag = NBTProcessors.process(blockEntity, tag, false);
                                }
                                if (blockEntity != null && tag != null) {
                                    tag.putInt("x", targetPos.m_123341_());
                                    tag.putInt("y", targetPos.m_123342_());
                                    tag.putInt("z", targetPos.m_123343_());
                                    if (verticalRotation && blockEntity instanceof PulleyBlockEntity) {
                                        tag.remove("Offset");
                                        tag.remove("InitialOffset");
                                    }
                                    if (blockEntity instanceof IMultiBlockEntityContainer && tag.contains("LastKnownPos")) {
                                        tag.put("LastKnownPos", NbtUtils.writeBlockPos(BlockPos.ZERO.below(2147483646)));
                                    }
                                    blockEntity.load(tag);
                                    this.storage.addStorageToWorld(block, blockEntity);
                                }
                                transform.apply(blockEntity);
                            } else {
                                if (targetPos.m_123342_() == world.m_141937_()) {
                                    targetPos = targetPos.above();
                                }
                                world.m_46796_(2001, targetPos, Block.getId(state));
                                Block.dropResources(state, world, targetPos, null);
                            }
                        }
                    }
                }
            }
            for (StructureTemplate.StructureBlockInfo blockx : this.blocks.values()) {
                if (this.shouldUpdateAfterMovement(blockx)) {
                    BlockPos targetPos = transform.apply(blockx.pos());
                    world.markAndNotifyBlock(targetPos, world.getChunkAt(targetPos), blockx.state(), blockx.state(), 67, 512);
                }
            }
            for (AABB box : this.superglue) {
                box = new AABB(transform.apply(new Vec3(box.minX, box.minY, box.minZ)), transform.apply(new Vec3(box.maxX, box.maxY, box.maxZ)));
                if (!world.isClientSide) {
                    world.m_7967_(new SuperGlueEntity(world, box));
                }
            }
            this.storage.clear();
        }
    }

    public void addPassengersToWorld(Level world, StructureTransform transform, List<Entity> seatedEntities) {
        for (Entity seatedEntity : seatedEntities) {
            if (!this.getSeatMapping().isEmpty()) {
                Integer seatIndex = (Integer) this.getSeatMapping().get(seatedEntity.getUUID());
                if (seatIndex != null) {
                    BlockPos seatPos = (BlockPos) this.getSeats().get(seatIndex);
                    seatPos = transform.apply(seatPos);
                    if (world.getBlockState(seatPos).m_60734_() instanceof SeatBlock && !SeatBlock.isSeatOccupied(world, seatPos)) {
                        SeatBlock.sitDown(world, seatPos, seatedEntity);
                    }
                }
            }
        }
    }

    public void startMoving(Level world) {
        this.disabledActors.clear();
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            MovementContext context = new MovementContext(world, (StructureTemplate.StructureBlockInfo) pair.left, this);
            MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.left).state());
            if (behaviour != null) {
                behaviour.startMoving(context);
            }
            pair.setRight(context);
            if (behaviour instanceof ContraptionControlsMovement) {
                this.disableActorOnStart(context);
            }
        }
        for (ItemStack stack : this.disabledActors) {
            this.setActorsActive(stack, false);
        }
    }

    protected void disableActorOnStart(MovementContext context) {
        if (ContraptionControlsMovement.isDisabledInitially(context)) {
            ItemStack filter = ContraptionControlsMovement.getFilter(context);
            if (filter != null) {
                if (!this.isActorTypeDisabled(filter)) {
                    this.disabledActors.add(filter);
                }
            }
        }
    }

    public boolean isActorTypeDisabled(ItemStack filter) {
        return this.disabledActors.stream().anyMatch(i -> ContraptionControlsMovement.isSameFilter(i, filter));
    }

    public void setActorsActive(ItemStack referenceStack, boolean enable) {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.left).state());
            if (behaviour != null) {
                ItemStack behaviourStack = behaviour.canBeDisabledVia((MovementContext) pair.right);
                if (behaviourStack != null && (referenceStack.isEmpty() || ContraptionControlsMovement.isSameFilter(referenceStack, behaviourStack))) {
                    ((MovementContext) pair.right).disabled = !enable;
                    if (!enable) {
                        behaviour.onDisabledByControls((MovementContext) pair.right);
                    }
                }
            }
        }
    }

    public List<ItemStack> getDisabledActors() {
        return this.disabledActors;
    }

    public void stop(Level world) {
        this.forEachActor(world, (behaviour, ctx) -> {
            behaviour.stopMoving(ctx);
            ctx.position = null;
            ctx.motion = Vec3.ZERO;
            ctx.relativeMotion = Vec3.ZERO;
            ctx.rotation = v -> v;
        });
    }

    public void forEachActor(Level world, BiConsumer<MovementBehaviour, MovementContext> callBack) {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.getLeft()).state());
            if (behaviour != null) {
                callBack.accept(behaviour, (MovementContext) pair.getRight());
            }
        }
    }

    protected boolean shouldUpdateAfterMovement(StructureTemplate.StructureBlockInfo info) {
        return PoiTypes.forState(info.state()).isPresent() ? false : !(info.state().m_60734_() instanceof SlidingDoorBlock);
    }

    public void expandBoundsAroundAxis(Direction.Axis axis) {
        Set<BlockPos> blocks = this.getBlocks().keySet();
        int radius = (int) Math.ceil(Math.sqrt((double) getRadius(blocks, axis)));
        int maxX = radius + 2;
        int maxY = radius + 2;
        int maxZ = radius + 2;
        int minX = -radius - 1;
        int minY = -radius - 1;
        int minZ = -radius - 1;
        if (axis == Direction.Axis.X) {
            maxX = (int) this.bounds.maxX;
            minX = (int) this.bounds.minX;
        } else if (axis == Direction.Axis.Y) {
            maxY = (int) this.bounds.maxY;
            minY = (int) this.bounds.minY;
        } else if (axis == Direction.Axis.Z) {
            maxZ = (int) this.bounds.maxZ;
            minZ = (int) this.bounds.minZ;
        }
        this.bounds = new AABB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ);
    }

    public Map<UUID, Integer> getSeatMapping() {
        return this.seatMapping;
    }

    public BlockPos getSeatOf(UUID entityId) {
        if (!this.getSeatMapping().containsKey(entityId)) {
            return null;
        } else {
            int seatIndex = (Integer) this.getSeatMapping().get(entityId);
            return seatIndex >= this.getSeats().size() ? null : (BlockPos) this.getSeats().get(seatIndex);
        }
    }

    public BlockPos getBearingPosOf(UUID subContraptionEntityId) {
        return this.stabilizedSubContraptions.containsKey(subContraptionEntityId) ? ((BlockFace) this.stabilizedSubContraptions.get(subContraptionEntityId)).getConnectedPos() : null;
    }

    public void setSeatMapping(Map<UUID, Integer> seatMapping) {
        this.seatMapping = seatMapping;
    }

    public List<BlockPos> getSeats() {
        return this.seats;
    }

    public Map<BlockPos, StructureTemplate.StructureBlockInfo> getBlocks() {
        return this.blocks;
    }

    public List<MutablePair<StructureTemplate.StructureBlockInfo, MovementContext>> getActors() {
        return this.actors;
    }

    @Nullable
    public MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> getActorAt(BlockPos localPos) {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            if (localPos.equals(((StructureTemplate.StructureBlockInfo) pair.left).pos())) {
                return pair;
            }
        }
        return null;
    }

    public Map<BlockPos, MovingInteractionBehaviour> getInteractors() {
        return this.interactors;
    }

    @OnlyIn(Dist.CLIENT)
    public ContraptionLighter<?> makeLighter() {
        return new EmptyLighter(this);
    }

    public void invalidateColliders() {
        this.simplifiedEntityColliders = Optional.empty();
        this.gatherBBsOffThread();
    }

    private void gatherBBsOffThread() {
        this.getContraptionWorld();
        this.simplifiedEntityColliderProvider = CompletableFuture.supplyAsync(() -> {
            VoxelShape combinedShape = Shapes.empty();
            for (Entry<BlockPos, StructureTemplate.StructureBlockInfo> entry : this.blocks.entrySet()) {
                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) entry.getValue();
                BlockPos localPos = (BlockPos) entry.getKey();
                VoxelShape collisionShape = info.state().m_60742_(this.world, localPos, CollisionContext.empty());
                if (!collisionShape.isEmpty()) {
                    combinedShape = Shapes.joinUnoptimized(combinedShape, collisionShape.move((double) localPos.m_123341_(), (double) localPos.m_123342_(), (double) localPos.m_123343_()), BooleanOp.OR);
                }
            }
            return combinedShape.optimize().toAabbs();
        }).thenAccept(r -> {
            this.simplifiedEntityColliders = Optional.of(r);
            this.simplifiedEntityColliderProvider = null;
        });
    }

    public static float getRadius(Set<BlockPos> blocks, Direction.Axis axis) {
        switch(axis) {
            case X:
                return getMaxDistSqr(blocks, Vec3i::m_123342_, Vec3i::m_123343_);
            case Y:
                return getMaxDistSqr(blocks, Vec3i::m_123341_, Vec3i::m_123343_);
            case Z:
                return getMaxDistSqr(blocks, Vec3i::m_123341_, Vec3i::m_123342_);
            default:
                throw new IllegalStateException("Impossible axis");
        }
    }

    public static float getMaxDistSqr(Set<BlockPos> blocks, ICoordinate one, ICoordinate other) {
        float maxDistSq = -1.0F;
        for (BlockPos pos : blocks) {
            float a = one.get(pos);
            float b = other.get(pos);
            float distSq = a * a + b * b;
            if (distSq > maxDistSq) {
                maxDistSq = distSq;
            }
        }
        return maxDistSq;
    }

    public IItemHandlerModifiable getSharedInventory() {
        return this.storage.getItems();
    }

    public IItemHandlerModifiable getSharedFuelInventory() {
        return this.storage.getFuelItems();
    }

    public IFluidHandler getSharedFluidTanks() {
        return this.storage.getFluids();
    }

    public Collection<StructureTemplate.StructureBlockInfo> getRenderedBlocks() {
        return this.blocks.values();
    }

    public Collection<BlockEntity> getSpecialRenderedBEs() {
        return this.specialRenderedBlockEntities;
    }

    public boolean isHiddenInPortal(BlockPos localPos) {
        return false;
    }

    public Optional<List<AABB>> getSimplifiedEntityColliders() {
        return this.simplifiedEntityColliders;
    }

    public void handleContraptionFluidPacket(BlockPos localPos, FluidStack containedFluid) {
        this.storage.updateContainedFluid(localPos, containedFluid);
    }

    public void tickStorage(AbstractContraptionEntity entity) {
        this.storage.entityTick(entity);
    }

    public boolean containsBlockBreakers() {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            MovementBehaviour behaviour = AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.getLeft()).state());
            if (behaviour instanceof BlockBreakingMovementBehaviour || behaviour instanceof HarvesterMovementBehaviour) {
                return true;
            }
        }
        return false;
    }

    public static class ContraptionInvWrapper extends CombinedInvWrapper {

        protected final boolean isExternal;

        public ContraptionInvWrapper(boolean isExternal, IItemHandlerModifiable... itemHandler) {
            super(itemHandler);
            this.isExternal = isExternal;
        }

        public ContraptionInvWrapper(IItemHandlerModifiable... itemHandler) {
            this(false, itemHandler);
        }

        public boolean isSlotExternal(int slot) {
            if (this.isExternal) {
                return true;
            } else {
                IItemHandlerModifiable handler = this.getHandlerFromIndex(this.getIndexForSlot(slot));
                return handler instanceof Contraption.ContraptionInvWrapper && ((Contraption.ContraptionInvWrapper) handler).isSlotExternal(slot);
            }
        }
    }
}