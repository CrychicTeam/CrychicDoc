package mezz.jei.gui.input;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import mezz.jei.common.gui.elements.DrawableNineSliceTexture;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.core.util.TextHistory;
import mezz.jei.gui.input.handlers.TextFieldInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.Nullable;

public class GuiTextFieldFilter extends EditBox {

    private static final int maxSearchLength = 128;

    private static final TextHistory history = new TextHistory();

    private final BooleanSupplier filterEmpty;

    private ImmutableRect2i area;

    private final DrawableNineSliceTexture background;

    private ImmutableRect2i backgroundBounds;

    @Nullable
    private AbstractWidget previouslyFocusedWidget;

    public GuiTextFieldFilter(Textures textures, BooleanSupplier filterEmpty) {
        super(Minecraft.getInstance().font, 0, 0, 0, 0, CommonComponents.EMPTY);
        this.filterEmpty = filterEmpty;
        this.m_94199_(128);
        this.area = ImmutableRect2i.EMPTY;
        this.background = textures.getSearchBackground();
        this.backgroundBounds = ImmutableRect2i.EMPTY;
        this.m_94182_(false);
    }

    public void updateBounds(ImmutableRect2i area) {
        this.backgroundBounds = area;
        this.m_252865_(area.getX() + 4);
        this.m_253211_(area.getY() + (area.getHeight() - 8) / 2);
        this.f_93618_ = area.getWidth() - 12;
        this.f_93619_ = area.getHeight();
        this.area = area;
    }

    @Override
    public void setValue(String filterText) {
        if (!filterText.equals(this.m_94155_())) {
            super.setValue(filterText);
        }
        int color = this.filterEmpty.getAsBoolean() ? -65536 : -1;
        this.m_94202_(color);
    }

    public Optional<String> getHistory(TextHistory.Direction direction) {
        String currentText = this.m_94155_();
        return history.get(direction, currentText);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.area.contains(mouseX, mouseY);
    }

    public IUserInputHandler createInputHandler() {
        return new TextFieldInputHandler(this);
    }

    @Override
    public void setFocused(boolean keyboardFocus) {
        boolean previousFocus = this.m_93696_();
        super.setFocused(keyboardFocus);
        if (previousFocus != keyboardFocus) {
            Minecraft minecraft = Minecraft.getInstance();
            if (keyboardFocus) {
                Screen screen = minecraft.screen;
                if (screen != null) {
                    if (screen.m_7222_() instanceof AbstractWidget widget) {
                        IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
                        screenHelper.setFocused(widget, false);
                        this.previouslyFocusedWidget = widget;
                    }
                    screen.m_7522_(null);
                }
            } else if (this.previouslyFocusedWidget != null) {
                Screen screen = minecraft.screen;
                if (screen != null) {
                    IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
                    screenHelper.setFocused(this.previouslyFocusedWidget, true);
                    screen.m_7522_(this.previouslyFocusedWidget);
                }
                this.previouslyFocusedWidget = null;
            }
            String text = this.m_94155_();
            history.add(text);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (this.m_94213_()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.background.draw(guiGraphics, this.backgroundBounds);
        }
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
}