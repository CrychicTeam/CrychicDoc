package portb.biggerstacks.net;

import net.minecraft.network.FriendlyByteBuf;

public class ClientboundConfigureScreenOpenPacket extends GenericTemplateOptionsPacket {

    private final boolean isAlreadyUsingCustomFile;

    public ClientboundConfigureScreenOpenPacket(boolean hasExistingCustomFile, int normalStackLimit, int potionsStackLimit, int enchantedBooksStackLimit) {
        super(normalStackLimit, potionsStackLimit, enchantedBooksStackLimit);
        this.isAlreadyUsingCustomFile = hasExistingCustomFile;
    }

    public ClientboundConfigureScreenOpenPacket(FriendlyByteBuf buf) {
        super(buf);
        this.isAlreadyUsingCustomFile = buf.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        super.encode(buf);
        buf.writeBoolean(this.isAlreadyUsingCustomFile);
    }

    public boolean isAlreadyUsingCustomFile() {
        return this.isAlreadyUsingCustomFile;
    }
}