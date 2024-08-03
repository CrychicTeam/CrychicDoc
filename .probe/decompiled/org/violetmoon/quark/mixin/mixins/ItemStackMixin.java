package org.violetmoon.quark.mixin.mixins;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.client.hax.PseudoAccessorItemStack;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.client.resources.AttributeSlot;
import org.violetmoon.quark.content.tools.module.AncientTomesModule;
import org.violetmoon.quark.content.tweaks.module.GoldToolsHaveFortuneModule;

@Mixin({ ItemStack.class })
public class ItemStackMixin implements PseudoAccessorItemStack {

    @Unique
    private Map<AttributeSlot, Multimap<Attribute, AttributeModifier>> capturedAttributes = new HashMap();

    @ModifyReturnValue(method = { "getRarity" }, at = { @At("RETURN") })
    private Rarity getRarity(Rarity prev) {
        return AncientTomesModule.shiftRarity((ItemStack) this, prev);
    }

    @Inject(method = { "getTooltipLines" }, at = { @At("HEAD") })
    private void hasTagIfBaked(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir, @Share("removedEnchantments") LocalBooleanRef ref) {
        ItemStack self = (ItemStack) this;
        if (!self.hasTag() && GoldToolsHaveFortuneModule.shouldShowEnchantments(self)) {
            ref.set(true);
            self.setTag(new CompoundTag());
        }
    }

    @ModifyExpressionValue(method = { "getTooltipLines" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hasTag()Z", ordinal = 0) })
    private boolean hasTagIfBaked(boolean hasTag, @Share("removedEnchantments") LocalBooleanRef ref) {
        return hasTag || ref.get();
    }

    @Inject(method = { "getTooltipLines" }, at = { @At("RETURN") })
    private void removeTagIfBaked(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir, @Share("removedEnchantments") LocalBooleanRef ref) {
        ItemStack self = (ItemStack) this;
        if (ref.get()) {
            self.setTag(null);
        }
    }

    @ModifyArg(method = { "getTooltipLines" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private ListTag hideSmallerEnchantments(ListTag tag) {
        return GoldToolsHaveFortuneModule.hideSmallerEnchantments((ItemStack) this, tag);
    }

    @ModifyArg(method = { "getTooltipLines" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;appendEnchantmentNames(Ljava/util/List;Lnet/minecraft/nbt/ListTag;)V"))
    private List<Component> appendEnchantmentNames(List<Component> components) {
        GoldToolsHaveFortuneModule.fakeEnchantmentTooltip((ItemStack) this, components);
        return components;
    }

    @Override
    public Map<AttributeSlot, Multimap<Attribute, AttributeModifier>> quark$getCapturedAttributes() {
        return this.capturedAttributes;
    }

    @Override
    public void quark$capturePotionAttributes(List<Pair<Attribute, AttributeModifier>> attributes) {
        Multimap<Attribute, AttributeModifier> attributeContainer = LinkedHashMultimap.create();
        for (Pair<Attribute, AttributeModifier> pair : attributes) {
            attributeContainer.put((Attribute) pair.getFirst(), (AttributeModifier) pair.getSecond());
        }
        this.capturedAttributes.put(AttributeSlot.POTION, attributeContainer);
    }

    @Inject(method = { "getTooltipLines" }, at = { @At("HEAD") })
    private void clearCapturedTooltip(Player player, TooltipFlag flag, CallbackInfoReturnable<List<Component>> cir) {
        this.capturedAttributes = new HashMap();
    }

    @ModifyReceiver(method = { "getTooltipLines" }, at = { @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;isEmpty()Z", remap = false) })
    private Multimap<Attribute, AttributeModifier> overrideAttributeTooltips(Multimap<Attribute, AttributeModifier> attributes, @Local EquipmentSlot slot) {
        if (ImprovedTooltipsModule.shouldHideAttributes()) {
            this.capturedAttributes.put(AttributeSlot.fromCanonicalSlot(slot), LinkedHashMultimap.create(attributes));
            return ImmutableMultimap.of();
        } else {
            return attributes;
        }
    }
}