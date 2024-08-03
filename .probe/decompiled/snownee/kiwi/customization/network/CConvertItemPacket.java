package snownee.kiwi.customization.network;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.IntStream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.block.family.BlockFamilies;
import snownee.kiwi.customization.block.family.BlockFamily;
import snownee.kiwi.network.KiwiPacket;
import snownee.kiwi.network.PacketHandler;
import snownee.kiwi.util.KHolder;

@KiwiPacket(value = "convert_item", dir = KiwiPacket.Direction.PLAY_TO_SERVER)
public class CConvertItemPacket extends PacketHandler {

    public static final int MAX_STEPS = 4;

    public static CConvertItemPacket I;

    public static void send(boolean inContainer, int slot, CConvertItemPacket.Entry entry, Item from, boolean convertOne) {
        I.sendToServer(buf -> {
            buf.writeBoolean(inContainer);
            buf.writeBoolean(convertOne);
            buf.writeVarInt(slot);
            buf.writeId(BuiltInRegistries.ITEM, from);
            buf.writeVarInt(entry.steps().size());
            for (Pair<KHolder<BlockFamily>, Item> step : entry.steps()) {
                buf.writeResourceLocation(((KHolder) step.getFirst()).key());
                buf.writeId(BuiltInRegistries.ITEM, (Item) step.getSecond());
            }
        });
    }

    @Override
    public CompletableFuture<FriendlyByteBuf> receive(Function<Runnable, CompletableFuture<FriendlyByteBuf>> function, FriendlyByteBuf buf, @Nullable ServerPlayer player) {
        boolean inContainer = buf.readBoolean();
        boolean convertOne = buf.readBoolean();
        int slotIndex = buf.readVarInt();
        Item from = buf.readById(BuiltInRegistries.ITEM);
        if (from == null) {
            return null;
        } else {
            int size = buf.readVarInt();
            List<Pair<ResourceLocation, Item>> steps = Lists.newArrayListWithExpectedSize(size);
            for (int i = 0; i < size; i++) {
                ResourceLocation familyId = buf.readResourceLocation();
                Item item = buf.readById(BuiltInRegistries.ITEM);
                if (item == null) {
                    return null;
                }
                steps.add(Pair.of(familyId, item));
            }
            if (!steps.isEmpty() && steps.size() <= 4) {
                Item to = (Item) ((Pair) steps.get(steps.size() - 1)).getSecond();
                return from == to ? null : (CompletableFuture) function.apply((Runnable) () -> {
                    Objects.requireNonNull(player);
                    Item itemx = from;
                    int index = 0;
                    float ratio = 1.0F;
                    for (Pair<ResourceLocation, Item> step : steps) {
                        BlockFamily family = BlockFamilies.get((ResourceLocation) step.getFirst());
                        if (family == null || !family.switchAttrs().enabled() || !family.contains(itemx) || !family.contains((Item) step.getSecond())) {
                            return;
                        }
                        if (!family.switchAttrs().cascading() && index != steps.size() - 1) {
                            return;
                        }
                        if (!player.isCreative()) {
                            ratio *= BlockFamilies.getConvertRatio(itemx) / BlockFamilies.getConvertRatio((Item) step.getSecond());
                        }
                        itemx = (Item) step.getSecond();
                        index++;
                    }
                    Slot slot = null;
                    Inventory playerInventory = player.m_150109_();
                    ItemStack sourceItem;
                    try {
                        if (slotIndex == -500) {
                            if (!player.isCreative()) {
                                return;
                            }
                            sourceItem = from.getDefaultInstance();
                        } else if (inContainer) {
                            slot = player.f_36096_.slots.get(slotIndex);
                            if (!slot.allowModification(player)) {
                                return;
                            }
                            sourceItem = slot.getItem();
                        } else {
                            sourceItem = playerInventory.getItem(slotIndex);
                        }
                    } catch (Exception var21) {
                        return;
                    }
                    if (sourceItem.is(from)) {
                        boolean skipSettingSlot = false;
                        int inventorySwap = Integer.MIN_VALUE;
                        ItemStack newItem;
                        if (ratio >= 1.0F) {
                            newItem = to.getDefaultInstance();
                        } else {
                            if (convertOne) {
                                return;
                            }
                            for (int i = 0; i < playerInventory.getContainerSize(); i++) {
                                ItemStack stack = playerInventory.getItem(i);
                                if (stack.is(to)) {
                                    inventorySwap = i;
                                    break;
                                }
                            }
                            if (inventorySwap == Integer.MIN_VALUE) {
                                return;
                            }
                            newItem = playerInventory.getItem(inventorySwap);
                        }
                        newItem.setPopTime(5);
                        int ratioInt = Mth.floor(ratio);
                        if (convertOne) {
                            if (!player.isCreative()) {
                                sourceItem.shrink(1);
                                newItem.setCount(ratioInt);
                            }
                            if (!sourceItem.isEmpty()) {
                                this.addToPlayer(player, newItem, !inContainer);
                                skipSettingSlot = true;
                            }
                        } else if (inventorySwap == Integer.MIN_VALUE) {
                            int maxSize = newItem.getMaxStackSize();
                            int count = Math.min(sourceItem.getCount(), maxSize / ratioInt);
                            newItem.setCount(count * ratioInt);
                            if (!player.isCreative()) {
                                sourceItem.shrink(count);
                            }
                        }
                        if (slotIndex != -500 && !skipSettingSlot) {
                            try {
                                if (inContainer) {
                                    if (!slot.mayPlace(newItem)) {
                                        return;
                                    }
                                    slot.setByPlayer(newItem);
                                } else {
                                    playerInventory.setItem(slotIndex, newItem);
                                }
                            } catch (Exception var20) {
                                return;
                            }
                        }
                        if (inventorySwap != Integer.MIN_VALUE) {
                            playerInventory.setItem(inventorySwap, sourceItem);
                        } else if (!skipSettingSlot && !player.isCreative()) {
                            this.addToPlayer(player, sourceItem.copy(), !inContainer);
                        }
                        playPickupSound(player);
                        player.f_36096_.broadcastChanges();
                    }
                });
            } else {
                return null;
            }
        }
    }

    private void addToPlayer(ServerPlayer player, ItemStack itemStack, boolean nextToSelected) {
        Inventory inventory = player.m_150109_();
        IntStream intStream = IntStream.range(0, 9);
        if (nextToSelected) {
            IntStream leftAndRight = IntStream.of(new int[] { inventory.selected + 1, inventory.selected - 1 });
            intStream = IntStream.concat(leftAndRight, intStream);
        }
        int slot = intStream.filter(Inventory::m_36045_).filter(i -> {
            ItemStack stack = inventory.getItem(i);
            return stack.isEmpty() ? true : stack.getCount() < stack.getMaxStackSize() && ItemStack.isSameItemSameTags(stack, itemStack);
        }).findFirst().orElse(-1);
        if (!inventory.add(slot, itemStack) && !player.isCreative()) {
            player.m_36176_(itemStack, true);
        }
    }

    public static void playPickupSound(Player player) {
        player.m_9236_().playSound(player.isLocalPlayer() ? player : null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.m_217043_().nextFloat() - player.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }

    public static record Entry(float ratio, List<Pair<KHolder<BlockFamily>, Item>> steps) {

        public Entry(float ratio) {
            this(ratio, Lists.newArrayList());
        }

        public Item item() {
            return (Item) ((Pair) this.steps.get(this.steps.size() - 1)).getSecond();
        }
    }

    public static record Group(LinkedHashSet<CConvertItemPacket.Entry> entries) {

        public Group() {
            this(Sets.newLinkedHashSet());
        }
    }
}