package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class DisconnectedScreen extends Screen {

    private static final Component TO_SERVER_LIST = Component.translatable("gui.toMenu");

    private static final Component TO_TITLE = Component.translatable("gui.toTitle");

    private final Screen parent;

    private final Component reason;

    private final Component buttonText;

    private final GridLayout layout = new GridLayout();

    public DisconnectedScreen(Screen screen0, Component component1, Component component2) {
        this(screen0, component1, component2, TO_SERVER_LIST);
    }

    public DisconnectedScreen(Screen screen0, Component component1, Component component2, Component component3) {
        super(component1);
        this.parent = screen0;
        this.reason = component2;
        this.buttonText = component3;
    }

    @Override
    protected void init() {
        this.layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
        GridLayout.RowHelper $$0 = this.layout.createRowHelper(1);
        $$0.addChild(new StringWidget(this.f_96539_, this.f_96547_));
        $$0.addChild(new MultiLineTextWidget(this.reason, this.f_96547_).setMaxWidth(this.f_96543_ - 50).setCentered(true));
        Button $$1;
        if (this.f_96541_.allowsMultiplayer()) {
            $$1 = Button.builder(this.buttonText, p_280799_ -> this.f_96541_.setScreen(this.parent)).build();
        } else {
            $$1 = Button.builder(TO_TITLE, p_280800_ -> this.f_96541_.setScreen(new TitleScreen())).build();
        }
        $$0.addChild($$1);
        this.layout.arrangeElements();
        this.layout.m_264134_(this::m_142416_);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        FrameLayout.centerInRectangle(this.layout, this.m_264198_());
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(this.f_96539_, this.reason);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
    }
}