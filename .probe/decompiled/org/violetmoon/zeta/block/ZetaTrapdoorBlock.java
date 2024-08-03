package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.RenderLayerRegistry;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaTrapdoorBlock extends TrapDoorBlock implements IZetaBlock {

    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaTrapdoorBlock(BlockSetType setType, String regname, ZetaModule module, BlockBehaviour.Properties properties) {
        super(properties, setType);
        this.module = module;
        if (module == null) {
            throw new IllegalArgumentException("Must provide a module for ZetaTrapdoorBlock");
        } else {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            module.zeta.registry.registerBlock(this, regname, true);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, this);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS, this);
        }
    }

    @Override
    public boolean isLadderZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        if ((Boolean) state.m_61143_(f_57514_)) {
            BlockPos downPos = pos.below();
            BlockState down = level.m_8055_(downPos);
            return this.module.zeta.blockExtensions.get(down).makesOpenTrapdoorAboveClimbableZeta(down, level, downPos, state);
        } else {
            return false;
        }
    }

    public ZetaTrapdoorBlock setCondition(BooleanSupplier enabledSupplier) {
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