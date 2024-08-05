package net.minecraft.client.gui.screens;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;

public class PopupScreen extends Screen {

    private static final int BUTTON_PADDING = 20;

    private static final int BUTTON_MARGIN = 5;

    private static final int BUTTON_HEIGHT = 20;

    private final Component narrationMessage;

    private final FormattedText message;

    private final ImmutableList<PopupScreen.ButtonOption> buttonOptions;

    private MultiLineLabel messageLines = MultiLineLabel.EMPTY;

    private int contentTop;

    private int buttonWidth;

    protected PopupScreen(Component component0, List<Component> listComponent1, ImmutableList<PopupScreen.ButtonOption> immutableListPopupScreenButtonOption2) {
        super(component0);
        this.message = FormattedText.composite(listComponent1);
        this.narrationMessage = CommonComponents.joinForNarration(component0, ComponentUtils.formatList(listComponent1, CommonComponents.EMPTY));
        this.buttonOptions = immutableListPopupScreenButtonOption2;
    }

    @Override
    public Component getNarrationMessage() {
        return this.narrationMessage;
    }

    @Override
    public void init() {
        UnmodifiableIterator $$1 = this.buttonOptions.iterator();
        while ($$1.hasNext()) {
            PopupScreen.ButtonOption $$0 = (PopupScreen.ButtonOption) $$1.next();
            this.buttonWidth = Math.max(this.buttonWidth, 20 + this.f_96547_.width($$0.message) + 20);
        }
        int $$1x = 5 + this.buttonWidth + 5;
        int $$2 = $$1x * this.buttonOptions.size();
        this.messageLines = MultiLineLabel.create(this.f_96547_, this.message, $$2);
        int $$3 = this.messageLines.getLineCount() * 9;
        this.contentTop = (int) ((double) this.f_96544_ / 2.0 - (double) $$3 / 2.0);
        int $$4 = this.contentTop + $$3 + 9 * 2;
        int $$5 = (int) ((double) this.f_96543_ / 2.0 - (double) $$2 / 2.0);
        for (UnmodifiableIterator var6 = this.buttonOptions.iterator(); var6.hasNext(); $$5 += $$1x) {
            PopupScreen.ButtonOption $$6 = (PopupScreen.ButtonOption) var6.next();
            this.m_142416_(Button.builder($$6.message, $$6.onPress).bounds($$5, $$4, this.buttonWidth, 20).build());
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280039_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, this.contentTop - 9 * 2, -1);
        this.messageLines.renderCentered(guiGraphics0, this.f_96543_ / 2, this.contentTop);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    public static final class ButtonOption {

        final Component message;

        final Button.OnPress onPress;

        public ButtonOption(Component component0, Button.OnPress buttonOnPress1) {
            this.message = component0;
            this.onPress = buttonOnPress1;
        }
    }
}