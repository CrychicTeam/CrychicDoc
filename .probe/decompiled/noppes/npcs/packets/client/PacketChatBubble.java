package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.client.ChatMessages;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.common.PacketBasic;

public class PacketChatBubble extends PacketBasic {

    private final int id;

    private final Component message;

    private final boolean showMessage;

    public PacketChatBubble(int id, Component message, boolean showMessage) {
        this.id = id;
        this.message = message;
        this.showMessage = showMessage;
    }

    public static void encode(PacketChatBubble msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeComponent(msg.message);
        buf.writeBoolean(msg.showMessage);
    }

    public static PacketChatBubble decode(FriendlyByteBuf buf) {
        return new PacketChatBubble(buf.readInt(), buf.readComponent(), buf.readBoolean());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Entity entity = Minecraft.getInstance().level.getEntity(this.id);
        if (entity != null && entity instanceof EntityNPCInterface npc) {
            if (npc.messages == null) {
                npc.messages = new ChatMessages();
            }
            String text = NoppesStringUtils.formatText(this.message, this.player, npc);
            npc.messages.addMessage(text, npc);
            if (this.showMessage) {
                this.player.m_213846_(Component.literal(npc.getName().getString() + ": ").append(Component.translatable(text)));
            }
        }
    }
}