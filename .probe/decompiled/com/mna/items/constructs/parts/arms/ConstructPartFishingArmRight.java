package com.mna.items.constructs.parts.arms;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;

public class ConstructPartFishingArmRight extends ItemConstructPart {

    public ConstructPartFishingArmRight(ConstructMaterial material) {
        super(material, ConstructSlot.RIGHT_ARM, 256);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.FISH, ConstructCapability.MELEE_ATTACK };
    }

    @Override
    public float getAttackDamage() {
        return 1.0F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 10;
    }
}