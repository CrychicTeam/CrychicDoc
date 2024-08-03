package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.bindings.event.ItemEvents;
import dev.latvian.mods.kubejs.item.ItemClickedEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class FirstClickMessage extends BaseC2SMessage {

    private final int type;

    public FirstClickMessage(int t) {
        this.type = t;
    }

    FirstClickMessage(FriendlyByteBuf buf) {
        this.type = buf.readByte();
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.FIRST_CLICK;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.type);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            if (this.type == 0 && ItemEvents.FIRST_LEFT_CLICKED.hasListeners()) {
                ItemStack stack = serverPlayer.m_21120_(InteractionHand.MAIN_HAND);
                ItemEvents.FIRST_LEFT_CLICKED.post(ScriptType.SERVER, stack.getItem(), new ItemClickedEventJS(serverPlayer, InteractionHand.MAIN_HAND, stack));
            } else if (this.type == 1 && ItemEvents.FIRST_RIGHT_CLICKED.hasListeners()) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack stack = serverPlayer.m_21120_(hand);
                    ItemEvents.FIRST_RIGHT_CLICKED.post(ScriptType.SERVER, stack.getItem(), new ItemClickedEventJS(serverPlayer, hand, stack));
                }
            }
        }
    }
}