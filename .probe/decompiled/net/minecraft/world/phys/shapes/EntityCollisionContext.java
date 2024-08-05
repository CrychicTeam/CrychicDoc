package net.minecraft.world.phys.shapes;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;

public class EntityCollisionContext implements CollisionContext {

    protected static final CollisionContext EMPTY = new EntityCollisionContext(false, -Double.MAX_VALUE, ItemStack.EMPTY, p_205118_ -> false, null) {

        @Override
        public boolean isAbove(VoxelShape p_82898_, BlockPos p_82899_, boolean p_82900_) {
            return p_82900_;
        }
    };

    private final boolean descending;

    private final double entityBottom;

    private final ItemStack heldItem;

    private final Predicate<FluidState> canStandOnFluid;

    @Nullable
    private final Entity entity;

    protected EntityCollisionContext(boolean boolean0, double double1, ItemStack itemStack2, Predicate<FluidState> predicateFluidState3, @Nullable Entity entity4) {
        this.descending = boolean0;
        this.entityBottom = double1;
        this.heldItem = itemStack2;
        this.canStandOnFluid = predicateFluidState3;
        this.entity = entity4;
    }

    @Deprecated
    protected EntityCollisionContext(Entity entity0) {
        this(entity0.isDescending(), entity0.getY(), entity0 instanceof LivingEntity ? ((LivingEntity) entity0).getMainHandItem() : ItemStack.EMPTY, entity0 instanceof LivingEntity ? ((LivingEntity) entity0)::m_203441_ : p_205113_ -> false, entity0);
    }

    @Override
    public boolean isHoldingItem(Item item0) {
        return this.heldItem.is(item0);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState0, FluidState fluidState1) {
        return this.canStandOnFluid.test(fluidState1) && !fluidState0.getType().isSame(fluidState1.getType());
    }

    @Override
    public boolean isDescending() {
        return this.descending;
    }

    @Override
    public boolean isAbove(VoxelShape voxelShape0, BlockPos blockPos1, boolean boolean2) {
        return this.entityBottom > (double) blockPos1.m_123342_() + voxelShape0.max(Direction.Axis.Y) - 1.0E-5F;
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }
}