package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Giant;

public class GiantZombieModel extends AbstractZombieModel<Giant> {

    public GiantZombieModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public boolean isAggressive(Giant giant0) {
        return false;
    }
}