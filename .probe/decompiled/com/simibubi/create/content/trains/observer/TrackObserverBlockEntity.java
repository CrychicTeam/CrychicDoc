package com.simibubi.create.content.trains.observer;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TrackObserverBlockEntity extends SmartBlockEntity implements ITransformableBlockEntity {

    public TrackTargetingBehaviour<TrackObserver> edgePoint;

    private FilteringBehaviour filtering;

    public TrackObserverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.edgePoint = new TrackTargetingBehaviour<>(this, EdgePointType.OBSERVER));
        behaviours.add(this.filtering = this.createFilter().withCallback(this::onFilterChanged));
        this.filtering.setLabel(Lang.translateDirect("logistics.train_observer.cargo_filter"));
    }

    private void onFilterChanged(ItemStack newFilter) {
        if (!this.f_58857_.isClientSide()) {
            TrackObserver observer = this.getObserver();
            if (observer != null) {
                observer.setFilterAndNotify(this.f_58857_, newFilter);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide()) {
            boolean shouldBePowered = false;
            TrackObserver observer = this.getObserver();
            if (observer != null) {
                shouldBePowered = observer.isActivated();
            }
            if (this.isBlockPowered() != shouldBePowered) {
                BlockState blockState = this.m_58900_();
                if (blockState.m_61138_(TrackObserverBlock.POWERED)) {
                    this.f_58857_.setBlock(this.f_58858_, (BlockState) blockState.m_61124_(TrackObserverBlock.POWERED, shouldBePowered), 3);
                }
                DisplayLinkBlock.notifyGatherers(this.f_58857_, this.f_58858_);
            }
        }
    }

    @Nullable
    public TrackObserver getObserver() {
        return this.edgePoint.getEdgePoint();
    }

    public ItemStack getFilter() {
        return this.filtering.getFilter();
    }

    public boolean isBlockPowered() {
        return (Boolean) this.m_58900_().m_61145_(TrackObserverBlock.POWERED).orElse(false);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_, this.edgePoint.getGlobalPosition()).inflate(2.0);
    }

    @Override
    public void transform(StructureTransform transform) {
        this.edgePoint.transform(transform);
    }

    public FilteringBehaviour createFilter() {
        return new FilteringBehaviour(this, new ValueBoxTransform() {

            @Override
            public void rotate(BlockState state, PoseStack ms) {
                TransformStack.cast(ms).rotateX(90.0);
            }

            @Override
            public Vec3 getLocalOffset(BlockState state) {
                return new Vec3(0.5, 0.96875, 0.5);
            }
        });
    }
}