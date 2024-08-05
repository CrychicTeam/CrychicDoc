package net.minecraft.client.gui.screens.multiplayer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SafetyScreen extends WarningScreen {

    private static final Component TITLE = Component.translatable("multiplayerWarning.header").withStyle(ChatFormatting.BOLD);

    private static final Component CONTENT = Component.translatable("multiplayerWarning.message");

    private static final Component CHECK = Component.translatable("multiplayerWarning.check");

    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);

    private final Screen previous;

    public SafetyScreen(Screen screen0) {
        super(TITLE, CONTENT, CHECK, NARRATION);
        this.previous = screen0;
    }

    @Override
    protected void initButtons(int int0) {
        this.m_142416_(Button.builder(CommonComponents.GUI_PROCEED, p_280872_ -> {
            if (this.f_210910_.selected()) {
                this.f_96541_.options.skipMultiplayerWarning = true;
                this.f_96541_.options.save();
            }
            this.f_96541_.setScreen(new JoinMultiplayerScreen(this.previous));
        }).bounds(this.f_96543_ / 2 - 155, 100 + int0, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_280871_ -> this.f_96541_.setScreen(this.previous)).bounds(this.f_96543_ / 2 - 155 + 160, 100 + int0, 150, 20).build());
    }
}