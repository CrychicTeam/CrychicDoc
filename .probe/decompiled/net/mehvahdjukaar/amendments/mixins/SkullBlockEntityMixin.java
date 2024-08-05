package net.mehvahdjukaar.amendments.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ SkullBlockEntity.class })
public abstract class SkullBlockEntityMixin extends BlockEntity {

    public SkullBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 0, -1), this.f_58858_.offset(1, 1, 1));
    }
}