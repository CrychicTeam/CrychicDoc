package net.minecraft.client.gui.screens;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

public class ChatOptionsScreen extends SimpleOptionsSubScreen {

    public ChatOptionsScreen(Screen screen0, Options options1) {
        super(screen0, options1, Component.translatable("options.chat.title"), new OptionInstance[] { options1.chatVisibility(), options1.chatColors(), options1.chatLinks(), options1.chatLinksPrompt(), options1.chatOpacity(), options1.textBackgroundOpacity(), options1.chatScale(), options1.chatLineSpacing(), options1.chatDelay(), options1.chatWidth(), options1.chatHeightFocused(), options1.chatHeightUnfocused(), options1.narrator(), options1.autoSuggestions(), options1.hideMatchedNames(), options1.reducedDebugInfo(), options1.onlyShowSecureChat() });
    }
}