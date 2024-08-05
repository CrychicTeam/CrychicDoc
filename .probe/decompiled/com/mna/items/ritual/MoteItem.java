package com.mna.items.ritual;

import com.mna.api.affinity.Affinity;
import com.mna.api.items.TieredItem;
import net.minecraft.world.item.Item;

public class MoteItem extends TieredItem {

    private final Affinity relatedAffinity;

    public MoteItem(Affinity related, Item.Properties itemProperties) {
        super(itemProperties);
        this.relatedAffinity = related;
    }

    public Affinity getRelatedAffinity() {
        return this.relatedAffinity;
    }
}