package journeymap.client.ui.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;

public class ButtonList extends ArrayList<Button> {

    static EnumSet<ButtonList.Layout> VerticalLayouts = EnumSet.of(ButtonList.Layout.Vertical, ButtonList.Layout.CenteredVertical);

    static EnumSet<ButtonList.Layout> HorizontalLayouts = EnumSet.of(ButtonList.Layout.Horizontal, ButtonList.Layout.CenteredHorizontal, ButtonList.Layout.DistributedHorizontal, ButtonList.Layout.FilledHorizontal);

    private ButtonList.Layout layout = ButtonList.Layout.Horizontal;

    private ButtonList.Direction direction = ButtonList.Direction.LeftToRight;

    private String label;

    private int hgap = 0;

    public ButtonList() {
    }

    public ButtonList(String label) {
        this.label = label;
    }

    public ButtonList(List<net.minecraft.client.gui.components.Button> buttons) {
        for (net.minecraft.client.gui.components.Button button : buttons) {
            if (button instanceof Button) {
                this.add((Button) button);
            }
        }
    }

    public ButtonList(Button... buttons) {
        super(Arrays.asList(buttons));
    }

    public int getWidth() {
        return this.getWidth(-1, this.hgap);
    }

    public int getWidth(int hgap) {
        return this.getWidth(-1, hgap);
    }

    private int getWidth(int buttonWidth, int hgap) {
        if (this.isEmpty()) {
            return 0;
        } else {
            int total = 0;
            if (HorizontalLayouts.contains(this.layout)) {
                int visible = 0;
                for (Button button : this) {
                    if (button.isVisible()) {
                        if (buttonWidth > 0) {
                            total += buttonWidth;
                        } else {
                            total += button.m_5711_();
                        }
                        visible++;
                    }
                }
                if (visible > 1) {
                    total += hgap * (visible - 1);
                }
            } else {
                if (buttonWidth > 0) {
                    total = buttonWidth;
                }
                for (Button buttonx : this) {
                    if (buttonx.isVisible()) {
                        total = Math.max(total, buttonx.m_5711_());
                    }
                }
            }
            return total;
        }
    }

    public int getHeight() {
        return this.getHeight(0);
    }

    public int getHeight(int vgap) {
        if (this.isEmpty()) {
            return 0;
        } else {
            int total = 0;
            if (VerticalLayouts.contains(this.layout)) {
                int visible = 0;
                for (Button button : this) {
                    if (button.isVisible()) {
                        total += button.m_93694_();
                        visible++;
                    }
                }
                if (visible > 1) {
                    total += vgap * (visible - 1);
                }
            } else {
                for (Button buttonx : this) {
                    if (buttonx.isVisible()) {
                        total = Math.max(total, buttonx.m_93694_() + vgap);
                    }
                }
            }
            return total;
        }
    }

    public int getLeftX() {
        int left = Integer.MAX_VALUE;
        for (Button button : this) {
            if (button.isVisible()) {
                left = Math.min(left, button.m_252754_());
            }
        }
        if (left == Integer.MAX_VALUE) {
            left = 0;
        }
        return left;
    }

    public int getTopY() {
        int top = Integer.MAX_VALUE;
        for (Button button : this) {
            if (button.isVisible()) {
                top = Math.min(top, button.m_252907_());
            }
        }
        if (top == Integer.MAX_VALUE) {
            top = 0;
        }
        return top;
    }

    public int getBottomY() {
        int bottom = Integer.MIN_VALUE;
        for (Button button : this) {
            if (button.isVisible()) {
                bottom = Math.max(bottom, button.m_252907_() + button.m_93694_());
            }
        }
        if (bottom == Integer.MIN_VALUE) {
            bottom = 0;
        }
        return bottom;
    }

    public int getRightX() {
        int right = 0;
        for (Button button : this) {
            if (button.isVisible()) {
                right = Math.max(right, button.m_252754_() + button.m_5711_());
            }
        }
        return right;
    }

    public void setLayout(ButtonList.Layout layout, ButtonList.Direction direction) {
        this.layout = layout;
        this.direction = direction;
    }

    public ButtonList layoutHorizontal(int startX, int y, boolean leftToRight, int hgap) {
        return this.layoutHorizontal(startX, y, leftToRight, hgap, false);
    }

    public ButtonList layoutHorizontal(int startX, int y, boolean leftToRight, int hgap, boolean alignCenter) {
        this.layout = ButtonList.Layout.Horizontal;
        this.direction = leftToRight ? ButtonList.Direction.LeftToRight : ButtonList.Direction.RightToLeft;
        this.hgap = hgap;
        Button last = null;
        for (Button button : this) {
            if (button.f_93624_) {
                if (last == null) {
                    if (leftToRight) {
                        button.rightOf(startX).setY(y);
                    } else {
                        button.leftOf(startX).setY(y);
                    }
                } else if (leftToRight) {
                    button.rightOf(last, hgap).setY(y);
                } else {
                    button.leftOf(last, hgap).setY(y);
                }
                last = button;
            }
        }
        if (alignCenter && !this.isEmpty()) {
            int maxButtonHeight = ((Button) this.stream().max(Comparator.comparing(AbstractWidget::m_93694_)).get()).m_93694_();
            this.forEach(buttonx -> {
                if (buttonx.m_93694_() < maxButtonHeight) {
                    int buttonHeight = buttonx.m_93694_();
                    buttonx.setY((maxButtonHeight - buttonHeight) / 2 + buttonx.m_252907_());
                }
            });
        }
        this.layout = ButtonList.Layout.Horizontal;
        return this;
    }

    public ButtonList layoutVertical(int x, int startY, boolean leftToRight, int vgap) {
        this.layout = ButtonList.Layout.Vertical;
        this.direction = leftToRight ? ButtonList.Direction.LeftToRight : ButtonList.Direction.RightToLeft;
        Button last = null;
        for (Button button : this) {
            if (last == null) {
                if (leftToRight) {
                    button.rightOf(x).setY(startY);
                } else {
                    button.leftOf(x).setY(startY);
                }
            } else if (leftToRight) {
                button.rightOf(x).below(last, vgap);
            } else {
                button.leftOf(x).below(last, vgap);
            }
            last = button;
        }
        this.layout = ButtonList.Layout.Vertical;
        return this;
    }

    public ButtonList layoutCenteredVertical(int x, int centerY, boolean leftToRight, int vgap) {
        this.layout = ButtonList.Layout.CenteredVertical;
        int height = this.getHeight(vgap);
        this.layoutVertical(x, centerY - height / 2, leftToRight, vgap);
        return this;
    }

    public ButtonList layoutCenteredHorizontal(int centerX, int y, boolean leftToRight, int hgap) {
        return this.layoutCenteredHorizontal(centerX, y, leftToRight, hgap, false);
    }

    public ButtonList layoutCenteredHorizontal(int centerX, int y, boolean leftToRight, int hgap, boolean alignCenter) {
        this.layout = ButtonList.Layout.CenteredHorizontal;
        int width = this.getWidth(hgap);
        this.layoutHorizontal(centerX - width / 2, y, leftToRight, hgap, alignCenter);
        return this;
    }

    public ButtonList layoutDistributedHorizontal(int leftX, int y, int rightX, boolean leftToRight) {
        this.layout = ButtonList.Layout.DistributedHorizontal;
        if (this.size() == 0) {
            return this;
        } else {
            int width = this.getWidth(0);
            int filler = rightX - leftX - width;
            int gaps = this.size() - 1;
            int hgap = gaps == 0 ? 0 : (filler >= gaps ? filler / gaps : 0);
            if (leftToRight) {
                this.layoutHorizontal(leftX, y, true, hgap);
            } else {
                this.layoutHorizontal(rightX, y, false, hgap);
            }
            this.layout = ButtonList.Layout.DistributedHorizontal;
            return this;
        }
    }

    public ButtonList layoutFilledHorizontal(Font fr, int leftX, int y, int rightX, int hgap, boolean leftToRight) {
        this.hgap = hgap;
        this.layout = ButtonList.Layout.FilledHorizontal;
        if (this.size() == 0) {
            return this;
        } else {
            this.equalizeWidths(fr);
            int width = this.getWidth(hgap);
            int remaining = rightX - leftX - width;
            if (remaining > this.size()) {
                int gaps = hgap * this.size();
                int area = rightX - leftX - gaps;
                int wider = area / this.size();
                this.setWidths(wider);
                this.layoutDistributedHorizontal(leftX, y, rightX, leftToRight);
            } else {
                this.layoutCenteredHorizontal((rightX - leftX) / 2, y, leftToRight, hgap);
            }
            this.layout = ButtonList.Layout.FilledHorizontal;
            return this;
        }
    }

    public void setFitWidths(Font fr) {
        this.fitWidths(fr);
    }

    public boolean isHorizontal() {
        return this.layout != ButtonList.Layout.Vertical && this.layout != ButtonList.Layout.CenteredVertical;
    }

    public ButtonList setEnabled(boolean enabled) {
        for (Button button : this) {
            button.setEnabled(enabled);
        }
        return this;
    }

    public ButtonList setVisible(boolean visible) {
        for (Button button : this) {
            button.setVisible(visible);
        }
        return this;
    }

    public ButtonList setOptions(boolean enabled, boolean drawBackground, boolean drawFrame) {
        for (Button button : this) {
            button.setEnabled(enabled);
            button.setDrawFrame(drawFrame);
            button.setDrawBackground(drawBackground);
        }
        return this;
    }

    public ButtonList setDefaultStyle(boolean defaultStyle) {
        for (Button button : this) {
            button.setDefaultStyle(defaultStyle);
        }
        return this;
    }

    public ButtonList draw(GuiGraphics graphics, int mouseX, int mouseY) {
        for (Button button : this) {
            button.render(graphics, mouseX, mouseY, 0.0F);
        }
        return this;
    }

    public void setHeights(int height) {
        for (Button button : this) {
            button.setHeight(height);
        }
    }

    public void setWidths(int width) {
        for (Button button : this) {
            button.setWidth(width);
        }
    }

    public void fitWidths(Font fr) {
        for (Button button : this) {
            button.fitWidth(fr);
        }
    }

    public void setDrawButtons(boolean draw) {
        for (Button button : this) {
            button.setDrawButton(draw);
        }
    }

    public void equalizeWidths(Font fr) {
        int maxWidth = 0;
        for (Button button : this) {
            if (button.isVisible()) {
                button.fitWidth(fr);
                maxWidth = Math.max(maxWidth, button.m_5711_());
            }
        }
        this.setWidths(maxWidth);
    }

    public void equalizeWidths(Font fr, int hgap, int maxTotalWidth) {
        this.hgap = hgap;
        int maxWidth = 0;
        for (Button button : this) {
            button.fitWidth(fr);
            maxWidth = Math.max(maxWidth, button.m_5711_());
        }
        int totalWidth = this.getWidth(maxWidth, hgap);
        if (totalWidth <= maxTotalWidth) {
            this.setWidths(maxWidth);
        } else {
            totalWidth = this.getWidth(hgap);
        }
        if (totalWidth < maxTotalWidth) {
            int pad = (maxTotalWidth - totalWidth) / this.size();
            if (pad > 0) {
                for (Button button : this) {
                    button.setWidth(button.m_5711_() + pad);
                }
            }
        }
    }

    public int getVisibleButtonCount() {
        int count = 0;
        for (Button button : this) {
            if (button.f_93624_) {
                count++;
            }
        }
        return count;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ButtonList reverse() {
        Collections.reverse(this);
        return this;
    }

    public static enum Direction {

        LeftToRight, RightToLeft
    }

    public static enum Layout {

        Horizontal,
        Vertical,
        CenteredHorizontal,
        CenteredVertical,
        DistributedHorizontal,
        FilledHorizontal
    }
}