package org.violetmoon.quark.integration.lootr;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import org.violetmoon.quark.base.Quark;

public class LootrVariantChestBlockEntity extends LootrChestBlockEntity {

    public LootrVariantChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LootrVariantChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        this(Quark.LOOTR_INTEGRATION.chestTE(), pWorldPosition, pBlockState);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 0, -1), this.f_58858_.offset(2, 2, 2));
    }
}