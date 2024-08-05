package noppes.npcs.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import noppes.npcs.CustomItems;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ServerboundSetCreativeModeSlotPacket.class })
public abstract class CCreativeInventoryActionPacketMixin implements Packet<ServerGamePacketListener> {

    @Override
    public void write(FriendlyByteBuf buffer) {
        ServerboundSetCreativeModeSlotPacket p = (ServerboundSetCreativeModeSlotPacket) this;
        if (p.getItem().getItem() == CustomItems.scripted_item) {
            buffer.writeShort(p.getSlotNum());
            buffer.writeItemStack(p.getItem(), true);
        } else {
            buffer.writeShort(p.getSlotNum());
            buffer.writeItemStack(p.getItem(), false);
        }
    }
}