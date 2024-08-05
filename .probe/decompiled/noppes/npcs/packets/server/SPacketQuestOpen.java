package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketQuestOpen extends PacketServerBasic {

    private EnumGuiType gui;

    private CompoundTag data;

    public SPacketQuestOpen(EnumGuiType gui, CompoundTag data) {
        this.gui = gui;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_DIALOG;
    }

    public static void encode(SPacketQuestOpen msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.gui);
        buf.writeNbt(msg.data);
    }

    public static SPacketQuestOpen decode(FriendlyByteBuf buf) {
        return new SPacketQuestOpen(buf.readEnum(EnumGuiType.class), buf.readNbt());
    }

    @Override
    protected void handle() {
        Quest quest = new Quest(null);
        quest.readNBT(this.data);
        NoppesUtilServer.setEditingQuest(this.player, quest);
        NoppesUtilServer.openContainerGui(this.player, this.gui, buf -> buf.writeBlockPos(BlockPos.ZERO));
    }
}