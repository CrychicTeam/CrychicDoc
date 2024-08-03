package top.theillusivec4.curios.common.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;

public class CuriosContainerHandler implements IGuiContainerHandler<CuriosScreen> {

    @Nonnull
    public List<Rect2i> getGuiExtraAreas(CuriosScreen containerScreen) {
        LocalPlayer player = containerScreen.getMinecraft().player;
        return player != null ? (List) CuriosApi.getCuriosInventory(containerScreen.getMinecraft().player).map(handler -> {
            List<Rect2i> areas = new ArrayList();
            int slotCount = handler.getVisibleSlots();
            if (slotCount <= 0) {
                return areas;
            } else {
                int width = slotCount > 8 ? 42 : 26;
                if (((CuriosContainer) containerScreen.m_6262_()).hasCosmeticColumn()) {
                    width += 18;
                }
                int height = 7 + slotCount * 18;
                int left = containerScreen.getGuiLeft() - width;
                int top = containerScreen.getGuiTop() + 4;
                areas.add(new Rect2i(left, top, width, height));
                RecipeBookComponent guiRecipeBook = containerScreen.getRecipeBookComponent();
                if (guiRecipeBook.isVisible()) {
                    int i = (containerScreen.f_96543_ - 147) / 2 - (containerScreen.widthTooNarrow ? 0 : 86);
                    int j = (containerScreen.f_96544_ - 166) / 2;
                    areas.add(new Rect2i(i, j, 147, 166));
                    for (RecipeBookTabButton tab : guiRecipeBook.tabButtons) {
                        if (tab.f_93623_) {
                            areas.add(new Rect2i(tab.m_252754_(), tab.m_252754_(), tab.m_5711_(), tab.m_93694_()));
                        }
                    }
                    return areas;
                } else {
                    return areas;
                }
            }
        }).orElse(Collections.emptyList()) : Collections.emptyList();
    }
}