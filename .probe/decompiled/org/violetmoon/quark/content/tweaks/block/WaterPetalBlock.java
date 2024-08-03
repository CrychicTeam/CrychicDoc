package org.violetmoon.quark.content.tweaks.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class WaterPetalBlock extends PinkPetalsBlock implements IZetaBlock {

    private final Item base;

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public WaterPetalBlock(Item base, String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(properties);
        this.base = base;
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname, false);
            if (module.category.isAddon()) {
                module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
            }
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(this.base);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState state = pLevel.m_8055_(blockpos);
        if (state.m_60734_() == Blocks.WATER) {
            FluidState fluid = pLevel.m_6425_(blockpos);
            return fluid.isSource();
        } else {
            return false;
        }
    }

    public WaterPetalBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }
}