package se.mickelus.tetra.items.modular.impl.toolbelt.suspend;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class ToggleSuspendPacket extends AbstractPacket {

    boolean toggleOn;

    public ToggleSuspendPacket() {
    }

    public ToggleSuspendPacket(boolean toggleOn) {
        this.toggleOn = toggleOn;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.toggleOn);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.toggleOn = buffer.readBoolean();
    }

    @Override
    public void handle(Player player) {
        SuspendEffect.toggleSuspend(player, this.toggleOn);
    }
}