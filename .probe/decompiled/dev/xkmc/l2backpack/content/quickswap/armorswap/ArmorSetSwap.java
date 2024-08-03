package dev.xkmc.l2backpack.content.quickswap.armorswap;

import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SetSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SetSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SimpleMenuPvd;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorSetSwap extends SetSwapItem implements ItemOnBackItem {

    public ArmorSetSwap(Item.Properties props) {
        super(props, 4);
    }

    @Override
    public void open(ServerPlayer player, PlayerSlot<?> slot, ItemStack stack) {
        new SimpleMenuPvd(player, slot, this, stack, ArmorSetBagMenu::new).open();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        LangData.addInfo(list, LangData.Info.SUIT_BAG_INFO, LangData.Info.INHERIT);
    }

    @Nullable
    @Override
    public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity player, QuickSwapType type) {
        return type != QuickSwapTypes.ARMOR ? null : new SetSwapToken(this, stack, type);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        EquipmentSlot e = LivingEntity.getEquipmentSlotForItem(stack);
        return e.getType() != EquipmentSlot.Type.ARMOR ? false : slot / 9 + e.ordinal() == 5;
    }

    @Override
    public boolean isValidContent(ItemStack stack) {
        return ArmorSwap.isValidItem(stack);
    }
}