package net.mehvahdjukaar.moonlight.api.item.additional_placements;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AdditionalItemPlacement {

    private final Block placeable;

    public AdditionalItemPlacement(Block placeable) {
        this.placeable = placeable;
    }

    public static BlockPlacerItem getBlockPlacer() {
        return BlockPlacerItem.get();
    }

    @Nullable
    public BlockState overrideGetPlacementState(BlockPlaceContext pContext) {
        return getBlockPlacer().mimicGetPlacementState(pContext, this.placeable);
    }

    public InteractionResult overrideUseOn(UseOnContext pContext, FoodProperties foodProperties) {
        return getBlockPlacer().mimicUseOn(pContext, this.placeable, foodProperties);
    }

    public InteractionResult overridePlace(BlockPlaceContext pContext) {
        return getBlockPlacer().mimicPlace(pContext, this.placeable, null);
    }

    @Nullable
    public BlockPlaceContext overrideUpdatePlacementContext(BlockPlaceContext context) {
        return null;
    }

    public Block getPlacedBlock() {
        return this.placeable;
    }
}