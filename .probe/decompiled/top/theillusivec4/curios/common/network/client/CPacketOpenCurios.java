package top.theillusivec4.curios.common.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerProvider;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.SPacketGrabbedItem;

public class CPacketOpenCurios {

    private final ItemStack carried;

    public CPacketOpenCurios(ItemStack stack) {
        this.carried = stack;
    }

    public static void encode(CPacketOpenCurios msg, FriendlyByteBuf buf) {
        buf.writeItem(msg.carried);
    }

    public static CPacketOpenCurios decode(FriendlyByteBuf buf) {
        return new CPacketOpenCurios(buf.readItem());
    }

    public static void handle(CPacketOpenCurios msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                ItemStack stack = sender.isCreative() ? msg.carried : sender.f_36096_.getCarried();
                sender.f_36096_.setCarried(ItemStack.EMPTY);
                NetworkHooks.openScreen(sender, new CuriosContainerProvider());
                if (!stack.isEmpty()) {
                    sender.f_36096_.setCarried(stack);
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> sender), new SPacketGrabbedItem(stack));
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}