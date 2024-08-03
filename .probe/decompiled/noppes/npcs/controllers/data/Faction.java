package noppes.npcs.controllers.data;

import java.util.HashSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.entity.EntityNPCInterface;

public class Faction implements IFaction {

    public String name = "";

    public int color = Integer.parseInt("FF00", 16);

    public HashSet<Integer> attackFactions;

    public int id = -1;

    public int neutralPoints = 500;

    public int friendlyPoints = 1500;

    public int defaultPoints = 1000;

    public boolean hideFaction = false;

    public boolean getsAttacked = false;

    public Faction() {
        this.attackFactions = new HashSet();
    }

    public Faction(int id, String name, int color, int defaultPoints) {
        this.name = name;
        this.color = color;
        this.defaultPoints = defaultPoints;
        this.id = id;
        this.attackFactions = new HashSet();
    }

    public static String formatName(String name) {
        name = name.toLowerCase().trim();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void readNBT(CompoundTag compound) {
        this.name = compound.getString("Name");
        this.color = compound.getInt("Color");
        this.id = compound.getInt("Slot");
        this.neutralPoints = compound.getInt("NeutralPoints");
        this.friendlyPoints = compound.getInt("FriendlyPoints");
        this.defaultPoints = compound.getInt("DefaultPoints");
        this.hideFaction = compound.getBoolean("HideFaction");
        this.getsAttacked = compound.getBoolean("GetsAttacked");
        this.attackFactions = NBTTags.getIntegerSet(compound.getList("AttackFactions", 10));
    }

    public CompoundTag writeNBT(CompoundTag compound) {
        compound.putInt("Slot", this.id);
        compound.putString("Name", this.name);
        compound.putInt("Color", this.color);
        compound.putInt("NeutralPoints", this.neutralPoints);
        compound.putInt("FriendlyPoints", this.friendlyPoints);
        compound.putInt("DefaultPoints", this.defaultPoints);
        compound.putBoolean("HideFaction", this.hideFaction);
        compound.putBoolean("GetsAttacked", this.getsAttacked);
        compound.put("AttackFactions", NBTTags.nbtIntegerCollection(this.attackFactions));
        return compound;
    }

    public boolean isFriendlyToPlayer(Player player) {
        PlayerFactionData data = PlayerData.get(player).factionData;
        return data.getFactionPoints(player, this.id) >= this.friendlyPoints;
    }

    public boolean isAggressiveToPlayer(Player player) {
        if (player.getAbilities().instabuild) {
            return false;
        } else {
            PlayerFactionData data = PlayerData.get(player).factionData;
            return data.getFactionPoints(player, this.id) < this.neutralPoints;
        }
    }

    public boolean isNeutralToPlayer(Player player) {
        PlayerFactionData data = PlayerData.get(player).factionData;
        int points = data.getFactionPoints(player, this.id);
        return points >= this.neutralPoints && points < this.friendlyPoints;
    }

    public boolean isAggressiveToNpc(EntityNPCInterface entity) {
        return this.attackFactions.contains(entity.faction.id);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getDefaultPoints() {
        return this.defaultPoints;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public int playerStatus(IPlayer player) {
        PlayerFactionData data = PlayerData.get(player.getMCEntity()).factionData;
        int points = data.getFactionPoints(player.getMCEntity(), this.id);
        if (points >= this.friendlyPoints) {
            return 1;
        } else {
            return points < this.neutralPoints ? -1 : 0;
        }
    }

    @Override
    public boolean hostileToNpc(ICustomNpc npc) {
        return this.attackFactions.contains(npc.getFaction().getId());
    }

    @Override
    public void setDefaultPoints(int points) {
        this.defaultPoints = points;
    }

    @Override
    public boolean hostileToFaction(int factionId) {
        return this.attackFactions.contains(factionId);
    }

    @Override
    public int[] getHostileList() {
        int[] a = new int[this.attackFactions.size()];
        int i = 0;
        for (Integer val : this.attackFactions) {
            a[i++] = val;
        }
        return a;
    }

    @Override
    public void addHostile(int id) {
        if (this.attackFactions.contains(id)) {
            throw new CustomNPCsException("Faction " + this.id + " is already hostile to " + id);
        } else {
            this.attackFactions.add(id);
        }
    }

    @Override
    public void removeHostile(int id) {
        this.attackFactions.remove(id);
    }

    @Override
    public boolean hasHostile(int id) {
        return this.attackFactions.contains(id);
    }

    @Override
    public boolean getIsHidden() {
        return this.hideFaction;
    }

    @Override
    public void setIsHidden(boolean bo) {
        this.hideFaction = bo;
    }

    @Override
    public boolean getAttackedByMobs() {
        return this.getsAttacked;
    }

    @Override
    public void setAttackedByMobs(boolean bo) {
        this.getsAttacked = bo;
    }

    @Override
    public void save() {
        FactionController.instance.saveFaction(this);
    }
}