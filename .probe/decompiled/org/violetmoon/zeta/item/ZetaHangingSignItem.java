package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaHangingSignItem extends HangingSignItem implements IZetaItem {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaHangingSignItem(@Nullable ZetaModule module, Block sign, Block wallSign) {
        super(sign, wallSign, new Item.Properties().stacksTo(16));
        this.module = module;
        if (module != null) {
            String resloc = module.zeta.registryUtil.inherit(sign, "%s");
            module.zeta.registry.registerItem(this, resloc);
            CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.FUNCTIONAL_BLOCKS, this, Items.CHEST, true);
        }
    }

    public ZetaHangingSignItem setCondition(BooleanSupplier enabledSupplier) {
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