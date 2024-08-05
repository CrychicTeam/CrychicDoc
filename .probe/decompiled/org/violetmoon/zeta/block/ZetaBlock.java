package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaBlock extends Block implements IZetaBlock {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(properties);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerBlock(this, regname);
            if (module.category.isAddon()) {
                module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
            }
        }
    }

    public ZetaBlock setCondition(BooleanSupplier enabledSupplier) {
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

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> thisType, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
        return targetType == thisType ? ticker : null;
    }

    public interface Constructor<T extends Block> {

        T make(String var1, ZetaModule var2, BlockBehaviour.Properties var3);
    }
}