package com.mna.items.constructs.parts.legs;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartBasicLegs extends ItemConstructPart {

    public ConstructPartBasicLegs(ConstructMaterial material) {
        super(material, ConstructSlot.LEGS, 1);
    }
}