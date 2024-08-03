package io.redspace.ironsspellbooks.item.curios;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.compat.Curios;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LurkerRing extends SimpleDescriptiveCurio {

    public static final int COOLDOWN_IN_TICKS = 300;

    public static final float MULTIPLIER = 1.5F;

    public LurkerRing() {
        super(new Item.Properties().stacksTo(1), Curios.RING_SLOT);
    }

    @Override
    public List<Component> getDescriptionLines(ItemStack stack) {
        double playerCooldownModifier = Minecraft.getInstance().player == null ? 1.0 : Minecraft.getInstance().player.m_21133_(AttributeRegistry.COOLDOWN_REDUCTION.get());
        return List.of(Component.translatable("tooltip.irons_spellbooks.passive_ability", Utils.timeFromTicks((float) (300.0 * (2.0 - Utils.softCapFormula(playerCooldownModifier))), 1)).withStyle(ChatFormatting.GREEN), this.getDescription(stack));
    }

    @Override
    public Component getDescription(ItemStack stack) {
        return Component.literal(" ").append(Component.translatable(this.m_5524_() + ".desc", 50)).withStyle(this.descriptionStyle);
    }
}