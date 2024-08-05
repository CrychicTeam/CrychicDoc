package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.tabs.IFilterScreen;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ShapeMenuScreen extends BaseContainerScreen<ShapeMenu> implements IFilterScreen {

    public ShapeMenuScreen(ShapeMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, LangData.TAB_SHAPE.get());
    }

    @Override
    protected final void init() {
        super.m_7856_();
        new FilterTabManager(this, ((ShapeMenu) this.f_97732_).token).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, FilterTabManager.SHAPE);
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig.ScreenRenderer sr = ((ShapeMenu) this.f_97732_).sprite.get().getRenderer(this);
        sr.start(g);
        this.drawDisable(sr, g, ShapeSlots.BOOST_MAIN, 0, "altas_boost_main");
        for (int i = 0; i < 4; i++) {
            this.drawDisable(sr, g, ShapeSlots.ARTIFACT_SUB, i, null);
        }
        for (int i = 0; i < 4; i++) {
            this.drawDisable(sr, g, ShapeSlots.STAT_SUB, i, "altas_stat_container");
        }
        for (int i = 0; i < 4; i++) {
            this.drawDisable(sr, g, ShapeSlots.BOOST_SUB, i, "altas_boost_sub");
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawString(this.f_96547_, this.f_96539_.copy().withStyle(ChatFormatting.GRAY), this.f_97728_, this.f_97729_, 4210752, false);
        guiGraphics0.drawString(this.f_96547_, this.f_169604_.copy().withStyle(ChatFormatting.GRAY), this.f_97730_, this.f_97731_, 4210752, false);
    }

    public void drawDisable(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, ShapeSlots slot, int i, @Nullable String altas) {
        if (slot.get((ShapeMenu) this.f_97732_, i).isInputLocked()) {
            sr.draw(g, slot.slot(), "toggle_slot_2", -1 + i * 18, -1);
        } else {
            sr.draw(g, slot.slot(), "toggle_slot_0", -1 + i * 18, -1);
        }
        if (altas != null && slot.get((ShapeMenu) this.f_97732_, i).m_7993_().isEmpty()) {
            sr.draw(g, slot.slot(), altas, i * 18, 0);
        }
    }
}