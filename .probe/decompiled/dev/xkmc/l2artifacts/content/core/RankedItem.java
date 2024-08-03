package dev.xkmc.l2artifacts.content.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RankedItem extends Item {

    public final int rank;

    protected static Rarity getRarity(int rank) {
        return rank <= 2 ? Rarity.UNCOMMON : (rank <= 4 ? Rarity.RARE : Rarity.EPIC);
    }

    public RankedItem(Item.Properties props, int rank) {
        super(props.rarity(getRarity(rank)));
        this.rank = rank;
    }
}