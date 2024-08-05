package com.mna.items.constructs.parts.torso;

import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartArmorTorso extends ItemConstructPart {

    public ConstructPartArmorTorso(ConstructMaterial material) {
        super(material, ConstructSlot.TORSO, 2);
    }

    @Override
    public int getArmor() {
        return this.getMaterial().getArmorBonus(this.getSlot());
    }

    @Override
    public int getToughness() {
        return this.getMaterial().getToughnessBonus(this.getSlot());
    }
}