package io.github.lightman314.lightmanscurrency.client.gui.widget;

import com.google.common.base.Supplier;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ScrollTextDisplay extends EasyWidget {

    private final Supplier<List<? extends Component>> textSource;

    public boolean invertText = false;

    public int backgroundColor = -16777216;

    public int textColor = 16777215;

    private int columnCount = 1;

    private int scroll = 0;

    public void setColumnCount(int columnCount) {
        this.columnCount = MathUtil.clamp(columnCount, 1, Integer.MAX_VALUE);
    }

    public ScrollTextDisplay(ScreenPosition pos, int width, int height, Supplier<List<? extends Component>> textSource) {
        this(pos.x, pos.y, width, height, textSource);
    }

    public ScrollTextDisplay(int x, int y, int width, int height, Supplier<List<? extends Component>> textSource) {
        super(x, y, width, height);
        this.textSource = textSource;
    }

    public ScrollTextDisplay withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        if (this.f_93624_) {
            gui.fill(this.getArea().atPosition(ScreenPosition.ZERO), this.backgroundColor);
            List<? extends Component> text = (List<? extends Component>) this.textSource.get();
            this.validateScroll(text.size());
            int i = this.getStartingIndex(text.size());
            int columnWidth = this.getColumnWidth();
            int bottom = this.m_252907_() + this.f_93619_;
            int yPos = 2;
            while (yPos < bottom && i >= 0 && i < text.size()) {
                int rowHeight = 0;
                for (int col = 0; col < this.columnCount && i >= 0 && i < text.size(); col++) {
                    int xPos = this.getXPos(col);
                    Component thisText = (Component) text.get(i);
                    int thisHeight = gui.font.wordWrapHeight(thisText.getString(), columnWidth);
                    if (yPos + thisHeight < bottom) {
                        gui.drawWordWrap(thisText, xPos, yPos, columnWidth, this.textColor);
                    }
                    if (thisHeight > rowHeight) {
                        rowHeight = thisHeight;
                    }
                    i = this.invertText ? i - 1 : i + 1;
                }
                yPos += rowHeight;
            }
        }
    }

    private void validateScroll(int listSize) {
        if (this.scroll * this.columnCount >= listSize) {
            this.scroll = MathUtil.clamp(this.scroll, 0, listSize / this.columnCount - 1);
        }
    }

    private int getStartingIndex(int listSize) {
        return this.invertText ? listSize - 1 - this.scroll * this.columnCount : this.scroll * this.columnCount;
    }

    private int getColumnWidth() {
        return (this.f_93618_ - 4) / this.columnCount;
    }

    private int getXPos(int column) {
        int columnSpacing = this.f_93618_ / this.columnCount;
        return 2 + column * columnSpacing;
    }

    private boolean canScrollDown() {
        return this.scroll < ((List) this.textSource.get()).size();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!this.f_93624_) {
            return false;
        } else {
            if (delta < 0.0) {
                if (!this.canScrollDown()) {
                    return false;
                }
                this.scroll++;
            } else if (delta > 0.0) {
                if (this.scroll <= 0) {
                    return false;
                }
                this.scroll--;
            }
            return true;
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
    }
}