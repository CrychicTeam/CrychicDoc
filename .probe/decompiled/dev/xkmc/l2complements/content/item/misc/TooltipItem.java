package dev.xkmc.l2complements.content.item.misc;

import dev.xkmc.l2complements.init.data.LangData;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TooltipItem extends Item {

    private final Supplier<MutableComponent> sup;

    public TooltipItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties);
        this.sup = sup;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        MutableComponent e = (MutableComponent) this.sup.get();
        if (e == null) {
            list.add(LangData.IDS.BANNED.get().withStyle(ChatFormatting.RED));
        } else {
            list.add(e.withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, list, flag);
    }

    @Override
    public boolean canBeHurtBy(DamageSource source) {
        return this.m_41460_(this.m_7968_()) == Rarity.COMMON || !source.is(DamageTypeTags.IS_LIGHTNING) && !source.is(DamageTypeTags.IS_FIRE) && !source.is(DamageTypeTags.IS_EXPLOSION);
    }
}