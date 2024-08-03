package dev.xkmc.l2backpack.content.quickswap.quiver;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapToken;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapTypes;
import dev.xkmc.l2backpack.content.render.ItemOnBackItem;
import dev.xkmc.l2backpack.init.data.LangData;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Quiver extends SingleSwapItem implements ItemOnBackItem {

    public static float displayArrow(ItemStack stack) {
        int disp = 0;
        for (ItemStack arrow : getItems(stack)) {
            if (!arrow.isEmpty()) {
                disp++;
            }
        }
        return disp == 0 ? 0.0F : (float) (Math.ceil((double) ((float) disp / 3.0F)) + 0.5);
    }

    public static boolean isValidStack(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems() && stack.getItem() instanceof ArrowItem;
    }

    public Quiver(Item.Properties props) {
        super(props.stacksTo(1).fireResistant());
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new SimpleMenuPvd(player, slot, this, stack, QuiverMenu::new).open();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        PickupConfig.addText(stack, list);
        LangData.addInfo(list, LangData.Info.ARROW_INFO, LangData.Info.INHERIT);
    }

    @Nullable
    @Override
    public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity player, QuickSwapType type) {
        if (type != QuickSwapTypes.ARROW) {
            return null;
        } else if (player.getMainHandItem().getItem() instanceof ProjectileWeaponItem bow) {
            List<ItemStack> list = getItems(stack);
            if (list.isEmpty()) {
                return null;
            } else {
                for (ItemStack arrow : list) {
                    if (!arrow.isEmpty() && bow.getAllSupportedProjectiles().test(arrow)) {
                        return new SingleSwapToken(this, stack, QuickSwapTypes.ARROW);
                    }
                }
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return isValidStack(stack);
    }
}