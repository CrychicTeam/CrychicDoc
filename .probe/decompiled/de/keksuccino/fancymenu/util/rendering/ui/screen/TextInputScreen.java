package de.keksuccino.fancymenu.util.rendering.ui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.fancymenu.util.rendering.ui.widget.editbox.ExtendedEditBox;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextInputScreen extends Screen {

    @NotNull
    protected Consumer<String> callback;

    protected ExtendedEditBox input;

    protected ExtendedButton cancelButton;

    protected ExtendedButton doneButton;

    protected ConsumingSupplier<TextInputScreen, Boolean> textValidator = null;

    protected Tooltip textValidatorFeedbackTooltip = null;

    @NotNull
    public static TextInputScreen build(@NotNull Component title, @Nullable CharacterFilter filter, @NotNull Consumer<String> callback) {
        return new TextInputScreen(title, filter, callback);
    }

    public TextInputScreen(@NotNull Component title, @Nullable CharacterFilter filter, @NotNull Consumer<String> callback) {
        super(title);
        this.callback = callback;
        this.input = new ExtendedEditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.empty());
        this.input.m_94199_(10000);
        this.input.setCharacterFilter(filter);
        UIBase.applyDefaultWidgetSkinTo(this.input);
    }

    @Override
    protected void init() {
        this.m_7787_(this.input);
        this.m_7522_(this.input);
        this.cancelButton = new ExtendedButton(0, 0, 100, 20, Component.translatable("fancymenu.guicomponents.cancel"), button -> this.callback.accept(null));
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
        this.doneButton = new ExtendedButton(0, 0, 100, 20, Component.translatable("fancymenu.guicomponents.done"), button -> {
            if (this.isTextValid()) {
                this.callback.accept(this.input.m_94155_());
            }
        });
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
    }

    @Override
    public void tick() {
        this.input.m_94120_();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        RenderSystem.enableBlend();
        MutableComponent t = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        int titleWidth = Minecraft.getInstance().font.width(t);
        graphics.drawString(this.f_96547_, t, this.f_96543_ / 2 - titleWidth / 2, this.f_96544_ / 2 - 30, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.input.m_252865_(this.f_96543_ / 2 - this.input.m_5711_() / 2);
        this.input.m_253211_(this.f_96544_ / 2 - this.input.m_93694_() / 2);
        this.input.m_88315_(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ / 2 - 5 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.f_96544_ - 40);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        this.doneButton.f_93623_ = this.isTextValid();
        if (this.textValidatorFeedbackTooltip != null) {
            this.textValidatorFeedbackTooltip.setDefaultStyle();
        }
        this.doneButton.setTooltip(this.textValidatorFeedbackTooltip);
        this.doneButton.m_252865_(this.f_96543_ / 2 + 5);
        this.doneButton.m_253211_(this.f_96544_ - 40);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
    }

    @Override
    public boolean keyPressed(int button, int int0, int int1) {
        if (button == 257 && this.isTextValid()) {
            this.callback.accept(this.input.m_94155_());
            return true;
        } else {
            return super.keyPressed(button, int0, int1);
        }
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    public TextInputScreen setText(@Nullable String text) {
        if (text == null) {
            text = "";
        }
        this.input.setValue(text);
        return this;
    }

    public String getText() {
        return this.input.m_94155_();
    }

    protected boolean isTextValid() {
        return this.textValidator != null ? this.textValidator.get(this) : true;
    }

    public TextInputScreen setTextValidator(@Nullable ConsumingSupplier<TextInputScreen, Boolean> textValidator) {
        this.textValidator = textValidator;
        return this;
    }

    public TextInputScreen setTextValidatorUserFeedback(@Nullable Tooltip feedback) {
        this.textValidatorFeedbackTooltip = feedback;
        return this;
    }
}