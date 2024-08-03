package noppes.npcs.packets.client;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.shared.common.PacketBasic;
import noppes.npcs.shared.common.util.LogWriter;

public class PacketGuiOpen extends PacketBasic {

    private final EnumGuiType gui;

    private final BlockPos pos;

    public PacketGuiOpen(EnumGuiType gui, BlockPos pos) {
        this.gui = gui;
        this.pos = pos;
    }

    public static void encode(PacketGuiOpen msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.gui);
        buf.writeBlockPos(msg.pos);
    }

    public static PacketGuiOpen decode(FriendlyByteBuf buf) {
        return new PacketGuiOpen(buf.readEnum(EnumGuiType.class), buf.readBlockPos());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        try {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeBlockPos(this.pos);
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(ClientProxy.getGui(this.gui, NoppesUtil.getLastNpc(), buffer));
        } catch (Exception var3) {
            LogWriter.error("Error in gui: " + this.gui, var3);
        }
    }
}