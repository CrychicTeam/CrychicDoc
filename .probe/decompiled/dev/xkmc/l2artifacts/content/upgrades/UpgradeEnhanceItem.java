package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.core.RankedItem;
import net.minecraft.world.item.Item;

public abstract class UpgradeEnhanceItem extends RankedItem {

    public UpgradeEnhanceItem(Item.Properties props, int rank) {
        super(props, rank);
    }
}