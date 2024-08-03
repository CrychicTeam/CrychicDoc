package vazkii.patchouli.client.book.gui.button;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.client.book.gui.GuiBook;

public class GuiButtonBook extends Button {

    protected final GuiBook parent;

    protected final int u;

    protected final int v;

    private final Supplier<Boolean> displayCondition;

    protected final List<Component> tooltip;

    public GuiButtonBook(GuiBook parent, int x, int y, int u, int v, int w, int h, Button.OnPress onPress, Component... tooltip) {
        this(parent, x, y, u, v, w, h, () -> true, onPress, tooltip);
    }

    public GuiButtonBook(GuiBook parent, int x, int y, int u, int v, int w, int h, Supplier<Boolean> displayCondition, Button.OnPress onPress, Component... tooltip) {
        super(x, y, w, h, tooltip[0], onPress, f_252438_);
        this.parent = parent;
        this.u = u;
        this.v = v;
        this.displayCondition = displayCondition;
        this.tooltip = Arrays.asList(tooltip);
    }

    @Override
    public final void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.f_93623_ = this.f_93624_ = (Boolean) this.displayCondition.get();
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GuiBook.drawFromTexture(graphics, this.parent.book, this.m_252754_(), this.m_252907_(), this.u + (this.m_198029_() ? this.f_93618_ : 0), this.v, this.f_93618_, this.f_93619_);
        if (this.m_198029_()) {
            this.parent.setTooltip(this.getTooltipLines());
        }
    }

    @Override
    public void playDownSound(SoundManager soundHandlerIn) {
        GuiBook.playBookFlipSound(this.parent.book);
    }

    public List<Component> getTooltipLines() {
        return this.tooltip;
    }
}