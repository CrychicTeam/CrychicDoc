package com.mna.items.constructs.parts.torso;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartStorageTorso extends ItemConstructPart {

    public ConstructPartStorageTorso(ConstructMaterial material) {
        super(material, ConstructSlot.TORSO, 16);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.ITEM_STORAGE };
    }

    @Override
    public int getInventorySizeBonus() {
        return 9;
    }

    @Override
    public int getBackpackCapacityBoost() {
        return this.getMaterial().getBackpackCapacityBoost();
    }
}