package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.IAugment;
import net.minecraft.resources.ResourceLocation;

public class AugmentBase implements IAugment {

    ResourceLocation name;

    double cost;

    public AugmentBase(ResourceLocation name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    @Override
    public ResourceLocation getName() {
        return this.name;
    }

    @Override
    public double getCost() {
        return this.cost;
    }
}