package mezz.jei.gui.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.common.config.DebugConfig;
import mezz.jei.common.gui.TooltipRenderer;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.common.util.RectDebugger;
import mezz.jei.core.util.LimitedLogger;
import mezz.jei.gui.input.MouseUtil;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.gui.overlay.bookmarks.BookmarkOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final LimitedLogger missingBackgroundLogger = new LimitedLogger(LOGGER, Duration.ofHours(1L));

    private final IngredientListOverlay ingredientListOverlay;

    private final IScreenHelper screenHelper;

    private final BookmarkOverlay bookmarkOverlay;

    private boolean drawnOnBackground = false;

    public GuiEventHandler(IScreenHelper screenHelper, BookmarkOverlay bookmarkOverlay, IngredientListOverlay ingredientListOverlay) {
        this.screenHelper = screenHelper;
        this.bookmarkOverlay = bookmarkOverlay;
        this.ingredientListOverlay = ingredientListOverlay;
    }

    public void onGuiInit(Screen screen) {
        Set<ImmutableRect2i> guiExclusionAreas = (Set<ImmutableRect2i>) this.screenHelper.getGuiExclusionAreas(screen).map(ImmutableRect2i::new).collect(Collectors.toUnmodifiableSet());
        this.ingredientListOverlay.updateScreen(screen, guiExclusionAreas);
        this.bookmarkOverlay.updateScreen(screen, guiExclusionAreas);
    }

    public void onGuiOpen(Screen screen) {
        this.ingredientListOverlay.updateScreen(screen, null);
        this.bookmarkOverlay.updateScreen(screen, null);
    }

    public void onDrawBackgroundPost(Screen screen, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Set<ImmutableRect2i> guiExclusionAreas = (Set<ImmutableRect2i>) this.screenHelper.getGuiExclusionAreas(screen).map(ImmutableRect2i::new).collect(Collectors.toUnmodifiableSet());
        this.ingredientListOverlay.updateScreen(screen, guiExclusionAreas);
        this.bookmarkOverlay.updateScreen(screen, guiExclusionAreas);
        this.drawnOnBackground = true;
        double mouseX = MouseUtil.getX();
        double mouseY = MouseUtil.getY();
        this.ingredientListOverlay.drawScreen(minecraft, guiGraphics, (int) mouseX, (int) mouseY, minecraft.getFrameTime());
        this.bookmarkOverlay.drawScreen(minecraft, guiGraphics, (int) mouseX, (int) mouseY, minecraft.getFrameTime());
    }

    public void onDrawForeground(AbstractContainerScreen<?> screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
        poseStack.translate((float) (-screenHelper.getGuiLeft(screen)), (float) (-screenHelper.getGuiTop(screen)), 0.0F);
        this.bookmarkOverlay.drawOnForeground(minecraft, guiGraphics, mouseX, mouseY);
        this.ingredientListOverlay.drawOnForeground(minecraft, guiGraphics, mouseX, mouseY);
        poseStack.popPose();
    }

    public void onDrawScreenPost(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        this.ingredientListOverlay.updateScreen(screen, null);
        this.bookmarkOverlay.updateScreen(screen, null);
        if (!this.drawnOnBackground) {
            if (screen instanceof AbstractContainerScreen) {
                String guiName = screen.getClass().getName();
                missingBackgroundLogger.log(Level.WARN, guiName, "GUI did not draw the dark background layer behind itself, this may result in display issues: {}", guiName);
            }
            this.ingredientListOverlay.drawScreen(minecraft, guiGraphics, mouseX, mouseY, minecraft.getFrameTime());
            this.bookmarkOverlay.drawScreen(minecraft, guiGraphics, mouseX, mouseY, minecraft.getFrameTime());
        }
        this.drawnOnBackground = false;
        if (screen instanceof AbstractContainerScreen<?> guiContainer) {
            IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
            int guiLeft = screenHelper.getGuiLeft(guiContainer);
            int guiTop = screenHelper.getGuiTop(guiContainer);
            this.screenHelper.getGuiClickableArea(guiContainer, (double) (mouseX - guiLeft), (double) (mouseY - guiTop)).filter(IGuiClickableArea::isTooltipEnabled).map(IGuiClickableArea::getTooltipStrings).findFirst().ifPresent(tooltipStrings -> {
                if (tooltipStrings.isEmpty()) {
                    tooltipStrings = List.of(Component.translatable("jei.tooltip.show.recipes"));
                }
                TooltipRenderer.drawHoveringText(guiGraphics, tooltipStrings, mouseX, mouseY);
            });
        }
        this.ingredientListOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
        this.bookmarkOverlay.drawTooltips(minecraft, guiGraphics, mouseX, mouseY);
        if (DebugConfig.isDebugModeEnabled()) {
            this.drawDebugInfoForScreen(screen, guiGraphics);
        }
    }

    public void onClientTick() {
        this.ingredientListOverlay.handleTick();
    }

    public boolean renderCompactPotionIndicators() {
        return this.ingredientListOverlay.isListDisplayed();
    }

    private void drawDebugInfoForScreen(Screen screen, GuiGraphics guiGraphics) {
        RectDebugger.INSTANCE.draw(guiGraphics);
        this.screenHelper.getGuiProperties(screen).ifPresent(guiProperties -> {
            Set<Rect2i> guiExclusionAreas = (Set<Rect2i>) this.screenHelper.getGuiExclusionAreas(screen).collect(Collectors.toUnmodifiableSet());
            RenderSystem.disableDepthTest();
            for (Rect2i area : guiExclusionAreas) {
                guiGraphics.fill(RenderType.gui(), area.getX(), area.getY(), area.getX() + area.getWidth(), area.getY() + area.getHeight(), 1157562368);
            }
            guiGraphics.fill(RenderType.gui(), guiProperties.getGuiLeft(), guiProperties.getGuiTop(), guiProperties.getGuiLeft() + guiProperties.getGuiXSize(), guiProperties.getGuiTop() + guiProperties.getGuiYSize(), 583846912);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        });
    }
}