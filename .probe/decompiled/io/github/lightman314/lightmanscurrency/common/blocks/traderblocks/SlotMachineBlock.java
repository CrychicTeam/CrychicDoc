package io.github.lightman314.lightmanscurrency.common.blocks.traderblocks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import io.github.lightman314.lightmanscurrency.api.traders.blocks.TraderBlockTallRotatable;
import io.github.lightman314.lightmanscurrency.common.blockentity.trader.SlotMachineTraderBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.NonNullSupplier;

public class SlotMachineBlock extends TraderBlockTallRotatable {

    public static final ResourceLocation LIGHT_MODEL_LOCATION = new ResourceLocation("lightmanscurrency", "block/slot_machine/lights");

    public static final VoxelShape SHAPE_SOUTH = Shapes.or(m_49796_(0.0, 14.0, -1.0, 16.0, 16.0, 16.0), m_49796_(0.0, 0.0, 3.0, 16.0, 32.0, 16.0));

    public static final VoxelShape SHAPE_NORTH = Shapes.or(m_49796_(0.0, 14.0, 0.0, 16.0, 16.0, 17.0), m_49796_(0.0, 0.0, 0.0, 16.0, 32.0, 13.0));

    public static final VoxelShape SHAPE_EAST = Shapes.or(m_49796_(-1.0, 14.0, 0.0, 16.0, 16.0, 16.0), m_49796_(3.0, 0.0, 0.0, 16.0, 32.0, 16.0));

    public static final VoxelShape SHAPE_WEST = Shapes.or(m_49796_(0.0, 14.0, 0.0, 17.0, 16.0, 16.0), m_49796_(0.0, 0.0, 0.0, 13.0, 32.0, 16.0));

    public SlotMachineBlock(BlockBehaviour.Properties properties) {
        super(properties, LazyShapes.lazyTallDirectionalShape(SHAPE_NORTH, SHAPE_EAST, SHAPE_SOUTH, SHAPE_WEST));
    }

    @Override
    protected boolean isBlockOpaque() {
        return false;
    }

    @Override
    protected BlockEntity makeTrader(BlockPos pos, BlockState state) {
        return new SlotMachineTraderBlockEntity(pos, state);
    }

    @Override
    protected BlockEntityType<?> traderType() {
        return ModBlockEntities.SLOT_MACHINE_TRADER.get();
    }

    @Nullable
    public ResourceLocation getLightModel() {
        return LIGHT_MODEL_LOCATION;
    }

    @Override
    protected NonNullSupplier<List<Component>> getItemTooltips() {
        return LCText.TOOLTIP_SLOT_MACHINE.asTooltip();
    }
}