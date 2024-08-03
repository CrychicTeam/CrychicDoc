package noppes.npcs.packets.server;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.ForgeEventHandler;
import noppes.npcs.NBTTags;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketScriptGet extends PacketServerBasic {

    private int type;

    public SPacketScriptGet(int type) {
        this.type = type;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.scripted_item || item.getItem() == CustomItems.scripter || item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.scripted_door_item || item.getItem() == CustomBlocks.scripted_item;
    }

    @Override
    public boolean requiresNpc() {
        return this.type == 0;
    }

    public static void encode(SPacketScriptGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.type);
    }

    public static SPacketScriptGet decode(FriendlyByteBuf buf) {
        return new SPacketScriptGet(buf.readInt());
    }

    @Override
    protected void handle() {
        CompoundTag compound = new CompoundTag();
        if (this.type == 0) {
            this.npc.script.save(compound);
            compound.put("Methods", NBTTags.nbtStringList((List<String>) Arrays.stream(EnumScriptType.npcScripts).map(type -> type.function).collect(Collectors.toList())));
        }
        if (this.type == 1) {
            PlayerData data = PlayerData.get(this.player);
            BlockEntity tile = this.player.m_9236_().getBlockEntity(data.scriptBlockPos);
            if (!(tile instanceof TileScripted)) {
                return;
            }
            ((TileScripted) tile).getNBT(compound);
            compound.put("Methods", NBTTags.nbtStringList((List<String>) Arrays.stream(EnumScriptType.blockScripts).map(type -> type.function).collect(Collectors.toList())));
        }
        if (this.type == 2) {
            ItemScriptedWrapper iw = (ItemScriptedWrapper) NpcAPI.Instance().getIItemStack(this.player.m_21205_());
            compound = iw.getMCNbt();
            compound.put("Methods", NBTTags.nbtStringList((List<String>) Arrays.stream(EnumScriptType.itemScripts).map(type -> type.function).collect(Collectors.toList())));
        }
        if (this.type == 3) {
            ScriptController.Instance.forgeScripts.save(compound);
            compound.put("Methods", NBTTags.nbtStringList(ForgeEventHandler.eventNames));
        }
        if (this.type == 4) {
            ScriptController.Instance.playerScripts.save(compound);
            compound.put("Methods", NBTTags.nbtStringList((List<String>) Arrays.stream(EnumScriptType.playerScripts).map(type -> type.function).collect(Collectors.toList())));
        }
        if (this.type == 5) {
            PlayerData data = PlayerData.get(this.player);
            BlockEntity tile = this.player.m_9236_().getBlockEntity(data.scriptBlockPos);
            if (!(tile instanceof TileScriptedDoor)) {
                return;
            }
            ((TileScriptedDoor) tile).getNBT(compound);
            compound.put("Methods", NBTTags.nbtStringList((List<String>) Arrays.stream(EnumScriptType.doorScripts).map(type -> type.function).collect(Collectors.toList())));
        }
        compound.put("Languages", ScriptController.Instance.nbtLanguages());
        Packets.send(this.player, new PacketGuiData(compound));
    }
}