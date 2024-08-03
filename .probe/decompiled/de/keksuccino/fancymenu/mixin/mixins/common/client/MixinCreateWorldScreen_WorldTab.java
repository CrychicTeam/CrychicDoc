package de.keksuccino.fancymenu.mixin.mixins.common.client;

import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueLabeledSwitchCycleButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ CreateWorldScreen.WorldTab.class })
public class MixinCreateWorldScreen_WorldTab extends GridLayoutTab {

    public MixinCreateWorldScreen_WorldTab(Component $$0) {
        super($$0);
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void afterInitTab_FancyMenu(CreateWorldScreen createWorldScreen, CallbackInfo info) {
        this.m_267609_(this::makeWorldTabWidgetsUnique_FancyMenu);
    }

    @Unique
    private void makeWorldTabWidgetsUnique_FancyMenu(Object layoutElement) {
        if (layoutElement instanceof Layout l) {
            l.visitWidgets(this::makeWorldTabWidgetsUnique_FancyMenu);
        }
        if (layoutElement instanceof StringWidget w && w.m_6035_() instanceof MutableComponent c && c.getContents() instanceof TranslatableContents t) {
            if ("selectWorld.mapFeatures".equals(t.getKey())) {
                ((UniqueWidget) w).setWidgetIdentifierFancyMenu("generate_structures_label");
            }
            if ("selectWorld.bonusItems".equals(t.getKey())) {
                ((UniqueWidget) w).setWidgetIdentifierFancyMenu("bonus_chest_label");
            }
            if ("selectWorld.enterSeed".equals(t.getKey())) {
                ((UniqueWidget) w).setWidgetIdentifierFancyMenu("world_seed_label");
            }
        }
        if (layoutElement instanceof EditBox b) {
            ((UniqueWidget) b).setWidgetIdentifierFancyMenu("world_seed_field");
        }
        if (layoutElement instanceof CycleButton<?> c) {
            if (c.getValue() instanceof WorldCreationUiState.WorldTypeEntry) {
                ((UniqueWidget) c).setWidgetIdentifierFancyMenu("world_type_button");
            }
            if (c instanceof UniqueLabeledSwitchCycleButton s && s.getLabeledSwitchComponentLabel_FancyMenu() instanceof MutableComponent label && label.getContents() instanceof TranslatableContents localizedLabel) {
                if ("selectWorld.mapFeatures".equals(localizedLabel.getKey())) {
                    ((UniqueWidget) c).setWidgetIdentifierFancyMenu("generate_structures_button");
                }
                if ("selectWorld.bonusItems".equals(localizedLabel.getKey())) {
                    ((UniqueWidget) c).setWidgetIdentifierFancyMenu("bonus_chest_button");
                }
            }
        }
        if (layoutElement instanceof Button b && b.m_6035_() instanceof MutableComponent c && c.getContents() instanceof TranslatableContents t && "selectWorld.customizeType".equals(t.getKey())) {
            ((UniqueWidget) b).setWidgetIdentifierFancyMenu("customize_world_type_button");
        }
    }
}