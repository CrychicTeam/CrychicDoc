package com.mrcrayfish.configured.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class EditStringScreen extends TooltipScreen implements IBackgroundTexture, IEditing {

    private final Screen parent;

    private final IModConfig config;

    private final ResourceLocation background;

    private final String originalValue;

    private final Function<String, Pair<Boolean, Component>> validator;

    private final Consumer<String> onSave;

    private Button doneButton;

    private EditBox textField;

    private Component validationHint;

    private String value;

    protected EditStringScreen(Screen parent, IModConfig config, ResourceLocation background, Component component, String originalValue, Function<String, Pair<Boolean, Component>> validator, Consumer<String> onSave) {
        super(component);
        this.parent = parent;
        this.config = config;
        this.background = background;
        this.originalValue = originalValue;
        this.validator = validator;
        this.onSave = onSave;
        this.value = this.originalValue;
    }

    @Override
    protected void init() {
        this.textField = new EditBox(this.f_96547_, this.f_96543_ / 2 - 130, this.f_96544_ / 2 - 25, 260, 20, CommonComponents.EMPTY);
        this.textField.setMaxLength(32500);
        this.textField.setValue(this.value);
        this.textField.setResponder(s -> {
            this.value = s;
            this.updateValidation();
        });
        this.textField.setEditable(!this.config.isReadOnly());
        this.m_142416_(this.textField);
        this.doneButton = (Button) this.m_142416_(new IconButton(this.f_96543_ / 2 - 1 - 130, this.f_96544_ / 2 + 13, 0, 44, 128, Component.translatable("configured.gui.apply"), button -> {
            String text = this.textField.getValue();
            if ((Boolean) ((Pair) this.validator.apply(text)).getLeft()) {
                this.onSave.accept(text);
                this.f_96541_.setScreen(this.parent);
            }
        }));
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 + 3, this.f_96544_ / 2 + 13, 128, 20, CommonComponents.GUI_CANCEL, button -> this.f_96541_.setScreen(this.parent)));
        this.updateValidation();
    }

    protected void updateValidation() {
        Pair<Boolean, Component> result = (Pair<Boolean, Component>) this.validator.apply(this.textField.getValue());
        boolean valid = (Boolean) result.getLeft();
        this.doneButton.f_93623_ = !this.config.isReadOnly() && valid;
        this.textField.setTextColor(!valid && !this.textField.getValue().isEmpty() ? -65536 : -1);
        this.validationHint = !valid ? (Component) result.getRight() : null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.resetTooltip();
        this.m_280273_(graphics);
        ConfirmationScreen.drawListBackground(0.0, (double) this.f_96543_, (double) (this.textField.m_252907_() - 10), (double) (this.textField.m_252907_() + 20 + 10));
        this.textField.m_88315_(graphics, mouseX, mouseY, partialTicks);
        graphics.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, this.f_96544_ / 2 - 50, 16777215);
        boolean showValidationHint = this.validationHint != null;
        if (showValidationHint) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.blit(IconButton.ICONS, this.textField.m_252754_() - 20, this.textField.m_252907_() + 3, 16, 16, 11.0F, 11.0F, 11, 11, 64, 64);
            if (ScreenUtil.isMouseWithin(this.textField.m_252754_() - 20, this.textField.m_252907_() + 3, 16, 16, mouseX, mouseY)) {
                this.setActiveTooltip(this.validationHint, -1428357120);
            }
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        this.drawTooltip(graphics, mouseX, mouseY);
    }

    @Override
    public IModConfig getActiveConfig() {
        return this.config;
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.background;
    }
}