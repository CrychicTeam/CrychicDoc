package net.mehvahdjukaar.amendments.common.item.placement;

import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacement;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class WallLanternPlacement extends AdditionalItemPlacement {

    public WallLanternPlacement() {
        super((Block) ModRegistry.WALL_LANTERN.get());
    }

    @Override
    public InteractionResult overridePlace(BlockPlaceContext pContext) {
        if (CompatHandler.TORCHSLAB) {
            double y = pContext.m_43720_().y() % 1.0;
            if (y < 0.5) {
                return null;
            }
        }
        return super.overridePlace(pContext);
    }
}