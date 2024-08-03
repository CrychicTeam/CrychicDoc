package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.quickswap.armorswap.ArmorSwap;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
import dev.xkmc.l2backpack.content.quickswap.quiver.Quiver;
import dev.xkmc.l2backpack.content.quickswap.scabbard.Scabbard;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.content.render.ItemOnBackItem;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiSwitch extends BaseBagItem implements IQuickSwapItem, ItemOnBackItem {

    public static void setSelected(ItemStack stack, QuickSwapType type, int i) {
        int slot = i;
        if (i < 0) {
            slot = getSelected(stack, type);
            if (i == -1) {
                slot--;
            } else {
                slot++;
            }
            slot = (slot + 9) % 9;
        }
        stack.getOrCreateTag().putInt("selected_" + type.getName(), slot);
    }

    public static int getSelected(ItemStack stack, QuickSwapType type) {
        return Mth.clamp(stack.getOrCreateTag().getInt("selected_" + type.getName()), 0, 8);
    }

    public MultiSwitch(Item.Properties props) {
        super(props.stacksTo(1).fireResistant());
    }

    @Override
    public int getRows(ItemStack stack) {
        return 3;
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new SimpleMenuPvd(player, slot, this, stack, MultiSwitchMenu::new).open();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        PickupConfig.addText(stack, list);
        LangData.addInfo(list, LangData.Info.MULTI_SWITCH_INFO, LangData.Info.INHERIT);
    }

    @Nullable
    @Override
    public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity player, QuickSwapType type) {
        if (type.getIndex() >= 3) {
            return null;
        } else {
            List<ItemStack> list = getItems(stack);
            return list.isEmpty() ? null : new MultiSwapToken(this, stack, type);
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch(slot / 9) {
            case 0 ->
                Quiver.isValidStack(stack);
            case 1 ->
                Scabbard.isValidItem(stack);
            case 2 ->
                ArmorSwap.isValidItem(stack);
            default ->
                false;
        };
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return true;
    }

    @Override
    public void click(Player player, ItemStack stack, boolean client, boolean shift, boolean right, @Nullable IItemHandler target) {
        if (!client && shift && right && target != null) {
            List<ItemStack> list = getItems(stack);
            int moved = ContentTransfer.transfer(list, target);
            setItems(stack, list);
            ContentTransfer.onDump(player, moved, stack);
        } else if (client && shift && right && target != null) {
            ContentTransfer.playSound(player);
        }
        if (!client && shift && !right && target != null) {
            List<ItemStack> list = getItems(stack);
            List<ItemStack> list_a = list.subList(0, 9);
            List<ItemStack> list_b = list.subList(9, 18);
            List<ItemStack> list_c = list.subList(18, 27);
            int moved_a = ContentTransfer.loadFrom(list_a, target, player, Quiver::isValidStack);
            int moved_b = ContentTransfer.loadFrom(list_b, target, player, Scabbard::isValidItem);
            int moved_c = ContentTransfer.loadFrom(list_c, target, player, ArmorSwap::isValidItem);
            int moved = moved_a + moved_b + moved_c;
            setItems(stack, list);
            ContentTransfer.onLoad(player, moved, stack);
        } else if (client && shift && !right && target != null) {
            ContentTransfer.playSound(player);
        }
    }
}