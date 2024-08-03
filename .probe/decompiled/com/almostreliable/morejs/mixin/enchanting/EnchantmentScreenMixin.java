package com.almostreliable.morejs.mixin.enchanting;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.enchantment.EnchantmentMenuExtension;
import com.almostreliable.morejs.features.enchantment.EnchantmentTableTooltipEventJS;
import dev.latvian.mods.kubejs.bindings.TextWrapper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ EnchantmentScreen.class })
public abstract class EnchantmentScreenMixin extends AbstractContainerScreen<EnchantmentMenu> {

    public EnchantmentScreenMixin(EnchantmentMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V") }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void render$InvokeEnchantmentTooltipMenu(GuiGraphics graphics, int mx, int my, float pTick, CallbackInfo ci, boolean creative, int goldCount, int slot, int cost, Enchantment enchantment, int lClue, int level, List<Component> currentComponents) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {
            if (this.f_97732_ instanceof EnchantmentMenuExtension extension) {
                List<Object> components = new ArrayList(currentComponents);
                ItemStack var10002 = extension.getMoreJsEnchantSlots().getItem(0);
                ItemStack var10003 = extension.getMoreJsEnchantSlots().getItem(1);
                EnchantmentMenu var10006 = (EnchantmentMenu) this.f_97732_;
                EnchantmentTableTooltipEventJS e = new EnchantmentTableTooltipEventJS(var10002, var10003, Minecraft.getInstance().level, Minecraft.getInstance().player, var10006, slot, components);
                Events.ENCHANTMENT_TABLE_TOOLTIP.post(e);
                currentComponents.clear();
                components.forEach(o -> {
                    MutableComponent of = TextWrapper.of(o);
                    currentComponents.add(of);
                });
            }
        }
    }
}