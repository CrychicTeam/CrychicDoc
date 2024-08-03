package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.tools.RLoc;
import net.minecraft.resources.ResourceLocation;

public class ConstructHeadModels extends ConstructModelCollection {

    public static final String BASIC = "basic";

    public static final String DOOT = "doot";

    public static final String SMART = "smart";

    public ConstructHeadModels(String materialIdentifier) {
        super(materialIdentifier);
        this.defineModel("basic", 1);
        this.defineModel("doot", 8);
        this.defineModel("smart", 4);
    }

    @Override
    protected ResourceLocation getRLoc(String part_type) {
        return RLoc.create("construct/" + this.materialIdentifier + "/head_" + part_type);
    }

    @Override
    public ConstructSlot getSlot() {
        return ConstructSlot.HEAD;
    }
}