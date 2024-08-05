package dev.xkmc.modulargolems.content.menu.filter;

import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import dev.xkmc.modulargolems.content.capability.PickupFilterEditor;
import dev.xkmc.modulargolems.content.menu.ghost.GhostItemScreen;
import dev.xkmc.modulargolems.content.menu.ghost.ItemTarget;
import dev.xkmc.modulargolems.content.menu.registry.ConfigGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ItemConfigScreen extends GhostItemScreen<ItemConfigMenu> {

    public ItemConfigScreen(ItemConfigMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new ConfigGroup(((ItemConfigMenu) this.f_97732_).editor.editor())).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.CONFIG_ITEM);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float ptick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((ItemConfigMenu) this.f_97732_).sprite.getRenderer(this);
        sr.start(poseStack);
        PickupFilterEditor config = ((ItemConfigMenu) this.f_97732_).getConfig();
        if (config.isBlacklist()) {
            sr.draw(poseStack, "filter", "filter_on");
        }
        if (config.isTagMatch()) {
            sr.draw(poseStack, "match", "match_on");
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        if (((ItemConfigMenu) this.f_97732_).sprite.within("filter", mx - (double) this.f_97735_, my - (double) this.f_97736_)) {
            ((ItemConfigMenu) this.f_97732_).getConfig().toggleFilter();
            return true;
        } else if (((ItemConfigMenu) this.f_97732_).sprite.within("match", mx - (double) this.f_97735_, my - (double) this.f_97736_)) {
            ((ItemConfigMenu) this.f_97732_).getConfig().toggleTag();
            return true;
        } else {
            return super.m_6375_(mx, my, btn);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics g, int mx, int my) {
        if (((ItemConfigMenu) this.f_97732_).m_142621_().isEmpty()) {
            PickupFilterEditor c = ((ItemConfigMenu) this.f_97732_).getConfig();
            if (((ItemConfigMenu) this.f_97732_).sprite.within("filter", (double) (mx - this.f_97735_), (double) (my - this.f_97736_))) {
                g.renderTooltip(this.f_96547_, (c.isBlacklist() ? MGLangData.UI_BLACKLIST : MGLangData.UI_WHITELIST).get(), mx, my);
            }
            if (((ItemConfigMenu) this.f_97732_).sprite.within("match", (double) (mx - this.f_97735_), (double) (my - this.f_97736_))) {
                g.renderTooltip(this.f_96547_, (c.isTagMatch() ? MGLangData.UI_MATCH_TAG : MGLangData.UI_MATCH_ITEM).get(), mx, my);
            }
        }
        super.m_280072_(g, mx, my);
    }

    public List<ItemTarget> getTargets() {
        List<ItemTarget> ans = new ArrayList();
        MenuLayoutConfig.Rect rect = ((ItemConfigMenu) this.f_97732_).sprite.getComp("grid");
        for (int y = 0; y < rect.ry; y++) {
            for (int x = 0; x < rect.rx; x++) {
                int id = y * rect.rx + x;
                ans.add(new ItemTarget(rect.x + x * rect.w + this.f_97735_, rect.y + y * rect.h + this.f_97736_, 16, 16, stack -> this.addGhost(id, stack)));
            }
        }
        return ans;
    }
}