package top.theillusivec4.curios.common.network.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotAttribute;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;

public class CPacketDestroy {

    public static void encode(CPacketDestroy msg, FriendlyByteBuf buf) {
    }

    public static CPacketDestroy decode(FriendlyByteBuf buf) {
        return new CPacketDestroy();
    }

    public static void handle(CPacketDestroy msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer sender = ((NetworkEvent.Context) ctx.get()).getSender();
            if (sender != null) {
                CuriosApi.getCuriosInventory(sender).ifPresent(handler -> handler.getCurios().values().forEach(stacksHandler -> {
                    IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                    IDynamicStackHandler cosmeticStackHandler = stacksHandler.getCosmeticStacks();
                    String id = stacksHandler.getIdentifier();
                    for (int i = 0; i < stackHandler.getSlots(); i++) {
                        NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                        SlotContext slotContext = new SlotContext(id, sender, i, false, renderStates.size() > i && renderStates.get(i));
                        UUID uuid = CuriosApi.getSlotUuid(slotContext);
                        ItemStack stack = stackHandler.getStackInSlot(i);
                        Multimap<Attribute, AttributeModifier> map = CuriosApi.getAttributeModifiers(slotContext, uuid, stack);
                        Multimap<String, AttributeModifier> slots = HashMultimap.create();
                        Set<SlotAttribute> toRemove = new HashSet();
                        for (Attribute attribute : map.keySet()) {
                            if (attribute instanceof SlotAttribute wrapper) {
                                slots.putAll(wrapper.getIdentifier(), map.get(attribute));
                                toRemove.add(wrapper);
                            }
                        }
                        for (Attribute attributex : toRemove) {
                            map.removeAll(attributex);
                        }
                        sender.m_21204_().removeAttributeModifiers(map);
                        handler.removeSlotModifiers(slots);
                        CuriosApi.getCurio(stack).ifPresent(curio -> curio.onUnequip(slotContext, stack));
                        stackHandler.setStackInSlot(i, ItemStack.EMPTY);
                        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender), new SPacketSyncStack(sender.m_19879_(), id, i, ItemStack.EMPTY, SPacketSyncStack.HandlerType.EQUIPMENT, new CompoundTag()));
                        cosmeticStackHandler.setStackInSlot(i, ItemStack.EMPTY);
                        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> sender), new SPacketSyncStack(sender.m_19879_(), id, i, ItemStack.EMPTY, SPacketSyncStack.HandlerType.COSMETIC, new CompoundTag()));
                    }
                }));
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}