package com.mna.items.constructs.parts.head;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartBasicHead extends ItemConstructPart {

    public ConstructPartBasicHead(ConstructMaterial material) {
        super(material, ConstructSlot.HEAD, 1);
    }

    @Override
    public int getIntelligenceBonus() {
        return this.getMaterial() == ConstructMaterial.WICKERWOOD ? -3 : 0;
    }
}