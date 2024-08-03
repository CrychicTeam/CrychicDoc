package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerItemGiverData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobItemGiver extends JobInterface {

    public int cooldownType = 0;

    public int givingMethod = 0;

    public int cooldown = 10;

    public NpcMiscInventory inventory;

    public int itemGiverId = 0;

    public List<String> lines = new ArrayList();

    private int ticks = 10;

    private List<Player> recentlyChecked = new ArrayList();

    private List<Player> toCheck;

    public Availability availability = new Availability();

    public JobItemGiver(EntityNPCInterface npc) {
        super(npc);
        this.inventory = new NpcMiscInventory(9);
        this.lines.add("Have these items {player}");
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("igCooldownType", this.cooldownType);
        nbttagcompound.putInt("igGivingMethod", this.givingMethod);
        nbttagcompound.putInt("igCooldown", this.cooldown);
        nbttagcompound.putInt("ItemGiverId", this.itemGiverId);
        nbttagcompound.put("igLines", NBTTags.nbtStringList(this.lines));
        nbttagcompound.put("igJobInventory", this.inventory.getToNBT());
        nbttagcompound.put("igAvailability", this.availability.save(new CompoundTag()));
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.itemGiverId = nbttagcompound.getInt("ItemGiverId");
        this.cooldownType = nbttagcompound.getInt("igCooldownType");
        this.givingMethod = nbttagcompound.getInt("igGivingMethod");
        this.cooldown = nbttagcompound.getInt("igCooldown");
        this.lines = NBTTags.getStringList(nbttagcompound.getList("igLines", 10));
        this.inventory.setFromNBT(nbttagcompound.getCompound("igJobInventory"));
        if (this.itemGiverId == 0 && GlobalDataController.instance != null) {
            this.itemGiverId = GlobalDataController.instance.incrementItemGiverId();
        }
        this.availability.load(nbttagcompound.getCompound("igAvailability"));
    }

    public ListTag newHashMapNBTList(HashMap<String, Long> lines) {
        ListTag nbttaglist = new ListTag();
        for (String s : lines.keySet()) {
            CompoundTag nbttagcompound = new CompoundTag();
            nbttagcompound.putString("Line", s);
            nbttagcompound.putLong("Time", (Long) lines.get(s));
            nbttaglist.add(nbttagcompound);
        }
        return nbttaglist;
    }

    public HashMap<String, Long> getNBTLines(ListTag tagList) {
        HashMap<String, Long> map = new HashMap();
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag nbttagcompound = tagList.getCompound(i);
            String line = nbttagcompound.getString("Line");
            long time = nbttagcompound.getLong("Time");
            map.put(line, time);
        }
        return map;
    }

    private boolean giveItems(Player player) {
        PlayerItemGiverData data = PlayerData.get(player).itemgiverData;
        if (!this.canPlayerInteract(data)) {
            return false;
        } else {
            Vector<ItemStack> items = new Vector();
            Vector<ItemStack> toGive = new Vector();
            for (ItemStack is : this.inventory.items) {
                if (!is.isEmpty()) {
                    items.add(is.copy());
                }
            }
            if (items.isEmpty()) {
                return false;
            } else {
                if (this.isAllGiver()) {
                    toGive = items;
                } else if (this.isRemainingGiver()) {
                    for (ItemStack isx : items) {
                        if (!this.playerHasItem(player, isx.getItem())) {
                            toGive.add(isx);
                        }
                    }
                } else if (this.isRandomGiver()) {
                    toGive.add(((ItemStack) items.get(this.npc.m_9236_().random.nextInt(items.size()))).copy());
                } else if (this.isGiverWhenNotOwnedAny()) {
                    boolean ownsItems = false;
                    for (ItemStack isxx : items) {
                        if (this.playerHasItem(player, isxx.getItem())) {
                            ownsItems = true;
                            break;
                        }
                    }
                    if (ownsItems) {
                        return false;
                    }
                    toGive = items;
                } else if (this.isChainedGiver()) {
                    int itemIndex = data.getItemIndex(this);
                    int i = 0;
                    for (ItemStack item : this.inventory.items) {
                        if (i == itemIndex) {
                            toGive.add(item);
                            break;
                        }
                        i++;
                    }
                }
                if (toGive.isEmpty()) {
                    return false;
                } else if (this.givePlayerItems(player, toGive)) {
                    if (!this.lines.isEmpty()) {
                        this.npc.say(player, new Line((String) this.lines.get(this.npc.m_217043_().nextInt(this.lines.size()))));
                    }
                    if (this.isDaily()) {
                        data.setTime(this, (long) this.getDay());
                    } else {
                        data.setTime(this, System.currentTimeMillis());
                    }
                    if (this.isChainedGiver()) {
                        data.setItemIndex(this, (data.getItemIndex(this) + 1) % this.inventory.items.size());
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private int getDay() {
        return (int) (this.npc.m_9236_().getGameTime() / 24000L);
    }

    private boolean canPlayerInteract(PlayerItemGiverData data) {
        if (this.inventory.items.isEmpty()) {
            return false;
        } else if (this.isOnTimer()) {
            return !data.hasInteractedBefore(this) ? true : data.getTime(this) + (long) (this.cooldown * 1000) < System.currentTimeMillis();
        } else if (this.isGiveOnce()) {
            return !data.hasInteractedBefore(this);
        } else if (this.isDaily()) {
            return !data.hasInteractedBefore(this) ? true : (long) this.getDay() > data.getTime(this);
        } else {
            return false;
        }
    }

    private boolean givePlayerItems(Player player, Vector<ItemStack> toGive) {
        if (toGive.isEmpty()) {
            return false;
        } else if (this.freeInventorySlots(player) < toGive.size()) {
            return false;
        } else {
            for (ItemStack is : toGive) {
                this.npc.givePlayerItem(player, is);
            }
            return true;
        }
    }

    private boolean playerHasItem(Player player, Item item) {
        for (ItemStack is : player.getInventory().items) {
            if (!is.isEmpty() && is.getItem() == item) {
                return true;
            }
        }
        for (ItemStack isx : player.getInventory().armor) {
            if (!isx.isEmpty() && isx.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    private int freeInventorySlots(Player player) {
        int i = 0;
        for (ItemStack is : player.getInventory().items) {
            if (NoppesUtilServer.IsItemStackNull(is)) {
                i++;
            }
        }
        return i;
    }

    private boolean isRandomGiver() {
        return this.givingMethod == 0;
    }

    private boolean isAllGiver() {
        return this.givingMethod == 1;
    }

    private boolean isRemainingGiver() {
        return this.givingMethod == 2;
    }

    private boolean isGiverWhenNotOwnedAny() {
        return this.givingMethod == 3;
    }

    private boolean isChainedGiver() {
        return this.givingMethod == 4;
    }

    public boolean isOnTimer() {
        return this.cooldownType == 0;
    }

    private boolean isGiveOnce() {
        return this.cooldownType == 1;
    }

    private boolean isDaily() {
        return this.cooldownType == 2;
    }

    @Override
    public boolean aiShouldExecute() {
        if (this.npc.isAttacking()) {
            return false;
        } else {
            this.ticks--;
            if (this.ticks > 0) {
                return false;
            } else {
                this.ticks = 10;
                this.toCheck = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(3.0, 3.0, 3.0));
                this.toCheck.removeAll(this.recentlyChecked);
                List<Player> listMax = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(10.0, 10.0, 10.0));
                this.recentlyChecked.retainAll(listMax);
                this.recentlyChecked.addAll(this.toCheck);
                return this.toCheck.size() > 0;
            }
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return false;
    }

    @Override
    public void aiStartExecuting() {
        for (Player player : this.toCheck) {
            if (this.npc.canNpcSee(player) && this.availability.isAvailable(player)) {
                this.recentlyChecked.add(player);
                this.interact(player);
            }
        }
    }

    @Override
    public void killed() {
    }

    private boolean interact(Player player) {
        if (!this.giveItems(player)) {
            this.npc.say(player, this.npc.advanced.getInteractLine());
        }
        return true;
    }

    @Override
    public void delete() {
    }

    @Override
    public int getType() {
        return 4;
    }
}