package noppes.npcs.packets.server;

import java.io.File;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.SyncController;
import noppes.npcs.controllers.data.PlayerBankData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerDialogData;
import noppes.npcs.controllers.data.PlayerFactionData;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.PlayerTransportData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerDataRemove extends PacketServerBasic {

    private EnumPlayerData type;

    private String name;

    private int id;

    public SPacketPlayerDataRemove(EnumPlayerData type, String name, int id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_PLAYERDATA;
    }

    public static void encode(SPacketPlayerDataRemove msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeUtf(msg.name);
        buf.writeInt(msg.id);
    }

    public static SPacketPlayerDataRemove decode(FriendlyByteBuf buf) {
        return new SPacketPlayerDataRemove(buf.readEnum(EnumPlayerData.class), buf.readUtf(32767), buf.readInt());
    }

    @Override
    protected void handle() {
        if (this.name != null && !this.name.isEmpty()) {
            Player pl = this.player.m_20194_().getPlayerList().getPlayerByName(this.name);
            PlayerData playerdata = null;
            if (pl == null) {
                playerdata = PlayerDataController.instance.getDataFromUsername(this.player.m_20194_(), this.name);
            } else {
                playerdata = PlayerData.get(pl);
            }
            if (this.type == EnumPlayerData.Players) {
                File file = new File(CustomNpcs.getLevelSaveDirectory("playerdata"), playerdata.uuid + ".json");
                if (file.exists()) {
                    file.delete();
                }
                if (pl != null) {
                    playerdata.setNBT(new CompoundTag());
                    SPacketPlayerDataGet.sendPlayerData(this.type, this.player, this.name);
                    playerdata.save(true);
                    return;
                }
                PlayerDataController.instance.nameUUIDs.remove(this.name);
            }
            if (this.type == EnumPlayerData.Quest) {
                PlayerQuestData data = playerdata.questData;
                data.activeQuests.remove(this.id);
                data.finishedQuests.remove(this.id);
                playerdata.save(true);
            }
            if (this.type == EnumPlayerData.Dialog) {
                PlayerDialogData data = playerdata.dialogData;
                data.dialogsRead.remove(this.id);
                playerdata.save(true);
            }
            if (this.type == EnumPlayerData.Transport) {
                PlayerTransportData data = playerdata.transportData;
                data.transports.remove(this.id);
                playerdata.save(true);
            }
            if (this.type == EnumPlayerData.Bank) {
                PlayerBankData data = playerdata.bankData;
                data.banks.remove(this.id);
                playerdata.save(true);
            }
            if (this.type == EnumPlayerData.Factions) {
                PlayerFactionData data = playerdata.factionData;
                data.factionData.remove(this.id);
                playerdata.save(true);
            }
            if (pl != null) {
                SyncController.syncPlayer((ServerPlayer) pl);
            }
            SPacketPlayerDataGet.sendPlayerData(this.type, this.player, this.name);
        }
    }
}