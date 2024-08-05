package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketMenuSave extends PacketServerBasic {

    private EnumMenuType type;

    private CompoundTag data;

    public SPacketMenuSave(EnumMenuType type, CompoundTag data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return this.type == EnumMenuType.MOVING_PATH ? item.getItem() == CustomItems.moving : item.getItem() == CustomItems.wand;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        if (this.type == EnumMenuType.DISPLAY || this.type == EnumMenuType.MODEL) {
            return CustomNpcsPermissions.NPC_DISPLAY;
        } else if (this.type == EnumMenuType.STATS) {
            return CustomNpcsPermissions.NPC_STATS;
        } else if (this.type == EnumMenuType.INVENTORY) {
            return CustomNpcsPermissions.NPC_INVENTORY;
        } else if (this.type == EnumMenuType.AI) {
            return CustomNpcsPermissions.NPC_AI;
        } else if (this.type == EnumMenuType.ADVANCED || this.type == EnumMenuType.TRANSFORM || this.type == EnumMenuType.MARK) {
            return CustomNpcsPermissions.NPC_ADVANCED;
        } else {
            return this.type == EnumMenuType.MOVING_PATH ? CustomNpcsPermissions.TOOL_PATHER : CustomNpcsPermissions.NPC_GUI;
        }
    }

    public static void encode(SPacketMenuSave msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.type);
        buf.writeNbt(msg.data);
    }

    public static SPacketMenuSave decode(FriendlyByteBuf buf) {
        return new SPacketMenuSave(buf.readEnum(EnumMenuType.class), buf.readNbt());
    }

    @Override
    protected void handle() {
        if (this.type == EnumMenuType.DISPLAY) {
            this.npc.display.readToNBT(this.data);
        }
        if (this.type == EnumMenuType.STATS) {
            this.npc.stats.readToNBT(this.data);
        }
        if (this.type == EnumMenuType.INVENTORY) {
            this.npc.inventory.load(this.data);
            this.npc.updateAI = true;
        }
        if (this.type == EnumMenuType.AI) {
            this.npc.ais.readToNBT(this.data);
            this.npc.m_21153_(this.npc.m_21233_());
            this.npc.updateAI = true;
        }
        if (this.type == EnumMenuType.ADVANCED) {
            this.npc.advanced.readToNBT(this.data);
            this.npc.updateAI = true;
        }
        if (this.type == EnumMenuType.MODEL) {
            ((EntityCustomNpc) this.npc).modelData.load(this.data);
        }
        if (this.type == EnumMenuType.TRANSFORM) {
            boolean isValid = this.npc.transform.isValid();
            this.npc.transform.readOptions(this.data);
            if (isValid != this.npc.transform.isValid()) {
                this.npc.updateAI = true;
            }
        }
        if (this.type == EnumMenuType.MOVING_PATH) {
            this.npc.ais.setMovingPath(NBTTags.getIntegerArraySet(this.data.getList("MovingPathNew", 10)));
        }
        if (this.type == EnumMenuType.MARK) {
            MarkData mark = MarkData.get(this.npc);
            mark.setNBT(this.data);
            mark.syncClients();
        }
        this.npc.updateClient = true;
    }
}