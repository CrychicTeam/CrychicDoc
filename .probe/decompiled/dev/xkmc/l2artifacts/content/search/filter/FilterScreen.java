package dev.xkmc.l2artifacts.content.search.filter;

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

public class FilterScreen extends StackedScreen {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "filter");

    @Nullable
    private FilterScreen.ButtonHover btnHover;

    private FilterScreen.ButtonHover prevBtnHover;

    protected FilterScreen(ArtifactChestToken token) {
        super(LangData.TAB_FILTER.get(), MANAGER, FilterTabManager.FILTER, token);
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
        boolean pa = p && this.prevBtnHover.a();
        boolean pb = p && !this.prevBtnHover.a();
        TextButtonHandle btns = handle.drawTextWithButtons(((ArtifactFilter) this.token.filters.get(i)).getDescription(), false);
        CellEntry ca = btns.addButton(pa ? "button_1" : "button_1p");
        CellEntry cb = btns.addButton(pb ? "button_2" : "button_2p");
        if (this.isHovering(ca.x(), ca.y(), ca.w(), ca.h(), (double) mx, (double) my)) {
            this.btnHover = new FilterScreen.ButtonHover(i, true, ca);
        }
        if (this.isHovering(cb.x(), cb.y(), cb.w(), cb.h(), (double) mx, (double) my)) {
            this.btnHover = new FilterScreen.ButtonHover(i, false, cb);
        }
    }

    @Override
    protected boolean isAvailable(int i, int j) {
        return ((ArtifactFilter) this.token.filters.get(i)).getAvailability(j);
    }

    @Override
    protected void clickHover(int i, int j) {
        ((ArtifactFilter) this.token.filters.get(i)).toggle(j);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        this.pressed = false;
        if (this.btnHover != null) {
            ArtifactFilter<?> filter = (ArtifactFilter<?>) this.token.filters.get(this.btnHover.i());
            for (int i = filter.allEntries.size() - 1; i >= 0; i--) {
                if (filter.getSelected(i) != this.btnHover.a()) {
                    filter.toggle(i);
                }
            }
            return true;
        } else {
            return super.mouseReleased(pMouseX, pMouseY, pButton);
        }
    }

    private static record ButtonHover(int i, boolean a, CellEntry cell) {
    }
}