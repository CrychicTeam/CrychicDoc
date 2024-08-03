package net.blay09.mods.craftingtweaks;

import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.balm.mixin.ImageButtonAccessor;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksConfig;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksConfigData;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.jetbrains.annotations.Nullable;

public class CraftingGuideButtonFixer {

    public static Button fixMistakes(AbstractContainerScreen<?> screen) {
        Button button = findCraftButton(((ScreenAccessor) screen).balm_getChildren());
        if (button != null) {
            CraftingTweaksConfigData config = CraftingTweaksConfig.getActive();
            if (config.client.hideVanillaCraftingGuide) {
                button.f_93624_ = false;
            } else if (!(screen instanceof InventoryScreen)) {
                AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor) screen;
                button.m_252865_(accessor.getLeftPos() + accessor.getImageWidth() - 25);
                if (screen.getClass().getSimpleName().equals("GuiCraftingStation")) {
                    button.m_253211_(accessor.getTopPos() + 37);
                } else {
                    button.m_253211_(accessor.getTopPos() + 5);
                }
                if (Balm.isModLoaded("inventorytweaks")) {
                    button.m_252865_(button.m_252754_() - 15);
                }
            }
        }
        return button;
    }

    @Nullable
    private static Button findCraftButton(List<? extends GuiEventListener> buttonList) {
        return (Button) buttonList.stream().filter(p -> p instanceof ImageButton && ((ImageButtonAccessor) p).getResourceLocation() != null && ((ImageButtonAccessor) p).getResourceLocation().getPath().equals("textures/gui/recipe_button.png")).findFirst().orElse(null);
    }
}