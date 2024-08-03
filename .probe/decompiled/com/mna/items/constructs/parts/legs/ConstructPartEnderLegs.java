package com.mna.items.constructs.parts.legs;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartEnderLegs extends ItemConstructPart {

    public ConstructPartEnderLegs(ConstructMaterial material) {
        super(material, ConstructSlot.LEGS, 4);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.TELEPORT };
    }
}