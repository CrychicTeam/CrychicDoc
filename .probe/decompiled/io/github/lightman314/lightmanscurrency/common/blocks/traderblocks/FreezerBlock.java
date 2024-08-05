package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockTallRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.FreezerTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.traderblocks.interfaces.IItemTraderBlock;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.NonNullSupplier;

public class FreezerBlock extends TraderBlockTallRotatable implements IItemTraderBlock {

    public static final int TRADECOUNT = 8;

    public static final VoxelShape SHAPE_SOUTH = m_49796_(0.0, 0.0, 3.0, 16.0, 32.0, 16.0);

    public static final VoxelShape SHAPE_NORTH = m_49796_(0.0, 0.0, 0.0, 16.0, 32.0, 13.0);

    public static final VoxelShape SHAPE_EAST = m_49796_(3.0, 0.0, 0.0, 16.0, 32.0, 16.0);

    public static final VoxelShape SHAPE_WEST = m_49796_(0.0, 0.0, 0.0, 13.0, 32.0, 16.0);

    private final ResourceLocation doorModel;

    public FreezerBlock(BlockBehaviour.Properties properties, ResourceLocation doorModel) {
        super(properties, LazyShapes.lazyTallDirectionalShape(SHAPE_NORTH, SHAPE_EAST, SHAPE_SOUTH, SHAPE_WEST));
        this.doorModel = doorModel;
    }

    public ResourceLocation getDoorModel() {
        return this.doorModel;
    }

    public static ResourceLocation GenerateDoorModel(Color color) {
        return GenerateDoorModel("lightmanscurrency", color);
    }

    public static ResourceLocation GenerateDoorModel(String namespace, Color color) {
        return new ResourceLocation(namespace, "block/freezer/doors/" + color.toString().toLowerCase());
    }

    @Override
    public BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new FreezerTraderBlockEntity(pos, state, 8);
    }

    @Override
    public BlockEntityType<?> traderType() {
        return ModBlockEntities.FREEZER_TRADER.get();
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_ITEM_TRADER.asTooltip(8);
    }
}