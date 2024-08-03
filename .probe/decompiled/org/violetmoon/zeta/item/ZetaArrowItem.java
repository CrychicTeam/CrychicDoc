package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

public abstract class ZetaArrowItem extends ArrowItem implements IZetaItem {

    @Nullable
    private final ZetaModule module;

    private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

    public ZetaArrowItem(String name, @Nullable ZetaModule module) {
        super(new Item.Properties());
        this.module = module;
        if (module != null) {
            module.zeta.registry.registerItem(this, name);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.COMBAT, this);
        }
    }

    public ZetaArrowItem setCondition(BooleanSupplier enabledSupplier) {
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

    public static class Impl extends ZetaArrowItem {

        private final ZetaArrowItem.Impl.ArrowCreator creator;

        public Impl(String name, ZetaModule module, ZetaArrowItem.Impl.ArrowCreator creator) {
            super(name, module);
            this.creator = creator;
        }

        @Override
        public AbstractArrow createArrow(Level level0, ItemStack itemStack1, LivingEntity livingEntity2) {
            return this.creator.createArrow(level0, itemStack1, livingEntity2);
        }

        public interface ArrowCreator {

            AbstractArrow createArrow(Level var1, ItemStack var2, LivingEntity var3);
        }
    }
}