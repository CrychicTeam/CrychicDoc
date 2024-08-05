package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.ClientProxy;
import noppes.npcs.shared.common.PacketBasic;

public class PacketConfigFont extends PacketBasic {

    private final String font;

    private final int size;

    public PacketConfigFont(String font, int size) {
        this.font = font;
        this.size = size;
    }

    public static void encode(PacketConfigFont msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.font);
        buf.writeInt(msg.size);
    }

    public static PacketConfigFont decode(FriendlyByteBuf buf) {
        return new PacketConfigFont(buf.readUtf(32767), buf.readInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Runnable run = () -> {
            if (!this.font.isEmpty()) {
                CustomNpcs.FontType = this.font;
                CustomNpcs.FontSize = this.size;
                ClientProxy.Font.clear();
                ClientProxy.Font = new ClientProxy.FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
                CustomNpcs.Config.updateConfig();
                this.player.m_213846_(Component.translatable("Font set to %s", ClientProxy.Font.getName()));
            } else {
                this.player.m_213846_(Component.translatable("Current font is " + ClientProxy.Font.getName()));
            }
        };
        Minecraft.getInstance().m_18707_(run);
    }
}