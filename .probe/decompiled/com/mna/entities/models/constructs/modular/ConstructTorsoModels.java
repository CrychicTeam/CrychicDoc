package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.tools.RLoc;
import net.minecraft.resources.ResourceLocation;

public class ConstructTorsoModels extends ConstructModelCollection {

    public static final String BASIC = "basic";

    public static final String REINFORCED = "reinforced";

    public static final String STORAGE = "storage";

    public static final String MANA = "mana";

    public static final String TANK = "tank";

    public ConstructTorsoModels(String materialIdentifier) {
        super(materialIdentifier);
        this.defineModel("basic", 1);
        this.defineModel("reinforced", 2);
        this.defineModel("storage", 16);
        this.defineModel("mana", 4);
        this.defineModel("tank", 8);
    }

    @Override
    protected ResourceLocation getRLoc(String part_type) {
        return RLoc.create("construct/" + this.materialIdentifier + "/torso_" + part_type);
    }

    @Override
    public ConstructSlot getSlot() {
        return ConstructSlot.TORSO;
    }
}