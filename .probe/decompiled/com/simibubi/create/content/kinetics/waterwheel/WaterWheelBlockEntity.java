package com.simibubi.create.content.kinetics.waterwheel;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WaterWheelBlockEntity extends GeneratingKineticBlockEntity {

    public static final Map<Direction.Axis, Set<BlockPos>> SMALL_OFFSETS = new EnumMap(Direction.Axis.class);

    public static final Map<Direction.Axis, Set<BlockPos>> LARGE_OFFSETS = new EnumMap(Direction.Axis.class);

    public int flowScore;

    public BlockState material = Blocks.SPRUCE_PLANKS.defaultBlockState();

    public WaterWheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(60);
    }

    protected int getSize() {
        return 1;
    }

    protected Set<BlockPos> getOffsetsToCheck() {
        return (Set<BlockPos>) (this.getSize() == 1 ? SMALL_OFFSETS : LARGE_OFFSETS).get(this.getAxis());
    }

    public InteractionResult applyMaterialIfValid(ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            BlockState material = blockItem.getBlock().defaultBlockState();
            if (material == this.material) {
                return InteractionResult.PASS;
            } else if (!material.m_204336_(BlockTags.PLANKS)) {
                return InteractionResult.PASS;
            } else if (this.f_58857_.isClientSide() && !this.isVirtual()) {
                return InteractionResult.SUCCESS;
            } else {
                this.material = material;
                this.notifyUpdate();
                this.f_58857_.m_46796_(2001, this.f_58858_, Block.getId(material));
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    protected Direction.Axis getAxis() {
        Direction.Axis axis = Direction.Axis.X;
        BlockState blockState = this.m_58900_();
        if (blockState.m_60734_() instanceof IRotate irotate) {
            axis = irotate.getRotationAxis(blockState);
        }
        return axis;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.determineAndApplyFlowScore();
    }

    public void determineAndApplyFlowScore() {
        Vec3 wheelPlane = Vec3.atLowerCornerOf(new Vec3i(1, 1, 1).subtract(Direction.get(Direction.AxisDirection.POSITIVE, this.getAxis()).getNormal()));
        int flowScore = 0;
        boolean lava = false;
        for (BlockPos blockPos : this.getOffsetsToCheck()) {
            BlockPos targetPos = blockPos.offset(this.f_58858_);
            Vec3 flowAtPos = this.getFlowVectorAtPosition(targetPos).multiply(wheelPlane);
            lava |= FluidHelper.isLava(this.f_58857_.getFluidState(targetPos).getType());
            if (flowAtPos.lengthSqr() != 0.0) {
                flowAtPos = flowAtPos.normalize();
                Vec3 normal = Vec3.atLowerCornerOf(blockPos).normalize();
                Vec3 positiveMotion = VecHelper.rotate(normal, 90.0, this.getAxis());
                double dot = flowAtPos.dot(positiveMotion);
                if (Math.abs(dot) > 0.5) {
                    flowScore = (int) ((double) flowScore + Math.signum(dot));
                }
            }
        }
        if (flowScore != 0 && !this.f_58857_.isClientSide()) {
            this.award(lava ? AllAdvancements.LAVA_WHEEL : AllAdvancements.WATER_WHEEL);
        }
        this.setFlowScoreAndUpdate(flowScore);
    }

    public Vec3 getFlowVectorAtPosition(BlockPos pos) {
        FluidState fluid = this.f_58857_.getFluidState(pos);
        Vec3 vec = fluid.getFlow(this.f_58857_, pos);
        BlockState blockState = this.f_58857_.getBlockState(pos);
        if (blockState.m_60734_() == Blocks.BUBBLE_COLUMN) {
            vec = new Vec3(0.0, blockState.m_61143_(BubbleColumnBlock.DRAG_DOWN) ? -1.0 : 1.0, 0.0);
        }
        return vec;
    }

    public void setFlowScoreAndUpdate(int score) {
        if (this.flowScore != score) {
            this.flowScore = score;
            this.updateGeneratedRotation();
            this.m_6596_();
        }
    }

    private void redraw() {
        if (!this.isVirtual()) {
            this.requestModelDataUpdate();
        }
        if (this.m_58898_()) {
            this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 16);
            this.f_58857_.m_7726_().getLightEngine().checkBlock(this.f_58858_);
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.LAVA_WHEEL, AllAdvancements.WATER_WHEEL });
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.flowScore = compound.getInt("FlowScore");
        BlockState prevMaterial = this.material;
        if (compound.contains("Material")) {
            this.material = NbtUtils.readBlockState(this.blockHolderGetter(), compound.getCompound("Material"));
            if (this.material.m_60795_()) {
                this.material = Blocks.SPRUCE_PLANKS.defaultBlockState();
            }
            if (clientPacket && prevMaterial != this.material) {
                this.redraw();
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("FlowScore", this.flowScore);
        compound.put("Material", NbtUtils.writeBlockState(this.material));
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).inflate((double) this.getSize());
    }

    @Override
    public float getGeneratedSpeed() {
        return (float) (Mth.clamp(this.flowScore, -1, 1) * 8 / this.getSize());
    }

    static {
        for (Direction.Axis axis : Iterate.axes) {
            HashSet<BlockPos> offsets = new HashSet();
            for (Direction d : Iterate.directions) {
                if (d.getAxis() != axis) {
                    offsets.add(BlockPos.ZERO.relative(d));
                }
            }
            SMALL_OFFSETS.put(axis, offsets);
            offsets = new HashSet();
            for (Direction dx : Iterate.directions) {
                if (dx.getAxis() != axis) {
                    BlockPos centralOffset = BlockPos.ZERO.relative(dx, 2);
                    offsets.add(centralOffset);
                    for (Direction d2 : Iterate.directions) {
                        if (d2.getAxis() != axis && d2.getAxis() != dx.getAxis()) {
                            offsets.add(centralOffset.relative(d2));
                        }
                    }
                }
            }
            LARGE_OFFSETS.put(axis, offsets);
        }
    }
}