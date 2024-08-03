package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.data.Bank;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketBankSave extends PacketServerBasic {

    private CompoundTag data;

    public SPacketBankSave(CompoundTag data) {
        this.data = data;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_BANK;
    }

    public static void encode(SPacketBankSave msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SPacketBankSave decode(FriendlyByteBuf buf) {
        return new SPacketBankSave(buf.readNbt());
    }

    @Override
    protected void handle() {
        Bank bank = new Bank();
        bank.readAdditionalSaveData(this.data);
        BankController.getInstance().saveBank(bank);
        SPacketBanksGet.sendBankDataAll(this.player);
        SPacketBankGet.sendBank(this.player, bank);
    }
}