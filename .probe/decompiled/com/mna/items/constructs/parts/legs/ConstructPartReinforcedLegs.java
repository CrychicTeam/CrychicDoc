package com.mna.items.constructs.parts.legs;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartReinforcedLegs extends ItemConstructPart {

    public ConstructPartReinforcedLegs(ConstructMaterial material) {
        super(material, ConstructSlot.LEGS, 2);
    }

    @Override
    public int getArmor() {
        return this.getMaterial().getArmorBonus(this.getSlot());
    }

    @Override
    public int getToughness() {
        return this.getMaterial().getToughnessBonus(this.getSlot());
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.CARRY_PLAYER };
    }
}