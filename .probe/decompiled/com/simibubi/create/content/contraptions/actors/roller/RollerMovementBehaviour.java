package com.simibubi.create.content.contraptions.actors.roller;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.bogey.StandardBogeyBlock;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class RollerMovementBehaviour extends BlockBreakingMovementBehaviour {

    RollerMovementBehaviour.RollerTravellingPoint rollerScout = new RollerMovementBehaviour.RollerTravellingPoint();

    @Override
    public boolean isActive(MovementContext context) {
        return super.isActive(context) && !(context.contraption instanceof PulleyContraption) && VecHelper.isVecPointingTowards(context.relativeMotion, (Direction) context.state.m_61143_(RollerBlock.f_54117_));
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new RollerActorInstance(materialManager, simulationWorld, context);
    }

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffers) {
        if (!ContraptionRenderDispatcher.canInstance()) {
            RollerRenderer.renderInContraption(context, renderWorld, matrices, buffers);
        }
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(RollerBlock.f_54117_)).getNormal()).scale(0.45).subtract(0.0, 2.0, 0.0);
    }

    @Override
    protected float getBlockBreakingSpeed(MovementContext context) {
        return Mth.clamp(super.getBlockBreakingSpeed(context) * 1.5F, 0.0078125F, 16.0F);
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        for (Direction side : Iterate.directions) {
            if (world.getBlockState(breakingPos.relative(side)).m_204336_(BlockTags.PORTALS)) {
                return false;
            }
        }
        return super.canBreak(world, breakingPos, state) && !state.m_60812_(world, breakingPos).isEmpty() && !AllBlocks.TRACK.has(state);
    }

    @Override
    protected DamageSource getDamageSource(Level level) {
        return CreateDamageSources.roller(level);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        Level world = context.world;
        BlockState stateVisited = world.getBlockState(pos);
        if (!stateVisited.m_60796_(world, pos)) {
            this.damageEntities(context, pos, world);
        }
        if (!world.isClientSide) {
            List<BlockPos> positionsToBreak = this.getPositionsToBreak(context, pos);
            if (positionsToBreak.isEmpty()) {
                this.triggerPaver(context, pos);
            } else {
                BlockPos argMax = null;
                double max = -1.0;
                for (BlockPos toBreak : positionsToBreak) {
                    float hardness = context.world.getBlockState(toBreak).m_60800_(world, toBreak);
                    if (!((double) hardness < max)) {
                        max = (double) hardness;
                        argMax = toBreak;
                    }
                }
                if (argMax == null) {
                    this.triggerPaver(context, pos);
                } else {
                    context.data.put("ReferencePos", NbtUtils.writeBlockPos(pos));
                    context.data.put("BreakingPos", NbtUtils.writeBlockPos(argMax));
                    context.stall = true;
                }
            }
        }
    }

    @Override
    protected void onBlockBroken(MovementContext context, BlockPos pos, BlockState brokenState) {
        super.onBlockBroken(context, pos, brokenState);
        if (context.data.contains("ReferencePos")) {
            BlockPos referencePos = NbtUtils.readBlockPos(context.data.getCompound("ReferencePos"));
            for (BlockPos otherPos : this.getPositionsToBreak(context, referencePos)) {
                if (!otherPos.equals(pos)) {
                    this.destroyBlock(context, otherPos);
                }
            }
            this.triggerPaver(context, referencePos);
            context.data.remove("ReferencePos");
        }
    }

    @Override
    protected void destroyBlock(MovementContext context, BlockPos breakingPos) {
        BlockState blockState = context.world.getBlockState(breakingPos);
        boolean noHarvest = blockState.m_204336_(BlockTags.NEEDS_IRON_TOOL) || blockState.m_204336_(BlockTags.NEEDS_STONE_TOOL) || blockState.m_204336_(BlockTags.NEEDS_DIAMOND_TOOL);
        BlockHelper.destroyBlock(context.world, breakingPos, 1.0F, stack -> {
            if (!noHarvest && !context.world.random.nextBoolean()) {
                this.dropItem(context, stack);
            }
        });
        super.destroyBlock(context, breakingPos);
    }

    protected List<BlockPos> getPositionsToBreak(MovementContext context, BlockPos visitedPos) {
        ArrayList<BlockPos> positions = new ArrayList();
        RollerBlockEntity.RollingMode mode = this.getMode(context);
        if (mode != RollerBlockEntity.RollingMode.TUNNEL_PAVE) {
            return positions;
        } else {
            int startingY = 1;
            if (!this.getStateToPaveWith(context).m_60795_()) {
                FilterItemStack filter = context.getFilterFromBE();
                if (!ItemHelper.extract(context.contraption.getSharedInventory(), stack -> filter.test(context.world, stack), 1, true).isEmpty()) {
                    startingY = 0;
                }
            }
            PaveTask profileForTracks = this.createHeightProfileForTracks(context);
            if (profileForTracks == null) {
                for (int i = startingY; i <= 2; i++) {
                    if (this.testBreakerTarget(context, visitedPos.above(i), i)) {
                        positions.add(visitedPos.above(i));
                    }
                }
                return positions;
            } else {
                for (Couple<Integer> coords : profileForTracks.keys()) {
                    float height = profileForTracks.get(coords);
                    BlockPos targetPosition = BlockPos.containing((double) coords.getFirst().intValue(), (double) height, (double) coords.getSecond().intValue());
                    boolean shouldPlaceSlab = (double) height > Math.floor((double) height) + 0.45;
                    if (startingY == 1 && shouldPlaceSlab && context.world.getBlockState(targetPosition.above()).m_61145_(SlabBlock.TYPE).orElse(SlabType.DOUBLE) == SlabType.BOTTOM) {
                        startingY = 2;
                    }
                    for (int ix = startingY; ix <= (shouldPlaceSlab ? 3 : 2); ix++) {
                        if (this.testBreakerTarget(context, targetPosition.above(ix), ix)) {
                            positions.add(targetPosition.above(ix));
                        }
                    }
                }
                return positions;
            }
        }
    }

    protected boolean testBreakerTarget(MovementContext context, BlockPos target, int columnY) {
        BlockState stateToPaveWith = this.getStateToPaveWith(context);
        BlockState stateToPaveWithAsSlab = this.getStateToPaveWithAsSlab(context);
        BlockState stateAbove = context.world.getBlockState(target);
        if (columnY == 0 && stateAbove.m_60713_(stateToPaveWith.m_60734_())) {
            return false;
        } else {
            return stateToPaveWithAsSlab != null && columnY == 1 && stateAbove.m_60713_(stateToPaveWithAsSlab.m_60734_()) ? false : this.canBreak(context.world, target, stateAbove);
        }
    }

    @Nullable
    protected PaveTask createHeightProfileForTracks(MovementContext context) {
        if (context.contraption == null) {
            return null;
        } else if (context.contraption.entity instanceof CarriageContraptionEntity cce) {
            Carriage carriage = cce.getCarriage();
            if (carriage == null) {
                return null;
            } else {
                Train train = carriage.train;
                if (train != null && train.graph != null) {
                    CarriageBogey mainBogey = carriage.bogeys.getFirst();
                    TravellingPoint point = mainBogey.trailing();
                    this.rollerScout.node1 = point.node1;
                    this.rollerScout.node2 = point.node2;
                    this.rollerScout.edge = point.edge;
                    this.rollerScout.position = point.position;
                    Direction.Axis axis = Direction.Axis.X;
                    StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) context.contraption.getBlocks().get(BlockPos.ZERO);
                    if (info != null && info.state().m_61138_(StandardBogeyBlock.AXIS)) {
                        axis = (Direction.Axis) info.state().m_61143_(StandardBogeyBlock.AXIS);
                    }
                    Direction orientation = cce.getInitialOrientation();
                    Direction rollerFacing = (Direction) context.state.m_61143_(RollerBlock.f_54117_);
                    int step = orientation.getAxisDirection().getStep();
                    double widthWiseOffset = (double) (axis.choose(-context.localPos.m_123343_(), 0, -context.localPos.m_123341_()) * step);
                    double lengthWiseOffset = (double) (axis.choose(-context.localPos.m_123341_(), 0, context.localPos.m_123343_()) * step - 1);
                    if (rollerFacing == orientation.getClockWise()) {
                        lengthWiseOffset++;
                    }
                    double distanceToTravel = 2.0;
                    PaveTask heightProfile = new PaveTask(widthWiseOffset, widthWiseOffset);
                    TravellingPoint.ITrackSelector steering = this.rollerScout.steer(TravellingPoint.SteerDirection.NONE, new Vec3(0.0, 1.0, 0.0));
                    this.rollerScout.traversalCallback = (edge, coords) -> {
                    };
                    this.rollerScout.travel(train.graph, lengthWiseOffset + 1.0, steering);
                    this.rollerScout.traversalCallback = (edge, coords) -> {
                        if (edge != null) {
                            if (!edge.isInterDimensional()) {
                                if (edge.node1.getLocation().dimension == context.world.dimension()) {
                                    TrackPaverV2.pave(heightProfile, train.graph, edge, (Double) coords.getFirst(), (Double) coords.getSecond());
                                }
                            }
                        }
                    };
                    this.rollerScout.travel(train.graph, distanceToTravel, steering);
                    for (Couple<Integer> entry : heightProfile.keys()) {
                        heightProfile.put(entry.getFirst(), entry.getSecond(), (float) context.localPos.m_123342_() + heightProfile.get(entry));
                    }
                    return heightProfile;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    protected void triggerPaver(MovementContext context, BlockPos pos) {
        BlockState stateToPaveWith = this.getStateToPaveWith(context);
        BlockState stateToPaveWithAsSlab = this.getStateToPaveWithAsSlab(context);
        RollerBlockEntity.RollingMode mode = this.getMode(context);
        if (mode == RollerBlockEntity.RollingMode.TUNNEL_PAVE || !stateToPaveWith.m_60795_()) {
            Vec3 directionVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(RollerBlock.f_54117_)).getClockWise().getNormal());
            directionVec = (Vec3) context.rotation.apply(directionVec);
            RollerMovementBehaviour.PaveResult paveResult = RollerMovementBehaviour.PaveResult.PASS;
            int yOffset = 0;
            List<Pair<BlockPos, Boolean>> paveSet = new ArrayList();
            PaveTask profileForTracks = this.createHeightProfileForTracks(context);
            if (profileForTracks == null) {
                paveSet.add(Pair.of(pos, false));
            } else {
                for (Couple<Integer> coords : profileForTracks.keys()) {
                    float height = profileForTracks.get(coords);
                    boolean shouldPlaceSlab = (double) height > Math.floor((double) height) + 0.45;
                    BlockPos targetPosition = BlockPos.containing((double) coords.getFirst().intValue(), (double) height, (double) coords.getSecond().intValue());
                    paveSet.add(Pair.of(targetPosition, shouldPlaceSlab));
                }
            }
            if (!paveSet.isEmpty()) {
                while (paveResult == RollerMovementBehaviour.PaveResult.PASS) {
                    if (yOffset > AllConfigs.server().kinetics.rollerFillDepth.get()) {
                        paveResult = RollerMovementBehaviour.PaveResult.FAIL;
                        break;
                    }
                    Set<Pair<BlockPos, Boolean>> currentLayer = new HashSet();
                    if (mode == RollerBlockEntity.RollingMode.WIDE_FILL) {
                        for (Pair<BlockPos, Boolean> anchor : paveSet) {
                            int radius = (yOffset + 1) / 2;
                            for (int i = -radius; i <= radius; i++) {
                                for (int j = -radius; j <= radius; j++) {
                                    if (BlockPos.ZERO.m_123333_(new BlockPos(i, 0, j)) <= radius) {
                                        currentLayer.add(Pair.of(anchor.getFirst().offset(i, -yOffset, j), anchor.getSecond()));
                                    }
                                }
                            }
                        }
                    } else {
                        for (Pair<BlockPos, Boolean> anchor : paveSet) {
                            currentLayer.add(Pair.of(anchor.getFirst().below(yOffset), anchor.getSecond()));
                        }
                    }
                    boolean completelyBlocked = true;
                    boolean anyBlockPlaced = false;
                    for (Pair<BlockPos, Boolean> currentPos : currentLayer) {
                        if (stateToPaveWithAsSlab != null && yOffset == 0 && currentPos.getSecond()) {
                            this.tryFill(context, currentPos.getFirst().above(), stateToPaveWithAsSlab);
                        }
                        paveResult = this.tryFill(context, currentPos.getFirst(), stateToPaveWith);
                        if (paveResult != RollerMovementBehaviour.PaveResult.FAIL) {
                            completelyBlocked = false;
                        }
                        if (paveResult == RollerMovementBehaviour.PaveResult.SUCCESS) {
                            anyBlockPlaced = true;
                        }
                    }
                    if (anyBlockPlaced) {
                        paveResult = RollerMovementBehaviour.PaveResult.SUCCESS;
                    } else if (!completelyBlocked || yOffset == 0) {
                        paveResult = RollerMovementBehaviour.PaveResult.PASS;
                    }
                    if (paveResult == RollerMovementBehaviour.PaveResult.SUCCESS && stateToPaveWith.m_60734_() instanceof FallingBlock) {
                        paveResult = RollerMovementBehaviour.PaveResult.PASS;
                    }
                    if (paveResult != RollerMovementBehaviour.PaveResult.PASS || mode == RollerBlockEntity.RollingMode.TUNNEL_PAVE) {
                        break;
                    }
                    yOffset++;
                }
                if (paveResult == RollerMovementBehaviour.PaveResult.SUCCESS) {
                    context.data.putInt("WaitingTicks", 2);
                    context.data.put("LastPos", NbtUtils.writeBlockPos(pos));
                    context.stall = true;
                }
            }
        }
    }

    public static BlockState getStateToPaveWith(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem bi) {
            BlockState defaultBlockState = bi.getBlock().defaultBlockState();
            if (defaultBlockState.m_61138_(SlabBlock.TYPE)) {
                defaultBlockState = (BlockState) defaultBlockState.m_61124_(SlabBlock.TYPE, SlabType.DOUBLE);
            }
            return defaultBlockState;
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    protected BlockState getStateToPaveWith(MovementContext context) {
        return getStateToPaveWith(ItemStack.of(context.blockEntityData.getCompound("Filter")));
    }

    protected BlockState getStateToPaveWithAsSlab(MovementContext context) {
        BlockState stateToPaveWith = this.getStateToPaveWith(context);
        if (stateToPaveWith.m_61138_(SlabBlock.TYPE)) {
            return (BlockState) stateToPaveWith.m_61124_(SlabBlock.TYPE, SlabType.BOTTOM);
        } else {
            Block block = stateToPaveWith.m_60734_();
            if (block == null) {
                return null;
            } else {
                ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);
                String namespace = rl.getNamespace();
                String blockName = rl.getPath();
                int nameLength = blockName.length();
                List<String> possibleSlabLocations = new ArrayList();
                possibleSlabLocations.add(blockName + "_slab");
                if (blockName.endsWith("s") && nameLength > 1) {
                    possibleSlabLocations.add(blockName.substring(0, nameLength - 1) + "_slab");
                }
                if (blockName.endsWith("planks") && nameLength > 7) {
                    possibleSlabLocations.add(blockName.substring(0, nameLength - 7) + "_slab");
                }
                for (String locationAttempt : possibleSlabLocations) {
                    Optional<Block> result = ForgeRegistries.BLOCKS.getHolder(new ResourceLocation(namespace, locationAttempt)).map(slabHolder -> (Block) slabHolder.value());
                    if (!result.isEmpty()) {
                        return ((Block) result.get()).defaultBlockState();
                    }
                }
                return null;
            }
        }
    }

    protected RollerBlockEntity.RollingMode getMode(MovementContext context) {
        return RollerBlockEntity.RollingMode.values()[context.blockEntityData.getInt("ScrollValue")];
    }

    protected RollerMovementBehaviour.PaveResult tryFill(MovementContext context, BlockPos targetPos, BlockState toPlace) {
        Level level = context.world;
        if (!level.isLoaded(targetPos)) {
            return RollerMovementBehaviour.PaveResult.FAIL;
        } else {
            BlockState existing = level.getBlockState(targetPos);
            if (existing.m_60713_(toPlace.m_60734_())) {
                return RollerMovementBehaviour.PaveResult.PASS;
            } else if (!existing.m_204336_(BlockTags.LEAVES) && !existing.m_247087_() && !existing.m_60812_(level, targetPos).isEmpty()) {
                return RollerMovementBehaviour.PaveResult.FAIL;
            } else {
                FilterItemStack filter = context.getFilterFromBE();
                ItemStack held = ItemHelper.extract(context.contraption.getSharedInventory(), stack -> filter.test(context.world, stack), 1, false);
                if (held.isEmpty()) {
                    return RollerMovementBehaviour.PaveResult.FAIL;
                } else {
                    level.setBlockAndUpdate(targetPos, toPlace);
                    return RollerMovementBehaviour.PaveResult.SUCCESS;
                }
            }
        }
    }

    private static enum PaveResult {

        FAIL, PASS, SUCCESS
    }

    private final class RollerTravellingPoint extends TravellingPoint {

        public BiConsumer<TrackEdge, Couple<Double>> traversalCallback;

        @Override
        protected Double edgeTraversedFrom(TrackGraph graph, boolean forward, TravellingPoint.IEdgePointListener edgePointListener, TravellingPoint.ITurnListener turnListener, double prevPos, double totalDistance) {
            double from = forward ? prevPos : this.position;
            double to = forward ? this.position : prevPos;
            this.traversalCallback.accept(this.edge, Couple.create(from, to));
            return super.edgeTraversedFrom(graph, forward, edgePointListener, turnListener, prevPos, totalDistance);
        }
    }
}