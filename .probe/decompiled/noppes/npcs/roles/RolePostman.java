package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;

public class RolePostman extends RoleInterface {

    public NpcMiscInventory inventory = new NpcMiscInventory(1);

    private List<Player> recentlyChecked = new ArrayList();

    private List<Player> toCheck;

    public RolePostman(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public boolean aiShouldExecute() {
        if (this.npc.f_19797_ % 20 != 0) {
            return false;
        } else {
            this.toCheck = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(10.0, 10.0, 10.0));
            this.toCheck.removeAll(this.recentlyChecked);
            List<Player> listMax = this.npc.m_9236_().m_45976_(Player.class, this.npc.m_20191_().inflate(20.0, 20.0, 20.0));
            this.recentlyChecked.retainAll(listMax);
            this.recentlyChecked.addAll(this.toCheck);
            for (Player player : this.toCheck) {
                if (PlayerData.get(player).mailData.hasMail()) {
                    this.npc.say(player, new Line("mailbox.gotmail"));
                }
            }
            return false;
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return false;
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.put("PostInv", this.inventory.getToNBT());
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.inventory.setFromNBT(nbttagcompound.getCompound("PostInv"));
    }

    @Override
    public void interact(Player player) {
        NoppesUtilServer.openContainerGui((ServerPlayer) player, EnumGuiType.PlayerMailman, buf -> {
            buf.writeBoolean(true);
            buf.writeBoolean(true);
        });
    }

    @Override
    public int getType() {
        return 5;
    }
}