package com.mna.items.constructs.parts.head;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartSmartHead extends ItemConstructPart {

    public ConstructPartSmartHead(ConstructMaterial material) {
        super(material, ConstructSlot.HEAD, 4);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.ADVENTURE };
    }

    @Override
    public int getIntelligenceBonus() {
        return this.getMaterial().getIntelligenceBonus();
    }
}