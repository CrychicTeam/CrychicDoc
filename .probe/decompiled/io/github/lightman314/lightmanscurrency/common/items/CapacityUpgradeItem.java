package io.github.lightman314.lightmanscurrency.common.items;

import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeData;
import io.github.lightman314.lightmanscurrency.common.upgrades.types.capacity.CapacityUpgrade;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;

public class CapacityUpgradeItem extends UpgradeItem {

    private final Supplier<Integer> capacityAmount;

    public CapacityUpgradeItem(CapacityUpgrade upgradeType, int capacityAmount, Item.Properties properties) {
        this(upgradeType, () -> capacityAmount, properties);
    }

    public CapacityUpgradeItem(CapacityUpgrade upgradeType, Supplier<Integer> capacityAmount, Item.Properties properties) {
        super(upgradeType, properties);
        this.capacityAmount = capacityAmount;
    }

    @Override
    public void fillUpgradeData(@Nonnull UpgradeData data) {
        data.setValue(CapacityUpgrade.CAPACITY, Math.max((Integer) this.capacityAmount.get(), 1));
    }
}