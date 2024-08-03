package harmonised.pmmo.events.impl;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_ResetXP;
import harmonised.pmmo.network.clientpackets.CP_SyncVein;
import harmonised.pmmo.network.clientpackets.CP_UpdateExperience;
import harmonised.pmmo.network.clientpackets.CP_UpdateLevelCache;
import harmonised.pmmo.network.serverpackets.SP_SetVeinLimit;
import harmonised.pmmo.storage.PmmoSavedData;
import java.util.Map.Entry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;

public class LoginHandler {

    public static void handle(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Core core = Core.get(player.m_9236_());
        if (core.getSide().equals(LogicalSide.SERVER)) {
            Networking.sendToClient(new CP_ResetXP(), (ServerPlayer) player);
            for (Entry<String, Long> skillMap : core.getData().getXpMap(player.m_20148_()).entrySet()) {
                Networking.sendToClient(new CP_UpdateExperience((String) skillMap.getKey(), (Long) skillMap.getValue()), (ServerPlayer) player);
            }
            Networking.sendToClient(new CP_UpdateLevelCache(((PmmoSavedData) core.getData()).getLevelCache()), (ServerPlayer) player);
            Networking.sendToClient(new CP_SyncVein((double) VeinMiningLogic.getCurrentCharge(player)), (ServerPlayer) player);
            ((PmmoSavedData) core.getData()).awardScheduledXP(player.m_20148_());
        } else {
            Networking.sendToServer(new SP_SetVeinLimit(Config.VEIN_LIMIT.get()));
        }
    }
}