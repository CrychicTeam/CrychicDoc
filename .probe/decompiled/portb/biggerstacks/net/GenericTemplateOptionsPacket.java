package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;
import portb.configlib.template.TemplateOptions;

public class GenericTemplateOptionsPacket extends TemplateOptions {

    public GenericTemplateOptionsPacket(int normalStackLimit, int potionsStackLimit, int enchantedBooksStackLimit) {
        super(normalStackLimit, potionsStackLimit, enchantedBooksStackLimit);
    }

    public GenericTemplateOptionsPacket(FriendlyByteBuf buf) {
        super(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.getNormalStackLimit());
        buf.writeInt(this.getPotionStackLimit());
        buf.writeInt(this.getEnchBookLimit());
    }
}