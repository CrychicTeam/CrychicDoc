package net.blay09.mods.waystones.block.entity;

import net.blay09.mods.balm.api.block.entity.CustomRenderBoundingBox;
import net.blay09.mods.balm.common.BalmBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class PortstoneBlockEntity extends BalmBlockEntity implements CustomRenderBoundingBox {

    public PortstoneBlockEntity(BlockPos worldPosition, BlockState state) {
        super(ModBlockEntities.portstone.get(), worldPosition, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + 1), (double) (this.f_58858_.m_123342_() + 2), (double) (this.f_58858_.m_123343_() + 1));
    }
}