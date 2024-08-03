package net.zanckor.questapi.api.screen;

import java.io.IOException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AbstractQuestLog extends Screen {

    protected AbstractQuestLog(Component component) {
        super(component);
    }

    public abstract Screen modifyScreen() throws IOException;
}