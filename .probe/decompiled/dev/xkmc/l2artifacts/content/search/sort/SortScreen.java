package dev.xkmc.l2artifacts.content.search.sort;

import dev.xkmc.l2artifacts.content.search.common.StackedScreen;
import dev.xkmc.l2artifacts.content.search.tabs.FilterTabManager;
import dev.xkmc.l2artifacts.content.search.token.ArtifactChestToken;
import dev.xkmc.l2artifacts.content.search.token.ArtifactFilter;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import dev.xkmc.l2library.base.menu.stacked.CellEntry;
import dev.xkmc.l2library.base.menu.stacked.StackedRenderHandle;
import dev.xkmc.l2library.base.menu.stacked.TextButtonHandle;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SortScreen extends StackedScreen {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "sort");

    @Nullable
    private SortScreen.ButtonHover btnHover;

    private SortScreen.ButtonHover prevBtnHover;

    protected SortScreen(ArtifactChestToken token) {
        super(LangData.TAB_SORT.get(), MANAGER, FilterTabManager.SORT, token);
    }

    @Override
    protected void renderInit() {
        this.prevBtnHover = this.btnHover;
        this.btnHover = null;
    }

    @Override
    protected void renderPost(GuiGraphics g) {
        if (this.btnHover != null) {
            CellEntry cell = this.btnHover.cell();
            renderHighlight(g, cell.x(), cell.y(), cell.w(), cell.h(), -2130706433);
        }
    }

    @Override
    protected void renderText(StackedRenderHandle handle, int i, int mx, int my) {
        boolean p = this.pressed && this.prevBtnHover != null && this.prevBtnHover.i() == i;
        TextButtonHandle btns = handle.drawTextWithButtons(((ArtifactFilter) this.token.filters.get(i)).getDescription(), false);
        CellEntry ca = btns.addButton(p ? "sort_1" : "sort_1p");
        btns.drawText(ca, Component.literal(((ArtifactFilter) this.token.filters.get(i)).priority() + ""), false);
        if (this.isHovering(ca.x(), ca.y(), ca.w(), ca.h(), (double) mx, (double) my)) {
            this.btnHover = new SortScreen.ButtonHover(i, ca);
        }
    }

    @Override
    protected void renderItem(GuiGraphics g, StackedScreen.FilterHover hover) {
        super.renderItem(g, hover);
        String s = ((ArtifactFilter) this.token.filters.get(hover.i())).getPriority(hover.j()) + "";
        g.pose().pushPose();
        g.pose().translate(0.0, 0.0, 300.0);
        int tx = hover.x() + 19 - 2 - this.f_96547_.width(s);
        int ty = hover.y() + 6 + 3;
        g.drawString(this.f_96547_, s, tx, ty, 16777215, true);
        g.pose().popPose();
    }

    @Override
    protected boolean isAvailable(int i, int j) {
        ArtifactFilter<?> filter = (ArtifactFilter<?>) this.token.filters.get(i);
        return filter.getSelected(j) || filter.getAvailability(j);
    }

    @Override
    protected void clickHover(int i, int j) {
        if (this.isAvailable(i, j)) {
            ((ArtifactFilter) this.token.filters.get(i)).prioritize(j);
        }
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.pressed = false;
        if (this.btnHover != null) {
            this.token.prioritize(this.btnHover.i());
            return true;
        } else {
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }
    }

    private static record ButtonHover(int i, CellEntry cell) {
    }
}