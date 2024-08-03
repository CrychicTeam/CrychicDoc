package com.simibubi.create.content.trains.track;

import com.google.common.base.Predicates;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.girder.GirderBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.trains.CubeParticleData;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.block.render.MultiPosDestructionHandler;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.jetbrains.annotations.Nullable;

public class TrackBlock extends Block implements IBE<TrackBlockEntity>, IWrenchable, ITrackBlock, ISpecialBlockItemRequirement, ProperWaterloggedBlock {

    public static final EnumProperty<TrackShape> SHAPE = EnumProperty.create("shape", TrackShape.class);

    public static final BooleanProperty HAS_BE = BooleanProperty.create("turn");

    protected final TrackMaterial material;

    public TrackBlock(BlockBehaviour.Properties blockBehaviourProperties0, TrackMaterial material) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(SHAPE, TrackShape.ZO)).m_61124_(HAS_BE, false)).m_61124_(WATERLOGGED, false));
        this.material = material;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        super.createBlockStateDefinition(stateDefinitionBuilderBlockBlockState0.add(SHAPE, HAS_BE, WATERLOGGED));
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob mob) {
        return BlockPathTypes.RAIL;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this.fluidState(state);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new TrackBlock.RenderProperties());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState stateForPlacement = this.withWater(super.getStateForPlacement(ctx), ctx);
        if (ctx.m_43723_() == null) {
            return stateForPlacement;
        } else {
            Vec3 lookAngle = ctx.m_43723_().m_20154_();
            lookAngle = lookAngle.multiply(1.0, 0.0, 1.0);
            if (Mth.equal(lookAngle.length(), 0.0)) {
                lookAngle = VecHelper.rotate(new Vec3(0.0, 0.0, 1.0), (double) (-ctx.m_43723_().m_146908_()), Direction.Axis.Y);
            }
            lookAngle = lookAngle.normalize();
            TrackShape best = TrackShape.ZO;
            double bestValue = Float.MAX_VALUE;
            for (TrackShape shape : TrackShape.values()) {
                if (!shape.isJunction() && !shape.isPortal()) {
                    Vec3 axis = (Vec3) shape.getAxes().get(0);
                    double distance = Math.min(axis.distanceToSqr(lookAngle), axis.normalize().scale(-1.0).distanceToSqr(lookAngle));
                    if (!(distance > bestValue)) {
                        bestValue = distance;
                        best = shape;
                    }
                }
            }
            Level level = ctx.m_43725_();
            Vec3 bestAxis = (Vec3) best.getAxes().get(0);
            if (bestAxis.lengthSqr() == 1.0) {
                for (boolean neg : Iterate.trueAndFalse) {
                    BlockPos offset = ctx.getClickedPos().offset(BlockPos.containing(bestAxis.scale(neg ? -1.0 : 1.0)));
                    if (level.getBlockState(offset).m_60783_(level, offset, Direction.UP) && !level.getBlockState(offset.above()).m_60783_(level, offset, Direction.DOWN)) {
                        if (best == TrackShape.XO) {
                            best = neg ? TrackShape.AW : TrackShape.AE;
                        }
                        if (best == TrackShape.ZO) {
                            best = neg ? TrackShape.AN : TrackShape.AS;
                        }
                    }
                }
            }
            return (BlockState) stateForPlacement.m_61124_(SHAPE, best);
        }
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        if (!pLevel.isClientSide()) {
            if (pPlayer.isCreative()) {
                this.withBlockEntityDo(pLevel, pPos, be -> {
                    be.cancelDrops = true;
                    be.removeInboundConnections(true);
                });
            }
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.m_60734_() != this || pState.m_61124_(HAS_BE, true) != pOldState.m_61124_(HAS_BE, true)) {
            if (!pLevel.isClientSide) {
                LevelTickAccess<Block> blockTicks = pLevel.m_183326_();
                if (!blockTicks.m_183582_(pPos, this)) {
                    pLevel.m_186460_(pPos, this, 1);
                }
                this.updateGirders(pState, pLevel, pPos, blockTicks);
            }
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        this.withBlockEntityDo(pLevel, pPos, TrackBlockEntity::validateConnections);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource0) {
        TrackPropagator.onRailAdded(level, pos, state);
        this.withBlockEntityDo(level, pos, tbe -> tbe.tilt.undoSmoothing());
        if (!((TrackShape) state.m_61143_(SHAPE)).isPortal()) {
            this.connectToPortal(level, pos, state);
        }
    }

    protected void connectToPortal(ServerLevel level, BlockPos pos, BlockState state) {
        TrackShape shape = (TrackShape) state.m_61143_(SHAPE);
        Direction.Axis portalTest = shape == TrackShape.XO ? Direction.Axis.X : (shape == TrackShape.ZO ? Direction.Axis.Z : null);
        if (portalTest != null) {
            boolean pop = false;
            String fail = null;
            BlockPos failPos = null;
            for (Direction d : Iterate.directionsInAxis(portalTest)) {
                BlockPos portalPos = pos.relative(d);
                BlockState portalState = level.m_8055_(portalPos);
                if (AllPortalTracks.isSupportedPortal(portalState)) {
                    pop = true;
                    Pair<ServerLevel, BlockFace> otherSide = AllPortalTracks.getOtherSide(level, new BlockFace(pos, d));
                    if (otherSide == null) {
                        fail = "missing";
                    } else {
                        ServerLevel otherLevel = otherSide.getFirst();
                        BlockFace otherTrack = otherSide.getSecond();
                        BlockPos otherTrackPos = otherTrack.getPos();
                        BlockState existing = otherLevel.m_8055_(otherTrackPos);
                        if (!existing.m_247087_()) {
                            fail = "blocked";
                            failPos = otherTrackPos;
                        } else {
                            level.m_7731_(pos, (BlockState) ((BlockState) state.m_61124_(SHAPE, TrackShape.asPortal(d))).m_61124_(HAS_BE, true), 3);
                            if (level.m_7702_(pos) instanceof TrackBlockEntity tbe) {
                                tbe.bind(otherLevel.m_46472_(), otherTrackPos);
                            }
                            otherLevel.m_7731_(otherTrackPos, (BlockState) ((BlockState) state.m_61124_(SHAPE, TrackShape.asPortal(otherTrack.getFace()))).m_61124_(HAS_BE, true), 3);
                            if (otherLevel.m_7702_(otherTrackPos) instanceof TrackBlockEntity tbe) {
                                tbe.bind(level.m_46472_(), pos);
                            }
                            pop = false;
                        }
                    }
                }
            }
            if (pop) {
                level.m_46961_(pos, true);
                if (fail != null) {
                    Player player = level.m_5788_((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 10.0, Predicates.alwaysTrue());
                    if (player != null) {
                        player.displayClientMessage(Components.literal("<!> ").append(Lang.translateDirect("portal_track.failed")).withStyle(ChatFormatting.GOLD), false);
                        MutableComponent component = failPos != null ? Lang.translateDirect("portal_track." + fail, failPos.m_123341_(), failPos.m_123342_(), failPos.m_123343_()) : Lang.translateDirect("portal_track." + fail);
                        player.displayClientMessage(Components.literal(" - ").withStyle(ChatFormatting.GRAY).append(component.withStyle(st -> st.withColor(16765876))), false);
                    }
                }
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor level, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(level, state, pCurrentPos);
        TrackShape shape = (TrackShape) state.m_61143_(SHAPE);
        if (!shape.isPortal()) {
            return state;
        } else {
            for (Direction d : Iterate.horizontalDirections) {
                if (TrackShape.asPortal(d) == state.m_61143_(SHAPE) && pDirection == d) {
                    BlockPos portalPos = pCurrentPos.relative(d);
                    BlockState portalState = level.m_8055_(portalPos);
                    if (!AllPortalTracks.isSupportedPortal(portalState)) {
                        return Blocks.AIR.defaultBlockState();
                    }
                }
            }
            return state;
        }
    }

    @Override
    public int getYOffsetAt(BlockGetter world, BlockPos pos, BlockState state, Vec3 end) {
        return (Integer) this.getBlockEntityOptional(world, pos).map(tbe -> tbe.tilt.getYOffsetForAxisEnd(end)).orElse(0);
    }

    @Override
    public Collection<TrackNodeLocation.DiscoveredLocation> getConnected(BlockGetter worldIn, BlockPos pos, BlockState state, boolean linear, TrackNodeLocation connectedTo) {
        BlockGetter world = (BlockGetter) (connectedTo != null && worldIn instanceof ServerLevel sl ? sl.getServer().getLevel(connectedTo.dimension) : worldIn);
        Collection<TrackNodeLocation.DiscoveredLocation> list;
        if (this.getTrackAxes(world, pos, state).size() > 1) {
            Vec3 center = Vec3.atBottomCenterOf(pos).add(0.0, this.getElevationAtCenter(world, pos, state), 0.0);
            TrackShape shape = (TrackShape) state.m_61143_(SHAPE);
            list = new ArrayList();
            for (Vec3 axis : this.getTrackAxes(world, pos, state)) {
                for (boolean fromCenter : Iterate.trueAndFalse) {
                    ITrackBlock.addToListIfConnected(connectedTo, list, (d, b) -> axis.scale(b ? 0.0 : (fromCenter ? -d : d)).add(center), b -> shape.getNormal(), b -> world instanceof Level l ? l.dimension() : Level.OVERWORLD, v -> 0, axis, null, (b, v) -> ITrackBlock.getMaterialSimple(world, v));
                }
            }
        } else {
            list = ITrackBlock.super.getConnected(world, pos, state, linear, connectedTo);
        }
        if (!(Boolean) state.m_61143_(HAS_BE)) {
            return list;
        } else if (linear) {
            return list;
        } else if (world.getBlockEntity(pos) instanceof TrackBlockEntity trackBE) {
            Map<BlockPos, BezierConnection> connections = trackBE.getConnections();
            connections.forEach((connectedPos, bc) -> ITrackBlock.addToListIfConnected(connectedTo, list, (d, b) -> d == 1.0 ? Vec3.atLowerCornerOf(bc.tePositions.get(b)) : bc.starts.get(b), bc.normals::get, b -> world instanceof Level l ? l.dimension() : Level.OVERWORLD, bc::yOffsetAt, null, bc, (b, v) -> ITrackBlock.getMaterialSimple(world, v, bc.getMaterial())));
            if (trackBE.boundLocation != null && world instanceof ServerLevel level) {
                ResourceKey var26 = trackBE.boundLocation.getFirst();
                ServerLevel otherLevel = level.getServer().getLevel(var26);
                if (otherLevel == null) {
                    return list;
                } else {
                    BlockPos boundPos = trackBE.boundLocation.getSecond();
                    BlockState boundState = otherLevel.m_8055_(boundPos);
                    if (!AllTags.AllBlockTags.TRACKS.matches(boundState)) {
                        return list;
                    } else {
                        Vec3 center = Vec3.atBottomCenterOf(pos).add(0.0, this.getElevationAtCenter(world, pos, state), 0.0);
                        Vec3 boundCenter = Vec3.atBottomCenterOf(boundPos).add(0.0, this.getElevationAtCenter(otherLevel, boundPos, boundState), 0.0);
                        TrackShape shape = (TrackShape) state.m_61143_(SHAPE);
                        TrackShape boundShape = (TrackShape) boundState.m_61143_(SHAPE);
                        Vec3 boundAxis = (Vec3) this.getTrackAxes(otherLevel, boundPos, boundState).get(0);
                        this.getTrackAxes(world, pos, state).forEach(axis -> ITrackBlock.addToListIfConnected(connectedTo, list, (d, b) -> (b ? axis : boundAxis).scale(d).add(b ? center : boundCenter), b -> (b ? shape : boundShape).getNormal(), b -> b ? level.m_46472_() : otherLevel.m_46472_(), v -> 0, axis, null, (b, v) -> ITrackBlock.getMaterialSimple(b ? level : otherLevel, v)));
                        return list;
                    }
                }
            } else {
                return list;
            }
        } else {
            return list;
        }
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if (((TrackShape) pState.m_61143_(SHAPE)).isPortal()) {
            Vec3 v = Vec3.atLowerCornerOf(pPos).subtract(0.125, 0.0, 0.125);
            CubeParticleData data = new CubeParticleData(1.0F, pRand.nextFloat(), 1.0F, 0.0125F + 0.0625F * pRand.nextFloat(), 30, false);
            pLevel.addParticle(data, v.x + (double) (pRand.nextFloat() * 1.5F), v.y + 0.25, v.z + (double) (pRand.nextFloat() * 1.5F), 0.0, 0.04, 0.0);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        boolean removeBE = false;
        if ((Boolean) pState.m_61143_(HAS_BE) && (!pState.m_60713_(pNewState.m_60734_()) || !(Boolean) pNewState.m_61143_(HAS_BE))) {
            if (pLevel.getBlockEntity(pPos) instanceof TrackBlockEntity tbe && !pLevel.isClientSide) {
                tbe.cancelDrops = tbe.cancelDrops | pNewState.m_60734_() == this;
                tbe.removeInboundConnections(true);
            }
            removeBE = true;
        }
        if (pNewState.m_60734_() != this || pState.m_61124_(HAS_BE, true) != pNewState.m_61124_(HAS_BE, true)) {
            TrackPropagator.onRailRemoved(pLevel, pPos, pState);
        }
        if (removeBE) {
            pLevel.removeBlockEntity(pPos);
        }
        if (!pLevel.isClientSide) {
            this.updateGirders(pState, pLevel, pPos, pLevel.m_183326_());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            for (Entry<BlockPos, BoundingBox> entry : StationBlockEntity.assemblyAreas.get(world).entrySet()) {
                if (((BoundingBox) entry.getValue()).isInside(pos) && world.getBlockEntity((BlockPos) entry.getKey()) instanceof StationBlockEntity station && station.trackClicked(player, hand, this, state, pos)) {
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        }
    }

    private void updateGirders(BlockState pState, Level pLevel, BlockPos pPos, LevelTickAccess<Block> blockTicks) {
        for (Vec3 vec3 : this.getTrackAxes(pLevel, pPos, pState)) {
            if (!(vec3.length() > 1.0) && vec3.y == 0.0) {
                for (int side : Iterate.positiveAndNegative) {
                    BlockPos girderPos = pPos.below().offset(BlockPos.containing(vec3.z * (double) side, 0.0, vec3.x * (double) side));
                    BlockState girderState = pLevel.getBlockState(girderPos);
                    Block var14 = girderState.m_60734_();
                    if (var14 instanceof GirderBlock) {
                        GirderBlock girderBlock = (GirderBlock) var14;
                        if (!blockTicks.m_183582_(girderPos, girderBlock)) {
                            pLevel.m_186460_(girderPos, girderBlock, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.m_8055_(pos.below()).m_60734_() != this;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter0, BlockPos blockPos1, CollisionContext collisionContext2) {
        return this.getFullShape(state);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter pLevel, BlockPos pPos) {
        return this.getFullShape(state);
    }

    private VoxelShape getFullShape(BlockState state) {
        switch((TrackShape) state.m_61143_(SHAPE)) {
            case AE:
                return AllShapes.TRACK_ASC.get(Direction.EAST);
            case AW:
                return AllShapes.TRACK_ASC.get(Direction.WEST);
            case AN:
                return AllShapes.TRACK_ASC.get(Direction.NORTH);
            case AS:
                return AllShapes.TRACK_ASC.get(Direction.SOUTH);
            case CR_D:
                return AllShapes.TRACK_CROSS_DIAG;
            case CR_NDX:
                return AllShapes.TRACK_CROSS_ORTHO_DIAG.get(Direction.SOUTH);
            case CR_NDZ:
                return AllShapes.TRACK_CROSS_DIAG_ORTHO.get(Direction.SOUTH);
            case CR_O:
                return AllShapes.TRACK_CROSS;
            case CR_PDX:
                return AllShapes.TRACK_CROSS_DIAG_ORTHO.get(Direction.EAST);
            case CR_PDZ:
                return AllShapes.TRACK_CROSS_ORTHO_DIAG.get(Direction.EAST);
            case ND:
                return AllShapes.TRACK_DIAG.get(Direction.SOUTH);
            case PD:
                return AllShapes.TRACK_DIAG.get(Direction.EAST);
            case XO:
                return AllShapes.TRACK_ORTHO.get(Direction.EAST);
            case ZO:
                return AllShapes.TRACK_ORTHO.get(Direction.SOUTH);
            case TE:
                return AllShapes.TRACK_ORTHO_LONG.get(Direction.EAST);
            case TW:
                return AllShapes.TRACK_ORTHO_LONG.get(Direction.WEST);
            case TS:
                return AllShapes.TRACK_ORTHO_LONG.get(Direction.SOUTH);
            case TN:
                return AllShapes.TRACK_ORTHO_LONG.get(Direction.NORTH);
            case NONE:
            default:
                return AllShapes.TRACK_FALLBACK;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((TrackShape) pState.m_61143_(SHAPE)) {
            case AE:
            case AW:
            case AN:
            case AS:
                return Shapes.empty();
            default:
                return AllShapes.TRACK_COLLISION;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState state) {
        return !state.m_61143_(HAS_BE) ? null : AllBlockEntityTypes.TRACK.create(blockPos0, state);
    }

    @Override
    public Class<TrackBlockEntity> getBlockEntityClass() {
        return TrackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TrackBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends TrackBlockEntity>) AllBlockEntityTypes.TRACK.get();
    }

    @Override
    public Vec3 getUpNormal(BlockGetter world, BlockPos pos, BlockState state) {
        return ((TrackShape) state.m_61143_(SHAPE)).getNormal();
    }

    @Override
    public List<Vec3> getTrackAxes(BlockGetter world, BlockPos pos, BlockState state) {
        return ((TrackShape) state.m_61143_(SHAPE)).getAxes();
    }

    @Override
    public Vec3 getCurveStart(BlockGetter world, BlockPos pos, BlockState state, Vec3 axis) {
        boolean vertical = axis.y != 0.0;
        return VecHelper.getCenterOf(pos).add(0.0, (double) (vertical ? 0.0F : -0.5F), 0.0).add(axis.scale(0.5));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (!level.isClientSide && !player.isCreative() && (Boolean) state.m_61143_(HAS_BE) && level.getBlockEntity(context.getClickedPos()) instanceof TrackBlockEntity trackBE) {
            trackBE.cancelDrops = true;
            trackBE.connections.values().forEach(bc -> bc.addItemsToPlayer(player));
        }
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public BlockState overlay(BlockGetter world, BlockPos pos, BlockState existing, BlockState placed) {
        if (placed.m_60734_() != this) {
            return existing;
        } else {
            TrackShape existingShape = (TrackShape) existing.m_61143_(SHAPE);
            TrackShape placedShape = (TrackShape) placed.m_61143_(SHAPE);
            TrackShape combinedShape = null;
            for (boolean flip : Iterate.trueAndFalse) {
                TrackShape s1 = flip ? existingShape : placedShape;
                TrackShape s2 = flip ? placedShape : existingShape;
                if (s1 == TrackShape.XO && s2 == TrackShape.ZO) {
                    combinedShape = TrackShape.CR_O;
                }
                if (s1 == TrackShape.PD && s2 == TrackShape.ND) {
                    combinedShape = TrackShape.CR_D;
                }
                if (s1 == TrackShape.XO && s2 == TrackShape.PD) {
                    combinedShape = TrackShape.CR_PDX;
                }
                if (s1 == TrackShape.ZO && s2 == TrackShape.PD) {
                    combinedShape = TrackShape.CR_PDZ;
                }
                if (s1 == TrackShape.XO && s2 == TrackShape.ND) {
                    combinedShape = TrackShape.CR_NDX;
                }
                if (s1 == TrackShape.ZO && s2 == TrackShape.ND) {
                    combinedShape = TrackShape.CR_NDZ;
                }
            }
            if (combinedShape != null) {
                existing = (BlockState) existing.m_61124_(SHAPE, combinedShape);
            }
            return existing;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation pRotation) {
        return (BlockState) state.m_61124_(SHAPE, ((TrackShape) state.m_61143_(SHAPE)).rotate(pRotation));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror pMirror) {
        return (BlockState) state.m_61124_(SHAPE, ((TrackShape) state.m_61143_(SHAPE)).mirror(pMirror));
    }

    @Override
    public BlockState getBogeyAnchor(BlockGetter world, BlockPos pos, BlockState state) {
        return (BlockState) AllBlocks.SMALL_BOGEY.getDefaultState().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, state.m_61143_(SHAPE) == TrackShape.XO ? Direction.Axis.X : Direction.Axis.Z);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel prepareAssemblyOverlay(BlockGetter world, BlockPos pos, BlockState state, Direction direction, PoseStack ms) {
        TransformStack.cast(ms).rotateCentered(Direction.UP, AngleHelper.rad((double) AngleHelper.horizontalAngle(direction)));
        return AllPartialModels.TRACK_ASSEMBLING_OVERLAY;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel prepareTrackOverlay(BlockGetter world, BlockPos pos, BlockState state, BezierTrackPointLocation bezierPoint, Direction.AxisDirection direction, PoseStack ms, TrackTargetingBehaviour.RenderedTrackOverlayType type) {
        TransformStack msr = TransformStack.cast(ms);
        Vec3 axis = null;
        Vec3 diff = null;
        Vec3 normal = null;
        Vec3 offset = null;
        if (bezierPoint != null && world.getBlockEntity(pos) instanceof TrackBlockEntity trackBE) {
            BezierConnection bc = (BezierConnection) trackBE.connections.get(bezierPoint.curveTarget());
            if (bc == null) {
                return null;
            }
            double length = (double) Mth.floor(bc.getLength() * 2.0);
            int seg = bezierPoint.segment() + 1;
            double t = (double) seg / length;
            double tpre = (double) (seg - 1) / length;
            double tpost = (double) (seg + 1) / length;
            offset = bc.getPosition(t);
            normal = bc.getNormal(t);
            diff = bc.getPosition(tpost).subtract(bc.getPosition(tpre)).normalize();
            msr.translate(offset.subtract(Vec3.atBottomCenterOf(pos)));
            msr.translate(0.0, -0.25, 0.0);
        }
        if (normal == null) {
            axis = (Vec3) ((TrackShape) state.m_61143_(SHAPE)).getAxes().get(0);
            diff = axis.scale((double) direction.getStep()).normalize();
            normal = this.getUpNormal(world, pos, state);
        }
        Vec3 angles = TrackRenderer.getModelAngles(normal, diff);
        ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateYRadians(angles.y)).rotateXRadians(angles.x)).unCentre();
        if (axis != null) {
            msr.translate(0.0, axis.y != 0.0 ? 0.4375 : 0.0, axis.y != 0.0 ? (double) ((float) direction.getStep() * 2.5F / 16.0F) : 0.0);
        } else {
            msr.translate(0.0, 0.25, 0.0);
            if (direction == Direction.AxisDirection.NEGATIVE) {
                msr.rotateCentered(Direction.UP, (float) Math.PI);
            }
        }
        if (bezierPoint == null && world.getBlockEntity(pos) instanceof TrackBlockEntity trackTE && trackTE.isTilted()) {
            double yOffset = 0.0;
            for (BezierConnection bc : trackTE.connections.values()) {
                yOffset += bc.starts.getFirst().y - (double) pos.m_123342_();
            }
            ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateX((double) (-direction.getStep()) * (Double) trackTE.tilt.smoothingAngle.get())).unCentre()).translate(0.0, yOffset / 2.0, 0.0);
        }
        return switch(type) {
            case DUAL_SIGNAL ->
                AllPartialModels.TRACK_SIGNAL_DUAL_OVERLAY;
            case OBSERVER ->
                AllPartialModels.TRACK_OBSERVER_OVERLAY;
            case SIGNAL ->
                AllPartialModels.TRACK_SIGNAL_OVERLAY;
            case STATION ->
                AllPartialModels.TRACK_STATION_OVERLAY;
        };
    }

    @Override
    public boolean trackEquals(BlockState state1, BlockState state2) {
        return state1.m_60734_() == this && state2.m_60734_() == this && state1.m_61124_(HAS_BE, false) == state2.m_61124_(HAS_BE, false);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        int sameTypeTrackAmount = 1;
        Object2IntMap<TrackMaterial> otherTrackAmounts = new Object2IntArrayMap();
        int girderAmount = 0;
        if (be instanceof TrackBlockEntity track) {
            for (BezierConnection bezierConnection : track.getConnections().values()) {
                if (bezierConnection.isPrimary()) {
                    TrackMaterial material = bezierConnection.getMaterial();
                    if (material == this.getMaterial()) {
                        sameTypeTrackAmount += bezierConnection.getTrackItemCost();
                    } else {
                        otherTrackAmounts.put(material, otherTrackAmounts.getOrDefault(material, 0) + 1);
                    }
                    girderAmount += bezierConnection.getGirderItemCost();
                }
            }
        }
        List<ItemStack> stacks;
        for (stacks = new ArrayList(); sameTypeTrackAmount > 0; sameTypeTrackAmount -= 64) {
            stacks.add(new ItemStack(state.m_60734_(), Math.min(sameTypeTrackAmount, 64)));
        }
        ObjectIterator var11 = otherTrackAmounts.keySet().iterator();
        while (var11.hasNext()) {
            TrackMaterial material = (TrackMaterial) var11.next();
            for (int amt = otherTrackAmounts.getOrDefault(material, 0); amt > 0; amt -= 64) {
                stacks.add(material.asStack(Math.min(amt, 64)));
            }
        }
        while (girderAmount > 0) {
            stacks.add(AllBlocks.METAL_GIRDER.asStack(Math.min(girderAmount, 64)));
            girderAmount -= 64;
        }
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stacks);
    }

    @Override
    public TrackMaterial getMaterial() {
        return this.material;
    }

    public static class RenderProperties extends ReducedDestroyEffects implements MultiPosDestructionHandler {

        @Nullable
        @Override
        public Set<BlockPos> getExtraPositions(ClientLevel level, BlockPos pos, BlockState blockState, int progress) {
            return level.m_7702_(pos) instanceof TrackBlockEntity track ? new HashSet(track.connections.keySet()) : null;
        }
    }
}