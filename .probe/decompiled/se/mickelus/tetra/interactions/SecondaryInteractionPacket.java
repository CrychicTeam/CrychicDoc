package se.mickelus.tetra.interactions;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.BlockPosPacket;

public class SecondaryInteractionPacket extends BlockPosPacket {

    private String key;

    private int targetId = -1;

    public SecondaryInteractionPacket() {
    }

    public SecondaryInteractionPacket(String key, BlockPos pos, Entity target) {
        super(pos);
        this.key = key;
        this.targetId = (Integer) Optional.ofNullable(target).map(Entity::m_19879_).orElse(-1);
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeUtf(this.key);
        buffer.writeInt(this.targetId);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        this.key = buffer.readUtf();
        this.targetId = buffer.readInt();
    }

    @Override
    public void handle(Player player) {
        Entity target = (Entity) Optional.of(this.targetId).filter(id -> id != -1).map(id -> player.m_9236_().getEntity(id)).orElse(null);
        SecondaryInteraction interaction = SecondaryInteractionHandler.getInteraction(this.key);
        if (interaction != null) {
            interaction.perform(player, player.m_9236_(), this.pos, target);
        }
    }
}