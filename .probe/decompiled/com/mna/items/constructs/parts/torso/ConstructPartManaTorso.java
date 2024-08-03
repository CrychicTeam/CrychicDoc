package com.mna.items.constructs.parts.torso;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartManaTorso extends ItemConstructPart {

    public ConstructPartManaTorso(ConstructMaterial material) {
        super(material, ConstructSlot.TORSO, 4);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.STORE_MANA };
    }

    @Override
    public int getManaCapacity() {
        return this.getMaterial().getManaStorage();
    }
}