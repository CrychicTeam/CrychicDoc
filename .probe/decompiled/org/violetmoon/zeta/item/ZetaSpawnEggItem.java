package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaSpawnEggItem extends ForgeSpawnEggItem implements IZetaItem {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaSpawnEggItem(Supplier<EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, String regname, @Nullable ZetaModule module, Item.Properties properties) {
        super(type, primaryColor, secondaryColor, properties);
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerItem(this, regname);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.SPAWN_EGGS, this);
        }
    }

    public ZetaSpawnEggItem setCondition(BooleanSupplier enabledSupplier) {
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