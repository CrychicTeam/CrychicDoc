package noppes.npcs.controllers.data;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.EventHooks;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.FactionController;

public class PlayerFactionData {

    public HashMap<Integer, Integer> factionData = new HashMap();

    public void loadNBTData(CompoundTag compound) {
        HashMap<Integer, Integer> factionData = new HashMap();
        if (compound != null) {
            ListTag list = compound.getList("FactionData", 10);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    CompoundTag nbttagcompound = list.getCompound(i);
                    factionData.put(nbttagcompound.getInt("Faction"), nbttagcompound.getInt("Points"));
                }
                this.factionData = factionData;
            }
        }
    }

    public void saveNBTData(CompoundTag compound) {
        ListTag list = new ListTag();
        for (int faction : this.factionData.keySet()) {
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.putInt("Faction", faction);
            nbttagcompound.putInt("Points", (Integer) this.factionData.get(faction));
            list.add(nbttagcompound);
        }
        compound.put("FactionData", list);
    }

    public int getFactionPoints(Player player, int factionId) {
        Faction faction = FactionController.instance.getFaction(factionId);
        if (faction == null) {
            return 0;
        } else {
            if (!this.factionData.containsKey(factionId)) {
                if (player.m_9236_().isClientSide) {
                    this.factionData.put(factionId, faction.defaultPoints);
                    return faction.defaultPoints;
                }
                PlayerScriptData handler = PlayerData.get(player).scriptData;
                PlayerWrapper wrapper = (PlayerWrapper) NpcAPI.Instance().getIEntity(player);
                PlayerEvent.FactionUpdateEvent event = new PlayerEvent.FactionUpdateEvent(wrapper, faction, faction.defaultPoints, true);
                EventHooks.OnPlayerFactionChange(handler, event);
                this.factionData.put(factionId, event.points);
                PlayerData data = PlayerData.get(player);
                data.updateClient = true;
            }
            return (Integer) this.factionData.get(factionId);
        }
    }

    public void increasePoints(Player player, int factionId, int points) {
        Faction faction = FactionController.instance.getFaction(factionId);
        if (faction != null && player != null && !player.m_9236_().isClientSide) {
            PlayerScriptData handler = PlayerData.get(player).scriptData;
            PlayerWrapper wrapper = (PlayerWrapper) NpcAPI.Instance().getIEntity(player);
            if (!this.factionData.containsKey(factionId)) {
                PlayerEvent.FactionUpdateEvent event = new PlayerEvent.FactionUpdateEvent(wrapper, faction, faction.defaultPoints, true);
                EventHooks.OnPlayerFactionChange(handler, event);
                this.factionData.put(factionId, event.points);
            }
            PlayerEvent.FactionUpdateEvent event = new PlayerEvent.FactionUpdateEvent(wrapper, faction, points, false);
            EventHooks.OnPlayerFactionChange(handler, event);
            this.factionData.put(factionId, (Integer) this.factionData.get(factionId) + points);
        }
    }

    public CompoundTag getPlayerGuiData() {
        CompoundTag compound = new CompoundTag();
        this.saveNBTData(compound);
        ListTag list = new ListTag();
        for (int id : this.factionData.keySet()) {
            Faction faction = FactionController.instance.getFaction(id);
            if (faction != null && !faction.hideFaction) {
                CompoundTag com = new CompoundTag();
                faction.writeNBT(com);
                list.add(com);
            }
        }
        compound.put("FactionList", list);
        return compound;
    }
}