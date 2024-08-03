package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.tools.RLoc;
import net.minecraft.resources.ResourceLocation;

public class ConstructPelvisModels extends ConstructModelCollection {

    public static final String ENDER = "ender";

    public ConstructPelvisModels(String materialIdentifier) {
        super(materialIdentifier);
        this.defineModel("ender", 4);
    }

    @Override
    protected ResourceLocation getRLoc(String part_type) {
        return RLoc.create("construct/" + this.materialIdentifier + "/pelvis_" + part_type);
    }

    @Override
    public ConstructSlot getSlot() {
        return ConstructSlot.LEGS;
    }
}