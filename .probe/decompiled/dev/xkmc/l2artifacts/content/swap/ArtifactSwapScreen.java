package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2library.base.menu.base.BaseContainerScreen;
import dev.xkmc.l2library.base.menu.base.MenuLayoutConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ArtifactSwapScreen extends BaseContainerScreen<ArtifactSwapMenu> {

    public ArtifactSwapScreen(ArtifactSwapMenu cont, Inventory plInv, Component title) {
        super(cont, plInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics g, float pTick, int mx, int my) {
        MenuLayoutConfig sm = ((ArtifactSwapMenu) this.f_97732_).sprite.get();
        MenuLayoutConfig.ScreenRenderer sr = sm.getRenderer(this);
        sr.start(g);
        for (int i = 0; i < 45; i++) {
            this.drawDisable(sr, g, i);
        }
    }

    public void drawDisable(MenuLayoutConfig.ScreenRenderer sr, GuiGraphics g, int i) {
        boolean lock = ((ArtifactSwapMenu) this.f_97732_).disable.get(i);
        ArtifactSlot slot = ((ArtifactSwapMenu) this.f_97732_).data.contents[i].slot;
        ItemStack stack = ((ArtifactSwapMenu) this.f_97732_).container.getItem(i);
        if (lock) {
            sr.draw(g, "grid", "altas_disabled", i % 9 * 18, i / 9 * 18);
        } else if (stack.isEmpty()) {
            sr.draw(g, "grid", "altas_" + slot.getRegistryName().getPath(), i % 9 * 18, i / 9 * 18);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics0, int int1, int int2) {
        guiGraphics0.drawString(this.f_96547_, this.f_96539_.copy().withStyle(ChatFormatting.GRAY), this.f_97728_, this.f_97729_, 4210752, false);
        guiGraphics0.drawString(this.f_96547_, this.f_169604_.copy().withStyle(ChatFormatting.GRAY), this.f_97730_, this.f_97731_, 4210752, false);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        MenuLayoutConfig.Rect r = ((ArtifactSwapMenu) this.f_97732_).sprite.get().getComp("grid");
        int x = r.x + this.getGuiLeft();
        int y = r.y + this.getGuiTop();
        if (mx >= (double) x && my >= (double) y && mx < (double) (x + r.w * r.rx) && my < (double) (y + r.h * r.ry)) {
            Slot slot = this.getSlotUnderMouse();
            if (slot != null && slot.getItem().isEmpty() && ((ArtifactSwapMenu) this.f_97732_).m_142621_().isEmpty()) {
                this.click(slot.getContainerSlot());
                return true;
            }
        }
        return super.m_6375_(mx, my, btn);
    }
}