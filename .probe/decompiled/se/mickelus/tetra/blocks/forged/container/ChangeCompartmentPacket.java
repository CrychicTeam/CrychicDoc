package se.mickelus.tetra.blocks.forged.container;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.mutil.util.CastOptional;

@ParametersAreNonnullByDefault
public class ChangeCompartmentPacket extends AbstractPacket {

    private int compartmentIndex;

    public ChangeCompartmentPacket() {
    }

    public ChangeCompartmentPacket(int compartmentIndex) {
        this.compartmentIndex = compartmentIndex;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.compartmentIndex);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.compartmentIndex = buffer.readInt();
    }

    @Override
    public void handle(Player player) {
        CastOptional.cast(player.containerMenu, ForgedContainerMenu.class).ifPresent(container -> container.changeCompartment(this.compartmentIndex));
    }
}