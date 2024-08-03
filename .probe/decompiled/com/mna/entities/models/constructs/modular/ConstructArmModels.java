package com.mna.entities.models.constructs.modular;

import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.tools.RLoc;
import net.minecraft.resources.ResourceLocation;

public class ConstructArmModels extends ConstructModelCollection {

    private final boolean isLeft;

    public static final String AXE = "axe";

    public static final String BLADE = "blade";

    public static final String BLASTER = "blaster";

    public static final String GRABBER = "grabber";

    public static final String HAMMER = "hammer";

    public static final String NOZZLE = "nozzle";

    public static final String CASTER = "caster";

    public static final String SHIELD = "shield";

    public static final String FISHER = "fisher";

    public ConstructArmModels(boolean isLeft, String materialIdentifier) {
        super(materialIdentifier);
        this.isLeft = isLeft;
        this.defineModel("axe", 8);
        this.defineModel("blade", 4);
        this.defineModel("blaster", 16);
        this.defineModel("grabber", 1);
        this.defineModel("hammer", 2);
        this.defineModel("nozzle", 64);
        this.defineModel("caster", 128);
        this.defineModel("shield", 32);
        this.defineModel("fisher", 256);
    }

    @Override
    protected ResourceLocation getRLoc(String part_type) {
        return RLoc.create("construct/" + this.materialIdentifier + "/arm_" + part_type + "_" + (this.isLeft ? 108 : 114));
    }

    @Override
    public ConstructSlot getSlot() {
        return this.isLeft ? ConstructSlot.LEFT_ARM : ConstructSlot.RIGHT_ARM;
    }
}