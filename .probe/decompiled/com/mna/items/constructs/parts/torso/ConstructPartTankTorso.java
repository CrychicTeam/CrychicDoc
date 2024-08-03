package com.mna.items.constructs.parts.torso;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartTankTorso extends ItemConstructPart {

    public ConstructPartTankTorso(ConstructMaterial material) {
        super(material, ConstructSlot.TORSO, 8);
    }

    @Override
    public int getFluidCapacity() {
        return 8000;
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.FLUID_STORE };
    }
}