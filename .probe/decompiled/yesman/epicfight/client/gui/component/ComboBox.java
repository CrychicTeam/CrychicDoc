package yesman.epicfight.client.gui.component;

import java.util.List;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ComboBox<T> extends AbstractWidget implements ResizableComponent {

    private final ComboBox<T>.ComboItemList comboItemList;

    private final Font font;

    private final int rows;

    private boolean listOpened;

    private final int x1;

    private final int x2;

    private final int y1;

    private final int y2;

    private final ResizableComponent.HorizontalSizing horizontalSizingOption;

    private final ResizableComponent.VerticalSizing verticalSizingOption;

    public ComboBox(Screen parent, Font font, int x1, int x2, int y1, int y2, ResizableComponent.HorizontalSizing horizontal, ResizableComponent.VerticalSizing vertical, int maxRows, Component title, List<T> items, Function<T, String> displayStringMapper) {
        super(x1, x2, y1, y2, title);
        this.font = font;
        this.rows = maxRows;
        this.comboItemList = new ComboBox.ComboItemList(parent.getMinecraft(), maxRows, 15);
        for (T item : items) {
            this.comboItemList.addEntry(item, (String) displayStringMapper.apply(item));
        }
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.horizontalSizingOption = horizontal;
        this.verticalSizingOption = vertical;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (this.listOpened && this.comboItemList.m_6375_(x, y, button)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            this.listOpened = false;
            return true;
        } else {
            if (this.m_7972_(button)) {
                boolean flag = this.clicked(x, y);
                if (flag) {
                    this.onClick(x, y);
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double x, double y, double amount) {
        return this.listOpened ? this.comboItemList.m_6050_(x, y, amount) : false;
    }

    @Override
    protected boolean clicked(double x, double y) {
        return this.f_93623_ && this.f_93624_ && x >= (double) this.m_252754_() && y >= (double) this.m_252907_() && x < (double) (this.m_252754_() + this.f_93618_) && y < (double) (this.m_252907_() + this.f_93619_);
    }

    @Override
    public boolean isMouseOver(double x, double y) {
        return this.f_93623_ && this.f_93624_ && x >= (double) this.m_252754_() && y >= (double) this.m_252907_() && x < (double) (this.m_252754_() + this.f_93618_) && y < (double) (this.m_252907_() + this.f_93619_ * (this.rows + 1));
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        this.relocateComboList();
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        this.relocateComboList();
    }

    private void relocateComboList() {
        this.comboItemList.m_93437_(this.f_93618_, this.f_93619_ * this.rows, this.m_252907_() + this.f_93619_ + 1, this.m_252907_() + this.f_93619_ * this.rows + 1);
        this.comboItemList.m_93507_(this.m_252754_());
    }

    @Override
    public void onClick(double x, double y) {
        if (this.arrowClicked(x, y)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            this.listOpened = !this.listOpened;
        } else if (this.listOpened) {
            this.listOpened = false;
            this.m_7435_(Minecraft.getInstance().getSoundManager());
        }
    }

    private boolean arrowClicked(double x, double y) {
        int openPressed = this.m_252754_() + this.f_93618_ - 14;
        return this.f_93623_ && this.f_93624_ && x >= (double) openPressed && y >= (double) this.m_252907_() && x < (double) (this.m_252754_() + this.f_93618_) && y < (double) (this.m_252907_() + this.f_93619_);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int outlineColor = this.m_93696_() ? -1 : -6250336;
        guiGraphics.fill(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_ + 1, outlineColor);
        guiGraphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.f_93618_, this.m_252907_() + this.f_93619_, -16777216);
        String correctedString = this.font.plainSubstrByWidth(this.comboItemList.m_93511_() == null ? "" : ((ComboBox.ComboItemList.ComboItemEntry) this.comboItemList.m_93511_()).displayName, this.f_93618_ - 10);
        guiGraphics.drawString(this.font, Component.literal(correctedString), this.m_252754_() + 4, this.m_252907_() + this.f_93619_ / 2 - 9 / 2 + 1, 16777215, false);
        guiGraphics.drawString(this.font, Component.literal("▼"), this.m_252754_() + this.f_93618_ - 8, this.m_252907_() + this.f_93619_ / 2 - 9 / 2 + 1, 16777215, false);
        if (this.listOpened) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);
            this.comboItemList.render(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        Component component = this.m_6035_();
        return Component.translatable("gui.epicfight.narrate.comboBox", component);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementInput) {
        narrationElementInput.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    @Override
    public int getX1() {
        return this.x1;
    }

    @Override
    public int getX2() {
        return this.x2;
    }

    @Override
    public int getY1() {
        return this.y1;
    }

    @Override
    public int getY2() {
        return this.y2;
    }

    @Override
    public ResizableComponent.HorizontalSizing getHorizontalSizingOption() {
        return this.horizontalSizingOption;
    }

    @Override
    public ResizableComponent.VerticalSizing getVerticalSizingOption() {
        return this.verticalSizingOption;
    }

    @OnlyIn(Dist.CLIENT)
    class ComboItemList extends ObjectSelectionList<ComboBox<T>.ComboItemList.ComboItemEntry> {

        public ComboItemList(Minecraft minecraft, int maxRows, int itemHeight) {
            super(minecraft, ComboBox.this.f_93618_, ComboBox.this.f_93619_, 0, itemHeight * maxRows, itemHeight);
            this.m_93496_(false);
            this.m_93473_(false, 0);
            this.m_93488_(false);
        }

        public void addEntry(T item, String displayName) {
            this.m_7085_(new ComboBox.ComboItemList.ComboItemEntry(item, displayName));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            guiGraphics.fill(this.f_93393_ - 1, this.f_93390_ - 1, this.f_93392_ + 1, this.f_93391_ + 1, -1);
            guiGraphics.fill(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_, -16777216);
            super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.f_93392_ - 6;
        }

        @OnlyIn(Dist.CLIENT)
        class ComboItemEntry extends ObjectSelectionList.Entry<ComboBox<T>.ComboItemList.ComboItemEntry> {

            private final T item;

            private final String displayName;

            protected ComboItemEntry(T item, String displayName) {
                this.item = item;
                this.displayName = displayName;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 0) {
                    ComboItemList.this.m_6987_(this);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public Component getNarration() {
                return Component.empty();
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                guiGraphics.drawString(ComboBox.this.font, this.displayName, left + 2, top + 1, 16777215, false);
            }

            public T getItem() {
                return this.item;
            }
        }
    }
}