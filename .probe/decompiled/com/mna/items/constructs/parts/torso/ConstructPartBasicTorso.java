package com.mna.items.constructs.parts.torso;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartBasicTorso extends ItemConstructPart {

    public ConstructPartBasicTorso(ConstructMaterial material) {
        super(material, ConstructSlot.TORSO, 1);
    }
}