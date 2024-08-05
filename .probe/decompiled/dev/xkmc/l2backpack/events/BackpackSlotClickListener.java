package dev.xkmc.l2backpack.events;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.bag.AbstractBag;
import dev.xkmc.l2backpack.content.capability.PickupBagItem;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.remote.player.EnderBackpackItem;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestItem;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestMenuPvd;
import dev.xkmc.l2backpack.content.tool.IBagTool;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2screentracker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2screentracker.click.writable.ContainerCallback;
import dev.xkmc.l2screentracker.click.writable.WritableStackClickHandler;
import dev.xkmc.l2screentracker.screen.base.ScreenTracker;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class BackpackSlotClickListener extends WritableStackClickHandler {

    public static boolean canOpen(ItemStack stack) {
        return stack.getItem() instanceof BaseBagItem || stack.getItem() instanceof EnderBackpackItem || stack.getItem() instanceof WorldChestItem;
    }

    public BackpackSlotClickListener() {
        super(new ResourceLocation("l2backpack", "backpack"));
    }

    public boolean isAllowed(ItemStack itemStack) {
        return canOpen(itemStack) || itemStack.getItem() instanceof BaseDrawerItem || itemStack.getItem() instanceof AbstractBag;
    }

    public void keyBind() {
        this.slotClickToServer(-1, -1, -1);
    }

    protected ClickedPlayerSlotResult getSlot(ServerPlayer player, int index, int slot, int wid) {
        if (wid == -1) {
            ItemStack stack = player.m_6844_(EquipmentSlot.CHEST);
            if (canOpen(stack)) {
                return new ClickedPlayerSlotResult(stack, PlayerSlot.ofInventory(36 + EquipmentSlot.CHEST.getIndex()), new BackpackSlotClickListener.PlayerInvCallback());
            } else {
                if (!canOpen(stack)) {
                    Optional<Pair<ItemStack, PlayerSlot<?>>> pairOpt = CuriosCompat.getSlot(player, BackpackSlotClickListener::canOpen);
                    if (pairOpt.isPresent()) {
                        return new ClickedPlayerSlotResult((ItemStack) ((Pair) pairOpt.get()).getFirst(), (PlayerSlot) ((Pair) pairOpt.get()).getSecond(), new BackpackSlotClickListener.PlayerInvCallback());
                    }
                }
                return null;
            }
        } else {
            return super.getSlot(player, index, slot, wid);
        }
    }

    public void handle(ServerPlayer player, int index, int slot, int wid) {
        ClickedPlayerSlotResult result = this.getSlot(player, index, slot, wid);
        if (result != null) {
            this.handle(player, result);
        } else {
            this.handleNoMenu(player, index);
        }
    }

    private void handleNoMenu(ServerPlayer player, int index) {
        ItemStack stack = player.f_36096_.getSlot(index).getItem();
        ItemStack carried = player.f_36096_.getCarried();
        if (carried.getItem() instanceof IBagTool tool && stack.getItem() instanceof PickupBagItem) {
            tool.click(stack);
            return;
        }
        if (carried.isEmpty()) {
            if (!(stack.getItem() instanceof BaseDrawerItem)) {
                boolean others = false;
                ScreenTracker.onServerOpen(player);
                if (stack.getItem() instanceof EnderBackpackItem) {
                    NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inv, pl) -> ChestMenu.threeRows(id, inv, pl.getEnderChestInventory()), stack.getHoverName()));
                } else if (stack.getItem() instanceof WorldChestItem chest) {
                    others = (Boolean) WorldChestItem.getOwner(stack).map(e -> !e.equals(player.m_20148_())).orElse(false);
                    new WorldChestMenuPvd(player, stack, chest).open();
                }
                if (others) {
                    BackpackTriggers.SHARE.trigger(player);
                }
            }
        }
    }

    protected void handle(ServerPlayer player, ClickedPlayerSlotResult result) {
        boolean others = false;
        boolean keybind = result.container() instanceof BackpackSlotClickListener.PlayerInvCallback;
        ItemStack carried = player.f_36096_.getCarried();
        if (!keybind && carried.getItem() instanceof IBagTool tool && result.stack().getItem() instanceof PickupBagItem) {
            tool.click(result.stack());
            result.container().update();
        } else if (carried.isEmpty()) {
            if (!(result.stack().getItem() instanceof BaseDrawerItem)) {
                if (!keybind) {
                    ScreenTracker.onServerOpen(player);
                }
                if (result.stack().getItem() instanceof BaseBagItem bag) {
                    bag.open(player, result.slot(), result.stack());
                    result.container().update();
                } else if (result.stack().getItem() instanceof EnderBackpackItem) {
                    NetworkHooks.openScreen(player, new SimpleMenuProvider((id, inv, pl) -> ChestMenu.threeRows(id, inv, pl.getEnderChestInventory()), result.stack().getHoverName()));
                } else if (result.stack().getItem() instanceof WorldChestItem chest) {
                    others = (Boolean) WorldChestItem.getOwner(result.stack()).map(e -> !e.equals(player.m_20148_())).orElse(false);
                    new WorldChestMenuPvd(player, result.stack(), chest).open();
                    result.container().update();
                }
                BackpackTriggers.SLOT_CLICK.trigger(player, result.slot().type(), keybind);
                if (others) {
                    BackpackTriggers.SHARE.trigger(player);
                }
            }
        }
    }

    public static record PlayerInvCallback() implements ContainerCallback {

        public void update() {
        }
    }
}