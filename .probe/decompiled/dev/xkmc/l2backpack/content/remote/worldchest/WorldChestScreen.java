package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.content.common.BaseOpenableScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class WorldChestScreen extends BaseOpenableScreen<WorldChestContainer> {

    public WorldChestScreen(WorldChestContainer cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics stack, float pt, int mx, int my) {
        MenuLayoutConfig sm = ((WorldChestContainer) this.f_97732_).sprite.get();
        MenuLayoutConfig.ScreenRenderer sr = sm.getRenderer(this);
        sr.start(stack);
    }
}