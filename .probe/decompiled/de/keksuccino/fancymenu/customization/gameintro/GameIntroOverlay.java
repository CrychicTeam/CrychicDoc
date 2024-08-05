package de.keksuccino.fancymenu.customization.gameintro;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenCompletedEvent;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenEvent;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenStartingEvent;
import de.keksuccino.fancymenu.events.screen.OpenScreenEvent;
import de.keksuccino.fancymenu.events.screen.OpenScreenPostInitEvent;
import de.keksuccino.fancymenu.events.screen.RenderScreenEvent;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.resource.RenderableResource;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class GameIntroOverlay extends Overlay {

    private static final Logger LOGGER = LogManager.getLogger();

    protected Font font;

    @NotNull
    protected Screen fadeTo;

    protected PlayableResource intro;

    protected float opacity;

    protected long start;

    protected boolean fadeToInitialized;

    protected int width;

    protected int height;

    public GameIntroOverlay(@NotNull Screen fadeTo, @NotNull PlayableResource intro) {
        this.font = Minecraft.getInstance().font;
        this.opacity = 1.0F;
        this.start = -1L;
        this.fadeToInitialized = false;
        this.fadeTo = (Screen) Objects.requireNonNull(fadeTo);
        this.intro = (PlayableResource) Objects.requireNonNull(intro);
        this.intro.waitForReady(5000L);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        if (this.start == -1L) {
            this.start = System.currentTimeMillis();
            this.intro.stop();
            this.intro.play();
        }
        if (this.endOfIntroReached() && !this.fadeToInitialized) {
            this.initFadeToScreen();
        }
        this.tickFadeOut();
        if (!this.endOfIntroReached() || this.fadeOutIntro() && !(this.opacity < 0.1F)) {
            if (this.endOfIntroReached()) {
                EventHandler.INSTANCE.postEvent(new RenderScreenEvent.Pre(this.fadeTo, graphics, mouseX, mouseY, partial));
                this.fadeTo.render(graphics, mouseX, mouseY, partial);
                EventHandler.INSTANCE.postEvent(new RenderScreenEvent.Post(this.fadeTo, graphics, mouseX, mouseY, partial));
            } else {
                graphics.fill(0, 0, this.width, this.height, DrawableColor.BLACK.getColorInt());
            }
            RenderingUtils.resetShaderColor(graphics);
            this.renderIntro(graphics, mouseX, mouseY, partial);
            this.renderSkipText(graphics, mouseX, mouseY, partial);
        } else {
            this.close();
        }
    }

    protected void renderIntro(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.intro instanceof RenderableResource r) {
            AspectRatio ratio = r.getAspectRatio();
            int[] size = ratio.getAspectRatioSizeByMinimumSize(this.width, this.height);
            int aspectWidth = size[0];
            int aspectHeight = size[1];
            int x = this.width / 2 - aspectWidth / 2;
            int y = this.height / 2 - aspectHeight / 2;
            ResourceLocation location = r.getResourceLocation();
            if (location != null) {
                RenderSystem.enableBlend();
                graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
                graphics.blit(location, x, y, 0.0F, 0.0F, aspectWidth, aspectHeight, aspectWidth, aspectHeight);
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderSkipText(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (FancyMenu.getOptions().gameIntroAllowSkip.getValue()) {
            float scale = 1.3F;
            String customSkipText = FancyMenu.getOptions().gameIntroCustomSkipText.getValue();
            if (!customSkipText.isEmpty() && LocalizationUtils.isLocalizationKey(customSkipText)) {
                customSkipText = I18n.get(customSkipText);
            }
            Component skipComp = customSkipText.isEmpty() ? Component.translatable("fancymenu.game_intro.press_any_key") : Component.literal(customSkipText);
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);
            int normalizedWidth = (int) ((float) this.width / scale);
            int normalizedHeight = (int) ((float) this.height / scale);
            int textX = normalizedWidth / 2 - this.font.width(skipComp) / 2;
            int textY = normalizedHeight - 40;
            graphics.drawString(this.font, skipComp, textX, textY, RenderingUtils.replaceAlphaInColor(DrawableColor.WHITE.getColorInt(), Math.max(0.1F, 0.6F * this.opacity)), false);
            graphics.pose().popPose();
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected boolean fadeOutIntro() {
        return FancyMenu.getOptions().gameIntroFadeOut.getValue();
    }

    protected boolean endOfIntroReached() {
        if (this.start == -1L) {
            return false;
        } else {
            long now = System.currentTimeMillis();
            return this.start + 2000L < now && !this.intro.isPlaying();
        }
    }

    protected void tickFadeOut() {
        if (this.endOfIntroReached() && this.fadeOutIntro()) {
            this.opacity -= 0.02F;
        }
    }

    protected void initFadeToScreen() {
        ScreenCustomization.setIsNewMenu(true);
        ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(this.fadeTo);
        if (layer != null) {
            layer.resetLayer();
        }
        EventHandler.INSTANCE.postEvent(new OpenScreenEvent(this.fadeTo));
        EventHandler.INSTANCE.postEvent(new InitOrResizeScreenStartingEvent(this.fadeTo, InitOrResizeScreenEvent.InitializationPhase.INIT));
        EventHandler.INSTANCE.postEvent(new InitOrResizeScreenEvent.Pre(this.fadeTo, InitOrResizeScreenEvent.InitializationPhase.INIT));
        this.fadeTo.init(Minecraft.getInstance(), Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
        EventHandler.INSTANCE.postEvent(new InitOrResizeScreenEvent.Post(this.fadeTo, InitOrResizeScreenEvent.InitializationPhase.INIT));
        EventHandler.INSTANCE.postEvent(new InitOrResizeScreenCompletedEvent(this.fadeTo, InitOrResizeScreenEvent.InitializationPhase.INIT));
        EventHandler.INSTANCE.postEvent(new OpenScreenPostInitEvent(this.fadeTo));
        this.fadeToInitialized = true;
    }

    protected void close() {
        if (!this.fadeToInitialized) {
            this.initFadeToScreen();
        }
        Minecraft.getInstance().setOverlay(null);
    }

    public void keyPressed(int keycode, int scancode, int modifiers) {
        if (FancyMenu.getOptions().gameIntroAllowSkip.getValue()) {
            this.close();
        }
    }

    public void mouseClicked(int button) {
        if (FancyMenu.getOptions().gameIntroAllowSkip.getValue()) {
            this.close();
        }
    }
}