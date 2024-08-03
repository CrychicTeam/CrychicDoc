package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.MsLoggy;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;

public class DimensionTravelHandler {

    public static void handle(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!Core.get(player.m_9236_()).isActionPermitted(ReqType.TRAVEL, event.getDimension(), player)) {
                Map<String, Integer> req = Core.get(player.m_9236_()).getObjectSkillMap(ObjectType.DIMENSION, event.getDimension().location(), ReqType.TRAVEL, new CompoundTag());
                Messenger.sendDenialMsg(ReqType.TRAVEL, player, event.getDimension().location().toString(), MsLoggy.mapToString(req));
                event.setCanceled(true);
            }
        }
    }
}