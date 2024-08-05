package com.simibubi.create.content.logistics.crate;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class CreativeCrateBlockEntity extends CrateBlockEntity {

    FilteringBehaviour filtering;

    LazyOptional<IItemHandler> itemHandler;

    private BottomlessItemHandler inv;

    public CreativeCrateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inv = new BottomlessItemHandler(this.filtering::getFilter);
        this.itemHandler = LazyOptional.of(() -> this.inv);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.filtering = this.createFilter());
        this.filtering.setLabel(Lang.translateDirect("logistics.creative_crate.supply"));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (this.itemHandler != null) {
            this.itemHandler.invalidate();
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.itemHandler.cast() : super.getCapability(cap, side);
    }

    public FilteringBehaviour createFilter() {
        return new FilteringBehaviour(this, new ValueBoxTransform() {

            @Override
            public void rotate(BlockState state, PoseStack ms) {
                TransformStack.cast(ms).rotateX(90.0);
            }

            @Override
            public Vec3 getLocalOffset(BlockState state) {
                return new Vec3(0.5, 0.84375, 0.5);
            }

            @Override
            public float getScale() {
                return super.getScale();
            }
        });
    }
}