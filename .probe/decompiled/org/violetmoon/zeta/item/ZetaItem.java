package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaItem extends Item implements IZetaItem, IZetaItemExtensions {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaItem(String regname, @Nullable ZetaModule module, Item.Properties properties) {
        super(properties);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerItem(this, regname);
            if (module.category.isAddon()) {
                module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
            }
        }
    }

    public ZetaItem setCondition(BooleanSupplier enabledSupplier) {
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