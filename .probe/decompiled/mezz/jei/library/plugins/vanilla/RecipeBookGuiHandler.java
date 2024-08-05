package mezz.jei.library.plugins.vanilla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class RecipeBookGuiHandler<C extends AbstractContainerMenu, T extends AbstractContainerScreen<C> & RecipeUpdateListener> implements IGuiContainerHandler<T> {

    @Override
    public List<Rect2i> getGuiExtraAreas(T containerScreen) {
        RecipeBookComponent guiRecipeBook = containerScreen.getRecipeBookComponent();
        if (guiRecipeBook.isVisible()) {
            IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
            List<Rect2i> tabAreas = new ArrayList();
            for (RecipeBookTabButton tab : screenHelper.getTabButtons(guiRecipeBook)) {
                if (tab.f_93624_) {
                    tabAreas.add(new Rect2i(tab.m_252754_(), tab.m_252907_(), tab.m_5711_(), tab.m_93694_()));
                }
            }
            return tabAreas;
        } else {
            return Collections.emptyList();
        }
    }
}