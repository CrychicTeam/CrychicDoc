package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaDoubleHighBlockItem extends DoubleHighBlockItem implements IZetaItem {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaDoubleHighBlockItem(IZetaBlock baseBlock, Item.Properties props) {
        super(baseBlock.getBlock(), props);
        this.module = baseBlock.getModule();
    }

    public ZetaDoubleHighBlockItem setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    @Override
    public boolean doesConditionApply() {
        return this.enabledSupplier.getAsBoolean();
    }
}