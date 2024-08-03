package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileCopy;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketSchematicsStore extends PacketServerBasic {

    private String name;

    private CompoundTag data;

    public SPacketSchematicsStore(String name, CompoundTag data) {
        this.name = name;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.copy_item;
    }

    public static void encode(SPacketSchematicsStore msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name);
        buf.writeNbt(msg.data);
    }

    public static SPacketSchematicsStore decode(FriendlyByteBuf buf) {
        return new SPacketSchematicsStore(buf.readUtf(32767), buf.readNbt());
    }

    @Override
    protected void handle() {
        TileCopy tile = (TileCopy) SPacketTileEntitySave.saveTileEntity(this.player, this.data);
        if (tile != null && !this.name.isEmpty()) {
            SchematicController.Instance.save(this.player.m_20203_(), this.name, tile.m_58899_(), tile.height, tile.width, tile.length);
        }
    }
}