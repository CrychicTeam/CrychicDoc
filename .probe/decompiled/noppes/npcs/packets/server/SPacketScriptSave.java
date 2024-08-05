package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketScriptSave extends PacketServerBasic {

    private int type;

    private CompoundTag data;

    public SPacketScriptSave(int type, CompoundTag data) {
        this.type = type;
        this.data = data;
    }

    public SPacketScriptSave(FriendlyByteBuf buf) {
        this.type = buf.readInt();
        this.data = buf.readNbt();
    }

    public static SPacketScriptSave decode(FriendlyByteBuf buf) {
        return new SPacketScriptSave(buf);
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.scripter || item.getItem() == CustomBlocks.scripted_door_item || item.getItem() == CustomItems.wand || item.getItem() == CustomItems.scripted_item || item.getItem() == CustomBlocks.scripted_item;
    }

    @Override
    public boolean requiresNpc() {
        return this.type == 0;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.TOOL_SCRIPTER;
    }

    @Override
    public void handle() {
        if (this.type == 0) {
            this.npc.script.load(this.data);
            this.npc.updateAI = true;
            this.npc.script.lastInited = -1L;
        }
        if (this.type == 1) {
            PlayerData pd = PlayerData.get(this.player);
            if (!(this.player.m_9236_().getBlockEntity(pd.scriptBlockPos) instanceof TileScripted script)) {
                return;
            }
            script.setNBT(this.data);
            script.lastInited = -1L;
        }
        if (this.type == 2) {
            if (!this.player.isCreative()) {
                return;
            }
            ItemScriptedWrapper wrapper = (ItemScriptedWrapper) NpcAPI.Instance().getIItemStack(this.player.m_21205_());
            wrapper.setMCNbt(this.data);
            wrapper.lastInited = -1L;
            wrapper.saveScriptData();
            wrapper.updateClient = true;
            this.player.f_36096_.sendAllDataToRemote();
        }
        if (this.type == 3) {
            ScriptController.Instance.setForgeScripts(this.data);
        }
        if (this.type == 4) {
            ScriptController.Instance.setPlayerScripts(this.data);
        }
        if (this.type == 5) {
            PlayerData pd = PlayerData.get(this.player);
            if (!(this.player.m_9236_().getBlockEntity(pd.scriptBlockPos) instanceof TileScriptedDoor script)) {
                return;
            }
            script.setNBT(this.data);
            script.lastInited = -1L;
        }
    }

    public static void encode(SPacketScriptSave msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.type);
        buf.writeNbt(msg.data);
    }
}