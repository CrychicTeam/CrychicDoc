package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ CreateWorldScreen.MoreTab.class })
public class MixinCreateWorldScreen_MoreTab {

    @WrapOperation(method = { "<init>" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;") })
    private <T extends LayoutElement> T wrapAddChild2_FancyMenu(GridLayout.RowHelper instance, T layoutElement, Operation<T> original) {
        this.makeMoreTabWidgetsUnique_FancyMenu(layoutElement);
        return (T) original.call(new Object[] { instance, layoutElement });
    }

    @Unique
    private void makeMoreTabWidgetsUnique_FancyMenu(Object layoutElement) {
        if (layoutElement instanceof Button b && b.m_6035_() instanceof MutableComponent c && c.getContents() instanceof TranslatableContents t) {
            if ("selectWorld.gameRules".equals(t.getKey())) {
                ((UniqueWidget) b).setWidgetIdentifierFancyMenu("game_rules_button");
            }
            if ("selectWorld.experiments".equals(t.getKey())) {
                ((UniqueWidget) b).setWidgetIdentifierFancyMenu("experiments_button");
            }
            if ("selectWorld.dataPacks".equals(t.getKey())) {
                ((UniqueWidget) b).setWidgetIdentifierFancyMenu("datapacks_button");
            }
        }
    }
}