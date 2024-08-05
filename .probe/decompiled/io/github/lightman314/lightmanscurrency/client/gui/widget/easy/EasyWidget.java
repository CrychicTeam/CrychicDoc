package io.github.lightman314.lightmanscurrency.client.gui.widget.easy;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public abstract class EasyWidget extends AbstractWidget {

    private ScreenArea area;

    private final List<WidgetAddon> addons = new ArrayList();

    private boolean lockAddons = false;

    public final ScreenArea getArea() {
        return this.area;
    }

    @Override
    public final int getX() {
        return this.area.x;
    }

    @Override
    public final void setX(int x) {
        this.area = this.area.atPosition(ScreenPosition.of(x, this.area.y));
    }

    @Override
    public final int getY() {
        return this.area.y;
    }

    @Override
    public final void setY(int y) {
        this.area = this.area.atPosition(ScreenPosition.of(this.area.x, y));
    }

    public final ScreenPosition getPosition() {
        return this.area.pos;
    }

    @Override
    public final void setPosition(int x, int y) {
        this.area = this.area.atPosition(x, y);
    }

    public final void setPosition(@Nonnull ScreenPosition pos) {
        this.area = this.area.atPosition(pos);
    }

    @Override
    public final int getWidth() {
        return this.area.width;
    }

    @Override
    public final int getHeight() {
        return this.area.height;
    }

    @Override
    public final void setWidth(int width) {
        this.area = this.area.ofSize(width, this.area.height);
        super.setWidth(width);
    }

    public final void setHeight(int height) {
        this.area = this.area.ofSize(this.area.width, height);
        super.setHeight(height);
    }

    public final void setSize(int width, int height) {
        this.area = this.area.ofSize(width, height);
        super.setWidth(width);
        super.setHeight(height);
    }

    public final boolean isVisible() {
        this.visibleTickInternal();
        return this.f_93624_;
    }

    public final void setVisible(boolean visible) {
        this.f_93624_ = visible;
    }

    public boolean hideFromMouse() {
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (this.hideFromMouse()) {
            return false;
        } else {
            this.activeTickInternal();
            return super.isMouseOver(mouseX, mouseY);
        }
    }

    @Override
    public final boolean isActive() {
        this.activeTickInternal();
        return super.isActive();
    }

    public final void setActive(boolean active) {
        this.f_93623_ = active;
    }

    public final boolean isMouseOver(ScreenPosition mousePos) {
        return this.isMouseOver((double) mousePos.x, (double) mousePos.y);
    }

    protected EasyWidget(int x, int y, int width, int height) {
        this(ScreenArea.of(ScreenPosition.of(x, y), width, height));
    }

    protected EasyWidget(int x, int y, int width, int height, Component title) {
        this(ScreenArea.of(ScreenPosition.of(x, y), width, height), title);
    }

    protected EasyWidget(ScreenPosition position, int width, int height) {
        this(ScreenArea.of(position, width, height));
    }

    protected EasyWidget(ScreenPosition position, int width, int height, Component title) {
        this(ScreenArea.of(position, width, height), title);
    }

    protected EasyWidget(ScreenArea area) {
        this(area, EasyText.empty());
    }

    protected EasyWidget(ScreenArea area, Component title) {
        super(area.x, area.y, area.width, area.height, title);
        this.area = area;
    }

    public abstract Object withAddons(WidgetAddon... var1);

    protected final void withAddonsInternal(WidgetAddon... addons) {
        if (!this.lockAddons) {
            for (WidgetAddon a : addons) {
                if (a != null && !this.addons.contains(a)) {
                    this.addons.add(a);
                    a.attach(this);
                }
            }
        }
    }

    public final void addAddons(Consumer<WidgetAddon> consumer) {
        this.lockAddons = true;
        for (WidgetAddon addon : this.addons) {
            consumer.accept(addon);
        }
    }

    public void removeAddons(Consumer<WidgetAddon> consumer) {
        for (WidgetAddon addon : this.addons) {
            consumer.accept(addon);
        }
    }

    @Override
    public final void render(@Nonnull GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        this.renderTickInternal();
        super.render(gui, mouseX, mouseY, partialTick);
    }

    private void visibleTickInternal() {
        this.addons.forEach(WidgetAddon::visibleTick);
    }

    private void activeTickInternal() {
        this.visibleTickInternal();
        this.addons.forEach(WidgetAddon::activeTick);
    }

    private void renderTickInternal() {
        this.activeTickInternal();
        this.addons.forEach(WidgetAddon::renderTick);
        this.renderTick();
    }

    protected void renderTick() {
    }

    @Override
    protected final void renderWidget(@Nonnull GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        try {
            this.renderWidget(EasyGuiGraphics.create(gui, mouseX, mouseY, partialTicks).pushOffset(this.getPosition()));
        } catch (Throwable var6) {
            LightmansCurrency.LogError("Error occurred while rendering " + this.getClass().getName(), var6);
        }
    }

    protected abstract void renderWidget(@Nonnull EasyGuiGraphics var1);

    @Override
    protected boolean isValidClickButton(int button) {
        return false;
    }

    @Override
    public void playDownSound(@Nonnull SoundManager manager) {
    }

    @Override
    protected void updateWidgetNarration(@Nonnull NarrationElementOutput narrator) {
    }
}