package noppes.npcs.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityNPCInterface;

public class VisibilityController {

    public static VisibilityController instance = new VisibilityController();

    private Map<Integer, EntityNPCInterface> trackedEntityHashTable = new ConcurrentHashMap();

    public VisibilityController() {
        this.trackedEntityHashTable = new TreeMap();
    }

    public void trackNpc(EntityNPCInterface npc) {
        if (!npc.isClientSide()) {
            boolean hasOptions = npc.display.availability.hasOptions();
            if ((hasOptions || npc.display.getVisible() != 0) && !this.trackedEntityHashTable.containsKey(npc.m_19879_())) {
                this.trackedEntityHashTable.put(npc.m_19879_(), npc);
            }
            if (!hasOptions && npc.display.getVisible() == 0 && this.trackedEntityHashTable.containsKey(npc.m_19879_())) {
                this.trackedEntityHashTable.remove(npc.m_19879_());
            }
        }
    }

    public void remove(EntityNPCInterface npc) {
        if (!npc.isClientSide()) {
            this.trackedEntityHashTable.remove(npc.m_19879_());
        }
    }

    public void onUpdate(ServerPlayer player) {
        if (CustomNpcs.EnableInvisibleNpcs) {
            for (Entry<Integer, EntityNPCInterface> entry : this.trackedEntityHashTable.entrySet()) {
                checkIsVisible((EntityNPCInterface) entry.getValue(), player);
            }
        }
    }

    public static void checkIsVisible(EntityNPCInterface npc, ServerPlayer playerMP) {
        if (CustomNpcs.EnableInvisibleNpcs) {
            if (!npc.display.isVisibleTo(playerMP) && !playerMP.isSpectator() && playerMP.m_21205_().getItem() != CustomItems.wand) {
                npc.setInvisible(playerMP);
            } else {
                npc.setVisible(playerMP);
            }
        }
    }

    public static void addValue(HashMap<Integer, ArrayList<EntityNPCInterface>> map, int id, EntityNPCInterface npc) {
        if (!map.containsKey(id)) {
            map.put(id, new ArrayList());
        }
        ArrayList<EntityNPCInterface> npcs = (ArrayList<EntityNPCInterface>) map.get(id);
        if (!npcs.contains(npc)) {
            npcs.add(npc);
            map.replace(id, npcs);
        }
    }
}