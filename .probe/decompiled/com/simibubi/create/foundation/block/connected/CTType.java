package com.simibubi.create.foundation.block.connected;

import net.minecraft.resources.ResourceLocation;

public interface CTType {

    ResourceLocation getId();

    int getSheetSize();

    ConnectedTextureBehaviour.ContextRequirement getContextRequirement();

    int getTextureIndex(ConnectedTextureBehaviour.CTContext var1);
}