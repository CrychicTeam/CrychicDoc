package top.theillusivec4.curios.common.network.server.sync;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class SPacketSyncStack {

    private int entityId;

    private int slotId;

    private String curioId;

    private ItemStack stack;

    private int handlerType;

    private CompoundTag compound;

    public SPacketSyncStack(int entityId, String curioId, int slotId, ItemStack stack, SPacketSyncStack.HandlerType handlerType, CompoundTag data) {
        this.entityId = entityId;
        this.slotId = slotId;
        this.stack = stack.copy();
        this.curioId = curioId;
        this.handlerType = handlerType.ordinal();
        this.compound = data;
    }

    public static void encode(SPacketSyncStack msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeUtf(msg.curioId);
        buf.writeInt(msg.slotId);
        buf.writeItem(msg.stack);
        buf.writeInt(msg.handlerType);
        buf.writeNbt(msg.compound);
    }

    public static SPacketSyncStack decode(FriendlyByteBuf buf) {
        return new SPacketSyncStack(buf.readInt(), buf.readUtf(), buf.readInt(), buf.readItem(), SPacketSyncStack.HandlerType.fromValue(buf.readInt()), buf.readNbt());
    }

    public static void handle(SPacketSyncStack msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Entity entity = world.getEntity(msg.entityId);
                if (entity instanceof LivingEntity) {
                    CuriosApi.getCuriosInventory((LivingEntity) entity).ifPresent(handler -> handler.getStacksHandler(msg.curioId).ifPresent(stacksHandler -> {
                        ItemStack stack = msg.stack;
                        CompoundTag compoundNBT = msg.compound;
                        int slot = msg.slotId;
                        boolean cosmetic = SPacketSyncStack.HandlerType.fromValue(msg.handlerType) == SPacketSyncStack.HandlerType.COSMETIC;
                        if (!compoundNBT.isEmpty()) {
                            NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                            CuriosApi.getCurio(stack).ifPresent(curio -> curio.readSyncData(new SlotContext(msg.curioId, (LivingEntity) entity, slot, cosmetic, renderStates.size() > slot && renderStates.get(slot)), compoundNBT));
                        }
                        if (cosmetic) {
                            stacksHandler.getCosmeticStacks().setStackInSlot(slot, stack);
                        } else {
                            stacksHandler.getStacks().setStackInSlot(slot, stack);
                        }
                    }));
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum HandlerType {

        EQUIPMENT, COSMETIC;

        public static SPacketSyncStack.HandlerType fromValue(int value) {
            try {
                return values()[value];
            } catch (ArrayIndexOutOfBoundsException var2) {
                throw new IllegalArgumentException("Unknown handler value: " + value);
            }
        }
    }
}