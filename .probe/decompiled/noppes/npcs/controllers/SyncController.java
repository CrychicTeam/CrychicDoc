package noppes.npcs.controllers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketSync;

public class SyncController {

    public static void syncPlayer(ServerPlayer player) {
        ListTag list = new ListTag();
        for (Faction faction : FactionController.instance.factions.values()) {
            list.add(faction.writeNBT(new CompoundTag()));
        }
        CompoundTag compound = new CompoundTag();
        compound.put("Data", list);
        Packets.send(player, new PacketSync(1, compound, true));
        for (QuestCategory category : QuestController.instance.categories.values()) {
            Packets.send(player, new PacketSync(3, category.writeNBT(new CompoundTag()), false));
        }
        Packets.send(player, new PacketSync(3, new CompoundTag(), true));
        for (DialogCategory category : DialogController.instance.categories.values()) {
            Packets.send(player, new PacketSync(5, category.writeNBT(new CompoundTag()), false));
        }
        Packets.send(player, new PacketSync(5, new CompoundTag(), true));
        list = new ListTag();
        for (RecipeCarpentry category : RecipeController.instance.globalRecipes.values()) {
            list.add(category.writeNBT());
            if (list.size() > 10) {
                compound = new CompoundTag();
                compound.put("Data", list);
                Packets.send(player, new PacketSync(6, compound, false));
                list = new ListTag();
            }
        }
        compound = new CompoundTag();
        compound.put("Data", list);
        Packets.send(player, new PacketSync(6, compound, true));
        list = new ListTag();
        for (RecipeCarpentry categoryx : RecipeController.instance.anvilRecipes.values()) {
            list.add(categoryx.writeNBT());
            if (list.size() > 10) {
                compound = new CompoundTag();
                compound.put("Data", list);
                Packets.send(player, new PacketSync(7, compound, false));
                list = new ListTag();
            }
        }
        compound = new CompoundTag();
        compound.put("Data", list);
        Packets.send(player, new PacketSync(7, compound, true));
        PlayerData data = PlayerData.get(player);
        Packets.send(player, new PacketSync(8, data.getNBT(), true));
    }

    public static void syncAllDialogs() {
        for (DialogCategory category : DialogController.instance.categories.values()) {
            Packets.sendAll(new PacketSync(5, category.writeNBT(new CompoundTag()), false));
        }
        Packets.sendAll(new PacketSync(5, new CompoundTag(), true));
    }

    public static void syncAllQuests() {
        for (QuestCategory category : QuestController.instance.categories.values()) {
            Packets.sendAll(new PacketSync(3, category.writeNBT(new CompoundTag()), false));
        }
        Packets.sendAll(new PacketSync(3, new CompoundTag(), true));
    }
}