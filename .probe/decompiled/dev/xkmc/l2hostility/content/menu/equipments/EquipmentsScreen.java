package dev.xkmc.l2hostility.content.menu.equipments;

import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EquipmentsScreen extends BaseContainerScreen<EquipmentsMenu> {

    public EquipmentsScreen(EquipmentsMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((EquipmentsMenu) this.f_97732_).sprite.get().getRenderer(this);
        sr.start(g);
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("hand", 0, 1).m_7993_().isEmpty()) {
            sr.draw(g, "hand", "altas_shield", 1, 19);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 0).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_helmet", 1, 1);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 1).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_chestplate", 1, 19);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 2).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_leggings", 1, 37);
        }
        if (((EquipmentsMenu) this.f_97732_).getAsPredSlot("armor", 0, 3).m_7993_().isEmpty()) {
            sr.draw(g, "armor", "altas_boots", 1, 55);
        }
    }
}