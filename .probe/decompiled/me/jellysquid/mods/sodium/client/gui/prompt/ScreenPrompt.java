package me.jellysquid.mods.sodium.client.gui.prompt;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

@Deprecated(forRemoval = true)
public class ScreenPrompt implements GuiEventListener, Renderable {

    private final ScreenPromptable parent;

    private final List<FormattedText> text;

    private final ScreenPrompt.Action action;

    private FlatButtonWidget closeButton;

    private FlatButtonWidget actionButton;

    private final int width;

    private final int height;

    public ScreenPrompt(ScreenPromptable parent, List<FormattedText> text, int width, int height, ScreenPrompt.Action action) {
        this.parent = parent;
        this.text = text;
        this.width = width;
        this.height = height;
        this.action = action;
    }

    public void init() {
        Dim2i parentDimensions = this.parent.getDimensions();
        int boxX = parentDimensions.width() / 2 - this.width / 2;
        int boxY = parentDimensions.height() / 2 - this.height / 2;
        this.closeButton = new FlatButtonWidget(new Dim2i(boxX + this.width - 84, boxY + this.height - 24, 80, 20), Component.literal("Close"), this::close);
        this.closeButton.setStyle(createButtonStyle());
        this.actionButton = new FlatButtonWidget(new Dim2i(boxX + this.width - 198, boxY + this.height - 24, 110, 20), this.action.label, this::runAction);
        this.actionButton.setStyle(createButtonStyle());
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        PoseStack matrices = drawContext.pose();
        matrices.pushPose();
        matrices.translate(0.0F, 0.0F, 1000.0F);
        Dim2i parentDimensions = this.parent.getDimensions();
        drawContext.fill(0, 0, parentDimensions.width(), parentDimensions.height(), 1879640329);
        matrices.translate(0.0F, 0.0F, 50.0F);
        int boxX = parentDimensions.width() / 2 - this.width / 2;
        int boxY = parentDimensions.height() / 2 - this.height / 2;
        drawContext.fill(boxX, boxY, boxX + this.width, boxY + this.height, -15263977);
        drawContext.renderOutline(boxX, boxY, this.width, this.height, -15592942);
        matrices.translate(0.0F, 0.0F, 50.0F);
        int padding = 5;
        int textX = boxX + padding;
        int textY = boxY + padding;
        int textMaxWidth = this.width - padding * 2;
        int textMaxHeight = this.height - padding * 2;
        Font textRenderer = Minecraft.getInstance().font;
        for (FormattedText paragraph : this.text) {
            for (FormattedCharSequence line : textRenderer.split(paragraph, textMaxWidth)) {
                drawContext.drawString(textRenderer, line, textX, textY, -1, true);
                textY += 9 + 2;
            }
            textY += 8;
        }
        for (AbstractWidget button : this.getWidgets()) {
            button.m_88315_(drawContext, mouseX, mouseY, delta);
        }
        matrices.popPose();
    }

    private static FlatButtonWidget.Style createButtonStyle() {
        FlatButtonWidget.Style style = new FlatButtonWidget.Style();
        style.bgDefault = -13948117;
        style.bgHovered = -13027015;
        style.bgDisabled = style.bgDefault;
        style.textDefault = -1;
        style.textDisabled = style.textDefault;
        return style;
    }

    @NotNull
    public List<AbstractWidget> getWidgets() {
        return List.of(this.actionButton, this.closeButton);
    }

    @Override
    public void setFocused(boolean focused) {
        if (focused) {
            this.parent.setPrompt(this);
        } else {
            this.parent.setPrompt(null);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AbstractWidget widget : this.getWidgets()) {
            if (widget.m_6375_(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.close();
            return true;
        } else {
            return GuiEventListener.super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean isFocused() {
        return this.parent.getPrompt() == this;
    }

    private void close() {
        this.parent.setPrompt(null);
    }

    private void runAction() {
        this.action.runnable.run();
        this.close();
    }

    public static record Action(Component label, Runnable runnable) {
    }
}