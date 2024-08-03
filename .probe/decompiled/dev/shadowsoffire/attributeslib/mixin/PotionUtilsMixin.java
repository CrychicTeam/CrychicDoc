package dev.shadowsoffire.attributeslib.mixin;

import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ PotionUtils.class })
public class PotionUtilsMixin {

    @Redirect(method = { "addPotionTooltip(Ljava/util/List;Ljava/util/List;F)V" }, at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1), require = 1)
    private static boolean attributeslib_potionTooltips(List<Pair<Attribute, AttributeModifier>> list, List<MobEffectInstance> effects, List<Component> tooltips, float durationFactor) {
        if (!list.isEmpty()) {
            tooltips.add(CommonComponents.EMPTY);
            tooltips.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            for (Pair<Attribute, AttributeModifier> pair : list) {
                tooltips.add(IFormattableAttribute.toComponent((Attribute) pair.getFirst(), (AttributeModifier) pair.getSecond(), AttributesLib.getTooltipFlag()));
            }
        }
        return true;
    }
}