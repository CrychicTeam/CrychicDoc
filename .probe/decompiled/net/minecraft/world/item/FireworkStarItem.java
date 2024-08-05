package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

public class FireworkStarItem extends Item {

    public FireworkStarItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        CompoundTag $$4 = itemStack0.getTagElement("Explosion");
        if ($$4 != null) {
            appendHoverText($$4, listComponent2);
        }
    }

    public static void appendHoverText(CompoundTag compoundTag0, List<Component> listComponent1) {
        FireworkRocketItem.Shape $$2 = FireworkRocketItem.Shape.byId(compoundTag0.getByte("Type"));
        listComponent1.add(Component.translatable("item.minecraft.firework_star.shape." + $$2.getName()).withStyle(ChatFormatting.GRAY));
        int[] $$3 = compoundTag0.getIntArray("Colors");
        if ($$3.length > 0) {
            listComponent1.add(appendColors(Component.empty().withStyle(ChatFormatting.GRAY), $$3));
        }
        int[] $$4 = compoundTag0.getIntArray("FadeColors");
        if ($$4.length > 0) {
            listComponent1.add(appendColors(Component.translatable("item.minecraft.firework_star.fade_to").append(CommonComponents.SPACE).withStyle(ChatFormatting.GRAY), $$4));
        }
        if (compoundTag0.getBoolean("Trail")) {
            listComponent1.add(Component.translatable("item.minecraft.firework_star.trail").withStyle(ChatFormatting.GRAY));
        }
        if (compoundTag0.getBoolean("Flicker")) {
            listComponent1.add(Component.translatable("item.minecraft.firework_star.flicker").withStyle(ChatFormatting.GRAY));
        }
    }

    private static Component appendColors(MutableComponent mutableComponent0, int[] int1) {
        for (int $$2 = 0; $$2 < int1.length; $$2++) {
            if ($$2 > 0) {
                mutableComponent0.append(", ");
            }
            mutableComponent0.append(getColorName(int1[$$2]));
        }
        return mutableComponent0;
    }

    private static Component getColorName(int int0) {
        DyeColor $$1 = DyeColor.byFireworkColor(int0);
        return $$1 == null ? Component.translatable("item.minecraft.firework_star.custom_color") : Component.translatable("item.minecraft.firework_star." + $$1.getName());
    }
}