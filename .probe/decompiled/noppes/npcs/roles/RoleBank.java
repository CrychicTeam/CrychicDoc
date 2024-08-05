package noppes.npcs.roles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.controllers.data.BankData;
import noppes.npcs.entity.EntityNPCInterface;

public class RoleBank extends RoleInterface {

    public int bankId = -1;

    public RoleBank(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public CompoundTag save(CompoundTag nbttagcompound) {
        nbttagcompound.putInt("RoleBankID", this.bankId);
        return nbttagcompound;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
        this.bankId = nbttagcompound.getInt("RoleBankID");
    }

    @Override
    public void interact(Player player) {
        NoppesUtilServer.setEditingNpc(player, this.npc);
        BankData data = PlayerDataController.instance.getBankData(player, this.bankId).getBankOrDefault(this.bankId);
        data.openBankGui((ServerPlayer) player, this.npc, this.bankId, 0);
        this.npc.say(player, this.npc.advanced.getInteractLine());
    }

    public Bank getBank() {
        Bank bank = (Bank) BankController.getInstance().banks.get(this.bankId);
        return bank != null ? bank : (Bank) BankController.getInstance().banks.values().iterator().next();
    }

    @Override
    public int getType() {
        return 3;
    }
}