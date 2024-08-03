package top.theillusivec4.curios.common.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class SPacketBreak {

    private int entityId;

    private int slotId;

    private String curioId;

    public SPacketBreak(int entityId, String curioId, int slotId) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.curioId = curioId;
    }

    public static void encode(SPacketBreak msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeUtf(msg.curioId);
        buf.writeInt(msg.slotId);
    }

    public static SPacketBreak decode(FriendlyByteBuf buf) {
        return new SPacketBreak(buf.readInt(), buf.readUtf(), buf.readInt());
    }

    public static void handle(SPacketBreak msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null && Minecraft.getInstance().level.getEntity(msg.entityId) instanceof LivingEntity livingEntity) {
                CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> handler.getStacksHandler(msg.curioId).ifPresent(stacks -> {
                    ItemStack stack = stacks.getStacks().getStackInSlot(msg.slotId);
                    LazyOptional<ICurio> possibleCurio = CuriosApi.getCurio(stack);
                    NonNullList<Boolean> renderStates = stacks.getRenders();
                    possibleCurio.ifPresent(curio -> curio.curioBreak(new SlotContext(msg.curioId, livingEntity, msg.slotId, false, renderStates.size() > msg.slotId && renderStates.get(msg.slotId))));
                    if (!possibleCurio.isPresent()) {
                        ICurio.playBreakAnimation(stack, livingEntity);
                    }
                }));
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}