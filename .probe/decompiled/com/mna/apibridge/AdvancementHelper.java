package com.mna.apibridge;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.advancements.IAdvancementHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementHelper implements IAdvancementHelper {

    @Override
    public void triggerCompleteMultiblock(ServerPlayer player, ResourceLocation structureID) {
        CustomAdvancementTriggers.COMPLETE_MULTIBLOCK.trigger(player, structureID);
    }
}