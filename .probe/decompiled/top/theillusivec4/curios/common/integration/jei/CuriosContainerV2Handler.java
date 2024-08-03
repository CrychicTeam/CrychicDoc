package top.theillusivec4.curios.common.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;

public class CuriosContainerV2Handler implements IGuiContainerHandler<CuriosScreenV2> {

    @Nonnull
    public List<Rect2i> getGuiExtraAreas(CuriosScreenV2 containerScreen) {
        LocalPlayer player = containerScreen.getMinecraft().player;
        if (player != null) {
            List<Rect2i> areas = new ArrayList();
            int left = containerScreen.getGuiLeft() - containerScreen.panelWidth;
            int top = containerScreen.getGuiTop();
            List<Integer> list = ((CuriosContainerV2) containerScreen.m_6262_()).grid;
            int height = 0;
            if (!list.isEmpty()) {
                height = (Integer) list.get(0) * 18 + 14;
                if (((CuriosContainerV2) containerScreen.m_6262_()).hasCosmetics) {
                    areas.add(new Rect2i(containerScreen.getGuiLeft() - 30, top - 34, 28, 34));
                }
            }
            areas.add(new Rect2i(left, top, containerScreen.panelWidth, height));
            return areas;
        } else {
            return Collections.emptyList();
        }
    }
}