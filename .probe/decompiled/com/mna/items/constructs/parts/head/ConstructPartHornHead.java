package com.mna.items.constructs.parts.head;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartHornHead extends ItemConstructPart {

    public ConstructPartHornHead(ConstructMaterial material) {
        super(material, ConstructSlot.HEAD, 8);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.TAUNT };
    }
}