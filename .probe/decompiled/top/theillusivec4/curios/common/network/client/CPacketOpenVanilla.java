package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketGrabbedItem;

public class CPacketOpenVanilla {

    private final ItemStack carried;

    public CPacketOpenVanilla(ItemStack stack) {
        this.carried = stack;
    }

    public static void encode(CPacketOpenVanilla msg, FriendlyByteBuf buf) {
        buf.writeItem(msg.carried);
    }

    public static CPacketOpenVanilla decode(FriendlyByteBuf buf) {
        return new CPacketOpenVanilla(buf.readItem());
    }

    public static void handle(CPacketOpenVanilla msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                ItemStack stack = sender.isCreative() ? msg.carried : sender.f_36096_.getCarried();
                sender.f_36096_.setCarried(ItemStack.EMPTY);
                sender.doCloseContainer();
                if (!stack.isEmpty()) {
                    if (!sender.isCreative()) {
                        sender.f_36096_.setCarried(stack);
                    }
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), new SPacketGrabbedItem(stack));
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}