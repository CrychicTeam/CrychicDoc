package se.mickelus.tetra.effect.revenge;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class RemoveRevengePacket extends AbstractPacket {

    private int entityId = -1;

    public RemoveRevengePacket(Entity attacker) {
        this.entityId = attacker.getId();
    }

    public RemoveRevengePacket() {
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityId);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.entityId = buffer.readVarInt();
    }

    @Override
    public void handle(Player player) {
        RevengeTracker.removeEnemy(player, this.entityId);
    }
}