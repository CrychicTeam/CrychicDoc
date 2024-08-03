package noppes.npcs.roles;

import java.util.HashMap;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.role.IRoleFollower;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class RoleFollower extends RoleInterface implements IRoleFollower {

    private String ownerUUID;

    public boolean isFollowing = true;

    public HashMap<Integer, Integer> rates;

    public NpcMiscInventory inventory;

    public String dialogHire = "";

    public String dialogFarewell = "";

    public int daysHired;

    public long hiredTime;

    public boolean disableGui = false;

    public boolean infiniteDays = false;

    public boolean refuseSoulStone = false;

    public Player owner = null;

    public RoleFollower(EntityNPCInterface npc) {
        super(npc);
        this.inventory = new NpcMiscInventory(3);
        this.rates = new HashMap();
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("MercenaryDaysHired", this.daysHired);
        nbttagcompound.putLong("MercenaryHiredTime", this.hiredTime);
        nbttagcompound.putString("MercenaryDialogHired", this.dialogHire);
        nbttagcompound.putString("MercenaryDialogFarewell", this.dialogFarewell);
        if (this.hasOwner()) {
            nbttagcompound.putString("MercenaryOwner", this.ownerUUID);
        }
        nbttagcompound.put("MercenaryDayRates", NBTTags.nbtIntegerIntegerMap(this.rates));
        nbttagcompound.put("MercenaryInv", this.inventory.getToNBT());
        nbttagcompound.putBoolean("MercenaryIsFollowing", this.isFollowing);
        nbttagcompound.putBoolean("MercenaryDisableGui", this.disableGui);
        nbttagcompound.putBoolean("MercenaryInfiniteDays", this.infiniteDays);
        nbttagcompound.putBoolean("MercenaryRefuseSoulstone", this.refuseSoulStone);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.ownerUUID = nbttagcompound.getString("MercenaryOwner");
        this.daysHired = nbttagcompound.getInt("MercenaryDaysHired");
        this.hiredTime = nbttagcompound.getLong("MercenaryHiredTime");
        this.dialogHire = nbttagcompound.getString("MercenaryDialogHired");
        this.dialogFarewell = nbttagcompound.getString("MercenaryDialogFarewell");
        this.rates = NBTTags.getIntegerIntegerMap(nbttagcompound.getList("MercenaryDayRates", 10));
        this.inventory.setFromNBT(nbttagcompound.getCompound("MercenaryInv"));
        this.isFollowing = nbttagcompound.getBoolean("MercenaryIsFollowing");
        this.disableGui = nbttagcompound.getBoolean("MercenaryDisableGui");
        this.infiniteDays = nbttagcompound.getBoolean("MercenaryInfiniteDays");
        this.refuseSoulStone = nbttagcompound.getBoolean("MercenaryRefuseSoulstone");
    }

    @Override
    public boolean aiShouldExecute() {
        this.owner = this.getOwner();
        if (!this.infiniteDays && this.owner != null && this.getDays() <= 0) {
            RoleEvent.FollowerFinishedEvent event = new RoleEvent.FollowerFinishedEvent(this.owner, this.npc.wrappedNPC);
            EventHooks.onNPCRole(this.npc, event);
            this.npc.say(this.owner, new Line(NoppesStringUtils.formatText(this.dialogFarewell, this.owner, this.npc)));
            this.killed();
        }
        return false;
    }

    public Player getOwner() {
        if (this.npc.m_9236_().isClientSide) {
            return null;
        } else if (this.ownerUUID != null && !this.ownerUUID.isEmpty()) {
            try {
                UUID uuid = UUID.fromString(this.ownerUUID);
                if (uuid != null) {
                    return this.npc.m_9236_().m_46003_(uuid);
                }
            } catch (IllegalArgumentException var2) {
            }
            return (Player) ((ServerLevel) this.npc.m_9236_()).players().stream().filter(t -> t.m_7755_().getString().equals(this.ownerUUID)).findFirst().orElse(null);
        } else {
            return null;
        }
    }

    public boolean hasOwner() {
        return !this.infiniteDays && this.daysHired <= 0 ? false : this.ownerUUID != null && !this.ownerUUID.isEmpty();
    }

    @Override
    public void killed() {
        this.ownerUUID = null;
        this.daysHired = 0;
        this.hiredTime = 0L;
        this.isFollowing = true;
    }

    @Override
    public void reset() {
        this.killed();
    }

    @Override
    public void interact(Player player) {
        if (this.ownerUUID == null || this.ownerUUID.isEmpty()) {
            this.npc.say(player, this.npc.advanced.getInteractLine());
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollowerHire, this.npc);
        } else if (player == this.owner && !this.disableGui) {
            NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerFollower, this.npc);
        }
    }

    @Override
    public boolean defendOwner() {
        return this.isFollowing() && this.npc.job.getType() == 3;
    }

    @Override
    public void delete() {
    }

    @Override
    public boolean isFollowing() {
        return this.owner != null && this.isFollowing && this.getDays() > 0;
    }

    public void setOwner(Player player) {
        UUID id = player.m_20148_();
        if (this.ownerUUID == null || id == null || !this.ownerUUID.equals(id.toString())) {
            this.killed();
        }
        this.ownerUUID = id.toString();
    }

    @Override
    public int getDays() {
        if (this.infiniteDays) {
            return 100;
        } else if (this.daysHired <= 0) {
            return 0;
        } else {
            int days = (int) ((this.npc.m_9236_().getGameTime() - this.hiredTime) / 24000L);
            return this.daysHired - days;
        }
    }

    @Override
    public void addDays(int days) {
        this.daysHired = days + this.getDays();
        this.hiredTime = this.npc.m_9236_().getGameTime();
    }

    @Override
    public boolean getInfinite() {
        return this.infiniteDays;
    }

    @Override
    public void setInfinite(boolean infinite) {
        this.infiniteDays = infinite;
    }

    @Override
    public boolean getGuiDisabled() {
        return this.disableGui;
    }

    @Override
    public void setGuiDisabled(boolean disabled) {
        this.disableGui = disabled;
    }

    @Override
    public boolean getRefuseSoulstone() {
        return this.refuseSoulStone;
    }

    @Override
    public void setRefuseSoulstone(boolean refuse) {
        this.refuseSoulStone = refuse;
    }

    @Override
    public IPlayer getFollowing() {
        Player owner = this.getOwner();
        return owner != null ? (IPlayer) NpcAPI.Instance().getIEntity(owner) : null;
    }

    @Override
    public void setFollowing(IPlayer player) {
        if (player == null) {
            this.setOwner(null);
        } else {
            this.setOwner(player.getMCEntity());
        }
    }

    @Override
    public int getType() {
        return 2;
    }
}