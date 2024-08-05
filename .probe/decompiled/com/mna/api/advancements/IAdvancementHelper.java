package com.mna.api.advancements;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface IAdvancementHelper {

    void triggerCompleteMultiblock(ServerPlayer var1, ResourceLocation var2);
}