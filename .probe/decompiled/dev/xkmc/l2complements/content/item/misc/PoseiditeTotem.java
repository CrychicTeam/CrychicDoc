package dev.xkmc.l2complements.content.item.misc;

import dev.xkmc.l2complements.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PoseiditeTotem extends Item implements ILCTotem {

    public PoseiditeTotem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean allow(LivingEntity self, DamageSource pDamageSource) {
        return !pDamageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && self.m_20071_();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.IDS.TOTEM_SEA.get().withStyle(ChatFormatting.GRAY));
    }
}