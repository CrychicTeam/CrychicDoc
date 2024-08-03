package harmonised.pmmo.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

public class SelectionWidget<T extends SelectionWidget.SelectionEntry<?>> extends AbstractWidget {

    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/resource_packs.png");

    private static final int ENTRY_HEIGHT = 20;

    private final Component title;

    private final Consumer<T> selectCallback;

    private List<T> entries = new ArrayList();

    private T selected = (T) null;

    private boolean extended = false;

    private int scrollOffset = 0;

    private Font font;

    public SelectionWidget(int x, int y, int width, Component title, Consumer<T> selectCallback) {
        super(x, y, width, 20, Component.empty());
        this.font = Minecraft.getInstance().font;
        this.title = title;
        this.selectCallback = selectCallback;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(f_93617_, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.getHeight(), 20, 4, 200, 20, 0, 66);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.selected != null) {
            this.selected.render(graphics, this.m_252754_(), this.m_252907_(), this.f_93618_, false, this.getFGColor(), this.f_93625_);
        } else {
            graphics.drawString(this.font, this.title, this.m_252754_() + 6, this.m_252907_() + (this.f_93619_ - 8) / 2, this.getFGColor() | Mth.ceil(this.f_93625_ * 255.0F) << 24);
        }
        if (this.extended) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.0F, 0.0F, 500.0F);
            int boxHeight = Math.max(1, 20 * Math.min(this.entries.size(), 4)) + 2;
            graphics.fill(RenderType.gui(), this.m_252754_(), this.m_252907_() + 20 - 1, this.m_252754_() + this.f_93618_, this.m_252907_() + 20 + boxHeight - 1, -1);
            graphics.fill(RenderType.gui(), this.m_252754_() + 1, this.m_252907_() + 20, this.m_252754_() + this.f_93618_ - 1, this.m_252907_() + 20 + boxHeight - 2, -16777216);
            graphics.blit(ICONS, this.m_252754_() + this.f_93618_ - 17, this.m_252907_() + 6, 114, 5, 11, 7);
            T hoverEntry = this.getEntryAtPosition((double) mouseX, (double) mouseY);
            for (int i = 0; i < 4; i++) {
                int idx = i + this.scrollOffset;
                if (idx < this.entries.size()) {
                    int entryY = this.m_252907_() + (i + 1) * 20;
                    T entry = (T) this.entries.get(idx);
                    entry.render(graphics, this.m_252754_() + 1, entryY, this.f_93618_ - 2, entry == hoverEntry, this.getFGColor(), this.f_93625_);
                }
            }
            if (this.entries.size() > 4) {
                float scale = 4.0F / (float) this.entries.size();
                int scrollY = this.m_252907_() + (int) ((float) (20 * this.scrollOffset) * scale) + 20;
                int barHeight = (int) (80.0F * scale + 1.0F);
                int scrollBotY = Math.min(scrollY + barHeight, this.m_252907_() + 20 + boxHeight - 2);
                graphics.fill(RenderType.gui(), this.m_252754_() + this.f_93618_ - 5, scrollY, this.m_252754_() + this.f_93618_ - 1, scrollBotY, -10066330);
                graphics.fill(RenderType.gui(), this.m_252754_() + this.f_93618_ - 4, scrollY + 1, this.m_252754_() + this.f_93618_ - 2, scrollBotY - 1, -5592406);
            }
            graphics.pose().popPose();
        } else {
            graphics.blit(ICONS, this.m_252754_() + this.f_93618_ - 17, this.m_252907_() + 6, 82, 20, 11, 7);
        }
    }

    @Override
    public int getHeight() {
        return this.extended ? 20 * (Math.min(this.entries.size(), 4) + 1) + 1 : 20;
    }

    public boolean isExtended() {
        return this.extended;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.f_93623_ && mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + this.f_93618_) && mouseY >= (double) this.m_252907_() && mouseY <= (double) (this.m_252907_() + this.getHeight())) {
            int maxX = this.m_252754_() + this.f_93618_ - (this.entries.size() > 4 ? 5 : 0);
            int maxY = this.m_252907_() + 20 * Math.min(this.entries.size() + 1, 5);
            if (this.extended && mouseX < (double) maxX && mouseY > (double) (this.m_252907_() + 20) && mouseY < (double) maxY) {
                this.setSelected(this.getEntryAtPosition(mouseX, mouseY), true);
            }
            if (mouseY < (double) (this.m_252907_() + 20) && mouseX < (double) (this.m_252754_() + this.f_93618_) || mouseX < (double) maxX) {
                this.extended = !this.extended;
                this.scrollOffset = 0;
            }
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            return true;
        } else {
            this.extended = false;
            this.scrollOffset = 0;
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int maxY = this.m_252907_() + 20 * Math.min(this.entries.size() + 1, 5);
        if (this.extended && mouseX >= (double) this.m_252754_() && mouseX <= (double) (this.m_252754_() + this.f_93618_) && mouseY > (double) (this.m_252907_() + 20) && mouseY < (double) maxY) {
            if (delta < 0.0 && this.scrollOffset < this.entries.size() - 4) {
                this.scrollOffset++;
            } else if (delta > 0.0 && this.scrollOffset > 0) {
                this.scrollOffset--;
            }
            return true;
        } else {
            return super.m_6050_(mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return this.f_93623_ && this.f_93624_ ? pMouseX >= (double) this.m_252754_() && pMouseY >= (double) this.m_252907_() && pMouseX < (double) (this.m_252754_() + this.f_93618_) && pMouseY < (double) (this.m_252907_() + this.getHeight()) : false;
    }

    private T getEntryAtPosition(double mouseX, double mouseY) {
        if (!(mouseX < (double) this.m_252754_()) && !(mouseX > (double) (this.m_252754_() + this.f_93618_)) && !(mouseY < (double) (this.m_252907_() + 20)) && !(mouseY > (double) (this.m_252907_() + 100))) {
            double posY = mouseY - (double) (this.m_252907_() + 20);
            int idx = (int) (posY / 20.0) + this.scrollOffset;
            return (T) (idx < this.entries.size() ? this.entries.get(idx) : null);
        } else {
            return null;
        }
    }

    public void setEntries(Collection<T> entry) {
        this.entries = new ArrayList(entry);
    }

    public void setSelected(T selected, boolean notify) {
        this.selected = selected;
        if (notify && this.selectCallback != null) {
            this.selectCallback.accept(selected);
        }
    }

    public T getSelected() {
        return this.selected;
    }

    public Stream<T> stream() {
        return this.entries.stream();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }

    public static class SelectionEntry<T> implements GuiEventListener {

        private Font font;

        public final Component message;

        public T reference;

        public SelectionEntry(Component message, T reference) {
            this.font = Minecraft.getInstance().font;
            this.message = message;
            this.reference = reference;
        }

        public void render(GuiGraphics graphics, int x, int y, int width, boolean hovered, int fgColor, float alpha) {
            if (hovered) {
                graphics.fill(RenderType.gui(), x, y, x + width, y + 20, -6250336);
            }
            FormattedCharSequence text = Language.getInstance().getVisualOrder(FormattedText.composite(this.font.substrByWidth(this.message, width - 12)));
            graphics.drawString(this.font, text, x + 6, y + 6, fgColor | Mth.ceil(alpha * 255.0F) << 24);
        }

        @Override
        public void setFocused(boolean boolean0) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }
}