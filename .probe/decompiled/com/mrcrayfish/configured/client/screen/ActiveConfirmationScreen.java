package com.mrcrayfish.configured.client.screen;

import com.mrcrayfish.configured.api.IModConfig;
import java.util.function.Function;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ActiveConfirmationScreen extends ConfirmationScreen implements IEditing {

    private final IModConfig config;

    public ActiveConfirmationScreen(Screen parent, IModConfig config, Component message, ConfirmationScreen.Icon icon, Function<Boolean, Boolean> handler) {
        super(parent, message, icon, handler);
        this.config = config;
    }

    @Override
    public IModConfig getActiveConfig() {
        return this.config;
    }
}