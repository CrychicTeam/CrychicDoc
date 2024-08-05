package dev.xkmc.modulargolems.content.menu.path;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemScreen;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PathConfigScreen extends GhostItemScreen<PathConfigMenu> {

    public PathConfigScreen(PathConfigMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new ConfigGroup(((PathConfigMenu) this.f_97732_).editor.editor())).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.CONFIG_PATH);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float ptick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((PathConfigMenu) this.f_97732_).sprite.getRenderer(this);
        sr.start(poseStack);
    }
}