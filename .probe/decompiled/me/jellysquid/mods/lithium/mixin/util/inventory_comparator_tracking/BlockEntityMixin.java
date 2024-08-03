package me.jellysquid.mods.lithium.mixin.util.inventory_comparator_tracking;

import me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking.InventoryChangeTracker;
import me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking.ComparatorTracker;
import me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking.ComparatorTracking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BlockEntity.class })
public class BlockEntityMixin implements ComparatorTracker {

    @Shadow
    @Nullable
    protected Level level;

    @Shadow
    @Final
    protected BlockPos worldPosition;

    private static final byte UNKNOWN = -1;

    private static final byte COMPARATOR_PRESENT = 1;

    private static final byte COMPARATOR_ABSENT = 0;

    byte hasComparators;

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void init(BlockEntityType<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        this.hasComparators = -1;
    }

    @Override
    public void onComparatorAdded(Direction direction, int offset) {
        byte hasComparators = this.hasComparators;
        if (direction.getAxis() != Direction.Axis.Y && hasComparators != 1 && offset >= 1 && offset <= 2) {
            this.hasComparators = 1;
            if (this instanceof InventoryChangeTracker inventoryChangeTracker) {
                inventoryChangeTracker.emitFirstComparatorAdded();
            }
        }
    }

    @Override
    public boolean hasAnyComparatorNearby() {
        if (this.hasComparators == -1) {
            this.hasComparators = (byte) (ComparatorTracking.findNearbyComparators(this.level, this.worldPosition) ? 1 : 0);
        }
        return this.hasComparators == 1;
    }

    @Inject(method = { "markRemoved()V" }, at = { @At("HEAD") })
    private void forgetNearbyComparators(CallbackInfo ci) {
        this.hasComparators = -1;
    }
}