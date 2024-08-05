package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieModel<T extends Zombie> extends AbstractZombieModel<T> {

    public ZombieModel(ModelPart modelPart0) {
        super(modelPart0);
    }

    public boolean isAggressive(T t0) {
        return t0.m_5912_();
    }
}