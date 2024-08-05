package de.keksuccino.fancymenu.customization.element.elements.ticker;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.action.blocks.GenericExecutableBlock;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.ExecutableElement;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TickerElement extends AbstractElement implements ExecutableElement {

    private static final DrawableColor BACKGROUND_COLOR = DrawableColor.of(Color.ORANGE);

    @NotNull
    public volatile GenericExecutableBlock actionExecutor = new GenericExecutableBlock();

    public volatile long tickDelayMs = 0L;

    public volatile boolean isAsync = false;

    public volatile TickerElement.TickMode tickMode = TickerElement.TickMode.NORMAL;

    protected volatile boolean ready = false;

    protected volatile boolean ticked = false;

    protected volatile long lastTick = -1L;

    protected volatile TickerElement.TickerElementThreadController asyncThreadController = null;

    public TickerElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    protected void tickerElementTick() {
        if (this.ready && this.shouldRender()) {
            if (this.ticked && this.tickMode == TickerElement.TickMode.ON_MENU_LOAD) {
                return;
            }
            if (this.tickMode == TickerElement.TickMode.ONCE_PER_SESSION && TickerElementBuilder.cachedOncePerSessionItems.contains(this.getInstanceIdentifier())) {
                return;
            }
            if (this.tickMode == TickerElement.TickMode.ONCE_PER_SESSION) {
                TickerElementBuilder.cachedOncePerSessionItems.add(this.getInstanceIdentifier());
            } else {
                TickerElementBuilder.cachedOncePerSessionItems.remove(this.getInstanceIdentifier());
            }
            long now = System.currentTimeMillis();
            if (this.tickDelayMs <= 0L || this.lastTick + this.tickDelayMs <= now) {
                this.lastTick = now;
                this.ticked = true;
                this.actionExecutor.execute();
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.ready = true;
        if (isEditor()) {
            int x = this.getAbsoluteX();
            int y = this.getAbsoluteY();
            int w = this.getAbsoluteWidth();
            int h = this.getAbsoluteHeight();
            RenderSystem.enableBlend();
            graphics.fill(x, y, x + w, y + h, BACKGROUND_COLOR.getColorInt());
            graphics.enableScissor(x, y, x + w, y + h);
            graphics.drawCenteredString(Minecraft.getInstance().font, this.getDisplayName(), x + w / 2, y + h / 2 - 9 / 2, -1);
            graphics.disableScissor();
            RenderingUtils.resetShaderColor(graphics);
        } else if (!this.isAsync) {
            this.tickerElementTick();
        }
        if (this.isAsync && (this.asyncThreadController == null || !this.asyncThreadController.running) && !isEditor()) {
            this.asyncThreadController = new TickerElement.TickerElementThreadController();
            TickerElementBuilder.cachedThreadControllers.add(this.asyncThreadController);
            new Thread(() -> {
                while (this.asyncThreadController != null && this.asyncThreadController.running && this.isAsync) {
                    this.tickerElementTick();
                    try {
                        Thread.sleep(50L);
                    } catch (Exception var2x) {
                        var2x.printStackTrace();
                    }
                }
            }).start();
        }
        if (!this.isAsync && this.asyncThreadController != null) {
            this.asyncThreadController.running = false;
        }
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @NotNull
    @Override
    public GenericExecutableBlock getExecutableBlock() {
        return this.actionExecutor;
    }

    public static enum TickMode {

        NORMAL("normal"), ONCE_PER_SESSION("once_per_session"), ON_MENU_LOAD("on_menu_load");

        public final String name;

        private TickMode(String name) {
            this.name = name;
        }

        @Nullable
        public static TickerElement.TickMode getByName(String name) {
            for (TickerElement.TickMode t : values()) {
                if (t.name.equals(name)) {
                    return t;
                }
            }
            return null;
        }
    }

    public static class TickerElementThreadController {

        public volatile boolean running = true;
    }
}