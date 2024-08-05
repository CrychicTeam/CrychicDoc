package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaVineBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;

public class CutVineBlock extends ZetaVineBlock implements IZetaBlockColorProvider {

    public CutVineBlock(@Nullable ZetaModule module) {
        super(module, "cut_vine", false);
    }

    @Override
    public boolean canSupportAtFace(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction dir) {
        if (dir == Direction.UP) {
            return super.m_57887_(level, pos, dir);
        } else {
            BooleanProperty booleanproperty = (BooleanProperty) f_57838_.get(dir);
            BlockState blockstate = level.getBlockState(pos.above());
            return blockstate.m_60713_(Blocks.VINE) && (Boolean) blockstate.m_61143_(booleanproperty);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Items.VINE);
    }

    @Nullable
    @Override
    public String getBlockColorProviderName() {
        return "vine";
    }

    @Nullable
    @Override
    public String getItemColorProviderName() {
        return "vine";
    }
}