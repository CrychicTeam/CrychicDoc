package io.github.lightman314.lightmanscurrency.api.money.input.templates;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.money.input.MoneyInputHandler;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.TextInputUtil;
import io.github.lightman314.lightmanscurrency.client.util.TextRenderUtil;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public abstract class SimpleDisplayInput extends MoneyInputHandler {

    private Component prefix = EasyText.empty();

    private Component postfix = EasyText.empty();

    private EditBox input;

    protected SimpleDisplayInput() {
    }

    protected void setPrefix(@Nonnull String prefix) {
        this.prefix = EasyText.literal(prefix);
    }

    protected void setPrefix(@Nonnull Component prefix) {
        this.prefix = prefix;
    }

    protected void setPostfix(@Nonnull String postfix) {
        this.postfix = EasyText.literal(postfix);
    }

    protected void setPostfix(@Nonnull Component postfix) {
        this.postfix = postfix;
    }

    @Override
    public void initialize(@Nonnull ScreenArea widgetArea) {
        int prefixWidth = this.getFont().width(this.prefix);
        if (prefixWidth > 0) {
            prefixWidth += 2;
        }
        int postfixWidth = this.getFont().width(this.postfix);
        if (postfixWidth > 0) {
            postfixWidth += 2;
        }
        if (prefixWidth + postfixWidth > widgetArea.width + 40) {
            LightmansCurrency.LogError("Prefix & Postfix are too long. Cannot setup display!\nPrefix: " + this.prefix.getString() + "\nPostfix: " + this.postfix.getString());
        } else {
            this.input = this.addChild(new EditBox(this.getFont(), widgetArea.x + 10 + prefixWidth, widgetArea.y + 22, 156 - prefixWidth - postfixWidth, 20, EasyText.empty()));
            this.input.setResponder(this::onValueTextChanges);
            this.input.setMaxLength(this.maxLength());
            this.input.setFilter(TextInputUtil::isPositiveDouble);
            this.onValueChanged(this.currentValue());
        }
    }

    protected int maxLength() {
        return 10;
    }

    @Override
    public void renderTick() {
        if (this.input != null) {
            this.input.f_93624_ = this.isVisible();
            this.input.f_93623_ = !this.isFree() && !this.isLocked();
        }
    }

    protected Component getErrorText() {
        return EasyText.literal("DISPLAY FORMAT TOO LONG");
    }

    @Override
    protected void renderBG(@Nonnull ScreenArea widgetArea, @Nonnull EasyGuiGraphics gui) {
        super.renderBG(widgetArea, gui);
        if (this.input == null) {
            TextRenderUtil.drawCenteredText(gui, this.getErrorText(), widgetArea.width / 2, widgetArea.height / 2 - 10, 16711680);
            Component formatText = EasyText.empty().append(this.prefix).append(EasyText.literal("###")).append(this.postfix);
            TextRenderUtil.drawCenteredText(gui, formatText, widgetArea.width / 2, widgetArea.height / 2, 16711680);
        } else {
            if (this.isFree()) {
                this.input.setValue("");
            }
            if (!this.prefix.getString().isEmpty()) {
                gui.drawShadowed(this.prefix, widgetArea.pos.offset(10, 28), 16777215);
            }
            if (!this.postfix.getString().isEmpty()) {
                int width = gui.font.width(this.postfix);
                gui.drawShadowed(this.postfix, widgetArea.width - 10 - width, 28, 16777215);
            }
        }
    }

    private void onValueTextChanges(@Nonnull String newText) {
        if (!this.isFree()) {
            double valueNumber = TextInputUtil.getDoubleValue(this.input);
            new Thread(() -> {
                MoneyValue newValue = this.getValueFromInput(valueNumber);
                this.changeValue(newValue);
            }).start();
        }
    }

    @Override
    public void onValueChanged(@Nonnull MoneyValue newValue) {
        double valueNumber = 0.0;
        if (newValue.getUniqueName().equals(this.getUniqueName())) {
            valueNumber = this.getTextFromDisplay(newValue);
        }
        String text;
        if (valueNumber % 1.0 == 0.0) {
            text = String.valueOf((long) valueNumber);
        } else {
            text = String.valueOf(valueNumber);
        }
        if (this.input != null) {
            this.input.setValue(text);
        }
    }

    @Nonnull
    protected abstract MoneyValue getValueFromInput(double var1);

    protected abstract double getTextFromDisplay(@Nonnull MoneyValue var1);
}