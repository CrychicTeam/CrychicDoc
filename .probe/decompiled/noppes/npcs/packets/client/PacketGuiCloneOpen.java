package noppes.npcs.packets.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNpcMobSpawnerAdd;
import noppes.npcs.shared.common.PacketBasic;

public class PacketGuiCloneOpen extends PacketBasic {

    private final CompoundTag data;

    public PacketGuiCloneOpen(CompoundTag data) {
        this.data = data;
    }

    public static void encode(PacketGuiCloneOpen msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static PacketGuiCloneOpen decode(FriendlyByteBuf buf) {
        return new PacketGuiCloneOpen(buf.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        NoppesUtil.openGUI(this.player, new GuiNpcMobSpawnerAdd(this.data));
    }
}