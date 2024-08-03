package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.gui.custom.GuiCreationNewParts;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.shared.common.PacketBasic;

public class PacketGuiParts extends PacketBasic {

    private final int id;

    private final CompoundTag data;

    public PacketGuiParts(int id, CompoundTag data) {
        this.id = id;
        this.data = data;
    }

    public static void encode(PacketGuiParts msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeNbt(msg.data);
    }

    public static PacketGuiParts decode(FriendlyByteBuf buf) {
        return new PacketGuiParts(buf.readInt(), buf.readNbt(new NbtAccounter(Long.MAX_VALUE)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = this.player.m_9236_().getEntity(this.id);
        if (Minecraft.getInstance().screen instanceof GuiCustom gui && entity instanceof EntityCustomNpc npc) {
            GuiCreationNewParts parts = new GuiCreationNewParts(gui, npc);
            gui.initCallback = () -> {
                gui.add(parts);
                parts.init();
            };
            gui.setGuiData(this.data);
        }
    }
}