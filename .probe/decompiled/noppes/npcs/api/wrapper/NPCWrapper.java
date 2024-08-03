package noppes.npcs.api.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.IProjectile;
import noppes.npcs.api.entity.data.INPCAdvanced;
import noppes.npcs.api.entity.data.INPCAi;
import noppes.npcs.api.entity.data.INPCDisplay;
import noppes.npcs.api.entity.data.INPCInventory;
import noppes.npcs.api.entity.data.INPCJob;
import noppes.npcs.api.entity.data.INPCRole;
import noppes.npcs.api.entity.data.INPCStats;
import noppes.npcs.api.handler.data.IDialog;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.util.ValueUtil;

public class NPCWrapper<T extends EntityNPCInterface> extends EntityLivingWrapper<T> implements ICustomNpc {

    public NPCWrapper(T npc) {
        super(npc);
    }

    @Override
    public void setMaxHealth(float health) {
        if ((int) health != this.entity.stats.maxHealth) {
            super.setMaxHealth(health);
            this.entity.stats.maxHealth = (int) health;
            this.entity.updateClient = true;
        }
    }

    @Override
    public INPCDisplay getDisplay() {
        return this.entity.display;
    }

    @Override
    public INPCInventory getInventory() {
        return this.entity.inventory;
    }

    @Override
    public INPCAi getAi() {
        return this.entity.ais;
    }

    @Override
    public INPCAdvanced getAdvanced() {
        return this.entity.advanced;
    }

    @Override
    public INPCStats getStats() {
        return this.entity.stats;
    }

    @Override
    public IFaction getFaction() {
        return this.entity.faction;
    }

    @Override
    public ITimers getTimers() {
        return this.entity.timers;
    }

    @Override
    public void setFaction(int id) {
        Faction faction = FactionController.instance.getFaction(id);
        if (faction == null) {
            throw new CustomNPCsException("Unknown faction id: " + id);
        } else {
            this.entity.setFaction(id);
        }
    }

    @Override
    public INPCRole getRole() {
        return this.entity.role;
    }

    @Override
    public INPCJob getJob() {
        return this.entity.job;
    }

    @Override
    public int getHomeX() {
        return this.entity.ais.startPos().m_123341_();
    }

    @Override
    public int getHomeY() {
        return this.entity.ais.startPos().m_123342_();
    }

    @Override
    public int getHomeZ() {
        return this.entity.ais.startPos().m_123343_();
    }

    @Override
    public void setHome(int x, int y, int z) {
        this.entity.ais.setStartPos(new BlockPos(x, y, z));
    }

    public int getOffsetX() {
        return (int) this.entity.ais.bodyOffsetX;
    }

    public int getOffsetY() {
        return (int) this.entity.ais.bodyOffsetY;
    }

    public int getOffsetZ() {
        return (int) this.entity.ais.bodyOffsetZ;
    }

    public void setOffset(int x, int y, int z) {
        this.entity.ais.bodyOffsetX = ValueUtil.correctFloat((float) x, 0.0F, 9.0F);
        this.entity.ais.bodyOffsetY = ValueUtil.correctFloat((float) y, 0.0F, 9.0F);
        this.entity.ais.bodyOffsetZ = ValueUtil.correctFloat((float) z, 0.0F, 9.0F);
        this.entity.updateClient = true;
    }

    @Override
    public void say(String message) {
        this.entity.saySurrounding(new Line(message));
    }

    @Override
    public void sayTo(IPlayer player, String message) {
        this.entity.say(player.getMCEntity(), new Line(message));
    }

    @Override
    public void reset() {
        this.entity.reset();
    }

    @Override
    public long getAge() {
        return this.entity.totalTicksAlive;
    }

    @Override
    public IProjectile shootItem(IEntityLiving target, IItemStack item, int accuracy) {
        if (item == null) {
            throw new CustomNPCsException("No item was given");
        } else if (target == null) {
            throw new CustomNPCsException("No target was given");
        } else {
            accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
            return (IProjectile) NpcAPI.Instance().getIEntity(this.entity.shoot(target.getMCEntity(), accuracy, item.getMCItemStack(), false));
        }
    }

    @Override
    public IProjectile shootItem(double x, double y, double z, IItemStack item, int accuracy) {
        if (item == null) {
            throw new CustomNPCsException("No item was given");
        } else {
            accuracy = ValueUtil.CorrectInt(accuracy, 1, 100);
            return (IProjectile) NpcAPI.Instance().getIEntity(this.entity.shoot(x, y, z, accuracy, item.getMCItemStack(), false));
        }
    }

    @Override
    public void giveItem(IPlayer player, IItemStack item) {
        this.entity.givePlayerItem(player.getMCEntity(), item.getMCItemStack());
    }

    @Override
    public String executeCommand(String command) {
        if (!this.entity.m_20194_().isCommandBlockEnabled()) {
            throw new CustomNPCsException("Command blocks need to be enabled to executeCommands");
        } else {
            return NoppesUtilServer.runCommand(this.entity, this.entity.getName().getString(), command, null);
        }
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return this.entity.display.getName();
    }

    @Override
    public void setName(String name) {
        this.entity.display.setName(name);
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        int r = (int) rotation;
        if (this.entity.ais.orientation != r) {
            this.entity.ais.orientation = r;
            this.entity.updateClient = true;
        }
    }

    @Override
    public boolean typeOf(int type) {
        return type == 2 ? true : super.typeOf(type);
    }

    @Override
    public void setDialog(int slot, IDialog dialog) {
        if (slot >= 0 && slot <= 11) {
            if (dialog == null) {
                this.entity.dialogs.remove(slot);
            } else {
                DialogOption option = new DialogOption();
                option.dialogId = dialog.getId();
                option.title = dialog.getName();
                this.entity.dialogs.put(slot, option);
            }
        } else {
            throw new CustomNPCsException("Slot needs to be between 0 and 11");
        }
    }

    @Override
    public IDialog getDialog(int slot) {
        if (slot >= 0 && slot <= 11) {
            DialogOption option = (DialogOption) this.entity.dialogs.get(slot);
            return option != null && option.hasDialog() ? option.getDialog() : null;
        } else {
            throw new CustomNPCsException("Slot needs to be between 0 and 11");
        }
    }

    @Override
    public void updateClient() {
        this.entity.updateClient();
    }

    @Override
    public IEntityLiving getOwner() {
        LivingEntity owner = this.entity.getOwner();
        return owner != null ? (IEntityLiving) NpcAPI.Instance().getIEntity(owner) : null;
    }

    @Override
    public void trigger(int id, Object... arguments) {
        EventHooks.onScriptTriggerEvent(this.entity.script, id, this.getWorld(), this.getPos(), null, arguments);
    }
}