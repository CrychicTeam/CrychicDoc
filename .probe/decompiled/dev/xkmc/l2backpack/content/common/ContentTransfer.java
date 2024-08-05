package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.drawer.IDrawerHandler;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2backpack.init.advancement.BagInteractTrigger;
import dev.xkmc.l2backpack.init.data.LangData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContentTransfer {

    public static int transfer(List<ItemStack> list, IItemHandler cap) {
        int n = list.size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            ItemStack stack = (ItemStack) list.get(i);
            count += stack.getCount();
            stack = ItemHandlerHelper.insertItemStacked(cap, stack, false);
            count -= stack.getCount();
            list.set(i, stack);
        }
        return count;
    }

    public static int transfer(Item item, int count, IItemHandler cap) {
        int maxSize = cap instanceof IDrawerHandler ? count : item.getMaxStackSize();
        while (count > 0) {
            int step = Math.min(maxSize, count);
            ItemStack toInsert = new ItemStack(item, step);
            ItemStack remainer = ItemHandlerHelper.insertItemStacked(cap, toInsert, false);
            count = count - step + remainer.getCount();
            if (!remainer.isEmpty()) {
                return count;
            }
        }
        return 0;
    }

    public static int loadFrom(List<ItemStack> list, IItemHandler cap, Player player, Predicate<ItemStack> pred) {
        SimpleContainer cont = new SimpleContainer((ItemStack[]) list.toArray(new ItemStack[0]));
        IItemHandler handler = new InvWrapper(cont);
        int n = cap.getSlots();
        int count = 0;
        int maxAttempt;
        ItemStack removalReal;
        ItemStack error;
        for (int i = 0; i < n; i++) {
            do {
                ItemStack stack = cap.getStackInSlot(i);
                if (stack.isEmpty() || !pred.test(stack)) {
                    break;
                }
                if (!stack.isStackable()) {
                    boolean hasSpace = false;
                    for (int j = 0; j < list.size(); j++) {
                        if (cont.getItem(j).isEmpty()) {
                            hasSpace = true;
                            break;
                        }
                    }
                    if (hasSpace) {
                        ItemStack removal = cap.extractItem(i, 1, false);
                        ItemStack errorx = ItemHandlerHelper.insertItemStacked(handler, removal, false);
                        if (!errorx.isEmpty()) {
                            player.drop(errorx, true);
                        } else {
                            count++;
                        }
                    }
                    break;
                }
                maxAttempt = Math.min(stack.getCount(), stack.getMaxStackSize());
                ItemStack removalSim = cap.extractItem(i, maxAttempt, true);
                ItemStack remain = ItemHandlerHelper.insertItemStacked(handler, removalSim, true);
                if (removalSim.getCount() == remain.getCount()) {
                    break;
                }
                removalReal = cap.extractItem(i, removalSim.getCount() - remain.getCount(), false);
                error = ItemHandlerHelper.insertItemStacked(handler, removalReal, false);
                count += removalReal.getCount() - error.getCount();
                if (!error.isEmpty()) {
                    player.drop(error, true);
                }
            } while (!error.isEmpty() || removalReal.getCount() != maxAttempt);
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, cont.getItem(i));
        }
        return count;
    }

    public static int loadFrom(Item item, int space, IItemHandler cap) {
        int n = cap.getSlots();
        int count = 0;
        label32: for (int i = 0; i < n; i++) {
            if (space <= 0) {
                return count;
            }
            do {
                ItemStack stack = cap.getStackInSlot(i);
                if (stack.isEmpty() || stack.hasTag() || stack.getItem() != item) {
                    continue label32;
                }
                int allow = Math.min(space, Math.min(stack.getMaxStackSize(), stack.getCount()));
                ItemStack removal = cap.extractItem(i, allow, false);
                int toRemove = removal.getCount();
                space -= toRemove;
                count += toRemove;
            } while (space > 0);
            return count;
        }
        return count;
    }

    public static InteractionResult blockInteract(UseOnContext context, ContentTransfer.Quad item) {
        Player player = context.getPlayer();
        if (player != null) {
            BlockPos pos = context.getClickedPos();
            BlockEntity target = context.getLevel().getBlockEntity(pos);
            if (target != null) {
                LazyOptional<IItemHandler> capLazy = target.getCapability(ForgeCapabilities.ITEM_HANDLER);
                if (capLazy.resolve().isPresent()) {
                    IItemHandler cap = (IItemHandler) capLazy.resolve().get();
                    item.click(player, context.getItemInHand(), context.getLevel().isClientSide(), player.m_6144_(), true, cap);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void leftClick(ContentTransfer.Quad load, Level level, BlockPos pos, ItemStack stack, @Nullable Player player) {
        if (player != null) {
            BlockEntity target = level.getBlockEntity(pos);
            if (target != null) {
                LazyOptional<IItemHandler> capLazy = target.getCapability(ForgeCapabilities.ITEM_HANDLER);
                if (capLazy.resolve().isPresent()) {
                    IItemHandler cap = (IItemHandler) capLazy.resolve().get();
                    load.click(player, stack, level.isClientSide(), player.m_6144_(), false, cap);
                }
            }
        }
    }

    public static void onDump(Player player, int count, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(LangData.IDS.DUMP_FEEDBACK.get(count), true);
            BackpackTriggers.INTERACT.trigger(serverPlayer, stack, BagInteractTrigger.Type.DUMP, count);
        }
    }

    public static void onLoad(Player player, int count, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer && count > 0) {
            serverPlayer.sendSystemMessage(LangData.IDS.LOAD_FEEDBACK.get(count), true);
            BackpackTriggers.INTERACT.trigger(serverPlayer, stack, BagInteractTrigger.Type.LOAD, count);
        }
    }

    public static void onExtract(Player player, int count, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(LangData.IDS.EXTRACT_FEEDBACK.get(count), true);
            BackpackTriggers.INTERACT.trigger(serverPlayer, stack, BagInteractTrigger.Type.EXTRACT, count);
        }
    }

    public static void onCollect(Player player, int count, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(LangData.IDS.COLLECT_FEEDBACK.get(count), true);
            BackpackTriggers.INTERACT.trigger(serverPlayer, stack, BagInteractTrigger.Type.COLLECT, count);
        }
    }

    public static void playSound(Player player) {
        player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
    }

    public static void playDrawerSound(Player player) {
        player.playSound(SoundEvents.AMETHYST_BLOCK_PLACE, 1.0F, 1.0F);
    }

    public static Item filterMaxItem(IItemHandler target) {
        Map<Item, Integer> map = new HashMap();
        for (int i = 0; i < target.getSlots(); i++) {
            ItemStack stack = target.getStackInSlot(i);
            if (!stack.hasTag()) {
                map.compute(stack.getItem(), (k, v) -> (v == null ? 0 : v) + stack.getCount());
            }
        }
        Item max = Items.AIR;
        int count = 0;
        for (Entry<Item, Integer> ent : map.entrySet()) {
            if ((Integer) ent.getValue() > count) {
                max = (Item) ent.getKey();
                count = (Integer) ent.getValue();
            }
        }
        return max;
    }

    public interface Quad {

        void click(Player var1, ItemStack var2, boolean var3, boolean var4, boolean var5, @Nullable IItemHandler var6);
    }
}