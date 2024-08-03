package com.mna.entities.models.constructs.modular;

import net.minecraft.resources.ResourceLocation;

public class ConstructMutexedModel {

    public final ResourceLocation rLoc;

    public final int mutex;

    public ConstructMutexedModel(ResourceLocation rLoc, int mutex) {
        this.rLoc = rLoc;
        this.mutex = mutex;
    }

    public boolean matchesMutex(int mutex) {
        return (this.mutex & mutex) != 0;
    }
}