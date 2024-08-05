package dev.xkmc.l2backpack.content.quickswap.merged;

import dev.xkmc.l2backpack.content.common.BaseOpenableScreen;
import dev.xkmc.l2library.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MultiSwitchScreen<T extends BaseContainerMenu<T>> extends BaseOpenableScreen<T> {

    public MultiSwitchScreen(T cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics stack, float pt, int mx, int my) {
        MenuLayoutConfig sm = ((BaseContainerMenu) this.f_97732_).sprite.get();
        MenuLayoutConfig.ScreenRenderer sr = sm.getRenderer(this);
        sr.start(stack);
        int offset = ((BaseContainerMenu) this.m_6262_()).f_38839_.size() / 9 - 3;
        for (int i = 0; i < 9; i++) {
            if (((BaseContainerMenu) this.m_6262_()).m_38853_(offset * 9 + i).getItem().isEmpty()) {
                sr.draw(stack, "arrow", "altas_arrow", i * 18 - 1, -1);
            }
        }
        offset++;
        for (int ix = 0; ix < 9; ix++) {
            if (((BaseContainerMenu) this.m_6262_()).m_38853_(offset * 9 + ix).getItem().isEmpty()) {
                sr.draw(stack, "tool", "altas_tool", ix * 18 - 1, -1);
            }
        }
        offset++;
        for (int ixx = 0; ixx < 9; ixx++) {
            if (((BaseContainerMenu) this.m_6262_()).m_38853_(offset * 9 + ixx).getItem().isEmpty()) {
                sr.draw(stack, "armor", "altas_armor", ixx * 18 - 1, -1);
            }
        }
    }
}