package com.simibubi.create.content.trains.bogey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageBogey;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.track.TrackMaterial;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBogeyBlock<T extends AbstractBogeyBlockEntity> extends Block implements IBE<T>, ProperWaterloggedBlock, ISpecialBlockItemRequirement, IWrenchable {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    static final List<ResourceLocation> BOGEYS = new ArrayList();

    public BogeySizes.BogeySize size;

    static final EnumSet<Direction> STICKY_X = EnumSet.of(Direction.EAST, Direction.WEST);

    static final EnumSet<Direction> STICKY_Z = EnumSet.of(Direction.SOUTH, Direction.NORTH);

    public AbstractBogeyBlock(BlockBehaviour.Properties pProperties, BogeySizes.BogeySize size) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
        this.size = size;
    }

    public boolean isOnIncompatibleTrack(Carriage carriage, boolean leading) {
        TravellingPoint point = leading ? carriage.getLeadingPoint() : carriage.getTrailingPoint();
        CarriageBogey bogey = leading ? carriage.leadingBogey() : carriage.trailingBogey();
        TrackEdge currentEdge = point.edge;
        return currentEdge == null ? false : currentEdge.getTrackMaterial().trackType != this.getTrackType(bogey.getStyle());
    }

    public Set<TrackMaterial.TrackType> getValidPathfindingTypes(BogeyStyle style) {
        return ImmutableSet.of(this.getTrackType(style));
    }

    public abstract TrackMaterial.TrackType getTrackType(BogeyStyle var1);

    @Deprecated
    public static void registerStandardBogey(ResourceLocation block) {
        BOGEYS.add(block);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    public EnumSet<Direction> getStickySurfaces(BlockGetter world, BlockPos pos, BlockState state) {
        return state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X ? STICKY_X : STICKY_Z;
    }

    public abstract double getWheelPointSpacing();

    public abstract double getWheelRadius();

    public Vec3 getConnectorAnchorOffset(boolean upsideDown) {
        return this.getConnectorAnchorOffset();
    }

    protected abstract Vec3 getConnectorAnchorOffset();

    public boolean allowsSingleBogeyCarriage() {
        return true;
    }

    public abstract BogeyStyle getDefaultStyle();

    public boolean captureBlockEntityForTrain() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(@Nullable BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay, BogeyStyle style, CompoundTag bogeyData) {
        if (style == null) {
            style = this.getDefaultStyle();
        }
        Optional<BogeyRenderer.CommonRenderer> commonRenderer = style.getInWorldCommonRenderInstance();
        BogeyRenderer renderer = style.getInWorldRenderInstance(this.getSize());
        if (state != null) {
            ms.translate(0.5F, 0.5F, 0.5F);
            if (state.m_61143_(AXIS) == Direction.Axis.X) {
                ms.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
        }
        ms.translate(0.0, -1.5078125, 0.0);
        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());
        if (bogeyData == null) {
            bogeyData = new CompoundTag();
        }
        renderer.render(bogeyData, wheelAngle, ms, light, vb, state == null);
        CompoundTag finalBogeyData = bogeyData;
        commonRenderer.ifPresent(common -> common.render(finalBogeyData, wheelAngle, ms, light, vb, state == null));
    }

    public BogeySizes.BogeySize getSize() {
        return this.size;
    }

    public Direction getBogeyUpDirection() {
        return Direction.UP;
    }

    public boolean isTrackAxisAlongFirstCoordinate(BlockState state) {
        return state.m_61143_(AXIS) == Direction.Axis.X;
    }

    @Nullable
    public BlockState getMatchingBogey(Direction upDirection, boolean axisAlongFirst) {
        return upDirection != Direction.UP ? null : (BlockState) this.m_49966_().m_61124_(AXIS, axisAlongFirst ? Direction.Axis.X : Direction.Axis.Z);
    }

    @Override
    public final InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = player.m_21120_(hand);
            if (!player.m_6144_() && stack.is((Item) AllItems.WRENCH.get()) && !player.getCooldowns().isOnCooldown(stack.getItem()) && AllBogeyStyles.BOGEY_STYLES.size() > 1) {
                if (!(level.getBlockEntity(pos) instanceof AbstractBogeyBlockEntity sbbe)) {
                    return InteractionResult.FAIL;
                } else {
                    player.getCooldowns().addCooldown(stack.getItem(), 20);
                    BogeyStyle currentStyle = sbbe.getStyle();
                    BogeySizes.BogeySize size = this.getSize();
                    BogeyStyle style = this.getNextStyle(currentStyle);
                    if (style == currentStyle) {
                        return InteractionResult.PASS;
                    } else {
                        Set<BogeySizes.BogeySize> validSizes = style.validSizes();
                        for (int i = 0; i < BogeySizes.count() && !validSizes.contains(size); i++) {
                            size = size.increment();
                        }
                        sbbe.setBogeyStyle(style);
                        CompoundTag defaultData = style.defaultData;
                        sbbe.setBogeyData(sbbe.getBogeyData().merge(defaultData));
                        if (size == this.getSize()) {
                            player.displayClientMessage(Lang.translateDirect("bogey.style.updated_style").append(": ").append(style.displayName), true);
                        } else {
                            CompoundTag oldData = sbbe.getBogeyData();
                            level.setBlock(pos, this.getStateOfSize(sbbe, size), 3);
                            if (!(level.getBlockEntity(pos) instanceof AbstractBogeyBlockEntity newBlockEntity1)) {
                                return InteractionResult.FAIL;
                            }
                            newBlockEntity1.setBogeyData(oldData);
                            player.displayClientMessage(Lang.translateDirect("bogey.style.updated_style_and_size").append(": ").append(style.displayName), true);
                        }
                        return InteractionResult.CONSUME;
                    }
                }
            } else {
                return this.onInteractWithBogey(state, level, pos, player, hand, hit);
            }
        }
    }

    protected InteractionResult onInteractWithBogey(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    protected List<ResourceLocation> getBogeyBlockCycle() {
        return BOGEYS;
    }

    @Override
    public BlockState getRotatedBlockState(BlockState state, Direction targetedFace) {
        Block block = state.m_60734_();
        List<ResourceLocation> bogeyCycle = this.getBogeyBlockCycle();
        int indexOf = bogeyCycle.indexOf(RegisteredObjects.getKeyOrThrow(block));
        if (indexOf == -1) {
            return state;
        } else {
            int index = (indexOf + 1) % bogeyCycle.size();
            Direction bogeyUpDirection = this.getBogeyUpDirection();
            for (boolean trackAxisAlongFirstCoordinate = this.isTrackAxisAlongFirstCoordinate(state); index != indexOf; index = (index + 1) % bogeyCycle.size()) {
                ResourceLocation id = (ResourceLocation) bogeyCycle.get(index);
                Block newBlock = ForgeRegistries.BLOCKS.getValue(id);
                if (newBlock instanceof AbstractBogeyBlock<?> bogey) {
                    BlockState matchingBogey = bogey.getMatchingBogey(bogeyUpDirection, trackAxisAlongFirstCoordinate);
                    if (matchingBogey != null) {
                        return this.copyProperties(state, matchingBogey);
                    }
                }
            }
            return state;
        }
    }

    public BlockState getNextSize(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof AbstractBogeyBlockEntity sbbe ? this.getNextSize(sbbe) : level.getBlockState(pos);
    }

    public List<Property<?>> propertiesToCopy() {
        return ImmutableList.of(WATERLOGGED, AXIS);
    }

    private <V extends Comparable<V>> BlockState copyProperty(BlockState source, BlockState target, Property<V> property) {
        return source.m_61138_(property) && target.m_61138_(property) ? (BlockState) target.m_61124_(property, source.m_61143_(property)) : target;
    }

    private BlockState copyProperties(BlockState source, BlockState target) {
        for (Property<?> property : this.propertiesToCopy()) {
            target = this.copyProperty(source, target, property);
        }
        return target;
    }

    public BlockState getNextSize(AbstractBogeyBlockEntity sbte) {
        BogeySizes.BogeySize size = this.getSize();
        BogeyStyle style = sbte.getStyle();
        BlockState nextBlock = style.getNextBlock(size).defaultBlockState();
        return this.copyProperties(sbte.m_58900_(), nextBlock);
    }

    public BlockState getStateOfSize(AbstractBogeyBlockEntity sbte, BogeySizes.BogeySize size) {
        BogeyStyle style = sbte.getStyle();
        BlockState state = style.getBlockOfSize(size).defaultBlockState();
        return this.copyProperties(sbte.m_58900_(), state);
    }

    public BogeyStyle getNextStyle(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof AbstractBogeyBlockEntity sbbe ? this.getNextStyle(sbbe.getStyle()) : this.getDefaultStyle();
    }

    public BogeyStyle getNextStyle(BogeyStyle style) {
        Collection<BogeyStyle> allStyles = style.getCycleGroup().values();
        if (allStyles.size() <= 1) {
            return style;
        } else {
            List<BogeyStyle> list = new ArrayList(allStyles);
            return Iterate.cycleValue(list, style);
        }
    }

    @NotNull
    @Override
    public BlockState rotate(@NotNull BlockState pState, Rotation pRotation) {
        return switch(pRotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 ->
                (BlockState) pState.m_61122_(AXIS);
            default ->
                pState;
        };
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, AllBlocks.RAILWAY_CASING.asStack());
    }

    public boolean canBeUpsideDown() {
        return false;
    }

    public boolean isUpsideDown(BlockState state) {
        return false;
    }

    public BlockState getVersion(BlockState base, boolean upsideDown) {
        return base;
    }
}