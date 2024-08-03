package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class LungeEchoPacket extends AbstractPacket {

    boolean isVertical;

    public LungeEchoPacket() {
    }

    public LungeEchoPacket(boolean isVertical) {
        this.isVertical = isVertical;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.isVertical);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.isVertical = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        LungeEffect.receiveEchoPacket(player, this.isVertical);
    }
}