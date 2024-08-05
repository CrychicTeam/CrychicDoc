package dev.xkmc.l2backpack.content.quickswap.type;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.scabbard.Scabbard;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class QuickSwapManager {

    @Nullable
    public static QuickSwapType getValidType(LivingEntity player, boolean isAltDown) {
        QuickSwapType main = getValidType(player, player.getMainHandItem(), isAltDown);
        if (main != null) {
            return main;
        } else {
            for (MatcherSwapType e : QuickSwapTypes.MATCHER) {
                if (e.allowsOffhand() && e.match(player.getOffhandItem())) {
                    return e;
                }
            }
            return null;
        }
    }

    @Nullable
    public static QuickSwapType getValidType(LivingEntity player, ItemStack focus, boolean isAltDown) {
        if (isAltDown && Scabbard.isValidItem(focus)) {
            return QuickSwapTypes.TOOL;
        } else {
            for (MatcherSwapType e : QuickSwapTypes.MATCHER) {
                if (e.match(focus)) {
                    return e;
                }
            }
            if ((!isAltDown || !focus.isEmpty()) && !Scabbard.isValidItem(focus)) {
                return focus.isEmpty() ? QuickSwapTypes.ARMOR : null;
            } else {
                return QuickSwapTypes.TOOL;
            }
        }
    }

    @Nullable
    public static IQuickSwapToken<?> getToken(LivingEntity user, boolean isAltDown) {
        return getToken(user, null, isAltDown);
    }

    @Nullable
    public static IQuickSwapToken<?> getToken(LivingEntity user, @Nullable ItemStack focus, boolean isAltDown) {
        List<ItemStack> list = new ArrayList();
        list.add(user.getOffhandItem());
        list.add(user.getItemBySlot(EquipmentSlot.CHEST));
        Optional<Pair<ItemStack, PlayerSlot<?>>> opt = CuriosCompat.getSlot(user, stackx -> stackx.getItem() instanceof IQuickSwapItem);
        opt.ifPresent(pair -> list.add((ItemStack) pair.getFirst()));
        QuickSwapType type = focus == null ? getValidType(user, isAltDown) : getValidType(user, focus, isAltDown);
        if (type == null) {
            return null;
        } else {
            for (ItemStack stack : list) {
                if (stack.getItem() instanceof IQuickSwapItem item) {
                    IQuickSwapToken<?> token = item.getTokenOfType(stack, user, type);
                    if (token != null) {
                        return token;
                    }
                }
            }
            return null;
        }
    }
}