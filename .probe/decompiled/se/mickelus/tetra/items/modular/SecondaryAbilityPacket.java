package se.mickelus.tetra.items.modular;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class SecondaryAbilityPacket extends AbstractPacket {

    private int targetId = -1;

    private InteractionHand hand;

    public SecondaryAbilityPacket() {
    }

    public SecondaryAbilityPacket(@Nullable LivingEntity target, InteractionHand hand) {
        this.targetId = (Integer) Optional.ofNullable(target).map(Entity::m_19879_).orElse(-1);
        this.hand = hand;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.targetId);
        buffer.writeInt(this.hand.ordinal());
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.targetId = buffer.readInt();
        this.hand = InteractionHand.values()[buffer.readInt()];
    }

    @Override
    public void handle(Player player) {
        LivingEntity target = (LivingEntity) Optional.of(this.targetId).filter(id -> id != -1).map(id -> player.m_9236_().getEntity(id)).filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity) entity).orElse(null);
        ItemModularHandheld.handleSecondaryAbility(player, this.hand, target);
    }
}