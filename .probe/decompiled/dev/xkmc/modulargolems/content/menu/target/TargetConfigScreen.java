package dev.xkmc.modulargolems.content.menu.target;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemScreen;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.init.data.MGLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

public class TargetConfigScreen extends GhostItemScreen<TargetConfigMenu> {

    private boolean hoverHostile;

    private boolean hoverFriendly;

    public TargetConfigScreen(TargetConfigMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new ConfigGroup(((TargetConfigMenu) this.f_97732_).editor.editor())).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.CONFIG_TARGET);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int x, int y) {
        super.m_280003_(g, x, y);
        this.drawLeft(g, MGLangData.UI_TARGET_HOSTILE.get(), 13);
        this.drawLeft(g, MGLangData.UI_TARGET_FRIENDLY.get(), 62);
        this.hoverHostile = this.drawRight(g, MGLangData.UI_TARGET_RESET.get().withStyle(ChatFormatting.UNDERLINE), 13, x, y);
        this.hoverFriendly = this.drawRight(g, MGLangData.UI_TARGET_RESET.get().withStyle(ChatFormatting.UNDERLINE), 62, x, y);
    }

    private void drawLeft(GuiGraphics g, Component comp, int y) {
        int x = this.f_97728_;
        y += this.f_97729_;
        g.drawString(this.f_96547_, comp, x, y, 4210752, false);
    }

    private boolean drawRight(GuiGraphics g, MutableComponent comp, int y, int mx, int my) {
        int w = this.f_96547_.width(comp);
        int x = this.f_97726_ - this.f_97728_ - w;
        y += this.f_97729_;
        int h = 13;
        boolean ans = this.m_6774_(x, y, w, h, (double) mx, (double) my);
        if (ans) {
            comp = comp.withStyle(ChatFormatting.ITALIC);
        }
        g.drawString(this.f_96547_, comp, x, y, 4210752, false);
        return ans;
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float ptick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((TargetConfigMenu) this.f_97732_).sprite.getRenderer(this);
        sr.start(poseStack);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (this.hoverHostile) {
            ((TargetConfigMenu) this.f_97732_).getConfig().resetHostile();
            return true;
        } else if (this.hoverFriendly) {
            ((TargetConfigMenu) this.f_97732_).getConfig().resetFriendly();
            return true;
        } else {
            return super.m_6375_(mx, my, btn);
        }
    }
}