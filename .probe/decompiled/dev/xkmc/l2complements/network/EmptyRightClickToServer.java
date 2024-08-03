package dev.xkmc.l2complements.network;

import dev.xkmc.l2complements.events.ItemUseEventHandler;
import dev.xkmc.l2complements.init.L2Complements;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class EmptyRightClickToServer extends SerialPacketBase {

    @SerialField
    public boolean hand;

    @SerialField
    public boolean right;

    public EmptyRightClickToServer(boolean right, boolean hand) {
        this.right = right;
        this.hand = hand;
    }

    @Deprecated
    public EmptyRightClickToServer() {
        this(true, true);
    }

    public void handle(NetworkEvent.Context ctx) {
        ServerPlayer pl = ctx.getSender();
        if (pl != null) {
            if (this.right) {
                PlayerInteractEvent.RightClickEmpty event = new PlayerInteractEvent.RightClickEmpty(pl, this.hand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                ItemUseEventHandler.onPlayerRightClickEmpty(event);
            } else {
                PlayerInteractEvent.LeftClickEmpty event = new PlayerInteractEvent.LeftClickEmpty(pl);
                ItemUseEventHandler.onPlayerLeftClickEmpty(event);
            }
        }
    }

    public void toServer() {
        L2Complements.HANDLER.toServer(this);
    }
}