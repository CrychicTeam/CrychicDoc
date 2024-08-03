package dev.xkmc.l2hostility.content.item.wand;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class TraitAdderWand extends BaseWand {

    private static final String TRAIT = "l2hostility_trait";

    public static ItemStack set(ItemStack ans, MobTrait trait) {
        ans.getOrCreateTag().putString("l2hostility_trait", trait.getID());
        return ans;
    }

    public static MobTrait get(ItemStack stack) {
        if (stack.getOrCreateTag().contains("l2hostility_trait", 8)) {
            String str = stack.getOrCreateTag().getString("l2hostility_trait");
            ResourceLocation id = new ResourceLocation(str);
            MobTrait ans = LHTraits.TRAITS.get().getValue(id);
            if (ans != null) {
                return ans;
            }
        }
        return (MobTrait) LHTraits.TANK.get();
    }

    private static List<MobTrait> values() {
        return new ArrayList(LHTraits.TRAITS.get().getValues());
    }

    private static MobTrait next(MobTrait mod) {
        List<MobTrait> list = values();
        int index = list.indexOf(mod);
        return index + 1 >= list.size() ? (MobTrait) list.get(0) : (MobTrait) list.get(index + 1);
    }

    private static MobTrait prev(MobTrait mod) {
        List<MobTrait> list = values();
        int index = list.indexOf(mod);
        return index == 0 ? (MobTrait) list.get(list.size() - 1) : (MobTrait) list.get(index - 1);
    }

    @Nullable
    public static Integer decrease(MobTrait k, @Nullable Integer old) {
        if (old != null && old != 0) {
            return old == 1 ? null : old - 1;
        } else {
            return k.getMaxLevel();
        }
    }

    @Nullable
    public static Integer increase(MobTrait k, @Nullable Integer old) {
        if (old == null) {
            return 1;
        } else {
            return old == k.getMaxLevel() ? null : old + 1;
        }
    }

    public TraitAdderWand(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void clickTarget(ItemStack stack, Player player, LivingEntity target) {
        if (MobTraitCap.HOLDER.isProper(target)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(target);
            MobTrait trait = get(stack);
            Integer ans;
            if (player.m_6144_()) {
                ans = (Integer) cap.traits.compute(trait, TraitAdderWand::decrease);
            } else {
                ans = (Integer) cap.traits.compute(trait, TraitAdderWand::increase);
            }
            int val = ans == null ? 0 : ans;
            trait.initialize(target, val);
            trait.postInit(target, val);
            cap.syncToClient(target);
            target.setHealth(target.getMaxHealth());
            player.m_213846_(LangData.MSG_SET_TRAIT.get(trait.getDesc(), target.m_5446_(), val));
        }
    }

    @Override
    public void clickNothing(ItemStack stack, Player player) {
        MobTrait old = get(stack);
        MobTrait next;
        if (player.m_6144_()) {
            next = prev(old);
        } else {
            next = next(old);
        }
        set(stack, next);
        player.m_213846_(LangData.MSG_SELECT_TRAIT.get(next.getDesc()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_WAND_ADDER.get().withStyle(ChatFormatting.GRAY));
        MobTrait trait = get(stack);
        list.add(LangData.MSG_SELECT_TRAIT.get(trait.getDesc().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
    }
}