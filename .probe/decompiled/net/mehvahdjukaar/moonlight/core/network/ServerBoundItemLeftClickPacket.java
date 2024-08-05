package net.mehvahdjukaar.moonlight.core.network;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.item.ILeftClickReact;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record ServerBoundItemLeftClickPacket(InteractionHand hand) implements Message {

    public ServerBoundItemLeftClickPacket(FriendlyByteBuf buf) {
        this(buf.readEnum(InteractionHand.class));
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeEnum(this.hand);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        Player player = (Player) Objects.requireNonNull(context.getSender());
        ItemStack stack = player.m_21120_(this.hand);
        if (stack.getItem() instanceof ILeftClickReact lr) {
            lr.onLeftClick(stack, player, this.hand);
        }
    }
}