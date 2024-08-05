package io.github.lightman314.lightmanscurrency.client.gui.easy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IEasyScreen;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ILateRender;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IPreRender;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public abstract class EasyScreen extends Screen implements IEasyScreen {

    private final List<IPreRender> preRenders = new ArrayList();

    private final List<ILateRender> lateRenders = new ArrayList();

    private final List<IEasyTickable> guiTickers = new ArrayList();

    private final List<ITooltipSource> tooltipSources = new ArrayList();

    private final List<IScrollListener> scrollListeners = new ArrayList();

    private final List<IMouseListener> mouseListeners = new ArrayList();

    private ScreenArea screenArea = ScreenArea.of(0, 0, 100, 100);

    @Nonnull
    @Override
    public final Font getFont() {
        return this.f_96547_;
    }

    @Nonnull
    @Override
    public final Player getPlayer() {
        return this.f_96541_.player;
    }

    protected EasyScreen() {
        this(EasyText.empty());
    }

    protected EasyScreen(Component title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Nonnull
    @Override
    public final ScreenArea getArea() {
        return this.screenArea;
    }

    @Override
    public final int getGuiLeft() {
        return this.screenArea.x;
    }

    @Override
    public final int getGuiTop() {
        return this.screenArea.y;
    }

    @Nonnull
    @Override
    public final ScreenPosition getCorner() {
        return this.screenArea.pos;
    }

    @Override
    public final int getXSize() {
        return this.screenArea.width;
    }

    @Override
    public final int getYSize() {
        return this.screenArea.height;
    }

    protected final void resize(int width, int height) {
        this.screenArea = this.screenArea.ofSize(width, height);
        this.recalculateCorner();
    }

    @Override
    protected final void init() {
        this.preRenders.clear();
        this.lateRenders.clear();
        this.guiTickers.clear();
        this.tooltipSources.clear();
        this.scrollListeners.clear();
        this.mouseListeners.clear();
        super.init();
        this.recalculateCorner();
        this.initialize(this.screenArea);
    }

    protected void recalculateCorner() {
        this.screenArea = this.screenArea.atPosition(ScreenPosition.of((this.f_96543_ - this.screenArea.width) / 2, (this.f_96544_ - this.screenArea.height) / 2));
    }

    protected abstract void initialize(ScreenArea var1);

    @Override
    public final void render(@Nonnull GuiGraphics mcgui, int mouseX, int mouseY, float partialTicks) {
        this.renderTick();
        EasyGuiGraphics gui = EasyGuiGraphics.create(mcgui, this.f_96547_, mouseX, mouseY, partialTicks).pushOffset(this.getCorner());
        UnmodifiableIterator var6 = ImmutableList.copyOf(this.preRenders).iterator();
        while (var6.hasNext()) {
            IPreRender r = (IPreRender) var6.next();
            try {
                r.preRender(gui);
            } catch (Throwable var10) {
                LightmansCurrency.LogError("Error occurred while early rendering " + r.getClass().getName(), var10);
            }
        }
        this.m_280273_(mcgui);
        this.renderBG(gui);
        super.render(mcgui, mouseX, mouseY, partialTicks);
        var6 = ImmutableList.copyOf(this.lateRenders).iterator();
        while (var6.hasNext()) {
            ILateRender r = (ILateRender) var6.next();
            try {
                r.lateRender(gui);
            } catch (Throwable var9) {
                LightmansCurrency.LogError("Error occurred while late rendering " + r.getClass().getName(), var9);
            }
        }
        this.renderAfterWidgets(gui);
        EasyScreenHelper.RenderTooltips(gui, ImmutableList.copyOf(this.tooltipSources));
        this.renderAfterTooltips(gui);
    }

    protected void renderTick() {
    }

    protected abstract void renderBG(@Nonnull EasyGuiGraphics var1);

    protected void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
    }

    protected void renderAfterTooltips(@Nonnull EasyGuiGraphics gui) {
    }

    @Override
    public final <T> T addChild(T child) {
        if (child instanceof EasyWidgetWithChildren w) {
            w.pairWithScreen(this::addChild, this::removeChild);
            if (w.addChildrenBeforeThis()) {
                w.addChildren();
            }
        }
        if (child instanceof Renderable r && !this.f_169369_.contains(child)) {
            this.f_169369_.add(r);
        }
        if (child instanceof GuiEventListener && child instanceof NarratableEntry) {
            super.addWidget((T) ((GuiEventListener) ((NarratableEntry) child)));
        }
        IEasyTickable ticker = EasyScreenHelper.getWidgetTicker(child);
        if (ticker != null && !this.guiTickers.contains(ticker)) {
            this.guiTickers.add(ticker);
        }
        if (child instanceof ITooltipSource t && !this.tooltipSources.contains(t)) {
            this.tooltipSources.add(t);
        }
        if (child instanceof IMouseListener l && !this.mouseListeners.contains(l)) {
            this.mouseListeners.add(l);
        }
        if (child instanceof IScrollListener l && !this.scrollListeners.contains(l)) {
            this.scrollListeners.add(l);
        }
        if (child instanceof IPreRender r && !this.preRenders.contains(r)) {
            this.preRenders.add(r);
        }
        if (child instanceof ILateRender r && !this.lateRenders.contains(r)) {
            this.lateRenders.add(r);
        }
        if (child instanceof EasyWidget wx) {
            wx.addAddons(this::addChild);
        }
        if (child instanceof EasyWidgetWithChildren wx && !wx.addChildrenBeforeThis()) {
            wx.addChildren();
        }
        return child;
    }

    @Override
    public final void removeChild(Object child) {
        if (child instanceof Renderable r) {
            this.f_169369_.remove(r);
        }
        if (child instanceof GuiEventListener l) {
            super.removeWidget(l);
        }
        IEasyTickable ticker = EasyScreenHelper.getWidgetTicker(child);
        this.guiTickers.remove(ticker);
        if (child instanceof ITooltipSource t) {
            this.tooltipSources.remove(t);
        }
        if (child instanceof IMouseListener l) {
            this.mouseListeners.remove(l);
        }
        if (child instanceof IScrollListener l) {
            this.scrollListeners.remove(l);
        }
        if (child instanceof EasyWidget w) {
            w.removeAddons(this::removeChild);
        }
        if (child instanceof IPreRender r) {
            this.preRenders.remove(r);
        }
        if (child instanceof ILateRender r) {
            this.lateRenders.remove(r);
        }
        if (child instanceof EasyWidgetWithChildren w) {
            w.removeChildren();
        }
    }

    @Override
    public final void tick() {
        super.tick();
        UnmodifiableIterator var1 = ImmutableList.copyOf(this.guiTickers).iterator();
        while (var1.hasNext()) {
            IEasyTickable t = (IEasyTickable) var1.next();
            t.tick();
        }
        this.screenTick();
    }

    protected void screenTick() {
    }

    @Nonnull
    @Deprecated
    @Override
    protected final <T extends GuiEventListener & NarratableEntry> T addWidget(@Nonnull T widget) {
        return this.addChild(widget);
    }

    @Nonnull
    @Deprecated
    @Override
    protected final <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(@Nonnull T widget) {
        return this.addChild(widget);
    }

    @Nonnull
    @Deprecated
    @Override
    protected final <T extends Renderable> T addRenderableOnly(@Nonnull T widget) {
        return this.addChild(widget);
    }

    @Deprecated
    @Override
    protected final void removeWidget(@Nonnull GuiEventListener widget) {
        this.removeChild(widget);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        UnmodifiableIterator var7 = ImmutableList.copyOf(this.scrollListeners).iterator();
        while (var7.hasNext()) {
            IScrollListener l = (IScrollListener) var7.next();
            if (l.mouseScrolled(mouseX, mouseY, scroll)) {
                return true;
            }
        }
        return super.m_6050_(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        UnmodifiableIterator var6 = ImmutableList.copyOf(this.mouseListeners).iterator();
        while (var6.hasNext()) {
            IMouseListener l = (IMouseListener) var6.next();
            if (l.onMouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.m_6375_(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        UnmodifiableIterator var6 = ImmutableList.copyOf(this.mouseListeners).iterator();
        while (var6.hasNext()) {
            IMouseListener l = (IMouseListener) var6.next();
            if (l.onMouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.m_6348_(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        InputConstants.Key mouseKey = InputConstants.getKey(int0, int1);
        if (this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey) && this.blockInventoryClosing()) {
            return true;
        } else if (this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.m_7379_();
            return true;
        } else {
            return super.keyPressed(int0, int1, int2);
        }
    }
}