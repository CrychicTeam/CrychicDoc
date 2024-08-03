package dev.xkmc.l2backpack.content.quickswap.scabbard;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Scabbard extends SingleSwapItem implements ItemOnBackItem {

    public static boolean isValidItem(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems() && !stack.isStackable() && LivingEntity.getEquipmentSlotForItem(stack).getType() != EquipmentSlot.Type.ARMOR;
    }

    public Scabbard(Item.Properties props) {
        super(props.stacksTo(1).fireResistant());
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new SimpleMenuPvd(player, slot, this, stack, ScabbardMenu::new).open();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        LangData.addInfo(list, LangData.Info.SCABBARD_INFO, LangData.Info.INHERIT);
    }

    @Nullable
    @Override
    public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity player, QuickSwapType type) {
        return type != QuickSwapTypes.TOOL ? null : new SingleSwapToken(this, stack, type);
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return isValidItem(stack);
    }
}