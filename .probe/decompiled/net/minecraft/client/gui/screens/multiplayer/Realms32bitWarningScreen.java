package net.minecraft.client.gui.screens.multiplayer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class Realms32bitWarningScreen extends WarningScreen {

    private static final Component TITLE = Component.translatable("title.32bit.deprecation.realms.header").withStyle(ChatFormatting.BOLD);

    private static final Component CONTENT = Component.translatable("title.32bit.deprecation.realms");

    private static final Component CHECK = Component.translatable("title.32bit.deprecation.realms.check");

    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);

    private final Screen previous;

    public Realms32bitWarningScreen(Screen screen0) {
        super(TITLE, CONTENT, CHECK, NARRATION);
        this.previous = screen0;
    }

    @Override
    protected void initButtons(int int0) {
        this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280870_ -> {
            if (this.f_210910_.selected()) {
                this.f_96541_.options.skipRealms32bitWarning = true;
                this.f_96541_.options.save();
            }
            this.f_96541_.setScreen(this.previous);
        }).bounds(this.f_96543_ / 2 - 75, 100 + int0, 150, 20).build());
    }
}